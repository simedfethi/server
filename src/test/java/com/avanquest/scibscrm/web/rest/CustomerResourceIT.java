package com.avanquest.scibscrm.web.rest;

import static com.avanquest.scibscrm.web.rest.TestUtil.sameNumber;
import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import com.avanquest.scibscrm.IntegrationTest;
import com.avanquest.scibscrm.domain.Customer;
import com.avanquest.scibscrm.domain.Employee;
import com.avanquest.scibscrm.domain.enumeration.CustomerType;
import com.avanquest.scibscrm.repository.CustomerRepository;
import com.avanquest.scibscrm.service.CustomerService;
import com.avanquest.scibscrm.service.criteria.CustomerCriteria;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.ZoneId;
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
 * Integration tests for the {@link CustomerResource} REST controller.
 */
@IntegrationTest
@ExtendWith(MockitoExtension.class)
@AutoConfigureMockMvc
@WithMockUser
class CustomerResourceIT {

    private static final CustomerType DEFAULT_CUSTOMER_TYPE = CustomerType.PHYSIQUE;
    private static final CustomerType UPDATED_CUSTOMER_TYPE = CustomerType.MORALE;

    private static final String DEFAULT_COMPANY = "AAAAAAAAAA";
    private static final String UPDATED_COMPANY = "BBBBBBBBBB";

    private static final String DEFAULT_LAST_NAME = "AAAAAAAAAA";
    private static final String UPDATED_LAST_NAME = "BBBBBBBBBB";

    private static final String DEFAULT_FIRST_NAME = "AAAAAAAAAA";
    private static final String UPDATED_FIRST_NAME = "BBBBBBBBBB";

    private static final String DEFAULT_EMAIL_ADDRESS = "AAAAAAAAAA";
    private static final String UPDATED_EMAIL_ADDRESS = "BBBBBBBBBB";

    private static final String DEFAULT_JOB_TITLE = "AAAAAAAAAA";
    private static final String UPDATED_JOB_TITLE = "BBBBBBBBBB";

    private static final String DEFAULT_BUSINESS_PHONE = "AAAAAAAAAA";
    private static final String UPDATED_BUSINESS_PHONE = "BBBBBBBBBB";

    private static final String DEFAULT_HOME_PHONE = "AAAAAAAAAA";
    private static final String UPDATED_HOME_PHONE = "BBBBBBBBBB";

    private static final String DEFAULT_MOBILE_PHONE = "AAAAAAAAAA";
    private static final String UPDATED_MOBILE_PHONE = "BBBBBBBBBB";

    private static final String DEFAULT_FAX_NUMBER = "AAAAAAAAAA";
    private static final String UPDATED_FAX_NUMBER = "BBBBBBBBBB";

    private static final String DEFAULT_ADDRESSE = "AAAAAAAAAA";
    private static final String UPDATED_ADDRESSE = "BBBBBBBBBB";

    private static final String DEFAULT_WILAYA = "AAAAAAAAAA";
    private static final String UPDATED_WILAYA = "BBBBBBBBBB";

    private static final String DEFAULT_DAIRA = "AAAAAAAAAA";
    private static final String UPDATED_DAIRA = "BBBBBBBBBB";

    private static final String DEFAULT_CODE_POSTAL = "AAAAAAAAAA";
    private static final String UPDATED_CODE_POSTAL = "BBBBBBBBBB";

    private static final String DEFAULT_COMMUNE = "AAAAAAAAAA";
    private static final String UPDATED_COMMUNE = "BBBBBBBBBB";

    private static final String DEFAULT_WEB_PAGE = "AAAAAAAAAA";
    private static final String UPDATED_WEB_PAGE = "BBBBBBBBBB";

    private static final String DEFAULT_NOTES = "AAAAAAAAAA";
    private static final String UPDATED_NOTES = "BBBBBBBBBB";

    private static final byte[] DEFAULT_ATTACHMENTS = TestUtil.createByteArray(1, "0");
    private static final byte[] UPDATED_ATTACHMENTS = TestUtil.createByteArray(1, "1");
    private static final String DEFAULT_ATTACHMENTS_CONTENT_TYPE = "image/jpg";
    private static final String UPDATED_ATTACHMENTS_CONTENT_TYPE = "image/png";

    private static final Boolean DEFAULT_DEJA_CLIENT = false;
    private static final Boolean UPDATED_DEJA_CLIENT = true;

    private static final LocalDate DEFAULT_DATE_DERNIERE_VISTE = LocalDate.ofEpochDay(0L);
    private static final LocalDate UPDATED_DATE_DERNIERE_VISTE = LocalDate.now(ZoneId.systemDefault());
    private static final LocalDate SMALLER_DATE_DERNIERE_VISTE = LocalDate.ofEpochDay(-1L);

    private static final BigDecimal DEFAULT_LATITUDE = new BigDecimal(1);
    private static final BigDecimal UPDATED_LATITUDE = new BigDecimal(2);
    private static final BigDecimal SMALLER_LATITUDE = new BigDecimal(1 - 1);

    private static final BigDecimal DEFAULT_LONGITUDE = new BigDecimal(1);
    private static final BigDecimal UPDATED_LONGITUDE = new BigDecimal(2);
    private static final BigDecimal SMALLER_LONGITUDE = new BigDecimal(1 - 1);

    private static final String ENTITY_API_URL = "/api/customers";
    private static final String ENTITY_API_URL_ID = ENTITY_API_URL + "/{id}";

    private static Random random = new Random();
    private static AtomicLong count = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    @Autowired
    private CustomerRepository customerRepository;

    @Mock
    private CustomerRepository customerRepositoryMock;

    @Mock
    private CustomerService customerServiceMock;

    @Autowired
    private EntityManager em;

    @Autowired
    private MockMvc restCustomerMockMvc;

    private Customer customer;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Customer createEntity(EntityManager em) {
        Customer customer = new Customer()
            .customerType(DEFAULT_CUSTOMER_TYPE)
            .company(DEFAULT_COMPANY)
            .lastName(DEFAULT_LAST_NAME)
            .firstName(DEFAULT_FIRST_NAME)
            .emailAddress(DEFAULT_EMAIL_ADDRESS)
            .jobTitle(DEFAULT_JOB_TITLE)
            .businessPhone(DEFAULT_BUSINESS_PHONE)
            .homePhone(DEFAULT_HOME_PHONE)
            .mobilePhone(DEFAULT_MOBILE_PHONE)
            .faxNumber(DEFAULT_FAX_NUMBER)
            .addresse(DEFAULT_ADDRESSE)
            .wilaya(DEFAULT_WILAYA)
            .daira(DEFAULT_DAIRA)
            .codePostal(DEFAULT_CODE_POSTAL)
            .commune(DEFAULT_COMMUNE)
            .webPage(DEFAULT_WEB_PAGE)
            .notes(DEFAULT_NOTES)
            .attachments(DEFAULT_ATTACHMENTS)
            .attachmentsContentType(DEFAULT_ATTACHMENTS_CONTENT_TYPE)
            .dejaClient(DEFAULT_DEJA_CLIENT)
            .dateDerniereViste(DEFAULT_DATE_DERNIERE_VISTE)
            .latitude(DEFAULT_LATITUDE)
            .longitude(DEFAULT_LONGITUDE);
        return customer;
    }

    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Customer createUpdatedEntity(EntityManager em) {
        Customer customer = new Customer()
            .customerType(UPDATED_CUSTOMER_TYPE)
            .company(UPDATED_COMPANY)
            .lastName(UPDATED_LAST_NAME)
            .firstName(UPDATED_FIRST_NAME)
            .emailAddress(UPDATED_EMAIL_ADDRESS)
            .jobTitle(UPDATED_JOB_TITLE)
            .businessPhone(UPDATED_BUSINESS_PHONE)
            .homePhone(UPDATED_HOME_PHONE)
            .mobilePhone(UPDATED_MOBILE_PHONE)
            .faxNumber(UPDATED_FAX_NUMBER)
            .addresse(UPDATED_ADDRESSE)
            .wilaya(UPDATED_WILAYA)
            .daira(UPDATED_DAIRA)
            .codePostal(UPDATED_CODE_POSTAL)
            .commune(UPDATED_COMMUNE)
            .webPage(UPDATED_WEB_PAGE)
            .notes(UPDATED_NOTES)
            .attachments(UPDATED_ATTACHMENTS)
            .attachmentsContentType(UPDATED_ATTACHMENTS_CONTENT_TYPE)
            .dejaClient(UPDATED_DEJA_CLIENT)
            .dateDerniereViste(UPDATED_DATE_DERNIERE_VISTE)
            .latitude(UPDATED_LATITUDE)
            .longitude(UPDATED_LONGITUDE);
        return customer;
    }

    @BeforeEach
    public void initTest() {
        customer = createEntity(em);
    }

