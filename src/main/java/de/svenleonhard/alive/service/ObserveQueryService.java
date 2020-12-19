package de.svenleonhard.alive.service;

import de.svenleonhard.alive.domain.*; // for static metamodels
import de.svenleonhard.alive.domain.Observe;
import de.svenleonhard.alive.repository.ObserveRepository;
import de.svenleonhard.alive.service.dto.ObserveCriteria;
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
 * Service for executing complex queries for {@link Observe} entities in the database.
 * The main input is a {@link ObserveCriteria} which gets converted to {@link Specification},
 * in a way that all the filters must apply.
 * It returns a {@link List} of {@link Observe} or a {@link Page} of {@link Observe} which fulfills the criteria.
 */
@Service
@Transactional(readOnly = true)
public class ObserveQueryService extends QueryService<Observe> {
    private final Logger log = LoggerFactory.getLogger(ObserveQueryService.class);

    private final ObserveRepository observeRepository;

    public ObserveQueryService(ObserveRepository observeRepository) {
        this.observeRepository = observeRepository;
    }

    /**
     * Return a {@link List} of {@link Observe} which matches the criteria from the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the matching entities.
     */
    @Transactional(readOnly = true)
    public List<Observe> findByCriteria(ObserveCriteria criteria) {
        log.debug("find by criteria : {}", criteria);
        final Specification<Observe> specification = createSpecification(criteria);
        return observeRepository.findAll(specification);
    }

    /**
     * Return a {@link Page} of {@link Observe} which matches the criteria from the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @param page The page, which should be returned.
     * @return the matching entities.
     */
    @Transactional(readOnly = true)
    public Page<Observe> findByCriteria(ObserveCriteria criteria, Pageable page) {
        log.debug("find by criteria : {}, page: {}", criteria, page);
        final Specification<Observe> specification = createSpecification(criteria);
        return observeRepository.findAll(specification, page);
    }

    /**
     * Return the number of matching entities in the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the number of matching entities.
     */
    @Transactional(readOnly = true)
    public long countByCriteria(ObserveCriteria criteria) {
        log.debug("count by criteria : {}", criteria);
        final Specification<Observe> specification = createSpecification(criteria);
        return observeRepository.count(specification);
    }

    /**
     * Function to convert {@link ObserveCriteria} to a {@link Specification}
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the matching {@link Specification} of the entity.
     */
    protected Specification<Observe> createSpecification(ObserveCriteria criteria) {
        Specification<Observe> specification = Specification.where(null);
        if (criteria != null) {
            if (criteria.getId() != null) {
                specification = specification.and(buildRangeSpecification(criteria.getId(), Observe_.id));
            }
            if (criteria.getDescription() != null) {
                specification = specification.and(buildStringSpecification(criteria.getDescription(), Observe_.description));
            }
            if (criteria.getStartdate() != null) {
                specification = specification.and(buildRangeSpecification(criteria.getStartdate(), Observe_.startdate));
            }
            if (criteria.getUserId() != null) {
                specification =
                    specification.and(
                        buildSpecification(criteria.getUserId(), root -> root.join(Observe_.user, JoinType.LEFT).get(User_.id))
                    );
            }
        }
        return specification;
    }
}
