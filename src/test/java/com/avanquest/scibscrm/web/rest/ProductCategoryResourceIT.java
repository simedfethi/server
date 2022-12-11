package com.avanquest.scibscrm.web.rest;

import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import com.avanquest.scibscrm.IntegrationTest;
import com.avanquest.scibscrm.domain.ProductCategory;
import com.avanquest.scibscrm.repository.ProductCategoryRepository;
import com.avanquest.scibscrm.service.criteria.ProductCategoryCriteria;
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
 * Integration tests for the {@link ProductCategoryResource} REST controller.
 */
@IntegrationTest
@AutoConfigureMockMvc
@WithMockUser
class ProductCategoryResourceIT {

    private static final String DEFAULT_CATEGORY_NAME = "AAAAAAAAAA";
    private static final String UPDATED_CATEGORY_NAME = "BBBBBBBBBB";

    private static final String DEFAULT_CATEGORY_CODE = "AAAAAAAAAA";
    private static final String UPDATED_CATEGORY_CODE = "BBBBBBBBBB";

    private static final String ENTITY_API_URL = "/api/product-categories";
    private static final String ENTITY_API_URL_ID = ENTITY_API_URL + "/{id}";

    private static Random random = new Random();
    private static AtomicLong count = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    @Autowired
    private ProductCategoryRepository productCategoryRepository;

    @Autowired
    private EntityManager em;

    @Autowired
    private MockMvc restProductCategoryMockMvc;

    private ProductCategory productCategory;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static ProductCategory createEntity(EntityManager em) {
        ProductCategory productCategory = new ProductCategory().categoryName(DEFAULT_CATEGORY_NAME).categoryCode(DEFAULT_CATEGORY_CODE);
        return productCategory;
    }

    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static ProductCategory createUpdatedEntity(EntityManager em) {
        ProductCategory productCategory = new ProductCategory().categoryName(UPDATED_CATEGORY_NAME).categoryCode(UPDATED_CATEGORY_CODE);
        return productCategory;
    }

    @BeforeEach
    public void initTest() {
        productCategory = createEntity(em);
    }

    @Test
    @Transactional
    void createProductCategory() throws Exception {
        int databaseSizeBeforeCreate = productCategoryRepository.findAll().size();
        // Create the ProductCategory
        restProductCategoryMockMvc
            .perform(
                post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(productCategory))
            )
            .andExpect(status().isCreated());

