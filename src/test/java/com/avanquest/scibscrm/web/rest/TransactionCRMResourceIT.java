package com.avanquest.scibscrm.web.rest;

import static com.avanquest.scibscrm.web.rest.TestUtil.sameInstant;
import static com.avanquest.scibscrm.web.rest.TestUtil.sameNumber;
import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import com.avanquest.scibscrm.IntegrationTest;
import com.avanquest.scibscrm.domain.Activite;
import com.avanquest.scibscrm.domain.Customer;
import com.avanquest.scibscrm.domain.Employee;
import com.avanquest.scibscrm.domain.Monnaie;
import com.avanquest.scibscrm.domain.TransactionCRM;
import com.avanquest.scibscrm.domain.enumeration.TransactionEtape;
import com.avanquest.scibscrm.domain.enumeration.TransactionSource;
import com.avanquest.scibscrm.repository.TransactionCRMRepository;
import com.avanquest.scibscrm.service.TransactionCRMService;
import com.avanquest.scibscrm.service.criteria.TransactionCRMCriteria;
import java.math.BigDecimal;
import java.time.Instant;
import java.time.LocalDate;
import java.time.ZoneId;
import java.time.ZoneOffset;
import java.time.ZonedDateTime;
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
 * Integration tests for the {@link TransactionCRMResource} REST controller.
 */
@IntegrationTest
@ExtendWith(MockitoExtension.class)
@AutoConfigureMockMvc
@WithMockUser
class TransactionCRMResourceIT {

    private static final String DEFAULT_REFERENCE = "AAAAAAAAAA";
    private static final String UPDATED_REFERENCE = "BBBBBBBBBB";

    private static final BigDecimal DEFAULT_MONTANT = new BigDecimal(0);
    private static final BigDecimal UPDATED_MONTANT = new BigDecimal(1);
    private static final BigDecimal SMALLER_MONTANT = new BigDecimal(0 - 1);

    private static final TransactionEtape DEFAULT_TRANSACTION_ETAPE = TransactionEtape.NOUVEAU;
    private static final TransactionEtape UPDATED_TRANSACTION_ETAPE = TransactionEtape.PAPIERS;

    private static final LocalDate DEFAULT_DATE_FIN = LocalDate.ofEpochDay(0L);
    private static final LocalDate UPDATED_DATE_FIN = LocalDate.now(ZoneId.systemDefault());
    private static final LocalDate SMALLER_DATE_FIN = LocalDate.ofEpochDay(-1L);

    private static final Boolean DEFAULT_TRANSACTION_RECURRENTE = false;
    private static final Boolean UPDATED_TRANSACTION_RECURRENTE = true;

    private static final ZonedDateTime DEFAULT_CREE_LE = ZonedDateTime.ofInstant(Instant.ofEpochMilli(0L), ZoneOffset.UTC);
    private static final ZonedDateTime UPDATED_CREE_LE = ZonedDateTime.now(ZoneId.systemDefault()).withNano(0);
    private static final ZonedDateTime SMALLER_CREE_LE = ZonedDateTime.ofInstant(Instant.ofEpochMilli(-1L), ZoneOffset.UTC);

    private static final ZonedDateTime DEFAULT_DERNIER_UPDATE = ZonedDateTime.ofInstant(Instant.ofEpochMilli(0L), ZoneOffset.UTC);
    private static final ZonedDateTime UPDATED_DERNIER_UPDATE = ZonedDateTime.now(ZoneId.systemDefault()).withNano(0);
    private static final ZonedDateTime SMALLER_DERNIER_UPDATE = ZonedDateTime.ofInstant(Instant.ofEpochMilli(-1L), ZoneOffset.UTC);

    private static final String DEFAULT_TELEPHONE = "AAAAAAAAAA";
    private static final String UPDATED_TELEPHONE = "BBBBBBBBBB";

    private static final TransactionSource DEFAULT_SOURCE = TransactionSource.APPEL;
    private static final TransactionSource UPDATED_SOURCE = TransactionSource.EMAIL;

    private static final String DEFAULT_ADRESSE = "AAAAAAAAAA";
    private static final String UPDATED_ADRESSE = "BBBBBBBBBB";

    private static final String DEFAULT_NOTES = "AAAAAAAAAA";
    private static final String UPDATED_NOTES = "BBBBBBBBBB";

    private static final BigDecimal DEFAULT_LATITUDE = new BigDecimal(1);
    private static final BigDecimal UPDATED_LATITUDE = new BigDecimal(2);
    private static final BigDecimal SMALLER_LATITUDE = new BigDecimal(1 - 1);

    private static final BigDecimal DEFAULT_LONGITUDE = new BigDecimal(1);
    private static final BigDecimal UPDATED_LONGITUDE = new BigDecimal(2);
    private static final BigDecimal SMALLER_LONGITUDE = new BigDecimal(1 - 1);

    private static final String ENTITY_API_URL = "/api/transaction-crms";
    private static final String ENTITY_API_URL_ID = ENTITY_API_URL + "/{id}";

    private static Random random = new Random();
    private static AtomicLong count = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    @Autowired
    private TransactionCRMRepository transactionCRMRepository;

    @Mock
    private TransactionCRMRepository transactionCRMRepositoryMock;

    @Mock
    private TransactionCRMService transactionCRMServiceMock;

    @Autowired
    private EntityManager em;

    @Autowired
    private MockMvc restTransactionCRMMockMvc;

    private TransactionCRM transactionCRM;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static TransactionCRM createEntity(EntityManager em) {
        TransactionCRM transactionCRM = new TransactionCRM()
            .reference(DEFAULT_REFERENCE)
            .montant(DEFAULT_MONTANT)
            .transactionEtape(DEFAULT_TRANSACTION_ETAPE)
            .dateFin(DEFAULT_DATE_FIN)
            .transactionRecurrente(DEFAULT_TRANSACTION_RECURRENTE)
            .creeLe(DEFAULT_CREE_LE)
            .dernierUpdate(DEFAULT_DERNIER_UPDATE)
            .telephone(DEFAULT_TELEPHONE)
            .source(DEFAULT_SOURCE)
            .adresse(DEFAULT_ADRESSE)
            .notes(DEFAULT_NOTES)
            .latitude(DEFAULT_LATITUDE)
            .longitude(DEFAULT_LONGITUDE);
        return transactionCRM;
    }

    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static TransactionCRM createUpdatedEntity(EntityManager em) {
        TransactionCRM transactionCRM = new TransactionCRM()
            .reference(UPDATED_REFERENCE)
            .montant(UPDATED_MONTANT)
            .transactionEtape(UPDATED_TRANSACTION_ETAPE)
            .dateFin(UPDATED_DATE_FIN)
            .transactionRecurrente(UPDATED_TRANSACTION_RECURRENTE)
            .creeLe(UPDATED_CREE_LE)
            .dernierUpdate(UPDATED_DERNIER_UPDATE)
            .telephone(UPDATED_TELEPHONE)
            .source(UPDATED_SOURCE)
            .adresse(UPDATED_ADRESSE)
            .notes(UPDATED_NOTES)
            .latitude(UPDATED_LATITUDE)
            .longitude(UPDATED_LONGITUDE);
        return transactionCRM;
    }

    @BeforeEach
    public void initTest() {
        transactionCRM = createEntity(em);
    }

    @Test
    @Transactional
    void createTransactionCRM() throws Exception {
        int databaseSizeBeforeCreate = transactionCRMRepository.findAll().size();
        // Create the TransactionCRM
        restTransactionCRMMockMvc
            .perform(
                post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(transactionCRM))
            )
            .andExpect(status().isCreated());

