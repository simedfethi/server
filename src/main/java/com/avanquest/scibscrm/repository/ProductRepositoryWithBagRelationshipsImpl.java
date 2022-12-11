package com.avanquest.scibscrm.repository;

import com.avanquest.scibscrm.domain.Product;
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
public class ProductRepositoryWithBagRelationshipsImpl implements ProductRepositoryWithBagRelationships {

    @PersistenceContext
    private EntityManager entityManager;

    @Override
    public Optional<Product> fetchBagRelationships(Optional<Product> product) {
        return product.map(this::fetchProductvariantes);
    }

    @Override
    public Page<Product> fetchBagRelationships(Page<Product> products) {
        return new PageImpl<>(fetchBagRelationships(products.getContent()), products.getPageable(), products.getTotalElements());
    }

    @Override
    public List<Product> fetchBagRelationships(List<Product> products) {
        return Optional.of(products).map(this::fetchProductvariantes).orElse(Collections.emptyList());
    }

    Product fetchProductvariantes(Product result) {
        return entityManager
            .createQuery(
                "select product from Product product left join fetch product.productvariantes where product is :product",
                Product.class
            )
            .setParameter("product", result)
            .setHint(QueryHints.PASS_DISTINCT_THROUGH, false)
            .getSingleResult();
    }

    List<Product> fetchProductvariantes(List<Product> products) {
        return entityManager
            .createQuery(
                "select distinct product from Product product left join fetch product.productvariantes where product in :products",
                Product.class
            )
            .setParameter("products", products)
            .setHint(QueryHints.PASS_DISTINCT_THROUGH, false)
            .getResultList();
    }
}
