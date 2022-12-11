package com.avanquest.scibscrm.web.rest;

import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import com.avanquest.scibscrm.IntegrationTest;
import com.avanquest.scibscrm.domain.Monnaie;
import com.avanquest.scibscrm.repository.MonnaieRepository;
import com.avanquest.scibscrm.service.criteria.MonnaieCriteria;
import java.util.List;
import java.util.Random;
import java.util.concurrent.atomic.AtomicLong;
import javax.persistence.EntityManager;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.transaction.annotation.Transactional;

/**
 * Integration tests for the {@link MonnaieResource} REST controller.
 */
@IntegrationTest
@AutoConfigureMockMvc
@WithMockUser
class MonnaieResourceIT {

    private static final String DEFAULT_MONEY_NAME = "AAAAAAAAAA";
    private static final String UPDATED_MONEY_NAME = "BBBBBBBBBB";

    private static final String DEFAULT_MONEY_ISOCODE = "AAAAAAAAAA";
    private static final String UPDATED_MONEY_ISOCODE = "BBBBBBBBBB";

    private static final String ENTITY_API_URL = "/api/monnaies";
    private static final String ENTITY_API_URL_ID = ENTITY_API_URL + "/{id}";

    private static Random random = new Random();
    private static AtomicLong count = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    @Autowired
    private MonnaieRepository monnaieRepository;

    @Autowired
    private EntityManager em;

    @Autowired
    private MockMvc restMonnaieMockMvc;

    private Monnaie monnaie;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Monnaie createEntity(EntityManager em) {
        Monnaie monnaie = new Monnaie().moneyName(DEFAULT_MONEY_NAME).moneyIsocode(DEFAULT_MONEY_ISOCODE);
        return monnaie;
    }

    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Monnaie createUpdatedEntity(EntityManager em) {
        Monnaie monnaie = new Monnaie().moneyName(UPDATED_MONEY_NAME).moneyIsocode(UPDATED_MONEY_ISOCODE);
        return monnaie;
    }

    @BeforeEach
    public void initTest() {
        monnaie = createEntity(em);
    }

