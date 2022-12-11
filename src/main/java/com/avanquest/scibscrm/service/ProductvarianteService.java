package com.avanquest.scibscrm.service;

import com.avanquest.scibscrm.domain.Productvariante;
import java.util.Optional;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

/**
 * Service Interface for managing {@link Productvariante}.
 */
public interface ProductvarianteService {
    /**
     * Save a productvariante.
     *
     * @param productvariante the entity to save.
     * @return the persisted entity.
     */
    Productvariante save(Productvariante productvariante);

    /**
     * Updates a productvariante.
     *
     * @param productvariante the entity to update.
     * @return the persisted entity.
     */
    Productvariante update(Productvariante productvariante);

    /**
     * Partially updates a productvariante.
     *
     * @param productvariante the entity to update partially.
     * @return the persisted entity.
     */
    Optional<Productvariante> partialUpdate(Productvariante productvariante);

    /**
     * Get all the productvariantes.
     *
     * @param pageable the pagination information.
     * @return the list of entities.
     */
    Page<Productvariante> findAll(Pageable pageable);

    /**
     * Get the "id" productvariante.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    Optional<Productvariante> findOne(Long id);

    /**
     * Delete the "id" productvariante.
     *
     * @param id the id of the entity.
     */
    void delete(Long id);
}
