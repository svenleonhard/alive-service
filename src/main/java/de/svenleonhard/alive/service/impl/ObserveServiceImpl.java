package de.svenleonhard.alive.service.impl;

import de.svenleonhard.alive.domain.Observe;
import de.svenleonhard.alive.repository.ObserveRepository;
import de.svenleonhard.alive.service.ObserveService;
import java.util.Optional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * Service Implementation for managing {@link Observe}.
 */
@Service
@Transactional
public class ObserveServiceImpl implements ObserveService {
    private final Logger log = LoggerFactory.getLogger(ObserveServiceImpl.class);

    private final ObserveRepository observeRepository;

    public ObserveServiceImpl(ObserveRepository observeRepository) {
        this.observeRepository = observeRepository;
    }

    @Override
    public Observe save(Observe observe) {
        log.debug("Request to save Observe : {}", observe);
        return observeRepository.save(observe);
    }

    @Override
    @Transactional(readOnly = true)
    public Page<Observe> findAll(Pageable pageable) {
        log.debug("Request to get all Observes");
        return observeRepository.findAll(pageable);
    }

    @Override
    @Transactional(readOnly = true)
    public Optional<Observe> findOne(Long id) {
        log.debug("Request to get Observe : {}", id);
        return observeRepository.findById(id);
    }

    @Override
    public void delete(Long id) {
        log.debug("Request to delete Observe : {}", id);
        observeRepository.deleteById(id);
    }
}
