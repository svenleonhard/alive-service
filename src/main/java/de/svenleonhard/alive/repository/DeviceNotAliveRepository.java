package de.svenleonhard.alive.repository;

import de.svenleonhard.alive.domain.DeviceNotAlive;
import java.util.List;
import org.springframework.data.jpa.repository.*;
import org.springframework.stereotype.Repository;

/**
 * Spring Data  repository for the DeviceNotAlive entity.
 */
@SuppressWarnings("unused")
@Repository
public interface DeviceNotAliveRepository extends JpaRepository<DeviceNotAlive, Long>, JpaSpecificationExecutor<DeviceNotAlive> {
    @Query("select deviceNotAlive from DeviceNotAlive deviceNotAlive where deviceNotAlive.user.login = ?#{principal.username}")
    List<DeviceNotAlive> findByUserIsCurrentUser();
}
