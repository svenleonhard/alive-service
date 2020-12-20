package de.svenleonhard.alive.service;

import de.svenleonhard.alive.domain.DeviceNotAlive;
import java.util.Optional;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

/**
 * Service Interface for managing {@link DeviceNotAlive}.
 */
public interface DeviceNotAliveService {
    /**
     * Save a deviceNotAlive.
     *
     * @param deviceNotAlive the entity to save.
     * @return the persisted entity.
     */
    DeviceNotAlive save(DeviceNotAlive deviceNotAlive);

    /**
     * Get all the deviceNotAlives.
     *
     * @param pageable the pagination information.
     * @return the list of entities.
     */
    Page<DeviceNotAlive> findAll(Pageable pageable);

    /**
     * Get the "id" deviceNotAlive.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    Optional<DeviceNotAlive> findOne(Long id);

    /**
     * Delete the "id" deviceNotAlive.
     *
     * @param id the id of the entity.
     */
    void delete(Long id);
}
