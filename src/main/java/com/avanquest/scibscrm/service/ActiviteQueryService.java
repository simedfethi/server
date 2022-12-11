package com.avanquest.scibscrm.service;

import com.avanquest.scibscrm.domain.*; // for static metamodels
import com.avanquest.scibscrm.domain.Activite;
import com.avanquest.scibscrm.repository.ActiviteRepository;
import com.avanquest.scibscrm.service.criteria.ActiviteCriteria;
import java.util.List;
import javax.persistence.criteria.JoinType;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import tech.jhipster.service.QueryService;

/**
 * Service for executing complex queries for {@link Activite} entities in the database.
 * The main input is a {@link ActiviteCriteria} which gets converted to {@link Specification},
 * in a way that all the filters must apply.
 * It returns a {@link List} of {@link Activite} or a {@link Page} of {@link Activite} which fulfills the criteria.
 */
@Service
@Transactional(readOnly = true)
public class ActiviteQueryService extends QueryService<Activite> {

    private final Logger log = LoggerFactory.getLogger(ActiviteQueryService.class);

    private final ActiviteRepository activiteRepository;

    public ActiviteQueryService(ActiviteRepository activiteRepository) {
        this.activiteRepository = activiteRepository;
    }

    /**
     * Return a {@link List} of {@link Activite} which matches the criteria from the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the matching entities.
     */
    @Transactional(readOnly = true)
    public List<Activite> findByCriteria(ActiviteCriteria criteria) {
        log.debug("find by criteria : {}", criteria);
        final Specification<Activite> specification = createSpecification(criteria);
        return activiteRepository.findAll(specification);
    }

    /**
     * Return a {@link Page} of {@link Activite} which matches the criteria from the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @param page The page, which should be returned.
     * @return the matching entities.
     */
    @Transactional(readOnly = true)
    public Page<Activite> findByCriteria(ActiviteCriteria criteria, Pageable page) {
        log.debug("find by criteria : {}, page: {}", criteria, page);
        final Specification<Activite> specification = createSpecification(criteria);
        return activiteRepository.findAll(specification, page);
    }

    /**
     * Return the number of matching entities in the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the number of matching entities.
     */
    @Transactional(readOnly = true)
    public long countByCriteria(ActiviteCriteria criteria) {
        log.debug("count by criteria : {}", criteria);
        final Specification<Activite> specification = createSpecification(criteria);
        return activiteRepository.count(specification);
    }

    /**
     * Function to convert {@link ActiviteCriteria} to a {@link Specification}
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the matching {@link Specification} of the entity.
     */
    protected Specification<Activite> createSpecification(ActiviteCriteria criteria) {
        Specification<Activite> specification = Specification.where(null);
        if (criteria != null) {
            // This has to be called first, because the distinct method returns null
            if (criteria.getDistinct() != null) {
                specification = specification.and(distinct(criteria.getDistinct()));
            }
            if (criteria.getId() != null) {
                specification = specification.and(buildRangeSpecification(criteria.getId(), Activite_.id));
            }
            if (criteria.getTypeactivite() != null) {
                specification = specification.and(buildSpecification(criteria.getTypeactivite(), Activite_.typeactivite));
            }
            if (criteria.getResume() != null) {
                specification = specification.and(buildStringSpecification(criteria.getResume(), Activite_.resume));
            }
            if (criteria.getDateEcheance() != null) {
                specification = specification.and(buildRangeSpecification(criteria.getDateEcheance(), Activite_.dateEcheance));
            }
            if (criteria.getHeureActivite() != null) {
                specification = specification.and(buildRangeSpecification(criteria.getHeureActivite(), Activite_.heureActivite));
            }
            if (criteria.getImportance() != null) {
                specification = specification.and(buildSpecification(criteria.getImportance(), Activite_.importance));
            }
            if (criteria.getCreateurId() != null) {
                specification =
                    specification.and(
                        buildSpecification(criteria.getCreateurId(), root -> root.join(Activite_.createur, JoinType.LEFT).get(Employee_.id))
                    );
            }
            if (criteria.getEmployeeInclusId() != null) {
                specification =
                    specification.and(
                        buildSpecification(
                            criteria.getEmployeeInclusId(),
                            root -> root.join(Activite_.employeeIncluses, JoinType.LEFT).get(Employee_.id)
                        )
                    );
            }
            if (criteria.getTransactionCRMId() != null) {
                specification =
                    specification.and(
                        buildSpecification(
                            criteria.getTransactionCRMId(),
                            root -> root.join(Activite_.transactionCRMS, JoinType.LEFT).get(TransactionCRM_.id)
                        )
                    );
            }
        }
        return specification;
    }
}
