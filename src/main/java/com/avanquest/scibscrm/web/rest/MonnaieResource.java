package com.avanquest.scibscrm.web.rest;

import com.avanquest.scibscrm.domain.Monnaie;
import com.avanquest.scibscrm.repository.MonnaieRepository;
import com.avanquest.scibscrm.service.MonnaieQueryService;
import com.avanquest.scibscrm.service.MonnaieService;
import com.avanquest.scibscrm.service.criteria.MonnaieCriteria;
import com.avanquest.scibscrm.web.rest.errors.BadRequestAlertException;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
import javax.validation.Valid;
import javax.validation.constraints.NotNull;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;
import tech.jhipster.web.util.HeaderUtil;
import tech.jhipster.web.util.PaginationUtil;
import tech.jhipster.web.util.ResponseUtil;

/**
 * REST controller for managing {@link com.avanquest.scibscrm.domain.Monnaie}.
 */
@RestController
@RequestMapping("/api")
public class MonnaieResource {

    private final Logger log = LoggerFactory.getLogger(MonnaieResource.class);

    private static final String ENTITY_NAME = "monnaie";

    @Value("${jhipster.clientApp.name}")
    private String applicationName;

    private final MonnaieService monnaieService;

    private final MonnaieRepository monnaieRepository;

    private final MonnaieQueryService monnaieQueryService;

    public MonnaieResource(MonnaieService monnaieService, MonnaieRepository monnaieRepository, MonnaieQueryService monnaieQueryService) {
        this.monnaieService = monnaieService;
        this.monnaieRepository = monnaieRepository;
        this.monnaieQueryService = monnaieQueryService;
    }

    /**
     * {@code POST  /monnaies} : Create a new monnaie.
     *
     * @param monnaie the monnaie to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new monnaie, or with status {@code 400 (Bad Request)} if the monnaie has already an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping("/monnaies")
    public ResponseEntity<Monnaie> createMonnaie(@Valid @RequestBody Monnaie monnaie) throws URISyntaxException {
        log.debug("REST request to save Monnaie : {}", monnaie);
        if (monnaie.getId() != null) {
            throw new BadRequestAlertException("A new monnaie cannot already have an ID", ENTITY_NAME, "idexists");
        }
        Monnaie result = monnaieService.save(monnaie);
        return ResponseEntity
            .created(new URI("/api/monnaies/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(applicationName, true, ENTITY_NAME, result.getId().toString()))
            .body(result);
    }

    /**
     * {@code PUT  /monnaies/:id} : Updates an existing monnaie.
     *
     * @param id the id of the monnaie to save.
     * @param monnaie the monnaie to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated monnaie,
     * or with status {@code 400 (Bad Request)} if the monnaie is not valid,
     * or with status {@code 500 (Internal Server Error)} if the monnaie couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PutMapping("/monnaies/{id}")
    public ResponseEntity<Monnaie> updateMonnaie(
        @PathVariable(value = "id", required = false) final Long id,
        @Valid @RequestBody Monnaie monnaie
    ) throws URISyntaxException {
        log.debug("REST request to update Monnaie : {}, {}", id, monnaie);
        if (monnaie.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, monnaie.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!monnaieRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        Monnaie result = monnaieService.update(monnaie);
        return ResponseEntity
            .ok()
            .headers(HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, monnaie.getId().toString()))
            .body(result);
    }

    /**
     * {@code PATCH  /monnaies/:id} : Partial updates given fields of an existing monnaie, field will ignore if it is null
     *
     * @param id the id of the monnaie to save.
     * @param monnaie the monnaie to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated monnaie,
     * or with status {@code 400 (Bad Request)} if the monnaie is not valid,
     * or with status {@code 404 (Not Found)} if the monnaie is not found,
     * or with status {@code 500 (Internal Server Error)} if the monnaie couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PatchMapping(value = "/monnaies/{id}", consumes = { "application/json", "application/merge-patch+json" })
    public ResponseEntity<Monnaie> partialUpdateMonnaie(
        @PathVariable(value = "id", required = false) final Long id,
        @NotNull @RequestBody Monnaie monnaie
    ) throws URISyntaxException {
        log.debug("REST request to partial update Monnaie partially : {}, {}", id, monnaie);
        if (monnaie.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, monnaie.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!monnaieRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        Optional<Monnaie> result = monnaieService.partialUpdate(monnaie);

        return ResponseUtil.wrapOrNotFound(
            result,
            HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, monnaie.getId().toString())
        );
    }

    /**
     * {@code GET  /monnaies} : get all the monnaies.
     *
     * @param pageable the pagination information.
     * @param criteria the criteria which the requested entities should match.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of monnaies in body.
     */
    @GetMapping("/monnaies")
    public ResponseEntity<List<Monnaie>> getAllMonnaies(
        MonnaieCriteria criteria,
        @org.springdoc.api.annotations.ParameterObject Pageable pageable
    ) {
        log.debug("REST request to get Monnaies by criteria: {}", criteria);
        Page<Monnaie> page = monnaieQueryService.findByCriteria(criteria, pageable);
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(ServletUriComponentsBuilder.fromCurrentRequest(), page);
        return ResponseEntity.ok().headers(headers).body(page.getContent());
    }

    /**
     * {@code GET  /monnaies/count} : count all the monnaies.
     *
     * @param criteria the criteria which the requested entities should match.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the count in body.
     */
    @GetMapping("/monnaies/count")
    public ResponseEntity<Long> countMonnaies(MonnaieCriteria criteria) {
        log.debug("REST request to count Monnaies by criteria: {}", criteria);
        return ResponseEntity.ok().body(monnaieQueryService.countByCriteria(criteria));
    }

    /**
     * {@code GET  /monnaies/:id} : get the "id" monnaie.
     *
     * @param id the id of the monnaie to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the monnaie, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/monnaies/{id}")
    public ResponseEntity<Monnaie> getMonnaie(@PathVariable Long id) {
        log.debug("REST request to get Monnaie : {}", id);
        Optional<Monnaie> monnaie = monnaieService.findOne(id);
        return ResponseUtil.wrapOrNotFound(monnaie);
    }

    /**
     * {@code DELETE  /monnaies/:id} : delete the "id" monnaie.
     *
     * @param id the id of the monnaie to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @DeleteMapping("/monnaies/{id}")
    public ResponseEntity<Void> deleteMonnaie(@PathVariable Long id) {
        log.debug("REST request to delete Monnaie : {}", id);
        monnaieService.delete(id);
        return ResponseEntity
            .noContent()
            .headers(HeaderUtil.createEntityDeletionAlert(applicationName, true, ENTITY_NAME, id.toString()))
            .build();
    }
}
