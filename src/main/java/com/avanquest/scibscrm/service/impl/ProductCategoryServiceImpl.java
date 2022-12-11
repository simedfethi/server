package com.avanquest.scibscrm.service.impl;

import com.avanquest.scibscrm.domain.ProductCategory;
import com.avanquest.scibscrm.repository.ProductCategoryRepository;
import com.avanquest.scibscrm.service.ProductCategoryService;
import java.util.Optional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * Service Implementation for managing {@link ProductCategory}.
 */
@Service
@Transactional
public class ProductCategoryServiceImpl implements ProductCategoryService {

    private final Logger log = LoggerFactory.getLogger(ProductCategoryServiceImpl.class);

    private final ProductCategoryRepository productCategoryRepository;

    public ProductCategoryServiceImpl(ProductCategoryRepository productCategoryRepository) {
        this.productCategoryRepository = productCategoryRepository;
    }

    @Override
    public ProductCategory save(ProductCategory productCategory) {
        log.debug("Request to save ProductCategory : {}", productCategory);
        return productCategoryRepository.save(productCategory);
    }

    @Override
    public ProductCategory update(ProductCategory productCategory) {
        log.debug("Request to save ProductCategory : {}", productCategory);
        return productCategoryRepository.save(productCategory);
    }

    @Override
    public Optional<ProductCategory> partialUpdate(ProductCategory productCategory) {
        log.debug("Request to partially update ProductCategory : {}", productCategory);

        return productCategoryRepository
            .findById(productCategory.getId())
            .map(existingProductCategory -> {
                if (productCategory.getCategoryName() != null) {
                    existingProductCategory.setCategoryName(productCategory.getCategoryName());
                }
                if (productCategory.getCategoryCode() != null) {
                    existingProductCategory.setCategoryCode(productCategory.getCategoryCode());
                }

                return existingProductCategory;
            })
            .map(productCategoryRepository::save);
    }

    @Override
    @Transactional(readOnly = true)
    public Page<ProductCategory> findAll(Pageable pageable) {
        log.debug("Request to get all ProductCategories");
        return productCategoryRepository.findAll(pageable);
    }

    @Override
    @Transactional(readOnly = true)
    public Optional<ProductCategory> findOne(Long id) {
        log.debug("Request to get ProductCategory : {}", id);
        return productCategoryRepository.findById(id);
    }

    @Override
    public void delete(Long id) {
        log.debug("Request to delete ProductCategory : {}", id);
        productCategoryRepository.deleteById(id);
    }
}
