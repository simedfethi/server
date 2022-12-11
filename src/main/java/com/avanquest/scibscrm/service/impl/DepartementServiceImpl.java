package com.avanquest.scibscrm.service.impl;

import com.avanquest.scibscrm.domain.Departement;
import com.avanquest.scibscrm.repository.DepartementRepository;
import com.avanquest.scibscrm.service.DepartementService;
import java.util.Optional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * Service Implementation for managing {@link Departement}.
 */
@Service
@Transactional
public class DepartementServiceImpl implements DepartementService {

    private final Logger log = LoggerFactory.getLogger(DepartementServiceImpl.class);

    private final DepartementRepository departementRepository;

    public DepartementServiceImpl(DepartementRepository departementRepository) {
        this.departementRepository = departementRepository;
    }

    @Override
    public Departement save(Departement departement) {
        log.debug("Request to save Departement : {}", departement);
        return departementRepository.save(departement);
    }

    @Override
    public Departement update(Departement departement) {
        log.debug("Request to save Departement : {}", departement);
        return departementRepository.save(departement);
    }

    @Override
    public Optional<Departement> partialUpdate(Departement departement) {
        log.debug("Request to partially update Departement : {}", departement);

        return departementRepository
            .findById(departement.getId())
            .map(existingDepartement -> {
                if (departement.getDepartmentName() != null) {
                    existingDepartement.setDepartmentName(departement.getDepartmentName());
                }
                if (departement.getDepartmentCode() != null) {
                    existingDepartement.setDepartmentCode(departement.getDepartmentCode());
                }

                return existingDepartement;
            })
            .map(departementRepository::save);
    }

    @Override
    @Transactional(readOnly = true)
    public Page<Departement> findAll(Pageable pageable) {
        log.debug("Request to get all Departements");
        return departementRepository.findAll(pageable);
    }

    public Page<Departement> findAllWithEagerRelationships(Pageable pageable) {
        return departementRepository.findAllWithEagerRelationships(pageable);
    }

    @Override
    @Transactional(readOnly = true)
    public Optional<Departement> findOne(Long id) {
        log.debug("Request to get Departement : {}", id);
        return departementRepository.findOneWithEagerRelationships(id);
    }

    @Override
    public void delete(Long id) {
        log.debug("Request to delete Departement : {}", id);
        departementRepository.deleteById(id);
    }
}
