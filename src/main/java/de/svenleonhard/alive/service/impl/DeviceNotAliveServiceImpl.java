package de.svenleonhard.alive.service.impl;

import de.svenleonhard.alive.domain.DeviceNotAlive;
import de.svenleonhard.alive.repository.DeviceNotAliveRepository;
import de.svenleonhard.alive.service.DeviceNotAliveService;
import java.util.Optional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * Service Implementation for managing {@link DeviceNotAlive}.
 */
@Service
@Transactional
public class DeviceNotAliveServiceImpl implements DeviceNotAliveService {
    private final Logger log = LoggerFactory.getLogger(DeviceNotAliveServiceImpl.class);

    private final DeviceNotAliveRepository deviceNotAliveRepository;

    public DeviceNotAliveServiceImpl(DeviceNotAliveRepository deviceNotAliveRepository) {
        this.deviceNotAliveRepository = deviceNotAliveRepository;
    }

    @Override
    public DeviceNotAlive save(DeviceNotAlive deviceNotAlive) {
        log.debug("Request to save DeviceNotAlive : {}", deviceNotAlive);
        return deviceNotAliveRepository.save(deviceNotAlive);
    }

    @Override
    @Transactional(readOnly = true)
    public Page<DeviceNotAlive> findAll(Pageable pageable) {
        log.debug("Request to get all DeviceNotAlives");
        return deviceNotAliveRepository.findAll(pageable);
    }

    @Override
    @Transactional(readOnly = true)
    public Optional<DeviceNotAlive> findOne(Long id) {
        log.debug("Request to get DeviceNotAlive : {}", id);
        return deviceNotAliveRepository.findById(id);
    }

    @Override
    public void delete(Long id) {
        log.debug("Request to delete DeviceNotAlive : {}", id);
        deviceNotAliveRepository.deleteById(id);
    }
}
