package de.svenleonhard.alive.service;

import de.svenleonhard.alive.domain.Observe;
import java.util.Optional;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

/**
 * Service Interface for managing {@link Observe}.
 */
public interface ObserveService {
    /**
     * Save a observe.
     *
     * @param observe the entity to save.
     * @return the persisted entity.
     */
    Observe save(Observe observe);

    /**
     * Get all the observes.
     *
     * @param pageable the pagination information.
     * @return the list of entities.
     */
    Page<Observe> findAll(Pageable pageable);

    /**
     * Get the "id" observe.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    Optional<Observe> findOne(Long id);

    /**
     * Delete the "id" observe.
     *
     * @param id the id of the entity.
     */
    void delete(Long id);
}
