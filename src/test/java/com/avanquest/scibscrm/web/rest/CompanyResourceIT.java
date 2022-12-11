package com.avanquest.scibscrm.web.rest;

import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import com.avanquest.scibscrm.IntegrationTest;
import com.avanquest.scibscrm.domain.Company;
import com.avanquest.scibscrm.repository.CompanyRepository;
import com.avanquest.scibscrm.service.criteria.CompanyCriteria;
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
import org.springframework.util.Base64Utils;

/**
 * Integration tests for the {@link CompanyResource} REST controller.
 */
@IntegrationTest
@AutoConfigureMockMvc
@WithMockUser
class CompanyResourceIT {

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

    private static final String DEFAULT_ADDRESS = "AAAAAAAAAA";
    private static final String UPDATED_ADDRESS = "BBBBBBBBBB";

    private static final String DEFAULT_CITY = "AAAAAAAAAA";
    private static final String UPDATED_CITY = "BBBBBBBBBB";

    private static final String DEFAULT_STATE_PROVINCE = "AAAAAAAAAA";
    private static final String UPDATED_STATE_PROVINCE = "BBBBBBBBBB";

    private static final String DEFAULT_ZIP_POSTAL_CODE = "AAAAAAAAAA";
    private static final String UPDATED_ZIP_POSTAL_CODE = "BBBBBBBBBB";

    private static final String DEFAULT_COUNTRY_REGION = "AAAAAAAAAA";
    private static final String UPDATED_COUNTRY_REGION = "BBBBBBBBBB";

    private static final String DEFAULT_WEB_PAGE = "AAAAAAAAAA";
    private static final String UPDATED_WEB_PAGE = "BBBBBBBBBB";

    private static final String DEFAULT_NOTES = "AAAAAAAAAA";
    private static final String UPDATED_NOTES = "BBBBBBBBBB";

    private static final byte[] DEFAULT_ATTACHMENTS = TestUtil.createByteArray(1, "0");
    private static final byte[] UPDATED_ATTACHMENTS = TestUtil.createByteArray(1, "1");
    private static final String DEFAULT_ATTACHMENTS_CONTENT_TYPE = "image/jpg";
    private static final String UPDATED_ATTACHMENTS_CONTENT_TYPE = "image/png";

    private static final String ENTITY_API_URL = "/api/companies";
    private static final String ENTITY_API_URL_ID = ENTITY_API_URL + "/{id}";

    private static Random random = new Random();
    private static AtomicLong count = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    @Autowired
    private CompanyRepository companyRepository;

    @Autowired
    private EntityManager em;

    @Autowired
    private MockMvc restCompanyMockMvc;

    private Company company;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Company createEntity(EntityManager em) {
        Company company = new Company()
            .company(DEFAULT_COMPANY)
            .lastName(DEFAULT_LAST_NAME)
            .firstName(DEFAULT_FIRST_NAME)
            .emailAddress(DEFAULT_EMAIL_ADDRESS)
            .jobTitle(DEFAULT_JOB_TITLE)
            .businessPhone(DEFAULT_BUSINESS_PHONE)
            .homePhone(DEFAULT_HOME_PHONE)
            .mobilePhone(DEFAULT_MOBILE_PHONE)
            .faxNumber(DEFAULT_FAX_NUMBER)
            .address(DEFAULT_ADDRESS)
            .city(DEFAULT_CITY)
            .stateProvince(DEFAULT_STATE_PROVINCE)
            .zipPostalCode(DEFAULT_ZIP_POSTAL_CODE)
            .countryRegion(DEFAULT_COUNTRY_REGION)
            .webPage(DEFAULT_WEB_PAGE)
            .notes(DEFAULT_NOTES)
            .attachments(DEFAULT_ATTACHMENTS)
            .attachmentsContentType(DEFAULT_ATTACHMENTS_CONTENT_TYPE);
        return company;
    }

    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Company createUpdatedEntity(EntityManager em) {
        Company company = new Company()
            .company(UPDATED_COMPANY)
            .lastName(UPDATED_LAST_NAME)
            .firstName(UPDATED_FIRST_NAME)
            .emailAddress(UPDATED_EMAIL_ADDRESS)
            .jobTitle(UPDATED_JOB_TITLE)
            .businessPhone(UPDATED_BUSINESS_PHONE)
            .homePhone(UPDATED_HOME_PHONE)
            .mobilePhone(UPDATED_MOBILE_PHONE)
            .faxNumber(UPDATED_FAX_NUMBER)
            .address(UPDATED_ADDRESS)
            .city(UPDATED_CITY)
            .stateProvince(UPDATED_STATE_PROVINCE)
            .zipPostalCode(UPDATED_ZIP_POSTAL_CODE)
            .countryRegion(UPDATED_COUNTRY_REGION)
            .webPage(UPDATED_WEB_PAGE)
            .notes(UPDATED_NOTES)
            .attachments(UPDATED_ATTACHMENTS)
            .attachmentsContentType(UPDATED_ATTACHMENTS_CONTENT_TYPE);
        return company;
    }

    @BeforeEach
    public void initTest() {
        company = createEntity(em);
    }

