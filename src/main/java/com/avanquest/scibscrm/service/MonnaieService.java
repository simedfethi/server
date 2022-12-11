package com.avanquest.scibscrm.service;

import com.avanquest.scibscrm.domain.Monnaie;
import java.util.Optional;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

/**
 * Service Interface for managing {@link Monnaie}.
 */
public interface MonnaieService {
    /**
     * Save a monnaie.
     *
     * @param monnaie the entity to save.
     * @return the persisted entity.
     */
    Monnaie save(Monnaie monnaie);

    /**
     * Updates a monnaie.
     *
     * @param monnaie the entity to update.
     * @return the persisted entity.
     */
    Monnaie update(Monnaie monnaie);

    /**
     * Partially updates a monnaie.
     *
     * @param monnaie the entity to update partially.
     * @return the persisted entity.
     */
    Optional<Monnaie> partialUpdate(Monnaie monnaie);

    /**
     * Get all the monnaies.
     *
     * @param pageable the pagination information.
     * @return the list of entities.
     */
    Page<Monnaie> findAll(Pageable pageable);

    /**
     * Get the "id" monnaie.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    Optional<Monnaie> findOne(Long id);

    /**
     * Delete the "id" monnaie.
     *
     * @param id the id of the entity.
     */
    void delete(Long id);
}