        // Validate the TransactionCRM in the database
        List<TransactionCRM> transactionCRMList = transactionCRMRepository.findAll();
        assertThat(transactionCRMList).hasSize(databaseSizeBeforeCreate + 1);
        TransactionCRM testTransactionCRM = transactionCRMList.get(transactionCRMList.size() - 1);
        assertThat(testTransactionCRM.getReference()).isEqualTo(DEFAULT_REFERENCE);
        assertThat(testTransactionCRM.getMontant()).isEqualByComparingTo(DEFAULT_MONTANT);
        assertThat(testTransactionCRM.getTransactionEtape()).isEqualTo(DEFAULT_TRANSACTION_ETAPE);
        assertThat(testTransactionCRM.getDateFin()).isEqualTo(DEFAULT_DATE_FIN);
        assertThat(testTransactionCRM.getTransactionRecurrente()).isEqualTo(DEFAULT_TRANSACTION_RECURRENTE);
        assertThat(testTransactionCRM.getCreeLe()).isEqualTo(DEFAULT_CREE_LE);
        assertThat(testTransactionCRM.getDernierUpdate()).isEqualTo(DEFAULT_DERNIER_UPDATE);
        assertThat(testTransactionCRM.getTelephone()).isEqualTo(DEFAULT_TELEPHONE);
        assertThat(testTransactionCRM.getSource()).isEqualTo(DEFAULT_SOURCE);
        assertThat(testTransactionCRM.getAdresse()).isEqualTo(DEFAULT_ADRESSE);
        assertThat(testTransactionCRM.getNotes()).isEqualTo(DEFAULT_NOTES);
        assertThat(testTransactionCRM.getLatitude()).isEqualByComparingTo(DEFAULT_LATITUDE);
        assertThat(testTransactionCRM.getLongitude()).isEqualByComparingTo(DEFAULT_LONGITUDE);
    }

    @Test
    @Transactional
    void createTransactionCRMWithExistingId() throws Exception {
        // Create the TransactionCRM with an existing ID
        transactionCRM.setId(1L);

        int databaseSizeBeforeCreate = transactionCRMRepository.findAll().size();

        // An entity with an existing ID cannot be created, so this API call must fail
        restTransactionCRMMockMvc
            .perform(
                post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(transactionCRM))
            )
            .andExpect(status().isBadRequest());

        // Validate the TransactionCRM in the database
        List<TransactionCRM> transactionCRMList = transactionCRMRepository.findAll();
        assertThat(transactionCRMList).hasSize(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    void checkMontantIsRequired() throws Exception {
        int databaseSizeBeforeTest = transactionCRMRepository.findAll().size();
        // set the field null
        transactionCRM.setMontant(null);

        // Create the TransactionCRM, which fails.

        restTransactionCRMMockMvc
            .perform(
                post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(transactionCRM))
            )
            .andExpect(status().isBadRequest());

        List<TransactionCRM> transactionCRMList = transactionCRMRepository.findAll();
        assertThat(transactionCRMList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void getAllTransactionCRMS() throws Exception {
        // Initialize the database
        transactionCRMRepository.saveAndFlush(transactionCRM);

        // Get all the transactionCRMList
        restTransactionCRMMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(transactionCRM.getId().intValue())))
            .andExpect(jsonPath("$.[*].reference").value(hasItem(DEFAULT_REFERENCE)))
            .andExpect(jsonPath("$.[*].montant").value(hasItem(sameNumber(DEFAULT_MONTANT))))
            .andExpect(jsonPath("$.[*].transactionEtape").value(hasItem(DEFAULT_TRANSACTION_ETAPE.toString())))
            .andExpect(jsonPath("$.[*].dateFin").value(hasItem(DEFAULT_DATE_FIN.toString())))
            .andExpect(jsonPath("$.[*].transactionRecurrente").value(hasItem(DEFAULT_TRANSACTION_RECURRENTE.booleanValue())))
            .andExpect(jsonPath("$.[*].creeLe").value(hasItem(sameInstant(DEFAULT_CREE_LE))))
            .andExpect(jsonPath("$.[*].dernierUpdate").value(hasItem(sameInstant(DEFAULT_DERNIER_UPDATE))))
            .andExpect(jsonPath("$.[*].telephone").value(hasItem(DEFAULT_TELEPHONE)))
            .andExpect(jsonPath("$.[*].source").value(hasItem(DEFAULT_SOURCE.toString())))
            .andExpect(jsonPath("$.[*].adresse").value(hasItem(DEFAULT_ADRESSE.toString())))
            .andExpect(jsonPath("$.[*].notes").value(hasItem(DEFAULT_NOTES.toString())))
            .andExpect(jsonPath("$.[*].latitude").value(hasItem(sameNumber(DEFAULT_LATITUDE))))
            .andExpect(jsonPath("$.[*].longitude").value(hasItem(sameNumber(DEFAULT_LONGITUDE))));
    }

    @SuppressWarnings({ "unchecked" })
    void getAllTransactionCRMSWithEagerRelationshipsIsEnabled() throws Exception {
        when(transactionCRMServiceMock.findAllWithEagerRelationships(any())).thenReturn(new PageImpl(new ArrayList<>()));

        restTransactionCRMMockMvc.perform(get(ENTITY_API_URL + "?eagerload=true")).andExpect(status().isOk());

        verify(transactionCRMServiceMock, times(1)).findAllWithEagerRelationships(any());
    }

    @SuppressWarnings({ "unchecked" })
    void getAllTransactionCRMSWithEagerRelationshipsIsNotEnabled() throws Exception {
        when(transactionCRMServiceMock.findAllWithEagerRelationships(any())).thenReturn(new PageImpl(new ArrayList<>()));

        restTransactionCRMMockMvc.perform(get(ENTITY_API_URL + "?eagerload=true")).andExpect(status().isOk());

        verify(transactionCRMServiceMock, times(1)).findAllWithEagerRelationships(any());
    }

    @Test
    @Transactional
    void getTransactionCRM() throws Exception {
        // Initialize the database
        transactionCRMRepository.saveAndFlush(transactionCRM);

        // Get the transactionCRM
        restTransactionCRMMockMvc
            .perform(get(ENTITY_API_URL_ID, transactionCRM.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.id").value(transactionCRM.getId().intValue()))
            .andExpect(jsonPath("$.reference").value(DEFAULT_REFERENCE))
            .andExpect(jsonPath("$.montant").value(sameNumber(DEFAULT_MONTANT)))
            .andExpect(jsonPath("$.transactionEtape").value(DEFAULT_TRANSACTION_ETAPE.toString()))
            .andExpect(jsonPath("$.dateFin").value(DEFAULT_DATE_FIN.toString()))
            .andExpect(jsonPath("$.transactionRecurrente").value(DEFAULT_TRANSACTION_RECURRENTE.booleanValue()))
            .andExpect(jsonPath("$.creeLe").value(sameInstant(DEFAULT_CREE_LE)))
            .andExpect(jsonPath("$.dernierUpdate").value(sameInstant(DEFAULT_DERNIER_UPDATE)))
            .andExpect(jsonPath("$.telephone").value(DEFAULT_TELEPHONE))
            .andExpect(jsonPath("$.source").value(DEFAULT_SOURCE.toString()))
            .andExpect(jsonPath("$.adresse").value(DEFAULT_ADRESSE.toString()))
            .andExpect(jsonPath("$.notes").value(DEFAULT_NOTES.toString()))
            .andExpect(jsonPath("$.latitude").value(sameNumber(DEFAULT_LATITUDE)))
            .andExpect(jsonPath("$.longitude").value(sameNumber(DEFAULT_LONGITUDE)));
    }

    @Test
    @Transactional
    void getTransactionCRMSByIdFiltering() throws Exception {
        // Initialize the database
        transactionCRMRepository.saveAndFlush(transactionCRM);

        Long id = transactionCRM.getId();

        defaultTransactionCRMShouldBeFound("id.equals=" + id);
        defaultTransactionCRMShouldNotBeFound("id.notEquals=" + id);

        defaultTransactionCRMShouldBeFound("id.greaterThanOrEqual=" + id);
        defaultTransactionCRMShouldNotBeFound("id.greaterThan=" + id);

        defaultTransactionCRMShouldBeFound("id.lessThanOrEqual=" + id);
        defaultTransactionCRMShouldNotBeFound("id.lessThan=" + id);
    }

    @Test
    @Transactional
    void getAllTransactionCRMSByReferenceIsEqualToSomething() throws Exception {
        // Initialize the database
        transactionCRMRepository.saveAndFlush(transactionCRM);

        // Get all the transactionCRMList where reference equals to DEFAULT_REFERENCE
        defaultTransactionCRMShouldBeFound("reference.equals=" + DEFAULT_REFERENCE);

        // Get all the transactionCRMList where reference equals to UPDATED_REFERENCE
        defaultTransactionCRMShouldNotBeFound("reference.equals=" + UPDATED_REFERENCE);
    }

    @Test
    @Transactional
    void getAllTransactionCRMSByReferenceIsNotEqualToSomething() throws Exception {
        // Initialize the database
        transactionCRMRepository.saveAndFlush(transactionCRM);

        // Get all the transactionCRMList where reference not equals to DEFAULT_REFERENCE
        defaultTransactionCRMShouldNotBeFound("reference.notEquals=" + DEFAULT_REFERENCE);

        // Get all the transactionCRMList where reference not equals to UPDATED_REFERENCE
        defaultTransactionCRMShouldBeFound("reference.notEquals=" + UPDATED_REFERENCE);
    }

    @Test
    @Transactional
    void getAllTransactionCRMSByReferenceIsInShouldWork() throws Exception {
        // Initialize the database
        transactionCRMRepository.saveAndFlush(transactionCRM);

        // Get all the transactionCRMList where reference in DEFAULT_REFERENCE or UPDATED_REFERENCE
        defaultTransactionCRMShouldBeFound("reference.in=" + DEFAULT_REFERENCE + "," + UPDATED_REFERENCE);

        // Get all the transactionCRMList where reference equals to UPDATED_REFERENCE
        defaultTransactionCRMShouldNotBeFound("reference.in=" + UPDATED_REFERENCE);
    }

    @Test
    @Transactional
    void getAllTransactionCRMSByReferenceIsNullOrNotNull() throws Exception {
        // Initialize the database
        transactionCRMRepository.saveAndFlush(transactionCRM);

        // Get all the transactionCRMList where reference is not null
        defaultTransactionCRMShouldBeFound("reference.specified=true");

        // Get all the transactionCRMList where reference is null
        defaultTransactionCRMShouldNotBeFound("reference.specified=false");
    }

    @Test
    @Transactional
    void getAllTransactionCRMSByReferenceContainsSomething() throws Exception {
        // Initialize the database
        transactionCRMRepository.saveAndFlush(transactionCRM);

        // Get all the transactionCRMList where reference contains DEFAULT_REFERENCE
        defaultTransactionCRMShouldBeFound("reference.contains=" + DEFAULT_REFERENCE);

        // Get all the transactionCRMList where reference contains UPDATED_REFERENCE
        defaultTransactionCRMShouldNotBeFound("reference.contains=" + UPDATED_REFERENCE);
    }

    @Test
    @Transactional
    void getAllTransactionCRMSByReferenceNotContainsSomething() throws Exception {
        // Initialize the database
        transactionCRMRepository.saveAndFlush(transactionCRM);

        // Get all the transactionCRMList where reference does not contain DEFAULT_REFERENCE
        defaultTransactionCRMShouldNotBeFound("reference.doesNotContain=" + DEFAULT_REFERENCE);

        // Get all the transactionCRMList where reference does not contain UPDATED_REFERENCE
        defaultTransactionCRMShouldBeFound("reference.doesNotContain=" + UPDATED_REFERENCE);
    }

    @Test
    @Transactional
    void getAllTransactionCRMSByMontantIsEqualToSomething() throws Exception {
        // Initialize the database
        transactionCRMRepository.saveAndFlush(transactionCRM);

        // Get all the transactionCRMList where montant equals to DEFAULT_MONTANT
        defaultTransactionCRMShouldBeFound("montant.equals=" + DEFAULT_MONTANT);

        // Get all the transactionCRMList where montant equals to UPDATED_MONTANT
        defaultTransactionCRMShouldNotBeFound("montant.equals=" + UPDATED_MONTANT);
    }

    @Test
    @Transactional
    void getAllTransactionCRMSByMontantIsNotEqualToSomething() throws Exception {
        // Initialize the database
        transactionCRMRepository.saveAndFlush(transactionCRM);

        // Get all the transactionCRMList where montant not equals to DEFAULT_MONTANT
        defaultTransactionCRMShouldNotBeFound("montant.notEquals=" + DEFAULT_MONTANT);

        // Get all the transactionCRMList where montant not equals to UPDATED_MONTANT
        defaultTransactionCRMShouldBeFound("montant.notEquals=" + UPDATED_MONTANT);
    }

    @Test
    @Transactional
    void getAllTransactionCRMSByMontantIsInShouldWork() throws Exception {
        // Initialize the database
        transactionCRMRepository.saveAndFlush(transactionCRM);

        // Get all the transactionCRMList where montant in DEFAULT_MONTANT or UPDATED_MONTANT
        defaultTransactionCRMShouldBeFound("montant.in=" + DEFAULT_MONTANT + "," + UPDATED_MONTANT);

        // Get all the transactionCRMList where montant equals to UPDATED_MONTANT
        defaultTransactionCRMShouldNotBeFound("montant.in=" + UPDATED_MONTANT);
    }

    @Test
    @Transactional
    void getAllTransactionCRMSByMontantIsNullOrNotNull() throws Exception {
        // Initialize the database
        transactionCRMRepository.saveAndFlush(transactionCRM);

        // Get all the transactionCRMList where montant is not null
        defaultTransactionCRMShouldBeFound("montant.specified=true");

        // Get all the transactionCRMList where montant is null
        defaultTransactionCRMShouldNotBeFound("montant.specified=false");
    }

    @Test
    @Transactional
    void getAllTransactionCRMSByMontantIsGreaterThanOrEqualToSomething() throws Exception {
        // Initialize the database
        transactionCRMRepository.saveAndFlush(transactionCRM);

        // Get all the transactionCRMList where montant is greater than or equal to DEFAULT_MONTANT
        defaultTransactionCRMShouldBeFound("montant.greaterThanOrEqual=" + DEFAULT_MONTANT);

        // Get all the transactionCRMList where montant is greater than or equal to UPDATED_MONTANT
        defaultTransactionCRMShouldNotBeFound("montant.greaterThanOrEqual=" + UPDATED_MONTANT);
    }

    @Test
    @Transactional
    void getAllTransactionCRMSByMontantIsLessThanOrEqualToSomething() throws Exception {
        // Initialize the database
        transactionCRMRepository.saveAndFlush(transactionCRM);

        // Get all the transactionCRMList where montant is less than or equal to DEFAULT_MONTANT
        defaultTransactionCRMShouldBeFound("montant.lessThanOrEqual=" + DEFAULT_MONTANT);

        // Get all the transactionCRMList where montant is less than or equal to SMALLER_MONTANT
        defaultTransactionCRMShouldNotBeFound("montant.lessThanOrEqual=" + SMALLER_MONTANT);
    }

    @Test
    @Transactional
    void getAllTransactionCRMSByMontantIsLessThanSomething() throws Exception {
        // Initialize the database
        transactionCRMRepository.saveAndFlush(transactionCRM);

        // Get all the transactionCRMList where montant is less than DEFAULT_MONTANT
        defaultTransactionCRMShouldNotBeFound("montant.lessThan=" + DEFAULT_MONTANT);

        // Get all the transactionCRMList where montant is less than UPDATED_MONTANT
        defaultTransactionCRMShouldBeFound("montant.lessThan=" + UPDATED_MONTANT);
    }

    @Test
    @Transactional
    void getAllTransactionCRMSByMontantIsGreaterThanSomething() throws Exception {
        // Initialize the database
        transactionCRMRepository.saveAndFlush(transactionCRM);

        // Get all the transactionCRMList where montant is greater than DEFAULT_MONTANT
        defaultTransactionCRMShouldNotBeFound("montant.greaterThan=" + DEFAULT_MONTANT);

        // Get all the transactionCRMList where montant is greater than SMALLER_MONTANT
        defaultTransactionCRMShouldBeFound("montant.greaterThan=" + SMALLER_MONTANT);
    }

    @Test
    @Transactional
    void getAllTransactionCRMSByTransactionEtapeIsEqualToSomething() throws Exception {
        // Initialize the database
        transactionCRMRepository.saveAndFlush(transactionCRM);

        // Get all the transactionCRMList where transactionEtape equals to DEFAULT_TRANSACTION_ETAPE
        defaultTransactionCRMShouldBeFound("transactionEtape.equals=" + DEFAULT_TRANSACTION_ETAPE);

        // Get all the transactionCRMList where transactionEtape equals to UPDATED_TRANSACTION_ETAPE
        defaultTransactionCRMShouldNotBeFound("transactionEtape.equals=" + UPDATED_TRANSACTION_ETAPE);
    }

    @Test
    @Transactional
    void getAllTransactionCRMSByTransactionEtapeIsNotEqualToSomething() throws Exception {
        // Initialize the database
        transactionCRMRepository.saveAndFlush(transactionCRM);

        // Get all the transactionCRMList where transactionEtape not equals to DEFAULT_TRANSACTION_ETAPE
        defaultTransactionCRMShouldNotBeFound("transactionEtape.notEquals=" + DEFAULT_TRANSACTION_ETAPE);

        // Get all the transactionCRMList where transactionEtape not equals to UPDATED_TRANSACTION_ETAPE
        defaultTransactionCRMShouldBeFound("transactionEtape.notEquals=" + UPDATED_TRANSACTION_ETAPE);
    }

    @Test
    @Transactional
    void getAllTransactionCRMSByTransactionEtapeIsInShouldWork() throws Exception {
        // Initialize the database
        transactionCRMRepository.saveAndFlush(transactionCRM);

        // Get all the transactionCRMList where transactionEtape in DEFAULT_TRANSACTION_ETAPE or UPDATED_TRANSACTION_ETAPE
        defaultTransactionCRMShouldBeFound("transactionEtape.in=" + DEFAULT_TRANSACTION_ETAPE + "," + UPDATED_TRANSACTION_ETAPE);

        // Get all the transactionCRMList where transactionEtape equals to UPDATED_TRANSACTION_ETAPE
        defaultTransactionCRMShouldNotBeFound("transactionEtape.in=" + UPDATED_TRANSACTION_ETAPE);
    }

    @Test
    @Transactional
    void getAllTransactionCRMSByTransactionEtapeIsNullOrNotNull() throws Exception {
        // Initialize the database
        transactionCRMRepository.saveAndFlush(transactionCRM);

        // Get all the transactionCRMList where transactionEtape is not null
        defaultTransactionCRMShouldBeFound("transactionEtape.specified=true");

        // Get all the transactionCRMList where transactionEtape is null
        defaultTransactionCRMShouldNotBeFound("transactionEtape.specified=false");
    }

    @Test
    @Transactional
    void getAllTransactionCRMSByDateFinIsEqualToSomething() throws Exception {
        // Initialize the database
        transactionCRMRepository.saveAndFlush(transactionCRM);

        // Get all the transactionCRMList where dateFin equals to DEFAULT_DATE_FIN
        defaultTransactionCRMShouldBeFound("dateFin.equals=" + DEFAULT_DATE_FIN);

        // Get all the transactionCRMList where dateFin equals to UPDATED_DATE_FIN
        defaultTransactionCRMShouldNotBeFound("dateFin.equals=" + UPDATED_DATE_FIN);
    }

    @Test
    @Transactional
    void getAllTransactionCRMSByDateFinIsNotEqualToSomething() throws Exception {
        // Initialize the database
        transactionCRMRepository.saveAndFlush(transactionCRM);

        // Get all the transactionCRMList where dateFin not equals to DEFAULT_DATE_FIN
        defaultTransactionCRMShouldNotBeFound("dateFin.notEquals=" + DEFAULT_DATE_FIN);

        // Get all the transactionCRMList where dateFin not equals to UPDATED_DATE_FIN
        defaultTransactionCRMShouldBeFound("dateFin.notEquals=" + UPDATED_DATE_FIN);
    }

    @Test
    @Transactional
    void getAllTransactionCRMSByDateFinIsInShouldWork() throws Exception {
        // Initialize the database
        transactionCRMRepository.saveAndFlush(transactionCRM);

        // Get all the transactionCRMList where dateFin in DEFAULT_DATE_FIN or UPDATED_DATE_FIN
        defaultTransactionCRMShouldBeFound("dateFin.in=" + DEFAULT_DATE_FIN + "," + UPDATED_DATE_FIN);

        // Get all the transactionCRMList where dateFin equals to UPDATED_DATE_FIN
        defaultTransactionCRMShouldNotBeFound("dateFin.in=" + UPDATED_DATE_FIN);
    }

    @Test
    @Transactional
    void getAllTransactionCRMSByDateFinIsNullOrNotNull() throws Exception {
        // Initialize the database
        transactionCRMRepository.saveAndFlush(transactionCRM);

        // Get all the transactionCRMList where dateFin is not null
        defaultTransactionCRMShouldBeFound("dateFin.specified=true");

        // Get all the transactionCRMList where dateFin is null
        defaultTransactionCRMShouldNotBeFound("dateFin.specified=false");
    }

    @Test
    @Transactional
    void getAllTransactionCRMSByDateFinIsGreaterThanOrEqualToSomething() throws Exception {
        // Initialize the database
        transactionCRMRepository.saveAndFlush(transactionCRM);

        // Get all the transactionCRMList where dateFin is greater than or equal to DEFAULT_DATE_FIN
        defaultTransactionCRMShouldBeFound("dateFin.greaterThanOrEqual=" + DEFAULT_DATE_FIN);

        // Get all the transactionCRMList where dateFin is greater than or equal to UPDATED_DATE_FIN
        defaultTransactionCRMShouldNotBeFound("dateFin.greaterThanOrEqual=" + UPDATED_DATE_FIN);
    }

    @Test
    @Transactional
    void getAllTransactionCRMSByDateFinIsLessThanOrEqualToSomething() throws Exception {
        // Initialize the database
        transactionCRMRepository.saveAndFlush(transactionCRM);

        // Get all the transactionCRMList where dateFin is less than or equal to DEFAULT_DATE_FIN
        defaultTransactionCRMShouldBeFound("dateFin.lessThanOrEqual=" + DEFAULT_DATE_FIN);

        // Get all the transactionCRMList where dateFin is less than or equal to SMALLER_DATE_FIN
        defaultTransactionCRMShouldNotBeFound("dateFin.lessThanOrEqual=" + SMALLER_DATE_FIN);
    }

    @Test
    @Transactional
    void getAllTransactionCRMSByDateFinIsLessThanSomething() throws Exception {
        // Initialize the database
        transactionCRMRepository.saveAndFlush(transactionCRM);

        // Get all the transactionCRMList where dateFin is less than DEFAULT_DATE_FIN
        defaultTransactionCRMShouldNotBeFound("dateFin.lessThan=" + DEFAULT_DATE_FIN);

        // Get all the transactionCRMList where dateFin is less than UPDATED_DATE_FIN
        defaultTransactionCRMShouldBeFound("dateFin.lessThan=" + UPDATED_DATE_FIN);
    }

    @Test
    @Transactional
    void getAllTransactionCRMSByDateFinIsGreaterThanSomething() throws Exception {
        // Initialize the database
        transactionCRMRepository.saveAndFlush(transactionCRM);

        // Get all the transactionCRMList where dateFin is greater than DEFAULT_DATE_FIN
        defaultTransactionCRMShouldNotBeFound("dateFin.greaterThan=" + DEFAULT_DATE_FIN);

        // Get all the transactionCRMList where dateFin is greater than SMALLER_DATE_FIN
        defaultTransactionCRMShouldBeFound("dateFin.greaterThan=" + SMALLER_DATE_FIN);
    }

    @Test
    @Transactional
    void getAllTransactionCRMSByTransactionRecurrenteIsEqualToSomething() throws Exception {
        // Initialize the database
        transactionCRMRepository.saveAndFlush(transactionCRM);

        // Get all the transactionCRMList where transactionRecurrente equals to DEFAULT_TRANSACTION_RECURRENTE
        defaultTransactionCRMShouldBeFound("transactionRecurrente.equals=" + DEFAULT_TRANSACTION_RECURRENTE);

        // Get all the transactionCRMList where transactionRecurrente equals to UPDATED_TRANSACTION_RECURRENTE
        defaultTransactionCRMShouldNotBeFound("transactionRecurrente.equals=" + UPDATED_TRANSACTION_RECURRENTE);
    }

    @Test
    @Transactional
    void getAllTransactionCRMSByTransactionRecurrenteIsNotEqualToSomething() throws Exception {
        // Initialize the database
        transactionCRMRepository.saveAndFlush(transactionCRM);

        // Get all the transactionCRMList where transactionRecurrente not equals to DEFAULT_TRANSACTION_RECURRENTE
        defaultTransactionCRMShouldNotBeFound("transactionRecurrente.notEquals=" + DEFAULT_TRANSACTION_RECURRENTE);

        // Get all the transactionCRMList where transactionRecurrente not equals to UPDATED_TRANSACTION_RECURRENTE
        defaultTransactionCRMShouldBeFound("transactionRecurrente.notEquals=" + UPDATED_TRANSACTION_RECURRENTE);
    }

    @Test
    @Transactional
    void getAllTransactionCRMSByTransactionRecurrenteIsInShouldWork() throws Exception {
        // Initialize the database
        transactionCRMRepository.saveAndFlush(transactionCRM);

        // Get all the transactionCRMList where transactionRecurrente in DEFAULT_TRANSACTION_RECURRENTE or UPDATED_TRANSACTION_RECURRENTE
        defaultTransactionCRMShouldBeFound(
            "transactionRecurrente.in=" + DEFAULT_TRANSACTION_RECURRENTE + "," + UPDATED_TRANSACTION_RECURRENTE
        );

        // Get all the transactionCRMList where transactionRecurrente equals to UPDATED_TRANSACTION_RECURRENTE
        defaultTransactionCRMShouldNotBeFound("transactionRecurrente.in=" + UPDATED_TRANSACTION_RECURRENTE);
    }

    @Test
    @Transactional
    void getAllTransactionCRMSByTransactionRecurrenteIsNullOrNotNull() throws Exception {
        // Initialize the database
        transactionCRMRepository.saveAndFlush(transactionCRM);

        // Get all the transactionCRMList where transactionRecurrente is not null
        defaultTransactionCRMShouldBeFound("transactionRecurrente.specified=true");

        // Get all the transactionCRMList where transactionRecurrente is null
        defaultTransactionCRMShouldNotBeFound("transactionRecurrente.specified=false");
    }

    @Test
    @Transactional
    void getAllTransactionCRMSByCreeLeIsEqualToSomething() throws Exception {
        // Initialize the database
        transactionCRMRepository.saveAndFlush(transactionCRM);

        // Get all the transactionCRMList where creeLe equals to DEFAULT_CREE_LE
        defaultTransactionCRMShouldBeFound("creeLe.equals=" + DEFAULT_CREE_LE);

        // Get all the transactionCRMList where creeLe equals to UPDATED_CREE_LE
        defaultTransactionCRMShouldNotBeFound("creeLe.equals=" + UPDATED_CREE_LE);
    }

    @Test
    @Transactional
    void getAllTransactionCRMSByCreeLeIsNotEqualToSomething() throws Exception {
        // Initialize the database
        transactionCRMRepository.saveAndFlush(transactionCRM);

        // Get all the transactionCRMList where creeLe not equals to DEFAULT_CREE_LE
        defaultTransactionCRMShouldNotBeFound("creeLe.notEquals=" + DEFAULT_CREE_LE);

        // Get all the transactionCRMList where creeLe not equals to UPDATED_CREE_LE
        defaultTransactionCRMShouldBeFound("creeLe.notEquals=" + UPDATED_CREE_LE);
    }

    @Test
    @Transactional
    void getAllTransactionCRMSByCreeLeIsInShouldWork() throws Exception {
        // Initialize the database
        transactionCRMRepository.saveAndFlush(transactionCRM);

        // Get all the transactionCRMList where creeLe in DEFAULT_CREE_LE or UPDATED_CREE_LE
        defaultTransactionCRMShouldBeFound("creeLe.in=" + DEFAULT_CREE_LE + "," + UPDATED_CREE_LE);

        // Get all the transactionCRMList where creeLe equals to UPDATED_CREE_LE
        defaultTransactionCRMShouldNotBeFound("creeLe.in=" + UPDATED_CREE_LE);
    }

    @Test
    @Transactional
    void getAllTransactionCRMSByCreeLeIsNullOrNotNull() throws Exception {
        // Initialize the database
        transactionCRMRepository.saveAndFlush(transactionCRM);

        // Get all the transactionCRMList where creeLe is not null
        defaultTransactionCRMShouldBeFound("creeLe.specified=true");

        // Get all the transactionCRMList where creeLe is null
        defaultTransactionCRMShouldNotBeFound("creeLe.specified=false");
    }

    @Test
    @Transactional
    void getAllTransactionCRMSByCreeLeIsGreaterThanOrEqualToSomething() throws Exception {
        // Initialize the database
        transactionCRMRepository.saveAndFlush(transactionCRM);

        // Get all the transactionCRMList where creeLe is greater than or equal to DEFAULT_CREE_LE
        defaultTransactionCRMShouldBeFound("creeLe.greaterThanOrEqual=" + DEFAULT_CREE_LE);

        // Get all the transactionCRMList where creeLe is greater than or equal to UPDATED_CREE_LE
        defaultTransactionCRMShouldNotBeFound("creeLe.greaterThanOrEqual=" + UPDATED_CREE_LE);
    }

    @Test
    @Transactional
    void getAllTransactionCRMSByCreeLeIsLessThanOrEqualToSomething() throws Exception {
        // Initialize the database
        transactionCRMRepository.saveAndFlush(transactionCRM);

        // Get all the transactionCRMList where creeLe is less than or equal to DEFAULT_CREE_LE
        defaultTransactionCRMShouldBeFound("creeLe.lessThanOrEqual=" + DEFAULT_CREE_LE);

        // Get all the transactionCRMList where creeLe is less than or equal to SMALLER_CREE_LE
        defaultTransactionCRMShouldNotBeFound("creeLe.lessThanOrEqual=" + SMALLER_CREE_LE);
    }

    @Test
    @Transactional
    void getAllTransactionCRMSByCreeLeIsLessThanSomething() throws Exception {
        // Initialize the database
        transactionCRMRepository.saveAndFlush(transactionCRM);

        // Get all the transactionCRMList where creeLe is less than DEFAULT_CREE_LE
        defaultTransactionCRMShouldNotBeFound("creeLe.lessThan=" + DEFAULT_CREE_LE);

        // Get all the transactionCRMList where creeLe is less than UPDATED_CREE_LE
        defaultTransactionCRMShouldBeFound("creeLe.lessThan=" + UPDATED_CREE_LE);
    }

    @Test
    @Transactional
    void getAllTransactionCRMSByCreeLeIsGreaterThanSomething() throws Exception {
        // Initialize the database
        transactionCRMRepository.saveAndFlush(transactionCRM);

        // Get all the transactionCRMList where creeLe is greater than DEFAULT_CREE_LE
        defaultTransactionCRMShouldNotBeFound("creeLe.greaterThan=" + DEFAULT_CREE_LE);

        // Get all the transactionCRMList where creeLe is greater than SMALLER_CREE_LE
        defaultTransactionCRMShouldBeFound("creeLe.greaterThan=" + SMALLER_CREE_LE);
    }

    @Test
    @Transactional
    void getAllTransactionCRMSByDernierUpdateIsEqualToSomething() throws Exception {
        // Initialize the database
        transactionCRMRepository.saveAndFlush(transactionCRM);

        // Get all the transactionCRMList where dernierUpdate equals to DEFAULT_DERNIER_UPDATE
        defaultTransactionCRMShouldBeFound("dernierUpdate.equals=" + DEFAULT_DERNIER_UPDATE);

        // Get all the transactionCRMList where dernierUpdate equals to UPDATED_DERNIER_UPDATE
        defaultTransactionCRMShouldNotBeFound("dernierUpdate.equals=" + UPDATED_DERNIER_UPDATE);
    }

    @Test
    @Transactional
    void getAllTransactionCRMSByDernierUpdateIsNotEqualToSomething() throws Exception {
        // Initialize the database
        transactionCRMRepository.saveAndFlush(transactionCRM);

        // Get all the transactionCRMList where dernierUpdate not equals to DEFAULT_DERNIER_UPDATE
        defaultTransactionCRMShouldNotBeFound("dernierUpdate.notEquals=" + DEFAULT_DERNIER_UPDATE);

        // Get all the transactionCRMList where dernierUpdate not equals to UPDATED_DERNIER_UPDATE
        defaultTransactionCRMShouldBeFound("dernierUpdate.notEquals=" + UPDATED_DERNIER_UPDATE);
    }

    @Test
    @Transactional
    void getAllTransactionCRMSByDernierUpdateIsInShouldWork() throws Exception {
        // Initialize the database
        transactionCRMRepository.saveAndFlush(transactionCRM);

        // Get all the transactionCRMList where dernierUpdate in DEFAULT_DERNIER_UPDATE or UPDATED_DERNIER_UPDATE
        defaultTransactionCRMShouldBeFound("dernierUpdate.in=" + DEFAULT_DERNIER_UPDATE + "," + UPDATED_DERNIER_UPDATE);

        // Get all the transactionCRMList where dernierUpdate equals to UPDATED_DERNIER_UPDATE
        defaultTransactionCRMShouldNotBeFound("dernierUpdate.in=" + UPDATED_DERNIER_UPDATE);
    }

    @Test
    @Transactional
    void getAllTransactionCRMSByDernierUpdateIsNullOrNotNull() throws Exception {
        // Initialize the database
        transactionCRMRepository.saveAndFlush(transactionCRM);

        // Get all the transactionCRMList where dernierUpdate is not null
        defaultTransactionCRMShouldBeFound("dernierUpdate.specified=true");

        // Get all the transactionCRMList where dernierUpdate is null
        defaultTransactionCRMShouldNotBeFound("dernierUpdate.specified=false");
    }

    @Test
    @Transactional
    void getAllTransactionCRMSByDernierUpdateIsGreaterThanOrEqualToSomething() throws Exception {
        // Initialize the database
        transactionCRMRepository.saveAndFlush(transactionCRM);

        // Get all the transactionCRMList where dernierUpdate is greater than or equal to DEFAULT_DERNIER_UPDATE
        defaultTransactionCRMShouldBeFound("dernierUpdate.greaterThanOrEqual=" + DEFAULT_DERNIER_UPDATE);

        // Get all the transactionCRMList where dernierUpdate is greater than or equal to UPDATED_DERNIER_UPDATE
        defaultTransactionCRMShouldNotBeFound("dernierUpdate.greaterThanOrEqual=" + UPDATED_DERNIER_UPDATE);
    }

    @Test
    @Transactional
    void getAllTransactionCRMSByDernierUpdateIsLessThanOrEqualToSomething() throws Exception {
        // Initialize the database
        transactionCRMRepository.saveAndFlush(transactionCRM);

        // Get all the transactionCRMList where dernierUpdate is less than or equal to DEFAULT_DERNIER_UPDATE
        defaultTransactionCRMShouldBeFound("dernierUpdate.lessThanOrEqual=" + DEFAULT_DERNIER_UPDATE);

        // Get all the transactionCRMList where dernierUpdate is less than or equal to SMALLER_DERNIER_UPDATE
        defaultTransactionCRMShouldNotBeFound("dernierUpdate.lessThanOrEqual=" + SMALLER_DERNIER_UPDATE);
    }

    @Test
    @Transactional
    void getAllTransactionCRMSByDernierUpdateIsLessThanSomething() throws Exception {
        // Initialize the database
        transactionCRMRepository.saveAndFlush(transactionCRM);

        // Get all the transactionCRMList where dernierUpdate is less than DEFAULT_DERNIER_UPDATE
        defaultTransactionCRMShouldNotBeFound("dernierUpdate.lessThan=" + DEFAULT_DERNIER_UPDATE);

        // Get all the transactionCRMList where dernierUpdate is less than UPDATED_DERNIER_UPDATE
        defaultTransactionCRMShouldBeFound("dernierUpdate.lessThan=" + UPDATED_DERNIER_UPDATE);
    }

    @Test
    @Transactional
    void getAllTransactionCRMSByDernierUpdateIsGreaterThanSomething() throws Exception {
        // Initialize the database
        transactionCRMRepository.saveAndFlush(transactionCRM);

        // Get all the transactionCRMList where dernierUpdate is greater than DEFAULT_DERNIER_UPDATE
        defaultTransactionCRMShouldNotBeFound("dernierUpdate.greaterThan=" + DEFAULT_DERNIER_UPDATE);

        // Get all the transactionCRMList where dernierUpdate is greater than SMALLER_DERNIER_UPDATE
        defaultTransactionCRMShouldBeFound("dernierUpdate.greaterThan=" + SMALLER_DERNIER_UPDATE);
    }

    @Test
    @Transactional
    void getAllTransactionCRMSByTelephoneIsEqualToSomething() throws Exception {
        // Initialize the database
        transactionCRMRepository.saveAndFlush(transactionCRM);

        // Get all the transactionCRMList where telephone equals to DEFAULT_TELEPHONE
        defaultTransactionCRMShouldBeFound("telephone.equals=" + DEFAULT_TELEPHONE);

        // Get all the transactionCRMList where telephone equals to UPDATED_TELEPHONE
        defaultTransactionCRMShouldNotBeFound("telephone.equals=" + UPDATED_TELEPHONE);
    }

    @Test
    @Transactional
    void getAllTransactionCRMSByTelephoneIsNotEqualToSomething() throws Exception {
        // Initialize the database
        transactionCRMRepository.saveAndFlush(transactionCRM);

        // Get all the transactionCRMList where telephone not equals to DEFAULT_TELEPHONE
        defaultTransactionCRMShouldNotBeFound("telephone.notEquals=" + DEFAULT_TELEPHONE);

        // Get all the transactionCRMList where telephone not equals to UPDATED_TELEPHONE
        defaultTransactionCRMShouldBeFound("telephone.notEquals=" + UPDATED_TELEPHONE);
    }

    @Test
    @Transactional
    void getAllTransactionCRMSByTelephoneIsInShouldWork() throws Exception {
        // Initialize the database
        transactionCRMRepository.saveAndFlush(transactionCRM);

        // Get all the transactionCRMList where telephone in DEFAULT_TELEPHONE or UPDATED_TELEPHONE
        defaultTransactionCRMShouldBeFound("telephone.in=" + DEFAULT_TELEPHONE + "," + UPDATED_TELEPHONE);

        // Get all the transactionCRMList where telephone equals to UPDATED_TELEPHONE
        defaultTransactionCRMShouldNotBeFound("telephone.in=" + UPDATED_TELEPHONE);
    }

    @Test
    @Transactional
    void getAllTransactionCRMSByTelephoneIsNullOrNotNull() throws Exception {
        // Initialize the database
        transactionCRMRepository.saveAndFlush(transactionCRM);

        // Get all the transactionCRMList where telephone is not null
        defaultTransactionCRMShouldBeFound("telephone.specified=true");

        // Get all the transactionCRMList where telephone is null
        defaultTransactionCRMShouldNotBeFound("telephone.specified=false");
    }

    @Test
    @Transactional
    void getAllTransactionCRMSByTelephoneContainsSomething() throws Exception {
        // Initialize the database
        transactionCRMRepository.saveAndFlush(transactionCRM);

        // Get all the transactionCRMList where telephone contains DEFAULT_TELEPHONE
        defaultTransactionCRMShouldBeFound("telephone.contains=" + DEFAULT_TELEPHONE);

        // Get all the transactionCRMList where telephone contains UPDATED_TELEPHONE
        defaultTransactionCRMShouldNotBeFound("telephone.contains=" + UPDATED_TELEPHONE);
    }

    @Test
    @Transactional
    void getAllTransactionCRMSByTelephoneNotContainsSomething() throws Exception {
        // Initialize the database
        transactionCRMRepository.saveAndFlush(transactionCRM);

        // Get all the transactionCRMList where telephone does not contain DEFAULT_TELEPHONE
        defaultTransactionCRMShouldNotBeFound("telephone.doesNotContain=" + DEFAULT_TELEPHONE);

        // Get all the transactionCRMList where telephone does not contain UPDATED_TELEPHONE
        defaultTransactionCRMShouldBeFound("telephone.doesNotContain=" + UPDATED_TELEPHONE);
    }

    @Test
    @Transactional
    void getAllTransactionCRMSBySourceIsEqualToSomething() throws Exception {
        // Initialize the database
        transactionCRMRepository.saveAndFlush(transactionCRM);

        // Get all the transactionCRMList where source equals to DEFAULT_SOURCE
        defaultTransactionCRMShouldBeFound("source.equals=" + DEFAULT_SOURCE);

        // Get all the transactionCRMList where source equals to UPDATED_SOURCE
        defaultTransactionCRMShouldNotBeFound("source.equals=" + UPDATED_SOURCE);
    }

    @Test
    @Transactional
    void getAllTransactionCRMSBySourceIsNotEqualToSomething() throws Exception {
        // Initialize the database
        transactionCRMRepository.saveAndFlush(transactionCRM);

        // Get all the transactionCRMList where source not equals to DEFAULT_SOURCE
        defaultTransactionCRMShouldNotBeFound("source.notEquals=" + DEFAULT_SOURCE);

        // Get all the transactionCRMList where source not equals to UPDATED_SOURCE
        defaultTransactionCRMShouldBeFound("source.notEquals=" + UPDATED_SOURCE);
    }

    @Test
    @Transactional
    void getAllTransactionCRMSBySourceIsInShouldWork() throws Exception {
        // Initialize the database
        transactionCRMRepository.saveAndFlush(transactionCRM);

        // Get all the transactionCRMList where source in DEFAULT_SOURCE or UPDATED_SOURCE
        defaultTransactionCRMShouldBeFound("source.in=" + DEFAULT_SOURCE + "," + UPDATED_SOURCE);

        // Get all the transactionCRMList where source equals to UPDATED_SOURCE
        defaultTransactionCRMShouldNotBeFound("source.in=" + UPDATED_SOURCE);
    }

    @Test
    @Transactional
    void getAllTransactionCRMSBySourceIsNullOrNotNull() throws Exception {
        // Initialize the database
        transactionCRMRepository.saveAndFlush(transactionCRM);

        // Get all the transactionCRMList where source is not null
        defaultTransactionCRMShouldBeFound("source.specified=true");

        // Get all the transactionCRMList where source is null
        defaultTransactionCRMShouldNotBeFound("source.specified=false");
    }

    @Test
    @Transactional
    void getAllTransactionCRMSByLatitudeIsEqualToSomething() throws Exception {
        // Initialize the database
        transactionCRMRepository.saveAndFlush(transactionCRM);

        // Get all the transactionCRMList where latitude equals to DEFAULT_LATITUDE
        defaultTransactionCRMShouldBeFound("latitude.equals=" + DEFAULT_LATITUDE);

        // Get all the transactionCRMList where latitude equals to UPDATED_LATITUDE
        defaultTransactionCRMShouldNotBeFound("latitude.equals=" + UPDATED_LATITUDE);
    }

    @Test
    @Transactional
    void getAllTransactionCRMSByLatitudeIsNotEqualToSomething() throws Exception {
        // Initialize the database
        transactionCRMRepository.saveAndFlush(transactionCRM);

        // Get all the transactionCRMList where latitude not equals to DEFAULT_LATITUDE
        defaultTransactionCRMShouldNotBeFound("latitude.notEquals=" + DEFAULT_LATITUDE);

        // Get all the transactionCRMList where latitude not equals to UPDATED_LATITUDE
        defaultTransactionCRMShouldBeFound("latitude.notEquals=" + UPDATED_LATITUDE);
    }

    @Test
    @Transactional
    void getAllTransactionCRMSByLatitudeIsInShouldWork() throws Exception {
        // Initialize the database
        transactionCRMRepository.saveAndFlush(transactionCRM);

        // Get all the transactionCRMList where latitude in DEFAULT_LATITUDE or UPDATED_LATITUDE
        defaultTransactionCRMShouldBeFound("latitude.in=" + DEFAULT_LATITUDE + "," + UPDATED_LATITUDE);

        // Get all the transactionCRMList where latitude equals to UPDATED_LATITUDE
        defaultTransactionCRMShouldNotBeFound("latitude.in=" + UPDATED_LATITUDE);
    }

    @Test
    @Transactional
    void getAllTransactionCRMSByLatitudeIsNullOrNotNull() throws Exception {
        // Initialize the database
        transactionCRMRepository.saveAndFlush(transactionCRM);

        // Get all the transactionCRMList where latitude is not null
        defaultTransactionCRMShouldBeFound("latitude.specified=true");

        // Get all the transactionCRMList where latitude is null
        defaultTransactionCRMShouldNotBeFound("latitude.specified=false");
    }

    @Test
    @Transactional
    void getAllTransactionCRMSByLatitudeIsGreaterThanOrEqualToSomething() throws Exception {
        // Initialize the database
        transactionCRMRepository.saveAndFlush(transactionCRM);

        // Get all the transactionCRMList where latitude is greater than or equal to DEFAULT_LATITUDE
        defaultTransactionCRMShouldBeFound("latitude.greaterThanOrEqual=" + DEFAULT_LATITUDE);

        // Get all the transactionCRMList where latitude is greater than or equal to UPDATED_LATITUDE
        defaultTransactionCRMShouldNotBeFound("latitude.greaterThanOrEqual=" + UPDATED_LATITUDE);
    }

    @Test
    @Transactional
    void getAllTransactionCRMSByLatitudeIsLessThanOrEqualToSomething() throws Exception {
        // Initialize the database
        transactionCRMRepository.saveAndFlush(transactionCRM);

        // Get all the transactionCRMList where latitude is less than or equal to DEFAULT_LATITUDE
        defaultTransactionCRMShouldBeFound("latitude.lessThanOrEqual=" + DEFAULT_LATITUDE);

        // Get all the transactionCRMList where latitude is less than or equal to SMALLER_LATITUDE
        defaultTransactionCRMShouldNotBeFound("latitude.lessThanOrEqual=" + SMALLER_LATITUDE);
    }

    @Test
    @Transactional
    void getAllTransactionCRMSByLatitudeIsLessThanSomething() throws Exception {
        // Initialize the database
        transactionCRMRepository.saveAndFlush(transactionCRM);

        // Get all the transactionCRMList where latitude is less than DEFAULT_LATITUDE
        defaultTransactionCRMShouldNotBeFound("latitude.lessThan=" + DEFAULT_LATITUDE);

        // Get all the transactionCRMList where latitude is less than UPDATED_LATITUDE
        defaultTransactionCRMShouldBeFound("latitude.lessThan=" + UPDATED_LATITUDE);
    }

    @Test
    @Transactional
    void getAllTransactionCRMSByLatitudeIsGreaterThanSomething() throws Exception {
        // Initialize the database
        transactionCRMRepository.saveAndFlush(transactionCRM);

        // Get all the transactionCRMList where latitude is greater than DEFAULT_LATITUDE
        defaultTransactionCRMShouldNotBeFound("latitude.greaterThan=" + DEFAULT_LATITUDE);

        // Get all the transactionCRMList where latitude is greater than SMALLER_LATITUDE
        defaultTransactionCRMShouldBeFound("latitude.greaterThan=" + SMALLER_LATITUDE);
    }

    @Test
    @Transactional
    void getAllTransactionCRMSByLongitudeIsEqualToSomething() throws Exception {
        // Initialize the database
        transactionCRMRepository.saveAndFlush(transactionCRM);

        // Get all the transactionCRMList where longitude equals to DEFAULT_LONGITUDE
        defaultTransactionCRMShouldBeFound("longitude.equals=" + DEFAULT_LONGITUDE);

        // Get all the transactionCRMList where longitude equals to UPDATED_LONGITUDE
        defaultTransactionCRMShouldNotBeFound("longitude.equals=" + UPDATED_LONGITUDE);
    }

    @Test
    @Transactional
    void getAllTransactionCRMSByLongitudeIsNotEqualToSomething() throws Exception {
        // Initialize the database
        transactionCRMRepository.saveAndFlush(transactionCRM);

        // Get all the transactionCRMList where longitude not equals to DEFAULT_LONGITUDE
        defaultTransactionCRMShouldNotBeFound("longitude.notEquals=" + DEFAULT_LONGITUDE);

        // Get all the transactionCRMList where longitude not equals to UPDATED_LONGITUDE
        defaultTransactionCRMShouldBeFound("longitude.notEquals=" + UPDATED_LONGITUDE);
    }

    @Test
    @Transactional
    void getAllTransactionCRMSByLongitudeIsInShouldWork() throws Exception {
        // Initialize the database
        transactionCRMRepository.saveAndFlush(transactionCRM);

        // Get all the transactionCRMList where longitude in DEFAULT_LONGITUDE or UPDATED_LONGITUDE
        defaultTransactionCRMShouldBeFound("longitude.in=" + DEFAULT_LONGITUDE + "," + UPDATED_LONGITUDE);

        // Get all the transactionCRMList where longitude equals to UPDATED_LONGITUDE
        defaultTransactionCRMShouldNotBeFound("longitude.in=" + UPDATED_LONGITUDE);
    }

    @Test
    @Transactional
    void getAllTransactionCRMSByLongitudeIsNullOrNotNull() throws Exception {
        // Initialize the database
        transactionCRMRepository.saveAndFlush(transactionCRM);

        // Get all the transactionCRMList where longitude is not null
        defaultTransactionCRMShouldBeFound("longitude.specified=true");

        // Get all the transactionCRMList where longitude is null
        defaultTransactionCRMShouldNotBeFound("longitude.specified=false");
    }

    @Test
    @Transactional
    void getAllTransactionCRMSByLongitudeIsGreaterThanOrEqualToSomething() throws Exception {
        // Initialize the database
        transactionCRMRepository.saveAndFlush(transactionCRM);

        // Get all the transactionCRMList where longitude is greater than or equal to DEFAULT_LONGITUDE
        defaultTransactionCRMShouldBeFound("longitude.greaterThanOrEqual=" + DEFAULT_LONGITUDE);

        // Get all the transactionCRMList where longitude is greater than or equal to UPDATED_LONGITUDE
        defaultTransactionCRMShouldNotBeFound("longitude.greaterThanOrEqual=" + UPDATED_LONGITUDE);
    }

    @Test
    @Transactional
    void getAllTransactionCRMSByLongitudeIsLessThanOrEqualToSomething() throws Exception {
        // Initialize the database
        transactionCRMRepository.saveAndFlush(transactionCRM);

        // Get all the transactionCRMList where longitude is less than or equal to DEFAULT_LONGITUDE
        defaultTransactionCRMShouldBeFound("longitude.lessThanOrEqual=" + DEFAULT_LONGITUDE);

        // Get all the transactionCRMList where longitude is less than or equal to SMALLER_LONGITUDE
        defaultTransactionCRMShouldNotBeFound("longitude.lessThanOrEqual=" + SMALLER_LONGITUDE);
    }

    @Test
    @Transactional
    void getAllTransactionCRMSByLongitudeIsLessThanSomething() throws Exception {
        // Initialize the database
        transactionCRMRepository.saveAndFlush(transactionCRM);

        // Get all the transactionCRMList where longitude is less than DEFAULT_LONGITUDE
        defaultTransactionCRMShouldNotBeFound("longitude.lessThan=" + DEFAULT_LONGITUDE);

        // Get all the transactionCRMList where longitude is less than UPDATED_LONGITUDE
        defaultTransactionCRMShouldBeFound("longitude.lessThan=" + UPDATED_LONGITUDE);
    }

    @Test
    @Transactional
    void getAllTransactionCRMSByLongitudeIsGreaterThanSomething() throws Exception {
        // Initialize the database
        transactionCRMRepository.saveAndFlush(transactionCRM);

        // Get all the transactionCRMList where longitude is greater than DEFAULT_LONGITUDE
        defaultTransactionCRMShouldNotBeFound("longitude.greaterThan=" + DEFAULT_LONGITUDE);

        // Get all the transactionCRMList where longitude is greater than SMALLER_LONGITUDE
        defaultTransactionCRMShouldBeFound("longitude.greaterThan=" + SMALLER_LONGITUDE);
    }

    @Test
    @Transactional
    void getAllTransactionCRMSByMonnaieIsEqualToSomething() throws Exception {
        // Initialize the database
        transactionCRMRepository.saveAndFlush(transactionCRM);
        Monnaie monnaie;
        if (TestUtil.findAll(em, Monnaie.class).isEmpty()) {
            monnaie = MonnaieResourceIT.createEntity(em);
            em.persist(monnaie);
            em.flush();
        } else {
            monnaie = TestUtil.findAll(em, Monnaie.class).get(0);
        }
        em.persist(monnaie);
        em.flush();
        transactionCRM.setMonnaie(monnaie);
        transactionCRMRepository.saveAndFlush(transactionCRM);
        Long monnaieId = monnaie.getId();

        // Get all the transactionCRMList where monnaie equals to monnaieId
        defaultTransactionCRMShouldBeFound("monnaieId.equals=" + monnaieId);

        // Get all the transactionCRMList where monnaie equals to (monnaieId + 1)
        defaultTransactionCRMShouldNotBeFound("monnaieId.equals=" + (monnaieId + 1));
    }

    @Test
    @Transactional
    void getAllTransactionCRMSByChargeAffaireIsEqualToSomething() throws Exception {
        // Initialize the database
        transactionCRMRepository.saveAndFlush(transactionCRM);
        Employee chargeAffaire;
        if (TestUtil.findAll(em, Employee.class).isEmpty()) {
            chargeAffaire = EmployeeResourceIT.createEntity(em);
            em.persist(chargeAffaire);
            em.flush();
        } else {
            chargeAffaire = TestUtil.findAll(em, Employee.class).get(0);
        }
        em.persist(chargeAffaire);
        em.flush();
        transactionCRM.setChargeAffaire(chargeAffaire);
        transactionCRMRepository.saveAndFlush(transactionCRM);
        Long chargeAffaireId = chargeAffaire.getId();

        // Get all the transactionCRMList where chargeAffaire equals to chargeAffaireId
        defaultTransactionCRMShouldBeFound("chargeAffaireId.equals=" + chargeAffaireId);

        // Get all the transactionCRMList where chargeAffaire equals to (chargeAffaireId + 1)
        defaultTransactionCRMShouldNotBeFound("chargeAffaireId.equals=" + (chargeAffaireId + 1));
    }

    @Test
    @Transactional
    void getAllTransactionCRMSByClientIsEqualToSomething() throws Exception {
        // Initialize the database
        transactionCRMRepository.saveAndFlush(transactionCRM);
        Customer client;
        if (TestUtil.findAll(em, Customer.class).isEmpty()) {
            client = CustomerResourceIT.createEntity(em);
            em.persist(client);
            em.flush();
        } else {
            client = TestUtil.findAll(em, Customer.class).get(0);
        }
        em.persist(client);
        em.flush();
        transactionCRM.setClient(client);
        transactionCRMRepository.saveAndFlush(transactionCRM);
        Long clientId = client.getId();

        // Get all the transactionCRMList where client equals to clientId
        defaultTransactionCRMShouldBeFound("clientId.equals=" + clientId);

        // Get all the transactionCRMList where client equals to (clientId + 1)
        defaultTransactionCRMShouldNotBeFound("clientId.equals=" + (clientId + 1));
    }

    @Test
    @Transactional
    void getAllTransactionCRMSByActiviteIsEqualToSomething() throws Exception {
        // Initialize the database
        transactionCRMRepository.saveAndFlush(transactionCRM);
        Activite activite;
        if (TestUtil.findAll(em, Activite.class).isEmpty()) {
            activite = ActiviteResourceIT.createEntity(em);
            em.persist(activite);
            em.flush();
        } else {
            activite = TestUtil.findAll(em, Activite.class).get(0);
        }
        em.persist(activite);
        em.flush();
        transactionCRM.addActivite(activite);
        transactionCRMRepository.saveAndFlush(transactionCRM);
        Long activiteId = activite.getId();

        // Get all the transactionCRMList where activite equals to activiteId
        defaultTransactionCRMShouldBeFound("activiteId.equals=" + activiteId);

        // Get all the transactionCRMList where activite equals to (activiteId + 1)
        defaultTransactionCRMShouldNotBeFound("activiteId.equals=" + (activiteId + 1));
    }

    /**
     * Executes the search, and checks that the default entity is returned.
     */
    private void defaultTransactionCRMShouldBeFound(String filter) throws Exception {
        restTransactionCRMMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(transactionCRM.getId().intValue())))
            .andExpect(jsonPath("$.[*].reference").value(hasItem(DEFAULT_REFERENCE)))
            .andExpect(jsonPath("$.[*].montant").value(hasItem(sameNumber(DEFAULT_MONTANT))))
            .andExpect(jsonPath("$.[*].transactionEtape").value(hasItem(DEFAULT_TRANSACTION_ETAPE.toString())))
            .andExpect(jsonPath("$.[*].dateFin").value(hasItem(DEFAULT_DATE_FIN.toString())))
            .andExpect(jsonPath("$.[*].transactionRecurrente").value(hasItem(DEFAULT_TRANSACTION_RECURRENTE.booleanValue())))
            .andExpect(jsonPath("$.[*].creeLe").value(hasItem(sameInstant(DEFAULT_CREE_LE))))
            .andExpect(jsonPath("$.[*].dernierUpdate").value(hasItem(sameInstant(DEFAULT_DERNIER_UPDATE))))
            .andExpect(jsonPath("$.[*].telephone").value(hasItem(DEFAULT_TELEPHONE)))
            .andExpect(jsonPath("$.[*].source").value(hasItem(DEFAULT_SOURCE.toString())))
            .andExpect(jsonPath("$.[*].adresse").value(hasItem(DEFAULT_ADRESSE.toString())))
            .andExpect(jsonPath("$.[*].notes").value(hasItem(DEFAULT_NOTES.toString())))
            .andExpect(jsonPath("$.[*].latitude").value(hasItem(sameNumber(DEFAULT_LATITUDE))))
            .andExpect(jsonPath("$.[*].longitude").value(hasItem(sameNumber(DEFAULT_LONGITUDE))));

        // Check, that the count call also returns 1
        restTransactionCRMMockMvc
            .perform(get(ENTITY_API_URL + "/count?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(content().string("1"));
    }

    /**
     * Executes the search, and checks that the default entity is not returned.
     */
    private void defaultTransactionCRMShouldNotBeFound(String filter) throws Exception {
        restTransactionCRMMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$").isArray())
            .andExpect(jsonPath("$").isEmpty());

        // Check, that the count call also returns 0
        restTransactionCRMMockMvc
            .perform(get(ENTITY_API_URL + "/count?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(content().string("0"));
    }

    @Test
    @Transactional
    void getNonExistingTransactionCRM() throws Exception {
        // Get the transactionCRM
        restTransactionCRMMockMvc.perform(get(ENTITY_API_URL_ID, Long.MAX_VALUE)).andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    void putNewTransactionCRM() throws Exception {
        // Initialize the database
        transactionCRMRepository.saveAndFlush(transactionCRM);

        int databaseSizeBeforeUpdate = transactionCRMRepository.findAll().size();

        // Update the transactionCRM
        TransactionCRM updatedTransactionCRM = transactionCRMRepository.findById(transactionCRM.getId()).get();
        // Disconnect from session so that the updates on updatedTransactionCRM are not directly saved in db
        em.detach(updatedTransactionCRM);
        updatedTransactionCRM
            .reference(UPDATED_REFERENCE)
            .montant(UPDATED_MONTANT)
            .transactionEtape(UPDATED_TRANSACTION_ETAPE)
            .dateFin(UPDATED_DATE_FIN)
            .transactionRecurrente(UPDATED_TRANSACTION_RECURRENTE)
            .creeLe(UPDATED_CREE_LE)
            .dernierUpdate(UPDATED_DERNIER_UPDATE)
            .telephone(UPDATED_TELEPHONE)
            .source(UPDATED_SOURCE)
            .adresse(UPDATED_ADRESSE)
            .notes(UPDATED_NOTES)
            .latitude(UPDATED_LATITUDE)
            .longitude(UPDATED_LONGITUDE);

        restTransactionCRMMockMvc
            .perform(
                put(ENTITY_API_URL_ID, updatedTransactionCRM.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(updatedTransactionCRM))
            )
            .andExpect(status().isOk());

        // Validate the TransactionCRM in the database
        List<TransactionCRM> transactionCRMList = transactionCRMRepository.findAll();
        assertThat(transactionCRMList).hasSize(databaseSizeBeforeUpdate);
        TransactionCRM testTransactionCRM = transactionCRMList.get(transactionCRMList.size() - 1);
        assertThat(testTransactionCRM.getReference()).isEqualTo(UPDATED_REFERENCE);
        assertThat(testTransactionCRM.getMontant()).isEqualByComparingTo(UPDATED_MONTANT);
        assertThat(testTransactionCRM.getTransactionEtape()).isEqualTo(UPDATED_TRANSACTION_ETAPE);
        assertThat(testTransactionCRM.getDateFin()).isEqualTo(UPDATED_DATE_FIN);
        assertThat(testTransactionCRM.getTransactionRecurrente()).isEqualTo(UPDATED_TRANSACTION_RECURRENTE);
        assertThat(testTransactionCRM.getCreeLe()).isEqualTo(UPDATED_CREE_LE);
        assertThat(testTransactionCRM.getDernierUpdate()).isEqualTo(UPDATED_DERNIER_UPDATE);
        assertThat(testTransactionCRM.getTelephone()).isEqualTo(UPDATED_TELEPHONE);
        assertThat(testTransactionCRM.getSource()).isEqualTo(UPDATED_SOURCE);
        assertThat(testTransactionCRM.getAdresse()).isEqualTo(UPDATED_ADRESSE);
        assertThat(testTransactionCRM.getNotes()).isEqualTo(UPDATED_NOTES);
        assertThat(testTransactionCRM.getLatitude()).isEqualByComparingTo(UPDATED_LATITUDE);
        assertThat(testTransactionCRM.getLongitude()).isEqualByComparingTo(UPDATED_LONGITUDE);
    }

    @Test
    @Transactional
    void putNonExistingTransactionCRM() throws Exception {
        int databaseSizeBeforeUpdate = transactionCRMRepository.findAll().size();
        transactionCRM.setId(count.incrementAndGet());

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restTransactionCRMMockMvc
            .perform(
                put(ENTITY_API_URL_ID, transactionCRM.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(transactionCRM))
            )
            .andExpect(status().isBadRequest());

        // Validate the TransactionCRM in the database
        List<TransactionCRM> transactionCRMList = transactionCRMRepository.findAll();
        assertThat(transactionCRMList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithIdMismatchTransactionCRM() throws Exception {
        int databaseSizeBeforeUpdate = transactionCRMRepository.findAll().size();
        transactionCRM.setId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restTransactionCRMMockMvc
            .perform(
                put(ENTITY_API_URL_ID, count.incrementAndGet())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(transactionCRM))
            )
            .andExpect(status().isBadRequest());

        // Validate the TransactionCRM in the database
        List<TransactionCRM> transactionCRMList = transactionCRMRepository.findAll();
        assertThat(transactionCRMList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithMissingIdPathParamTransactionCRM() throws Exception {
        int databaseSizeBeforeUpdate = transactionCRMRepository.findAll().size();
        transactionCRM.setId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restTransactionCRMMockMvc
            .perform(put(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(transactionCRM)))
            .andExpect(status().isMethodNotAllowed());

        // Validate the TransactionCRM in the database
        List<TransactionCRM> transactionCRMList = transactionCRMRepository.findAll();
        assertThat(transactionCRMList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void partialUpdateTransactionCRMWithPatch() throws Exception {
        // Initialize the database
        transactionCRMRepository.saveAndFlush(transactionCRM);

        int databaseSizeBeforeUpdate = transactionCRMRepository.findAll().size();

        // Update the transactionCRM using partial update
        TransactionCRM partialUpdatedTransactionCRM = new TransactionCRM();
        partialUpdatedTransactionCRM.setId(transactionCRM.getId());

        partialUpdatedTransactionCRM
            .transactionEtape(UPDATED_TRANSACTION_ETAPE)
            .dateFin(UPDATED_DATE_FIN)
            .creeLe(UPDATED_CREE_LE)
            .dernierUpdate(UPDATED_DERNIER_UPDATE)
            .adresse(UPDATED_ADRESSE)
            .latitude(UPDATED_LATITUDE)
            .longitude(UPDATED_LONGITUDE);

        restTransactionCRMMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedTransactionCRM.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedTransactionCRM))
            )
            .andExpect(status().isOk());

        // Validate the TransactionCRM in the database
        List<TransactionCRM> transactionCRMList = transactionCRMRepository.findAll();
        assertThat(transactionCRMList).hasSize(databaseSizeBeforeUpdate);
        TransactionCRM testTransactionCRM = transactionCRMList.get(transactionCRMList.size() - 1);
        assertThat(testTransactionCRM.getReference()).isEqualTo(DEFAULT_REFERENCE);
        assertThat(testTransactionCRM.getMontant()).isEqualByComparingTo(DEFAULT_MONTANT);
        assertThat(testTransactionCRM.getTransactionEtape()).isEqualTo(UPDATED_TRANSACTION_ETAPE);
        assertThat(testTransactionCRM.getDateFin()).isEqualTo(UPDATED_DATE_FIN);
        assertThat(testTransactionCRM.getTransactionRecurrente()).isEqualTo(DEFAULT_TRANSACTION_RECURRENTE);
        assertThat(testTransactionCRM.getCreeLe()).isEqualTo(UPDATED_CREE_LE);
        assertThat(testTransactionCRM.getDernierUpdate()).isEqualTo(UPDATED_DERNIER_UPDATE);
        assertThat(testTransactionCRM.getTelephone()).isEqualTo(DEFAULT_TELEPHONE);
        assertThat(testTransactionCRM.getSource()).isEqualTo(DEFAULT_SOURCE);
        assertThat(testTransactionCRM.getAdresse()).isEqualTo(UPDATED_ADRESSE);
        assertThat(testTransactionCRM.getNotes()).isEqualTo(DEFAULT_NOTES);
        assertThat(testTransactionCRM.getLatitude()).isEqualByComparingTo(UPDATED_LATITUDE);
        assertThat(testTransactionCRM.getLongitude()).isEqualByComparingTo(UPDATED_LONGITUDE);
    }

    @Test
    @Transactional
    void fullUpdateTransactionCRMWithPatch() throws Exception {
        // Initialize the database
        transactionCRMRepository.saveAndFlush(transactionCRM);

        int databaseSizeBeforeUpdate = transactionCRMRepository.findAll().size();

        // Update the transactionCRM using partial update
        TransactionCRM partialUpdatedTransactionCRM = new TransactionCRM();
        partialUpdatedTransactionCRM.setId(transactionCRM.getId());

        partialUpdatedTransactionCRM
            .reference(UPDATED_REFERENCE)
            .montant(UPDATED_MONTANT)
            .transactionEtape(UPDATED_TRANSACTION_ETAPE)
            .dateFin(UPDATED_DATE_FIN)
            .transactionRecurrente(UPDATED_TRANSACTION_RECURRENTE)
            .creeLe(UPDATED_CREE_LE)
            .dernierUpdate(UPDATED_DERNIER_UPDATE)
            .telephone(UPDATED_TELEPHONE)
            .source(UPDATED_SOURCE)
            .adresse(UPDATED_ADRESSE)
            .notes(UPDATED_NOTES)
            .latitude(UPDATED_LATITUDE)
            .longitude(UPDATED_LONGITUDE);

        restTransactionCRMMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedTransactionCRM.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedTransactionCRM))
            )
            .andExpect(status().isOk());

        // Validate the TransactionCRM in the database
        List<TransactionCRM> transactionCRMList = transactionCRMRepository.findAll();
        assertThat(transactionCRMList).hasSize(databaseSizeBeforeUpdate);
        TransactionCRM testTransactionCRM = transactionCRMList.get(transactionCRMList.size() - 1);
        assertThat(testTransactionCRM.getReference()).isEqualTo(UPDATED_REFERENCE);
        assertThat(testTransactionCRM.getMontant()).isEqualByComparingTo(UPDATED_MONTANT);
        assertThat(testTransactionCRM.getTransactionEtape()).isEqualTo(UPDATED_TRANSACTION_ETAPE);
        assertThat(testTransactionCRM.getDateFin()).isEqualTo(UPDATED_DATE_FIN);
        assertThat(testTransactionCRM.getTransactionRecurrente()).isEqualTo(UPDATED_TRANSACTION_RECURRENTE);
        assertThat(testTransactionCRM.getCreeLe()).isEqualTo(UPDATED_CREE_LE);
        assertThat(testTransactionCRM.getDernierUpdate()).isEqualTo(UPDATED_DERNIER_UPDATE);
        assertThat(testTransactionCRM.getTelephone()).isEqualTo(UPDATED_TELEPHONE);
        assertThat(testTransactionCRM.getSource()).isEqualTo(UPDATED_SOURCE);
        assertThat(testTransactionCRM.getAdresse()).isEqualTo(UPDATED_ADRESSE);
        assertThat(testTransactionCRM.getNotes()).isEqualTo(UPDATED_NOTES);
        assertThat(testTransactionCRM.getLatitude()).isEqualByComparingTo(UPDATED_LATITUDE);
        assertThat(testTransactionCRM.getLongitude()).isEqualByComparingTo(UPDATED_LONGITUDE);
    }

    @Test
    @Transactional
    void patchNonExistingTransactionCRM() throws Exception {
        int databaseSizeBeforeUpdate = transactionCRMRepository.findAll().size();
        transactionCRM.setId(count.incrementAndGet());

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restTransactionCRMMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, transactionCRM.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(transactionCRM))
            )
            .andExpect(status().isBadRequest());

        // Validate the TransactionCRM in the database
        List<TransactionCRM> transactionCRMList = transactionCRMRepository.findAll();
        assertThat(transactionCRMList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithIdMismatchTransactionCRM() throws Exception {
        int databaseSizeBeforeUpdate = transactionCRMRepository.findAll().size();
        transactionCRM.setId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restTransactionCRMMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, count.incrementAndGet())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(transactionCRM))
            )
            .andExpect(status().isBadRequest());

        // Validate the TransactionCRM in the database
        List<TransactionCRM> transactionCRMList = transactionCRMRepository.findAll();
        assertThat(transactionCRMList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithMissingIdPathParamTransactionCRM() throws Exception {
        int databaseSizeBeforeUpdate = transactionCRMRepository.findAll().size();
        transactionCRM.setId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restTransactionCRMMockMvc
            .perform(
                patch(ENTITY_API_URL).contentType("application/merge-patch+json").content(TestUtil.convertObjectToJsonBytes(transactionCRM))
            )
            .andExpect(status().isMethodNotAllowed());

        // Validate the TransactionCRM in the database
        List<TransactionCRM> transactionCRMList = transactionCRMRepository.findAll();
        assertThat(transactionCRMList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void deleteTransactionCRM() throws Exception {
        // Initialize the database
        transactionCRMRepository.saveAndFlush(transactionCRM);

        int databaseSizeBeforeDelete = transactionCRMRepository.findAll().size();

        // Delete the transactionCRM
        restTransactionCRMMockMvc
            .perform(delete(ENTITY_API_URL_ID, transactionCRM.getId()).accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        List<TransactionCRM> transactionCRMList = transactionCRMRepository.findAll();
        assertThat(transactionCRMList).hasSize(databaseSizeBeforeDelete - 1);
    }
}
