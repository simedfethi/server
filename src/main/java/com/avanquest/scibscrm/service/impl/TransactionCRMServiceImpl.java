package com.avanquest.scibscrm.service.impl;

import com.avanquest.scibscrm.domain.TransactionCRM;
import com.avanquest.scibscrm.repository.TransactionCRMRepository;
import com.avanquest.scibscrm.service.TransactionCRMService;
import java.util.Optional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * Service Implementation for managing {@link TransactionCRM}.
 */
@Service
@Transactional
public class TransactionCRMServiceImpl implements TransactionCRMService {

    private final Logger log = LoggerFactory.getLogger(TransactionCRMServiceImpl.class);

    private final TransactionCRMRepository transactionCRMRepository;

    public TransactionCRMServiceImpl(TransactionCRMRepository transactionCRMRepository) {
        this.transactionCRMRepository = transactionCRMRepository;
    }

    @Override
    public TransactionCRM save(TransactionCRM transactionCRM) {
        log.debug("Request to save TransactionCRM : {}", transactionCRM);
        return transactionCRMRepository.save(transactionCRM);
    }

    @Override
    public TransactionCRM update(TransactionCRM transactionCRM) {
        log.debug("Request to save TransactionCRM : {}", transactionCRM);
        return transactionCRMRepository.save(transactionCRM);
    }

    @Override
    public Optional<TransactionCRM> partialUpdate(TransactionCRM transactionCRM) {
        log.debug("Request to partially update TransactionCRM : {}", transactionCRM);

        return transactionCRMRepository
            .findById(transactionCRM.getId())
            .map(existingTransactionCRM -> {
                if (transactionCRM.getReference() != null) {
                    existingTransactionCRM.setReference(transactionCRM.getReference());
                }
                if (transactionCRM.getMontant() != null) {
                    existingTransactionCRM.setMontant(transactionCRM.getMontant());
                }
                if (transactionCRM.getTransactionEtape() != null) {
                    existingTransactionCRM.setTransactionEtape(transactionCRM.getTransactionEtape());
                }
                if (transactionCRM.getDateFin() != null) {
                    existingTransactionCRM.setDateFin(transactionCRM.getDateFin());
                }
                if (transactionCRM.getTransactionRecurrente() != null) {
                    existingTransactionCRM.setTransactionRecurrente(transactionCRM.getTransactionRecurrente());
                }
                if (transactionCRM.getCreeLe() != null) {
                    existingTransactionCRM.setCreeLe(transactionCRM.getCreeLe());
                }
                if (transactionCRM.getDernierUpdate() != null) {
                    existingTransactionCRM.setDernierUpdate(transactionCRM.getDernierUpdate());
                }
                if (transactionCRM.getTelephone() != null) {
                    existingTransactionCRM.setTelephone(transactionCRM.getTelephone());
                }
                if (transactionCRM.getSource() != null) {
                    existingTransactionCRM.setSource(transactionCRM.getSource());
                }
                if (transactionCRM.getAdresse() != null) {
                    existingTransactionCRM.setAdresse(transactionCRM.getAdresse());
                }
                if (transactionCRM.getNotes() != null) {
                    existingTransactionCRM.setNotes(transactionCRM.getNotes());
                }
                if (transactionCRM.getLatitude() != null) {
                    existingTransactionCRM.setLatitude(transactionCRM.getLatitude());
                }
                if (transactionCRM.getLongitude() != null) {
                    existingTransactionCRM.setLongitude(transactionCRM.getLongitude());
                }

                return existingTransactionCRM;
            })
            .map(transactionCRMRepository::save);
    }

    @Override
    @Transactional(readOnly = true)
    public Page<TransactionCRM> findAll(Pageable pageable) {
        log.debug("Request to get all TransactionCRMS");
        return transactionCRMRepository.findAll(pageable);
    }

    public Page<TransactionCRM> findAllWithEagerRelationships(Pageable pageable) {
        return transactionCRMRepository.findAllWithEagerRelationships(pageable);
    }

    @Override
    @Transactional(readOnly = true)
    public Optional<TransactionCRM> findOne(Long id) {
        log.debug("Request to get TransactionCRM : {}", id);
        return transactionCRMRepository.findOneWithEagerRelationships(id);
    }

    @Override
    public void delete(Long id) {
        log.debug("Request to delete TransactionCRM : {}", id);
        transactionCRMRepository.deleteById(id);
    }
}
