package com.avanquest.scibscrm.service;

import com.avanquest.scibscrm.domain.*; // for static metamodels
import com.avanquest.scibscrm.domain.Productvariante;
import com.avanquest.scibscrm.repository.ProductvarianteRepository;
import com.avanquest.scibscrm.service.criteria.ProductvarianteCriteria;
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
 * Service for executing complex queries for {@link Productvariante} entities in the database.
 * The main input is a {@link ProductvarianteCriteria} which gets converted to {@link Specification},
 * in a way that all the filters must apply.
 * It returns a {@link List} of {@link Productvariante} or a {@link Page} of {@link Productvariante} which fulfills the criteria.
 */
@Service
@Transactional(readOnly = true)
public class ProductvarianteQueryService extends QueryService<Productvariante> {

    private final Logger log = LoggerFactory.getLogger(ProductvarianteQueryService.class);

    private final ProductvarianteRepository productvarianteRepository;

    public ProductvarianteQueryService(ProductvarianteRepository productvarianteRepository) {
        this.productvarianteRepository = productvarianteRepository;
    }

    /**
     * Return a {@link List} of {@link Productvariante} which matches the criteria from the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the matching entities.
     */
    @Transactional(readOnly = true)
    public List<Productvariante> findByCriteria(ProductvarianteCriteria criteria) {
        log.debug("find by criteria : {}", criteria);
        final Specification<Productvariante> specification = createSpecification(criteria);
        return productvarianteRepository.findAll(specification);
    }

    /**
     * Return a {@link Page} of {@link Productvariante} which matches the criteria from the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @param page The page, which should be returned.
     * @return the matching entities.
     */
    @Transactional(readOnly = true)
    public Page<Productvariante> findByCriteria(ProductvarianteCriteria criteria, Pageable page) {
        log.debug("find by criteria : {}, page: {}", criteria, page);
        final Specification<Productvariante> specification = createSpecification(criteria);
        return productvarianteRepository.findAll(specification, page);
    }

    /**
     * Return the number of matching entities in the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the number of matching entities.
     */
    @Transactional(readOnly = true)
    public long countByCriteria(ProductvarianteCriteria criteria) {
        log.debug("count by criteria : {}", criteria);
        final Specification<Productvariante> specification = createSpecification(criteria);
        return productvarianteRepository.count(specification);
    }

    /**
     * Function to convert {@link ProductvarianteCriteria} to a {@link Specification}
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the matching {@link Specification} of the entity.
     */
    protected Specification<Productvariante> createSpecification(ProductvarianteCriteria criteria) {
        Specification<Productvariante> specification = Specification.where(null);
        if (criteria != null) {
            // This has to be called first, because the distinct method returns null
            if (criteria.getDistinct() != null) {
                specification = specification.and(distinct(criteria.getDistinct()));
            }
            if (criteria.getId() != null) {
                specification = specification.and(buildRangeSpecification(criteria.getId(), Productvariante_.id));
            }
            if (criteria.getCodebarre() != null) {
                specification = specification.and(buildStringSpecification(criteria.getCodebarre(), Productvariante_.codebarre));
            }
            if (criteria.getProductCode() != null) {
                specification = specification.and(buildStringSpecification(criteria.getProductCode(), Productvariante_.productCode));
            }
            if (criteria.getSalePrice() != null) {
                specification = specification.and(buildRangeSpecification(criteria.getSalePrice(), Productvariante_.salePrice));
            }
            if (criteria.getUniteMesure() != null) {
                specification = specification.and(buildStringSpecification(criteria.getUniteMesure(), Productvariante_.uniteMesure));
            }
            if (criteria.getStockDisponible() != null) {
                specification = specification.and(buildRangeSpecification(criteria.getStockDisponible(), Productvariante_.stockDisponible));
            }
            if (criteria.getProductId() != null) {
                specification =
                    specification.and(
                        buildSpecification(
                            criteria.getProductId(),
                            root -> root.join(Productvariante_.products, JoinType.LEFT).get(Product_.id)
                        )
                    );
            }
        }
        return specification;
    }
}
