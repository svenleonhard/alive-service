package de.svenleonhard.alive.service;

import de.svenleonhard.alive.domain.AliveMessage;
import de.svenleonhard.alive.domain.DeviceNotAlive;
import de.svenleonhard.alive.domain.User;
import de.svenleonhard.alive.service.dto.AliveMessageCriteria;
import de.svenleonhard.alive.service.dto.DeviceNotAliveCriteria;
import de.svenleonhard.alive.service.impl.DeviceNotAliveServiceImpl;
import de.svenleonhard.alive.service.impl.RegisterMessageServiceImpl;
import io.github.jhipster.service.filter.BooleanFilter;
import io.github.jhipster.service.filter.LongFilter;
import java.time.ZonedDateTime;
import java.util.Comparator;
import java.util.List;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

@Component
public class IsAliveScheduledTask {
    private final Logger log = LoggerFactory.getLogger(RegisterMessageServiceImpl.class);

    private final ObserveQueryService observeQueryService;
    private final AliveMessageQueryService aliveMessageQueryService;
    private final DeviceNotAliveServiceImpl deviceNotAliveService;
    private final DeviceNotAliveQueryService deviceNotAliveQueryService;
    private final MailService mailService;

    private final int INTERVAL = 10;

    public IsAliveScheduledTask(
        ObserveQueryService observeQueryService,
        AliveMessageQueryService aliveMessageQueryService,
        DeviceNotAliveServiceImpl deviceNotAliveService,
        DeviceNotAliveQueryService deviceNotAliveQueryService,
        MailService mailService
    ) {
        this.observeQueryService = observeQueryService;
        this.aliveMessageQueryService = aliveMessageQueryService;
        this.deviceNotAliveService = deviceNotAliveService;
        this.deviceNotAliveQueryService = deviceNotAliveQueryService;
        this.mailService = mailService;
    }

    @Scheduled(cron = "10 * * * * * ")
    public void checkIsAlive() {
        log.info("Check is alive");
        observeQueryService
            .findByCriteria(null)
            .forEach(
                observe -> {
                    LongFilter longFilter = new LongFilter();
                    longFilter.setEquals(observe.getUser().getId());
                    AliveMessageCriteria aliveMessageCriteria = new AliveMessageCriteria();
                    aliveMessageCriteria.setUserId(longFilter);
                    List<AliveMessage> aliveMessageList = aliveMessageQueryService.findByCriteria(aliveMessageCriteria);
                    aliveMessageList.sort(Comparator.comparing(AliveMessage::getReceivetime).reversed());
                    if (aliveMessageList.stream().findFirst().isPresent()) {
                        if (
                            !aliveMessageList.stream().findFirst().get().getReceivetime().plusMinutes(INTERVAL).isAfter(ZonedDateTime.now())
                        ) {
                            if (!alreadyDetected(observe.getUser())) {
                                createDeviceNotAliveFor(observe.getUser());
                                mailService.sendDeviceNotAliveMail(observe.getUser());
                            }
                        }
                    }
                }
            );
    }

    public boolean alreadyDetected(User user) {
        BooleanFilter booleanFilter = new BooleanFilter();
        booleanFilter.setEquals(false);
        DeviceNotAliveCriteria deviceNotAliveCriteria = new DeviceNotAliveCriteria();
        deviceNotAliveCriteria.setConfirmed(booleanFilter);
        LongFilter longFilter = new LongFilter();
        longFilter.setEquals(user.getId());
        deviceNotAliveCriteria.setUserId(longFilter);
        List<DeviceNotAlive> deviceNotAliveList = deviceNotAliveQueryService.findByCriteria(deviceNotAliveCriteria);
        return deviceNotAliveList.size() > 1;
    }

    public void createDeviceNotAliveFor(User user) {
        log.error(user.getFirstName() + " is not alive");
        DeviceNotAlive deviceNotAlive = new DeviceNotAlive();
        deviceNotAlive.setOccured(ZonedDateTime.now());
        deviceNotAlive.setUser(user);
        deviceNotAlive.setConfirmed(false);
        deviceNotAliveService.save(deviceNotAlive);
    }
}
