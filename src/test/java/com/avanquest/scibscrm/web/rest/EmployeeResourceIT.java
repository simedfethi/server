package com.avanquest.scibscrm.web.rest;

import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import com.avanquest.scibscrm.IntegrationTest;
import com.avanquest.scibscrm.domain.Activite;
import com.avanquest.scibscrm.domain.Departement;
import com.avanquest.scibscrm.domain.Employee;
import com.avanquest.scibscrm.domain.User;
import com.avanquest.scibscrm.domain.enumeration.Gender;
import com.avanquest.scibscrm.repository.EmployeeRepository;
import com.avanquest.scibscrm.service.EmployeeService;
import com.avanquest.scibscrm.service.criteria.EmployeeCriteria;
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
 * Integration tests for the {@link EmployeeResource} REST controller.
 */
@IntegrationTest
@ExtendWith(MockitoExtension.class)
@AutoConfigureMockMvc
@WithMockUser
class EmployeeResourceIT {

    private static final String DEFAULT_EMPLOYEE_NAME = "AAAAAAAAAA";
    private static final String UPDATED_EMPLOYEE_NAME = "BBBBBBBBBB";

    private static final Gender DEFAULT_GENDER = Gender.MALE;
    private static final Gender UPDATED_GENDER = Gender.FEMALE;

    private static final String DEFAULT_PHONE = "AAAAAAAAAA";
    private static final String UPDATED_PHONE = "BBBBBBBBBB";

    private static final String DEFAULT_ADDRESS_LINE_1 = "AAAAAAAAAA";
    private static final String UPDATED_ADDRESS_LINE_1 = "BBBBBBBBBB";

    private static final String ENTITY_API_URL = "/api/employees";
    private static final String ENTITY_API_URL_ID = ENTITY_API_URL + "/{id}";

    private static Random random = new Random();
    private static AtomicLong count = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    @Autowired
    private EmployeeRepository employeeRepository;

    @Mock
    private EmployeeRepository employeeRepositoryMock;

    @Mock
    private EmployeeService employeeServiceMock;

    @Autowired
    private EntityManager em;

    @Autowired
    private MockMvc restEmployeeMockMvc;

    private Employee employee;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Employee createEntity(EntityManager em) {
        Employee employee = new Employee()
            .employeeName(DEFAULT_EMPLOYEE_NAME)
            .gender(DEFAULT_GENDER)
            .phone(DEFAULT_PHONE)
            .addressLine1(DEFAULT_ADDRESS_LINE_1);
        // Add required entity
        User user = UserResourceIT.createEntity(em);
        em.persist(user);
        em.flush();
        employee.setUtilisateur(user);
        return employee;
    }

    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Employee createUpdatedEntity(EntityManager em) {
        Employee employee = new Employee()
            .employeeName(UPDATED_EMPLOYEE_NAME)
            .gender(UPDATED_GENDER)
            .phone(UPDATED_PHONE)
            .addressLine1(UPDATED_ADDRESS_LINE_1);
        // Add required entity
        User user = UserResourceIT.createEntity(em);
        em.persist(user);
        em.flush();
        employee.setUtilisateur(user);
        return employee;
    }

    @BeforeEach
    public void initTest() {
        employee = createEntity(em);
    }

