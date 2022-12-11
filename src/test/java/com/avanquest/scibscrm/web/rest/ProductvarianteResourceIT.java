package com.avanquest.scibscrm.web.rest;

import static com.avanquest.scibscrm.web.rest.TestUtil.sameNumber;
import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import com.avanquest.scibscrm.IntegrationTest;
import com.avanquest.scibscrm.domain.Product;
import com.avanquest.scibscrm.domain.Productvariante;
import com.avanquest.scibscrm.repository.ProductvarianteRepository;
import com.avanquest.scibscrm.service.criteria.ProductvarianteCriteria;
import java.math.BigDecimal;
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
 * Integration tests for the {@link ProductvarianteResource} REST controller.
 */
@IntegrationTest
@AutoConfigureMockMvc
@WithMockUser
class ProductvarianteResourceIT {

    private static final byte[] DEFAULT_PICTURE = TestUtil.createByteArray(1, "0");
    private static final byte[] UPDATED_PICTURE = TestUtil.createByteArray(1, "1");
    private static final String DEFAULT_PICTURE_CONTENT_TYPE = "image/jpg";
    private static final String UPDATED_PICTURE_CONTENT_TYPE = "image/png";

    private static final String DEFAULT_CODEBARRE = "AAAAAAAAAA";
    private static final String UPDATED_CODEBARRE = "BBBBBBBBBB";

    private static final String DEFAULT_PRODUCT_CODE = "AAAAAAAAAA";
    private static final String UPDATED_PRODUCT_CODE = "BBBBBBBBBB";

    private static final BigDecimal DEFAULT_SALE_PRICE = new BigDecimal(1);
    private static final BigDecimal UPDATED_SALE_PRICE = new BigDecimal(2);
    private static final BigDecimal SMALLER_SALE_PRICE = new BigDecimal(1 - 1);

    private static final String DEFAULT_UNITE_MESURE = "AAAAAAAAAA";
    private static final String UPDATED_UNITE_MESURE = "BBBBBBBBBB";

    private static final Double DEFAULT_STOCK_DISPONIBLE = 1D;
    private static final Double UPDATED_STOCK_DISPONIBLE = 2D;
    private static final Double SMALLER_STOCK_DISPONIBLE = 1D - 1D;

    private static final String ENTITY_API_URL = "/api/productvariantes";
    private static final String ENTITY_API_URL_ID = ENTITY_API_URL + "/{id}";

    private static Random random = new Random();
    private static AtomicLong count = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    @Autowired
    private ProductvarianteRepository productvarianteRepository;

    @Autowired
    private EntityManager em;

    @Autowired
    private MockMvc restProductvarianteMockMvc;

    private Productvariante productvariante;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Productvariante createEntity(EntityManager em) {
        Productvariante productvariante = new Productvariante()
            .picture(DEFAULT_PICTURE)
            .pictureContentType(DEFAULT_PICTURE_CONTENT_TYPE)
            .codebarre(DEFAULT_CODEBARRE)
            .productCode(DEFAULT_PRODUCT_CODE)
            .salePrice(DEFAULT_SALE_PRICE)
            .uniteMesure(DEFAULT_UNITE_MESURE)
            .stockDisponible(DEFAULT_STOCK_DISPONIBLE);
        return productvariante;
    }

    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Productvariante createUpdatedEntity(EntityManager em) {
        Productvariante productvariante = new Productvariante()
            .picture(UPDATED_PICTURE)
            .pictureContentType(UPDATED_PICTURE_CONTENT_TYPE)
            .codebarre(UPDATED_CODEBARRE)
            .productCode(UPDATED_PRODUCT_CODE)
            .salePrice(UPDATED_SALE_PRICE)
            .uniteMesure(UPDATED_UNITE_MESURE)
            .stockDisponible(UPDATED_STOCK_DISPONIBLE);
        return productvariante;
    }

    @BeforeEach
    public void initTest() {
        productvariante = createEntity(em);
    }

    @Test
    @Transactional
    void createProductvariante() throws Exception {
        int databaseSizeBeforeCreate = productvarianteRepository.findAll().size();
        // Create the Productvariante
        restProductvarianteMockMvc
            .perform(
                post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(productvariante))
            )
            .andExpect(status().isCreated());