    @Test
    @Transactional
    void createMonnaie() throws Exception {
        int databaseSizeBeforeCreate = monnaieRepository.findAll().size();
        // Create the Monnaie
        restMonnaieMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(monnaie)))
            .andExpect(status().isCreated());

        // Validate the Monnaie in the database
        List<Monnaie> monnaieList = monnaieRepository.findAll();
        assertThat(monnaieList).hasSize(databaseSizeBeforeCreate + 1);
        Monnaie testMonnaie = monnaieList.get(monnaieList.size() - 1);
        assertThat(testMonnaie.getMoneyName()).isEqualTo(DEFAULT_MONEY_NAME);
        assertThat(testMonnaie.getMoneyIsocode()).isEqualTo(DEFAULT_MONEY_ISOCODE);
    }

    @Test
    @Transactional
    void createMonnaieWithExistingId() throws Exception {
        // Create the Monnaie with an existing ID
        monnaie.setId(1L);

        int databaseSizeBeforeCreate = monnaieRepository.findAll().size();

        // An entity with an existing ID cannot be created, so this API call must fail
        restMonnaieMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(monnaie)))
            .andExpect(status().isBadRequest());

        // Validate the Monnaie in the database
        List<Monnaie> monnaieList = monnaieRepository.findAll();
        assertThat(monnaieList).hasSize(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    void getAllMonnaies() throws Exception {
        // Initialize the database
        monnaieRepository.saveAndFlush(monnaie);

        // Get all the monnaieList
        restMonnaieMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(monnaie.getId().intValue())))
            .andExpect(jsonPath("$.[*].moneyName").value(hasItem(DEFAULT_MONEY_NAME)))
            .andExpect(jsonPath("$.[*].moneyIsocode").value(hasItem(DEFAULT_MONEY_ISOCODE)));
    }

    @Test
    @Transactional
    void getMonnaie() throws Exception {
        // Initialize the database
        monnaieRepository.saveAndFlush(monnaie);

        // Get the monnaie
        restMonnaieMockMvc
            .perform(get(ENTITY_API_URL_ID, monnaie.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.id").value(monnaie.getId().intValue()))
            .andExpect(jsonPath("$.moneyName").value(DEFAULT_MONEY_NAME))
            .andExpect(jsonPath("$.moneyIsocode").value(DEFAULT_MONEY_ISOCODE));
    }

    @Test
    @Transactional
    void getMonnaiesByIdFiltering() throws Exception {
        // Initialize the database
        monnaieRepository.saveAndFlush(monnaie);

        Long id = monnaie.getId();

        defaultMonnaieShouldBeFound("id.equals=" + id);
        defaultMonnaieShouldNotBeFound("id.notEquals=" + id);

        defaultMonnaieShouldBeFound("id.greaterThanOrEqual=" + id);
        defaultMonnaieShouldNotBeFound("id.greaterThan=" + id);

        defaultMonnaieShouldBeFound("id.lessThanOrEqual=" + id);
        defaultMonnaieShouldNotBeFound("id.lessThan=" + id);
    }

    @Test
    @Transactional
    void getAllMonnaiesByMoneyNameIsEqualToSomething() throws Exception {
        // Initialize the database
        monnaieRepository.saveAndFlush(monnaie);

        // Get all the monnaieList where moneyName equals to DEFAULT_MONEY_NAME
        defaultMonnaieShouldBeFound("moneyName.equals=" + DEFAULT_MONEY_NAME);

        // Get all the monnaieList where moneyName equals to UPDATED_MONEY_NAME
        defaultMonnaieShouldNotBeFound("moneyName.equals=" + UPDATED_MONEY_NAME);
    }

    @Test
    @Transactional
    void getAllMonnaiesByMoneyNameIsNotEqualToSomething() throws Exception {
        // Initialize the database
        monnaieRepository.saveAndFlush(monnaie);

        // Get all the monnaieList where moneyName not equals to DEFAULT_MONEY_NAME
        defaultMonnaieShouldNotBeFound("moneyName.notEquals=" + DEFAULT_MONEY_NAME);

        // Get all the monnaieList where moneyName not equals to UPDATED_MONEY_NAME
        defaultMonnaieShouldBeFound("moneyName.notEquals=" + UPDATED_MONEY_NAME);
    }

    @Test
    @Transactional
    void getAllMonnaiesByMoneyNameIsInShouldWork() throws Exception {
        // Initialize the database
        monnaieRepository.saveAndFlush(monnaie);

        // Get all the monnaieList where moneyName in DEFAULT_MONEY_NAME or UPDATED_MONEY_NAME
        defaultMonnaieShouldBeFound("moneyName.in=" + DEFAULT_MONEY_NAME + "," + UPDATED_MONEY_NAME);

        // Get all the monnaieList where moneyName equals to UPDATED_MONEY_NAME
        defaultMonnaieShouldNotBeFound("moneyName.in=" + UPDATED_MONEY_NAME);
    }

    @Test
    @Transactional
    void getAllMonnaiesByMoneyNameIsNullOrNotNull() throws Exception {
        // Initialize the database
        monnaieRepository.saveAndFlush(monnaie);

        // Get all the monnaieList where moneyName is not null
        defaultMonnaieShouldBeFound("moneyName.specified=true");

        // Get all the monnaieList where moneyName is null
        defaultMonnaieShouldNotBeFound("moneyName.specified=false");
    }

    @Test
    @Transactional
    void getAllMonnaiesByMoneyNameContainsSomething() throws Exception {
        // Initialize the database
        monnaieRepository.saveAndFlush(monnaie);

        // Get all the monnaieList where moneyName contains DEFAULT_MONEY_NAME
        defaultMonnaieShouldBeFound("moneyName.contains=" + DEFAULT_MONEY_NAME);

        // Get all the monnaieList where moneyName contains UPDATED_MONEY_NAME
        defaultMonnaieShouldNotBeFound("moneyName.contains=" + UPDATED_MONEY_NAME);
    }

    @Test
    @Transactional
    void getAllMonnaiesByMoneyNameNotContainsSomething() throws Exception {
        // Initialize the database
        monnaieRepository.saveAndFlush(monnaie);

        // Get all the monnaieList where moneyName does not contain DEFAULT_MONEY_NAME
        defaultMonnaieShouldNotBeFound("moneyName.doesNotContain=" + DEFAULT_MONEY_NAME);

        // Get all the monnaieList where moneyName does not contain UPDATED_MONEY_NAME
        defaultMonnaieShouldBeFound("moneyName.doesNotContain=" + UPDATED_MONEY_NAME);
    }

    @Test
    @Transactional
    void getAllMonnaiesByMoneyIsocodeIsEqualToSomething() throws Exception {
        // Initialize the database
        monnaieRepository.saveAndFlush(monnaie);

        // Get all the monnaieList where moneyIsocode equals to DEFAULT_MONEY_ISOCODE
        defaultMonnaieShouldBeFound("moneyIsocode.equals=" + DEFAULT_MONEY_ISOCODE);

        // Get all the monnaieList where moneyIsocode equals to UPDATED_MONEY_ISOCODE
        defaultMonnaieShouldNotBeFound("moneyIsocode.equals=" + UPDATED_MONEY_ISOCODE);
    }

    @Test
    @Transactional
    void getAllMonnaiesByMoneyIsocodeIsNotEqualToSomething() throws Exception {
        // Initialize the database
        monnaieRepository.saveAndFlush(monnaie);

        // Get all the monnaieList where moneyIsocode not equals to DEFAULT_MONEY_ISOCODE
        defaultMonnaieShouldNotBeFound("moneyIsocode.notEquals=" + DEFAULT_MONEY_ISOCODE);

        // Get all the monnaieList where moneyIsocode not equals to UPDATED_MONEY_ISOCODE
        defaultMonnaieShouldBeFound("moneyIsocode.notEquals=" + UPDATED_MONEY_ISOCODE);
    }

    @Test
    @Transactional
    void getAllMonnaiesByMoneyIsocodeIsInShouldWork() throws Exception {
        // Initialize the database
        monnaieRepository.saveAndFlush(monnaie);

        // Get all the monnaieList where moneyIsocode in DEFAULT_MONEY_ISOCODE or UPDATED_MONEY_ISOCODE
        defaultMonnaieShouldBeFound("moneyIsocode.in=" + DEFAULT_MONEY_ISOCODE + "," + UPDATED_MONEY_ISOCODE);

        // Get all the monnaieList where moneyIsocode equals to UPDATED_MONEY_ISOCODE
        defaultMonnaieShouldNotBeFound("moneyIsocode.in=" + UPDATED_MONEY_ISOCODE);
    }

    @Test
    @Transactional
    void getAllMonnaiesByMoneyIsocodeIsNullOrNotNull() throws Exception {
        // Initialize the database
        monnaieRepository.saveAndFlush(monnaie);

        // Get all the monnaieList where moneyIsocode is not null
        defaultMonnaieShouldBeFound("moneyIsocode.specified=true");

        // Get all the monnaieList where moneyIsocode is null
        defaultMonnaieShouldNotBeFound("moneyIsocode.specified=false");
    }

    @Test
    @Transactional
    void getAllMonnaiesByMoneyIsocodeContainsSomething() throws Exception {
        // Initialize the database
        monnaieRepository.saveAndFlush(monnaie);

        // Get all the monnaieList where moneyIsocode contains DEFAULT_MONEY_ISOCODE
        defaultMonnaieShouldBeFound("moneyIsocode.contains=" + DEFAULT_MONEY_ISOCODE);

        // Get all the monnaieList where moneyIsocode contains UPDATED_MONEY_ISOCODE
        defaultMonnaieShouldNotBeFound("moneyIsocode.contains=" + UPDATED_MONEY_ISOCODE);
    }

    @Test
    @Transactional
    void getAllMonnaiesByMoneyIsocodeNotContainsSomething() throws Exception {
        // Initialize the database
        monnaieRepository.saveAndFlush(monnaie);

        // Get all the monnaieList where moneyIsocode does not contain DEFAULT_MONEY_ISOCODE
        defaultMonnaieShouldNotBeFound("moneyIsocode.doesNotContain=" + DEFAULT_MONEY_ISOCODE);

        // Get all the monnaieList where moneyIsocode does not contain UPDATED_MONEY_ISOCODE
        defaultMonnaieShouldBeFound("moneyIsocode.doesNotContain=" + UPDATED_MONEY_ISOCODE);
    }

    /**
     * Executes the search, and checks that the default entity is returned.
     */
    private void defaultMonnaieShouldBeFound(String filter) throws Exception {
        restMonnaieMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(monnaie.getId().intValue())))
            .andExpect(jsonPath("$.[*].moneyName").value(hasItem(DEFAULT_MONEY_NAME)))
            .andExpect(jsonPath("$.[*].moneyIsocode").value(hasItem(DEFAULT_MONEY_ISOCODE)));

        // Check, that the count call also returns 1
        restMonnaieMockMvc
            .perform(get(ENTITY_API_URL + "/count?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(content().string("1"));
    }

    /**
     * Executes the search, and checks that the default entity is not returned.
     */
    private void defaultMonnaieShouldNotBeFound(String filter) throws Exception {
        restMonnaieMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$").isArray())
            .andExpect(jsonPath("$").isEmpty());

        // Check, that the count call also returns 0
        restMonnaieMockMvc
            .perform(get(ENTITY_API_URL + "/count?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(content().string("0"));
    }

    @Test
    @Transactional
    void getNonExistingMonnaie() throws Exception {
        // Get the monnaie
        restMonnaieMockMvc.perform(get(ENTITY_API_URL_ID, Long.MAX_VALUE)).andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    void putNewMonnaie() throws Exception {
        // Initialize the database
        monnaieRepository.saveAndFlush(monnaie);

        int databaseSizeBeforeUpdate = monnaieRepository.findAll().size();

        // Update the monnaie
        Monnaie updatedMonnaie = monnaieRepository.findById(monnaie.getId()).get();
        // Disconnect from session so that the updates on updatedMonnaie are not directly saved in db
        em.detach(updatedMonnaie);
        updatedMonnaie.moneyName(UPDATED_MONEY_NAME).moneyIsocode(UPDATED_MONEY_ISOCODE);

        restMonnaieMockMvc
            .perform(
                put(ENTITY_API_URL_ID, updatedMonnaie.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(updatedMonnaie))
            )
            .andExpect(status().isOk());

        // Validate the Monnaie in the database
        List<Monnaie> monnaieList = monnaieRepository.findAll();
        assertThat(monnaieList).hasSize(databaseSizeBeforeUpdate);
        Monnaie testMonnaie = monnaieList.get(monnaieList.size() - 1);
        assertThat(testMonnaie.getMoneyName()).isEqualTo(UPDATED_MONEY_NAME);
        assertThat(testMonnaie.getMoneyIsocode()).isEqualTo(UPDATED_MONEY_ISOCODE);
    }

    @Test
    @Transactional
    void putNonExistingMonnaie() throws Exception {
        int databaseSizeBeforeUpdate = monnaieRepository.findAll().size();
        monnaie.setId(count.incrementAndGet());

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restMonnaieMockMvc
            .perform(
                put(ENTITY_API_URL_ID, monnaie.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(monnaie))
            )
            .andExpect(status().isBadRequest());

        // Validate the Monnaie in the database
        List<Monnaie> monnaieList = monnaieRepository.findAll();
        assertThat(monnaieList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithIdMismatchMonnaie() throws Exception {
        int databaseSizeBeforeUpdate = monnaieRepository.findAll().size();
        monnaie.setId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restMonnaieMockMvc
            .perform(
                put(ENTITY_API_URL_ID, count.incrementAndGet())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(monnaie))
            )
            .andExpect(status().isBadRequest());

        // Validate the Monnaie in the database
        List<Monnaie> monnaieList = monnaieRepository.findAll();
        assertThat(monnaieList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithMissingIdPathParamMonnaie() throws Exception {
        int databaseSizeBeforeUpdate = monnaieRepository.findAll().size();
        monnaie.setId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restMonnaieMockMvc
            .perform(put(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(monnaie)))
            .andExpect(status().isMethodNotAllowed());

        // Validate the Monnaie in the database
        List<Monnaie> monnaieList = monnaieRepository.findAll();
        assertThat(monnaieList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void partialUpdateMonnaieWithPatch() throws Exception {
        // Initialize the database
        monnaieRepository.saveAndFlush(monnaie);

        int databaseSizeBeforeUpdate = monnaieRepository.findAll().size();

        // Update the monnaie using partial update
        Monnaie partialUpdatedMonnaie = new Monnaie();
        partialUpdatedMonnaie.setId(monnaie.getId());

        partialUpdatedMonnaie.moneyName(UPDATED_MONEY_NAME).moneyIsocode(UPDATED_MONEY_ISOCODE);

        restMonnaieMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedMonnaie.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedMonnaie))
            )
            .andExpect(status().isOk());

        // Validate the Monnaie in the database
        List<Monnaie> monnaieList = monnaieRepository.findAll();
        assertThat(monnaieList).hasSize(databaseSizeBeforeUpdate);
        Monnaie testMonnaie = monnaieList.get(monnaieList.size() - 1);
        assertThat(testMonnaie.getMoneyName()).isEqualTo(UPDATED_MONEY_NAME);
        assertThat(testMonnaie.getMoneyIsocode()).isEqualTo(UPDATED_MONEY_ISOCODE);
    }

    @Test
    @Transactional
    void fullUpdateMonnaieWithPatch() throws Exception {
        // Initialize the database
        monnaieRepository.saveAndFlush(monnaie);

        int databaseSizeBeforeUpdate = monnaieRepository.findAll().size();

        // Update the monnaie using partial update
        Monnaie partialUpdatedMonnaie = new Monnaie();
        partialUpdatedMonnaie.setId(monnaie.getId());

        partialUpdatedMonnaie.moneyName(UPDATED_MONEY_NAME).moneyIsocode(UPDATED_MONEY_ISOCODE);

        restMonnaieMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedMonnaie.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedMonnaie))
            )
            .andExpect(status().isOk());

        // Validate the Monnaie in the database
        List<Monnaie> monnaieList = monnaieRepository.findAll();
        assertThat(monnaieList).hasSize(databaseSizeBeforeUpdate);
        Monnaie testMonnaie = monnaieList.get(monnaieList.size() - 1);
        assertThat(testMonnaie.getMoneyName()).isEqualTo(UPDATED_MONEY_NAME);
        assertThat(testMonnaie.getMoneyIsocode()).isEqualTo(UPDATED_MONEY_ISOCODE);
    }

    @Test
    @Transactional
    void patchNonExistingMonnaie() throws Exception {
        int databaseSizeBeforeUpdate = monnaieRepository.findAll().size();
        monnaie.setId(count.incrementAndGet());

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restMonnaieMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, monnaie.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(monnaie))
            )
            .andExpect(status().isBadRequest());

        // Validate the Monnaie in the database
        List<Monnaie> monnaieList = monnaieRepository.findAll();
        assertThat(monnaieList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithIdMismatchMonnaie() throws Exception {
        int databaseSizeBeforeUpdate = monnaieRepository.findAll().size();
        monnaie.setId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restMonnaieMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, count.incrementAndGet())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(monnaie))
            )
            .andExpect(status().isBadRequest());

        // Validate the Monnaie in the database
        List<Monnaie> monnaieList = monnaieRepository.findAll();
        assertThat(monnaieList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithMissingIdPathParamMonnaie() throws Exception {
        int databaseSizeBeforeUpdate = monnaieRepository.findAll().size();
        monnaie.setId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restMonnaieMockMvc
            .perform(patch(ENTITY_API_URL).contentType("application/merge-patch+json").content(TestUtil.convertObjectToJsonBytes(monnaie)))
            .andExpect(status().isMethodNotAllowed());

        // Validate the Monnaie in the database
        List<Monnaie> monnaieList = monnaieRepository.findAll();
        assertThat(monnaieList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void deleteMonnaie() throws Exception {
        // Initialize the database
        monnaieRepository.saveAndFlush(monnaie);

        int databaseSizeBeforeDelete = monnaieRepository.findAll().size();

        // Delete the monnaie
        restMonnaieMockMvc
            .perform(delete(ENTITY_API_URL_ID, monnaie.getId()).accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        List<Monnaie> monnaieList = monnaieRepository.findAll();
        assertThat(monnaieList).hasSize(databaseSizeBeforeDelete - 1);
    }
}