    @Test
    @Transactional
    void createEmployee() throws Exception {
        int databaseSizeBeforeCreate = employeeRepository.findAll().size();
        // Create the Employee
        restEmployeeMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(employee)))
            .andExpect(status().isCreated());

        // Validate the Employee in the database
        List<Employee> employeeList = employeeRepository.findAll();
        assertThat(employeeList).hasSize(databaseSizeBeforeCreate + 1);
        Employee testEmployee = employeeList.get(employeeList.size() - 1);
        assertThat(testEmployee.getEmployeeName()).isEqualTo(DEFAULT_EMPLOYEE_NAME);
        assertThat(testEmployee.getGender()).isEqualTo(DEFAULT_GENDER);
        assertThat(testEmployee.getPhone()).isEqualTo(DEFAULT_PHONE);
        assertThat(testEmployee.getAddressLine1()).isEqualTo(DEFAULT_ADDRESS_LINE_1);
    }

    @Test
    @Transactional
    void createEmployeeWithExistingId() throws Exception {
        // Create the Employee with an existing ID
        employee.setId(1L);

        int databaseSizeBeforeCreate = employeeRepository.findAll().size();

        // An entity with an existing ID cannot be created, so this API call must fail
        restEmployeeMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(employee)))
            .andExpect(status().isBadRequest());

        // Validate the Employee in the database
        List<Employee> employeeList = employeeRepository.findAll();
        assertThat(employeeList).hasSize(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    void checkGenderIsRequired() throws Exception {
        int databaseSizeBeforeTest = employeeRepository.findAll().size();
        // set the field null
        employee.setGender(null);

        // Create the Employee, which fails.

        restEmployeeMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(employee)))
            .andExpect(status().isBadRequest());

        List<Employee> employeeList = employeeRepository.findAll();
        assertThat(employeeList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void checkPhoneIsRequired() throws Exception {
        int databaseSizeBeforeTest = employeeRepository.findAll().size();
        // set the field null
        employee.setPhone(null);

        // Create the Employee, which fails.

        restEmployeeMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(employee)))
            .andExpect(status().isBadRequest());

        List<Employee> employeeList = employeeRepository.findAll();
        assertThat(employeeList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void getAllEmployees() throws Exception {
        // Initialize the database
        employeeRepository.saveAndFlush(employee);

        // Get all the employeeList
        restEmployeeMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(employee.getId().intValue())))
            .andExpect(jsonPath("$.[*].employeeName").value(hasItem(DEFAULT_EMPLOYEE_NAME)))
            .andExpect(jsonPath("$.[*].gender").value(hasItem(DEFAULT_GENDER.toString())))
            .andExpect(jsonPath("$.[*].phone").value(hasItem(DEFAULT_PHONE)))
            .andExpect(jsonPath("$.[*].addressLine1").value(hasItem(DEFAULT_ADDRESS_LINE_1)));
    }

    @SuppressWarnings({ "unchecked" })
    void getAllEmployeesWithEagerRelationshipsIsEnabled() throws Exception {
        when(employeeServiceMock.findAllWithEagerRelationships(any())).thenReturn(new PageImpl(new ArrayList<>()));

        restEmployeeMockMvc.perform(get(ENTITY_API_URL + "?eagerload=true")).andExpect(status().isOk());

        verify(employeeServiceMock, times(1)).findAllWithEagerRelationships(any());
    }

    @SuppressWarnings({ "unchecked" })
    void getAllEmployeesWithEagerRelationshipsIsNotEnabled() throws Exception {
        when(employeeServiceMock.findAllWithEagerRelationships(any())).thenReturn(new PageImpl(new ArrayList<>()));

        restEmployeeMockMvc.perform(get(ENTITY_API_URL + "?eagerload=true")).andExpect(status().isOk());

        verify(employeeServiceMock, times(1)).findAllWithEagerRelationships(any());
    }

    @Test
    @Transactional
    void getEmployee() throws Exception {
        // Initialize the database
        employeeRepository.saveAndFlush(employee);

        // Get the employee
        restEmployeeMockMvc
            .perform(get(ENTITY_API_URL_ID, employee.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.id").value(employee.getId().intValue()))
            .andExpect(jsonPath("$.employeeName").value(DEFAULT_EMPLOYEE_NAME))
            .andExpect(jsonPath("$.gender").value(DEFAULT_GENDER.toString()))
            .andExpect(jsonPath("$.phone").value(DEFAULT_PHONE))
            .andExpect(jsonPath("$.addressLine1").value(DEFAULT_ADDRESS_LINE_1));
    }

    @Test
    @Transactional
    void getEmployeesByIdFiltering() throws Exception {
        // Initialize the database
        employeeRepository.saveAndFlush(employee);

        Long id = employee.getId();

        defaultEmployeeShouldBeFound("id.equals=" + id);
        defaultEmployeeShouldNotBeFound("id.notEquals=" + id);

        defaultEmployeeShouldBeFound("id.greaterThanOrEqual=" + id);
        defaultEmployeeShouldNotBeFound("id.greaterThan=" + id);

        defaultEmployeeShouldBeFound("id.lessThanOrEqual=" + id);
        defaultEmployeeShouldNotBeFound("id.lessThan=" + id);
    }

    @Test
    @Transactional
    void getAllEmployeesByEmployeeNameIsEqualToSomething() throws Exception {
        // Initialize the database
        employeeRepository.saveAndFlush(employee);

        // Get all the employeeList where employeeName equals to DEFAULT_EMPLOYEE_NAME
        defaultEmployeeShouldBeFound("employeeName.equals=" + DEFAULT_EMPLOYEE_NAME);

        // Get all the employeeList where employeeName equals to UPDATED_EMPLOYEE_NAME
        defaultEmployeeShouldNotBeFound("employeeName.equals=" + UPDATED_EMPLOYEE_NAME);
    }

    @Test
    @Transactional
    void getAllEmployeesByEmployeeNameIsNotEqualToSomething() throws Exception {
        // Initialize the database
        employeeRepository.saveAndFlush(employee);

        // Get all the employeeList where employeeName not equals to DEFAULT_EMPLOYEE_NAME
        defaultEmployeeShouldNotBeFound("employeeName.notEquals=" + DEFAULT_EMPLOYEE_NAME);

        // Get all the employeeList where employeeName not equals to UPDATED_EMPLOYEE_NAME
        defaultEmployeeShouldBeFound("employeeName.notEquals=" + UPDATED_EMPLOYEE_NAME);
    }

    @Test
    @Transactional
    void getAllEmployeesByEmployeeNameIsInShouldWork() throws Exception {
        // Initialize the database
        employeeRepository.saveAndFlush(employee);

        // Get all the employeeList where employeeName in DEFAULT_EMPLOYEE_NAME or UPDATED_EMPLOYEE_NAME
        defaultEmployeeShouldBeFound("employeeName.in=" + DEFAULT_EMPLOYEE_NAME + "," + UPDATED_EMPLOYEE_NAME);

        // Get all the employeeList where employeeName equals to UPDATED_EMPLOYEE_NAME
        defaultEmployeeShouldNotBeFound("employeeName.in=" + UPDATED_EMPLOYEE_NAME);
    }

    @Test
    @Transactional
    void getAllEmployeesByEmployeeNameIsNullOrNotNull() throws Exception {
        // Initialize the database
        employeeRepository.saveAndFlush(employee);

        // Get all the employeeList where employeeName is not null
        defaultEmployeeShouldBeFound("employeeName.specified=true");

        // Get all the employeeList where employeeName is null
        defaultEmployeeShouldNotBeFound("employeeName.specified=false");
    }

    @Test
    @Transactional
    void getAllEmployeesByEmployeeNameContainsSomething() throws Exception {
        // Initialize the database
        employeeRepository.saveAndFlush(employee);

        // Get all the employeeList where employeeName contains DEFAULT_EMPLOYEE_NAME
        defaultEmployeeShouldBeFound("employeeName.contains=" + DEFAULT_EMPLOYEE_NAME);

        // Get all the employeeList where employeeName contains UPDATED_EMPLOYEE_NAME
        defaultEmployeeShouldNotBeFound("employeeName.contains=" + UPDATED_EMPLOYEE_NAME);
    }

    @Test
    @Transactional
    void getAllEmployeesByEmployeeNameNotContainsSomething() throws Exception {
        // Initialize the database
        employeeRepository.saveAndFlush(employee);

        // Get all the employeeList where employeeName does not contain DEFAULT_EMPLOYEE_NAME
        defaultEmployeeShouldNotBeFound("employeeName.doesNotContain=" + DEFAULT_EMPLOYEE_NAME);

        // Get all the employeeList where employeeName does not contain UPDATED_EMPLOYEE_NAME
        defaultEmployeeShouldBeFound("employeeName.doesNotContain=" + UPDATED_EMPLOYEE_NAME);
    }

    @Test
    @Transactional
    void getAllEmployeesByGenderIsEqualToSomething() throws Exception {
        // Initialize the database
        employeeRepository.saveAndFlush(employee);

        // Get all the employeeList where gender equals to DEFAULT_GENDER
        defaultEmployeeShouldBeFound("gender.equals=" + DEFAULT_GENDER);

        // Get all the employeeList where gender equals to UPDATED_GENDER
        defaultEmployeeShouldNotBeFound("gender.equals=" + UPDATED_GENDER);
    }

    @Test
    @Transactional
    void getAllEmployeesByGenderIsNotEqualToSomething() throws Exception {
        // Initialize the database
        employeeRepository.saveAndFlush(employee);

        // Get all the employeeList where gender not equals to DEFAULT_GENDER
        defaultEmployeeShouldNotBeFound("gender.notEquals=" + DEFAULT_GENDER);

        // Get all the employeeList where gender not equals to UPDATED_GENDER
        defaultEmployeeShouldBeFound("gender.notEquals=" + UPDATED_GENDER);
    }

    @Test
    @Transactional
    void getAllEmployeesByGenderIsInShouldWork() throws Exception {
        // Initialize the database
        employeeRepository.saveAndFlush(employee);

        // Get all the employeeList where gender in DEFAULT_GENDER or UPDATED_GENDER
        defaultEmployeeShouldBeFound("gender.in=" + DEFAULT_GENDER + "," + UPDATED_GENDER);

        // Get all the employeeList where gender equals to UPDATED_GENDER
        defaultEmployeeShouldNotBeFound("gender.in=" + UPDATED_GENDER);
    }

    @Test
    @Transactional
    void getAllEmployeesByGenderIsNullOrNotNull() throws Exception {
        // Initialize the database
        employeeRepository.saveAndFlush(employee);

        // Get all the employeeList where gender is not null
        defaultEmployeeShouldBeFound("gender.specified=true");

        // Get all the employeeList where gender is null
        defaultEmployeeShouldNotBeFound("gender.specified=false");
    }

    @Test
    @Transactional
    void getAllEmployeesByPhoneIsEqualToSomething() throws Exception {
        // Initialize the database
        employeeRepository.saveAndFlush(employee);

        // Get all the employeeList where phone equals to DEFAULT_PHONE
        defaultEmployeeShouldBeFound("phone.equals=" + DEFAULT_PHONE);

        // Get all the employeeList where phone equals to UPDATED_PHONE
        defaultEmployeeShouldNotBeFound("phone.equals=" + UPDATED_PHONE);
    }

    @Test
    @Transactional
    void getAllEmployeesByPhoneIsNotEqualToSomething() throws Exception {
        // Initialize the database
        employeeRepository.saveAndFlush(employee);

        // Get all the employeeList where phone not equals to DEFAULT_PHONE
        defaultEmployeeShouldNotBeFound("phone.notEquals=" + DEFAULT_PHONE);

        // Get all the employeeList where phone not equals to UPDATED_PHONE
        defaultEmployeeShouldBeFound("phone.notEquals=" + UPDATED_PHONE);
    }

    @Test
    @Transactional
    void getAllEmployeesByPhoneIsInShouldWork() throws Exception {
        // Initialize the database
        employeeRepository.saveAndFlush(employee);

        // Get all the employeeList where phone in DEFAULT_PHONE or UPDATED_PHONE
        defaultEmployeeShouldBeFound("phone.in=" + DEFAULT_PHONE + "," + UPDATED_PHONE);

        // Get all the employeeList where phone equals to UPDATED_PHONE
        defaultEmployeeShouldNotBeFound("phone.in=" + UPDATED_PHONE);
    }

    @Test
    @Transactional
    void getAllEmployeesByPhoneIsNullOrNotNull() throws Exception {
        // Initialize the database
        employeeRepository.saveAndFlush(employee);

        // Get all the employeeList where phone is not null
        defaultEmployeeShouldBeFound("phone.specified=true");

        // Get all the employeeList where phone is null
        defaultEmployeeShouldNotBeFound("phone.specified=false");
    }

    @Test
    @Transactional
    void getAllEmployeesByPhoneContainsSomething() throws Exception {
        // Initialize the database
        employeeRepository.saveAndFlush(employee);

        // Get all the employeeList where phone contains DEFAULT_PHONE
        defaultEmployeeShouldBeFound("phone.contains=" + DEFAULT_PHONE);

        // Get all the employeeList where phone contains UPDATED_PHONE
        defaultEmployeeShouldNotBeFound("phone.contains=" + UPDATED_PHONE);
    }

    @Test
    @Transactional
    void getAllEmployeesByPhoneNotContainsSomething() throws Exception {
        // Initialize the database
        employeeRepository.saveAndFlush(employee);

        // Get all the employeeList where phone does not contain DEFAULT_PHONE
        defaultEmployeeShouldNotBeFound("phone.doesNotContain=" + DEFAULT_PHONE);

        // Get all the employeeList where phone does not contain UPDATED_PHONE
        defaultEmployeeShouldBeFound("phone.doesNotContain=" + UPDATED_PHONE);
    }

    @Test
    @Transactional
    void getAllEmployeesByAddressLine1IsEqualToSomething() throws Exception {
        // Initialize the database
        employeeRepository.saveAndFlush(employee);

        // Get all the employeeList where addressLine1 equals to DEFAULT_ADDRESS_LINE_1
        defaultEmployeeShouldBeFound("addressLine1.equals=" + DEFAULT_ADDRESS_LINE_1);

        // Get all the employeeList where addressLine1 equals to UPDATED_ADDRESS_LINE_1
        defaultEmployeeShouldNotBeFound("addressLine1.equals=" + UPDATED_ADDRESS_LINE_1);
    }

    @Test
    @Transactional
    void getAllEmployeesByAddressLine1IsNotEqualToSomething() throws Exception {
        // Initialize the database
        employeeRepository.saveAndFlush(employee);

        // Get all the employeeList where addressLine1 not equals to DEFAULT_ADDRESS_LINE_1
        defaultEmployeeShouldNotBeFound("addressLine1.notEquals=" + DEFAULT_ADDRESS_LINE_1);

        // Get all the employeeList where addressLine1 not equals to UPDATED_ADDRESS_LINE_1
        defaultEmployeeShouldBeFound("addressLine1.notEquals=" + UPDATED_ADDRESS_LINE_1);
    }

    @Test
    @Transactional
    void getAllEmployeesByAddressLine1IsInShouldWork() throws Exception {
        // Initialize the database
        employeeRepository.saveAndFlush(employee);

        // Get all the employeeList where addressLine1 in DEFAULT_ADDRESS_LINE_1 or UPDATED_ADDRESS_LINE_1
        defaultEmployeeShouldBeFound("addressLine1.in=" + DEFAULT_ADDRESS_LINE_1 + "," + UPDATED_ADDRESS_LINE_1);

        // Get all the employeeList where addressLine1 equals to UPDATED_ADDRESS_LINE_1
        defaultEmployeeShouldNotBeFound("addressLine1.in=" + UPDATED_ADDRESS_LINE_1);
    }

    @Test
    @Transactional
    void getAllEmployeesByAddressLine1IsNullOrNotNull() throws Exception {
        // Initialize the database
        employeeRepository.saveAndFlush(employee);

        // Get all the employeeList where addressLine1 is not null
        defaultEmployeeShouldBeFound("addressLine1.specified=true");

        // Get all the employeeList where addressLine1 is null
        defaultEmployeeShouldNotBeFound("addressLine1.specified=false");
    }

    @Test
    @Transactional
    void getAllEmployeesByAddressLine1ContainsSomething() throws Exception {
        // Initialize the database
        employeeRepository.saveAndFlush(employee);

        // Get all the employeeList where addressLine1 contains DEFAULT_ADDRESS_LINE_1
        defaultEmployeeShouldBeFound("addressLine1.contains=" + DEFAULT_ADDRESS_LINE_1);

        // Get all the employeeList where addressLine1 contains UPDATED_ADDRESS_LINE_1
        defaultEmployeeShouldNotBeFound("addressLine1.contains=" + UPDATED_ADDRESS_LINE_1);
    }

    @Test
    @Transactional
    void getAllEmployeesByAddressLine1NotContainsSomething() throws Exception {
        // Initialize the database
        employeeRepository.saveAndFlush(employee);

        // Get all the employeeList where addressLine1 does not contain DEFAULT_ADDRESS_LINE_1
        defaultEmployeeShouldNotBeFound("addressLine1.doesNotContain=" + DEFAULT_ADDRESS_LINE_1);

        // Get all the employeeList where addressLine1 does not contain UPDATED_ADDRESS_LINE_1
        defaultEmployeeShouldBeFound("addressLine1.doesNotContain=" + UPDATED_ADDRESS_LINE_1);
    }

    @Test
    @Transactional
    void getAllEmployeesByUtilisateurIsEqualToSomething() throws Exception {
        // Get already existing entity
        User utilisateur = employee.getUtilisateur();
        employeeRepository.saveAndFlush(employee);
        Long utilisateurId = utilisateur.getId();

        // Get all the employeeList where utilisateur equals to utilisateurId
        defaultEmployeeShouldBeFound("utilisateurId.equals=" + utilisateurId);

        // Get all the employeeList where utilisateur equals to (utilisateurId + 1)
        defaultEmployeeShouldNotBeFound("utilisateurId.equals=" + (utilisateurId + 1));
    }

    @Test
    @Transactional
    void getAllEmployeesByDepartementIsEqualToSomething() throws Exception {
        // Initialize the database
        employeeRepository.saveAndFlush(employee);
        Departement departement;
        if (TestUtil.findAll(em, Departement.class).isEmpty()) {
            departement = DepartementResourceIT.createEntity(em);
            em.persist(departement);
            em.flush();
        } else {
            departement = TestUtil.findAll(em, Departement.class).get(0);
        }
        em.persist(departement);
        em.flush();
        employee.setDepartement(departement);
        employeeRepository.saveAndFlush(employee);
        Long departementId = departement.getId();

        // Get all the employeeList where departement equals to departementId
        defaultEmployeeShouldBeFound("departementId.equals=" + departementId);

        // Get all the employeeList where departement equals to (departementId + 1)
        defaultEmployeeShouldNotBeFound("departementId.equals=" + (departementId + 1));
    }

    @Test
    @Transactional
    void getAllEmployeesByActivityAssIsEqualToSomething() throws Exception {
        // Initialize the database
        employeeRepository.saveAndFlush(employee);
        Activite activityAss;
        if (TestUtil.findAll(em, Activite.class).isEmpty()) {
            activityAss = ActiviteResourceIT.createEntity(em);
            em.persist(activityAss);
            em.flush();
        } else {
            activityAss = TestUtil.findAll(em, Activite.class).get(0);
        }
        em.persist(activityAss);
        em.flush();
        employee.addActivityAss(activityAss);
        employeeRepository.saveAndFlush(employee);
        Long activityAssId = activityAss.getId();

        // Get all the employeeList where activityAss equals to activityAssId
        defaultEmployeeShouldBeFound("activityAssId.equals=" + activityAssId);

        // Get all the employeeList where activityAss equals to (activityAssId + 1)
        defaultEmployeeShouldNotBeFound("activityAssId.equals=" + (activityAssId + 1));
    }

    @Test
    @Transactional
    void getAllEmployeesByActivityEmpIsEqualToSomething() throws Exception {
        // Initialize the database
        employeeRepository.saveAndFlush(employee);
        Activite activityEmp;
        if (TestUtil.findAll(em, Activite.class).isEmpty()) {
            activityEmp = ActiviteResourceIT.createEntity(em);
            em.persist(activityEmp);
            em.flush();
        } else {
            activityEmp = TestUtil.findAll(em, Activite.class).get(0);
        }
        em.persist(activityEmp);
        em.flush();
        employee.addActivityEmp(activityEmp);
        employeeRepository.saveAndFlush(employee);
        Long activityEmpId = activityEmp.getId();

        // Get all the employeeList where activityEmp equals to activityEmpId
        defaultEmployeeShouldBeFound("activityEmpId.equals=" + activityEmpId);

        // Get all the employeeList where activityEmp equals to (activityEmpId + 1)
        defaultEmployeeShouldNotBeFound("activityEmpId.equals=" + (activityEmpId + 1));
    }

    /**
     * Executes the search, and checks that the default entity is returned.
     */
    private void defaultEmployeeShouldBeFound(String filter) throws Exception {
        restEmployeeMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(employee.getId().intValue())))
            .andExpect(jsonPath("$.[*].employeeName").value(hasItem(DEFAULT_EMPLOYEE_NAME)))
            .andExpect(jsonPath("$.[*].gender").value(hasItem(DEFAULT_GENDER.toString())))
            .andExpect(jsonPath("$.[*].phone").value(hasItem(DEFAULT_PHONE)))
            .andExpect(jsonPath("$.[*].addressLine1").value(hasItem(DEFAULT_ADDRESS_LINE_1)));

        // Check, that the count call also returns 1
        restEmployeeMockMvc
            .perform(get(ENTITY_API_URL + "/count?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(content().string("1"));
    }

    /**
     * Executes the search, and checks that the default entity is not returned.
     */
    private void defaultEmployeeShouldNotBeFound(String filter) throws Exception {
        restEmployeeMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$").isArray())
            .andExpect(jsonPath("$").isEmpty());

        // Check, that the count call also returns 0
        restEmployeeMockMvc
            .perform(get(ENTITY_API_URL + "/count?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(content().string("0"));
    }

    @Test
    @Transactional
    void getNonExistingEmployee() throws Exception {
        // Get the employee
        restEmployeeMockMvc.perform(get(ENTITY_API_URL_ID, Long.MAX_VALUE)).andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    void putNewEmployee() throws Exception {
        // Initialize the database
        employeeRepository.saveAndFlush(employee);

        int databaseSizeBeforeUpdate = employeeRepository.findAll().size();

        // Update the employee
        Employee updatedEmployee = employeeRepository.findById(employee.getId()).get();
        // Disconnect from session so that the updates on updatedEmployee are not directly saved in db
        em.detach(updatedEmployee);
        updatedEmployee
            .employeeName(UPDATED_EMPLOYEE_NAME)
            .gender(UPDATED_GENDER)
            .phone(UPDATED_PHONE)
            .addressLine1(UPDATED_ADDRESS_LINE_1);

        restEmployeeMockMvc
            .perform(
                put(ENTITY_API_URL_ID, updatedEmployee.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(updatedEmployee))
            )
            .andExpect(status().isOk());

        // Validate the Employee in the database
        List<Employee> employeeList = employeeRepository.findAll();
        assertThat(employeeList).hasSize(databaseSizeBeforeUpdate);
        Employee testEmployee = employeeList.get(employeeList.size() - 1);
        assertThat(testEmployee.getEmployeeName()).isEqualTo(UPDATED_EMPLOYEE_NAME);
        assertThat(testEmployee.getGender()).isEqualTo(UPDATED_GENDER);
        assertThat(testEmployee.getPhone()).isEqualTo(UPDATED_PHONE);
        assertThat(testEmployee.getAddressLine1()).isEqualTo(UPDATED_ADDRESS_LINE_1);
    }

    @Test
    @Transactional
    void putNonExistingEmployee() throws Exception {
        int databaseSizeBeforeUpdate = employeeRepository.findAll().size();
        employee.setId(count.incrementAndGet());

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restEmployeeMockMvc
            .perform(
                put(ENTITY_API_URL_ID, employee.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(employee))
            )
            .andExpect(status().isBadRequest());

        // Validate the Employee in the database
        List<Employee> employeeList = employeeRepository.findAll();
        assertThat(employeeList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithIdMismatchEmployee() throws Exception {
        int databaseSizeBeforeUpdate = employeeRepository.findAll().size();
        employee.setId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restEmployeeMockMvc
            .perform(
                put(ENTITY_API_URL_ID, count.incrementAndGet())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(employee))
            )
            .andExpect(status().isBadRequest());

        // Validate the Employee in the database
        List<Employee> employeeList = employeeRepository.findAll();
        assertThat(employeeList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithMissingIdPathParamEmployee() throws Exception {
        int databaseSizeBeforeUpdate = employeeRepository.findAll().size();
        employee.setId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restEmployeeMockMvc
            .perform(put(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(employee)))
            .andExpect(status().isMethodNotAllowed());

        // Validate the Employee in the database
        List<Employee> employeeList = employeeRepository.findAll();
        assertThat(employeeList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void partialUpdateEmployeeWithPatch() throws Exception {
        // Initialize the database
        employeeRepository.saveAndFlush(employee);

        int databaseSizeBeforeUpdate = employeeRepository.findAll().size();

        // Update the employee using partial update
        Employee partialUpdatedEmployee = new Employee();
        partialUpdatedEmployee.setId(employee.getId());

        partialUpdatedEmployee.gender(UPDATED_GENDER).phone(UPDATED_PHONE);

        restEmployeeMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedEmployee.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedEmployee))
            )
            .andExpect(status().isOk());

        // Validate the Employee in the database
        List<Employee> employeeList = employeeRepository.findAll();
        assertThat(employeeList).hasSize(databaseSizeBeforeUpdate);
        Employee testEmployee = employeeList.get(employeeList.size() - 1);
        assertThat(testEmployee.getEmployeeName()).isEqualTo(DEFAULT_EMPLOYEE_NAME);
        assertThat(testEmployee.getGender()).isEqualTo(UPDATED_GENDER);
        assertThat(testEmployee.getPhone()).isEqualTo(UPDATED_PHONE);
        assertThat(testEmployee.getAddressLine1()).isEqualTo(DEFAULT_ADDRESS_LINE_1);
    }

    @Test
    @Transactional
    void fullUpdateEmployeeWithPatch() throws Exception {
        // Initialize the database
        employeeRepository.saveAndFlush(employee);

        int databaseSizeBeforeUpdate = employeeRepository.findAll().size();

        // Update the employee using partial update
        Employee partialUpdatedEmployee = new Employee();
        partialUpdatedEmployee.setId(employee.getId());

        partialUpdatedEmployee
            .employeeName(UPDATED_EMPLOYEE_NAME)
            .gender(UPDATED_GENDER)
            .phone(UPDATED_PHONE)
            .addressLine1(UPDATED_ADDRESS_LINE_1);

        restEmployeeMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedEmployee.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedEmployee))
            )
            .andExpect(status().isOk());

        // Validate the Employee in the database
        List<Employee> employeeList = employeeRepository.findAll();
        assertThat(employeeList).hasSize(databaseSizeBeforeUpdate);
        Employee testEmployee = employeeList.get(employeeList.size() - 1);
        assertThat(testEmployee.getEmployeeName()).isEqualTo(UPDATED_EMPLOYEE_NAME);
        assertThat(testEmployee.getGender()).isEqualTo(UPDATED_GENDER);
        assertThat(testEmployee.getPhone()).isEqualTo(UPDATED_PHONE);
        assertThat(testEmployee.getAddressLine1()).isEqualTo(UPDATED_ADDRESS_LINE_1);
    }

    @Test
    @Transactional
    void patchNonExistingEmployee() throws Exception {
        int databaseSizeBeforeUpdate = employeeRepository.findAll().size();
        employee.setId(count.incrementAndGet());

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restEmployeeMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, employee.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(employee))
            )
            .andExpect(status().isBadRequest());

        // Validate the Employee in the database
        List<Employee> employeeList = employeeRepository.findAll();
        assertThat(employeeList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithIdMismatchEmployee() throws Exception {
        int databaseSizeBeforeUpdate = employeeRepository.findAll().size();
        employee.setId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restEmployeeMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, count.incrementAndGet())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(employee))
            )
            .andExpect(status().isBadRequest());

        // Validate the Employee in the database
        List<Employee> employeeList = employeeRepository.findAll();
        assertThat(employeeList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithMissingIdPathParamEmployee() throws Exception {
        int databaseSizeBeforeUpdate = employeeRepository.findAll().size();
        employee.setId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restEmployeeMockMvc
            .perform(patch(ENTITY_API_URL).contentType("application/merge-patch+json").content(TestUtil.convertObjectToJsonBytes(employee)))
            .andExpect(status().isMethodNotAllowed());

        // Validate the Employee in the database
        List<Employee> employeeList = employeeRepository.findAll();
        assertThat(employeeList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void deleteEmployee() throws Exception {
        // Initialize the database
        employeeRepository.saveAndFlush(employee);

        int databaseSizeBeforeDelete = employeeRepository.findAll().size();

        // Delete the employee
        restEmployeeMockMvc
            .perform(delete(ENTITY_API_URL_ID, employee.getId()).accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        List<Employee> employeeList = employeeRepository.findAll();
        assertThat(employeeList).hasSize(databaseSizeBeforeDelete - 1);
    }
}
