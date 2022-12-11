package com.avanquest.scibscrm.service.impl;

import com.avanquest.scibscrm.domain.Productvariante;
import com.avanquest.scibscrm.repository.ProductvarianteRepository;
import com.avanquest.scibscrm.service.ProductvarianteService;
import java.util.Optional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * Service Implementation for managing {@link Productvariante}.
 */
@Service
@Transactional
public class ProductvarianteServiceImpl implements ProductvarianteService {

    private final Logger log = LoggerFactory.getLogger(ProductvarianteServiceImpl.class);

    private final ProductvarianteRepository productvarianteRepository;

    public ProductvarianteServiceImpl(ProductvarianteRepository productvarianteRepository) {
        this.productvarianteRepository = productvarianteRepository;
    }

    @Override
    public Productvariante save(Productvariante productvariante) {
        log.debug("Request to save Productvariante : {}", productvariante);
        return productvarianteRepository.save(productvariante);
    }

    @Override
    public Productvariante update(Productvariante productvariante) {
        log.debug("Request to save Productvariante : {}", productvariante);
        return productvarianteRepository.save(productvariante);
    }

    @Override
    public Optional<Productvariante> partialUpdate(Productvariante productvariante) {
        log.debug("Request to partially update Productvariante : {}", productvariante);

        return productvarianteRepository
            .findById(productvariante.getId())
            .map(existingProductvariante -> {
                if (productvariante.getPicture() != null) {
                    existingProductvariante.setPicture(productvariante.getPicture());
                }
                if (productvariante.getPictureContentType() != null) {
                    existingProductvariante.setPictureContentType(productvariante.getPictureContentType());
                }
                if (productvariante.getCodebarre() != null) {
                    existingProductvariante.setCodebarre(productvariante.getCodebarre());
                }
                if (productvariante.getProductCode() != null) {
                    existingProductvariante.setProductCode(productvariante.getProductCode());
                }
                if (productvariante.getSalePrice() != null) {
                    existingProductvariante.setSalePrice(productvariante.getSalePrice());
                }
                if (productvariante.getUniteMesure() != null) {
                    existingProductvariante.setUniteMesure(productvariante.getUniteMesure());
                }
                if (productvariante.getStockDisponible() != null) {
                    existingProductvariante.setStockDisponible(productvariante.getStockDisponible());
                }

                return existingProductvariante;
            })
            .map(productvarianteRepository::save);
    }

    @Override
    @Transactional(readOnly = true)
    public Page<Productvariante> findAll(Pageable pageable) {
        log.debug("Request to get all Productvariantes");
        return productvarianteRepository.findAll(pageable);
    }

    @Override
    @Transactional(readOnly = true)
    public Optional<Productvariante> findOne(Long id) {
        log.debug("Request to get Productvariante : {}", id);
        return productvarianteRepository.findById(id);
    }

    @Override
    public void delete(Long id) {
        log.debug("Request to delete Productvariante : {}", id);
        productvarianteRepository.deleteById(id);
    }
}
