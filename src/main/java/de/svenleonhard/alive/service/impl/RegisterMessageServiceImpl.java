package de.svenleonhard.alive.service.impl;

import de.svenleonhard.alive.domain.Observe;
import de.svenleonhard.alive.domain.RegisterMessage;
import de.svenleonhard.alive.repository.RegisterMessageRepository;
import de.svenleonhard.alive.service.ObserveQueryService;
import de.svenleonhard.alive.service.RegisterMessageService;
import de.svenleonhard.alive.service.UserService;
import de.svenleonhard.alive.service.dto.ObserveCriteria;
import io.github.jhipster.service.filter.LongFilter;
import java.time.LocalDate;
import java.time.ZonedDateTime;
import java.util.Optional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * Service Implementation for managing {@link RegisterMessage}.
 */
@Service
@Transactional
public class RegisterMessageServiceImpl implements RegisterMessageService {
    private final Logger log = LoggerFactory.getLogger(RegisterMessageServiceImpl.class);

    private final RegisterMessageRepository registerMessageRepository;
    private final UserService userService;
    private final ObserveQueryService observeQueryService;
    private final ObserveServiceImpl observeService;

    public RegisterMessageServiceImpl(
        RegisterMessageRepository registerMessageRepository,
        UserService userService,
        ObserveQueryService observeQueryService,
        ObserveServiceImpl observeService
    ) {
        this.registerMessageRepository = registerMessageRepository;
        this.userService = userService;
        this.observeQueryService = observeQueryService;
        this.observeService = observeService;
    }

    @Override
    public RegisterMessage save(RegisterMessage registerMessage) {
        log.debug("Request to save RegisterMessage : {}", registerMessage);
        registerMessage.setUser(userService.getUserWithAuthorities().orElseThrow(() -> new IllegalStateException("User dose not exist")));
        registerMessage.setReceivetime(ZonedDateTime.now());
        LongFilter longFilter = new LongFilter();
        longFilter.setEquals(userService.getUserWithAuthorities().get().getId());
        ObserveCriteria observeCriteria = new ObserveCriteria();
        observeCriteria.setUserId(longFilter);
        if (!observeQueryService.findByCriteria(observeCriteria).stream().findFirst().isPresent()) {
            Observe observe = new Observe();
            observe.setUser(userService.getUserWithAuthorities().orElseThrow(() -> new IllegalStateException("User dose not exist")));
            observe.setDescription("Generic Description");
            observe.setStartdate(LocalDate.now());
            observeService.save(observe);
        }
        return registerMessageRepository.save(registerMessage);
    }

    @Override
    @Transactional(readOnly = true)
    public Page<RegisterMessage> findAll(Pageable pageable) {
        log.debug("Request to get all RegisterMessages");
        return registerMessageRepository.findAll(pageable);
    }

    @Override
    @Transactional(readOnly = true)
    public Optional<RegisterMessage> findOne(Long id) {
        log.debug("Request to get RegisterMessage : {}", id);
        return registerMessageRepository.findById(id);
    }

    @Override
    public void delete(Long id) {
        log.debug("Request to delete RegisterMessage : {}", id);
        registerMessageRepository.deleteById(id);
    }
}
