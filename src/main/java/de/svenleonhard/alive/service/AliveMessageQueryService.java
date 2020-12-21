package de.svenleonhard.alive.service;

import de.svenleonhard.alive.domain.*; // for static metamodels
import de.svenleonhard.alive.domain.AliveMessage;
import de.svenleonhard.alive.repository.AliveMessageRepository;
import de.svenleonhard.alive.service.dto.AliveMessageCriteria;
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
 * Service for executing complex queries for {@link AliveMessage} entities in the database.
 * The main input is a {@link AliveMessageCriteria} which gets converted to {@link Specification},
 * in a way that all the filters must apply.
 * It returns a {@link List} of {@link AliveMessage} or a {@link Page} of {@link AliveMessage} which fulfills the criteria.
 */
@Service
@Transactional(readOnly = true)
public class AliveMessageQueryService extends QueryService<AliveMessage> {
    private final Logger log = LoggerFactory.getLogger(AliveMessageQueryService.class);

    private final AliveMessageRepository aliveMessageRepository;
    private final UserService userService;

    public AliveMessageQueryService(AliveMessageRepository aliveMessageRepository, UserService userService) {
        this.aliveMessageRepository = aliveMessageRepository;
        this.userService = userService;
    }

    /**
     * Return a {@link List} of {@link AliveMessage} which matches the criteria from the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the matching entities.
     */
    @Transactional(readOnly = true)
    public List<AliveMessage> findByCriteria(AliveMessageCriteria criteria) {
        log.debug("find by criteria : {}", criteria);
        criteria.setUserId(userService.makeUserIdLongFilter());
        final Specification<AliveMessage> specification = createSpecification(criteria);
        return aliveMessageRepository.findAll(specification);
    }

    /**
     * Return a {@link Page} of {@link AliveMessage} which matches the criteria from the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @param page The page, which should be returned.
     * @return the matching entities.
     */
    @Transactional(readOnly = true)
    public Page<AliveMessage> findByCriteria(AliveMessageCriteria criteria, Pageable page) {
        log.debug("find by criteria : {}, page: {}", criteria, page);
        criteria.setUserId(userService.makeUserIdLongFilter());
        final Specification<AliveMessage> specification = createSpecification(criteria);
        return aliveMessageRepository.findAll(specification, page);
    }

    /**
     * Return the number of matching entities in the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the number of matching entities.
     */
    @Transactional(readOnly = true)
    public long countByCriteria(AliveMessageCriteria criteria) {
        log.debug("count by criteria : {}", criteria);
        criteria.setUserId(userService.makeUserIdLongFilter());
        final Specification<AliveMessage> specification = createSpecification(criteria);
        return aliveMessageRepository.count(specification);
    }

    /**
     * Function to convert {@link AliveMessageCriteria} to a {@link Specification}
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the matching {@link Specification} of the entity.
     */
    protected Specification<AliveMessage> createSpecification(AliveMessageCriteria criteria) {
        Specification<AliveMessage> specification = Specification.where(null);
        if (criteria != null) {
            if (criteria.getId() != null) {
                specification = specification.and(buildRangeSpecification(criteria.getId(), AliveMessage_.id));
            }
            if (criteria.getSendtime() != null) {
                specification = specification.and(buildRangeSpecification(criteria.getSendtime(), AliveMessage_.sendtime));
            }
            if (criteria.getReceivetime() != null) {
                specification = specification.and(buildRangeSpecification(criteria.getReceivetime(), AliveMessage_.receivetime));
            }
            if (criteria.getRetrycount() != null) {
                specification = specification.and(buildRangeSpecification(criteria.getRetrycount(), AliveMessage_.retrycount));
            }
            if (criteria.getUserId() != null) {
                specification =
                    specification.and(
                        buildSpecification(criteria.getUserId(), root -> root.join(AliveMessage_.user, JoinType.LEFT).get(User_.id))
                    );
            }
        }
        return specification;
    }
}
