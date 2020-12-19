package de.svenleonhard.alive.service;

import de.svenleonhard.alive.domain.RegisterMessage;
import java.util.Optional;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

/**
 * Service Interface for managing {@link RegisterMessage}.
 */
public interface RegisterMessageService {
    /**
     * Save a registerMessage.
     *
     * @param registerMessage the entity to save.
     * @return the persisted entity.
     */
    RegisterMessage save(RegisterMessage registerMessage);

    /**
     * Get all the registerMessages.
     *
     * @param pageable the pagination information.
     * @return the list of entities.
     */
    Page<RegisterMessage> findAll(Pageable pageable);

    /**
     * Get the "id" registerMessage.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    Optional<RegisterMessage> findOne(Long id);

    /**
     * Delete the "id" registerMessage.
     *
     * @param id the id of the entity.
     */
    void delete(Long id);
}
