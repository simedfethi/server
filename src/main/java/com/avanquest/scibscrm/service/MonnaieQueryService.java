package com.avanquest.scibscrm.service;

import com.avanquest.scibscrm.domain.*; // for static metamodels
import com.avanquest.scibscrm.domain.Monnaie;
import com.avanquest.scibscrm.repository.MonnaieRepository;
import com.avanquest.scibscrm.service.criteria.MonnaieCriteria;
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
 * Service for executing complex queries for {@link Monnaie} entities in the database.
 * The main input is a {@link MonnaieCriteria} which gets converted to {@link Specification},
 * in a way that all the filters must apply.
 * It returns a {@link List} of {@link Monnaie} or a {@link Page} of {@link Monnaie} which fulfills the criteria.
 */
@Service
@Transactional(readOnly = true)
public class MonnaieQueryService extends QueryService<Monnaie> {

    private final Logger log = LoggerFactory.getLogger(MonnaieQueryService.class);

    private final MonnaieRepository monnaieRepository;

    public MonnaieQueryService(MonnaieRepository monnaieRepository) {
        this.monnaieRepository = monnaieRepository;
    }

    /**
     * Return a {@link List} of {@link Monnaie} which matches the criteria from the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the matching entities.
     */
    @Transactional(readOnly = true)
    public List<Monnaie> findByCriteria(MonnaieCriteria criteria) {
        log.debug("find by criteria : {}", criteria);
        final Specification<Monnaie> specification = createSpecification(criteria);
        return monnaieRepository.findAll(specification);
    }

    /**
     * Return a {@link Page} of {@link Monnaie} which matches the criteria from the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @param page The page, which should be returned.
     * @return the matching entities.
     */
    @Transactional(readOnly = true)
    public Page<Monnaie> findByCriteria(MonnaieCriteria criteria, Pageable page) {
        log.debug("find by criteria : {}, page: {}", criteria, page);
        final Specification<Monnaie> specification = createSpecification(criteria);
        return monnaieRepository.findAll(specification, page);
    }

    /**
     * Return the number of matching entities in the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the number of matching entities.
     */
    @Transactional(readOnly = true)
    public long countByCriteria(MonnaieCriteria criteria) {
        log.debug("count by criteria : {}", criteria);
        final Specification<Monnaie> specification = createSpecification(criteria);
        return monnaieRepository.count(specification);
    }

    /**
     * Function to convert {@link MonnaieCriteria} to a {@link Specification}
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the matching {@link Specification} of the entity.
     */
    protected Specification<Monnaie> createSpecification(MonnaieCriteria criteria) {
        Specification<Monnaie> specification = Specification.where(null);
        if (criteria != null) {
            // This has to be called first, because the distinct method returns null
            if (criteria.getDistinct() != null) {
                specification = specification.and(distinct(criteria.getDistinct()));
            }
            if (criteria.getId() != null) {
                specification = specification.and(buildRangeSpecification(criteria.getId(), Monnaie_.id));
            }
            if (criteria.getMoneyName() != null) {
                specification = specification.and(buildStringSpecification(criteria.getMoneyName(), Monnaie_.moneyName));
            }
            if (criteria.getMoneyIsocode() != null) {
                specification = specification.and(buildStringSpecification(criteria.getMoneyIsocode(), Monnaie_.moneyIsocode));
            }
        }
        return specification;
    }
}
