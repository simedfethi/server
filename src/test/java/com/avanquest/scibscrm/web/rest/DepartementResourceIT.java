package com.avanquest.scibscrm.web.rest;

import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import com.avanquest.scibscrm.IntegrationTest;
import com.avanquest.scibscrm.domain.Company;
import com.avanquest.scibscrm.domain.Departement;
import com.avanquest.scibscrm.repository.DepartementRepository;
import com.avanquest.scibscrm.service.DepartementService;
import com.avanquest.scibscrm.service.criteria.DepartementCriteria;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import java.util.concurrent.atomic.AtomicLong;
import javax.persistence.EntityManager;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.transaction.annotation.Transactional;

/**
 * Integration tests for the {@link DepartementResource} REST controller.
 */
@IntegrationTest
@ExtendWith(MockitoExtension.class)
@AutoConfigureMockMvc
@WithMockUser
class DepartementResourceIT {

    private static final String DEFAULT_DEPARTMENT_NAME = "AAAAAAAAAA";
    private static final String UPDATED_DEPARTMENT_NAME = "BBBBBBBBBB";

    private static final String DEFAULT_DEPARTMENT_CODE = "AAAAAAAAAA";
    private static final String UPDATED_DEPARTMENT_CODE = "BBBBBBBBBB";

    private static final String ENTITY_API_URL = "/api/departements";
    private static final String ENTITY_API_URL_ID = ENTITY_API_URL + "/{id}";

    private static Random random = new Random();
    private static AtomicLong count = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    @Autowired
    private DepartementRepository departementRepository;

    @Mock
    private DepartementRepository departementRepositoryMock;

    @Mock
    private DepartementService departementServiceMock;

    @Autowired
    private EntityManager em;

    @Autowired
    private MockMvc restDepartementMockMvc;

    private Departement departement;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Departement createEntity(EntityManager em) {
        Departement departement = new Departement().departmentName(DEFAULT_DEPARTMENT_NAME).departmentCode(DEFAULT_DEPARTMENT_CODE);
        // Add required entity
        Company company;
        if (TestUtil.findAll(em, Company.class).isEmpty()) {
            company = CompanyResourceIT.createEntity(em);
            em.persist(company);
            em.flush();
        } else {
            company = TestUtil.findAll(em, Company.class).get(0);
        }
        departement.setEntreprise(company);
        return departement;
    }

    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Departement createUpdatedEntity(EntityManager em) {
        Departement departement = new Departement().departmentName(UPDATED_DEPARTMENT_NAME).departmentCode(UPDATED_DEPARTMENT_CODE);
        // Add required entity
        Company company;
        if (TestUtil.findAll(em, Company.class).isEmpty()) {
            company = CompanyResourceIT.createUpdatedEntity(em);
            em.persist(company);
            em.flush();
        } else {
            company = TestUtil.findAll(em, Company.class).get(0);
        }
        departement.setEntreprise(company);
        return departement;
    }

    @BeforeEach
    public void initTest() {
        departement = createEntity(em);
    }

