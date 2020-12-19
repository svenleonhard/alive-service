package de.svenleonhard.alive.service;

import de.svenleonhard.alive.domain.AliveMessage;
import java.util.Optional;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

/**
 * Service Interface for managing {@link AliveMessage}.
 */
public interface AliveMessageService {
    /**
     * Save a aliveMessage.
     *
     * @param aliveMessage the entity to save.
     * @return the persisted entity.
     */
    AliveMessage save(AliveMessage aliveMessage);

    /**
     * Get all the aliveMessages.
     *
     * @param pageable the pagination information.
     * @return the list of entities.
     */
    Page<AliveMessage> findAll(Pageable pageable);

    /**
     * Get the "id" aliveMessage.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    Optional<AliveMessage> findOne(Long id);

    /**
     * Delete the "id" aliveMessage.
     *
     * @param id the id of the entity.
     */
    void delete(Long id);
}