    @Test
    @Transactional
    void createCustomer() throws Exception {
        int databaseSizeBeforeCreate = customerRepository.findAll().size();
        // Create the Customer
        restCustomerMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(customer)))
            .andExpect(status().isCreated());

        // Validate the Customer in the database
        List<Customer> customerList = customerRepository.findAll();
        assertThat(customerList).hasSize(databaseSizeBeforeCreate + 1);
        Customer testCustomer = customerList.get(customerList.size() - 1);
        assertThat(testCustomer.getCustomerType()).isEqualTo(DEFAULT_CUSTOMER_TYPE);
        assertThat(testCustomer.getCompany()).isEqualTo(DEFAULT_COMPANY);
        assertThat(testCustomer.getLastName()).isEqualTo(DEFAULT_LAST_NAME);
        assertThat(testCustomer.getFirstName()).isEqualTo(DEFAULT_FIRST_NAME);
        assertThat(testCustomer.getEmailAddress()).isEqualTo(DEFAULT_EMAIL_ADDRESS);
        assertThat(testCustomer.getJobTitle()).isEqualTo(DEFAULT_JOB_TITLE);
        assertThat(testCustomer.getBusinessPhone()).isEqualTo(DEFAULT_BUSINESS_PHONE);
        assertThat(testCustomer.getHomePhone()).isEqualTo(DEFAULT_HOME_PHONE);
        assertThat(testCustomer.getMobilePhone()).isEqualTo(DEFAULT_MOBILE_PHONE);
        assertThat(testCustomer.getFaxNumber()).isEqualTo(DEFAULT_FAX_NUMBER);
        assertThat(testCustomer.getAddresse()).isEqualTo(DEFAULT_ADDRESSE);
        assertThat(testCustomer.getWilaya()).isEqualTo(DEFAULT_WILAYA);
        assertThat(testCustomer.getDaira()).isEqualTo(DEFAULT_DAIRA);
        assertThat(testCustomer.getCodePostal()).isEqualTo(DEFAULT_CODE_POSTAL);
        assertThat(testCustomer.getCommune()).isEqualTo(DEFAULT_COMMUNE);
        assertThat(testCustomer.getWebPage()).isEqualTo(DEFAULT_WEB_PAGE);
        assertThat(testCustomer.getNotes()).isEqualTo(DEFAULT_NOTES);
        assertThat(testCustomer.getAttachments()).isEqualTo(DEFAULT_ATTACHMENTS);
        assertThat(testCustomer.getAttachmentsContentType()).isEqualTo(DEFAULT_ATTACHMENTS_CONTENT_TYPE);
        assertThat(testCustomer.getDejaClient()).isEqualTo(DEFAULT_DEJA_CLIENT);
        assertThat(testCustomer.getDateDerniereViste()).isEqualTo(DEFAULT_DATE_DERNIERE_VISTE);
        assertThat(testCustomer.getLatitude()).isEqualByComparingTo(DEFAULT_LATITUDE);
        assertThat(testCustomer.getLongitude()).isEqualByComparingTo(DEFAULT_LONGITUDE);
    }

    @Test
    @Transactional
    void createCustomerWithExistingId() throws Exception {
        // Create the Customer with an existing ID
        customer.setId(1L);

        int databaseSizeBeforeCreate = customerRepository.findAll().size();

        // An entity with an existing ID cannot be created, so this API call must fail
        restCustomerMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(customer)))
            .andExpect(status().isBadRequest());

        // Validate the Customer in the database
        List<Customer> customerList = customerRepository.findAll();
        assertThat(customerList).hasSize(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    void getAllCustomers() throws Exception {
        // Initialize the database
        customerRepository.saveAndFlush(customer);

        // Get all the customerList
        restCustomerMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(customer.getId().intValue())))
            .andExpect(jsonPath("$.[*].customerType").value(hasItem(DEFAULT_CUSTOMER_TYPE.toString())))
            .andExpect(jsonPath("$.[*].company").value(hasItem(DEFAULT_COMPANY)))
            .andExpect(jsonPath("$.[*].lastName").value(hasItem(DEFAULT_LAST_NAME)))
            .andExpect(jsonPath("$.[*].firstName").value(hasItem(DEFAULT_FIRST_NAME)))
            .andExpect(jsonPath("$.[*].emailAddress").value(hasItem(DEFAULT_EMAIL_ADDRESS)))
            .andExpect(jsonPath("$.[*].jobTitle").value(hasItem(DEFAULT_JOB_TITLE)))
            .andExpect(jsonPath("$.[*].businessPhone").value(hasItem(DEFAULT_BUSINESS_PHONE)))
            .andExpect(jsonPath("$.[*].homePhone").value(hasItem(DEFAULT_HOME_PHONE)))
            .andExpect(jsonPath("$.[*].mobilePhone").value(hasItem(DEFAULT_MOBILE_PHONE)))
            .andExpect(jsonPath("$.[*].faxNumber").value(hasItem(DEFAULT_FAX_NUMBER)))
            .andExpect(jsonPath("$.[*].addresse").value(hasItem(DEFAULT_ADDRESSE.toString())))
            .andExpect(jsonPath("$.[*].wilaya").value(hasItem(DEFAULT_WILAYA)))
            .andExpect(jsonPath("$.[*].daira").value(hasItem(DEFAULT_DAIRA)))
            .andExpect(jsonPath("$.[*].codePostal").value(hasItem(DEFAULT_CODE_POSTAL)))
            .andExpect(jsonPath("$.[*].commune").value(hasItem(DEFAULT_COMMUNE)))
            .andExpect(jsonPath("$.[*].webPage").value(hasItem(DEFAULT_WEB_PAGE.toString())))
            .andExpect(jsonPath("$.[*].notes").value(hasItem(DEFAULT_NOTES.toString())))
            .andExpect(jsonPath("$.[*].attachmentsContentType").value(hasItem(DEFAULT_ATTACHMENTS_CONTENT_TYPE)))
            .andExpect(jsonPath("$.[*].attachments").value(hasItem(Base64Utils.encodeToString(DEFAULT_ATTACHMENTS))))
            .andExpect(jsonPath("$.[*].dejaClient").value(hasItem(DEFAULT_DEJA_CLIENT.booleanValue())))
            .andExpect(jsonPath("$.[*].dateDerniereViste").value(hasItem(DEFAULT_DATE_DERNIERE_VISTE.toString())))
            .andExpect(jsonPath("$.[*].latitude").value(hasItem(sameNumber(DEFAULT_LATITUDE))))
            .andExpect(jsonPath("$.[*].longitude").value(hasItem(sameNumber(DEFAULT_LONGITUDE))));
    }

    @SuppressWarnings({ "unchecked" })
    void getAllCustomersWithEagerRelationshipsIsEnabled() throws Exception {
        when(customerServiceMock.findAllWithEagerRelationships(any())).thenReturn(new PageImpl(new ArrayList<>()));

        restCustomerMockMvc.perform(get(ENTITY_API_URL + "?eagerload=true")).andExpect(status().isOk());

        verify(customerServiceMock, times(1)).findAllWithEagerRelationships(any());
    }

    @SuppressWarnings({ "unchecked" })
    void getAllCustomersWithEagerRelationshipsIsNotEnabled() throws Exception {
        when(customerServiceMock.findAllWithEagerRelationships(any())).thenReturn(new PageImpl(new ArrayList<>()));

        restCustomerMockMvc.perform(get(ENTITY_API_URL + "?eagerload=true")).andExpect(status().isOk());

        verify(customerServiceMock, times(1)).findAllWithEagerRelationships(any());
    }

    @Test
    @Transactional
    void getCustomer() throws Exception {
        // Initialize the database
        customerRepository.saveAndFlush(customer);

        // Get the customer
        restCustomerMockMvc
            .perform(get(ENTITY_API_URL_ID, customer.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.id").value(customer.getId().intValue()))
            .andExpect(jsonPath("$.customerType").value(DEFAULT_CUSTOMER_TYPE.toString()))
            .andExpect(jsonPath("$.company").value(DEFAULT_COMPANY))
            .andExpect(jsonPath("$.lastName").value(DEFAULT_LAST_NAME))
            .andExpect(jsonPath("$.firstName").value(DEFAULT_FIRST_NAME))
            .andExpect(jsonPath("$.emailAddress").value(DEFAULT_EMAIL_ADDRESS))
            .andExpect(jsonPath("$.jobTitle").value(DEFAULT_JOB_TITLE))
            .andExpect(jsonPath("$.businessPhone").value(DEFAULT_BUSINESS_PHONE))
            .andExpect(jsonPath("$.homePhone").value(DEFAULT_HOME_PHONE))
            .andExpect(jsonPath("$.mobilePhone").value(DEFAULT_MOBILE_PHONE))
            .andExpect(jsonPath("$.faxNumber").value(DEFAULT_FAX_NUMBER))
            .andExpect(jsonPath("$.addresse").value(DEFAULT_ADDRESSE.toString()))
            .andExpect(jsonPath("$.wilaya").value(DEFAULT_WILAYA))
            .andExpect(jsonPath("$.daira").value(DEFAULT_DAIRA))
            .andExpect(jsonPath("$.codePostal").value(DEFAULT_CODE_POSTAL))
            .andExpect(jsonPath("$.commune").value(DEFAULT_COMMUNE))
            .andExpect(jsonPath("$.webPage").value(DEFAULT_WEB_PAGE.toString()))
            .andExpect(jsonPath("$.notes").value(DEFAULT_NOTES.toString()))
            .andExpect(jsonPath("$.attachmentsContentType").value(DEFAULT_ATTACHMENTS_CONTENT_TYPE))
            .andExpect(jsonPath("$.attachments").value(Base64Utils.encodeToString(DEFAULT_ATTACHMENTS)))
            .andExpect(jsonPath("$.dejaClient").value(DEFAULT_DEJA_CLIENT.booleanValue()))
            .andExpect(jsonPath("$.dateDerniereViste").value(DEFAULT_DATE_DERNIERE_VISTE.toString()))
            .andExpect(jsonPath("$.latitude").value(sameNumber(DEFAULT_LATITUDE)))
            .andExpect(jsonPath("$.longitude").value(sameNumber(DEFAULT_LONGITUDE)));
    }

    @Test
    @Transactional
    void getCustomersByIdFiltering() throws Exception {
        // Initialize the database
        customerRepository.saveAndFlush(customer);

        Long id = customer.getId();

        defaultCustomerShouldBeFound("id.equals=" + id);
        defaultCustomerShouldNotBeFound("id.notEquals=" + id);

        defaultCustomerShouldBeFound("id.greaterThanOrEqual=" + id);
        defaultCustomerShouldNotBeFound("id.greaterThan=" + id);

        defaultCustomerShouldBeFound("id.lessThanOrEqual=" + id);
        defaultCustomerShouldNotBeFound("id.lessThan=" + id);
    }

    @Test
    @Transactional
    void getAllCustomersByCustomerTypeIsEqualToSomething() throws Exception {
        // Initialize the database
        customerRepository.saveAndFlush(customer);

        // Get all the customerList where customerType equals to DEFAULT_CUSTOMER_TYPE
        defaultCustomerShouldBeFound("customerType.equals=" + DEFAULT_CUSTOMER_TYPE);

        // Get all the customerList where customerType equals to UPDATED_CUSTOMER_TYPE
        defaultCustomerShouldNotBeFound("customerType.equals=" + UPDATED_CUSTOMER_TYPE);
    }

    @Test
    @Transactional
    void getAllCustomersByCustomerTypeIsNotEqualToSomething() throws Exception {
        // Initialize the database
        customerRepository.saveAndFlush(customer);

        // Get all the customerList where customerType not equals to DEFAULT_CUSTOMER_TYPE
        defaultCustomerShouldNotBeFound("customerType.notEquals=" + DEFAULT_CUSTOMER_TYPE);

        // Get all the customerList where customerType not equals to UPDATED_CUSTOMER_TYPE
        defaultCustomerShouldBeFound("customerType.notEquals=" + UPDATED_CUSTOMER_TYPE);
    }

    @Test
    @Transactional
    void getAllCustomersByCustomerTypeIsInShouldWork() throws Exception {
        // Initialize the database
        customerRepository.saveAndFlush(customer);

        // Get all the customerList where customerType in DEFAULT_CUSTOMER_TYPE or UPDATED_CUSTOMER_TYPE
        defaultCustomerShouldBeFound("customerType.in=" + DEFAULT_CUSTOMER_TYPE + "," + UPDATED_CUSTOMER_TYPE);

        // Get all the customerList where customerType equals to UPDATED_CUSTOMER_TYPE
        defaultCustomerShouldNotBeFound("customerType.in=" + UPDATED_CUSTOMER_TYPE);
    }

    @Test
    @Transactional
    void getAllCustomersByCustomerTypeIsNullOrNotNull() throws Exception {
        // Initialize the database
        customerRepository.saveAndFlush(customer);

        // Get all the customerList where customerType is not null
        defaultCustomerShouldBeFound("customerType.specified=true");

        // Get all the customerList where customerType is null
        defaultCustomerShouldNotBeFound("customerType.specified=false");
    }

    @Test
    @Transactional
    void getAllCustomersByCompanyIsEqualToSomething() throws Exception {
        // Initialize the database
        customerRepository.saveAndFlush(customer);

        // Get all the customerList where company equals to DEFAULT_COMPANY
        defaultCustomerShouldBeFound("company.equals=" + DEFAULT_COMPANY);

        // Get all the customerList where company equals to UPDATED_COMPANY
        defaultCustomerShouldNotBeFound("company.equals=" + UPDATED_COMPANY);
    }

    @Test
    @Transactional
    void getAllCustomersByCompanyIsNotEqualToSomething() throws Exception {
        // Initialize the database
        customerRepository.saveAndFlush(customer);

        // Get all the customerList where company not equals to DEFAULT_COMPANY
        defaultCustomerShouldNotBeFound("company.notEquals=" + DEFAULT_COMPANY);

        // Get all the customerList where company not equals to UPDATED_COMPANY
        defaultCustomerShouldBeFound("company.notEquals=" + UPDATED_COMPANY);
    }

    @Test
    @Transactional
    void getAllCustomersByCompanyIsInShouldWork() throws Exception {
        // Initialize the database
        customerRepository.saveAndFlush(customer);

        // Get all the customerList where company in DEFAULT_COMPANY or UPDATED_COMPANY
        defaultCustomerShouldBeFound("company.in=" + DEFAULT_COMPANY + "," + UPDATED_COMPANY);

        // Get all the customerList where company equals to UPDATED_COMPANY
        defaultCustomerShouldNotBeFound("company.in=" + UPDATED_COMPANY);
    }

    @Test
    @Transactional
    void getAllCustomersByCompanyIsNullOrNotNull() throws Exception {
        // Initialize the database
        customerRepository.saveAndFlush(customer);

        // Get all the customerList where company is not null
        defaultCustomerShouldBeFound("company.specified=true");

        // Get all the customerList where company is null
        defaultCustomerShouldNotBeFound("company.specified=false");
    }

    @Test
    @Transactional
    void getAllCustomersByCompanyContainsSomething() throws Exception {
        // Initialize the database
        customerRepository.saveAndFlush(customer);

        // Get all the customerList where company contains DEFAULT_COMPANY
        defaultCustomerShouldBeFound("company.contains=" + DEFAULT_COMPANY);

        // Get all the customerList where company contains UPDATED_COMPANY
        defaultCustomerShouldNotBeFound("company.contains=" + UPDATED_COMPANY);
    }

    @Test
    @Transactional
    void getAllCustomersByCompanyNotContainsSomething() throws Exception {
        // Initialize the database
        customerRepository.saveAndFlush(customer);

        // Get all the customerList where company does not contain DEFAULT_COMPANY
        defaultCustomerShouldNotBeFound("company.doesNotContain=" + DEFAULT_COMPANY);

        // Get all the customerList where company does not contain UPDATED_COMPANY
        defaultCustomerShouldBeFound("company.doesNotContain=" + UPDATED_COMPANY);
    }

    @Test
    @Transactional
    void getAllCustomersByLastNameIsEqualToSomething() throws Exception {
        // Initialize the database
        customerRepository.saveAndFlush(customer);

        // Get all the customerList where lastName equals to DEFAULT_LAST_NAME
        defaultCustomerShouldBeFound("lastName.equals=" + DEFAULT_LAST_NAME);

        // Get all the customerList where lastName equals to UPDATED_LAST_NAME
        defaultCustomerShouldNotBeFound("lastName.equals=" + UPDATED_LAST_NAME);
    }

    @Test
    @Transactional
    void getAllCustomersByLastNameIsNotEqualToSomething() throws Exception {
        // Initialize the database
        customerRepository.saveAndFlush(customer);

        // Get all the customerList where lastName not equals to DEFAULT_LAST_NAME
        defaultCustomerShouldNotBeFound("lastName.notEquals=" + DEFAULT_LAST_NAME);

        // Get all the customerList where lastName not equals to UPDATED_LAST_NAME
        defaultCustomerShouldBeFound("lastName.notEquals=" + UPDATED_LAST_NAME);
    }

    @Test
    @Transactional
    void getAllCustomersByLastNameIsInShouldWork() throws Exception {
        // Initialize the database
        customerRepository.saveAndFlush(customer);

        // Get all the customerList where lastName in DEFAULT_LAST_NAME or UPDATED_LAST_NAME
        defaultCustomerShouldBeFound("lastName.in=" + DEFAULT_LAST_NAME + "," + UPDATED_LAST_NAME);

        // Get all the customerList where lastName equals to UPDATED_LAST_NAME
        defaultCustomerShouldNotBeFound("lastName.in=" + UPDATED_LAST_NAME);
    }

    @Test
    @Transactional
    void getAllCustomersByLastNameIsNullOrNotNull() throws Exception {
        // Initialize the database
        customerRepository.saveAndFlush(customer);

        // Get all the customerList where lastName is not null
        defaultCustomerShouldBeFound("lastName.specified=true");

        // Get all the customerList where lastName is null
        defaultCustomerShouldNotBeFound("lastName.specified=false");
    }

    @Test
    @Transactional
    void getAllCustomersByLastNameContainsSomething() throws Exception {
        // Initialize the database
        customerRepository.saveAndFlush(customer);

        // Get all the customerList where lastName contains DEFAULT_LAST_NAME
        defaultCustomerShouldBeFound("lastName.contains=" + DEFAULT_LAST_NAME);

        // Get all the customerList where lastName contains UPDATED_LAST_NAME
        defaultCustomerShouldNotBeFound("lastName.contains=" + UPDATED_LAST_NAME);
    }

    @Test
    @Transactional
    void getAllCustomersByLastNameNotContainsSomething() throws Exception {
        // Initialize the database
        customerRepository.saveAndFlush(customer);

        // Get all the customerList where lastName does not contain DEFAULT_LAST_NAME
        defaultCustomerShouldNotBeFound("lastName.doesNotContain=" + DEFAULT_LAST_NAME);

        // Get all the customerList where lastName does not contain UPDATED_LAST_NAME
        defaultCustomerShouldBeFound("lastName.doesNotContain=" + UPDATED_LAST_NAME);
    }

    @Test
    @Transactional
    void getAllCustomersByFirstNameIsEqualToSomething() throws Exception {
        // Initialize the database
        customerRepository.saveAndFlush(customer);

        // Get all the customerList where firstName equals to DEFAULT_FIRST_NAME
        defaultCustomerShouldBeFound("firstName.equals=" + DEFAULT_FIRST_NAME);

        // Get all the customerList where firstName equals to UPDATED_FIRST_NAME
        defaultCustomerShouldNotBeFound("firstName.equals=" + UPDATED_FIRST_NAME);
    }

    @Test
    @Transactional
    void getAllCustomersByFirstNameIsNotEqualToSomething() throws Exception {
        // Initialize the database
        customerRepository.saveAndFlush(customer);

        // Get all the customerList where firstName not equals to DEFAULT_FIRST_NAME
        defaultCustomerShouldNotBeFound("firstName.notEquals=" + DEFAULT_FIRST_NAME);

        // Get all the customerList where firstName not equals to UPDATED_FIRST_NAME
        defaultCustomerShouldBeFound("firstName.notEquals=" + UPDATED_FIRST_NAME);
    }

    @Test
    @Transactional
    void getAllCustomersByFirstNameIsInShouldWork() throws Exception {
        // Initialize the database
        customerRepository.saveAndFlush(customer);

        // Get all the customerList where firstName in DEFAULT_FIRST_NAME or UPDATED_FIRST_NAME
        defaultCustomerShouldBeFound("firstName.in=" + DEFAULT_FIRST_NAME + "," + UPDATED_FIRST_NAME);

        // Get all the customerList where firstName equals to UPDATED_FIRST_NAME
        defaultCustomerShouldNotBeFound("firstName.in=" + UPDATED_FIRST_NAME);
    }

    @Test
    @Transactional
    void getAllCustomersByFirstNameIsNullOrNotNull() throws Exception {
        // Initialize the database
        customerRepository.saveAndFlush(customer);

        // Get all the customerList where firstName is not null
        defaultCustomerShouldBeFound("firstName.specified=true");

        // Get all the customerList where firstName is null
        defaultCustomerShouldNotBeFound("firstName.specified=false");
    }

    @Test
    @Transactional
    void getAllCustomersByFirstNameContainsSomething() throws Exception {
        // Initialize the database
        customerRepository.saveAndFlush(customer);

        // Get all the customerList where firstName contains DEFAULT_FIRST_NAME
        defaultCustomerShouldBeFound("firstName.contains=" + DEFAULT_FIRST_NAME);

        // Get all the customerList where firstName contains UPDATED_FIRST_NAME
        defaultCustomerShouldNotBeFound("firstName.contains=" + UPDATED_FIRST_NAME);
    }

    @Test
    @Transactional
    void getAllCustomersByFirstNameNotContainsSomething() throws Exception {
        // Initialize the database
        customerRepository.saveAndFlush(customer);

        // Get all the customerList where firstName does not contain DEFAULT_FIRST_NAME
        defaultCustomerShouldNotBeFound("firstName.doesNotContain=" + DEFAULT_FIRST_NAME);

        // Get all the customerList where firstName does not contain UPDATED_FIRST_NAME
        defaultCustomerShouldBeFound("firstName.doesNotContain=" + UPDATED_FIRST_NAME);
    }

    @Test
    @Transactional
    void getAllCustomersByEmailAddressIsEqualToSomething() throws Exception {
        // Initialize the database
        customerRepository.saveAndFlush(customer);

        // Get all the customerList where emailAddress equals to DEFAULT_EMAIL_ADDRESS
        defaultCustomerShouldBeFound("emailAddress.equals=" + DEFAULT_EMAIL_ADDRESS);

        // Get all the customerList where emailAddress equals to UPDATED_EMAIL_ADDRESS
        defaultCustomerShouldNotBeFound("emailAddress.equals=" + UPDATED_EMAIL_ADDRESS);
    }

    @Test
    @Transactional
    void getAllCustomersByEmailAddressIsNotEqualToSomething() throws Exception {
        // Initialize the database
        customerRepository.saveAndFlush(customer);

        // Get all the customerList where emailAddress not equals to DEFAULT_EMAIL_ADDRESS
        defaultCustomerShouldNotBeFound("emailAddress.notEquals=" + DEFAULT_EMAIL_ADDRESS);

        // Get all the customerList where emailAddress not equals to UPDATED_EMAIL_ADDRESS
        defaultCustomerShouldBeFound("emailAddress.notEquals=" + UPDATED_EMAIL_ADDRESS);
    }

    @Test
    @Transactional
    void getAllCustomersByEmailAddressIsInShouldWork() throws Exception {
        // Initialize the database
        customerRepository.saveAndFlush(customer);

        // Get all the customerList where emailAddress in DEFAULT_EMAIL_ADDRESS or UPDATED_EMAIL_ADDRESS
        defaultCustomerShouldBeFound("emailAddress.in=" + DEFAULT_EMAIL_ADDRESS + "," + UPDATED_EMAIL_ADDRESS);

        // Get all the customerList where emailAddress equals to UPDATED_EMAIL_ADDRESS
        defaultCustomerShouldNotBeFound("emailAddress.in=" + UPDATED_EMAIL_ADDRESS);
    }

    @Test
    @Transactional
    void getAllCustomersByEmailAddressIsNullOrNotNull() throws Exception {
        // Initialize the database
        customerRepository.saveAndFlush(customer);

        // Get all the customerList where emailAddress is not null
        defaultCustomerShouldBeFound("emailAddress.specified=true");

        // Get all the customerList where emailAddress is null
        defaultCustomerShouldNotBeFound("emailAddress.specified=false");
    }

    @Test
    @Transactional
    void getAllCustomersByEmailAddressContainsSomething() throws Exception {
        // Initialize the database
        customerRepository.saveAndFlush(customer);

        // Get all the customerList where emailAddress contains DEFAULT_EMAIL_ADDRESS
        defaultCustomerShouldBeFound("emailAddress.contains=" + DEFAULT_EMAIL_ADDRESS);

        // Get all the customerList where emailAddress contains UPDATED_EMAIL_ADDRESS
        defaultCustomerShouldNotBeFound("emailAddress.contains=" + UPDATED_EMAIL_ADDRESS);
    }

    @Test
    @Transactional
    void getAllCustomersByEmailAddressNotContainsSomething() throws Exception {
        // Initialize the database
        customerRepository.saveAndFlush(customer);

        // Get all the customerList where emailAddress does not contain DEFAULT_EMAIL_ADDRESS
        defaultCustomerShouldNotBeFound("emailAddress.doesNotContain=" + DEFAULT_EMAIL_ADDRESS);

        // Get all the customerList where emailAddress does not contain UPDATED_EMAIL_ADDRESS
        defaultCustomerShouldBeFound("emailAddress.doesNotContain=" + UPDATED_EMAIL_ADDRESS);
    }

    @Test
    @Transactional
    void getAllCustomersByJobTitleIsEqualToSomething() throws Exception {
        // Initialize the database
        customerRepository.saveAndFlush(customer);

        // Get all the customerList where jobTitle equals to DEFAULT_JOB_TITLE
        defaultCustomerShouldBeFound("jobTitle.equals=" + DEFAULT_JOB_TITLE);

        // Get all the customerList where jobTitle equals to UPDATED_JOB_TITLE
        defaultCustomerShouldNotBeFound("jobTitle.equals=" + UPDATED_JOB_TITLE);
    }

    @Test
    @Transactional
    void getAllCustomersByJobTitleIsNotEqualToSomething() throws Exception {
        // Initialize the database
        customerRepository.saveAndFlush(customer);

        // Get all the customerList where jobTitle not equals to DEFAULT_JOB_TITLE
        defaultCustomerShouldNotBeFound("jobTitle.notEquals=" + DEFAULT_JOB_TITLE);

        // Get all the customerList where jobTitle not equals to UPDATED_JOB_TITLE
        defaultCustomerShouldBeFound("jobTitle.notEquals=" + UPDATED_JOB_TITLE);
    }

    @Test
    @Transactional
    void getAllCustomersByJobTitleIsInShouldWork() throws Exception {
        // Initialize the database
        customerRepository.saveAndFlush(customer);

        // Get all the customerList where jobTitle in DEFAULT_JOB_TITLE or UPDATED_JOB_TITLE
        defaultCustomerShouldBeFound("jobTitle.in=" + DEFAULT_JOB_TITLE + "," + UPDATED_JOB_TITLE);

        // Get all the customerList where jobTitle equals to UPDATED_JOB_TITLE
        defaultCustomerShouldNotBeFound("jobTitle.in=" + UPDATED_JOB_TITLE);
    }

    @Test
    @Transactional
    void getAllCustomersByJobTitleIsNullOrNotNull() throws Exception {
        // Initialize the database
        customerRepository.saveAndFlush(customer);

        // Get all the customerList where jobTitle is not null
        defaultCustomerShouldBeFound("jobTitle.specified=true");

        // Get all the customerList where jobTitle is null
        defaultCustomerShouldNotBeFound("jobTitle.specified=false");
    }

    @Test
    @Transactional
    void getAllCustomersByJobTitleContainsSomething() throws Exception {
        // Initialize the database
        customerRepository.saveAndFlush(customer);

        // Get all the customerList where jobTitle contains DEFAULT_JOB_TITLE
        defaultCustomerShouldBeFound("jobTitle.contains=" + DEFAULT_JOB_TITLE);

        // Get all the customerList where jobTitle contains UPDATED_JOB_TITLE
        defaultCustomerShouldNotBeFound("jobTitle.contains=" + UPDATED_JOB_TITLE);
    }

    @Test
    @Transactional
    void getAllCustomersByJobTitleNotContainsSomething() throws Exception {
        // Initialize the database
        customerRepository.saveAndFlush(customer);

        // Get all the customerList where jobTitle does not contain DEFAULT_JOB_TITLE
        defaultCustomerShouldNotBeFound("jobTitle.doesNotContain=" + DEFAULT_JOB_TITLE);

        // Get all the customerList where jobTitle does not contain UPDATED_JOB_TITLE
        defaultCustomerShouldBeFound("jobTitle.doesNotContain=" + UPDATED_JOB_TITLE);
    }

    @Test
    @Transactional
    void getAllCustomersByBusinessPhoneIsEqualToSomething() throws Exception {
        // Initialize the database
        customerRepository.saveAndFlush(customer);

        // Get all the customerList where businessPhone equals to DEFAULT_BUSINESS_PHONE
        defaultCustomerShouldBeFound("businessPhone.equals=" + DEFAULT_BUSINESS_PHONE);

        // Get all the customerList where businessPhone equals to UPDATED_BUSINESS_PHONE
        defaultCustomerShouldNotBeFound("businessPhone.equals=" + UPDATED_BUSINESS_PHONE);
    }

    @Test
    @Transactional
    void getAllCustomersByBusinessPhoneIsNotEqualToSomething() throws Exception {
        // Initialize the database
        customerRepository.saveAndFlush(customer);

        // Get all the customerList where businessPhone not equals to DEFAULT_BUSINESS_PHONE
        defaultCustomerShouldNotBeFound("businessPhone.notEquals=" + DEFAULT_BUSINESS_PHONE);

        // Get all the customerList where businessPhone not equals to UPDATED_BUSINESS_PHONE
        defaultCustomerShouldBeFound("businessPhone.notEquals=" + UPDATED_BUSINESS_PHONE);
    }

    @Test
    @Transactional
    void getAllCustomersByBusinessPhoneIsInShouldWork() throws Exception {
        // Initialize the database
        customerRepository.saveAndFlush(customer);

        // Get all the customerList where businessPhone in DEFAULT_BUSINESS_PHONE or UPDATED_BUSINESS_PHONE
        defaultCustomerShouldBeFound("businessPhone.in=" + DEFAULT_BUSINESS_PHONE + "," + UPDATED_BUSINESS_PHONE);

        // Get all the customerList where businessPhone equals to UPDATED_BUSINESS_PHONE
        defaultCustomerShouldNotBeFound("businessPhone.in=" + UPDATED_BUSINESS_PHONE);
    }

    @Test
    @Transactional
    void getAllCustomersByBusinessPhoneIsNullOrNotNull() throws Exception {
        // Initialize the database
        customerRepository.saveAndFlush(customer);

        // Get all the customerList where businessPhone is not null
        defaultCustomerShouldBeFound("businessPhone.specified=true");

        // Get all the customerList where businessPhone is null
        defaultCustomerShouldNotBeFound("businessPhone.specified=false");
    }

    @Test
    @Transactional
    void getAllCustomersByBusinessPhoneContainsSomething() throws Exception {
        // Initialize the database
        customerRepository.saveAndFlush(customer);

        // Get all the customerList where businessPhone contains DEFAULT_BUSINESS_PHONE
        defaultCustomerShouldBeFound("businessPhone.contains=" + DEFAULT_BUSINESS_PHONE);

        // Get all the customerList where businessPhone contains UPDATED_BUSINESS_PHONE
        defaultCustomerShouldNotBeFound("businessPhone.contains=" + UPDATED_BUSINESS_PHONE);
    }

    @Test
    @Transactional
    void getAllCustomersByBusinessPhoneNotContainsSomething() throws Exception {
        // Initialize the database
        customerRepository.saveAndFlush(customer);

        // Get all the customerList where businessPhone does not contain DEFAULT_BUSINESS_PHONE
        defaultCustomerShouldNotBeFound("businessPhone.doesNotContain=" + DEFAULT_BUSINESS_PHONE);

        // Get all the customerList where businessPhone does not contain UPDATED_BUSINESS_PHONE
        defaultCustomerShouldBeFound("businessPhone.doesNotContain=" + UPDATED_BUSINESS_PHONE);
    }

    @Test
    @Transactional
    void getAllCustomersByHomePhoneIsEqualToSomething() throws Exception {
        // Initialize the database
        customerRepository.saveAndFlush(customer);

        // Get all the customerList where homePhone equals to DEFAULT_HOME_PHONE
        defaultCustomerShouldBeFound("homePhone.equals=" + DEFAULT_HOME_PHONE);

        // Get all the customerList where homePhone equals to UPDATED_HOME_PHONE
        defaultCustomerShouldNotBeFound("homePhone.equals=" + UPDATED_HOME_PHONE);
    }

    @Test
    @Transactional
    void getAllCustomersByHomePhoneIsNotEqualToSomething() throws Exception {
        // Initialize the database
        customerRepository.saveAndFlush(customer);

        // Get all the customerList where homePhone not equals to DEFAULT_HOME_PHONE
        defaultCustomerShouldNotBeFound("homePhone.notEquals=" + DEFAULT_HOME_PHONE);

        // Get all the customerList where homePhone not equals to UPDATED_HOME_PHONE
        defaultCustomerShouldBeFound("homePhone.notEquals=" + UPDATED_HOME_PHONE);
    }

    @Test
    @Transactional
    void getAllCustomersByHomePhoneIsInShouldWork() throws Exception {
        // Initialize the database
        customerRepository.saveAndFlush(customer);

        // Get all the customerList where homePhone in DEFAULT_HOME_PHONE or UPDATED_HOME_PHONE
        defaultCustomerShouldBeFound("homePhone.in=" + DEFAULT_HOME_PHONE + "," + UPDATED_HOME_PHONE);

        // Get all the customerList where homePhone equals to UPDATED_HOME_PHONE
        defaultCustomerShouldNotBeFound("homePhone.in=" + UPDATED_HOME_PHONE);
    }

    @Test
    @Transactional
    void getAllCustomersByHomePhoneIsNullOrNotNull() throws Exception {
        // Initialize the database
        customerRepository.saveAndFlush(customer);

        // Get all the customerList where homePhone is not null
        defaultCustomerShouldBeFound("homePhone.specified=true");

        // Get all the customerList where homePhone is null
        defaultCustomerShouldNotBeFound("homePhone.specified=false");
    }

    @Test
    @Transactional
    void getAllCustomersByHomePhoneContainsSomething() throws Exception {
        // Initialize the database
        customerRepository.saveAndFlush(customer);

        // Get all the customerList where homePhone contains DEFAULT_HOME_PHONE
        defaultCustomerShouldBeFound("homePhone.contains=" + DEFAULT_HOME_PHONE);

        // Get all the customerList where homePhone contains UPDATED_HOME_PHONE
        defaultCustomerShouldNotBeFound("homePhone.contains=" + UPDATED_HOME_PHONE);
    }

    @Test
    @Transactional
    void getAllCustomersByHomePhoneNotContainsSomething() throws Exception {
        // Initialize the database
        customerRepository.saveAndFlush(customer);

        // Get all the customerList where homePhone does not contain DEFAULT_HOME_PHONE
        defaultCustomerShouldNotBeFound("homePhone.doesNotContain=" + DEFAULT_HOME_PHONE);

        // Get all the customerList where homePhone does not contain UPDATED_HOME_PHONE
        defaultCustomerShouldBeFound("homePhone.doesNotContain=" + UPDATED_HOME_PHONE);
    }

    @Test
    @Transactional
    void getAllCustomersByMobilePhoneIsEqualToSomething() throws Exception {
        // Initialize the database
        customerRepository.saveAndFlush(customer);

        // Get all the customerList where mobilePhone equals to DEFAULT_MOBILE_PHONE
        defaultCustomerShouldBeFound("mobilePhone.equals=" + DEFAULT_MOBILE_PHONE);

        // Get all the customerList where mobilePhone equals to UPDATED_MOBILE_PHONE
        defaultCustomerShouldNotBeFound("mobilePhone.equals=" + UPDATED_MOBILE_PHONE);
    }

    @Test
    @Transactional
    void getAllCustomersByMobilePhoneIsNotEqualToSomething() throws Exception {
        // Initialize the database
        customerRepository.saveAndFlush(customer);

        // Get all the customerList where mobilePhone not equals to DEFAULT_MOBILE_PHONE
        defaultCustomerShouldNotBeFound("mobilePhone.notEquals=" + DEFAULT_MOBILE_PHONE);

        // Get all the customerList where mobilePhone not equals to UPDATED_MOBILE_PHONE
        defaultCustomerShouldBeFound("mobilePhone.notEquals=" + UPDATED_MOBILE_PHONE);
    }

    @Test
    @Transactional
    void getAllCustomersByMobilePhoneIsInShouldWork() throws Exception {
        // Initialize the database
        customerRepository.saveAndFlush(customer);

        // Get all the customerList where mobilePhone in DEFAULT_MOBILE_PHONE or UPDATED_MOBILE_PHONE
        defaultCustomerShouldBeFound("mobilePhone.in=" + DEFAULT_MOBILE_PHONE + "," + UPDATED_MOBILE_PHONE);

        // Get all the customerList where mobilePhone equals to UPDATED_MOBILE_PHONE
        defaultCustomerShouldNotBeFound("mobilePhone.in=" + UPDATED_MOBILE_PHONE);
    }

    @Test
    @Transactional
    void getAllCustomersByMobilePhoneIsNullOrNotNull() throws Exception {
        // Initialize the database
        customerRepository.saveAndFlush(customer);

        // Get all the customerList where mobilePhone is not null
        defaultCustomerShouldBeFound("mobilePhone.specified=true");

        // Get all the customerList where mobilePhone is null
        defaultCustomerShouldNotBeFound("mobilePhone.specified=false");
    }

    @Test
    @Transactional
    void getAllCustomersByMobilePhoneContainsSomething() throws Exception {
        // Initialize the database
        customerRepository.saveAndFlush(customer);

        // Get all the customerList where mobilePhone contains DEFAULT_MOBILE_PHONE
        defaultCustomerShouldBeFound("mobilePhone.contains=" + DEFAULT_MOBILE_PHONE);

        // Get all the customerList where mobilePhone contains UPDATED_MOBILE_PHONE
        defaultCustomerShouldNotBeFound("mobilePhone.contains=" + UPDATED_MOBILE_PHONE);
    }

    @Test
    @Transactional
    void getAllCustomersByMobilePhoneNotContainsSomething() throws Exception {
        // Initialize the database
        customerRepository.saveAndFlush(customer);

        // Get all the customerList where mobilePhone does not contain DEFAULT_MOBILE_PHONE
        defaultCustomerShouldNotBeFound("mobilePhone.doesNotContain=" + DEFAULT_MOBILE_PHONE);

        // Get all the customerList where mobilePhone does not contain UPDATED_MOBILE_PHONE
        defaultCustomerShouldBeFound("mobilePhone.doesNotContain=" + UPDATED_MOBILE_PHONE);
    }

    @Test
    @Transactional
    void getAllCustomersByFaxNumberIsEqualToSomething() throws Exception {
        // Initialize the database
        customerRepository.saveAndFlush(customer);

        // Get all the customerList where faxNumber equals to DEFAULT_FAX_NUMBER
        defaultCustomerShouldBeFound("faxNumber.equals=" + DEFAULT_FAX_NUMBER);

        // Get all the customerList where faxNumber equals to UPDATED_FAX_NUMBER
        defaultCustomerShouldNotBeFound("faxNumber.equals=" + UPDATED_FAX_NUMBER);
    }

    @Test
    @Transactional
    void getAllCustomersByFaxNumberIsNotEqualToSomething() throws Exception {
        // Initialize the database
        customerRepository.saveAndFlush(customer);

        // Get all the customerList where faxNumber not equals to DEFAULT_FAX_NUMBER
        defaultCustomerShouldNotBeFound("faxNumber.notEquals=" + DEFAULT_FAX_NUMBER);

        // Get all the customerList where faxNumber not equals to UPDATED_FAX_NUMBER
        defaultCustomerShouldBeFound("faxNumber.notEquals=" + UPDATED_FAX_NUMBER);
    }

    @Test
    @Transactional
    void getAllCustomersByFaxNumberIsInShouldWork() throws Exception {
        // Initialize the database
        customerRepository.saveAndFlush(customer);

        // Get all the customerList where faxNumber in DEFAULT_FAX_NUMBER or UPDATED_FAX_NUMBER
        defaultCustomerShouldBeFound("faxNumber.in=" + DEFAULT_FAX_NUMBER + "," + UPDATED_FAX_NUMBER);

        // Get all the customerList where faxNumber equals to UPDATED_FAX_NUMBER
        defaultCustomerShouldNotBeFound("faxNumber.in=" + UPDATED_FAX_NUMBER);
    }

    @Test
    @Transactional
    void getAllCustomersByFaxNumberIsNullOrNotNull() throws Exception {
        // Initialize the database
        customerRepository.saveAndFlush(customer);

        // Get all the customerList where faxNumber is not null
        defaultCustomerShouldBeFound("faxNumber.specified=true");

        // Get all the customerList where faxNumber is null
        defaultCustomerShouldNotBeFound("faxNumber.specified=false");
    }

    @Test
    @Transactional
    void getAllCustomersByFaxNumberContainsSomething() throws Exception {
        // Initialize the database
        customerRepository.saveAndFlush(customer);

        // Get all the customerList where faxNumber contains DEFAULT_FAX_NUMBER
        defaultCustomerShouldBeFound("faxNumber.contains=" + DEFAULT_FAX_NUMBER);

        // Get all the customerList where faxNumber contains UPDATED_FAX_NUMBER
        defaultCustomerShouldNotBeFound("faxNumber.contains=" + UPDATED_FAX_NUMBER);
    }

    @Test
    @Transactional
    void getAllCustomersByFaxNumberNotContainsSomething() throws Exception {
        // Initialize the database
        customerRepository.saveAndFlush(customer);

        // Get all the customerList where faxNumber does not contain DEFAULT_FAX_NUMBER
        defaultCustomerShouldNotBeFound("faxNumber.doesNotContain=" + DEFAULT_FAX_NUMBER);

        // Get all the customerList where faxNumber does not contain UPDATED_FAX_NUMBER
        defaultCustomerShouldBeFound("faxNumber.doesNotContain=" + UPDATED_FAX_NUMBER);
    }

    @Test
    @Transactional
    void getAllCustomersByWilayaIsEqualToSomething() throws Exception {
        // Initialize the database
        customerRepository.saveAndFlush(customer);

        // Get all the customerList where wilaya equals to DEFAULT_WILAYA
        defaultCustomerShouldBeFound("wilaya.equals=" + DEFAULT_WILAYA);

        // Get all the customerList where wilaya equals to UPDATED_WILAYA
        defaultCustomerShouldNotBeFound("wilaya.equals=" + UPDATED_WILAYA);
    }

    @Test
    @Transactional
    void getAllCustomersByWilayaIsNotEqualToSomething() throws Exception {
        // Initialize the database
        customerRepository.saveAndFlush(customer);

        // Get all the customerList where wilaya not equals to DEFAULT_WILAYA
        defaultCustomerShouldNotBeFound("wilaya.notEquals=" + DEFAULT_WILAYA);

        // Get all the customerList where wilaya not equals to UPDATED_WILAYA
        defaultCustomerShouldBeFound("wilaya.notEquals=" + UPDATED_WILAYA);
    }

    @Test
    @Transactional
    void getAllCustomersByWilayaIsInShouldWork() throws Exception {
        // Initialize the database
        customerRepository.saveAndFlush(customer);

        // Get all the customerList where wilaya in DEFAULT_WILAYA or UPDATED_WILAYA
        defaultCustomerShouldBeFound("wilaya.in=" + DEFAULT_WILAYA + "," + UPDATED_WILAYA);

        // Get all the customerList where wilaya equals to UPDATED_WILAYA
        defaultCustomerShouldNotBeFound("wilaya.in=" + UPDATED_WILAYA);
    }

    @Test
    @Transactional
    void getAllCustomersByWilayaIsNullOrNotNull() throws Exception {
        // Initialize the database
        customerRepository.saveAndFlush(customer);

        // Get all the customerList where wilaya is not null
        defaultCustomerShouldBeFound("wilaya.specified=true");

        // Get all the customerList where wilaya is null
        defaultCustomerShouldNotBeFound("wilaya.specified=false");
    }

    @Test
    @Transactional
    void getAllCustomersByWilayaContainsSomething() throws Exception {
        // Initialize the database
        customerRepository.saveAndFlush(customer);

        // Get all the customerList where wilaya contains DEFAULT_WILAYA
        defaultCustomerShouldBeFound("wilaya.contains=" + DEFAULT_WILAYA);

        // Get all the customerList where wilaya contains UPDATED_WILAYA
        defaultCustomerShouldNotBeFound("wilaya.contains=" + UPDATED_WILAYA);
    }

    @Test
    @Transactional
    void getAllCustomersByWilayaNotContainsSomething() throws Exception {
        // Initialize the database
        customerRepository.saveAndFlush(customer);

        // Get all the customerList where wilaya does not contain DEFAULT_WILAYA
        defaultCustomerShouldNotBeFound("wilaya.doesNotContain=" + DEFAULT_WILAYA);

        // Get all the customerList where wilaya does not contain UPDATED_WILAYA
        defaultCustomerShouldBeFound("wilaya.doesNotContain=" + UPDATED_WILAYA);
    }

    @Test
    @Transactional
    void getAllCustomersByDairaIsEqualToSomething() throws Exception {
        // Initialize the database
        customerRepository.saveAndFlush(customer);

        // Get all the customerList where daira equals to DEFAULT_DAIRA
        defaultCustomerShouldBeFound("daira.equals=" + DEFAULT_DAIRA);

        // Get all the customerList where daira equals to UPDATED_DAIRA
        defaultCustomerShouldNotBeFound("daira.equals=" + UPDATED_DAIRA);
    }

    @Test
    @Transactional
    void getAllCustomersByDairaIsNotEqualToSomething() throws Exception {
        // Initialize the database
        customerRepository.saveAndFlush(customer);

        // Get all the customerList where daira not equals to DEFAULT_DAIRA
        defaultCustomerShouldNotBeFound("daira.notEquals=" + DEFAULT_DAIRA);

        // Get all the customerList where daira not equals to UPDATED_DAIRA
        defaultCustomerShouldBeFound("daira.notEquals=" + UPDATED_DAIRA);
    }

    @Test
    @Transactional
    void getAllCustomersByDairaIsInShouldWork() throws Exception {
        // Initialize the database
        customerRepository.saveAndFlush(customer);

        // Get all the customerList where daira in DEFAULT_DAIRA or UPDATED_DAIRA
        defaultCustomerShouldBeFound("daira.in=" + DEFAULT_DAIRA + "," + UPDATED_DAIRA);

        // Get all the customerList where daira equals to UPDATED_DAIRA
        defaultCustomerShouldNotBeFound("daira.in=" + UPDATED_DAIRA);
    }

    @Test
    @Transactional
    void getAllCustomersByDairaIsNullOrNotNull() throws Exception {
        // Initialize the database
        customerRepository.saveAndFlush(customer);

        // Get all the customerList where daira is not null
        defaultCustomerShouldBeFound("daira.specified=true");

        // Get all the customerList where daira is null
        defaultCustomerShouldNotBeFound("daira.specified=false");
    }

    @Test
    @Transactional
    void getAllCustomersByDairaContainsSomething() throws Exception {
        // Initialize the database
        customerRepository.saveAndFlush(customer);

        // Get all the customerList where daira contains DEFAULT_DAIRA
        defaultCustomerShouldBeFound("daira.contains=" + DEFAULT_DAIRA);

        // Get all the customerList where daira contains UPDATED_DAIRA
        defaultCustomerShouldNotBeFound("daira.contains=" + UPDATED_DAIRA);
    }

    @Test
    @Transactional
    void getAllCustomersByDairaNotContainsSomething() throws Exception {
        // Initialize the database
        customerRepository.saveAndFlush(customer);

        // Get all the customerList where daira does not contain DEFAULT_DAIRA
        defaultCustomerShouldNotBeFound("daira.doesNotContain=" + DEFAULT_DAIRA);

        // Get all the customerList where daira does not contain UPDATED_DAIRA
        defaultCustomerShouldBeFound("daira.doesNotContain=" + UPDATED_DAIRA);
    }

    @Test
    @Transactional
    void getAllCustomersByCodePostalIsEqualToSomething() throws Exception {
        // Initialize the database
        customerRepository.saveAndFlush(customer);

        // Get all the customerList where codePostal equals to DEFAULT_CODE_POSTAL
        defaultCustomerShouldBeFound("codePostal.equals=" + DEFAULT_CODE_POSTAL);

        // Get all the customerList where codePostal equals to UPDATED_CODE_POSTAL
        defaultCustomerShouldNotBeFound("codePostal.equals=" + UPDATED_CODE_POSTAL);
    }

    @Test
    @Transactional
    void getAllCustomersByCodePostalIsNotEqualToSomething() throws Exception {
        // Initialize the database
        customerRepository.saveAndFlush(customer);

        // Get all the customerList where codePostal not equals to DEFAULT_CODE_POSTAL
        defaultCustomerShouldNotBeFound("codePostal.notEquals=" + DEFAULT_CODE_POSTAL);

        // Get all the customerList where codePostal not equals to UPDATED_CODE_POSTAL
        defaultCustomerShouldBeFound("codePostal.notEquals=" + UPDATED_CODE_POSTAL);
    }

    @Test
    @Transactional
    void getAllCustomersByCodePostalIsInShouldWork() throws Exception {
        // Initialize the database
        customerRepository.saveAndFlush(customer);

        // Get all the customerList where codePostal in DEFAULT_CODE_POSTAL or UPDATED_CODE_POSTAL
        defaultCustomerShouldBeFound("codePostal.in=" + DEFAULT_CODE_POSTAL + "," + UPDATED_CODE_POSTAL);

        // Get all the customerList where codePostal equals to UPDATED_CODE_POSTAL
        defaultCustomerShouldNotBeFound("codePostal.in=" + UPDATED_CODE_POSTAL);
    }

    @Test
    @Transactional
    void getAllCustomersByCodePostalIsNullOrNotNull() throws Exception {
        // Initialize the database
        customerRepository.saveAndFlush(customer);

        // Get all the customerList where codePostal is not null
        defaultCustomerShouldBeFound("codePostal.specified=true");

        // Get all the customerList where codePostal is null
        defaultCustomerShouldNotBeFound("codePostal.specified=false");
    }

    @Test
    @Transactional
    void getAllCustomersByCodePostalContainsSomething() throws Exception {
        // Initialize the database
        customerRepository.saveAndFlush(customer);

        // Get all the customerList where codePostal contains DEFAULT_CODE_POSTAL
        defaultCustomerShouldBeFound("codePostal.contains=" + DEFAULT_CODE_POSTAL);

        // Get all the customerList where codePostal contains UPDATED_CODE_POSTAL
        defaultCustomerShouldNotBeFound("codePostal.contains=" + UPDATED_CODE_POSTAL);
    }

    @Test
    @Transactional
    void getAllCustomersByCodePostalNotContainsSomething() throws Exception {
        // Initialize the database
        customerRepository.saveAndFlush(customer);

        // Get all the customerList where codePostal does not contain DEFAULT_CODE_POSTAL
        defaultCustomerShouldNotBeFound("codePostal.doesNotContain=" + DEFAULT_CODE_POSTAL);

        // Get all the customerList where codePostal does not contain UPDATED_CODE_POSTAL
        defaultCustomerShouldBeFound("codePostal.doesNotContain=" + UPDATED_CODE_POSTAL);
    }

    @Test
    @Transactional
    void getAllCustomersByCommuneIsEqualToSomething() throws Exception {
        // Initialize the database
        customerRepository.saveAndFlush(customer);

        // Get all the customerList where commune equals to DEFAULT_COMMUNE
        defaultCustomerShouldBeFound("commune.equals=" + DEFAULT_COMMUNE);

        // Get all the customerList where commune equals to UPDATED_COMMUNE
        defaultCustomerShouldNotBeFound("commune.equals=" + UPDATED_COMMUNE);
    }

    @Test
    @Transactional
    void getAllCustomersByCommuneIsNotEqualToSomething() throws Exception {
        // Initialize the database
        customerRepository.saveAndFlush(customer);

        // Get all the customerList where commune not equals to DEFAULT_COMMUNE
        defaultCustomerShouldNotBeFound("commune.notEquals=" + DEFAULT_COMMUNE);

        // Get all the customerList where commune not equals to UPDATED_COMMUNE
        defaultCustomerShouldBeFound("commune.notEquals=" + UPDATED_COMMUNE);
    }

    @Test
    @Transactional
    void getAllCustomersByCommuneIsInShouldWork() throws Exception {
        // Initialize the database
        customerRepository.saveAndFlush(customer);

        // Get all the customerList where commune in DEFAULT_COMMUNE or UPDATED_COMMUNE
        defaultCustomerShouldBeFound("commune.in=" + DEFAULT_COMMUNE + "," + UPDATED_COMMUNE);

        // Get all the customerList where commune equals to UPDATED_COMMUNE
        defaultCustomerShouldNotBeFound("commune.in=" + UPDATED_COMMUNE);
    }

    @Test
    @Transactional
    void getAllCustomersByCommuneIsNullOrNotNull() throws Exception {
        // Initialize the database
        customerRepository.saveAndFlush(customer);

        // Get all the customerList where commune is not null
        defaultCustomerShouldBeFound("commune.specified=true");

        // Get all the customerList where commune is null
        defaultCustomerShouldNotBeFound("commune.specified=false");
    }

    @Test
    @Transactional
    void getAllCustomersByCommuneContainsSomething() throws Exception {
        // Initialize the database
        customerRepository.saveAndFlush(customer);

        // Get all the customerList where commune contains DEFAULT_COMMUNE
        defaultCustomerShouldBeFound("commune.contains=" + DEFAULT_COMMUNE);

        // Get all the customerList where commune contains UPDATED_COMMUNE
        defaultCustomerShouldNotBeFound("commune.contains=" + UPDATED_COMMUNE);
    }

    @Test
    @Transactional
    void getAllCustomersByCommuneNotContainsSomething() throws Exception {
        // Initialize the database
        customerRepository.saveAndFlush(customer);

        // Get all the customerList where commune does not contain DEFAULT_COMMUNE
        defaultCustomerShouldNotBeFound("commune.doesNotContain=" + DEFAULT_COMMUNE);

        // Get all the customerList where commune does not contain UPDATED_COMMUNE
        defaultCustomerShouldBeFound("commune.doesNotContain=" + UPDATED_COMMUNE);
    }

    @Test
    @Transactional
    void getAllCustomersByDejaClientIsEqualToSomething() throws Exception {
        // Initialize the database
        customerRepository.saveAndFlush(customer);

        // Get all the customerList where dejaClient equals to DEFAULT_DEJA_CLIENT
        defaultCustomerShouldBeFound("dejaClient.equals=" + DEFAULT_DEJA_CLIENT);

        // Get all the customerList where dejaClient equals to UPDATED_DEJA_CLIENT
        defaultCustomerShouldNotBeFound("dejaClient.equals=" + UPDATED_DEJA_CLIENT);
    }

    @Test
    @Transactional
    void getAllCustomersByDejaClientIsNotEqualToSomething() throws Exception {
        // Initialize the database
        customerRepository.saveAndFlush(customer);

        // Get all the customerList where dejaClient not equals to DEFAULT_DEJA_CLIENT
        defaultCustomerShouldNotBeFound("dejaClient.notEquals=" + DEFAULT_DEJA_CLIENT);

        // Get all the customerList where dejaClient not equals to UPDATED_DEJA_CLIENT
        defaultCustomerShouldBeFound("dejaClient.notEquals=" + UPDATED_DEJA_CLIENT);
    }

    @Test
    @Transactional
    void getAllCustomersByDejaClientIsInShouldWork() throws Exception {
        // Initialize the database
        customerRepository.saveAndFlush(customer);

        // Get all the customerList where dejaClient in DEFAULT_DEJA_CLIENT or UPDATED_DEJA_CLIENT
        defaultCustomerShouldBeFound("dejaClient.in=" + DEFAULT_DEJA_CLIENT + "," + UPDATED_DEJA_CLIENT);

        // Get all the customerList where dejaClient equals to UPDATED_DEJA_CLIENT
        defaultCustomerShouldNotBeFound("dejaClient.in=" + UPDATED_DEJA_CLIENT);
    }

    @Test
    @Transactional
    void getAllCustomersByDejaClientIsNullOrNotNull() throws Exception {
        // Initialize the database
        customerRepository.saveAndFlush(customer);

        // Get all the customerList where dejaClient is not null
        defaultCustomerShouldBeFound("dejaClient.specified=true");

        // Get all the customerList where dejaClient is null
        defaultCustomerShouldNotBeFound("dejaClient.specified=false");
    }

    @Test
    @Transactional
    void getAllCustomersByDateDerniereVisteIsEqualToSomething() throws Exception {
        // Initialize the database
        customerRepository.saveAndFlush(customer);

        // Get all the customerList where dateDerniereViste equals to DEFAULT_DATE_DERNIERE_VISTE
        defaultCustomerShouldBeFound("dateDerniereViste.equals=" + DEFAULT_DATE_DERNIERE_VISTE);

        // Get all the customerList where dateDerniereViste equals to UPDATED_DATE_DERNIERE_VISTE
        defaultCustomerShouldNotBeFound("dateDerniereViste.equals=" + UPDATED_DATE_DERNIERE_VISTE);
    }

    @Test
    @Transactional
    void getAllCustomersByDateDerniereVisteIsNotEqualToSomething() throws Exception {
        // Initialize the database
        customerRepository.saveAndFlush(customer);

        // Get all the customerList where dateDerniereViste not equals to DEFAULT_DATE_DERNIERE_VISTE
        defaultCustomerShouldNotBeFound("dateDerniereViste.notEquals=" + DEFAULT_DATE_DERNIERE_VISTE);

        // Get all the customerList where dateDerniereViste not equals to UPDATED_DATE_DERNIERE_VISTE
        defaultCustomerShouldBeFound("dateDerniereViste.notEquals=" + UPDATED_DATE_DERNIERE_VISTE);
    }

    @Test
    @Transactional
    void getAllCustomersByDateDerniereVisteIsInShouldWork() throws Exception {
        // Initialize the database
        customerRepository.saveAndFlush(customer);

        // Get all the customerList where dateDerniereViste in DEFAULT_DATE_DERNIERE_VISTE or UPDATED_DATE_DERNIERE_VISTE
        defaultCustomerShouldBeFound("dateDerniereViste.in=" + DEFAULT_DATE_DERNIERE_VISTE + "," + UPDATED_DATE_DERNIERE_VISTE);

        // Get all the customerList where dateDerniereViste equals to UPDATED_DATE_DERNIERE_VISTE
        defaultCustomerShouldNotBeFound("dateDerniereViste.in=" + UPDATED_DATE_DERNIERE_VISTE);
    }

    @Test
    @Transactional
    void getAllCustomersByDateDerniereVisteIsNullOrNotNull() throws Exception {
        // Initialize the database
        customerRepository.saveAndFlush(customer);

        // Get all the customerList where dateDerniereViste is not null
        defaultCustomerShouldBeFound("dateDerniereViste.specified=true");

        // Get all the customerList where dateDerniereViste is null
        defaultCustomerShouldNotBeFound("dateDerniereViste.specified=false");
    }

    @Test
    @Transactional
    void getAllCustomersByDateDerniereVisteIsGreaterThanOrEqualToSomething() throws Exception {
        // Initialize the database
        customerRepository.saveAndFlush(customer);

        // Get all the customerList where dateDerniereViste is greater than or equal to DEFAULT_DATE_DERNIERE_VISTE
        defaultCustomerShouldBeFound("dateDerniereViste.greaterThanOrEqual=" + DEFAULT_DATE_DERNIERE_VISTE);

        // Get all the customerList where dateDerniereViste is greater than or equal to UPDATED_DATE_DERNIERE_VISTE
        defaultCustomerShouldNotBeFound("dateDerniereViste.greaterThanOrEqual=" + UPDATED_DATE_DERNIERE_VISTE);
    }

    @Test
    @Transactional
    void getAllCustomersByDateDerniereVisteIsLessThanOrEqualToSomething() throws Exception {
        // Initialize the database
        customerRepository.saveAndFlush(customer);

        // Get all the customerList where dateDerniereViste is less than or equal to DEFAULT_DATE_DERNIERE_VISTE
        defaultCustomerShouldBeFound("dateDerniereViste.lessThanOrEqual=" + DEFAULT_DATE_DERNIERE_VISTE);

        // Get all the customerList where dateDerniereViste is less than or equal to SMALLER_DATE_DERNIERE_VISTE
        defaultCustomerShouldNotBeFound("dateDerniereViste.lessThanOrEqual=" + SMALLER_DATE_DERNIERE_VISTE);
    }

    @Test
    @Transactional
    void getAllCustomersByDateDerniereVisteIsLessThanSomething() throws Exception {
        // Initialize the database
        customerRepository.saveAndFlush(customer);

        // Get all the customerList where dateDerniereViste is less than DEFAULT_DATE_DERNIERE_VISTE
        defaultCustomerShouldNotBeFound("dateDerniereViste.lessThan=" + DEFAULT_DATE_DERNIERE_VISTE);

        // Get all the customerList where dateDerniereViste is less than UPDATED_DATE_DERNIERE_VISTE
        defaultCustomerShouldBeFound("dateDerniereViste.lessThan=" + UPDATED_DATE_DERNIERE_VISTE);
    }

    @Test
    @Transactional
    void getAllCustomersByDateDerniereVisteIsGreaterThanSomething() throws Exception {
        // Initialize the database
        customerRepository.saveAndFlush(customer);

        // Get all the customerList where dateDerniereViste is greater than DEFAULT_DATE_DERNIERE_VISTE
        defaultCustomerShouldNotBeFound("dateDerniereViste.greaterThan=" + DEFAULT_DATE_DERNIERE_VISTE);

        // Get all the customerList where dateDerniereViste is greater than SMALLER_DATE_DERNIERE_VISTE
        defaultCustomerShouldBeFound("dateDerniereViste.greaterThan=" + SMALLER_DATE_DERNIERE_VISTE);
    }

    @Test
    @Transactional
    void getAllCustomersByLatitudeIsEqualToSomething() throws Exception {
        // Initialize the database
        customerRepository.saveAndFlush(customer);

        // Get all the customerList where latitude equals to DEFAULT_LATITUDE
        defaultCustomerShouldBeFound("latitude.equals=" + DEFAULT_LATITUDE);

        // Get all the customerList where latitude equals to UPDATED_LATITUDE
        defaultCustomerShouldNotBeFound("latitude.equals=" + UPDATED_LATITUDE);
    }

    @Test
    @Transactional
    void getAllCustomersByLatitudeIsNotEqualToSomething() throws Exception {
        // Initialize the database
        customerRepository.saveAndFlush(customer);

        // Get all the customerList where latitude not equals to DEFAULT_LATITUDE
        defaultCustomerShouldNotBeFound("latitude.notEquals=" + DEFAULT_LATITUDE);

        // Get all the customerList where latitude not equals to UPDATED_LATITUDE
        defaultCustomerShouldBeFound("latitude.notEquals=" + UPDATED_LATITUDE);
    }

    @Test
    @Transactional
    void getAllCustomersByLatitudeIsInShouldWork() throws Exception {
        // Initialize the database
        customerRepository.saveAndFlush(customer);

        // Get all the customerList where latitude in DEFAULT_LATITUDE or UPDATED_LATITUDE
        defaultCustomerShouldBeFound("latitude.in=" + DEFAULT_LATITUDE + "," + UPDATED_LATITUDE);

        // Get all the customerList where latitude equals to UPDATED_LATITUDE
        defaultCustomerShouldNotBeFound("latitude.in=" + UPDATED_LATITUDE);
    }

    @Test
    @Transactional
    void getAllCustomersByLatitudeIsNullOrNotNull() throws Exception {
        // Initialize the database
        customerRepository.saveAndFlush(customer);

        // Get all the customerList where latitude is not null
        defaultCustomerShouldBeFound("latitude.specified=true");

        // Get all the customerList where latitude is null
        defaultCustomerShouldNotBeFound("latitude.specified=false");
    }

    @Test
    @Transactional
    void getAllCustomersByLatitudeIsGreaterThanOrEqualToSomething() throws Exception {
        // Initialize the database
        customerRepository.saveAndFlush(customer);

        // Get all the customerList where latitude is greater than or equal to DEFAULT_LATITUDE
        defaultCustomerShouldBeFound("latitude.greaterThanOrEqual=" + DEFAULT_LATITUDE);

        // Get all the customerList where latitude is greater than or equal to UPDATED_LATITUDE
        defaultCustomerShouldNotBeFound("latitude.greaterThanOrEqual=" + UPDATED_LATITUDE);
    }

    @Test
    @Transactional
    void getAllCustomersByLatitudeIsLessThanOrEqualToSomething() throws Exception {
        // Initialize the database
        customerRepository.saveAndFlush(customer);

        // Get all the customerList where latitude is less than or equal to DEFAULT_LATITUDE
        defaultCustomerShouldBeFound("latitude.lessThanOrEqual=" + DEFAULT_LATITUDE);

        // Get all the customerList where latitude is less than or equal to SMALLER_LATITUDE
        defaultCustomerShouldNotBeFound("latitude.lessThanOrEqual=" + SMALLER_LATITUDE);
    }

    @Test
    @Transactional
    void getAllCustomersByLatitudeIsLessThanSomething() throws Exception {
        // Initialize the database
        customerRepository.saveAndFlush(customer);

        // Get all the customerList where latitude is less than DEFAULT_LATITUDE
        defaultCustomerShouldNotBeFound("latitude.lessThan=" + DEFAULT_LATITUDE);

        // Get all the customerList where latitude is less than UPDATED_LATITUDE
        defaultCustomerShouldBeFound("latitude.lessThan=" + UPDATED_LATITUDE);
    }

    @Test
    @Transactional
    void getAllCustomersByLatitudeIsGreaterThanSomething() throws Exception {
        // Initialize the database
        customerRepository.saveAndFlush(customer);

        // Get all the customerList where latitude is greater than DEFAULT_LATITUDE
        defaultCustomerShouldNotBeFound("latitude.greaterThan=" + DEFAULT_LATITUDE);

        // Get all the customerList where latitude is greater than SMALLER_LATITUDE
        defaultCustomerShouldBeFound("latitude.greaterThan=" + SMALLER_LATITUDE);
    }

    @Test
    @Transactional
    void getAllCustomersByLongitudeIsEqualToSomething() throws Exception {
        // Initialize the database
        customerRepository.saveAndFlush(customer);

        // Get all the customerList where longitude equals to DEFAULT_LONGITUDE
        defaultCustomerShouldBeFound("longitude.equals=" + DEFAULT_LONGITUDE);

        // Get all the customerList where longitude equals to UPDATED_LONGITUDE
        defaultCustomerShouldNotBeFound("longitude.equals=" + UPDATED_LONGITUDE);
    }

    @Test
    @Transactional
    void getAllCustomersByLongitudeIsNotEqualToSomething() throws Exception {
        // Initialize the database
        customerRepository.saveAndFlush(customer);

        // Get all the customerList where longitude not equals to DEFAULT_LONGITUDE
        defaultCustomerShouldNotBeFound("longitude.notEquals=" + DEFAULT_LONGITUDE);

        // Get all the customerList where longitude not equals to UPDATED_LONGITUDE
        defaultCustomerShouldBeFound("longitude.notEquals=" + UPDATED_LONGITUDE);
    }

    @Test
    @Transactional
    void getAllCustomersByLongitudeIsInShouldWork() throws Exception {
        // Initialize the database
        customerRepository.saveAndFlush(customer);

        // Get all the customerList where longitude in DEFAULT_LONGITUDE or UPDATED_LONGITUDE
        defaultCustomerShouldBeFound("longitude.in=" + DEFAULT_LONGITUDE + "," + UPDATED_LONGITUDE);

        // Get all the customerList where longitude equals to UPDATED_LONGITUDE
        defaultCustomerShouldNotBeFound("longitude.in=" + UPDATED_LONGITUDE);
    }

    @Test
    @Transactional
    void getAllCustomersByLongitudeIsNullOrNotNull() throws Exception {
        // Initialize the database
        customerRepository.saveAndFlush(customer);

        // Get all the customerList where longitude is not null
        defaultCustomerShouldBeFound("longitude.specified=true");

        // Get all the customerList where longitude is null
        defaultCustomerShouldNotBeFound("longitude.specified=false");
    }

    @Test
    @Transactional
    void getAllCustomersByLongitudeIsGreaterThanOrEqualToSomething() throws Exception {
        // Initialize the database
        customerRepository.saveAndFlush(customer);

        // Get all the customerList where longitude is greater than or equal to DEFAULT_LONGITUDE
        defaultCustomerShouldBeFound("longitude.greaterThanOrEqual=" + DEFAULT_LONGITUDE);

        // Get all the customerList where longitude is greater than or equal to UPDATED_LONGITUDE
        defaultCustomerShouldNotBeFound("longitude.greaterThanOrEqual=" + UPDATED_LONGITUDE);
    }

    @Test
    @Transactional
    void getAllCustomersByLongitudeIsLessThanOrEqualToSomething() throws Exception {
        // Initialize the database
        customerRepository.saveAndFlush(customer);

        // Get all the customerList where longitude is less than or equal to DEFAULT_LONGITUDE
        defaultCustomerShouldBeFound("longitude.lessThanOrEqual=" + DEFAULT_LONGITUDE);

        // Get all the customerList where longitude is less than or equal to SMALLER_LONGITUDE
        defaultCustomerShouldNotBeFound("longitude.lessThanOrEqual=" + SMALLER_LONGITUDE);
    }

    @Test
    @Transactional
    void getAllCustomersByLongitudeIsLessThanSomething() throws Exception {
        // Initialize the database
        customerRepository.saveAndFlush(customer);

        // Get all the customerList where longitude is less than DEFAULT_LONGITUDE
        defaultCustomerShouldNotBeFound("longitude.lessThan=" + DEFAULT_LONGITUDE);

        // Get all the customerList where longitude is less than UPDATED_LONGITUDE
        defaultCustomerShouldBeFound("longitude.lessThan=" + UPDATED_LONGITUDE);
    }

    @Test
    @Transactional
    void getAllCustomersByLongitudeIsGreaterThanSomething() throws Exception {
        // Initialize the database
        customerRepository.saveAndFlush(customer);

        // Get all the customerList where longitude is greater than DEFAULT_LONGITUDE
        defaultCustomerShouldNotBeFound("longitude.greaterThan=" + DEFAULT_LONGITUDE);

        // Get all the customerList where longitude is greater than SMALLER_LONGITUDE
        defaultCustomerShouldBeFound("longitude.greaterThan=" + SMALLER_LONGITUDE);
    }

    @Test
    @Transactional
    void getAllCustomersByCommercialIsEqualToSomething() throws Exception {
        // Initialize the database
        customerRepository.saveAndFlush(customer);
        Employee commercial;
        if (TestUtil.findAll(em, Employee.class).isEmpty()) {
            commercial = EmployeeResourceIT.createEntity(em);
            em.persist(commercial);
            em.flush();
        } else {
            commercial = TestUtil.findAll(em, Employee.class).get(0);
        }
        em.persist(commercial);
        em.flush();
        customer.setCommercial(commercial);
        customerRepository.saveAndFlush(customer);
        Long commercialId = commercial.getId();

        // Get all the customerList where commercial equals to commercialId
        defaultCustomerShouldBeFound("commercialId.equals=" + commercialId);

        // Get all the customerList where commercial equals to (commercialId + 1)
        defaultCustomerShouldNotBeFound("commercialId.equals=" + (commercialId + 1));
    }

    /**
     * Executes the search, and checks that the default entity is returned.
     */
    private void defaultCustomerShouldBeFound(String filter) throws Exception {
        restCustomerMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(customer.getId().intValue())))
            .andExpect(jsonPath("$.[*].customerType").value(hasItem(DEFAULT_CUSTOMER_TYPE.toString())))
            .andExpect(jsonPath("$.[*].company").value(hasItem(DEFAULT_COMPANY)))
            .andExpect(jsonPath("$.[*].lastName").value(hasItem(DEFAULT_LAST_NAME)))
            .andExpect(jsonPath("$.[*].firstName").value(hasItem(DEFAULT_FIRST_NAME)))
            .andExpect(jsonPath("$.[*].emailAddress").value(hasItem(DEFAULT_EMAIL_ADDRESS)))
            .andExpect(jsonPath("$.[*].jobTitle").value(hasItem(DEFAULT_JOB_TITLE)))
            .andExpect(jsonPath("$.[*].businessPhone").value(hasItem(DEFAULT_BUSINESS_PHONE)))
            .andExpect(jsonPath("$.[*].homePhone").value(hasItem(DEFAULT_HOME_PHONE)))
            .andExpect(jsonPath("$.[*].mobilePhone").value(hasItem(DEFAULT_MOBILE_PHONE)))
            .andExpect(jsonPath("$.[*].faxNumber").value(hasItem(DEFAULT_FAX_NUMBER)))
            .andExpect(jsonPath("$.[*].addresse").value(hasItem(DEFAULT_ADDRESSE.toString())))
            .andExpect(jsonPath("$.[*].wilaya").value(hasItem(DEFAULT_WILAYA)))
            .andExpect(jsonPath("$.[*].daira").value(hasItem(DEFAULT_DAIRA)))
            .andExpect(jsonPath("$.[*].codePostal").value(hasItem(DEFAULT_CODE_POSTAL)))
            .andExpect(jsonPath("$.[*].commune").value(hasItem(DEFAULT_COMMUNE)))
            .andExpect(jsonPath("$.[*].webPage").value(hasItem(DEFAULT_WEB_PAGE.toString())))
            .andExpect(jsonPath("$.[*].notes").value(hasItem(DEFAULT_NOTES.toString())))
            .andExpect(jsonPath("$.[*].attachmentsContentType").value(hasItem(DEFAULT_ATTACHMENTS_CONTENT_TYPE)))
            .andExpect(jsonPath("$.[*].attachments").value(hasItem(Base64Utils.encodeToString(DEFAULT_ATTACHMENTS))))
            .andExpect(jsonPath("$.[*].dejaClient").value(hasItem(DEFAULT_DEJA_CLIENT.booleanValue())))
            .andExpect(jsonPath("$.[*].dateDerniereViste").value(hasItem(DEFAULT_DATE_DERNIERE_VISTE.toString())))
            .andExpect(jsonPath("$.[*].latitude").value(hasItem(sameNumber(DEFAULT_LATITUDE))))
            .andExpect(jsonPath("$.[*].longitude").value(hasItem(sameNumber(DEFAULT_LONGITUDE))));

        // Check, that the count call also returns 1
        restCustomerMockMvc
            .perform(get(ENTITY_API_URL + "/count?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(content().string("1"));
    }

    /**
     * Executes the search, and checks that the default entity is not returned.
     */
    private void defaultCustomerShouldNotBeFound(String filter) throws Exception {
        restCustomerMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$").isArray())
            .andExpect(jsonPath("$").isEmpty());

        // Check, that the count call also returns 0
        restCustomerMockMvc
            .perform(get(ENTITY_API_URL + "/count?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(content().string("0"));
    }

    @Test
    @Transactional
    void getNonExistingCustomer() throws Exception {
        // Get the customer
        restCustomerMockMvc.perform(get(ENTITY_API_URL_ID, Long.MAX_VALUE)).andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    void putNewCustomer() throws Exception {
        // Initialize the database
        customerRepository.saveAndFlush(customer);

        int databaseSizeBeforeUpdate = customerRepository.findAll().size();

        // Update the customer
        Customer updatedCustomer = customerRepository.findById(customer.getId()).get();
        // Disconnect from session so that the updates on updatedCustomer are not directly saved in db
        em.detach(updatedCustomer);
        updatedCustomer
            .customerType(UPDATED_CUSTOMER_TYPE)
            .company(UPDATED_COMPANY)
            .lastName(UPDATED_LAST_NAME)
            .firstName(UPDATED_FIRST_NAME)
            .emailAddress(UPDATED_EMAIL_ADDRESS)
            .jobTitle(UPDATED_JOB_TITLE)
            .businessPhone(UPDATED_BUSINESS_PHONE)
            .homePhone(UPDATED_HOME_PHONE)
            .mobilePhone(UPDATED_MOBILE_PHONE)
            .faxNumber(UPDATED_FAX_NUMBER)
            .addresse(UPDATED_ADDRESSE)
            .wilaya(UPDATED_WILAYA)
            .daira(UPDATED_DAIRA)
            .codePostal(UPDATED_CODE_POSTAL)
            .commune(UPDATED_COMMUNE)
            .webPage(UPDATED_WEB_PAGE)
            .notes(UPDATED_NOTES)
            .attachments(UPDATED_ATTACHMENTS)
            .attachmentsContentType(UPDATED_ATTACHMENTS_CONTENT_TYPE)
            .dejaClient(UPDATED_DEJA_CLIENT)
            .dateDerniereViste(UPDATED_DATE_DERNIERE_VISTE)
            .latitude(UPDATED_LATITUDE)
            .longitude(UPDATED_LONGITUDE);

        restCustomerMockMvc
            .perform(
                put(ENTITY_API_URL_ID, updatedCustomer.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(updatedCustomer))
            )
            .andExpect(status().isOk());

        // Validate the Customer in the database
        List<Customer> customerList = customerRepository.findAll();
        assertThat(customerList).hasSize(databaseSizeBeforeUpdate);
        Customer testCustomer = customerList.get(customerList.size() - 1);
        assertThat(testCustomer.getCustomerType()).isEqualTo(UPDATED_CUSTOMER_TYPE);
        assertThat(testCustomer.getCompany()).isEqualTo(UPDATED_COMPANY);
        assertThat(testCustomer.getLastName()).isEqualTo(UPDATED_LAST_NAME);
        assertThat(testCustomer.getFirstName()).isEqualTo(UPDATED_FIRST_NAME);
        assertThat(testCustomer.getEmailAddress()).isEqualTo(UPDATED_EMAIL_ADDRESS);
        assertThat(testCustomer.getJobTitle()).isEqualTo(UPDATED_JOB_TITLE);
        assertThat(testCustomer.getBusinessPhone()).isEqualTo(UPDATED_BUSINESS_PHONE);
        assertThat(testCustomer.getHomePhone()).isEqualTo(UPDATED_HOME_PHONE);
        assertThat(testCustomer.getMobilePhone()).isEqualTo(UPDATED_MOBILE_PHONE);
        assertThat(testCustomer.getFaxNumber()).isEqualTo(UPDATED_FAX_NUMBER);
        assertThat(testCustomer.getAddresse()).isEqualTo(UPDATED_ADDRESSE);
        assertThat(testCustomer.getWilaya()).isEqualTo(UPDATED_WILAYA);
        assertThat(testCustomer.getDaira()).isEqualTo(UPDATED_DAIRA);
        assertThat(testCustomer.getCodePostal()).isEqualTo(UPDATED_CODE_POSTAL);
        assertThat(testCustomer.getCommune()).isEqualTo(UPDATED_COMMUNE);
        assertThat(testCustomer.getWebPage()).isEqualTo(UPDATED_WEB_PAGE);
        assertThat(testCustomer.getNotes()).isEqualTo(UPDATED_NOTES);
        assertThat(testCustomer.getAttachments()).isEqualTo(UPDATED_ATTACHMENTS);
        assertThat(testCustomer.getAttachmentsContentType()).isEqualTo(UPDATED_ATTACHMENTS_CONTENT_TYPE);
        assertThat(testCustomer.getDejaClient()).isEqualTo(UPDATED_DEJA_CLIENT);
        assertThat(testCustomer.getDateDerniereViste()).isEqualTo(UPDATED_DATE_DERNIERE_VISTE);
        assertThat(testCustomer.getLatitude()).isEqualByComparingTo(UPDATED_LATITUDE);
        assertThat(testCustomer.getLongitude()).isEqualByComparingTo(UPDATED_LONGITUDE);
    }

    @Test
    @Transactional
    void putNonExistingCustomer() throws Exception {
        int databaseSizeBeforeUpdate = customerRepository.findAll().size();
        customer.setId(count.incrementAndGet());

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restCustomerMockMvc
            .perform(
                put(ENTITY_API_URL_ID, customer.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(customer))
            )
            .andExpect(status().isBadRequest());

        // Validate the Customer in the database
        List<Customer> customerList = customerRepository.findAll();
        assertThat(customerList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithIdMismatchCustomer() throws Exception {
        int databaseSizeBeforeUpdate = customerRepository.findAll().size();
        customer.setId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restCustomerMockMvc
            .perform(
                put(ENTITY_API_URL_ID, count.incrementAndGet())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(customer))
            )
            .andExpect(status().isBadRequest());

        // Validate the Customer in the database
        List<Customer> customerList = customerRepository.findAll();
        assertThat(customerList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithMissingIdPathParamCustomer() throws Exception {
        int databaseSizeBeforeUpdate = customerRepository.findAll().size();
        customer.setId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restCustomerMockMvc
            .perform(put(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(customer)))
            .andExpect(status().isMethodNotAllowed());

        // Validate the Customer in the database
        List<Customer> customerList = customerRepository.findAll();
        assertThat(customerList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void partialUpdateCustomerWithPatch() throws Exception {
        // Initialize the database
        customerRepository.saveAndFlush(customer);

        int databaseSizeBeforeUpdate = customerRepository.findAll().size();

        // Update the customer using partial update
        Customer partialUpdatedCustomer = new Customer();
        partialUpdatedCustomer.setId(customer.getId());

        partialUpdatedCustomer
            .customerType(UPDATED_CUSTOMER_TYPE)
            .emailAddress(UPDATED_EMAIL_ADDRESS)
            .homePhone(UPDATED_HOME_PHONE)
            .mobilePhone(UPDATED_MOBILE_PHONE)
            .daira(UPDATED_DAIRA)
            .webPage(UPDATED_WEB_PAGE)
            .attachments(UPDATED_ATTACHMENTS)
            .attachmentsContentType(UPDATED_ATTACHMENTS_CONTENT_TYPE)
            .latitude(UPDATED_LATITUDE);

        restCustomerMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedCustomer.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedCustomer))
            )
            .andExpect(status().isOk());

        // Validate the Customer in the database
        List<Customer> customerList = customerRepository.findAll();
        assertThat(customerList).hasSize(databaseSizeBeforeUpdate);
        Customer testCustomer = customerList.get(customerList.size() - 1);
        assertThat(testCustomer.getCustomerType()).isEqualTo(UPDATED_CUSTOMER_TYPE);
        assertThat(testCustomer.getCompany()).isEqualTo(DEFAULT_COMPANY);
        assertThat(testCustomer.getLastName()).isEqualTo(DEFAULT_LAST_NAME);
        assertThat(testCustomer.getFirstName()).isEqualTo(DEFAULT_FIRST_NAME);
        assertThat(testCustomer.getEmailAddress()).isEqualTo(UPDATED_EMAIL_ADDRESS);
        assertThat(testCustomer.getJobTitle()).isEqualTo(DEFAULT_JOB_TITLE);
        assertThat(testCustomer.getBusinessPhone()).isEqualTo(DEFAULT_BUSINESS_PHONE);
        assertThat(testCustomer.getHomePhone()).isEqualTo(UPDATED_HOME_PHONE);
        assertThat(testCustomer.getMobilePhone()).isEqualTo(UPDATED_MOBILE_PHONE);
        assertThat(testCustomer.getFaxNumber()).isEqualTo(DEFAULT_FAX_NUMBER);
        assertThat(testCustomer.getAddresse()).isEqualTo(DEFAULT_ADDRESSE);
        assertThat(testCustomer.getWilaya()).isEqualTo(DEFAULT_WILAYA);
        assertThat(testCustomer.getDaira()).isEqualTo(UPDATED_DAIRA);
        assertThat(testCustomer.getCodePostal()).isEqualTo(DEFAULT_CODE_POSTAL);
        assertThat(testCustomer.getCommune()).isEqualTo(DEFAULT_COMMUNE);
        assertThat(testCustomer.getWebPage()).isEqualTo(UPDATED_WEB_PAGE);
        assertThat(testCustomer.getNotes()).isEqualTo(DEFAULT_NOTES);
        assertThat(testCustomer.getAttachments()).isEqualTo(UPDATED_ATTACHMENTS);
        assertThat(testCustomer.getAttachmentsContentType()).isEqualTo(UPDATED_ATTACHMENTS_CONTENT_TYPE);
        assertThat(testCustomer.getDejaClient()).isEqualTo(DEFAULT_DEJA_CLIENT);
        assertThat(testCustomer.getDateDerniereViste()).isEqualTo(DEFAULT_DATE_DERNIERE_VISTE);
        assertThat(testCustomer.getLatitude()).isEqualByComparingTo(UPDATED_LATITUDE);
        assertThat(testCustomer.getLongitude()).isEqualByComparingTo(DEFAULT_LONGITUDE);
    }

    @Test
    @Transactional
    void fullUpdateCustomerWithPatch() throws Exception {
        // Initialize the database
        customerRepository.saveAndFlush(customer);

        int databaseSizeBeforeUpdate = customerRepository.findAll().size();

        // Update the customer using partial update
        Customer partialUpdatedCustomer = new Customer();
        partialUpdatedCustomer.setId(customer.getId());

        partialUpdatedCustomer
            .customerType(UPDATED_CUSTOMER_TYPE)
            .company(UPDATED_COMPANY)
            .lastName(UPDATED_LAST_NAME)
            .firstName(UPDATED_FIRST_NAME)
            .emailAddress(UPDATED_EMAIL_ADDRESS)
            .jobTitle(UPDATED_JOB_TITLE)
            .businessPhone(UPDATED_BUSINESS_PHONE)
            .homePhone(UPDATED_HOME_PHONE)
            .mobilePhone(UPDATED_MOBILE_PHONE)
            .faxNumber(UPDATED_FAX_NUMBER)
            .addresse(UPDATED_ADDRESSE)
            .wilaya(UPDATED_WILAYA)
            .daira(UPDATED_DAIRA)
            .codePostal(UPDATED_CODE_POSTAL)
            .commune(UPDATED_COMMUNE)
            .webPage(UPDATED_WEB_PAGE)
            .notes(UPDATED_NOTES)
            .attachments(UPDATED_ATTACHMENTS)
            .attachmentsContentType(UPDATED_ATTACHMENTS_CONTENT_TYPE)
            .dejaClient(UPDATED_DEJA_CLIENT)
            .dateDerniereViste(UPDATED_DATE_DERNIERE_VISTE)
            .latitude(UPDATED_LATITUDE)
            .longitude(UPDATED_LONGITUDE);

        restCustomerMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedCustomer.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedCustomer))
            )
            .andExpect(status().isOk());

        // Validate the Customer in the database
        List<Customer> customerList = customerRepository.findAll();
        assertThat(customerList).hasSize(databaseSizeBeforeUpdate);
        Customer testCustomer = customerList.get(customerList.size() - 1);
        assertThat(testCustomer.getCustomerType()).isEqualTo(UPDATED_CUSTOMER_TYPE);
        assertThat(testCustomer.getCompany()).isEqualTo(UPDATED_COMPANY);
        assertThat(testCustomer.getLastName()).isEqualTo(UPDATED_LAST_NAME);
        assertThat(testCustomer.getFirstName()).isEqualTo(UPDATED_FIRST_NAME);
        assertThat(testCustomer.getEmailAddress()).isEqualTo(UPDATED_EMAIL_ADDRESS);
        assertThat(testCustomer.getJobTitle()).isEqualTo(UPDATED_JOB_TITLE);
        assertThat(testCustomer.getBusinessPhone()).isEqualTo(UPDATED_BUSINESS_PHONE);
        assertThat(testCustomer.getHomePhone()).isEqualTo(UPDATED_HOME_PHONE);
        assertThat(testCustomer.getMobilePhone()).isEqualTo(UPDATED_MOBILE_PHONE);
        assertThat(testCustomer.getFaxNumber()).isEqualTo(UPDATED_FAX_NUMBER);
        assertThat(testCustomer.getAddresse()).isEqualTo(UPDATED_ADDRESSE);
        assertThat(testCustomer.getWilaya()).isEqualTo(UPDATED_WILAYA);
        assertThat(testCustomer.getDaira()).isEqualTo(UPDATED_DAIRA);
        assertThat(testCustomer.getCodePostal()).isEqualTo(UPDATED_CODE_POSTAL);
        assertThat(testCustomer.getCommune()).isEqualTo(UPDATED_COMMUNE);
        assertThat(testCustomer.getWebPage()).isEqualTo(UPDATED_WEB_PAGE);
        assertThat(testCustomer.getNotes()).isEqualTo(UPDATED_NOTES);
        assertThat(testCustomer.getAttachments()).isEqualTo(UPDATED_ATTACHMENTS);
        assertThat(testCustomer.getAttachmentsContentType()).isEqualTo(UPDATED_ATTACHMENTS_CONTENT_TYPE);
        assertThat(testCustomer.getDejaClient()).isEqualTo(UPDATED_DEJA_CLIENT);
        assertThat(testCustomer.getDateDerniereViste()).isEqualTo(UPDATED_DATE_DERNIERE_VISTE);
        assertThat(testCustomer.getLatitude()).isEqualByComparingTo(UPDATED_LATITUDE);
        assertThat(testCustomer.getLongitude()).isEqualByComparingTo(UPDATED_LONGITUDE);
    }

    @Test
    @Transactional
    void patchNonExistingCustomer() throws Exception {
        int databaseSizeBeforeUpdate = customerRepository.findAll().size();
        customer.setId(count.incrementAndGet());

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restCustomerMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, customer.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(customer))
            )
            .andExpect(status().isBadRequest());

        // Validate the Customer in the database
        List<Customer> customerList = customerRepository.findAll();
        assertThat(customerList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithIdMismatchCustomer() throws Exception {
        int databaseSizeBeforeUpdate = customerRepository.findAll().size();
        customer.setId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restCustomerMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, count.incrementAndGet())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(customer))
            )
            .andExpect(status().isBadRequest());

        // Validate the Customer in the database
        List<Customer> customerList = customerRepository.findAll();
        assertThat(customerList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithMissingIdPathParamCustomer() throws Exception {
        int databaseSizeBeforeUpdate = customerRepository.findAll().size();
        customer.setId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restCustomerMockMvc
            .perform(patch(ENTITY_API_URL).contentType("application/merge-patch+json").content(TestUtil.convertObjectToJsonBytes(customer)))
            .andExpect(status().isMethodNotAllowed());

        // Validate the Customer in the database
        List<Customer> customerList = customerRepository.findAll();
        assertThat(customerList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void deleteCustomer() throws Exception {
        // Initialize the database
        customerRepository.saveAndFlush(customer);

        int databaseSizeBeforeDelete = customerRepository.findAll().size();

        // Delete the customer
        restCustomerMockMvc
            .perform(delete(ENTITY_API_URL_ID, customer.getId()).accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        List<Customer> customerList = customerRepository.findAll();
        assertThat(customerList).hasSize(databaseSizeBeforeDelete - 1);
    }
}
