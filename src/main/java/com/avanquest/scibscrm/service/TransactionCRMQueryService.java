package com.avanquest.scibscrm.service;

import com.avanquest.scibscrm.domain.*; // for static metamodels
import com.avanquest.scibscrm.domain.TransactionCRM;
import com.avanquest.scibscrm.repository.TransactionCRMRepository;
import com.avanquest.scibscrm.service.criteria.TransactionCRMCriteria;
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
 * Service for executing complex queries for {@link TransactionCRM} entities in the database.
 * The main input is a {@link TransactionCRMCriteria} which gets converted to {@link Specification},
 * in a way that all the filters must apply.
 * It returns a {@link List} of {@link TransactionCRM} or a {@link Page} of {@link TransactionCRM} which fulfills the criteria.
 */
@Service
@Transactional(readOnly = true)
public class TransactionCRMQueryService extends QueryService<TransactionCRM> {

    private final Logger log = LoggerFactory.getLogger(TransactionCRMQueryService.class);

    private final TransactionCRMRepository transactionCRMRepository;

    public TransactionCRMQueryService(TransactionCRMRepository transactionCRMRepository) {
        this.transactionCRMRepository = transactionCRMRepository;
    }

    /**
     * Return a {@link List} of {@link TransactionCRM} which matches the criteria from the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the matching entities.
     */
    @Transactional(readOnly = true)
    public List<TransactionCRM> findByCriteria(TransactionCRMCriteria criteria) {
        log.debug("find by criteria : {}", criteria);
        final Specification<TransactionCRM> specification = createSpecification(criteria);
        return transactionCRMRepository.findAll(specification);
    }

    /**
     * Return a {@link Page} of {@link TransactionCRM} which matches the criteria from the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @param page The page, which should be returned.
     * @return the matching entities.
     */
    @Transactional(readOnly = true)
    public Page<TransactionCRM> findByCriteria(TransactionCRMCriteria criteria, Pageable page) {
        log.debug("find by criteria : {}, page: {}", criteria, page);
        final Specification<TransactionCRM> specification = createSpecification(criteria);
        return transactionCRMRepository.findAll(specification, page);
    }

    /**
     * Return the number of matching entities in the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the number of matching entities.
     */
    @Transactional(readOnly = true)
    public long countByCriteria(TransactionCRMCriteria criteria) {
        log.debug("count by criteria : {}", criteria);
        final Specification<TransactionCRM> specification = createSpecification(criteria);
        return transactionCRMRepository.count(specification);
    }

    /**
     * Function to convert {@link TransactionCRMCriteria} to a {@link Specification}
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the matching {@link Specification} of the entity.
     */
    protected Specification<TransactionCRM> createSpecification(TransactionCRMCriteria criteria) {
        Specification<TransactionCRM> specification = Specification.where(null);
        if (criteria != null) {
            // This has to be called first, because the distinct method returns null
            if (criteria.getDistinct() != null) {
                specification = specification.and(distinct(criteria.getDistinct()));
            }
            if (criteria.getId() != null) {
                specification = specification.and(buildRangeSpecification(criteria.getId(), TransactionCRM_.id));
            }
            if (criteria.getReference() != null) {
                specification = specification.and(buildStringSpecification(criteria.getReference(), TransactionCRM_.reference));
            }
            if (criteria.getMontant() != null) {
                specification = specification.and(buildRangeSpecification(criteria.getMontant(), TransactionCRM_.montant));
            }
            if (criteria.getTransactionEtape() != null) {
                specification = specification.and(buildSpecification(criteria.getTransactionEtape(), TransactionCRM_.transactionEtape));
            }
            if (criteria.getDateFin() != null) {
                specification = specification.and(buildRangeSpecification(criteria.getDateFin(), TransactionCRM_.dateFin));
            }
            if (criteria.getTransactionRecurrente() != null) {
                specification =
                    specification.and(buildSpecification(criteria.getTransactionRecurrente(), TransactionCRM_.transactionRecurrente));
            }
            if (criteria.getCreeLe() != null) {
                specification = specification.and(buildRangeSpecification(criteria.getCreeLe(), TransactionCRM_.creeLe));
            }
            if (criteria.getDernierUpdate() != null) {
                specification = specification.and(buildRangeSpecification(criteria.getDernierUpdate(), TransactionCRM_.dernierUpdate));
            }
            if (criteria.getTelephone() != null) {
                specification = specification.and(buildStringSpecification(criteria.getTelephone(), TransactionCRM_.telephone));
            }
            if (criteria.getSource() != null) {
                specification = specification.and(buildSpecification(criteria.getSource(), TransactionCRM_.source));
            }
            if (criteria.getLatitude() != null) {
                specification = specification.and(buildRangeSpecification(criteria.getLatitude(), TransactionCRM_.latitude));
            }
            if (criteria.getLongitude() != null) {
                specification = specification.and(buildRangeSpecification(criteria.getLongitude(), TransactionCRM_.longitude));
            }
            if (criteria.getMonnaieId() != null) {
                specification =
                    specification.and(
                        buildSpecification(
                            criteria.getMonnaieId(),
                            root -> root.join(TransactionCRM_.monnaie, JoinType.LEFT).get(Monnaie_.id)
                        )
                    );
            }
            if (criteria.getChargeAffaireId() != null) {
                specification =
                    specification.and(
                        buildSpecification(
                            criteria.getChargeAffaireId(),
                            root -> root.join(TransactionCRM_.chargeAffaire, JoinType.LEFT).get(Employee_.id)
                        )
                    );
            }
            if (criteria.getClientId() != null) {
                specification =
                    specification.and(
                        buildSpecification(
                            criteria.getClientId(),
                            root -> root.join(TransactionCRM_.client, JoinType.LEFT).get(Customer_.id)
                        )
                    );
            }
            if (criteria.getActiviteId() != null) {
                specification =
                    specification.and(
                        buildSpecification(
                            criteria.getActiviteId(),
                            root -> root.join(TransactionCRM_.activites, JoinType.LEFT).get(Activite_.id)
                        )
                    );
            }
        }
        return specification;
    }
}