        // Validate the Productvariante in the database
        List<Productvariante> productvarianteList = productvarianteRepository.findAll();
        assertThat(productvarianteList).hasSize(databaseSizeBeforeCreate + 1);
        Productvariante testProductvariante = productvarianteList.get(productvarianteList.size() - 1);
        assertThat(testProductvariante.getPicture()).isEqualTo(DEFAULT_PICTURE);
        assertThat(testProductvariante.getPictureContentType()).isEqualTo(DEFAULT_PICTURE_CONTENT_TYPE);
        assertThat(testProductvariante.getCodebarre()).isEqualTo(DEFAULT_CODEBARRE);
        assertThat(testProductvariante.getProductCode()).isEqualTo(DEFAULT_PRODUCT_CODE);
        assertThat(testProductvariante.getSalePrice()).isEqualByComparingTo(DEFAULT_SALE_PRICE);
        assertThat(testProductvariante.getUniteMesure()).isEqualTo(DEFAULT_UNITE_MESURE);
        assertThat(testProductvariante.getStockDisponible()).isEqualTo(DEFAULT_STOCK_DISPONIBLE);
    }

    @Test
    @Transactional
    void createProductvarianteWithExistingId() throws Exception {
        // Create the Productvariante with an existing ID
        productvariante.setId(1L);

        int databaseSizeBeforeCreate = productvarianteRepository.findAll().size();

        // An entity with an existing ID cannot be created, so this API call must fail
        restProductvarianteMockMvc
            .perform(
                post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(productvariante))
            )
            .andExpect(status().isBadRequest());

        // Validate the Productvariante in the database
        List<Productvariante> productvarianteList = productvarianteRepository.findAll();
        assertThat(productvarianteList).hasSize(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    void getAllProductvariantes() throws Exception {
        // Initialize the database
        productvarianteRepository.saveAndFlush(productvariante);

        // Get all the productvarianteList
        restProductvarianteMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(productvariante.getId().intValue())))
            .andExpect(jsonPath("$.[*].pictureContentType").value(hasItem(DEFAULT_PICTURE_CONTENT_TYPE)))
            .andExpect(jsonPath("$.[*].picture").value(hasItem(Base64Utils.encodeToString(DEFAULT_PICTURE))))
            .andExpect(jsonPath("$.[*].codebarre").value(hasItem(DEFAULT_CODEBARRE)))
            .andExpect(jsonPath("$.[*].productCode").value(hasItem(DEFAULT_PRODUCT_CODE)))
            .andExpect(jsonPath("$.[*].salePrice").value(hasItem(sameNumber(DEFAULT_SALE_PRICE))))
            .andExpect(jsonPath("$.[*].uniteMesure").value(hasItem(DEFAULT_UNITE_MESURE)))
            .andExpect(jsonPath("$.[*].stockDisponible").value(hasItem(DEFAULT_STOCK_DISPONIBLE.doubleValue())));
    }

    @Test
    @Transactional
    void getProductvariante() throws Exception {
        // Initialize the database
        productvarianteRepository.saveAndFlush(productvariante);

        // Get the productvariante
        restProductvarianteMockMvc
            .perform(get(ENTITY_API_URL_ID, productvariante.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.id").value(productvariante.getId().intValue()))
            .andExpect(jsonPath("$.pictureContentType").value(DEFAULT_PICTURE_CONTENT_TYPE))
            .andExpect(jsonPath("$.picture").value(Base64Utils.encodeToString(DEFAULT_PICTURE)))
            .andExpect(jsonPath("$.codebarre").value(DEFAULT_CODEBARRE))
            .andExpect(jsonPath("$.productCode").value(DEFAULT_PRODUCT_CODE))
            .andExpect(jsonPath("$.salePrice").value(sameNumber(DEFAULT_SALE_PRICE)))
            .andExpect(jsonPath("$.uniteMesure").value(DEFAULT_UNITE_MESURE))
            .andExpect(jsonPath("$.stockDisponible").value(DEFAULT_STOCK_DISPONIBLE.doubleValue()));
    }

    @Test
    @Transactional
    void getProductvariantesByIdFiltering() throws Exception {
        // Initialize the database
        productvarianteRepository.saveAndFlush(productvariante);

        Long id = productvariante.getId();

        defaultProductvarianteShouldBeFound("id.equals=" + id);
        defaultProductvarianteShouldNotBeFound("id.notEquals=" + id);

        defaultProductvarianteShouldBeFound("id.greaterThanOrEqual=" + id);
        defaultProductvarianteShouldNotBeFound("id.greaterThan=" + id);

        defaultProductvarianteShouldBeFound("id.lessThanOrEqual=" + id);
        defaultProductvarianteShouldNotBeFound("id.lessThan=" + id);
    }

    @Test
    @Transactional
    void getAllProductvariantesByCodebarreIsEqualToSomething() throws Exception {
        // Initialize the database
        productvarianteRepository.saveAndFlush(productvariante);

        // Get all the productvarianteList where codebarre equals to DEFAULT_CODEBARRE
        defaultProductvarianteShouldBeFound("codebarre.equals=" + DEFAULT_CODEBARRE);

        // Get all the productvarianteList where codebarre equals to UPDATED_CODEBARRE
        defaultProductvarianteShouldNotBeFound("codebarre.equals=" + UPDATED_CODEBARRE);
    }

    @Test
    @Transactional
    void getAllProductvariantesByCodebarreIsNotEqualToSomething() throws Exception {
        // Initialize the database
        productvarianteRepository.saveAndFlush(productvariante);

        // Get all the productvarianteList where codebarre not equals to DEFAULT_CODEBARRE
        defaultProductvarianteShouldNotBeFound("codebarre.notEquals=" + DEFAULT_CODEBARRE);

        // Get all the productvarianteList where codebarre not equals to UPDATED_CODEBARRE
        defaultProductvarianteShouldBeFound("codebarre.notEquals=" + UPDATED_CODEBARRE);
    }

    @Test
    @Transactional
    void getAllProductvariantesByCodebarreIsInShouldWork() throws Exception {
        // Initialize the database
        productvarianteRepository.saveAndFlush(productvariante);

        // Get all the productvarianteList where codebarre in DEFAULT_CODEBARRE or UPDATED_CODEBARRE
        defaultProductvarianteShouldBeFound("codebarre.in=" + DEFAULT_CODEBARRE + "," + UPDATED_CODEBARRE);

        // Get all the productvarianteList where codebarre equals to UPDATED_CODEBARRE
        defaultProductvarianteShouldNotBeFound("codebarre.in=" + UPDATED_CODEBARRE);
    }

    @Test
    @Transactional
    void getAllProductvariantesByCodebarreIsNullOrNotNull() throws Exception {
        // Initialize the database
        productvarianteRepository.saveAndFlush(productvariante);

        // Get all the productvarianteList where codebarre is not null
        defaultProductvarianteShouldBeFound("codebarre.specified=true");

        // Get all the productvarianteList where codebarre is null
        defaultProductvarianteShouldNotBeFound("codebarre.specified=false");
    }

    @Test
    @Transactional
    void getAllProductvariantesByCodebarreContainsSomething() throws Exception {
        // Initialize the database
        productvarianteRepository.saveAndFlush(productvariante);

        // Get all the productvarianteList where codebarre contains DEFAULT_CODEBARRE
        defaultProductvarianteShouldBeFound("codebarre.contains=" + DEFAULT_CODEBARRE);

        // Get all the productvarianteList where codebarre contains UPDATED_CODEBARRE
        defaultProductvarianteShouldNotBeFound("codebarre.contains=" + UPDATED_CODEBARRE);
    }

    @Test
    @Transactional
    void getAllProductvariantesByCodebarreNotContainsSomething() throws Exception {
        // Initialize the database
        productvarianteRepository.saveAndFlush(productvariante);

        // Get all the productvarianteList where codebarre does not contain DEFAULT_CODEBARRE
        defaultProductvarianteShouldNotBeFound("codebarre.doesNotContain=" + DEFAULT_CODEBARRE);

        // Get all the productvarianteList where codebarre does not contain UPDATED_CODEBARRE
        defaultProductvarianteShouldBeFound("codebarre.doesNotContain=" + UPDATED_CODEBARRE);
    }

    @Test
    @Transactional
    void getAllProductvariantesByProductCodeIsEqualToSomething() throws Exception {
        // Initialize the database
        productvarianteRepository.saveAndFlush(productvariante);

        // Get all the productvarianteList where productCode equals to DEFAULT_PRODUCT_CODE
        defaultProductvarianteShouldBeFound("productCode.equals=" + DEFAULT_PRODUCT_CODE);

        // Get all the productvarianteList where productCode equals to UPDATED_PRODUCT_CODE
        defaultProductvarianteShouldNotBeFound("productCode.equals=" + UPDATED_PRODUCT_CODE);
    }

    @Test
    @Transactional
    void getAllProductvariantesByProductCodeIsNotEqualToSomething() throws Exception {
        // Initialize the database
        productvarianteRepository.saveAndFlush(productvariante);

        // Get all the productvarianteList where productCode not equals to DEFAULT_PRODUCT_CODE
        defaultProductvarianteShouldNotBeFound("productCode.notEquals=" + DEFAULT_PRODUCT_CODE);

        // Get all the productvarianteList where productCode not equals to UPDATED_PRODUCT_CODE
        defaultProductvarianteShouldBeFound("productCode.notEquals=" + UPDATED_PRODUCT_CODE);
    }

    @Test
    @Transactional
    void getAllProductvariantesByProductCodeIsInShouldWork() throws Exception {
        // Initialize the database
        productvarianteRepository.saveAndFlush(productvariante);

        // Get all the productvarianteList where productCode in DEFAULT_PRODUCT_CODE or UPDATED_PRODUCT_CODE
        defaultProductvarianteShouldBeFound("productCode.in=" + DEFAULT_PRODUCT_CODE + "," + UPDATED_PRODUCT_CODE);

        // Get all the productvarianteList where productCode equals to UPDATED_PRODUCT_CODE
        defaultProductvarianteShouldNotBeFound("productCode.in=" + UPDATED_PRODUCT_CODE);
    }

    @Test
    @Transactional
    void getAllProductvariantesByProductCodeIsNullOrNotNull() throws Exception {
        // Initialize the database
        productvarianteRepository.saveAndFlush(productvariante);

        // Get all the productvarianteList where productCode is not null
        defaultProductvarianteShouldBeFound("productCode.specified=true");

        // Get all the productvarianteList where productCode is null
        defaultProductvarianteShouldNotBeFound("productCode.specified=false");
    }

    @Test
    @Transactional
    void getAllProductvariantesByProductCodeContainsSomething() throws Exception {
        // Initialize the database
        productvarianteRepository.saveAndFlush(productvariante);

        // Get all the productvarianteList where productCode contains DEFAULT_PRODUCT_CODE
        defaultProductvarianteShouldBeFound("productCode.contains=" + DEFAULT_PRODUCT_CODE);

        // Get all the productvarianteList where productCode contains UPDATED_PRODUCT_CODE
        defaultProductvarianteShouldNotBeFound("productCode.contains=" + UPDATED_PRODUCT_CODE);
    }

    @Test
    @Transactional
    void getAllProductvariantesByProductCodeNotContainsSomething() throws Exception {
        // Initialize the database
        productvarianteRepository.saveAndFlush(productvariante);

        // Get all the productvarianteList where productCode does not contain DEFAULT_PRODUCT_CODE
        defaultProductvarianteShouldNotBeFound("productCode.doesNotContain=" + DEFAULT_PRODUCT_CODE);

        // Get all the productvarianteList where productCode does not contain UPDATED_PRODUCT_CODE
        defaultProductvarianteShouldBeFound("productCode.doesNotContain=" + UPDATED_PRODUCT_CODE);
    }

    @Test
    @Transactional
    void getAllProductvariantesBySalePriceIsEqualToSomething() throws Exception {
        // Initialize the database
        productvarianteRepository.saveAndFlush(productvariante);

        // Get all the productvarianteList where salePrice equals to DEFAULT_SALE_PRICE
        defaultProductvarianteShouldBeFound("salePrice.equals=" + DEFAULT_SALE_PRICE);

        // Get all the productvarianteList where salePrice equals to UPDATED_SALE_PRICE
        defaultProductvarianteShouldNotBeFound("salePrice.equals=" + UPDATED_SALE_PRICE);
    }

    @Test
    @Transactional
    void getAllProductvariantesBySalePriceIsNotEqualToSomething() throws Exception {
        // Initialize the database
        productvarianteRepository.saveAndFlush(productvariante);

        // Get all the productvarianteList where salePrice not equals to DEFAULT_SALE_PRICE
        defaultProductvarianteShouldNotBeFound("salePrice.notEquals=" + DEFAULT_SALE_PRICE);

        // Get all the productvarianteList where salePrice not equals to UPDATED_SALE_PRICE
        defaultProductvarianteShouldBeFound("salePrice.notEquals=" + UPDATED_SALE_PRICE);
    }

    @Test
    @Transactional
    void getAllProductvariantesBySalePriceIsInShouldWork() throws Exception {
        // Initialize the database
        productvarianteRepository.saveAndFlush(productvariante);

        // Get all the productvarianteList where salePrice in DEFAULT_SALE_PRICE or UPDATED_SALE_PRICE
        defaultProductvarianteShouldBeFound("salePrice.in=" + DEFAULT_SALE_PRICE + "," + UPDATED_SALE_PRICE);

        // Get all the productvarianteList where salePrice equals to UPDATED_SALE_PRICE
        defaultProductvarianteShouldNotBeFound("salePrice.in=" + UPDATED_SALE_PRICE);
    }

    @Test
    @Transactional
    void getAllProductvariantesBySalePriceIsNullOrNotNull() throws Exception {
        // Initialize the database
        productvarianteRepository.saveAndFlush(productvariante);

        // Get all the productvarianteList where salePrice is not null
        defaultProductvarianteShouldBeFound("salePrice.specified=true");

        // Get all the productvarianteList where salePrice is null
        defaultProductvarianteShouldNotBeFound("salePrice.specified=false");
    }

    @Test
    @Transactional
    void getAllProductvariantesBySalePriceIsGreaterThanOrEqualToSomething() throws Exception {
        // Initialize the database
        productvarianteRepository.saveAndFlush(productvariante);

        // Get all the productvarianteList where salePrice is greater than or equal to DEFAULT_SALE_PRICE
        defaultProductvarianteShouldBeFound("salePrice.greaterThanOrEqual=" + DEFAULT_SALE_PRICE);

        // Get all the productvarianteList where salePrice is greater than or equal to UPDATED_SALE_PRICE
        defaultProductvarianteShouldNotBeFound("salePrice.greaterThanOrEqual=" + UPDATED_SALE_PRICE);
    }

    @Test
    @Transactional
    void getAllProductvariantesBySalePriceIsLessThanOrEqualToSomething() throws Exception {
        // Initialize the database
        productvarianteRepository.saveAndFlush(productvariante);

        // Get all the productvarianteList where salePrice is less than or equal to DEFAULT_SALE_PRICE
        defaultProductvarianteShouldBeFound("salePrice.lessThanOrEqual=" + DEFAULT_SALE_PRICE);

        // Get all the productvarianteList where salePrice is less than or equal to SMALLER_SALE_PRICE
        defaultProductvarianteShouldNotBeFound("salePrice.lessThanOrEqual=" + SMALLER_SALE_PRICE);
    }

    @Test
    @Transactional
    void getAllProductvariantesBySalePriceIsLessThanSomething() throws Exception {
        // Initialize the database
        productvarianteRepository.saveAndFlush(productvariante);

        // Get all the productvarianteList where salePrice is less than DEFAULT_SALE_PRICE
        defaultProductvarianteShouldNotBeFound("salePrice.lessThan=" + DEFAULT_SALE_PRICE);

        // Get all the productvarianteList where salePrice is less than UPDATED_SALE_PRICE
        defaultProductvarianteShouldBeFound("salePrice.lessThan=" + UPDATED_SALE_PRICE);
    }

    @Test
    @Transactional
    void getAllProductvariantesBySalePriceIsGreaterThanSomething() throws Exception {
        // Initialize the database
        productvarianteRepository.saveAndFlush(productvariante);

        // Get all the productvarianteList where salePrice is greater than DEFAULT_SALE_PRICE
        defaultProductvarianteShouldNotBeFound("salePrice.greaterThan=" + DEFAULT_SALE_PRICE);

        // Get all the productvarianteList where salePrice is greater than SMALLER_SALE_PRICE
        defaultProductvarianteShouldBeFound("salePrice.greaterThan=" + SMALLER_SALE_PRICE);
    }

    @Test
    @Transactional
    void getAllProductvariantesByUniteMesureIsEqualToSomething() throws Exception {
        // Initialize the database
        productvarianteRepository.saveAndFlush(productvariante);

        // Get all the productvarianteList where uniteMesure equals to DEFAULT_UNITE_MESURE
        defaultProductvarianteShouldBeFound("uniteMesure.equals=" + DEFAULT_UNITE_MESURE);

        // Get all the productvarianteList where uniteMesure equals to UPDATED_UNITE_MESURE
        defaultProductvarianteShouldNotBeFound("uniteMesure.equals=" + UPDATED_UNITE_MESURE);
    }

    @Test
    @Transactional
    void getAllProductvariantesByUniteMesureIsNotEqualToSomething() throws Exception {
        // Initialize the database
        productvarianteRepository.saveAndFlush(productvariante);

        // Get all the productvarianteList where uniteMesure not equals to DEFAULT_UNITE_MESURE
        defaultProductvarianteShouldNotBeFound("uniteMesure.notEquals=" + DEFAULT_UNITE_MESURE);

        // Get all the productvarianteList where uniteMesure not equals to UPDATED_UNITE_MESURE
        defaultProductvarianteShouldBeFound("uniteMesure.notEquals=" + UPDATED_UNITE_MESURE);
    }

    @Test
    @Transactional
    void getAllProductvariantesByUniteMesureIsInShouldWork() throws Exception {
        // Initialize the database
        productvarianteRepository.saveAndFlush(productvariante);

        // Get all the productvarianteList where uniteMesure in DEFAULT_UNITE_MESURE or UPDATED_UNITE_MESURE
        defaultProductvarianteShouldBeFound("uniteMesure.in=" + DEFAULT_UNITE_MESURE + "," + UPDATED_UNITE_MESURE);

        // Get all the productvarianteList where uniteMesure equals to UPDATED_UNITE_MESURE
        defaultProductvarianteShouldNotBeFound("uniteMesure.in=" + UPDATED_UNITE_MESURE);
    }

    @Test
    @Transactional
    void getAllProductvariantesByUniteMesureIsNullOrNotNull() throws Exception {
        // Initialize the database
        productvarianteRepository.saveAndFlush(productvariante);

        // Get all the productvarianteList where uniteMesure is not null
        defaultProductvarianteShouldBeFound("uniteMesure.specified=true");

        // Get all the productvarianteList where uniteMesure is null
        defaultProductvarianteShouldNotBeFound("uniteMesure.specified=false");
    }

    @Test
    @Transactional
    void getAllProductvariantesByUniteMesureContainsSomething() throws Exception {
        // Initialize the database
        productvarianteRepository.saveAndFlush(productvariante);

        // Get all the productvarianteList where uniteMesure contains DEFAULT_UNITE_MESURE
        defaultProductvarianteShouldBeFound("uniteMesure.contains=" + DEFAULT_UNITE_MESURE);

        // Get all the productvarianteList where uniteMesure contains UPDATED_UNITE_MESURE
        defaultProductvarianteShouldNotBeFound("uniteMesure.contains=" + UPDATED_UNITE_MESURE);
    }

    @Test
    @Transactional
    void getAllProductvariantesByUniteMesureNotContainsSomething() throws Exception {
        // Initialize the database
        productvarianteRepository.saveAndFlush(productvariante);

        // Get all the productvarianteList where uniteMesure does not contain DEFAULT_UNITE_MESURE
        defaultProductvarianteShouldNotBeFound("uniteMesure.doesNotContain=" + DEFAULT_UNITE_MESURE);

        // Get all the productvarianteList where uniteMesure does not contain UPDATED_UNITE_MESURE
        defaultProductvarianteShouldBeFound("uniteMesure.doesNotContain=" + UPDATED_UNITE_MESURE);
    }

    @Test
    @Transactional
    void getAllProductvariantesByStockDisponibleIsEqualToSomething() throws Exception {
        // Initialize the database
        productvarianteRepository.saveAndFlush(productvariante);

        // Get all the productvarianteList where stockDisponible equals to DEFAULT_STOCK_DISPONIBLE
        defaultProductvarianteShouldBeFound("stockDisponible.equals=" + DEFAULT_STOCK_DISPONIBLE);

        // Get all the productvarianteList where stockDisponible equals to UPDATED_STOCK_DISPONIBLE
        defaultProductvarianteShouldNotBeFound("stockDisponible.equals=" + UPDATED_STOCK_DISPONIBLE);
    }

    @Test
    @Transactional
    void getAllProductvariantesByStockDisponibleIsNotEqualToSomething() throws Exception {
        // Initialize the database
        productvarianteRepository.saveAndFlush(productvariante);

        // Get all the productvarianteList where stockDisponible not equals to DEFAULT_STOCK_DISPONIBLE
        defaultProductvarianteShouldNotBeFound("stockDisponible.notEquals=" + DEFAULT_STOCK_DISPONIBLE);

        // Get all the productvarianteList where stockDisponible not equals to UPDATED_STOCK_DISPONIBLE
        defaultProductvarianteShouldBeFound("stockDisponible.notEquals=" + UPDATED_STOCK_DISPONIBLE);
    }

    @Test
    @Transactional
    void getAllProductvariantesByStockDisponibleIsInShouldWork() throws Exception {
        // Initialize the database
        productvarianteRepository.saveAndFlush(productvariante);

        // Get all the productvarianteList where stockDisponible in DEFAULT_STOCK_DISPONIBLE or UPDATED_STOCK_DISPONIBLE
        defaultProductvarianteShouldBeFound("stockDisponible.in=" + DEFAULT_STOCK_DISPONIBLE + "," + UPDATED_STOCK_DISPONIBLE);

        // Get all the productvarianteList where stockDisponible equals to UPDATED_STOCK_DISPONIBLE
        defaultProductvarianteShouldNotBeFound("stockDisponible.in=" + UPDATED_STOCK_DISPONIBLE);
    }

    @Test
    @Transactional
    void getAllProductvariantesByStockDisponibleIsNullOrNotNull() throws Exception {
        // Initialize the database
        productvarianteRepository.saveAndFlush(productvariante);

        // Get all the productvarianteList where stockDisponible is not null
        defaultProductvarianteShouldBeFound("stockDisponible.specified=true");

        // Get all the productvarianteList where stockDisponible is null
        defaultProductvarianteShouldNotBeFound("stockDisponible.specified=false");
    }

    @Test
    @Transactional
    void getAllProductvariantesByStockDisponibleIsGreaterThanOrEqualToSomething() throws Exception {
        // Initialize the database
        productvarianteRepository.saveAndFlush(productvariante);

        // Get all the productvarianteList where stockDisponible is greater than or equal to DEFAULT_STOCK_DISPONIBLE
        defaultProductvarianteShouldBeFound("stockDisponible.greaterThanOrEqual=" + DEFAULT_STOCK_DISPONIBLE);

        // Get all the productvarianteList where stockDisponible is greater than or equal to UPDATED_STOCK_DISPONIBLE
        defaultProductvarianteShouldNotBeFound("stockDisponible.greaterThanOrEqual=" + UPDATED_STOCK_DISPONIBLE);
    }

    @Test
    @Transactional
    void getAllProductvariantesByStockDisponibleIsLessThanOrEqualToSomething() throws Exception {
        // Initialize the database
        productvarianteRepository.saveAndFlush(productvariante);

        // Get all the productvarianteList where stockDisponible is less than or equal to DEFAULT_STOCK_DISPONIBLE
        defaultProductvarianteShouldBeFound("stockDisponible.lessThanOrEqual=" + DEFAULT_STOCK_DISPONIBLE);

        // Get all the productvarianteList where stockDisponible is less than or equal to SMALLER_STOCK_DISPONIBLE
        defaultProductvarianteShouldNotBeFound("stockDisponible.lessThanOrEqual=" + SMALLER_STOCK_DISPONIBLE);
    }

    @Test
    @Transactional
    void getAllProductvariantesByStockDisponibleIsLessThanSomething() throws Exception {
        // Initialize the database
        productvarianteRepository.saveAndFlush(productvariante);

        // Get all the productvarianteList where stockDisponible is less than DEFAULT_STOCK_DISPONIBLE
        defaultProductvarianteShouldNotBeFound("stockDisponible.lessThan=" + DEFAULT_STOCK_DISPONIBLE);

        // Get all the productvarianteList where stockDisponible is less than UPDATED_STOCK_DISPONIBLE
        defaultProductvarianteShouldBeFound("stockDisponible.lessThan=" + UPDATED_STOCK_DISPONIBLE);
    }

    @Test
    @Transactional
    void getAllProductvariantesByStockDisponibleIsGreaterThanSomething() throws Exception {
        // Initialize the database
        productvarianteRepository.saveAndFlush(productvariante);

        // Get all the productvarianteList where stockDisponible is greater than DEFAULT_STOCK_DISPONIBLE
        defaultProductvarianteShouldNotBeFound("stockDisponible.greaterThan=" + DEFAULT_STOCK_DISPONIBLE);

        // Get all the productvarianteList where stockDisponible is greater than SMALLER_STOCK_DISPONIBLE
        defaultProductvarianteShouldBeFound("stockDisponible.greaterThan=" + SMALLER_STOCK_DISPONIBLE);
    }

    @Test
    @Transactional
    void getAllProductvariantesByProductIsEqualToSomething() throws Exception {
        // Initialize the database
        productvarianteRepository.saveAndFlush(productvariante);
        Product product;
        if (TestUtil.findAll(em, Product.class).isEmpty()) {
            product = ProductResourceIT.createEntity(em);
            em.persist(product);
            em.flush();
        } else {
            product = TestUtil.findAll(em, Product.class).get(0);
        }
        em.persist(product);
        em.flush();
        productvariante.addProduct(product);
        productvarianteRepository.saveAndFlush(productvariante);
        Long productId = product.getId();

        // Get all the productvarianteList where product equals to productId
        defaultProductvarianteShouldBeFound("productId.equals=" + productId);

        // Get all the productvarianteList where product equals to (productId + 1)
        defaultProductvarianteShouldNotBeFound("productId.equals=" + (productId + 1));
    }

    /**
     * Executes the search, and checks that the default entity is returned.
     */
    private void defaultProductvarianteShouldBeFound(String filter) throws Exception {
        restProductvarianteMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(productvariante.getId().intValue())))
            .andExpect(jsonPath("$.[*].pictureContentType").value(hasItem(DEFAULT_PICTURE_CONTENT_TYPE)))
            .andExpect(jsonPath("$.[*].picture").value(hasItem(Base64Utils.encodeToString(DEFAULT_PICTURE))))
            .andExpect(jsonPath("$.[*].codebarre").value(hasItem(DEFAULT_CODEBARRE)))
            .andExpect(jsonPath("$.[*].productCode").value(hasItem(DEFAULT_PRODUCT_CODE)))
            .andExpect(jsonPath("$.[*].salePrice").value(hasItem(sameNumber(DEFAULT_SALE_PRICE))))
            .andExpect(jsonPath("$.[*].uniteMesure").value(hasItem(DEFAULT_UNITE_MESURE)))
            .andExpect(jsonPath("$.[*].stockDisponible").value(hasItem(DEFAULT_STOCK_DISPONIBLE.doubleValue())));

        // Check, that the count call also returns 1
        restProductvarianteMockMvc
            .perform(get(ENTITY_API_URL + "/count?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(content().string("1"));
    }

    /**
     * Executes the search, and checks that the default entity is not returned.
     */
    private void defaultProductvarianteShouldNotBeFound(String filter) throws Exception {
        restProductvarianteMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$").isArray())
            .andExpect(jsonPath("$").isEmpty());

        // Check, that the count call also returns 0
        restProductvarianteMockMvc
            .perform(get(ENTITY_API_URL + "/count?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(content().string("0"));
    }

    @Test
    @Transactional
    void getNonExistingProductvariante() throws Exception {
        // Get the productvariante
        restProductvarianteMockMvc.perform(get(ENTITY_API_URL_ID, Long.MAX_VALUE)).andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    void putNewProductvariante() throws Exception {
        // Initialize the database
        productvarianteRepository.saveAndFlush(productvariante);

        int databaseSizeBeforeUpdate = productvarianteRepository.findAll().size();

        // Update the productvariante
        Productvariante updatedProductvariante = productvarianteRepository.findById(productvariante.getId()).get();
        // Disconnect from session so that the updates on updatedProductvariante are not directly saved in db
        em.detach(updatedProductvariante);
        updatedProductvariante
            .picture(UPDATED_PICTURE)
            .pictureContentType(UPDATED_PICTURE_CONTENT_TYPE)
            .codebarre(UPDATED_CODEBARRE)
            .productCode(UPDATED_PRODUCT_CODE)
            .salePrice(UPDATED_SALE_PRICE)
            .uniteMesure(UPDATED_UNITE_MESURE)
            .stockDisponible(UPDATED_STOCK_DISPONIBLE);

        restProductvarianteMockMvc
            .perform(
                put(ENTITY_API_URL_ID, updatedProductvariante.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(updatedProductvariante))
            )
            .andExpect(status().isOk());

        // Validate the Productvariante in the database
        List<Productvariante> productvarianteList = productvarianteRepository.findAll();
        assertThat(productvarianteList).hasSize(databaseSizeBeforeUpdate);
        Productvariante testProductvariante = productvarianteList.get(productvarianteList.size() - 1);
        assertThat(testProductvariante.getPicture()).isEqualTo(UPDATED_PICTURE);
        assertThat(testProductvariante.getPictureContentType()).isEqualTo(UPDATED_PICTURE_CONTENT_TYPE);
        assertThat(testProductvariante.getCodebarre()).isEqualTo(UPDATED_CODEBARRE);
        assertThat(testProductvariante.getProductCode()).isEqualTo(UPDATED_PRODUCT_CODE);
        assertThat(testProductvariante.getSalePrice()).isEqualByComparingTo(UPDATED_SALE_PRICE);
        assertThat(testProductvariante.getUniteMesure()).isEqualTo(UPDATED_UNITE_MESURE);
        assertThat(testProductvariante.getStockDisponible()).isEqualTo(UPDATED_STOCK_DISPONIBLE);
    }

    @Test
    @Transactional
    void putNonExistingProductvariante() throws Exception {
        int databaseSizeBeforeUpdate = productvarianteRepository.findAll().size();
        productvariante.setId(count.incrementAndGet());

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restProductvarianteMockMvc
            .perform(
                put(ENTITY_API_URL_ID, productvariante.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(productvariante))
            )
            .andExpect(status().isBadRequest());

        // Validate the Productvariante in the database
        List<Productvariante> productvarianteList = productvarianteRepository.findAll();
        assertThat(productvarianteList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithIdMismatchProductvariante() throws Exception {
        int databaseSizeBeforeUpdate = productvarianteRepository.findAll().size();
        productvariante.setId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restProductvarianteMockMvc
            .perform(
                put(ENTITY_API_URL_ID, count.incrementAndGet())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(productvariante))
            )
            .andExpect(status().isBadRequest());

        // Validate the Productvariante in the database
        List<Productvariante> productvarianteList = productvarianteRepository.findAll();
        assertThat(productvarianteList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithMissingIdPathParamProductvariante() throws Exception {
        int databaseSizeBeforeUpdate = productvarianteRepository.findAll().size();
        productvariante.setId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restProductvarianteMockMvc
            .perform(
                put(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(productvariante))
            )
            .andExpect(status().isMethodNotAllowed());

        // Validate the Productvariante in the database
        List<Productvariante> productvarianteList = productvarianteRepository.findAll();
        assertThat(productvarianteList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void partialUpdateProductvarianteWithPatch() throws Exception {
        // Initialize the database
        productvarianteRepository.saveAndFlush(productvariante);

        int databaseSizeBeforeUpdate = productvarianteRepository.findAll().size();

        // Update the productvariante using partial update
        Productvariante partialUpdatedProductvariante = new Productvariante();
        partialUpdatedProductvariante.setId(productvariante.getId());

        partialUpdatedProductvariante
            .picture(UPDATED_PICTURE)
            .pictureContentType(UPDATED_PICTURE_CONTENT_TYPE)
            .codebarre(UPDATED_CODEBARRE)
            .salePrice(UPDATED_SALE_PRICE)
            .uniteMesure(UPDATED_UNITE_MESURE)
            .stockDisponible(UPDATED_STOCK_DISPONIBLE);

        restProductvarianteMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedProductvariante.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedProductvariante))
            )
            .andExpect(status().isOk());

        // Validate the Productvariante in the database
        List<Productvariante> productvarianteList = productvarianteRepository.findAll();
        assertThat(productvarianteList).hasSize(databaseSizeBeforeUpdate);
        Productvariante testProductvariante = productvarianteList.get(productvarianteList.size() - 1);
        assertThat(testProductvariante.getPicture()).isEqualTo(UPDATED_PICTURE);
        assertThat(testProductvariante.getPictureContentType()).isEqualTo(UPDATED_PICTURE_CONTENT_TYPE);
        assertThat(testProductvariante.getCodebarre()).isEqualTo(UPDATED_CODEBARRE);
        assertThat(testProductvariante.getProductCode()).isEqualTo(DEFAULT_PRODUCT_CODE);
        assertThat(testProductvariante.getSalePrice()).isEqualByComparingTo(UPDATED_SALE_PRICE);
        assertThat(testProductvariante.getUniteMesure()).isEqualTo(UPDATED_UNITE_MESURE);
        assertThat(testProductvariante.getStockDisponible()).isEqualTo(UPDATED_STOCK_DISPONIBLE);
    }

    @Test
    @Transactional
    void fullUpdateProductvarianteWithPatch() throws Exception {
        // Initialize the database
        productvarianteRepository.saveAndFlush(productvariante);

        int databaseSizeBeforeUpdate = productvarianteRepository.findAll().size();

        // Update the productvariante using partial update
        Productvariante partialUpdatedProductvariante = new Productvariante();
        partialUpdatedProductvariante.setId(productvariante.getId());

        partialUpdatedProductvariante
            .picture(UPDATED_PICTURE)
            .pictureContentType(UPDATED_PICTURE_CONTENT_TYPE)
            .codebarre(UPDATED_CODEBARRE)
            .productCode(UPDATED_PRODUCT_CODE)
            .salePrice(UPDATED_SALE_PRICE)
            .uniteMesure(UPDATED_UNITE_MESURE)
            .stockDisponible(UPDATED_STOCK_DISPONIBLE);

        restProductvarianteMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedProductvariante.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedProductvariante))
            )
            .andExpect(status().isOk());

        // Validate the Productvariante in the database
        List<Productvariante> productvarianteList = productvarianteRepository.findAll();
        assertThat(productvarianteList).hasSize(databaseSizeBeforeUpdate);
        Productvariante testProductvariante = productvarianteList.get(productvarianteList.size() - 1);
        assertThat(testProductvariante.getPicture()).isEqualTo(UPDATED_PICTURE);
        assertThat(testProductvariante.getPictureContentType()).isEqualTo(UPDATED_PICTURE_CONTENT_TYPE);
        assertThat(testProductvariante.getCodebarre()).isEqualTo(UPDATED_CODEBARRE);
        assertThat(testProductvariante.getProductCode()).isEqualTo(UPDATED_PRODUCT_CODE);
        assertThat(testProductvariante.getSalePrice()).isEqualByComparingTo(UPDATED_SALE_PRICE);
        assertThat(testProductvariante.getUniteMesure()).isEqualTo(UPDATED_UNITE_MESURE);
        assertThat(testProductvariante.getStockDisponible()).isEqualTo(UPDATED_STOCK_DISPONIBLE);
    }

    @Test
    @Transactional
    void patchNonExistingProductvariante() throws Exception {
        int databaseSizeBeforeUpdate = productvarianteRepository.findAll().size();
        productvariante.setId(count.incrementAndGet());

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restProductvarianteMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, productvariante.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(productvariante))
            )
            .andExpect(status().isBadRequest());

        // Validate the Productvariante in the database
        List<Productvariante> productvarianteList = productvarianteRepository.findAll();
        assertThat(productvarianteList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithIdMismatchProductvariante() throws Exception {
        int databaseSizeBeforeUpdate = productvarianteRepository.findAll().size();
        productvariante.setId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restProductvarianteMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, count.incrementAndGet())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(productvariante))
            )
            .andExpect(status().isBadRequest());

        // Validate the Productvariante in the database
        List<Productvariante> productvarianteList = productvarianteRepository.findAll();
        assertThat(productvarianteList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithMissingIdPathParamProductvariante() throws Exception {
        int databaseSizeBeforeUpdate = productvarianteRepository.findAll().size();
        productvariante.setId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restProductvarianteMockMvc
            .perform(
                patch(ENTITY_API_URL)
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(productvariante))
            )
            .andExpect(status().isMethodNotAllowed());

        // Validate the Productvariante in the database
        List<Productvariante> productvarianteList = productvarianteRepository.findAll();
        assertThat(productvarianteList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void deleteProductvariante() throws Exception {
        // Initialize the database
        productvarianteRepository.saveAndFlush(productvariante);

        int databaseSizeBeforeDelete = productvarianteRepository.findAll().size();

        // Delete the productvariante
        restProductvarianteMockMvc
            .perform(delete(ENTITY_API_URL_ID, productvariante.getId()).accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        List<Productvariante> productvarianteList = productvarianteRepository.findAll();
        assertThat(productvarianteList).hasSize(databaseSizeBeforeDelete - 1);
    }
}
