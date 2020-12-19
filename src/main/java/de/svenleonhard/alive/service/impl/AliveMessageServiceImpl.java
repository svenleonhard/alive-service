package de.svenleonhard.alive.service.impl;

import de.svenleonhard.alive.domain.AliveMessage;
import de.svenleonhard.alive.repository.AliveMessageRepository;
import de.svenleonhard.alive.service.AliveMessageService;
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

    public AliveMessageServiceImpl(AliveMessageRepository aliveMessageRepository) {
        this.aliveMessageRepository = aliveMessageRepository;
    }

    @Override
    public AliveMessage save(AliveMessage aliveMessage) {
        log.debug("Request to save AliveMessage : {}", aliveMessage);
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
