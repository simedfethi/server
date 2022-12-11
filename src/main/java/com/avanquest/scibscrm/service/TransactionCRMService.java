package com.avanquest.scibscrm.service;

import com.avanquest.scibscrm.domain.TransactionCRM;
import java.util.Optional;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

/**
 * Service Interface for managing {@link TransactionCRM}.
 */
public interface TransactionCRMService {
    /**
     * Save a transactionCRM.
     *
     * @param transactionCRM the entity to save.
     * @return the persisted entity.
     */
    TransactionCRM save(TransactionCRM transactionCRM);

    /**
     * Updates a transactionCRM.
     *
     * @param transactionCRM the entity to update.
     * @return the persisted entity.
     */
    TransactionCRM update(TransactionCRM transactionCRM);

    /**
     * Partially updates a transactionCRM.
     *
     * @param transactionCRM the entity to update partially.
     * @return the persisted entity.
     */
    Optional<TransactionCRM> partialUpdate(TransactionCRM transactionCRM);

    /**
     * Get all the transactionCRMS.
     *
     * @param pageable the pagination information.
     * @return the list of entities.
     */
    Page<TransactionCRM> findAll(Pageable pageable);

    /**
     * Get all the transactionCRMS with eager load of many-to-many relationships.
     *
     * @param pageable the pagination information.
     * @return the list of entities.
     */
    Page<TransactionCRM> findAllWithEagerRelationships(Pageable pageable);

    /**
     * Get the "id" transactionCRM.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    Optional<TransactionCRM> findOne(Long id);

    /**
     * Delete the "id" transactionCRM.
     *
     * @param id the id of the entity.
     */
    void delete(Long id);
}
