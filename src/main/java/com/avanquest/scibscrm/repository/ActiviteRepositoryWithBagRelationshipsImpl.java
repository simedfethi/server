package com.avanquest.scibscrm.repository;

import com.avanquest.scibscrm.domain.Activite;
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
public class ActiviteRepositoryWithBagRelationshipsImpl implements ActiviteRepositoryWithBagRelationships {

    @PersistenceContext
    private EntityManager entityManager;

    @Override
    public Optional<Activite> fetchBagRelationships(Optional<Activite> activite) {
        return activite.map(this::fetchEmployeeIncluses);
    }

    @Override
    public Page<Activite> fetchBagRelationships(Page<Activite> activites) {
        return new PageImpl<>(fetchBagRelationships(activites.getContent()), activites.getPageable(), activites.getTotalElements());
    }

    @Override
    public List<Activite> fetchBagRelationships(List<Activite> activites) {
        return Optional.of(activites).map(this::fetchEmployeeIncluses).orElse(Collections.emptyList());
    }

    Activite fetchEmployeeIncluses(Activite result) {
        return entityManager
            .createQuery(
                "select activite from Activite activite left join fetch activite.employeeIncluses where activite is :activite",
                Activite.class
            )
            .setParameter("activite", result)
            .setHint(QueryHints.PASS_DISTINCT_THROUGH, false)
            .getSingleResult();
    }

    List<Activite> fetchEmployeeIncluses(List<Activite> activites) {
        return entityManager
            .createQuery(
                "select distinct activite from Activite activite left join fetch activite.employeeIncluses where activite in :activites",
                Activite.class
            )
            .setParameter("activites", activites)
            .setHint(QueryHints.PASS_DISTINCT_THROUGH, false)
            .getResultList();
    }
}
