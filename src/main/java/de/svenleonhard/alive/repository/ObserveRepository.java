package de.svenleonhard.alive.repository;

import de.svenleonhard.alive.domain.Observe;
import java.util.List;
import org.springframework.data.jpa.repository.*;
import org.springframework.stereotype.Repository;

/**
 * Spring Data  repository for the Observe entity.
 */
@SuppressWarnings("unused")
@Repository
public interface ObserveRepository extends JpaRepository<Observe, Long>, JpaSpecificationExecutor<Observe> {
    @Query("select observe from Observe observe where observe.user.login = ?#{principal.username}")
    List<Observe> findByUserIsCurrentUser();
}
