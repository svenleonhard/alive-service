package de.svenleonhard.alive.service;

import de.svenleonhard.alive.domain.*; // for static metamodels
import de.svenleonhard.alive.domain.DeviceNotAlive;
import de.svenleonhard.alive.repository.DeviceNotAliveRepository;
import de.svenleonhard.alive.service.dto.DeviceNotAliveCriteria;
import io.github.jhipster.service.QueryService;
import java.util.List;
import javax.persistence.criteria.JoinType;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * Service for executing complex queries for {@link DeviceNotAlive} entities in the database.
 * The main input is a {@link DeviceNotAliveCriteria} which gets converted to {@link Specification},
 * in a way that all the filters must apply.
 * It returns a {@link List} of {@link DeviceNotAlive} or a {@link Page} of {@link DeviceNotAlive} which fulfills the criteria.
 */
@Service
@Transactional(readOnly = true)
public class DeviceNotAliveQueryService extends QueryService<DeviceNotAlive> {
    private final Logger log = LoggerFactory.getLogger(DeviceNotAliveQueryService.class);

    private final DeviceNotAliveRepository deviceNotAliveRepository;
    private final UserService userService;

    public DeviceNotAliveQueryService(DeviceNotAliveRepository deviceNotAliveRepository, UserService userService) {
        this.deviceNotAliveRepository = deviceNotAliveRepository;
        this.userService = userService;
    }

    /**
     * Return a {@link List} of {@link DeviceNotAlive} which matches the criteria from the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the matching entities.
     */
    @Transactional(readOnly = true)
    public List<DeviceNotAlive> findByCriteria(DeviceNotAliveCriteria criteria) {
        log.debug("find by criteria : {}", criteria);
        criteria.setUserId(userService.makeUserIdLongFilter());
        final Specification<DeviceNotAlive> specification = createSpecification(criteria);
        return deviceNotAliveRepository.findAll(specification);
    }

    /**
     * Return a {@link Page} of {@link DeviceNotAlive} which matches the criteria from the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @param page The page, which should be returned.
     * @return the matching entities.
     */
    @Transactional(readOnly = true)
    public Page<DeviceNotAlive> findByCriteria(DeviceNotAliveCriteria criteria, Pageable page) {
        log.debug("find by criteria : {}, page: {}", criteria, page);
        criteria.setUserId(userService.makeUserIdLongFilter());
        final Specification<DeviceNotAlive> specification = createSpecification(criteria);
        return deviceNotAliveRepository.findAll(specification, page);
    }

    /**
     * Return the number of matching entities in the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the number of matching entities.
     */
    @Transactional(readOnly = true)
    public long countByCriteria(DeviceNotAliveCriteria criteria) {
        log.debug("count by criteria : {}", criteria);
        criteria.setUserId(userService.makeUserIdLongFilter());
        final Specification<DeviceNotAlive> specification = createSpecification(criteria);
        return deviceNotAliveRepository.count(specification);
    }

    /**
     * Function to convert {@link DeviceNotAliveCriteria} to a {@link Specification}
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the matching {@link Specification} of the entity.
     */
    protected Specification<DeviceNotAlive> createSpecification(DeviceNotAliveCriteria criteria) {
        Specification<DeviceNotAlive> specification = Specification.where(null);
        if (criteria != null) {
            if (criteria.getId() != null) {
                specification = specification.and(buildRangeSpecification(criteria.getId(), DeviceNotAlive_.id));
            }
            if (criteria.getOccured() != null) {
                specification = specification.and(buildRangeSpecification(criteria.getOccured(), DeviceNotAlive_.occured));
            }
            if (criteria.getConfirmed() != null) {
                specification = specification.and(buildSpecification(criteria.getConfirmed(), DeviceNotAlive_.confirmed));
            }
            if (criteria.getUserId() != null) {
                specification =
                    specification.and(
                        buildSpecification(criteria.getUserId(), root -> root.join(DeviceNotAlive_.user, JoinType.LEFT).get(User_.id))
                    );
            }
        }
        return specification;
    }
}
