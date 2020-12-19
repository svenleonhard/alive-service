package de.svenleonhard.alive.repository;

import de.svenleonhard.alive.domain.RegisterMessage;
import java.util.List;
import org.springframework.data.jpa.repository.*;
import org.springframework.stereotype.Repository;

/**
 * Spring Data  repository for the RegisterMessage entity.
 */
@SuppressWarnings("unused")
@Repository
public interface RegisterMessageRepository extends JpaRepository<RegisterMessage, Long>, JpaSpecificationExecutor<RegisterMessage> {
    @Query("select registerMessage from RegisterMessage registerMessage where registerMessage.user.login = ?#{principal.username}")
    List<RegisterMessage> findByUserIsCurrentUser();
}
