package de.svenleonhard.alive.repository;

import de.svenleonhard.alive.domain.AliveMessage;
import java.util.List;
import org.springframework.data.jpa.repository.*;
import org.springframework.stereotype.Repository;

/**
 * Spring Data  repository for the AliveMessage entity.
 */
@SuppressWarnings("unused")
@Repository
public interface AliveMessageRepository extends JpaRepository<AliveMessage, Long>, JpaSpecificationExecutor<AliveMessage> {
    @Query("select aliveMessage from AliveMessage aliveMessage where aliveMessage.user.login = ?#{principal.username}")
    List<AliveMessage> findByUserIsCurrentUser();
}