    @Test
    @Transactional
    void createCompany() throws Exception {
        int databaseSizeBeforeCreate = companyRepository.findAll().size();
        // Create the Company
        restCompanyMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(company)))
            .andExpect(status().isCreated());

        // Validate the Company in the database
        List<Company> companyList = companyRepository.findAll();
        assertThat(companyList).hasSize(databaseSizeBeforeCreate + 1);
        Company testCompany = companyList.get(companyList.size() - 1);
        assertThat(testCompany.getCompany()).isEqualTo(DEFAULT_COMPANY);
        assertThat(testCompany.getLastName()).isEqualTo(DEFAULT_LAST_NAME);
        assertThat(testCompany.getFirstName()).isEqualTo(DEFAULT_FIRST_NAME);
        assertThat(testCompany.getEmailAddress()).isEqualTo(DEFAULT_EMAIL_ADDRESS);
        assertThat(testCompany.getJobTitle()).isEqualTo(DEFAULT_JOB_TITLE);
        assertThat(testCompany.getBusinessPhone()).isEqualTo(DEFAULT_BUSINESS_PHONE);
        assertThat(testCompany.getHomePhone()).isEqualTo(DEFAULT_HOME_PHONE);
        assertThat(testCompany.getMobilePhone()).isEqualTo(DEFAULT_MOBILE_PHONE);
        assertThat(testCompany.getFaxNumber()).isEqualTo(DEFAULT_FAX_NUMBER);
        assertThat(testCompany.getAddress()).isEqualTo(DEFAULT_ADDRESS);
        assertThat(testCompany.getCity()).isEqualTo(DEFAULT_CITY);
        assertThat(testCompany.getStateProvince()).isEqualTo(DEFAULT_STATE_PROVINCE);
        assertThat(testCompany.getZipPostalCode()).isEqualTo(DEFAULT_ZIP_POSTAL_CODE);
        assertThat(testCompany.getCountryRegion()).isEqualTo(DEFAULT_COUNTRY_REGION);
        assertThat(testCompany.getWebPage()).isEqualTo(DEFAULT_WEB_PAGE);
        assertThat(testCompany.getNotes()).isEqualTo(DEFAULT_NOTES);
        assertThat(testCompany.getAttachments()).isEqualTo(DEFAULT_ATTACHMENTS);
        assertThat(testCompany.getAttachmentsContentType()).isEqualTo(DEFAULT_ATTACHMENTS_CONTENT_TYPE);
    }

    @Test
    @Transactional
    void createCompanyWithExistingId() throws Exception {
        // Create the Company with an existing ID
        company.setId(1L);

        int databaseSizeBeforeCreate = companyRepository.findAll().size();

        // An entity with an existing ID cannot be created, so this API call must fail
        restCompanyMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(company)))
            .andExpect(status().isBadRequest());

        // Validate the Company in the database
        List<Company> companyList = companyRepository.findAll();
        assertThat(companyList).hasSize(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    void getAllCompanies() throws Exception {
        // Initialize the database
        companyRepository.saveAndFlush(company);

        // Get all the companyList
        restCompanyMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(company.getId().intValue())))
            .andExpect(jsonPath("$.[*].company").value(hasItem(DEFAULT_COMPANY)))
            .andExpect(jsonPath("$.[*].lastName").value(hasItem(DEFAULT_LAST_NAME)))
            .andExpect(jsonPath("$.[*].firstName").value(hasItem(DEFAULT_FIRST_NAME)))
            .andExpect(jsonPath("$.[*].emailAddress").value(hasItem(DEFAULT_EMAIL_ADDRESS)))
            .andExpect(jsonPath("$.[*].jobTitle").value(hasItem(DEFAULT_JOB_TITLE)))
            .andExpect(jsonPath("$.[*].businessPhone").value(hasItem(DEFAULT_BUSINESS_PHONE)))
            .andExpect(jsonPath("$.[*].homePhone").value(hasItem(DEFAULT_HOME_PHONE)))
            .andExpect(jsonPath("$.[*].mobilePhone").value(hasItem(DEFAULT_MOBILE_PHONE)))
            .andExpect(jsonPath("$.[*].faxNumber").value(hasItem(DEFAULT_FAX_NUMBER)))
            .andExpect(jsonPath("$.[*].address").value(hasItem(DEFAULT_ADDRESS.toString())))
            .andExpect(jsonPath("$.[*].city").value(hasItem(DEFAULT_CITY)))
            .andExpect(jsonPath("$.[*].stateProvince").value(hasItem(DEFAULT_STATE_PROVINCE)))
            .andExpect(jsonPath("$.[*].zipPostalCode").value(hasItem(DEFAULT_ZIP_POSTAL_CODE)))
            .andExpect(jsonPath("$.[*].countryRegion").value(hasItem(DEFAULT_COUNTRY_REGION)))
            .andExpect(jsonPath("$.[*].webPage").value(hasItem(DEFAULT_WEB_PAGE.toString())))
            .andExpect(jsonPath("$.[*].notes").value(hasItem(DEFAULT_NOTES.toString())))
            .andExpect(jsonPath("$.[*].attachmentsContentType").value(hasItem(DEFAULT_ATTACHMENTS_CONTENT_TYPE)))
            .andExpect(jsonPath("$.[*].attachments").value(hasItem(Base64Utils.encodeToString(DEFAULT_ATTACHMENTS))));
    }

    @Test
    @Transactional
    void getCompany() throws Exception {
        // Initialize the database
        companyRepository.saveAndFlush(company);

        // Get the company
        restCompanyMockMvc
            .perform(get(ENTITY_API_URL_ID, company.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.id").value(company.getId().intValue()))
            .andExpect(jsonPath("$.company").value(DEFAULT_COMPANY))
            .andExpect(jsonPath("$.lastName").value(DEFAULT_LAST_NAME))
            .andExpect(jsonPath("$.firstName").value(DEFAULT_FIRST_NAME))
            .andExpect(jsonPath("$.emailAddress").value(DEFAULT_EMAIL_ADDRESS))
            .andExpect(jsonPath("$.jobTitle").value(DEFAULT_JOB_TITLE))
            .andExpect(jsonPath("$.businessPhone").value(DEFAULT_BUSINESS_PHONE))
            .andExpect(jsonPath("$.homePhone").value(DEFAULT_HOME_PHONE))
            .andExpect(jsonPath("$.mobilePhone").value(DEFAULT_MOBILE_PHONE))
            .andExpect(jsonPath("$.faxNumber").value(DEFAULT_FAX_NUMBER))
            .andExpect(jsonPath("$.address").value(DEFAULT_ADDRESS.toString()))
            .andExpect(jsonPath("$.city").value(DEFAULT_CITY))
            .andExpect(jsonPath("$.stateProvince").value(DEFAULT_STATE_PROVINCE))
            .andExpect(jsonPath("$.zipPostalCode").value(DEFAULT_ZIP_POSTAL_CODE))
            .andExpect(jsonPath("$.countryRegion").value(DEFAULT_COUNTRY_REGION))
            .andExpect(jsonPath("$.webPage").value(DEFAULT_WEB_PAGE.toString()))
            .andExpect(jsonPath("$.notes").value(DEFAULT_NOTES.toString()))
            .andExpect(jsonPath("$.attachmentsContentType").value(DEFAULT_ATTACHMENTS_CONTENT_TYPE))
            .andExpect(jsonPath("$.attachments").value(Base64Utils.encodeToString(DEFAULT_ATTACHMENTS)));
    }

    @Test
    @Transactional
    void getCompaniesByIdFiltering() throws Exception {
        // Initialize the database
        companyRepository.saveAndFlush(company);

        Long id = company.getId();

        defaultCompanyShouldBeFound("id.equals=" + id);
        defaultCompanyShouldNotBeFound("id.notEquals=" + id);

        defaultCompanyShouldBeFound("id.greaterThanOrEqual=" + id);
        defaultCompanyShouldNotBeFound("id.greaterThan=" + id);

        defaultCompanyShouldBeFound("id.lessThanOrEqual=" + id);
        defaultCompanyShouldNotBeFound("id.lessThan=" + id);
    }

    @Test
    @Transactional
    void getAllCompaniesByCompanyIsEqualToSomething() throws Exception {
        // Initialize the database
        companyRepository.saveAndFlush(company);

        // Get all the companyList where company equals to DEFAULT_COMPANY
        defaultCompanyShouldBeFound("company.equals=" + DEFAULT_COMPANY);

        // Get all the companyList where company equals to UPDATED_COMPANY
        defaultCompanyShouldNotBeFound("company.equals=" + UPDATED_COMPANY);
    }

    @Test
    @Transactional
    void getAllCompaniesByCompanyIsNotEqualToSomething() throws Exception {
        // Initialize the database
        companyRepository.saveAndFlush(company);

        // Get all the companyList where company not equals to DEFAULT_COMPANY
        defaultCompanyShouldNotBeFound("company.notEquals=" + DEFAULT_COMPANY);

        // Get all the companyList where company not equals to UPDATED_COMPANY
        defaultCompanyShouldBeFound("company.notEquals=" + UPDATED_COMPANY);
    }

    @Test
    @Transactional
    void getAllCompaniesByCompanyIsInShouldWork() throws Exception {
        // Initialize the database
        companyRepository.saveAndFlush(company);

        // Get all the companyList where company in DEFAULT_COMPANY or UPDATED_COMPANY
        defaultCompanyShouldBeFound("company.in=" + DEFAULT_COMPANY + "," + UPDATED_COMPANY);

        // Get all the companyList where company equals to UPDATED_COMPANY
        defaultCompanyShouldNotBeFound("company.in=" + UPDATED_COMPANY);
    }

    @Test
    @Transactional
    void getAllCompaniesByCompanyIsNullOrNotNull() throws Exception {
        // Initialize the database
        companyRepository.saveAndFlush(company);

        // Get all the companyList where company is not null
        defaultCompanyShouldBeFound("company.specified=true");

        // Get all the companyList where company is null
        defaultCompanyShouldNotBeFound("company.specified=false");
    }

    @Test
    @Transactional
    void getAllCompaniesByCompanyContainsSomething() throws Exception {
        // Initialize the database
        companyRepository.saveAndFlush(company);

        // Get all the companyList where company contains DEFAULT_COMPANY
        defaultCompanyShouldBeFound("company.contains=" + DEFAULT_COMPANY);

        // Get all the companyList where company contains UPDATED_COMPANY
        defaultCompanyShouldNotBeFound("company.contains=" + UPDATED_COMPANY);
    }

    @Test
    @Transactional
    void getAllCompaniesByCompanyNotContainsSomething() throws Exception {
        // Initialize the database
        companyRepository.saveAndFlush(company);

        // Get all the companyList where company does not contain DEFAULT_COMPANY
        defaultCompanyShouldNotBeFound("company.doesNotContain=" + DEFAULT_COMPANY);

        // Get all the companyList where company does not contain UPDATED_COMPANY
        defaultCompanyShouldBeFound("company.doesNotContain=" + UPDATED_COMPANY);
    }

    @Test
    @Transactional
    void getAllCompaniesByLastNameIsEqualToSomething() throws Exception {
        // Initialize the database
        companyRepository.saveAndFlush(company);

        // Get all the companyList where lastName equals to DEFAULT_LAST_NAME
        defaultCompanyShouldBeFound("lastName.equals=" + DEFAULT_LAST_NAME);

        // Get all the companyList where lastName equals to UPDATED_LAST_NAME
        defaultCompanyShouldNotBeFound("lastName.equals=" + UPDATED_LAST_NAME);
    }

    @Test
    @Transactional
    void getAllCompaniesByLastNameIsNotEqualToSomething() throws Exception {
        // Initialize the database
        companyRepository.saveAndFlush(company);

        // Get all the companyList where lastName not equals to DEFAULT_LAST_NAME
        defaultCompanyShouldNotBeFound("lastName.notEquals=" + DEFAULT_LAST_NAME);

        // Get all the companyList where lastName not equals to UPDATED_LAST_NAME
        defaultCompanyShouldBeFound("lastName.notEquals=" + UPDATED_LAST_NAME);
    }

    @Test
    @Transactional
    void getAllCompaniesByLastNameIsInShouldWork() throws Exception {
        // Initialize the database
        companyRepository.saveAndFlush(company);

        // Get all the companyList where lastName in DEFAULT_LAST_NAME or UPDATED_LAST_NAME
        defaultCompanyShouldBeFound("lastName.in=" + DEFAULT_LAST_NAME + "," + UPDATED_LAST_NAME);

        // Get all the companyList where lastName equals to UPDATED_LAST_NAME
        defaultCompanyShouldNotBeFound("lastName.in=" + UPDATED_LAST_NAME);
    }

    @Test
    @Transactional
    void getAllCompaniesByLastNameIsNullOrNotNull() throws Exception {
        // Initialize the database
        companyRepository.saveAndFlush(company);

        // Get all the companyList where lastName is not null
        defaultCompanyShouldBeFound("lastName.specified=true");

        // Get all the companyList where lastName is null
        defaultCompanyShouldNotBeFound("lastName.specified=false");
    }

    @Test
    @Transactional
    void getAllCompaniesByLastNameContainsSomething() throws Exception {
        // Initialize the database
        companyRepository.saveAndFlush(company);

        // Get all the companyList where lastName contains DEFAULT_LAST_NAME
        defaultCompanyShouldBeFound("lastName.contains=" + DEFAULT_LAST_NAME);

        // Get all the companyList where lastName contains UPDATED_LAST_NAME
        defaultCompanyShouldNotBeFound("lastName.contains=" + UPDATED_LAST_NAME);
    }

    @Test
    @Transactional
    void getAllCompaniesByLastNameNotContainsSomething() throws Exception {
        // Initialize the database
        companyRepository.saveAndFlush(company);

        // Get all the companyList where lastName does not contain DEFAULT_LAST_NAME
        defaultCompanyShouldNotBeFound("lastName.doesNotContain=" + DEFAULT_LAST_NAME);

        // Get all the companyList where lastName does not contain UPDATED_LAST_NAME
        defaultCompanyShouldBeFound("lastName.doesNotContain=" + UPDATED_LAST_NAME);
    }

    @Test
    @Transactional
    void getAllCompaniesByFirstNameIsEqualToSomething() throws Exception {
        // Initialize the database
        companyRepository.saveAndFlush(company);

        // Get all the companyList where firstName equals to DEFAULT_FIRST_NAME
        defaultCompanyShouldBeFound("firstName.equals=" + DEFAULT_FIRST_NAME);

        // Get all the companyList where firstName equals to UPDATED_FIRST_NAME
        defaultCompanyShouldNotBeFound("firstName.equals=" + UPDATED_FIRST_NAME);
    }

    @Test
    @Transactional
    void getAllCompaniesByFirstNameIsNotEqualToSomething() throws Exception {
        // Initialize the database
        companyRepository.saveAndFlush(company);

        // Get all the companyList where firstName not equals to DEFAULT_FIRST_NAME
        defaultCompanyShouldNotBeFound("firstName.notEquals=" + DEFAULT_FIRST_NAME);

        // Get all the companyList where firstName not equals to UPDATED_FIRST_NAME
        defaultCompanyShouldBeFound("firstName.notEquals=" + UPDATED_FIRST_NAME);
    }

    @Test
    @Transactional
    void getAllCompaniesByFirstNameIsInShouldWork() throws Exception {
        // Initialize the database
        companyRepository.saveAndFlush(company);

        // Get all the companyList where firstName in DEFAULT_FIRST_NAME or UPDATED_FIRST_NAME
        defaultCompanyShouldBeFound("firstName.in=" + DEFAULT_FIRST_NAME + "," + UPDATED_FIRST_NAME);

        // Get all the companyList where firstName equals to UPDATED_FIRST_NAME
        defaultCompanyShouldNotBeFound("firstName.in=" + UPDATED_FIRST_NAME);
    }

    @Test
    @Transactional
    void getAllCompaniesByFirstNameIsNullOrNotNull() throws Exception {
        // Initialize the database
        companyRepository.saveAndFlush(company);

        // Get all the companyList where firstName is not null
        defaultCompanyShouldBeFound("firstName.specified=true");

        // Get all the companyList where firstName is null
        defaultCompanyShouldNotBeFound("firstName.specified=false");
    }

    @Test
    @Transactional
    void getAllCompaniesByFirstNameContainsSomething() throws Exception {
        // Initialize the database
        companyRepository.saveAndFlush(company);

        // Get all the companyList where firstName contains DEFAULT_FIRST_NAME
        defaultCompanyShouldBeFound("firstName.contains=" + DEFAULT_FIRST_NAME);

        // Get all the companyList where firstName contains UPDATED_FIRST_NAME
        defaultCompanyShouldNotBeFound("firstName.contains=" + UPDATED_FIRST_NAME);
    }

    @Test
    @Transactional
    void getAllCompaniesByFirstNameNotContainsSomething() throws Exception {
        // Initialize the database
        companyRepository.saveAndFlush(company);

        // Get all the companyList where firstName does not contain DEFAULT_FIRST_NAME
        defaultCompanyShouldNotBeFound("firstName.doesNotContain=" + DEFAULT_FIRST_NAME);

        // Get all the companyList where firstName does not contain UPDATED_FIRST_NAME
        defaultCompanyShouldBeFound("firstName.doesNotContain=" + UPDATED_FIRST_NAME);
    }

    @Test
    @Transactional
    void getAllCompaniesByEmailAddressIsEqualToSomething() throws Exception {
        // Initialize the database
        companyRepository.saveAndFlush(company);

        // Get all the companyList where emailAddress equals to DEFAULT_EMAIL_ADDRESS
        defaultCompanyShouldBeFound("emailAddress.equals=" + DEFAULT_EMAIL_ADDRESS);

        // Get all the companyList where emailAddress equals to UPDATED_EMAIL_ADDRESS
        defaultCompanyShouldNotBeFound("emailAddress.equals=" + UPDATED_EMAIL_ADDRESS);
    }

    @Test
    @Transactional
    void getAllCompaniesByEmailAddressIsNotEqualToSomething() throws Exception {
        // Initialize the database
        companyRepository.saveAndFlush(company);

        // Get all the companyList where emailAddress not equals to DEFAULT_EMAIL_ADDRESS
        defaultCompanyShouldNotBeFound("emailAddress.notEquals=" + DEFAULT_EMAIL_ADDRESS);

        // Get all the companyList where emailAddress not equals to UPDATED_EMAIL_ADDRESS
        defaultCompanyShouldBeFound("emailAddress.notEquals=" + UPDATED_EMAIL_ADDRESS);
    }

    @Test
    @Transactional
    void getAllCompaniesByEmailAddressIsInShouldWork() throws Exception {
        // Initialize the database
        companyRepository.saveAndFlush(company);

        // Get all the companyList where emailAddress in DEFAULT_EMAIL_ADDRESS or UPDATED_EMAIL_ADDRESS
        defaultCompanyShouldBeFound("emailAddress.in=" + DEFAULT_EMAIL_ADDRESS + "," + UPDATED_EMAIL_ADDRESS);

        // Get all the companyList where emailAddress equals to UPDATED_EMAIL_ADDRESS
        defaultCompanyShouldNotBeFound("emailAddress.in=" + UPDATED_EMAIL_ADDRESS);
    }

    @Test
    @Transactional
    void getAllCompaniesByEmailAddressIsNullOrNotNull() throws Exception {
        // Initialize the database
        companyRepository.saveAndFlush(company);

        // Get all the companyList where emailAddress is not null
        defaultCompanyShouldBeFound("emailAddress.specified=true");

        // Get all the companyList where emailAddress is null
        defaultCompanyShouldNotBeFound("emailAddress.specified=false");
    }

    @Test
    @Transactional
    void getAllCompaniesByEmailAddressContainsSomething() throws Exception {
        // Initialize the database
        companyRepository.saveAndFlush(company);

        // Get all the companyList where emailAddress contains DEFAULT_EMAIL_ADDRESS
        defaultCompanyShouldBeFound("emailAddress.contains=" + DEFAULT_EMAIL_ADDRESS);

        // Get all the companyList where emailAddress contains UPDATED_EMAIL_ADDRESS
        defaultCompanyShouldNotBeFound("emailAddress.contains=" + UPDATED_EMAIL_ADDRESS);
    }

    @Test
    @Transactional
    void getAllCompaniesByEmailAddressNotContainsSomething() throws Exception {
        // Initialize the database
        companyRepository.saveAndFlush(company);

        // Get all the companyList where emailAddress does not contain DEFAULT_EMAIL_ADDRESS
        defaultCompanyShouldNotBeFound("emailAddress.doesNotContain=" + DEFAULT_EMAIL_ADDRESS);

        // Get all the companyList where emailAddress does not contain UPDATED_EMAIL_ADDRESS
        defaultCompanyShouldBeFound("emailAddress.doesNotContain=" + UPDATED_EMAIL_ADDRESS);
    }

    @Test
    @Transactional
    void getAllCompaniesByJobTitleIsEqualToSomething() throws Exception {
        // Initialize the database
        companyRepository.saveAndFlush(company);

        // Get all the companyList where jobTitle equals to DEFAULT_JOB_TITLE
        defaultCompanyShouldBeFound("jobTitle.equals=" + DEFAULT_JOB_TITLE);

        // Get all the companyList where jobTitle equals to UPDATED_JOB_TITLE
        defaultCompanyShouldNotBeFound("jobTitle.equals=" + UPDATED_JOB_TITLE);
    }

    @Test
    @Transactional
    void getAllCompaniesByJobTitleIsNotEqualToSomething() throws Exception {
        // Initialize the database
        companyRepository.saveAndFlush(company);

        // Get all the companyList where jobTitle not equals to DEFAULT_JOB_TITLE
        defaultCompanyShouldNotBeFound("jobTitle.notEquals=" + DEFAULT_JOB_TITLE);

        // Get all the companyList where jobTitle not equals to UPDATED_JOB_TITLE
        defaultCompanyShouldBeFound("jobTitle.notEquals=" + UPDATED_JOB_TITLE);
    }

    @Test
    @Transactional
    void getAllCompaniesByJobTitleIsInShouldWork() throws Exception {
        // Initialize the database
        companyRepository.saveAndFlush(company);

        // Get all the companyList where jobTitle in DEFAULT_JOB_TITLE or UPDATED_JOB_TITLE
        defaultCompanyShouldBeFound("jobTitle.in=" + DEFAULT_JOB_TITLE + "," + UPDATED_JOB_TITLE);

        // Get all the companyList where jobTitle equals to UPDATED_JOB_TITLE
        defaultCompanyShouldNotBeFound("jobTitle.in=" + UPDATED_JOB_TITLE);
    }

    @Test
    @Transactional
    void getAllCompaniesByJobTitleIsNullOrNotNull() throws Exception {
        // Initialize the database
        companyRepository.saveAndFlush(company);

        // Get all the companyList where jobTitle is not null
        defaultCompanyShouldBeFound("jobTitle.specified=true");

        // Get all the companyList where jobTitle is null
        defaultCompanyShouldNotBeFound("jobTitle.specified=false");
    }

    @Test
    @Transactional
    void getAllCompaniesByJobTitleContainsSomething() throws Exception {
        // Initialize the database
        companyRepository.saveAndFlush(company);

        // Get all the companyList where jobTitle contains DEFAULT_JOB_TITLE
        defaultCompanyShouldBeFound("jobTitle.contains=" + DEFAULT_JOB_TITLE);

        // Get all the companyList where jobTitle contains UPDATED_JOB_TITLE
        defaultCompanyShouldNotBeFound("jobTitle.contains=" + UPDATED_JOB_TITLE);
    }

    @Test
    @Transactional
    void getAllCompaniesByJobTitleNotContainsSomething() throws Exception {
        // Initialize the database
        companyRepository.saveAndFlush(company);

        // Get all the companyList where jobTitle does not contain DEFAULT_JOB_TITLE
        defaultCompanyShouldNotBeFound("jobTitle.doesNotContain=" + DEFAULT_JOB_TITLE);

        // Get all the companyList where jobTitle does not contain UPDATED_JOB_TITLE
        defaultCompanyShouldBeFound("jobTitle.doesNotContain=" + UPDATED_JOB_TITLE);
    }

    @Test
    @Transactional
    void getAllCompaniesByBusinessPhoneIsEqualToSomething() throws Exception {
        // Initialize the database
        companyRepository.saveAndFlush(company);

        // Get all the companyList where businessPhone equals to DEFAULT_BUSINESS_PHONE
        defaultCompanyShouldBeFound("businessPhone.equals=" + DEFAULT_BUSINESS_PHONE);

        // Get all the companyList where businessPhone equals to UPDATED_BUSINESS_PHONE
        defaultCompanyShouldNotBeFound("businessPhone.equals=" + UPDATED_BUSINESS_PHONE);
    }

    @Test
    @Transactional
    void getAllCompaniesByBusinessPhoneIsNotEqualToSomething() throws Exception {
        // Initialize the database
        companyRepository.saveAndFlush(company);

        // Get all the companyList where businessPhone not equals to DEFAULT_BUSINESS_PHONE
        defaultCompanyShouldNotBeFound("businessPhone.notEquals=" + DEFAULT_BUSINESS_PHONE);

        // Get all the companyList where businessPhone not equals to UPDATED_BUSINESS_PHONE
        defaultCompanyShouldBeFound("businessPhone.notEquals=" + UPDATED_BUSINESS_PHONE);
    }

    @Test
    @Transactional
    void getAllCompaniesByBusinessPhoneIsInShouldWork() throws Exception {
        // Initialize the database
        companyRepository.saveAndFlush(company);

        // Get all the companyList where businessPhone in DEFAULT_BUSINESS_PHONE or UPDATED_BUSINESS_PHONE
        defaultCompanyShouldBeFound("businessPhone.in=" + DEFAULT_BUSINESS_PHONE + "," + UPDATED_BUSINESS_PHONE);

        // Get all the companyList where businessPhone equals to UPDATED_BUSINESS_PHONE
        defaultCompanyShouldNotBeFound("businessPhone.in=" + UPDATED_BUSINESS_PHONE);
    }

    @Test
    @Transactional
    void getAllCompaniesByBusinessPhoneIsNullOrNotNull() throws Exception {
        // Initialize the database
        companyRepository.saveAndFlush(company);

        // Get all the companyList where businessPhone is not null
        defaultCompanyShouldBeFound("businessPhone.specified=true");

        // Get all the companyList where businessPhone is null
        defaultCompanyShouldNotBeFound("businessPhone.specified=false");
    }

    @Test
    @Transactional
    void getAllCompaniesByBusinessPhoneContainsSomething() throws Exception {
        // Initialize the database
        companyRepository.saveAndFlush(company);

        // Get all the companyList where businessPhone contains DEFAULT_BUSINESS_PHONE
        defaultCompanyShouldBeFound("businessPhone.contains=" + DEFAULT_BUSINESS_PHONE);

        // Get all the companyList where businessPhone contains UPDATED_BUSINESS_PHONE
        defaultCompanyShouldNotBeFound("businessPhone.contains=" + UPDATED_BUSINESS_PHONE);
    }

    @Test
    @Transactional
    void getAllCompaniesByBusinessPhoneNotContainsSomething() throws Exception {
        // Initialize the database
        companyRepository.saveAndFlush(company);

        // Get all the companyList where businessPhone does not contain DEFAULT_BUSINESS_PHONE
        defaultCompanyShouldNotBeFound("businessPhone.doesNotContain=" + DEFAULT_BUSINESS_PHONE);

        // Get all the companyList where businessPhone does not contain UPDATED_BUSINESS_PHONE
        defaultCompanyShouldBeFound("businessPhone.doesNotContain=" + UPDATED_BUSINESS_PHONE);
    }

    @Test
    @Transactional
    void getAllCompaniesByHomePhoneIsEqualToSomething() throws Exception {
        // Initialize the database
        companyRepository.saveAndFlush(company);

        // Get all the companyList where homePhone equals to DEFAULT_HOME_PHONE
        defaultCompanyShouldBeFound("homePhone.equals=" + DEFAULT_HOME_PHONE);

        // Get all the companyList where homePhone equals to UPDATED_HOME_PHONE
        defaultCompanyShouldNotBeFound("homePhone.equals=" + UPDATED_HOME_PHONE);
    }

    @Test
    @Transactional
    void getAllCompaniesByHomePhoneIsNotEqualToSomething() throws Exception {
        // Initialize the database
        companyRepository.saveAndFlush(company);

        // Get all the companyList where homePhone not equals to DEFAULT_HOME_PHONE
        defaultCompanyShouldNotBeFound("homePhone.notEquals=" + DEFAULT_HOME_PHONE);

        // Get all the companyList where homePhone not equals to UPDATED_HOME_PHONE
        defaultCompanyShouldBeFound("homePhone.notEquals=" + UPDATED_HOME_PHONE);
    }

    @Test
    @Transactional
    void getAllCompaniesByHomePhoneIsInShouldWork() throws Exception {
        // Initialize the database
        companyRepository.saveAndFlush(company);

        // Get all the companyList where homePhone in DEFAULT_HOME_PHONE or UPDATED_HOME_PHONE
        defaultCompanyShouldBeFound("homePhone.in=" + DEFAULT_HOME_PHONE + "," + UPDATED_HOME_PHONE);

        // Get all the companyList where homePhone equals to UPDATED_HOME_PHONE
        defaultCompanyShouldNotBeFound("homePhone.in=" + UPDATED_HOME_PHONE);
    }

    @Test
    @Transactional
    void getAllCompaniesByHomePhoneIsNullOrNotNull() throws Exception {
        // Initialize the database
        companyRepository.saveAndFlush(company);

        // Get all the companyList where homePhone is not null
        defaultCompanyShouldBeFound("homePhone.specified=true");

        // Get all the companyList where homePhone is null
        defaultCompanyShouldNotBeFound("homePhone.specified=false");
    }

    @Test
    @Transactional
    void getAllCompaniesByHomePhoneContainsSomething() throws Exception {
        // Initialize the database
        companyRepository.saveAndFlush(company);

        // Get all the companyList where homePhone contains DEFAULT_HOME_PHONE
        defaultCompanyShouldBeFound("homePhone.contains=" + DEFAULT_HOME_PHONE);

        // Get all the companyList where homePhone contains UPDATED_HOME_PHONE
        defaultCompanyShouldNotBeFound("homePhone.contains=" + UPDATED_HOME_PHONE);
    }

    @Test
    @Transactional
    void getAllCompaniesByHomePhoneNotContainsSomething() throws Exception {
        // Initialize the database
        companyRepository.saveAndFlush(company);

        // Get all the companyList where homePhone does not contain DEFAULT_HOME_PHONE
        defaultCompanyShouldNotBeFound("homePhone.doesNotContain=" + DEFAULT_HOME_PHONE);

        // Get all the companyList where homePhone does not contain UPDATED_HOME_PHONE
        defaultCompanyShouldBeFound("homePhone.doesNotContain=" + UPDATED_HOME_PHONE);
    }

    @Test
    @Transactional
    void getAllCompaniesByMobilePhoneIsEqualToSomething() throws Exception {
        // Initialize the database
        companyRepository.saveAndFlush(company);

        // Get all the companyList where mobilePhone equals to DEFAULT_MOBILE_PHONE
        defaultCompanyShouldBeFound("mobilePhone.equals=" + DEFAULT_MOBILE_PHONE);

        // Get all the companyList where mobilePhone equals to UPDATED_MOBILE_PHONE
        defaultCompanyShouldNotBeFound("mobilePhone.equals=" + UPDATED_MOBILE_PHONE);
    }

    @Test
    @Transactional
    void getAllCompaniesByMobilePhoneIsNotEqualToSomething() throws Exception {
        // Initialize the database
        companyRepository.saveAndFlush(company);

        // Get all the companyList where mobilePhone not equals to DEFAULT_MOBILE_PHONE
        defaultCompanyShouldNotBeFound("mobilePhone.notEquals=" + DEFAULT_MOBILE_PHONE);

        // Get all the companyList where mobilePhone not equals to UPDATED_MOBILE_PHONE
        defaultCompanyShouldBeFound("mobilePhone.notEquals=" + UPDATED_MOBILE_PHONE);
    }

    @Test
    @Transactional
    void getAllCompaniesByMobilePhoneIsInShouldWork() throws Exception {
        // Initialize the database
        companyRepository.saveAndFlush(company);

        // Get all the companyList where mobilePhone in DEFAULT_MOBILE_PHONE or UPDATED_MOBILE_PHONE
        defaultCompanyShouldBeFound("mobilePhone.in=" + DEFAULT_MOBILE_PHONE + "," + UPDATED_MOBILE_PHONE);

        // Get all the companyList where mobilePhone equals to UPDATED_MOBILE_PHONE
        defaultCompanyShouldNotBeFound("mobilePhone.in=" + UPDATED_MOBILE_PHONE);
    }

    @Test
    @Transactional
    void getAllCompaniesByMobilePhoneIsNullOrNotNull() throws Exception {
        // Initialize the database
        companyRepository.saveAndFlush(company);

        // Get all the companyList where mobilePhone is not null
        defaultCompanyShouldBeFound("mobilePhone.specified=true");

        // Get all the companyList where mobilePhone is null
        defaultCompanyShouldNotBeFound("mobilePhone.specified=false");
    }

    @Test
    @Transactional
    void getAllCompaniesByMobilePhoneContainsSomething() throws Exception {
        // Initialize the database
        companyRepository.saveAndFlush(company);

        // Get all the companyList where mobilePhone contains DEFAULT_MOBILE_PHONE
        defaultCompanyShouldBeFound("mobilePhone.contains=" + DEFAULT_MOBILE_PHONE);

        // Get all the companyList where mobilePhone contains UPDATED_MOBILE_PHONE
        defaultCompanyShouldNotBeFound("mobilePhone.contains=" + UPDATED_MOBILE_PHONE);
    }

    @Test
    @Transactional
    void getAllCompaniesByMobilePhoneNotContainsSomething() throws Exception {
        // Initialize the database
        companyRepository.saveAndFlush(company);

        // Get all the companyList where mobilePhone does not contain DEFAULT_MOBILE_PHONE
        defaultCompanyShouldNotBeFound("mobilePhone.doesNotContain=" + DEFAULT_MOBILE_PHONE);

        // Get all the companyList where mobilePhone does not contain UPDATED_MOBILE_PHONE
        defaultCompanyShouldBeFound("mobilePhone.doesNotContain=" + UPDATED_MOBILE_PHONE);
    }

    @Test
    @Transactional
    void getAllCompaniesByFaxNumberIsEqualToSomething() throws Exception {
        // Initialize the database
        companyRepository.saveAndFlush(company);

        // Get all the companyList where faxNumber equals to DEFAULT_FAX_NUMBER
        defaultCompanyShouldBeFound("faxNumber.equals=" + DEFAULT_FAX_NUMBER);

        // Get all the companyList where faxNumber equals to UPDATED_FAX_NUMBER
        defaultCompanyShouldNotBeFound("faxNumber.equals=" + UPDATED_FAX_NUMBER);
    }

    @Test
    @Transactional
    void getAllCompaniesByFaxNumberIsNotEqualToSomething() throws Exception {
        // Initialize the database
        companyRepository.saveAndFlush(company);

        // Get all the companyList where faxNumber not equals to DEFAULT_FAX_NUMBER
        defaultCompanyShouldNotBeFound("faxNumber.notEquals=" + DEFAULT_FAX_NUMBER);

        // Get all the companyList where faxNumber not equals to UPDATED_FAX_NUMBER
        defaultCompanyShouldBeFound("faxNumber.notEquals=" + UPDATED_FAX_NUMBER);
    }

    @Test
    @Transactional
    void getAllCompaniesByFaxNumberIsInShouldWork() throws Exception {
        // Initialize the database
        companyRepository.saveAndFlush(company);

        // Get all the companyList where faxNumber in DEFAULT_FAX_NUMBER or UPDATED_FAX_NUMBER
        defaultCompanyShouldBeFound("faxNumber.in=" + DEFAULT_FAX_NUMBER + "," + UPDATED_FAX_NUMBER);

        // Get all the companyList where faxNumber equals to UPDATED_FAX_NUMBER
        defaultCompanyShouldNotBeFound("faxNumber.in=" + UPDATED_FAX_NUMBER);
    }

    @Test
    @Transactional
    void getAllCompaniesByFaxNumberIsNullOrNotNull() throws Exception {
        // Initialize the database
        companyRepository.saveAndFlush(company);

        // Get all the companyList where faxNumber is not null
        defaultCompanyShouldBeFound("faxNumber.specified=true");

        // Get all the companyList where faxNumber is null
        defaultCompanyShouldNotBeFound("faxNumber.specified=false");
    }

    @Test
    @Transactional
    void getAllCompaniesByFaxNumberContainsSomething() throws Exception {
        // Initialize the database
        companyRepository.saveAndFlush(company);

        // Get all the companyList where faxNumber contains DEFAULT_FAX_NUMBER
        defaultCompanyShouldBeFound("faxNumber.contains=" + DEFAULT_FAX_NUMBER);

        // Get all the companyList where faxNumber contains UPDATED_FAX_NUMBER
        defaultCompanyShouldNotBeFound("faxNumber.contains=" + UPDATED_FAX_NUMBER);
    }

    @Test
    @Transactional
    void getAllCompaniesByFaxNumberNotContainsSomething() throws Exception {
        // Initialize the database
        companyRepository.saveAndFlush(company);

        // Get all the companyList where faxNumber does not contain DEFAULT_FAX_NUMBER
        defaultCompanyShouldNotBeFound("faxNumber.doesNotContain=" + DEFAULT_FAX_NUMBER);

        // Get all the companyList where faxNumber does not contain UPDATED_FAX_NUMBER
        defaultCompanyShouldBeFound("faxNumber.doesNotContain=" + UPDATED_FAX_NUMBER);
    }

    @Test
    @Transactional
    void getAllCompaniesByCityIsEqualToSomething() throws Exception {
        // Initialize the database
        companyRepository.saveAndFlush(company);

        // Get all the companyList where city equals to DEFAULT_CITY
        defaultCompanyShouldBeFound("city.equals=" + DEFAULT_CITY);

        // Get all the companyList where city equals to UPDATED_CITY
        defaultCompanyShouldNotBeFound("city.equals=" + UPDATED_CITY);
    }

    @Test
    @Transactional
    void getAllCompaniesByCityIsNotEqualToSomething() throws Exception {
        // Initialize the database
        companyRepository.saveAndFlush(company);

        // Get all the companyList where city not equals to DEFAULT_CITY
        defaultCompanyShouldNotBeFound("city.notEquals=" + DEFAULT_CITY);

        // Get all the companyList where city not equals to UPDATED_CITY
        defaultCompanyShouldBeFound("city.notEquals=" + UPDATED_CITY);
    }

    @Test
    @Transactional
    void getAllCompaniesByCityIsInShouldWork() throws Exception {
        // Initialize the database
        companyRepository.saveAndFlush(company);

        // Get all the companyList where city in DEFAULT_CITY or UPDATED_CITY
        defaultCompanyShouldBeFound("city.in=" + DEFAULT_CITY + "," + UPDATED_CITY);

        // Get all the companyList where city equals to UPDATED_CITY
        defaultCompanyShouldNotBeFound("city.in=" + UPDATED_CITY);
    }

    @Test
    @Transactional
    void getAllCompaniesByCityIsNullOrNotNull() throws Exception {
        // Initialize the database
        companyRepository.saveAndFlush(company);

        // Get all the companyList where city is not null
        defaultCompanyShouldBeFound("city.specified=true");

        // Get all the companyList where city is null
        defaultCompanyShouldNotBeFound("city.specified=false");
    }

    @Test
    @Transactional
    void getAllCompaniesByCityContainsSomething() throws Exception {
        // Initialize the database
        companyRepository.saveAndFlush(company);

        // Get all the companyList where city contains DEFAULT_CITY
        defaultCompanyShouldBeFound("city.contains=" + DEFAULT_CITY);

        // Get all the companyList where city contains UPDATED_CITY
        defaultCompanyShouldNotBeFound("city.contains=" + UPDATED_CITY);
    }

    @Test
    @Transactional
    void getAllCompaniesByCityNotContainsSomething() throws Exception {
        // Initialize the database
        companyRepository.saveAndFlush(company);

        // Get all the companyList where city does not contain DEFAULT_CITY
        defaultCompanyShouldNotBeFound("city.doesNotContain=" + DEFAULT_CITY);

        // Get all the companyList where city does not contain UPDATED_CITY
        defaultCompanyShouldBeFound("city.doesNotContain=" + UPDATED_CITY);
    }

    @Test
    @Transactional
    void getAllCompaniesByStateProvinceIsEqualToSomething() throws Exception {
        // Initialize the database
        companyRepository.saveAndFlush(company);

        // Get all the companyList where stateProvince equals to DEFAULT_STATE_PROVINCE
        defaultCompanyShouldBeFound("stateProvince.equals=" + DEFAULT_STATE_PROVINCE);

        // Get all the companyList where stateProvince equals to UPDATED_STATE_PROVINCE
        defaultCompanyShouldNotBeFound("stateProvince.equals=" + UPDATED_STATE_PROVINCE);
    }

    @Test
    @Transactional
    void getAllCompaniesByStateProvinceIsNotEqualToSomething() throws Exception {
        // Initialize the database
        companyRepository.saveAndFlush(company);

        // Get all the companyList where stateProvince not equals to DEFAULT_STATE_PROVINCE
        defaultCompanyShouldNotBeFound("stateProvince.notEquals=" + DEFAULT_STATE_PROVINCE);

        // Get all the companyList where stateProvince not equals to UPDATED_STATE_PROVINCE
        defaultCompanyShouldBeFound("stateProvince.notEquals=" + UPDATED_STATE_PROVINCE);
    }

    @Test
    @Transactional
    void getAllCompaniesByStateProvinceIsInShouldWork() throws Exception {
        // Initialize the database
        companyRepository.saveAndFlush(company);

        // Get all the companyList where stateProvince in DEFAULT_STATE_PROVINCE or UPDATED_STATE_PROVINCE
        defaultCompanyShouldBeFound("stateProvince.in=" + DEFAULT_STATE_PROVINCE + "," + UPDATED_STATE_PROVINCE);

        // Get all the companyList where stateProvince equals to UPDATED_STATE_PROVINCE
        defaultCompanyShouldNotBeFound("stateProvince.in=" + UPDATED_STATE_PROVINCE);
    }

    @Test
    @Transactional
    void getAllCompaniesByStateProvinceIsNullOrNotNull() throws Exception {
        // Initialize the database
        companyRepository.saveAndFlush(company);

        // Get all the companyList where stateProvince is not null
        defaultCompanyShouldBeFound("stateProvince.specified=true");

        // Get all the companyList where stateProvince is null
        defaultCompanyShouldNotBeFound("stateProvince.specified=false");
    }

    @Test
    @Transactional
    void getAllCompaniesByStateProvinceContainsSomething() throws Exception {
        // Initialize the database
        companyRepository.saveAndFlush(company);

        // Get all the companyList where stateProvince contains DEFAULT_STATE_PROVINCE
        defaultCompanyShouldBeFound("stateProvince.contains=" + DEFAULT_STATE_PROVINCE);

        // Get all the companyList where stateProvince contains UPDATED_STATE_PROVINCE
        defaultCompanyShouldNotBeFound("stateProvince.contains=" + UPDATED_STATE_PROVINCE);
    }

    @Test
    @Transactional
    void getAllCompaniesByStateProvinceNotContainsSomething() throws Exception {
        // Initialize the database
        companyRepository.saveAndFlush(company);

        // Get all the companyList where stateProvince does not contain DEFAULT_STATE_PROVINCE
        defaultCompanyShouldNotBeFound("stateProvince.doesNotContain=" + DEFAULT_STATE_PROVINCE);

        // Get all the companyList where stateProvince does not contain UPDATED_STATE_PROVINCE
        defaultCompanyShouldBeFound("stateProvince.doesNotContain=" + UPDATED_STATE_PROVINCE);
    }

    @Test
    @Transactional
    void getAllCompaniesByZipPostalCodeIsEqualToSomething() throws Exception {
        // Initialize the database
        companyRepository.saveAndFlush(company);

        // Get all the companyList where zipPostalCode equals to DEFAULT_ZIP_POSTAL_CODE
        defaultCompanyShouldBeFound("zipPostalCode.equals=" + DEFAULT_ZIP_POSTAL_CODE);

        // Get all the companyList where zipPostalCode equals to UPDATED_ZIP_POSTAL_CODE
        defaultCompanyShouldNotBeFound("zipPostalCode.equals=" + UPDATED_ZIP_POSTAL_CODE);
    }

    @Test
    @Transactional
    void getAllCompaniesByZipPostalCodeIsNotEqualToSomething() throws Exception {
        // Initialize the database
        companyRepository.saveAndFlush(company);

        // Get all the companyList where zipPostalCode not equals to DEFAULT_ZIP_POSTAL_CODE
        defaultCompanyShouldNotBeFound("zipPostalCode.notEquals=" + DEFAULT_ZIP_POSTAL_CODE);

        // Get all the companyList where zipPostalCode not equals to UPDATED_ZIP_POSTAL_CODE
        defaultCompanyShouldBeFound("zipPostalCode.notEquals=" + UPDATED_ZIP_POSTAL_CODE);
    }

    @Test
    @Transactional
    void getAllCompaniesByZipPostalCodeIsInShouldWork() throws Exception {
        // Initialize the database
        companyRepository.saveAndFlush(company);

        // Get all the companyList where zipPostalCode in DEFAULT_ZIP_POSTAL_CODE or UPDATED_ZIP_POSTAL_CODE
        defaultCompanyShouldBeFound("zipPostalCode.in=" + DEFAULT_ZIP_POSTAL_CODE + "," + UPDATED_ZIP_POSTAL_CODE);

        // Get all the companyList where zipPostalCode equals to UPDATED_ZIP_POSTAL_CODE
        defaultCompanyShouldNotBeFound("zipPostalCode.in=" + UPDATED_ZIP_POSTAL_CODE);
    }

    @Test
    @Transactional
    void getAllCompaniesByZipPostalCodeIsNullOrNotNull() throws Exception {
        // Initialize the database
        companyRepository.saveAndFlush(company);

        // Get all the companyList where zipPostalCode is not null
        defaultCompanyShouldBeFound("zipPostalCode.specified=true");

        // Get all the companyList where zipPostalCode is null
        defaultCompanyShouldNotBeFound("zipPostalCode.specified=false");
    }

    @Test
    @Transactional
    void getAllCompaniesByZipPostalCodeContainsSomething() throws Exception {
        // Initialize the database
        companyRepository.saveAndFlush(company);

        // Get all the companyList where zipPostalCode contains DEFAULT_ZIP_POSTAL_CODE
        defaultCompanyShouldBeFound("zipPostalCode.contains=" + DEFAULT_ZIP_POSTAL_CODE);

        // Get all the companyList where zipPostalCode contains UPDATED_ZIP_POSTAL_CODE
        defaultCompanyShouldNotBeFound("zipPostalCode.contains=" + UPDATED_ZIP_POSTAL_CODE);
    }

    @Test
    @Transactional
    void getAllCompaniesByZipPostalCodeNotContainsSomething() throws Exception {
        // Initialize the database
        companyRepository.saveAndFlush(company);

        // Get all the companyList where zipPostalCode does not contain DEFAULT_ZIP_POSTAL_CODE
        defaultCompanyShouldNotBeFound("zipPostalCode.doesNotContain=" + DEFAULT_ZIP_POSTAL_CODE);

        // Get all the companyList where zipPostalCode does not contain UPDATED_ZIP_POSTAL_CODE
        defaultCompanyShouldBeFound("zipPostalCode.doesNotContain=" + UPDATED_ZIP_POSTAL_CODE);
    }

    @Test
    @Transactional
    void getAllCompaniesByCountryRegionIsEqualToSomething() throws Exception {
        // Initialize the database
        companyRepository.saveAndFlush(company);

        // Get all the companyList where countryRegion equals to DEFAULT_COUNTRY_REGION
        defaultCompanyShouldBeFound("countryRegion.equals=" + DEFAULT_COUNTRY_REGION);

        // Get all the companyList where countryRegion equals to UPDATED_COUNTRY_REGION
        defaultCompanyShouldNotBeFound("countryRegion.equals=" + UPDATED_COUNTRY_REGION);
    }

    @Test
    @Transactional
    void getAllCompaniesByCountryRegionIsNotEqualToSomething() throws Exception {
        // Initialize the database
        companyRepository.saveAndFlush(company);

        // Get all the companyList where countryRegion not equals to DEFAULT_COUNTRY_REGION
        defaultCompanyShouldNotBeFound("countryRegion.notEquals=" + DEFAULT_COUNTRY_REGION);

        // Get all the companyList where countryRegion not equals to UPDATED_COUNTRY_REGION
        defaultCompanyShouldBeFound("countryRegion.notEquals=" + UPDATED_COUNTRY_REGION);
    }

    @Test
    @Transactional
    void getAllCompaniesByCountryRegionIsInShouldWork() throws Exception {
        // Initialize the database
        companyRepository.saveAndFlush(company);

        // Get all the companyList where countryRegion in DEFAULT_COUNTRY_REGION or UPDATED_COUNTRY_REGION
        defaultCompanyShouldBeFound("countryRegion.in=" + DEFAULT_COUNTRY_REGION + "," + UPDATED_COUNTRY_REGION);

        // Get all the companyList where countryRegion equals to UPDATED_COUNTRY_REGION
        defaultCompanyShouldNotBeFound("countryRegion.in=" + UPDATED_COUNTRY_REGION);
    }

    @Test
    @Transactional
    void getAllCompaniesByCountryRegionIsNullOrNotNull() throws Exception {
        // Initialize the database
        companyRepository.saveAndFlush(company);

        // Get all the companyList where countryRegion is not null
        defaultCompanyShouldBeFound("countryRegion.specified=true");

        // Get all the companyList where countryRegion is null
        defaultCompanyShouldNotBeFound("countryRegion.specified=false");
    }

    @Test
    @Transactional
    void getAllCompaniesByCountryRegionContainsSomething() throws Exception {
        // Initialize the database
        companyRepository.saveAndFlush(company);

        // Get all the companyList where countryRegion contains DEFAULT_COUNTRY_REGION
        defaultCompanyShouldBeFound("countryRegion.contains=" + DEFAULT_COUNTRY_REGION);

        // Get all the companyList where countryRegion contains UPDATED_COUNTRY_REGION
        defaultCompanyShouldNotBeFound("countryRegion.contains=" + UPDATED_COUNTRY_REGION);
    }

    @Test
    @Transactional
    void getAllCompaniesByCountryRegionNotContainsSomething() throws Exception {
        // Initialize the database
        companyRepository.saveAndFlush(company);

        // Get all the companyList where countryRegion does not contain DEFAULT_COUNTRY_REGION
        defaultCompanyShouldNotBeFound("countryRegion.doesNotContain=" + DEFAULT_COUNTRY_REGION);

        // Get all the companyList where countryRegion does not contain UPDATED_COUNTRY_REGION
        defaultCompanyShouldBeFound("countryRegion.doesNotContain=" + UPDATED_COUNTRY_REGION);
    }

    /**
     * Executes the search, and checks that the default entity is returned.
     */
    private void defaultCompanyShouldBeFound(String filter) throws Exception {
        restCompanyMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(company.getId().intValue())))
            .andExpect(jsonPath("$.[*].company").value(hasItem(DEFAULT_COMPANY)))
            .andExpect(jsonPath("$.[*].lastName").value(hasItem(DEFAULT_LAST_NAME)))
            .andExpect(jsonPath("$.[*].firstName").value(hasItem(DEFAULT_FIRST_NAME)))
            .andExpect(jsonPath("$.[*].emailAddress").value(hasItem(DEFAULT_EMAIL_ADDRESS)))
            .andExpect(jsonPath("$.[*].jobTitle").value(hasItem(DEFAULT_JOB_TITLE)))
            .andExpect(jsonPath("$.[*].businessPhone").value(hasItem(DEFAULT_BUSINESS_PHONE)))
            .andExpect(jsonPath("$.[*].homePhone").value(hasItem(DEFAULT_HOME_PHONE)))
            .andExpect(jsonPath("$.[*].mobilePhone").value(hasItem(DEFAULT_MOBILE_PHONE)))
            .andExpect(jsonPath("$.[*].faxNumber").value(hasItem(DEFAULT_FAX_NUMBER)))
            .andExpect(jsonPath("$.[*].address").value(hasItem(DEFAULT_ADDRESS.toString())))
            .andExpect(jsonPath("$.[*].city").value(hasItem(DEFAULT_CITY)))
            .andExpect(jsonPath("$.[*].stateProvince").value(hasItem(DEFAULT_STATE_PROVINCE)))
            .andExpect(jsonPath("$.[*].zipPostalCode").value(hasItem(DEFAULT_ZIP_POSTAL_CODE)))
            .andExpect(jsonPath("$.[*].countryRegion").value(hasItem(DEFAULT_COUNTRY_REGION)))
            .andExpect(jsonPath("$.[*].webPage").value(hasItem(DEFAULT_WEB_PAGE.toString())))
            .andExpect(jsonPath("$.[*].notes").value(hasItem(DEFAULT_NOTES.toString())))
            .andExpect(jsonPath("$.[*].attachmentsContentType").value(hasItem(DEFAULT_ATTACHMENTS_CONTENT_TYPE)))
            .andExpect(jsonPath("$.[*].attachments").value(hasItem(Base64Utils.encodeToString(DEFAULT_ATTACHMENTS))));

        // Check, that the count call also returns 1
        restCompanyMockMvc
            .perform(get(ENTITY_API_URL + "/count?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(content().string("1"));
    }

    /**
     * Executes the search, and checks that the default entity is not returned.
     */
    private void defaultCompanyShouldNotBeFound(String filter) throws Exception {
        restCompanyMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$").isArray())
            .andExpect(jsonPath("$").isEmpty());

        // Check, that the count call also returns 0
        restCompanyMockMvc
            .perform(get(ENTITY_API_URL + "/count?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(content().string("0"));
    }

    @Test
    @Transactional
    void getNonExistingCompany() throws Exception {
        // Get the company
        restCompanyMockMvc.perform(get(ENTITY_API_URL_ID, Long.MAX_VALUE)).andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    void putNewCompany() throws Exception {
        // Initialize the database
        companyRepository.saveAndFlush(company);

        int databaseSizeBeforeUpdate = companyRepository.findAll().size();

        // Update the company
        Company updatedCompany = companyRepository.findById(company.getId()).get();
        // Disconnect from session so that the updates on updatedCompany are not directly saved in db
        em.detach(updatedCompany);
        updatedCompany
            .company(UPDATED_COMPANY)
            .lastName(UPDATED_LAST_NAME)
            .firstName(UPDATED_FIRST_NAME)
            .emailAddress(UPDATED_EMAIL_ADDRESS)
            .jobTitle(UPDATED_JOB_TITLE)
            .businessPhone(UPDATED_BUSINESS_PHONE)
            .homePhone(UPDATED_HOME_PHONE)
            .mobilePhone(UPDATED_MOBILE_PHONE)
            .faxNumber(UPDATED_FAX_NUMBER)
            .address(UPDATED_ADDRESS)
            .city(UPDATED_CITY)
            .stateProvince(UPDATED_STATE_PROVINCE)
            .zipPostalCode(UPDATED_ZIP_POSTAL_CODE)
            .countryRegion(UPDATED_COUNTRY_REGION)
            .webPage(UPDATED_WEB_PAGE)
            .notes(UPDATED_NOTES)
            .attachments(UPDATED_ATTACHMENTS)
            .attachmentsContentType(UPDATED_ATTACHMENTS_CONTENT_TYPE);

        restCompanyMockMvc
            .perform(
                put(ENTITY_API_URL_ID, updatedCompany.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(updatedCompany))
            )
            .andExpect(status().isOk());

        // Validate the Company in the database
        List<Company> companyList = companyRepository.findAll();
        assertThat(companyList).hasSize(databaseSizeBeforeUpdate);
        Company testCompany = companyList.get(companyList.size() - 1);
        assertThat(testCompany.getCompany()).isEqualTo(UPDATED_COMPANY);
        assertThat(testCompany.getLastName()).isEqualTo(UPDATED_LAST_NAME);
        assertThat(testCompany.getFirstName()).isEqualTo(UPDATED_FIRST_NAME);
        assertThat(testCompany.getEmailAddress()).isEqualTo(UPDATED_EMAIL_ADDRESS);
        assertThat(testCompany.getJobTitle()).isEqualTo(UPDATED_JOB_TITLE);
        assertThat(testCompany.getBusinessPhone()).isEqualTo(UPDATED_BUSINESS_PHONE);
        assertThat(testCompany.getHomePhone()).isEqualTo(UPDATED_HOME_PHONE);
        assertThat(testCompany.getMobilePhone()).isEqualTo(UPDATED_MOBILE_PHONE);
        assertThat(testCompany.getFaxNumber()).isEqualTo(UPDATED_FAX_NUMBER);
        assertThat(testCompany.getAddress()).isEqualTo(UPDATED_ADDRESS);
        assertThat(testCompany.getCity()).isEqualTo(UPDATED_CITY);
        assertThat(testCompany.getStateProvince()).isEqualTo(UPDATED_STATE_PROVINCE);
        assertThat(testCompany.getZipPostalCode()).isEqualTo(UPDATED_ZIP_POSTAL_CODE);
        assertThat(testCompany.getCountryRegion()).isEqualTo(UPDATED_COUNTRY_REGION);
        assertThat(testCompany.getWebPage()).isEqualTo(UPDATED_WEB_PAGE);
        assertThat(testCompany.getNotes()).isEqualTo(UPDATED_NOTES);
        assertThat(testCompany.getAttachments()).isEqualTo(UPDATED_ATTACHMENTS);
        assertThat(testCompany.getAttachmentsContentType()).isEqualTo(UPDATED_ATTACHMENTS_CONTENT_TYPE);
    }

    @Test
    @Transactional
    void putNonExistingCompany() throws Exception {
        int databaseSizeBeforeUpdate = companyRepository.findAll().size();
        company.setId(count.incrementAndGet());

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restCompanyMockMvc
            .perform(
                put(ENTITY_API_URL_ID, company.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(company))
            )
            .andExpect(status().isBadRequest());

        // Validate the Company in the database
        List<Company> companyList = companyRepository.findAll();
        assertThat(companyList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithIdMismatchCompany() throws Exception {
        int databaseSizeBeforeUpdate = companyRepository.findAll().size();
        company.setId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restCompanyMockMvc
            .perform(
                put(ENTITY_API_URL_ID, count.incrementAndGet())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(company))
            )
            .andExpect(status().isBadRequest());

        // Validate the Company in the database
        List<Company> companyList = companyRepository.findAll();
        assertThat(companyList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithMissingIdPathParamCompany() throws Exception {
        int databaseSizeBeforeUpdate = companyRepository.findAll().size();
        company.setId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restCompanyMockMvc
            .perform(put(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(company)))
            .andExpect(status().isMethodNotAllowed());

        // Validate the Company in the database
        List<Company> companyList = companyRepository.findAll();
        assertThat(companyList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void partialUpdateCompanyWithPatch() throws Exception {
        // Initialize the database
        companyRepository.saveAndFlush(company);

        int databaseSizeBeforeUpdate = companyRepository.findAll().size();

        // Update the company using partial update
        Company partialUpdatedCompany = new Company();
        partialUpdatedCompany.setId(company.getId());

        partialUpdatedCompany
            .lastName(UPDATED_LAST_NAME)
            .firstName(UPDATED_FIRST_NAME)
            .jobTitle(UPDATED_JOB_TITLE)
            .homePhone(UPDATED_HOME_PHONE)
            .address(UPDATED_ADDRESS)
            .city(UPDATED_CITY)
            .zipPostalCode(UPDATED_ZIP_POSTAL_CODE)
            .webPage(UPDATED_WEB_PAGE)
            .attachments(UPDATED_ATTACHMENTS)
            .attachmentsContentType(UPDATED_ATTACHMENTS_CONTENT_TYPE);

        restCompanyMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedCompany.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedCompany))
            )
            .andExpect(status().isOk());

        // Validate the Company in the database
        List<Company> companyList = companyRepository.findAll();
        assertThat(companyList).hasSize(databaseSizeBeforeUpdate);
        Company testCompany = companyList.get(companyList.size() - 1);
        assertThat(testCompany.getCompany()).isEqualTo(DEFAULT_COMPANY);
        assertThat(testCompany.getLastName()).isEqualTo(UPDATED_LAST_NAME);
        assertThat(testCompany.getFirstName()).isEqualTo(UPDATED_FIRST_NAME);
        assertThat(testCompany.getEmailAddress()).isEqualTo(DEFAULT_EMAIL_ADDRESS);
        assertThat(testCompany.getJobTitle()).isEqualTo(UPDATED_JOB_TITLE);
        assertThat(testCompany.getBusinessPhone()).isEqualTo(DEFAULT_BUSINESS_PHONE);
        assertThat(testCompany.getHomePhone()).isEqualTo(UPDATED_HOME_PHONE);
        assertThat(testCompany.getMobilePhone()).isEqualTo(DEFAULT_MOBILE_PHONE);
        assertThat(testCompany.getFaxNumber()).isEqualTo(DEFAULT_FAX_NUMBER);
        assertThat(testCompany.getAddress()).isEqualTo(UPDATED_ADDRESS);
        assertThat(testCompany.getCity()).isEqualTo(UPDATED_CITY);
        assertThat(testCompany.getStateProvince()).isEqualTo(DEFAULT_STATE_PROVINCE);
        assertThat(testCompany.getZipPostalCode()).isEqualTo(UPDATED_ZIP_POSTAL_CODE);
        assertThat(testCompany.getCountryRegion()).isEqualTo(DEFAULT_COUNTRY_REGION);
        assertThat(testCompany.getWebPage()).isEqualTo(UPDATED_WEB_PAGE);
        assertThat(testCompany.getNotes()).isEqualTo(DEFAULT_NOTES);
        assertThat(testCompany.getAttachments()).isEqualTo(UPDATED_ATTACHMENTS);
        assertThat(testCompany.getAttachmentsContentType()).isEqualTo(UPDATED_ATTACHMENTS_CONTENT_TYPE);
    }

    @Test
    @Transactional
    void fullUpdateCompanyWithPatch() throws Exception {
        // Initialize the database
        companyRepository.saveAndFlush(company);

        int databaseSizeBeforeUpdate = companyRepository.findAll().size();

        // Update the company using partial update
        Company partialUpdatedCompany = new Company();
        partialUpdatedCompany.setId(company.getId());

        partialUpdatedCompany
            .company(UPDATED_COMPANY)
            .lastName(UPDATED_LAST_NAME)
            .firstName(UPDATED_FIRST_NAME)
            .emailAddress(UPDATED_EMAIL_ADDRESS)
            .jobTitle(UPDATED_JOB_TITLE)
            .businessPhone(UPDATED_BUSINESS_PHONE)
            .homePhone(UPDATED_HOME_PHONE)
            .mobilePhone(UPDATED_MOBILE_PHONE)
            .faxNumber(UPDATED_FAX_NUMBER)
            .address(UPDATED_ADDRESS)
            .city(UPDATED_CITY)
            .stateProvince(UPDATED_STATE_PROVINCE)
            .zipPostalCode(UPDATED_ZIP_POSTAL_CODE)
            .countryRegion(UPDATED_COUNTRY_REGION)
            .webPage(UPDATED_WEB_PAGE)
            .notes(UPDATED_NOTES)
            .attachments(UPDATED_ATTACHMENTS)
            .attachmentsContentType(UPDATED_ATTACHMENTS_CONTENT_TYPE);

        restCompanyMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedCompany.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedCompany))
            )
            .andExpect(status().isOk());

        // Validate the Company in the database
        List<Company> companyList = companyRepository.findAll();
        assertThat(companyList).hasSize(databaseSizeBeforeUpdate);
        Company testCompany = companyList.get(companyList.size() - 1);
        assertThat(testCompany.getCompany()).isEqualTo(UPDATED_COMPANY);
        assertThat(testCompany.getLastName()).isEqualTo(UPDATED_LAST_NAME);
        assertThat(testCompany.getFirstName()).isEqualTo(UPDATED_FIRST_NAME);
        assertThat(testCompany.getEmailAddress()).isEqualTo(UPDATED_EMAIL_ADDRESS);
        assertThat(testCompany.getJobTitle()).isEqualTo(UPDATED_JOB_TITLE);
        assertThat(testCompany.getBusinessPhone()).isEqualTo(UPDATED_BUSINESS_PHONE);
        assertThat(testCompany.getHomePhone()).isEqualTo(UPDATED_HOME_PHONE);
        assertThat(testCompany.getMobilePhone()).isEqualTo(UPDATED_MOBILE_PHONE);
        assertThat(testCompany.getFaxNumber()).isEqualTo(UPDATED_FAX_NUMBER);
        assertThat(testCompany.getAddress()).isEqualTo(UPDATED_ADDRESS);
        assertThat(testCompany.getCity()).isEqualTo(UPDATED_CITY);
        assertThat(testCompany.getStateProvince()).isEqualTo(UPDATED_STATE_PROVINCE);
        assertThat(testCompany.getZipPostalCode()).isEqualTo(UPDATED_ZIP_POSTAL_CODE);
        assertThat(testCompany.getCountryRegion()).isEqualTo(UPDATED_COUNTRY_REGION);
        assertThat(testCompany.getWebPage()).isEqualTo(UPDATED_WEB_PAGE);
        assertThat(testCompany.getNotes()).isEqualTo(UPDATED_NOTES);
        assertThat(testCompany.getAttachments()).isEqualTo(UPDATED_ATTACHMENTS);
        assertThat(testCompany.getAttachmentsContentType()).isEqualTo(UPDATED_ATTACHMENTS_CONTENT_TYPE);
    }

    @Test
    @Transactional
    void patchNonExistingCompany() throws Exception {
        int databaseSizeBeforeUpdate = companyRepository.findAll().size();
        company.setId(count.incrementAndGet());

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restCompanyMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, company.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(company))
            )
            .andExpect(status().isBadRequest());

        // Validate the Company in the database
        List<Company> companyList = companyRepository.findAll();
        assertThat(companyList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithIdMismatchCompany() throws Exception {
        int databaseSizeBeforeUpdate = companyRepository.findAll().size();
        company.setId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restCompanyMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, count.incrementAndGet())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(company))
            )
            .andExpect(status().isBadRequest());

        // Validate the Company in the database
        List<Company> companyList = companyRepository.findAll();
        assertThat(companyList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithMissingIdPathParamCompany() throws Exception {
        int databaseSizeBeforeUpdate = companyRepository.findAll().size();
        company.setId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restCompanyMockMvc
            .perform(patch(ENTITY_API_URL).contentType("application/merge-patch+json").content(TestUtil.convertObjectToJsonBytes(company)))
            .andExpect(status().isMethodNotAllowed());

        // Validate the Company in the database
        List<Company> companyList = companyRepository.findAll();
        assertThat(companyList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void deleteCompany() throws Exception {
        // Initialize the database
        companyRepository.saveAndFlush(company);

        int databaseSizeBeforeDelete = companyRepository.findAll().size();

        // Delete the company
        restCompanyMockMvc
            .perform(delete(ENTITY_API_URL_ID, company.getId()).accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        List<Company> companyList = companyRepository.findAll();
        assertThat(companyList).hasSize(databaseSizeBeforeDelete - 1);
    }
}
