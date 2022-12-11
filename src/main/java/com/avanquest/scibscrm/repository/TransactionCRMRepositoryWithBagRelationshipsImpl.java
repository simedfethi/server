package com.avanquest.scibscrm.repository;

import com.avanquest.scibscrm.domain.TransactionCRM;
import java.util.Collections;
import java.util.List;
import java.util.Optional;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import org.hibernate.annotations.QueryHints;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;

/**
 * Utility repository to load bag relationships based on https://vladmihalcea.com/hibernate-multiplebagfetchexception/
 */
public class TransactionCRMRepositoryWithBagRelationshipsImpl implements TransactionCRMRepositoryWithBagRelationships {

    @PersistenceContext
    private EntityManager entityManager;

    @Override
    public Optional<TransactionCRM> fetchBagRelationships(Optional<TransactionCRM> transactionCRM) {
        return transactionCRM.map(this::fetchActivites);
    }

    @Override
    public Page<TransactionCRM> fetchBagRelationships(Page<TransactionCRM> transactionCRMS) {
        return new PageImpl<>(
            fetchBagRelationships(transactionCRMS.getContent()),
            transactionCRMS.getPageable(),
            transactionCRMS.getTotalElements()
        );
    }

    @Override
    public List<TransactionCRM> fetchBagRelationships(List<TransactionCRM> transactionCRMS) {
        return Optional.of(transactionCRMS).map(this::fetchActivites).orElse(Collections.emptyList());
    }

    TransactionCRM fetchActivites(TransactionCRM result) {
        return entityManager
            .createQuery(
                "select transactionCRM from TransactionCRM transactionCRM left join fetch transactionCRM.activites where transactionCRM is :transactionCRM",
                TransactionCRM.class
            )
            .setParameter("transactionCRM", result)
            .setHint(QueryHints.PASS_DISTINCT_THROUGH, false)
            .getSingleResult();
    }

    List<TransactionCRM> fetchActivites(List<TransactionCRM> transactionCRMS) {
        return entityManager
            .createQuery(
                "select distinct transactionCRM from TransactionCRM transactionCRM left join fetch transactionCRM.activites where transactionCRM in :transactionCRMS",
                TransactionCRM.class
            )
            .setParameter("transactionCRMS", transactionCRMS)
            .setHint(QueryHints.PASS_DISTINCT_THROUGH, false)
            .getResultList();
    }
}