        // Validate the ProductCategory in the database
        List<ProductCategory> productCategoryList = productCategoryRepository.findAll();
        assertThat(productCategoryList).hasSize(databaseSizeBeforeCreate + 1);
        ProductCategory testProductCategory = productCategoryList.get(productCategoryList.size() - 1);
        assertThat(testProductCategory.getCategoryName()).isEqualTo(DEFAULT_CATEGORY_NAME);
        assertThat(testProductCategory.getCategoryCode()).isEqualTo(DEFAULT_CATEGORY_CODE);
    }

    @Test
    @Transactional
    void createProductCategoryWithExistingId() throws Exception {
        // Create the ProductCategory with an existing ID
        productCategory.setId(1L);

        int databaseSizeBeforeCreate = productCategoryRepository.findAll().size();

        // An entity with an existing ID cannot be created, so this API call must fail
        restProductCategoryMockMvc
            .perform(
                post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(productCategory))
            )
            .andExpect(status().isBadRequest());

        // Validate the ProductCategory in the database
        List<ProductCategory> productCategoryList = productCategoryRepository.findAll();
        assertThat(productCategoryList).hasSize(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    void getAllProductCategories() throws Exception {
        // Initialize the database
        productCategoryRepository.saveAndFlush(productCategory);

        // Get all the productCategoryList
        restProductCategoryMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(productCategory.getId().intValue())))
            .andExpect(jsonPath("$.[*].categoryName").value(hasItem(DEFAULT_CATEGORY_NAME)))
            .andExpect(jsonPath("$.[*].categoryCode").value(hasItem(DEFAULT_CATEGORY_CODE)));
    }

    @Test
    @Transactional
    void getProductCategory() throws Exception {
        // Initialize the database
        productCategoryRepository.saveAndFlush(productCategory);

        // Get the productCategory
        restProductCategoryMockMvc
            .perform(get(ENTITY_API_URL_ID, productCategory.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.id").value(productCategory.getId().intValue()))
            .andExpect(jsonPath("$.categoryName").value(DEFAULT_CATEGORY_NAME))
            .andExpect(jsonPath("$.categoryCode").value(DEFAULT_CATEGORY_CODE));
    }

    @Test
    @Transactional
    void getProductCategoriesByIdFiltering() throws Exception {
        // Initialize the database
        productCategoryRepository.saveAndFlush(productCategory);

        Long id = productCategory.getId();

        defaultProductCategoryShouldBeFound("id.equals=" + id);
        defaultProductCategoryShouldNotBeFound("id.notEquals=" + id);

        defaultProductCategoryShouldBeFound("id.greaterThanOrEqual=" + id);
        defaultProductCategoryShouldNotBeFound("id.greaterThan=" + id);

        defaultProductCategoryShouldBeFound("id.lessThanOrEqual=" + id);
        defaultProductCategoryShouldNotBeFound("id.lessThan=" + id);
    }

    @Test
    @Transactional
    void getAllProductCategoriesByCategoryNameIsEqualToSomething() throws Exception {
        // Initialize the database
        productCategoryRepository.saveAndFlush(productCategory);

        // Get all the productCategoryList where categoryName equals to DEFAULT_CATEGORY_NAME
        defaultProductCategoryShouldBeFound("categoryName.equals=" + DEFAULT_CATEGORY_NAME);

        // Get all the productCategoryList where categoryName equals to UPDATED_CATEGORY_NAME
        defaultProductCategoryShouldNotBeFound("categoryName.equals=" + UPDATED_CATEGORY_NAME);
    }

    @Test
    @Transactional
    void getAllProductCategoriesByCategoryNameIsNotEqualToSomething() throws Exception {
        // Initialize the database
        productCategoryRepository.saveAndFlush(productCategory);

        // Get all the productCategoryList where categoryName not equals to DEFAULT_CATEGORY_NAME
        defaultProductCategoryShouldNotBeFound("categoryName.notEquals=" + DEFAULT_CATEGORY_NAME);

        // Get all the productCategoryList where categoryName not equals to UPDATED_CATEGORY_NAME
        defaultProductCategoryShouldBeFound("categoryName.notEquals=" + UPDATED_CATEGORY_NAME);
    }

    @Test
    @Transactional
    void getAllProductCategoriesByCategoryNameIsInShouldWork() throws Exception {
        // Initialize the database
        productCategoryRepository.saveAndFlush(productCategory);

        // Get all the productCategoryList where categoryName in DEFAULT_CATEGORY_NAME or UPDATED_CATEGORY_NAME
        defaultProductCategoryShouldBeFound("categoryName.in=" + DEFAULT_CATEGORY_NAME + "," + UPDATED_CATEGORY_NAME);

        // Get all the productCategoryList where categoryName equals to UPDATED_CATEGORY_NAME
        defaultProductCategoryShouldNotBeFound("categoryName.in=" + UPDATED_CATEGORY_NAME);
    }

    @Test
    @Transactional
    void getAllProductCategoriesByCategoryNameIsNullOrNotNull() throws Exception {
        // Initialize the database
        productCategoryRepository.saveAndFlush(productCategory);

        // Get all the productCategoryList where categoryName is not null
        defaultProductCategoryShouldBeFound("categoryName.specified=true");

        // Get all the productCategoryList where categoryName is null
        defaultProductCategoryShouldNotBeFound("categoryName.specified=false");
    }

    @Test
    @Transactional
    void getAllProductCategoriesByCategoryNameContainsSomething() throws Exception {
        // Initialize the database
        productCategoryRepository.saveAndFlush(productCategory);

        // Get all the productCategoryList where categoryName contains DEFAULT_CATEGORY_NAME
        defaultProductCategoryShouldBeFound("categoryName.contains=" + DEFAULT_CATEGORY_NAME);

        // Get all the productCategoryList where categoryName contains UPDATED_CATEGORY_NAME
        defaultProductCategoryShouldNotBeFound("categoryName.contains=" + UPDATED_CATEGORY_NAME);
    }

    @Test
    @Transactional
    void getAllProductCategoriesByCategoryNameNotContainsSomething() throws Exception {
        // Initialize the database
        productCategoryRepository.saveAndFlush(productCategory);

        // Get all the productCategoryList where categoryName does not contain DEFAULT_CATEGORY_NAME
        defaultProductCategoryShouldNotBeFound("categoryName.doesNotContain=" + DEFAULT_CATEGORY_NAME);

        // Get all the productCategoryList where categoryName does not contain UPDATED_CATEGORY_NAME
        defaultProductCategoryShouldBeFound("categoryName.doesNotContain=" + UPDATED_CATEGORY_NAME);
    }

    @Test
    @Transactional
    void getAllProductCategoriesByCategoryCodeIsEqualToSomething() throws Exception {
        // Initialize the database
        productCategoryRepository.saveAndFlush(productCategory);

        // Get all the productCategoryList where categoryCode equals to DEFAULT_CATEGORY_CODE
        defaultProductCategoryShouldBeFound("categoryCode.equals=" + DEFAULT_CATEGORY_CODE);

        // Get all the productCategoryList where categoryCode equals to UPDATED_CATEGORY_CODE
        defaultProductCategoryShouldNotBeFound("categoryCode.equals=" + UPDATED_CATEGORY_CODE);
    }

    @Test
    @Transactional
    void getAllProductCategoriesByCategoryCodeIsNotEqualToSomething() throws Exception {
        // Initialize the database
        productCategoryRepository.saveAndFlush(productCategory);

        // Get all the productCategoryList where categoryCode not equals to DEFAULT_CATEGORY_CODE
        defaultProductCategoryShouldNotBeFound("categoryCode.notEquals=" + DEFAULT_CATEGORY_CODE);

        // Get all the productCategoryList where categoryCode not equals to UPDATED_CATEGORY_CODE
        defaultProductCategoryShouldBeFound("categoryCode.notEquals=" + UPDATED_CATEGORY_CODE);
    }

    @Test
    @Transactional
    void getAllProductCategoriesByCategoryCodeIsInShouldWork() throws Exception {
        // Initialize the database
        productCategoryRepository.saveAndFlush(productCategory);

        // Get all the productCategoryList where categoryCode in DEFAULT_CATEGORY_CODE or UPDATED_CATEGORY_CODE
        defaultProductCategoryShouldBeFound("categoryCode.in=" + DEFAULT_CATEGORY_CODE + "," + UPDATED_CATEGORY_CODE);

        // Get all the productCategoryList where categoryCode equals to UPDATED_CATEGORY_CODE
        defaultProductCategoryShouldNotBeFound("categoryCode.in=" + UPDATED_CATEGORY_CODE);
    }

    @Test
    @Transactional
    void getAllProductCategoriesByCategoryCodeIsNullOrNotNull() throws Exception {
        // Initialize the database
        productCategoryRepository.saveAndFlush(productCategory);

        // Get all the productCategoryList where categoryCode is not null
        defaultProductCategoryShouldBeFound("categoryCode.specified=true");

        // Get all the productCategoryList where categoryCode is null
        defaultProductCategoryShouldNotBeFound("categoryCode.specified=false");
    }

    @Test
    @Transactional
    void getAllProductCategoriesByCategoryCodeContainsSomething() throws Exception {
        // Initialize the database
        productCategoryRepository.saveAndFlush(productCategory);

        // Get all the productCategoryList where categoryCode contains DEFAULT_CATEGORY_CODE
        defaultProductCategoryShouldBeFound("categoryCode.contains=" + DEFAULT_CATEGORY_CODE);

        // Get all the productCategoryList where categoryCode contains UPDATED_CATEGORY_CODE
        defaultProductCategoryShouldNotBeFound("categoryCode.contains=" + UPDATED_CATEGORY_CODE);
    }

    @Test
    @Transactional
    void getAllProductCategoriesByCategoryCodeNotContainsSomething() throws Exception {
        // Initialize the database
        productCategoryRepository.saveAndFlush(productCategory);

        // Get all the productCategoryList where categoryCode does not contain DEFAULT_CATEGORY_CODE
        defaultProductCategoryShouldNotBeFound("categoryCode.doesNotContain=" + DEFAULT_CATEGORY_CODE);

        // Get all the productCategoryList where categoryCode does not contain UPDATED_CATEGORY_CODE
        defaultProductCategoryShouldBeFound("categoryCode.doesNotContain=" + UPDATED_CATEGORY_CODE);
    }

    /**
     * Executes the search, and checks that the default entity is returned.
     */
    private void defaultProductCategoryShouldBeFound(String filter) throws Exception {
        restProductCategoryMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(productCategory.getId().intValue())))
            .andExpect(jsonPath("$.[*].categoryName").value(hasItem(DEFAULT_CATEGORY_NAME)))
            .andExpect(jsonPath("$.[*].categoryCode").value(hasItem(DEFAULT_CATEGORY_CODE)));

        // Check, that the count call also returns 1
        restProductCategoryMockMvc
            .perform(get(ENTITY_API_URL + "/count?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(content().string("1"));
    }

    /**
     * Executes the search, and checks that the default entity is not returned.
     */
    private void defaultProductCategoryShouldNotBeFound(String filter) throws Exception {
        restProductCategoryMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$").isArray())
            .andExpect(jsonPath("$").isEmpty());

        // Check, that the count call also returns 0
        restProductCategoryMockMvc
            .perform(get(ENTITY_API_URL + "/count?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(content().string("0"));
    }

    @Test
    @Transactional
    void getNonExistingProductCategory() throws Exception {
        // Get the productCategory
        restProductCategoryMockMvc.perform(get(ENTITY_API_URL_ID, Long.MAX_VALUE)).andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    void putNewProductCategory() throws Exception {
        // Initialize the database
        productCategoryRepository.saveAndFlush(productCategory);

        int databaseSizeBeforeUpdate = productCategoryRepository.findAll().size();

        // Update the productCategory
        ProductCategory updatedProductCategory = productCategoryRepository.findById(productCategory.getId()).get();
        // Disconnect from session so that the updates on updatedProductCategory are not directly saved in db
        em.detach(updatedProductCategory);
        updatedProductCategory.categoryName(UPDATED_CATEGORY_NAME).categoryCode(UPDATED_CATEGORY_CODE);

        restProductCategoryMockMvc
            .perform(
                put(ENTITY_API_URL_ID, updatedProductCategory.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(updatedProductCategory))
            )
            .andExpect(status().isOk());

        // Validate the ProductCategory in the database
        List<ProductCategory> productCategoryList = productCategoryRepository.findAll();
        assertThat(productCategoryList).hasSize(databaseSizeBeforeUpdate);
        ProductCategory testProductCategory = productCategoryList.get(productCategoryList.size() - 1);
        assertThat(testProductCategory.getCategoryName()).isEqualTo(UPDATED_CATEGORY_NAME);
        assertThat(testProductCategory.getCategoryCode()).isEqualTo(UPDATED_CATEGORY_CODE);
    }

    @Test
    @Transactional
    void putNonExistingProductCategory() throws Exception {
        int databaseSizeBeforeUpdate = productCategoryRepository.findAll().size();
        productCategory.setId(count.incrementAndGet());

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restProductCategoryMockMvc
            .perform(
                put(ENTITY_API_URL_ID, productCategory.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(productCategory))
            )
            .andExpect(status().isBadRequest());

        // Validate the ProductCategory in the database
        List<ProductCategory> productCategoryList = productCategoryRepository.findAll();
        assertThat(productCategoryList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithIdMismatchProductCategory() throws Exception {
        int databaseSizeBeforeUpdate = productCategoryRepository.findAll().size();
        productCategory.setId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restProductCategoryMockMvc
            .perform(
                put(ENTITY_API_URL_ID, count.incrementAndGet())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(productCategory))
            )
            .andExpect(status().isBadRequest());

        // Validate the ProductCategory in the database
        List<ProductCategory> productCategoryList = productCategoryRepository.findAll();
        assertThat(productCategoryList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithMissingIdPathParamProductCategory() throws Exception {
        int databaseSizeBeforeUpdate = productCategoryRepository.findAll().size();
        productCategory.setId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restProductCategoryMockMvc
            .perform(
                put(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(productCategory))
            )
            .andExpect(status().isMethodNotAllowed());

        // Validate the ProductCategory in the database
        List<ProductCategory> productCategoryList = productCategoryRepository.findAll();
        assertThat(productCategoryList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void partialUpdateProductCategoryWithPatch() throws Exception {
        // Initialize the database
        productCategoryRepository.saveAndFlush(productCategory);

        int databaseSizeBeforeUpdate = productCategoryRepository.findAll().size();

        // Update the productCategory using partial update
        ProductCategory partialUpdatedProductCategory = new ProductCategory();
        partialUpdatedProductCategory.setId(productCategory.getId());

        partialUpdatedProductCategory.categoryName(UPDATED_CATEGORY_NAME);

        restProductCategoryMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedProductCategory.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedProductCategory))
            )
            .andExpect(status().isOk());

        // Validate the ProductCategory in the database
        List<ProductCategory> productCategoryList = productCategoryRepository.findAll();
        assertThat(productCategoryList).hasSize(databaseSizeBeforeUpdate);
        ProductCategory testProductCategory = productCategoryList.get(productCategoryList.size() - 1);
        assertThat(testProductCategory.getCategoryName()).isEqualTo(UPDATED_CATEGORY_NAME);
        assertThat(testProductCategory.getCategoryCode()).isEqualTo(DEFAULT_CATEGORY_CODE);
    }

    @Test
    @Transactional
    void fullUpdateProductCategoryWithPatch() throws Exception {
        // Initialize the database
        productCategoryRepository.saveAndFlush(productCategory);

        int databaseSizeBeforeUpdate = productCategoryRepository.findAll().size();

        // Update the productCategory using partial update
        ProductCategory partialUpdatedProductCategory = new ProductCategory();
        partialUpdatedProductCategory.setId(productCategory.getId());

        partialUpdatedProductCategory.categoryName(UPDATED_CATEGORY_NAME).categoryCode(UPDATED_CATEGORY_CODE);

        restProductCategoryMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedProductCategory.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedProductCategory))
            )
            .andExpect(status().isOk());

        // Validate the ProductCategory in the database
        List<ProductCategory> productCategoryList = productCategoryRepository.findAll();
        assertThat(productCategoryList).hasSize(databaseSizeBeforeUpdate);
        ProductCategory testProductCategory = productCategoryList.get(productCategoryList.size() - 1);
        assertThat(testProductCategory.getCategoryName()).isEqualTo(UPDATED_CATEGORY_NAME);
        assertThat(testProductCategory.getCategoryCode()).isEqualTo(UPDATED_CATEGORY_CODE);
    }

    @Test
    @Transactional
    void patchNonExistingProductCategory() throws Exception {
        int databaseSizeBeforeUpdate = productCategoryRepository.findAll().size();
        productCategory.setId(count.incrementAndGet());

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restProductCategoryMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, productCategory.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(productCategory))
            )
            .andExpect(status().isBadRequest());

        // Validate the ProductCategory in the database
        List<ProductCategory> productCategoryList = productCategoryRepository.findAll();
        assertThat(productCategoryList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithIdMismatchProductCategory() throws Exception {
        int databaseSizeBeforeUpdate = productCategoryRepository.findAll().size();
        productCategory.setId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restProductCategoryMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, count.incrementAndGet())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(productCategory))
            )
            .andExpect(status().isBadRequest());

        // Validate the ProductCategory in the database
        List<ProductCategory> productCategoryList = productCategoryRepository.findAll();
        assertThat(productCategoryList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithMissingIdPathParamProductCategory() throws Exception {
        int databaseSizeBeforeUpdate = productCategoryRepository.findAll().size();
        productCategory.setId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restProductCategoryMockMvc
            .perform(
                patch(ENTITY_API_URL)
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(productCategory))
            )
            .andExpect(status().isMethodNotAllowed());

        // Validate the ProductCategory in the database
        List<ProductCategory> productCategoryList = productCategoryRepository.findAll();
        assertThat(productCategoryList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void deleteProductCategory() throws Exception {
        // Initialize the database
        productCategoryRepository.saveAndFlush(productCategory);

        int databaseSizeBeforeDelete = productCategoryRepository.findAll().size();

        // Delete the productCategory
        restProductCategoryMockMvc
            .perform(delete(ENTITY_API_URL_ID, productCategory.getId()).accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        List<ProductCategory> productCategoryList = productCategoryRepository.findAll();
        assertThat(productCategoryList).hasSize(databaseSizeBeforeDelete - 1);
    }
}