    @Test
    @Transactional
    void createDepartement() throws Exception {
        int databaseSizeBeforeCreate = departementRepository.findAll().size();
        // Create the Departement
        restDepartementMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(departement)))
            .andExpect(status().isCreated());

        // Validate the Departement in the database
        List<Departement> departementList = departementRepository.findAll();
        assertThat(departementList).hasSize(databaseSizeBeforeCreate + 1);
        Departement testDepartement = departementList.get(departementList.size() - 1);
        assertThat(testDepartement.getDepartmentName()).isEqualTo(DEFAULT_DEPARTMENT_NAME);
        assertThat(testDepartement.getDepartmentCode()).isEqualTo(DEFAULT_DEPARTMENT_CODE);
    }

    @Test
    @Transactional
    void createDepartementWithExistingId() throws Exception {
        // Create the Departement with an existing ID
        departement.setId(1L);

        int databaseSizeBeforeCreate = departementRepository.findAll().size();

        // An entity with an existing ID cannot be created, so this API call must fail
        restDepartementMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(departement)))
            .andExpect(status().isBadRequest());

        // Validate the Departement in the database
        List<Departement> departementList = departementRepository.findAll();
        assertThat(departementList).hasSize(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    void getAllDepartements() throws Exception {
        // Initialize the database
        departementRepository.saveAndFlush(departement);

        // Get all the departementList
        restDepartementMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(departement.getId().intValue())))
            .andExpect(jsonPath("$.[*].departmentName").value(hasItem(DEFAULT_DEPARTMENT_NAME)))
            .andExpect(jsonPath("$.[*].departmentCode").value(hasItem(DEFAULT_DEPARTMENT_CODE)));
    }

    @SuppressWarnings({ "unchecked" })
    void getAllDepartementsWithEagerRelationshipsIsEnabled() throws Exception {
        when(departementServiceMock.findAllWithEagerRelationships(any())).thenReturn(new PageImpl(new ArrayList<>()));

        restDepartementMockMvc.perform(get(ENTITY_API_URL + "?eagerload=true")).andExpect(status().isOk());

        verify(departementServiceMock, times(1)).findAllWithEagerRelationships(any());
    }

    @SuppressWarnings({ "unchecked" })
    void getAllDepartementsWithEagerRelationshipsIsNotEnabled() throws Exception {
        when(departementServiceMock.findAllWithEagerRelationships(any())).thenReturn(new PageImpl(new ArrayList<>()));

        restDepartementMockMvc.perform(get(ENTITY_API_URL + "?eagerload=true")).andExpect(status().isOk());

        verify(departementServiceMock, times(1)).findAllWithEagerRelationships(any());
    }

    @Test
    @Transactional
    void getDepartement() throws Exception {
        // Initialize the database
        departementRepository.saveAndFlush(departement);

        // Get the departement
        restDepartementMockMvc
            .perform(get(ENTITY_API_URL_ID, departement.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.id").value(departement.getId().intValue()))
            .andExpect(jsonPath("$.departmentName").value(DEFAULT_DEPARTMENT_NAME))
            .andExpect(jsonPath("$.departmentCode").value(DEFAULT_DEPARTMENT_CODE));
    }

    @Test
    @Transactional
    void getDepartementsByIdFiltering() throws Exception {
        // Initialize the database
        departementRepository.saveAndFlush(departement);

        Long id = departement.getId();

        defaultDepartementShouldBeFound("id.equals=" + id);
        defaultDepartementShouldNotBeFound("id.notEquals=" + id);

        defaultDepartementShouldBeFound("id.greaterThanOrEqual=" + id);
        defaultDepartementShouldNotBeFound("id.greaterThan=" + id);

        defaultDepartementShouldBeFound("id.lessThanOrEqual=" + id);
        defaultDepartementShouldNotBeFound("id.lessThan=" + id);
    }

    @Test
    @Transactional
    void getAllDepartementsByDepartmentNameIsEqualToSomething() throws Exception {
        // Initialize the database
        departementRepository.saveAndFlush(departement);

        // Get all the departementList where departmentName equals to DEFAULT_DEPARTMENT_NAME
        defaultDepartementShouldBeFound("departmentName.equals=" + DEFAULT_DEPARTMENT_NAME);

        // Get all the departementList where departmentName equals to UPDATED_DEPARTMENT_NAME
        defaultDepartementShouldNotBeFound("departmentName.equals=" + UPDATED_DEPARTMENT_NAME);
    }

    @Test
    @Transactional
    void getAllDepartementsByDepartmentNameIsNotEqualToSomething() throws Exception {
        // Initialize the database
        departementRepository.saveAndFlush(departement);

        // Get all the departementList where departmentName not equals to DEFAULT_DEPARTMENT_NAME
        defaultDepartementShouldNotBeFound("departmentName.notEquals=" + DEFAULT_DEPARTMENT_NAME);

        // Get all the departementList where departmentName not equals to UPDATED_DEPARTMENT_NAME
        defaultDepartementShouldBeFound("departmentName.notEquals=" + UPDATED_DEPARTMENT_NAME);
    }

    @Test
    @Transactional
    void getAllDepartementsByDepartmentNameIsInShouldWork() throws Exception {
        // Initialize the database
        departementRepository.saveAndFlush(departement);

        // Get all the departementList where departmentName in DEFAULT_DEPARTMENT_NAME or UPDATED_DEPARTMENT_NAME
        defaultDepartementShouldBeFound("departmentName.in=" + DEFAULT_DEPARTMENT_NAME + "," + UPDATED_DEPARTMENT_NAME);

        // Get all the departementList where departmentName equals to UPDATED_DEPARTMENT_NAME
        defaultDepartementShouldNotBeFound("departmentName.in=" + UPDATED_DEPARTMENT_NAME);
    }

    @Test
    @Transactional
    void getAllDepartementsByDepartmentNameIsNullOrNotNull() throws Exception {
        // Initialize the database
        departementRepository.saveAndFlush(departement);

        // Get all the departementList where departmentName is not null
        defaultDepartementShouldBeFound("departmentName.specified=true");

        // Get all the departementList where departmentName is null
        defaultDepartementShouldNotBeFound("departmentName.specified=false");
    }

    @Test
    @Transactional
    void getAllDepartementsByDepartmentNameContainsSomething() throws Exception {
        // Initialize the database
        departementRepository.saveAndFlush(departement);

        // Get all the departementList where departmentName contains DEFAULT_DEPARTMENT_NAME
        defaultDepartementShouldBeFound("departmentName.contains=" + DEFAULT_DEPARTMENT_NAME);

        // Get all the departementList where departmentName contains UPDATED_DEPARTMENT_NAME
        defaultDepartementShouldNotBeFound("departmentName.contains=" + UPDATED_DEPARTMENT_NAME);
    }

    @Test
    @Transactional
    void getAllDepartementsByDepartmentNameNotContainsSomething() throws Exception {
        // Initialize the database
        departementRepository.saveAndFlush(departement);

        // Get all the departementList where departmentName does not contain DEFAULT_DEPARTMENT_NAME
        defaultDepartementShouldNotBeFound("departmentName.doesNotContain=" + DEFAULT_DEPARTMENT_NAME);

        // Get all the departementList where departmentName does not contain UPDATED_DEPARTMENT_NAME
        defaultDepartementShouldBeFound("departmentName.doesNotContain=" + UPDATED_DEPARTMENT_NAME);
    }

    @Test
    @Transactional
    void getAllDepartementsByDepartmentCodeIsEqualToSomething() throws Exception {
        // Initialize the database
        departementRepository.saveAndFlush(departement);

        // Get all the departementList where departmentCode equals to DEFAULT_DEPARTMENT_CODE
        defaultDepartementShouldBeFound("departmentCode.equals=" + DEFAULT_DEPARTMENT_CODE);

        // Get all the departementList where departmentCode equals to UPDATED_DEPARTMENT_CODE
        defaultDepartementShouldNotBeFound("departmentCode.equals=" + UPDATED_DEPARTMENT_CODE);
    }

    @Test
    @Transactional
    void getAllDepartementsByDepartmentCodeIsNotEqualToSomething() throws Exception {
        // Initialize the database
        departementRepository.saveAndFlush(departement);

        // Get all the departementList where departmentCode not equals to DEFAULT_DEPARTMENT_CODE
        defaultDepartementShouldNotBeFound("departmentCode.notEquals=" + DEFAULT_DEPARTMENT_CODE);

        // Get all the departementList where departmentCode not equals to UPDATED_DEPARTMENT_CODE
        defaultDepartementShouldBeFound("departmentCode.notEquals=" + UPDATED_DEPARTMENT_CODE);
    }

    @Test
    @Transactional
    void getAllDepartementsByDepartmentCodeIsInShouldWork() throws Exception {
        // Initialize the database
        departementRepository.saveAndFlush(departement);

        // Get all the departementList where departmentCode in DEFAULT_DEPARTMENT_CODE or UPDATED_DEPARTMENT_CODE
        defaultDepartementShouldBeFound("departmentCode.in=" + DEFAULT_DEPARTMENT_CODE + "," + UPDATED_DEPARTMENT_CODE);

        // Get all the departementList where departmentCode equals to UPDATED_DEPARTMENT_CODE
        defaultDepartementShouldNotBeFound("departmentCode.in=" + UPDATED_DEPARTMENT_CODE);
    }

    @Test
    @Transactional
    void getAllDepartementsByDepartmentCodeIsNullOrNotNull() throws Exception {
        // Initialize the database
        departementRepository.saveAndFlush(departement);

        // Get all the departementList where departmentCode is not null
        defaultDepartementShouldBeFound("departmentCode.specified=true");

        // Get all the departementList where departmentCode is null
        defaultDepartementShouldNotBeFound("departmentCode.specified=false");
    }

    @Test
    @Transactional
    void getAllDepartementsByDepartmentCodeContainsSomething() throws Exception {
        // Initialize the database
        departementRepository.saveAndFlush(departement);

        // Get all the departementList where departmentCode contains DEFAULT_DEPARTMENT_CODE
        defaultDepartementShouldBeFound("departmentCode.contains=" + DEFAULT_DEPARTMENT_CODE);

        // Get all the departementList where departmentCode contains UPDATED_DEPARTMENT_CODE
        defaultDepartementShouldNotBeFound("departmentCode.contains=" + UPDATED_DEPARTMENT_CODE);
    }

    @Test
    @Transactional
    void getAllDepartementsByDepartmentCodeNotContainsSomething() throws Exception {
        // Initialize the database
        departementRepository.saveAndFlush(departement);

        // Get all the departementList where departmentCode does not contain DEFAULT_DEPARTMENT_CODE
        defaultDepartementShouldNotBeFound("departmentCode.doesNotContain=" + DEFAULT_DEPARTMENT_CODE);

        // Get all the departementList where departmentCode does not contain UPDATED_DEPARTMENT_CODE
        defaultDepartementShouldBeFound("departmentCode.doesNotContain=" + UPDATED_DEPARTMENT_CODE);
    }

    @Test
    @Transactional
    void getAllDepartementsByEntrepriseIsEqualToSomething() throws Exception {
        // Initialize the database
        departementRepository.saveAndFlush(departement);
        Company entreprise;
        if (TestUtil.findAll(em, Company.class).isEmpty()) {
            entreprise = CompanyResourceIT.createEntity(em);
            em.persist(entreprise);
            em.flush();
        } else {
            entreprise = TestUtil.findAll(em, Company.class).get(0);
        }
        em.persist(entreprise);
        em.flush();
        departement.setEntreprise(entreprise);
        departementRepository.saveAndFlush(departement);
        Long entrepriseId = entreprise.getId();

        // Get all the departementList where entreprise equals to entrepriseId
        defaultDepartementShouldBeFound("entrepriseId.equals=" + entrepriseId);

        // Get all the departementList where entreprise equals to (entrepriseId + 1)
        defaultDepartementShouldNotBeFound("entrepriseId.equals=" + (entrepriseId + 1));
    }

    /**
     * Executes the search, and checks that the default entity is returned.
     */
    private void defaultDepartementShouldBeFound(String filter) throws Exception {
        restDepartementMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(departement.getId().intValue())))
            .andExpect(jsonPath("$.[*].departmentName").value(hasItem(DEFAULT_DEPARTMENT_NAME)))
            .andExpect(jsonPath("$.[*].departmentCode").value(hasItem(DEFAULT_DEPARTMENT_CODE)));

        // Check, that the count call also returns 1
        restDepartementMockMvc
            .perform(get(ENTITY_API_URL + "/count?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(content().string("1"));
    }

    /**
     * Executes the search, and checks that the default entity is not returned.
     */
    private void defaultDepartementShouldNotBeFound(String filter) throws Exception {
        restDepartementMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$").isArray())
            .andExpect(jsonPath("$").isEmpty());

        // Check, that the count call also returns 0
        restDepartementMockMvc
            .perform(get(ENTITY_API_URL + "/count?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(content().string("0"));
    }

    @Test
    @Transactional
    void getNonExistingDepartement() throws Exception {
        // Get the departement
        restDepartementMockMvc.perform(get(ENTITY_API_URL_ID, Long.MAX_VALUE)).andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    void putNewDepartement() throws Exception {
        // Initialize the database
        departementRepository.saveAndFlush(departement);

        int databaseSizeBeforeUpdate = departementRepository.findAll().size();

        // Update the departement
        Departement updatedDepartement = departementRepository.findById(departement.getId()).get();
        // Disconnect from session so that the updates on updatedDepartement are not directly saved in db
        em.detach(updatedDepartement);
        updatedDepartement.departmentName(UPDATED_DEPARTMENT_NAME).departmentCode(UPDATED_DEPARTMENT_CODE);

        restDepartementMockMvc
            .perform(
                put(ENTITY_API_URL_ID, updatedDepartement.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(updatedDepartement))
            )
            .andExpect(status().isOk());

        // Validate the Departement in the database
        List<Departement> departementList = departementRepository.findAll();
        assertThat(departementList).hasSize(databaseSizeBeforeUpdate);
        Departement testDepartement = departementList.get(departementList.size() - 1);
        assertThat(testDepartement.getDepartmentName()).isEqualTo(UPDATED_DEPARTMENT_NAME);
        assertThat(testDepartement.getDepartmentCode()).isEqualTo(UPDATED_DEPARTMENT_CODE);
    }

    @Test
    @Transactional
    void putNonExistingDepartement() throws Exception {
        int databaseSizeBeforeUpdate = departementRepository.findAll().size();
        departement.setId(count.incrementAndGet());

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restDepartementMockMvc
            .perform(
                put(ENTITY_API_URL_ID, departement.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(departement))
            )
            .andExpect(status().isBadRequest());

        // Validate the Departement in the database
        List<Departement> departementList = departementRepository.findAll();
        assertThat(departementList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithIdMismatchDepartement() throws Exception {
        int databaseSizeBeforeUpdate = departementRepository.findAll().size();
        departement.setId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restDepartementMockMvc
            .perform(
                put(ENTITY_API_URL_ID, count.incrementAndGet())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(departement))
            )
            .andExpect(status().isBadRequest());

        // Validate the Departement in the database
        List<Departement> departementList = departementRepository.findAll();
        assertThat(departementList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithMissingIdPathParamDepartement() throws Exception {
        int databaseSizeBeforeUpdate = departementRepository.findAll().size();
        departement.setId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restDepartementMockMvc
            .perform(put(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(departement)))
            .andExpect(status().isMethodNotAllowed());

        // Validate the Departement in the database
        List<Departement> departementList = departementRepository.findAll();
        assertThat(departementList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void partialUpdateDepartementWithPatch() throws Exception {
        // Initialize the database
        departementRepository.saveAndFlush(departement);

        int databaseSizeBeforeUpdate = departementRepository.findAll().size();

        // Update the departement using partial update
        Departement partialUpdatedDepartement = new Departement();
        partialUpdatedDepartement.setId(departement.getId());

        partialUpdatedDepartement.departmentName(UPDATED_DEPARTMENT_NAME).departmentCode(UPDATED_DEPARTMENT_CODE);

        restDepartementMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedDepartement.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedDepartement))
            )
            .andExpect(status().isOk());

        // Validate the Departement in the database
        List<Departement> departementList = departementRepository.findAll();
        assertThat(departementList).hasSize(databaseSizeBeforeUpdate);
        Departement testDepartement = departementList.get(departementList.size() - 1);
        assertThat(testDepartement.getDepartmentName()).isEqualTo(UPDATED_DEPARTMENT_NAME);
        assertThat(testDepartement.getDepartmentCode()).isEqualTo(UPDATED_DEPARTMENT_CODE);
    }

    @Test
    @Transactional
    void fullUpdateDepartementWithPatch() throws Exception {
        // Initialize the database
        departementRepository.saveAndFlush(departement);

        int databaseSizeBeforeUpdate = departementRepository.findAll().size();

        // Update the departement using partial update
        Departement partialUpdatedDepartement = new Departement();
        partialUpdatedDepartement.setId(departement.getId());

        partialUpdatedDepartement.departmentName(UPDATED_DEPARTMENT_NAME).departmentCode(UPDATED_DEPARTMENT_CODE);

        restDepartementMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedDepartement.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedDepartement))
            )
            .andExpect(status().isOk());

        // Validate the Departement in the database
        List<Departement> departementList = departementRepository.findAll();
        assertThat(departementList).hasSize(databaseSizeBeforeUpdate);
        Departement testDepartement = departementList.get(departementList.size() - 1);
        assertThat(testDepartement.getDepartmentName()).isEqualTo(UPDATED_DEPARTMENT_NAME);
        assertThat(testDepartement.getDepartmentCode()).isEqualTo(UPDATED_DEPARTMENT_CODE);
    }

    @Test
    @Transactional
    void patchNonExistingDepartement() throws Exception {
        int databaseSizeBeforeUpdate = departementRepository.findAll().size();
        departement.setId(count.incrementAndGet());

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restDepartementMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, departement.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(departement))
            )
            .andExpect(status().isBadRequest());

        // Validate the Departement in the database
        List<Departement> departementList = departementRepository.findAll();
        assertThat(departementList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithIdMismatchDepartement() throws Exception {
        int databaseSizeBeforeUpdate = departementRepository.findAll().size();
        departement.setId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restDepartementMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, count.incrementAndGet())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(departement))
            )
            .andExpect(status().isBadRequest());

        // Validate the Departement in the database
        List<Departement> departementList = departementRepository.findAll();
        assertThat(departementList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithMissingIdPathParamDepartement() throws Exception {
        int databaseSizeBeforeUpdate = departementRepository.findAll().size();
        departement.setId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restDepartementMockMvc
            .perform(
                patch(ENTITY_API_URL).contentType("application/merge-patch+json").content(TestUtil.convertObjectToJsonBytes(departement))
            )
            .andExpect(status().isMethodNotAllowed());

        // Validate the Departement in the database
        List<Departement> departementList = departementRepository.findAll();
        assertThat(departementList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void deleteDepartement() throws Exception {
        // Initialize the database
        departementRepository.saveAndFlush(departement);

        int databaseSizeBeforeDelete = departementRepository.findAll().size();

        // Delete the departement
        restDepartementMockMvc
            .perform(delete(ENTITY_API_URL_ID, departement.getId()).accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        List<Departement> departementList = departementRepository.findAll();
        assertThat(departementList).hasSize(databaseSizeBeforeDelete - 1);
    }
}
