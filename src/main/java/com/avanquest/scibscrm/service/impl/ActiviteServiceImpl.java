package com.avanquest.scibscrm.service.impl;

import com.avanquest.scibscrm.domain.Activite;
import com.avanquest.scibscrm.repository.ActiviteRepository;
import com.avanquest.scibscrm.service.ActiviteService;
import java.util.Optional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * Service Implementation for managing {@link Activite}.
 */
@Service
@Transactional
public class ActiviteServiceImpl implements ActiviteService {

    private final Logger log = LoggerFactory.getLogger(ActiviteServiceImpl.class);

    private final ActiviteRepository activiteRepository;

    public ActiviteServiceImpl(ActiviteRepository activiteRepository) {
        this.activiteRepository = activiteRepository;
    }

    @Override
    public Activite save(Activite activite) {
        log.debug("Request to save Activite : {}", activite);
        return activiteRepository.save(activite);
    }

    @Override
    public Activite update(Activite activite) {
        log.debug("Request to save Activite : {}", activite);
        return activiteRepository.save(activite);
    }

    @Override
    public Optional<Activite> partialUpdate(Activite activite) {
        log.debug("Request to partially update Activite : {}", activite);

        return activiteRepository
            .findById(activite.getId())
            .map(existingActivite -> {
                if (activite.getTypeactivite() != null) {
                    existingActivite.setTypeactivite(activite.getTypeactivite());
                }
                if (activite.getResume() != null) {
                    existingActivite.setResume(activite.getResume());
                }
                if (activite.getDateEcheance() != null) {
                    existingActivite.setDateEcheance(activite.getDateEcheance());
                }
                if (activite.getHeureActivite() != null) {
                    existingActivite.setHeureActivite(activite.getHeureActivite());
                }
                if (activite.getImportance() != null) {
                    existingActivite.setImportance(activite.getImportance());
                }
                if (activite.getNote() != null) {
                    existingActivite.setNote(activite.getNote());
                }

                return existingActivite;
            })
            .map(activiteRepository::save);
    }

    @Override
    @Transactional(readOnly = true)
    public Page<Activite> findAll(Pageable pageable) {
        log.debug("Request to get all Activites");
        return activiteRepository.findAll(pageable);
    }

    public Page<Activite> findAllWithEagerRelationships(Pageable pageable) {
        return activiteRepository.findAllWithEagerRelationships(pageable);
    }

    @Override
    @Transactional(readOnly = true)
    public Optional<Activite> findOne(Long id) {
        log.debug("Request to get Activite : {}", id);
        return activiteRepository.findOneWithEagerRelationships(id);
    }

    @Override
    public void delete(Long id) {
        log.debug("Request to delete Activite : {}", id);
        activiteRepository.deleteById(id);
    }
}
