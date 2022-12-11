package com.avanquest.scibscrm.web.rest;

import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import com.avanquest.scibscrm.IntegrationTest;
import com.avanquest.scibscrm.domain.Activite;
import com.avanquest.scibscrm.domain.Employee;
import com.avanquest.scibscrm.domain.TransactionCRM;
import com.avanquest.scibscrm.domain.enumeration.Importance;
import com.avanquest.scibscrm.domain.enumeration.TypeActivite;
import com.avanquest.scibscrm.repository.ActiviteRepository;
import com.avanquest.scibscrm.service.ActiviteService;
import com.avanquest.scibscrm.service.criteria.ActiviteCriteria;
import java.time.Instant;
import java.time.LocalDate;
import java.time.ZoneId;
import java.time.temporal.ChronoUnit;
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
import org.springframework.util.Base64Utils;

/**
 * Integration tests for the {@link ActiviteResource} REST controller.
 */
@IntegrationTest
@ExtendWith(MockitoExtension.class)
@AutoConfigureMockMvc
@WithMockUser
class ActiviteResourceIT {

    private static final TypeActivite DEFAULT_TYPEACTIVITE = TypeActivite.EMAIL;
    private static final TypeActivite UPDATED_TYPEACTIVITE = TypeActivite.COMMENTAIRE;

    private static final String DEFAULT_RESUME = "AAAAAAAAAA";
    private static final String UPDATED_RESUME = "BBBBBBBBBB";

    private static final LocalDate DEFAULT_DATE_ECHEANCE = LocalDate.ofEpochDay(0L);
    private static final LocalDate UPDATED_DATE_ECHEANCE = LocalDate.now(ZoneId.systemDefault());
    private static final LocalDate SMALLER_DATE_ECHEANCE = LocalDate.ofEpochDay(-1L);

    private static final Instant DEFAULT_HEURE_ACTIVITE = Instant.ofEpochMilli(0L);
    private static final Instant UPDATED_HEURE_ACTIVITE = Instant.now().truncatedTo(ChronoUnit.MILLIS);

    private static final Importance DEFAULT_IMPORTANCE = Importance.FAIBLE;
    private static final Importance UPDATED_IMPORTANCE = Importance.MOYENNE;

    private static final String DEFAULT_NOTE = "AAAAAAAAAA";
    private static final String UPDATED_NOTE = "BBBBBBBBBB";

    private static final String ENTITY_API_URL = "/api/activites";
    private static final String ENTITY_API_URL_ID = ENTITY_API_URL + "/{id}";

    private static Random random = new Random();
    private static AtomicLong count = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    @Autowired
    private ActiviteRepository activiteRepository;

    @Mock
    private ActiviteRepository activiteRepositoryMock;

    @Mock
    private ActiviteService activiteServiceMock;

    @Autowired
    private EntityManager em;

    @Autowired
    private MockMvc restActiviteMockMvc;

    private Activite activite;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Activite createEntity(EntityManager em) {
        Activite activite = new Activite()
            .typeactivite(DEFAULT_TYPEACTIVITE)
            .resume(DEFAULT_RESUME)
            .dateEcheance(DEFAULT_DATE_ECHEANCE)
            .heureActivite(DEFAULT_HEURE_ACTIVITE)
            .importance(DEFAULT_IMPORTANCE)
            .note(DEFAULT_NOTE);
        return activite;
    }

    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Activite createUpdatedEntity(EntityManager em) {
        Activite activite = new Activite()
            .typeactivite(UPDATED_TYPEACTIVITE)
            .resume(UPDATED_RESUME)
            .dateEcheance(UPDATED_DATE_ECHEANCE)
            .heureActivite(UPDATED_HEURE_ACTIVITE)
            .importance(UPDATED_IMPORTANCE)
            .note(UPDATED_NOTE);
        return activite;
    }

    @BeforeEach
    public void initTest() {
        activite = createEntity(em);
    }

