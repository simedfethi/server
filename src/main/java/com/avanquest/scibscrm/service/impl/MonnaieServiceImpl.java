package com.avanquest.scibscrm.service.impl;

import com.avanquest.scibscrm.domain.Monnaie;
import com.avanquest.scibscrm.repository.MonnaieRepository;
import com.avanquest.scibscrm.service.MonnaieService;
import java.util.Optional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * Service Implementation for managing {@link Monnaie}.
 */
@Service
@Transactional
public class MonnaieServiceImpl implements MonnaieService {

    private final Logger log = LoggerFactory.getLogger(MonnaieServiceImpl.class);

    private final MonnaieRepository monnaieRepository;

    public MonnaieServiceImpl(MonnaieRepository monnaieRepository) {
        this.monnaieRepository = monnaieRepository;
    }

    @Override
    public Monnaie save(Monnaie monnaie) {
        log.debug("Request to save Monnaie : {}", monnaie);
        return monnaieRepository.save(monnaie);
    }

    @Override
    public Monnaie update(Monnaie monnaie) {
        log.debug("Request to save Monnaie : {}", monnaie);
        return monnaieRepository.save(monnaie);
    }

    @Override
    public Optional<Monnaie> partialUpdate(Monnaie monnaie) {
        log.debug("Request to partially update Monnaie : {}", monnaie);

        return monnaieRepository
            .findById(monnaie.getId())
            .map(existingMonnaie -> {
                if (monnaie.getMoneyName() != null) {
                    existingMonnaie.setMoneyName(monnaie.getMoneyName());
                }
                if (monnaie.getMoneyIsocode() != null) {
                    existingMonnaie.setMoneyIsocode(monnaie.getMoneyIsocode());
                }

                return existingMonnaie;
            })
            .map(monnaieRepository::save);
    }

    @Override
    @Transactional(readOnly = true)
    public Page<Monnaie> findAll(Pageable pageable) {
        log.debug("Request to get all Monnaies");
        return monnaieRepository.findAll(pageable);
    }

    @Override
    @Transactional(readOnly = true)
    public Optional<Monnaie> findOne(Long id) {
        log.debug("Request to get Monnaie : {}", id);
        return monnaieRepository.findById(id);
    }

    @Override
    public void delete(Long id) {
        log.debug("Request to delete Monnaie : {}", id);
        monnaieRepository.deleteById(id);
    }
}
