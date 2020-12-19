package de.svenleonhard.alive.service.impl;

import de.svenleonhard.alive.domain.AliveMessage;
import de.svenleonhard.alive.repository.AliveMessageRepository;
import de.svenleonhard.alive.service.AliveMessageService;
import de.svenleonhard.alive.service.UserService;
import java.time.ZonedDateTime;
import java.util.Optional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * Service Implementation for managing {@link AliveMessage}.
 */
@Service
@Transactional
public class AliveMessageServiceImpl implements AliveMessageService {
    private final Logger log = LoggerFactory.getLogger(AliveMessageServiceImpl.class);

    private final AliveMessageRepository aliveMessageRepository;
    private final UserService userService;

    public AliveMessageServiceImpl(AliveMessageRepository aliveMessageRepository, UserService userService) {
        this.aliveMessageRepository = aliveMessageRepository;
        this.userService = userService;
    }

    @Override
    public AliveMessage save(AliveMessage aliveMessage) {
        log.debug("Request to save AliveMessage : {}", aliveMessage);
        aliveMessage.setUser(userService.getUserWithAuthorities().orElseThrow(() -> new IllegalStateException("User dose not exist")));
        aliveMessage.setReceivetime(ZonedDateTime.now());
        return aliveMessageRepository.save(aliveMessage);
    }

    @Override
    @Transactional(readOnly = true)
    public Page<AliveMessage> findAll(Pageable pageable) {
        log.debug("Request to get all AliveMessages");
        return aliveMessageRepository.findAll(pageable);
    }

    @Override
    @Transactional(readOnly = true)
    public Optional<AliveMessage> findOne(Long id) {
        log.debug("Request to get AliveMessage : {}", id);
        return aliveMessageRepository.findById(id);
    }

    @Override
    public void delete(Long id) {
        log.debug("Request to delete AliveMessage : {}", id);
        aliveMessageRepository.deleteById(id);
    }
}