    @Test
    @Transactional
    void createActivite() throws Exception {
        int databaseSizeBeforeCreate = activiteRepository.findAll().size();
        // Create the Activite
        restActiviteMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(activite)))
            .andExpect(status().isCreated());

        // Validate the Activite in the database
        List<Activite> activiteList = activiteRepository.findAll();
        assertThat(activiteList).hasSize(databaseSizeBeforeCreate + 1);
        Activite testActivite = activiteList.get(activiteList.size() - 1);
        assertThat(testActivite.getTypeactivite()).isEqualTo(DEFAULT_TYPEACTIVITE);
        assertThat(testActivite.getResume()).isEqualTo(DEFAULT_RESUME);
        assertThat(testActivite.getDateEcheance()).isEqualTo(DEFAULT_DATE_ECHEANCE);
        assertThat(testActivite.getHeureActivite()).isEqualTo(DEFAULT_HEURE_ACTIVITE);
        assertThat(testActivite.getImportance()).isEqualTo(DEFAULT_IMPORTANCE);
        assertThat(testActivite.getNote()).isEqualTo(DEFAULT_NOTE);
    }

    @Test
    @Transactional
    void createActiviteWithExistingId() throws Exception {
        // Create the Activite with an existing ID
        activite.setId(1L);

        int databaseSizeBeforeCreate = activiteRepository.findAll().size();

        // An entity with an existing ID cannot be created, so this API call must fail
        restActiviteMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(activite)))
            .andExpect(status().isBadRequest());

        // Validate the Activite in the database
        List<Activite> activiteList = activiteRepository.findAll();
        assertThat(activiteList).hasSize(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    void checkTypeactiviteIsRequired() throws Exception {
        int databaseSizeBeforeTest = activiteRepository.findAll().size();
        // set the field null
        activite.setTypeactivite(null);

        // Create the Activite, which fails.

        restActiviteMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(activite)))
            .andExpect(status().isBadRequest());

        List<Activite> activiteList = activiteRepository.findAll();
        assertThat(activiteList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void getAllActivites() throws Exception {
        // Initialize the database
        activiteRepository.saveAndFlush(activite);

        // Get all the activiteList
        restActiviteMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(activite.getId().intValue())))
            .andExpect(jsonPath("$.[*].typeactivite").value(hasItem(DEFAULT_TYPEACTIVITE.toString())))
            .andExpect(jsonPath("$.[*].resume").value(hasItem(DEFAULT_RESUME)))
            .andExpect(jsonPath("$.[*].dateEcheance").value(hasItem(DEFAULT_DATE_ECHEANCE.toString())))
            .andExpect(jsonPath("$.[*].heureActivite").value(hasItem(DEFAULT_HEURE_ACTIVITE.toString())))
            .andExpect(jsonPath("$.[*].importance").value(hasItem(DEFAULT_IMPORTANCE.toString())))
            .andExpect(jsonPath("$.[*].note").value(hasItem(DEFAULT_NOTE.toString())));
    }

    @SuppressWarnings({ "unchecked" })
    void getAllActivitesWithEagerRelationshipsIsEnabled() throws Exception {
        when(activiteServiceMock.findAllWithEagerRelationships(any())).thenReturn(new PageImpl(new ArrayList<>()));

        restActiviteMockMvc.perform(get(ENTITY_API_URL + "?eagerload=true")).andExpect(status().isOk());

        verify(activiteServiceMock, times(1)).findAllWithEagerRelationships(any());
    }

    @SuppressWarnings({ "unchecked" })
    void getAllActivitesWithEagerRelationshipsIsNotEnabled() throws Exception {
        when(activiteServiceMock.findAllWithEagerRelationships(any())).thenReturn(new PageImpl(new ArrayList<>()));

        restActiviteMockMvc.perform(get(ENTITY_API_URL + "?eagerload=true")).andExpect(status().isOk());

        verify(activiteServiceMock, times(1)).findAllWithEagerRelationships(any());
    }

    @Test
    @Transactional
    void getActivite() throws Exception {
        // Initialize the database
        activiteRepository.saveAndFlush(activite);

        // Get the activite
        restActiviteMockMvc
            .perform(get(ENTITY_API_URL_ID, activite.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.id").value(activite.getId().intValue()))
            .andExpect(jsonPath("$.typeactivite").value(DEFAULT_TYPEACTIVITE.toString()))
            .andExpect(jsonPath("$.resume").value(DEFAULT_RESUME))
            .andExpect(jsonPath("$.dateEcheance").value(DEFAULT_DATE_ECHEANCE.toString()))
            .andExpect(jsonPath("$.heureActivite").value(DEFAULT_HEURE_ACTIVITE.toString()))
            .andExpect(jsonPath("$.importance").value(DEFAULT_IMPORTANCE.toString()))
            .andExpect(jsonPath("$.note").value(DEFAULT_NOTE.toString()));
    }

    @Test
    @Transactional
    void getActivitesByIdFiltering() throws Exception {
        // Initialize the database
        activiteRepository.saveAndFlush(activite);

        Long id = activite.getId();

        defaultActiviteShouldBeFound("id.equals=" + id);
        defaultActiviteShouldNotBeFound("id.notEquals=" + id);

        defaultActiviteShouldBeFound("id.greaterThanOrEqual=" + id);
        defaultActiviteShouldNotBeFound("id.greaterThan=" + id);

        defaultActiviteShouldBeFound("id.lessThanOrEqual=" + id);
        defaultActiviteShouldNotBeFound("id.lessThan=" + id);
    }

    @Test
    @Transactional
    void getAllActivitesByTypeactiviteIsEqualToSomething() throws Exception {
        // Initialize the database
        activiteRepository.saveAndFlush(activite);

        // Get all the activiteList where typeactivite equals to DEFAULT_TYPEACTIVITE
        defaultActiviteShouldBeFound("typeactivite.equals=" + DEFAULT_TYPEACTIVITE);

        // Get all the activiteList where typeactivite equals to UPDATED_TYPEACTIVITE
        defaultActiviteShouldNotBeFound("typeactivite.equals=" + UPDATED_TYPEACTIVITE);
    }

    @Test
    @Transactional
    void getAllActivitesByTypeactiviteIsNotEqualToSomething() throws Exception {
        // Initialize the database
        activiteRepository.saveAndFlush(activite);

        // Get all the activiteList where typeactivite not equals to DEFAULT_TYPEACTIVITE
        defaultActiviteShouldNotBeFound("typeactivite.notEquals=" + DEFAULT_TYPEACTIVITE);

        // Get all the activiteList where typeactivite not equals to UPDATED_TYPEACTIVITE
        defaultActiviteShouldBeFound("typeactivite.notEquals=" + UPDATED_TYPEACTIVITE);
    }

    @Test
    @Transactional
    void getAllActivitesByTypeactiviteIsInShouldWork() throws Exception {
        // Initialize the database
        activiteRepository.saveAndFlush(activite);

        // Get all the activiteList where typeactivite in DEFAULT_TYPEACTIVITE or UPDATED_TYPEACTIVITE
        defaultActiviteShouldBeFound("typeactivite.in=" + DEFAULT_TYPEACTIVITE + "," + UPDATED_TYPEACTIVITE);

        // Get all the activiteList where typeactivite equals to UPDATED_TYPEACTIVITE
        defaultActiviteShouldNotBeFound("typeactivite.in=" + UPDATED_TYPEACTIVITE);
    }

    @Test
    @Transactional
    void getAllActivitesByTypeactiviteIsNullOrNotNull() throws Exception {
        // Initialize the database
        activiteRepository.saveAndFlush(activite);

        // Get all the activiteList where typeactivite is not null
        defaultActiviteShouldBeFound("typeactivite.specified=true");

        // Get all the activiteList where typeactivite is null
        defaultActiviteShouldNotBeFound("typeactivite.specified=false");
    }

    @Test
    @Transactional
    void getAllActivitesByResumeIsEqualToSomething() throws Exception {
        // Initialize the database
        activiteRepository.saveAndFlush(activite);

        // Get all the activiteList where resume equals to DEFAULT_RESUME
        defaultActiviteShouldBeFound("resume.equals=" + DEFAULT_RESUME);

        // Get all the activiteList where resume equals to UPDATED_RESUME
        defaultActiviteShouldNotBeFound("resume.equals=" + UPDATED_RESUME);
    }

    @Test
    @Transactional
    void getAllActivitesByResumeIsNotEqualToSomething() throws Exception {
        // Initialize the database
        activiteRepository.saveAndFlush(activite);

        // Get all the activiteList where resume not equals to DEFAULT_RESUME
        defaultActiviteShouldNotBeFound("resume.notEquals=" + DEFAULT_RESUME);

        // Get all the activiteList where resume not equals to UPDATED_RESUME
        defaultActiviteShouldBeFound("resume.notEquals=" + UPDATED_RESUME);
    }

    @Test
    @Transactional
    void getAllActivitesByResumeIsInShouldWork() throws Exception {
        // Initialize the database
        activiteRepository.saveAndFlush(activite);

        // Get all the activiteList where resume in DEFAULT_RESUME or UPDATED_RESUME
        defaultActiviteShouldBeFound("resume.in=" + DEFAULT_RESUME + "," + UPDATED_RESUME);

        // Get all the activiteList where resume equals to UPDATED_RESUME
        defaultActiviteShouldNotBeFound("resume.in=" + UPDATED_RESUME);
    }

    @Test
    @Transactional
    void getAllActivitesByResumeIsNullOrNotNull() throws Exception {
        // Initialize the database
        activiteRepository.saveAndFlush(activite);

        // Get all the activiteList where resume is not null
        defaultActiviteShouldBeFound("resume.specified=true");

        // Get all the activiteList where resume is null
        defaultActiviteShouldNotBeFound("resume.specified=false");
    }

    @Test
    @Transactional
    void getAllActivitesByResumeContainsSomething() throws Exception {
        // Initialize the database
        activiteRepository.saveAndFlush(activite);

        // Get all the activiteList where resume contains DEFAULT_RESUME
        defaultActiviteShouldBeFound("resume.contains=" + DEFAULT_RESUME);

        // Get all the activiteList where resume contains UPDATED_RESUME
        defaultActiviteShouldNotBeFound("resume.contains=" + UPDATED_RESUME);
    }

    @Test
    @Transactional
    void getAllActivitesByResumeNotContainsSomething() throws Exception {
        // Initialize the database
        activiteRepository.saveAndFlush(activite);

        // Get all the activiteList where resume does not contain DEFAULT_RESUME
        defaultActiviteShouldNotBeFound("resume.doesNotContain=" + DEFAULT_RESUME);

        // Get all the activiteList where resume does not contain UPDATED_RESUME
        defaultActiviteShouldBeFound("resume.doesNotContain=" + UPDATED_RESUME);
    }

    @Test
    @Transactional
    void getAllActivitesByDateEcheanceIsEqualToSomething() throws Exception {
        // Initialize the database
        activiteRepository.saveAndFlush(activite);

        // Get all the activiteList where dateEcheance equals to DEFAULT_DATE_ECHEANCE
        defaultActiviteShouldBeFound("dateEcheance.equals=" + DEFAULT_DATE_ECHEANCE);

        // Get all the activiteList where dateEcheance equals to UPDATED_DATE_ECHEANCE
        defaultActiviteShouldNotBeFound("dateEcheance.equals=" + UPDATED_DATE_ECHEANCE);
    }

    @Test
    @Transactional
    void getAllActivitesByDateEcheanceIsNotEqualToSomething() throws Exception {
        // Initialize the database
        activiteRepository.saveAndFlush(activite);

        // Get all the activiteList where dateEcheance not equals to DEFAULT_DATE_ECHEANCE
        defaultActiviteShouldNotBeFound("dateEcheance.notEquals=" + DEFAULT_DATE_ECHEANCE);

        // Get all the activiteList where dateEcheance not equals to UPDATED_DATE_ECHEANCE
        defaultActiviteShouldBeFound("dateEcheance.notEquals=" + UPDATED_DATE_ECHEANCE);
    }

    @Test
    @Transactional
    void getAllActivitesByDateEcheanceIsInShouldWork() throws Exception {
        // Initialize the database
        activiteRepository.saveAndFlush(activite);

        // Get all the activiteList where dateEcheance in DEFAULT_DATE_ECHEANCE or UPDATED_DATE_ECHEANCE
        defaultActiviteShouldBeFound("dateEcheance.in=" + DEFAULT_DATE_ECHEANCE + "," + UPDATED_DATE_ECHEANCE);

        // Get all the activiteList where dateEcheance equals to UPDATED_DATE_ECHEANCE
        defaultActiviteShouldNotBeFound("dateEcheance.in=" + UPDATED_DATE_ECHEANCE);
    }

    @Test
    @Transactional
    void getAllActivitesByDateEcheanceIsNullOrNotNull() throws Exception {
        // Initialize the database
        activiteRepository.saveAndFlush(activite);

        // Get all the activiteList where dateEcheance is not null
        defaultActiviteShouldBeFound("dateEcheance.specified=true");

        // Get all the activiteList where dateEcheance is null
        defaultActiviteShouldNotBeFound("dateEcheance.specified=false");
    }

    @Test
    @Transactional
    void getAllActivitesByDateEcheanceIsGreaterThanOrEqualToSomething() throws Exception {
        // Initialize the database
        activiteRepository.saveAndFlush(activite);

        // Get all the activiteList where dateEcheance is greater than or equal to DEFAULT_DATE_ECHEANCE
        defaultActiviteShouldBeFound("dateEcheance.greaterThanOrEqual=" + DEFAULT_DATE_ECHEANCE);

        // Get all the activiteList where dateEcheance is greater than or equal to UPDATED_DATE_ECHEANCE
        defaultActiviteShouldNotBeFound("dateEcheance.greaterThanOrEqual=" + UPDATED_DATE_ECHEANCE);
    }

    @Test
    @Transactional
    void getAllActivitesByDateEcheanceIsLessThanOrEqualToSomething() throws Exception {
        // Initialize the database
        activiteRepository.saveAndFlush(activite);

        // Get all the activiteList where dateEcheance is less than or equal to DEFAULT_DATE_ECHEANCE
        defaultActiviteShouldBeFound("dateEcheance.lessThanOrEqual=" + DEFAULT_DATE_ECHEANCE);

        // Get all the activiteList where dateEcheance is less than or equal to SMALLER_DATE_ECHEANCE
        defaultActiviteShouldNotBeFound("dateEcheance.lessThanOrEqual=" + SMALLER_DATE_ECHEANCE);
    }

    @Test
    @Transactional
    void getAllActivitesByDateEcheanceIsLessThanSomething() throws Exception {
        // Initialize the database
        activiteRepository.saveAndFlush(activite);

        // Get all the activiteList where dateEcheance is less than DEFAULT_DATE_ECHEANCE
        defaultActiviteShouldNotBeFound("dateEcheance.lessThan=" + DEFAULT_DATE_ECHEANCE);

        // Get all the activiteList where dateEcheance is less than UPDATED_DATE_ECHEANCE
        defaultActiviteShouldBeFound("dateEcheance.lessThan=" + UPDATED_DATE_ECHEANCE);
    }

    @Test
    @Transactional
    void getAllActivitesByDateEcheanceIsGreaterThanSomething() throws Exception {
        // Initialize the database
        activiteRepository.saveAndFlush(activite);

        // Get all the activiteList where dateEcheance is greater than DEFAULT_DATE_ECHEANCE
        defaultActiviteShouldNotBeFound("dateEcheance.greaterThan=" + DEFAULT_DATE_ECHEANCE);

        // Get all the activiteList where dateEcheance is greater than SMALLER_DATE_ECHEANCE
        defaultActiviteShouldBeFound("dateEcheance.greaterThan=" + SMALLER_DATE_ECHEANCE);
    }

    @Test
    @Transactional
    void getAllActivitesByHeureActiviteIsEqualToSomething() throws Exception {
        // Initialize the database
        activiteRepository.saveAndFlush(activite);

        // Get all the activiteList where heureActivite equals to DEFAULT_HEURE_ACTIVITE
        defaultActiviteShouldBeFound("heureActivite.equals=" + DEFAULT_HEURE_ACTIVITE);

        // Get all the activiteList where heureActivite equals to UPDATED_HEURE_ACTIVITE
        defaultActiviteShouldNotBeFound("heureActivite.equals=" + UPDATED_HEURE_ACTIVITE);
    }

    @Test
    @Transactional
    void getAllActivitesByHeureActiviteIsNotEqualToSomething() throws Exception {
        // Initialize the database
        activiteRepository.saveAndFlush(activite);

        // Get all the activiteList where heureActivite not equals to DEFAULT_HEURE_ACTIVITE
        defaultActiviteShouldNotBeFound("heureActivite.notEquals=" + DEFAULT_HEURE_ACTIVITE);

        // Get all the activiteList where heureActivite not equals to UPDATED_HEURE_ACTIVITE
        defaultActiviteShouldBeFound("heureActivite.notEquals=" + UPDATED_HEURE_ACTIVITE);
    }

    @Test
    @Transactional
    void getAllActivitesByHeureActiviteIsInShouldWork() throws Exception {
        // Initialize the database
        activiteRepository.saveAndFlush(activite);

        // Get all the activiteList where heureActivite in DEFAULT_HEURE_ACTIVITE or UPDATED_HEURE_ACTIVITE
        defaultActiviteShouldBeFound("heureActivite.in=" + DEFAULT_HEURE_ACTIVITE + "," + UPDATED_HEURE_ACTIVITE);

        // Get all the activiteList where heureActivite equals to UPDATED_HEURE_ACTIVITE
        defaultActiviteShouldNotBeFound("heureActivite.in=" + UPDATED_HEURE_ACTIVITE);
    }

    @Test
    @Transactional
    void getAllActivitesByHeureActiviteIsNullOrNotNull() throws Exception {
        // Initialize the database
        activiteRepository.saveAndFlush(activite);

        // Get all the activiteList where heureActivite is not null
        defaultActiviteShouldBeFound("heureActivite.specified=true");

        // Get all the activiteList where heureActivite is null
        defaultActiviteShouldNotBeFound("heureActivite.specified=false");
    }

    @Test
    @Transactional
    void getAllActivitesByImportanceIsEqualToSomething() throws Exception {
        // Initialize the database
        activiteRepository.saveAndFlush(activite);

        // Get all the activiteList where importance equals to DEFAULT_IMPORTANCE
        defaultActiviteShouldBeFound("importance.equals=" + DEFAULT_IMPORTANCE);

        // Get all the activiteList where importance equals to UPDATED_IMPORTANCE
        defaultActiviteShouldNotBeFound("importance.equals=" + UPDATED_IMPORTANCE);
    }

    @Test
    @Transactional
    void getAllActivitesByImportanceIsNotEqualToSomething() throws Exception {
        // Initialize the database
        activiteRepository.saveAndFlush(activite);

        // Get all the activiteList where importance not equals to DEFAULT_IMPORTANCE
        defaultActiviteShouldNotBeFound("importance.notEquals=" + DEFAULT_IMPORTANCE);

        // Get all the activiteList where importance not equals to UPDATED_IMPORTANCE
        defaultActiviteShouldBeFound("importance.notEquals=" + UPDATED_IMPORTANCE);
    }

    @Test
    @Transactional
    void getAllActivitesByImportanceIsInShouldWork() throws Exception {
        // Initialize the database
        activiteRepository.saveAndFlush(activite);

        // Get all the activiteList where importance in DEFAULT_IMPORTANCE or UPDATED_IMPORTANCE
        defaultActiviteShouldBeFound("importance.in=" + DEFAULT_IMPORTANCE + "," + UPDATED_IMPORTANCE);

        // Get all the activiteList where importance equals to UPDATED_IMPORTANCE
        defaultActiviteShouldNotBeFound("importance.in=" + UPDATED_IMPORTANCE);
    }

    @Test
    @Transactional
    void getAllActivitesByImportanceIsNullOrNotNull() throws Exception {
        // Initialize the database
        activiteRepository.saveAndFlush(activite);

        // Get all the activiteList where importance is not null
        defaultActiviteShouldBeFound("importance.specified=true");

        // Get all the activiteList where importance is null
        defaultActiviteShouldNotBeFound("importance.specified=false");
    }

    @Test
    @Transactional
    void getAllActivitesByCreateurIsEqualToSomething() throws Exception {
        // Initialize the database
        activiteRepository.saveAndFlush(activite);
        Employee createur;
        if (TestUtil.findAll(em, Employee.class).isEmpty()) {
            createur = EmployeeResourceIT.createEntity(em);
            em.persist(createur);
            em.flush();
        } else {
            createur = TestUtil.findAll(em, Employee.class).get(0);
        }
        em.persist(createur);
        em.flush();
        activite.setCreateur(createur);
        activiteRepository.saveAndFlush(activite);
        Long createurId = createur.getId();

        // Get all the activiteList where createur equals to createurId
        defaultActiviteShouldBeFound("createurId.equals=" + createurId);

        // Get all the activiteList where createur equals to (createurId + 1)
        defaultActiviteShouldNotBeFound("createurId.equals=" + (createurId + 1));
    }

    @Test
    @Transactional
    void getAllActivitesByEmployeeInclusIsEqualToSomething() throws Exception {
        // Initialize the database
        activiteRepository.saveAndFlush(activite);
        Employee employeeInclus;
        if (TestUtil.findAll(em, Employee.class).isEmpty()) {
            employeeInclus = EmployeeResourceIT.createEntity(em);
            em.persist(employeeInclus);
            em.flush();
        } else {
            employeeInclus = TestUtil.findAll(em, Employee.class).get(0);
        }
        em.persist(employeeInclus);
        em.flush();
        activite.addEmployeeInclus(employeeInclus);
        activiteRepository.saveAndFlush(activite);
        Long employeeInclusId = employeeInclus.getId();

        // Get all the activiteList where employeeInclus equals to employeeInclusId
        defaultActiviteShouldBeFound("employeeInclusId.equals=" + employeeInclusId);

        // Get all the activiteList where employeeInclus equals to (employeeInclusId + 1)
        defaultActiviteShouldNotBeFound("employeeInclusId.equals=" + (employeeInclusId + 1));
    }

    @Test
    @Transactional
    void getAllActivitesByTransactionCRMIsEqualToSomething() throws Exception {
        // Initialize the database
        activiteRepository.saveAndFlush(activite);
        TransactionCRM transactionCRM;
        if (TestUtil.findAll(em, TransactionCRM.class).isEmpty()) {
            transactionCRM = TransactionCRMResourceIT.createEntity(em);
            em.persist(transactionCRM);
            em.flush();
        } else {
            transactionCRM = TestUtil.findAll(em, TransactionCRM.class).get(0);
        }
        em.persist(transactionCRM);
        em.flush();
        activite.addTransactionCRM(transactionCRM);
        activiteRepository.saveAndFlush(activite);
        Long transactionCRMId = transactionCRM.getId();

        // Get all the activiteList where transactionCRM equals to transactionCRMId
        defaultActiviteShouldBeFound("transactionCRMId.equals=" + transactionCRMId);

        // Get all the activiteList where transactionCRM equals to (transactionCRMId + 1)
        defaultActiviteShouldNotBeFound("transactionCRMId.equals=" + (transactionCRMId + 1));
    }

    /**
     * Executes the search, and checks that the default entity is returned.
     */
    private void defaultActiviteShouldBeFound(String filter) throws Exception {
        restActiviteMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(activite.getId().intValue())))
            .andExpect(jsonPath("$.[*].typeactivite").value(hasItem(DEFAULT_TYPEACTIVITE.toString())))
            .andExpect(jsonPath("$.[*].resume").value(hasItem(DEFAULT_RESUME)))
            .andExpect(jsonPath("$.[*].dateEcheance").value(hasItem(DEFAULT_DATE_ECHEANCE.toString())))
            .andExpect(jsonPath("$.[*].heureActivite").value(hasItem(DEFAULT_HEURE_ACTIVITE.toString())))
            .andExpect(jsonPath("$.[*].importance").value(hasItem(DEFAULT_IMPORTANCE.toString())))
            .andExpect(jsonPath("$.[*].note").value(hasItem(DEFAULT_NOTE.toString())));

        // Check, that the count call also returns 1
        restActiviteMockMvc
            .perform(get(ENTITY_API_URL + "/count?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(content().string("1"));
    }

    /**
     * Executes the search, and checks that the default entity is not returned.
     */
    private void defaultActiviteShouldNotBeFound(String filter) throws Exception {
        restActiviteMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$").isArray())
            .andExpect(jsonPath("$").isEmpty());

        // Check, that the count call also returns 0
        restActiviteMockMvc
            .perform(get(ENTITY_API_URL + "/count?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(content().string("0"));
    }

    @Test
    @Transactional
    void getNonExistingActivite() throws Exception {
        // Get the activite
        restActiviteMockMvc.perform(get(ENTITY_API_URL_ID, Long.MAX_VALUE)).andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    void putNewActivite() throws Exception {
        // Initialize the database
        activiteRepository.saveAndFlush(activite);

        int databaseSizeBeforeUpdate = activiteRepository.findAll().size();

        // Update the activite
        Activite updatedActivite = activiteRepository.findById(activite.getId()).get();
        // Disconnect from session so that the updates on updatedActivite are not directly saved in db
        em.detach(updatedActivite);
        updatedActivite
            .typeactivite(UPDATED_TYPEACTIVITE)
            .resume(UPDATED_RESUME)
            .dateEcheance(UPDATED_DATE_ECHEANCE)
            .heureActivite(UPDATED_HEURE_ACTIVITE)
            .importance(UPDATED_IMPORTANCE)
            .note(UPDATED_NOTE);

        restActiviteMockMvc
            .perform(
                put(ENTITY_API_URL_ID, updatedActivite.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(updatedActivite))
            )
            .andExpect(status().isOk());

        // Validate the Activite in the database
        List<Activite> activiteList = activiteRepository.findAll();
        assertThat(activiteList).hasSize(databaseSizeBeforeUpdate);
        Activite testActivite = activiteList.get(activiteList.size() - 1);
        assertThat(testActivite.getTypeactivite()).isEqualTo(UPDATED_TYPEACTIVITE);
        assertThat(testActivite.getResume()).isEqualTo(UPDATED_RESUME);
        assertThat(testActivite.getDateEcheance()).isEqualTo(UPDATED_DATE_ECHEANCE);
        assertThat(testActivite.getHeureActivite()).isEqualTo(UPDATED_HEURE_ACTIVITE);
        assertThat(testActivite.getImportance()).isEqualTo(UPDATED_IMPORTANCE);
        assertThat(testActivite.getNote()).isEqualTo(UPDATED_NOTE);
    }

    @Test
    @Transactional
    void putNonExistingActivite() throws Exception {
        int databaseSizeBeforeUpdate = activiteRepository.findAll().size();
        activite.setId(count.incrementAndGet());

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restActiviteMockMvc
            .perform(
                put(ENTITY_API_URL_ID, activite.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(activite))
            )
            .andExpect(status().isBadRequest());

        // Validate the Activite in the database
        List<Activite> activiteList = activiteRepository.findAll();
        assertThat(activiteList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithIdMismatchActivite() throws Exception {
        int databaseSizeBeforeUpdate = activiteRepository.findAll().size();
        activite.setId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restActiviteMockMvc
            .perform(
                put(ENTITY_API_URL_ID, count.incrementAndGet())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(activite))
            )
            .andExpect(status().isBadRequest());

        // Validate the Activite in the database
        List<Activite> activiteList = activiteRepository.findAll();
        assertThat(activiteList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithMissingIdPathParamActivite() throws Exception {
        int databaseSizeBeforeUpdate = activiteRepository.findAll().size();
        activite.setId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restActiviteMockMvc
            .perform(put(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(activite)))
            .andExpect(status().isMethodNotAllowed());

        // Validate the Activite in the database
        List<Activite> activiteList = activiteRepository.findAll();
        assertThat(activiteList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void partialUpdateActiviteWithPatch() throws Exception {
        // Initialize the database
        activiteRepository.saveAndFlush(activite);

        int databaseSizeBeforeUpdate = activiteRepository.findAll().size();

        // Update the activite using partial update
        Activite partialUpdatedActivite = new Activite();
        partialUpdatedActivite.setId(activite.getId());

        partialUpdatedActivite
            .typeactivite(UPDATED_TYPEACTIVITE)
            .resume(UPDATED_RESUME)
            .dateEcheance(UPDATED_DATE_ECHEANCE)
            .heureActivite(UPDATED_HEURE_ACTIVITE)
            .importance(UPDATED_IMPORTANCE);

        restActiviteMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedActivite.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedActivite))
            )
            .andExpect(status().isOk());

        // Validate the Activite in the database
        List<Activite> activiteList = activiteRepository.findAll();
        assertThat(activiteList).hasSize(databaseSizeBeforeUpdate);
        Activite testActivite = activiteList.get(activiteList.size() - 1);
        assertThat(testActivite.getTypeactivite()).isEqualTo(UPDATED_TYPEACTIVITE);
        assertThat(testActivite.getResume()).isEqualTo(UPDATED_RESUME);
        assertThat(testActivite.getDateEcheance()).isEqualTo(UPDATED_DATE_ECHEANCE);
        assertThat(testActivite.getHeureActivite()).isEqualTo(UPDATED_HEURE_ACTIVITE);
        assertThat(testActivite.getImportance()).isEqualTo(UPDATED_IMPORTANCE);
        assertThat(testActivite.getNote()).isEqualTo(DEFAULT_NOTE);
    }

    @Test
    @Transactional
    void fullUpdateActiviteWithPatch() throws Exception {
        // Initialize the database
        activiteRepository.saveAndFlush(activite);

        int databaseSizeBeforeUpdate = activiteRepository.findAll().size();

        // Update the activite using partial update
        Activite partialUpdatedActivite = new Activite();
        partialUpdatedActivite.setId(activite.getId());

        partialUpdatedActivite
            .typeactivite(UPDATED_TYPEACTIVITE)
            .resume(UPDATED_RESUME)
            .dateEcheance(UPDATED_DATE_ECHEANCE)
            .heureActivite(UPDATED_HEURE_ACTIVITE)
            .importance(UPDATED_IMPORTANCE)
            .note(UPDATED_NOTE);

        restActiviteMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedActivite.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedActivite))
            )
            .andExpect(status().isOk());

        // Validate the Activite in the database
        List<Activite> activiteList = activiteRepository.findAll();
        assertThat(activiteList).hasSize(databaseSizeBeforeUpdate);
        Activite testActivite = activiteList.get(activiteList.size() - 1);
        assertThat(testActivite.getTypeactivite()).isEqualTo(UPDATED_TYPEACTIVITE);
        assertThat(testActivite.getResume()).isEqualTo(UPDATED_RESUME);
        assertThat(testActivite.getDateEcheance()).isEqualTo(UPDATED_DATE_ECHEANCE);
        assertThat(testActivite.getHeureActivite()).isEqualTo(UPDATED_HEURE_ACTIVITE);
        assertThat(testActivite.getImportance()).isEqualTo(UPDATED_IMPORTANCE);
        assertThat(testActivite.getNote()).isEqualTo(UPDATED_NOTE);
    }

    @Test
    @Transactional
    void patchNonExistingActivite() throws Exception {
        int databaseSizeBeforeUpdate = activiteRepository.findAll().size();
        activite.setId(count.incrementAndGet());

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restActiviteMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, activite.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(activite))
            )
            .andExpect(status().isBadRequest());

        // Validate the Activite in the database
        List<Activite> activiteList = activiteRepository.findAll();
        assertThat(activiteList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithIdMismatchActivite() throws Exception {
        int databaseSizeBeforeUpdate = activiteRepository.findAll().size();
        activite.setId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restActiviteMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, count.incrementAndGet())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(activite))
            )
            .andExpect(status().isBadRequest());

        // Validate the Activite in the database
        List<Activite> activiteList = activiteRepository.findAll();
        assertThat(activiteList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithMissingIdPathParamActivite() throws Exception {
        int databaseSizeBeforeUpdate = activiteRepository.findAll().size();
        activite.setId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restActiviteMockMvc
            .perform(patch(ENTITY_API_URL).contentType("application/merge-patch+json").content(TestUtil.convertObjectToJsonBytes(activite)))
            .andExpect(status().isMethodNotAllowed());

        // Validate the Activite in the database
        List<Activite> activiteList = activiteRepository.findAll();
        assertThat(activiteList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void deleteActivite() throws Exception {
        // Initialize the database
        activiteRepository.saveAndFlush(activite);

        int databaseSizeBeforeDelete = activiteRepository.findAll().size();

        // Delete the activite
        restActiviteMockMvc
            .perform(delete(ENTITY_API_URL_ID, activite.getId()).accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        List<Activite> activiteList = activiteRepository.findAll();
        assertThat(activiteList).hasSize(databaseSizeBeforeDelete - 1);
    }
}
