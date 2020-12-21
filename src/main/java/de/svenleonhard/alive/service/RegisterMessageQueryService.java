package de.svenleonhard.alive.service;

import de.svenleonhard.alive.domain.*; // for static metamodels
import de.svenleonhard.alive.domain.RegisterMessage;
import de.svenleonhard.alive.repository.RegisterMessageRepository;
import de.svenleonhard.alive.service.dto.RegisterMessageCriteria;
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
 * Service for executing complex queries for {@link RegisterMessage} entities in the database.
 * The main input is a {@link RegisterMessageCriteria} which gets converted to {@link Specification},
 * in a way that all the filters must apply.
 * It returns a {@link List} of {@link RegisterMessage} or a {@link Page} of {@link RegisterMessage} which fulfills the criteria.
 */
@Service
@Transactional(readOnly = true)
public class RegisterMessageQueryService extends QueryService<RegisterMessage> {
    private final Logger log = LoggerFactory.getLogger(RegisterMessageQueryService.class);

    private final RegisterMessageRepository registerMessageRepository;
    private final UserService userService;

    public RegisterMessageQueryService(RegisterMessageRepository registerMessageRepository, UserService userService) {
        this.registerMessageRepository = registerMessageRepository;
        this.userService = userService;
    }

    /**
     * Return a {@link List} of {@link RegisterMessage} which matches the criteria from the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the matching entities.
     */
    @Transactional(readOnly = true)
    public List<RegisterMessage> findByCriteria(RegisterMessageCriteria criteria) {
        log.debug("find by criteria : {}", criteria);
        criteria.setUserId(userService.makeUserIdLongFilter());
        final Specification<RegisterMessage> specification = createSpecification(criteria);
        return registerMessageRepository.findAll(specification);
    }

    /**
     * Return a {@link Page} of {@link RegisterMessage} which matches the criteria from the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @param page The page, which should be returned.
     * @return the matching entities.
     */
    @Transactional(readOnly = true)
    public Page<RegisterMessage> findByCriteria(RegisterMessageCriteria criteria, Pageable page) {
        log.debug("find by criteria : {}, page: {}", criteria, page);
        criteria.setUserId(userService.makeUserIdLongFilter());
        final Specification<RegisterMessage> specification = createSpecification(criteria);
        return registerMessageRepository.findAll(specification, page);
    }

    /**
     * Return the number of matching entities in the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the number of matching entities.
     */
    @Transactional(readOnly = true)
    public long countByCriteria(RegisterMessageCriteria criteria) {
        log.debug("count by criteria : {}", criteria);
        criteria.setUserId(userService.makeUserIdLongFilter());
        final Specification<RegisterMessage> specification = createSpecification(criteria);
        return registerMessageRepository.count(specification);
    }

    /**
     * Function to convert {@link RegisterMessageCriteria} to a {@link Specification}
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the matching {@link Specification} of the entity.
     */
    protected Specification<RegisterMessage> createSpecification(RegisterMessageCriteria criteria) {
        Specification<RegisterMessage> specification = Specification.where(null);
        if (criteria != null) {
            if (criteria.getId() != null) {
                specification = specification.and(buildRangeSpecification(criteria.getId(), RegisterMessage_.id));
            }
            if (criteria.getSendtime() != null) {
                specification = specification.and(buildRangeSpecification(criteria.getSendtime(), RegisterMessage_.sendtime));
            }
            if (criteria.getReceivetime() != null) {
                specification = specification.and(buildRangeSpecification(criteria.getReceivetime(), RegisterMessage_.receivetime));
            }
            if (criteria.getRetrycount() != null) {
                specification = specification.and(buildRangeSpecification(criteria.getRetrycount(), RegisterMessage_.retrycount));
            }
            if (criteria.getUserId() != null) {
                specification =
                    specification.and(
                        buildSpecification(criteria.getUserId(), root -> root.join(RegisterMessage_.user, JoinType.LEFT).get(User_.id))
                    );
            }
        }
        return specification;
    }
}
