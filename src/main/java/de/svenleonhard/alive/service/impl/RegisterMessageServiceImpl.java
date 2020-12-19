package de.svenleonhard.alive.service.impl;

import de.svenleonhard.alive.domain.RegisterMessage;
import de.svenleonhard.alive.repository.RegisterMessageRepository;
import de.svenleonhard.alive.service.RegisterMessageService;
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

    public RegisterMessageServiceImpl(RegisterMessageRepository registerMessageRepository) {
        this.registerMessageRepository = registerMessageRepository;
    }

    @Override
    public RegisterMessage save(RegisterMessage registerMessage) {
        log.debug("Request to save RegisterMessage : {}", registerMessage);
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
