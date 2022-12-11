package com.avanquest.scibscrm.web.rest;

import com.avanquest.scibscrm.domain.TransactionCRM;
import com.avanquest.scibscrm.repository.TransactionCRMRepository;
import com.avanquest.scibscrm.service.TransactionCRMQueryService;
import com.avanquest.scibscrm.service.TransactionCRMService;
import com.avanquest.scibscrm.service.criteria.TransactionCRMCriteria;
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
 * REST controller for managing {@link com.avanquest.scibscrm.domain.TransactionCRM}.
 */
@RestController
@RequestMapping("/api")
public class TransactionCRMResource {

    private final Logger log = LoggerFactory.getLogger(TransactionCRMResource.class);

    private static final String ENTITY_NAME = "transactionCRM";

    @Value("${jhipster.clientApp.name}")
    private String applicationName;

    private final TransactionCRMService transactionCRMService;

    private final TransactionCRMRepository transactionCRMRepository;

    private final TransactionCRMQueryService transactionCRMQueryService;

    public TransactionCRMResource(
        TransactionCRMService transactionCRMService,
        TransactionCRMRepository transactionCRMRepository,
        TransactionCRMQueryService transactionCRMQueryService
    ) {
        this.transactionCRMService = transactionCRMService;
        this.transactionCRMRepository = transactionCRMRepository;
        this.transactionCRMQueryService = transactionCRMQueryService;
    }

    /**
     * {@code POST  /transaction-crms} : Create a new transactionCRM.
     *
     * @param transactionCRM the transactionCRM to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new transactionCRM, or with status {@code 400 (Bad Request)} if the transactionCRM has already an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping("/transaction-crms")
    public ResponseEntity<TransactionCRM> createTransactionCRM(@Valid @RequestBody TransactionCRM transactionCRM)
        throws URISyntaxException {
        log.debug("REST request to save TransactionCRM : {}", transactionCRM);
        if (transactionCRM.getId() != null) {
            throw new BadRequestAlertException("A new transactionCRM cannot already have an ID", ENTITY_NAME, "idexists");
        }
        TransactionCRM result = transactionCRMService.save(transactionCRM);
        return ResponseEntity
            .created(new URI("/api/transaction-crms/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(applicationName, true, ENTITY_NAME, result.getId().toString()))
            .body(result);
    }

    /**
     * {@code PUT  /transaction-crms/:id} : Updates an existing transactionCRM.
     *
     * @param id the id of the transactionCRM to save.
     * @param transactionCRM the transactionCRM to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated transactionCRM,
     * or with status {@code 400 (Bad Request)} if the transactionCRM is not valid,
     * or with status {@code 500 (Internal Server Error)} if the transactionCRM couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PutMapping("/transaction-crms/{id}")
    public ResponseEntity<TransactionCRM> updateTransactionCRM(
        @PathVariable(value = "id", required = false) final Long id,
        @Valid @RequestBody TransactionCRM transactionCRM
    ) throws URISyntaxException {
        log.debug("REST request to update TransactionCRM : {}, {}", id, transactionCRM);
        if (transactionCRM.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, transactionCRM.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!transactionCRMRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        TransactionCRM result = transactionCRMService.update(transactionCRM);
        return ResponseEntity
            .ok()
            .headers(HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, transactionCRM.getId().toString()))
            .body(result);
    }

    /**
     * {@code PATCH  /transaction-crms/:id} : Partial updates given fields of an existing transactionCRM, field will ignore if it is null
     *
     * @param id the id of the transactionCRM to save.
     * @param transactionCRM the transactionCRM to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated transactionCRM,
     * or with status {@code 400 (Bad Request)} if the transactionCRM is not valid,
     * or with status {@code 404 (Not Found)} if the transactionCRM is not found,
     * or with status {@code 500 (Internal Server Error)} if the transactionCRM couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PatchMapping(value = "/transaction-crms/{id}", consumes = { "application/json", "application/merge-patch+json" })
    public ResponseEntity<TransactionCRM> partialUpdateTransactionCRM(
        @PathVariable(value = "id", required = false) final Long id,
        @NotNull @RequestBody TransactionCRM transactionCRM
    ) throws URISyntaxException {
        log.debug("REST request to partial update TransactionCRM partially : {}, {}", id, transactionCRM);
        if (transactionCRM.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, transactionCRM.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!transactionCRMRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        Optional<TransactionCRM> result = transactionCRMService.partialUpdate(transactionCRM);

        return ResponseUtil.wrapOrNotFound(
            result,
            HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, transactionCRM.getId().toString())
        );
    }

    /**
     * {@code GET  /transaction-crms} : get all the transactionCRMS.
     *
     * @param pageable the pagination information.
     * @param criteria the criteria which the requested entities should match.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of transactionCRMS in body.
     */
    @GetMapping("/transaction-crms")
    public ResponseEntity<List<TransactionCRM>> getAllTransactionCRMS(
        TransactionCRMCriteria criteria,
        @org.springdoc.api.annotations.ParameterObject Pageable pageable
    ) {
        log.debug("REST request to get TransactionCRMS by criteria: {}", criteria);
        Page<TransactionCRM> page = transactionCRMQueryService.findByCriteria(criteria, pageable);
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(ServletUriComponentsBuilder.fromCurrentRequest(), page);
        return ResponseEntity.ok().headers(headers).body(page.getContent());
    }

    /**
     * {@code GET  /transaction-crms/count} : count all the transactionCRMS.
     *
     * @param criteria the criteria which the requested entities should match.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the count in body.
     */
    @GetMapping("/transaction-crms/count")
    public ResponseEntity<Long> countTransactionCRMS(TransactionCRMCriteria criteria) {
        log.debug("REST request to count TransactionCRMS by criteria: {}", criteria);
        return ResponseEntity.ok().body(transactionCRMQueryService.countByCriteria(criteria));
    }

    /**
     * {@code GET  /transaction-crms/:id} : get the "id" transactionCRM.
     *
     * @param id the id of the transactionCRM to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the transactionCRM, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/transaction-crms/{id}")
    public ResponseEntity<TransactionCRM> getTransactionCRM(@PathVariable Long id) {
        log.debug("REST request to get TransactionCRM : {}", id);
        Optional<TransactionCRM> transactionCRM = transactionCRMService.findOne(id);
        return ResponseUtil.wrapOrNotFound(transactionCRM);
    }

    /**
     * {@code DELETE  /transaction-crms/:id} : delete the "id" transactionCRM.
     *
     * @param id the id of the transactionCRM to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @DeleteMapping("/transaction-crms/{id}")
    public ResponseEntity<Void> deleteTransactionCRM(@PathVariable Long id) {
        log.debug("REST request to delete TransactionCRM : {}", id);
        transactionCRMService.delete(id);
        return ResponseEntity
            .noContent()
            .headers(HeaderUtil.createEntityDeletionAlert(applicationName, true, ENTITY_NAME, id.toString()))
            .build();
    }
}
