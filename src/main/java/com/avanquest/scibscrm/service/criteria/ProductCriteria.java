package com.avanquest.scibscrm.service.criteria;

import java.io.Serializable;
import java.util.Objects;
import org.springdoc.api.annotations.ParameterObject;
import tech.jhipster.service.Criteria;
import tech.jhipster.service.filter.BooleanFilter;
import tech.jhipster.service.filter.DoubleFilter;
import tech.jhipster.service.filter.Filter;
import tech.jhipster.service.filter.FloatFilter;
import tech.jhipster.service.filter.IntegerFilter;
import tech.jhipster.service.filter.LongFilter;
import tech.jhipster.service.filter.StringFilter;

/**
 * Criteria class for the {@link com.avanquest.scibscrm.domain.Product} entity. This class is used
 * in {@link com.avanquest.scibscrm.web.rest.ProductResource} to receive all the possible filtering options from
 * the Http GET request parameters.
 * For example the following could be a valid request:
 * {@code /products?id.greaterThan=5&attr1.contains=something&attr2.specified=false}
 * As Spring is unable to properly convert the types, unless specific {@link Filter} class are used, we need to use
 * fix type specific filters.
 */
@ParameterObject
public class ProductCriteria implements Serializable, Criteria {

    private static final long serialVersionUID = 1L;

    private LongFilter id;

    private StringFilter designation;

    private LongFilter categorieId;

    private LongFilter productvarianteId;

    private Boolean distinct;

    public ProductCriteria() {}

    public ProductCriteria(ProductCriteria other) {
        this.id = other.id == null ? null : other.id.copy();
        this.designation = other.designation == null ? null : other.designation.copy();
        this.categorieId = other.categorieId == null ? null : other.categorieId.copy();
        this.productvarianteId = other.productvarianteId == null ? null : other.productvarianteId.copy();
        this.distinct = other.distinct;
    }

    @Override
    public ProductCriteria copy() {
        return new ProductCriteria(this);
    }

    public LongFilter getId() {
        return id;
    }

    public LongFilter id() {
        if (id == null) {
            id = new LongFilter();
        }
        return id;
    }

    public void setId(LongFilter id) {
        this.id = id;
    }

    public StringFilter getDesignation() {
        return designation;
    }

    public StringFilter designation() {
        if (designation == null) {
            designation = new StringFilter();
        }
        return designation;
    }

    public void setDesignation(StringFilter designation) {
        this.designation = designation;
    }

    public LongFilter getCategorieId() {
        return categorieId;
    }

    public LongFilter categorieId() {
        if (categorieId == null) {
            categorieId = new LongFilter();
        }
        return categorieId;
    }

    public void setCategorieId(LongFilter categorieId) {
        this.categorieId = categorieId;
    }

    public LongFilter getProductvarianteId() {
        return productvarianteId;
    }

    public LongFilter productvarianteId() {
        if (productvarianteId == null) {
            productvarianteId = new LongFilter();
        }
        return productvarianteId;
    }

    public void setProductvarianteId(LongFilter productvarianteId) {
        this.productvarianteId = productvarianteId;
    }

    public Boolean getDistinct() {
        return distinct;
    }

    public void setDistinct(Boolean distinct) {
        this.distinct = distinct;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        final ProductCriteria that = (ProductCriteria) o;
        return (
            Objects.equals(id, that.id) &&
            Objects.equals(designation, that.designation) &&
            Objects.equals(categorieId, that.categorieId) &&
            Objects.equals(productvarianteId, that.productvarianteId) &&
            Objects.equals(distinct, that.distinct)
        );
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, designation, categorieId, productvarianteId, distinct);
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "ProductCriteria{" +
            (id != null ? "id=" + id + ", " : "") +
            (designation != null ? "designation=" + designation + ", " : "") +
            (categorieId != null ? "categorieId=" + categorieId + ", " : "") +
            (productvarianteId != null ? "productvarianteId=" + productvarianteId + ", " : "") +
            (distinct != null ? "distinct=" + distinct + ", " : "") +
            "}";
    }
}
