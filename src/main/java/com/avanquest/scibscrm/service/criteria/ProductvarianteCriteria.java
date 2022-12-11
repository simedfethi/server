package com.avanquest.scibscrm.service.criteria;

import java.io.Serializable;
import java.util.Objects;
import org.springdoc.api.annotations.ParameterObject;
import tech.jhipster.service.Criteria;
import tech.jhipster.service.filter.BigDecimalFilter;
import tech.jhipster.service.filter.BooleanFilter;
import tech.jhipster.service.filter.DoubleFilter;
import tech.jhipster.service.filter.Filter;
import tech.jhipster.service.filter.FloatFilter;
import tech.jhipster.service.filter.IntegerFilter;
import tech.jhipster.service.filter.LongFilter;
import tech.jhipster.service.filter.StringFilter;

/**
 * Criteria class for the {@link com.avanquest.scibscrm.domain.Productvariante} entity. This class is used
 * in {@link com.avanquest.scibscrm.web.rest.ProductvarianteResource} to receive all the possible filtering options from
 * the Http GET request parameters.
 * For example the following could be a valid request:
 * {@code /productvariantes?id.greaterThan=5&attr1.contains=something&attr2.specified=false}
 * As Spring is unable to properly convert the types, unless specific {@link Filter} class are used, we need to use
 * fix type specific filters.
 */
@ParameterObject
public class ProductvarianteCriteria implements Serializable, Criteria {

    private static final long serialVersionUID = 1L;

    private LongFilter id;

    private StringFilter codebarre;

    private StringFilter productCode;

    private BigDecimalFilter salePrice;

    private StringFilter uniteMesure;

    private DoubleFilter stockDisponible;

    private LongFilter productId;

    private Boolean distinct;

    public ProductvarianteCriteria() {}

    public ProductvarianteCriteria(ProductvarianteCriteria other) {
        this.id = other.id == null ? null : other.id.copy();
        this.codebarre = other.codebarre == null ? null : other.codebarre.copy();
        this.productCode = other.productCode == null ? null : other.productCode.copy();
        this.salePrice = other.salePrice == null ? null : other.salePrice.copy();
        this.uniteMesure = other.uniteMesure == null ? null : other.uniteMesure.copy();
        this.stockDisponible = other.stockDisponible == null ? null : other.stockDisponible.copy();
        this.productId = other.productId == null ? null : other.productId.copy();
        this.distinct = other.distinct;
    }

    @Override
    public ProductvarianteCriteria copy() {
        return new ProductvarianteCriteria(this);
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

    public StringFilter getCodebarre() {
        return codebarre;
    }

    public StringFilter codebarre() {
        if (codebarre == null) {
            codebarre = new StringFilter();
        }
        return codebarre;
    }

    public void setCodebarre(StringFilter codebarre) {
        this.codebarre = codebarre;
    }

    public StringFilter getProductCode() {
        return productCode;
    }

    public StringFilter productCode() {
        if (productCode == null) {
            productCode = new StringFilter();
        }
        return productCode;
    }

    public void setProductCode(StringFilter productCode) {
        this.productCode = productCode;
    }

    public BigDecimalFilter getSalePrice() {
        return salePrice;
    }

    public BigDecimalFilter salePrice() {
        if (salePrice == null) {
            salePrice = new BigDecimalFilter();
        }
        return salePrice;
    }

    public void setSalePrice(BigDecimalFilter salePrice) {
        this.salePrice = salePrice;
    }

    public StringFilter getUniteMesure() {
        return uniteMesure;
    }

    public StringFilter uniteMesure() {
        if (uniteMesure == null) {
            uniteMesure = new StringFilter();
        }
        return uniteMesure;
    }

    public void setUniteMesure(StringFilter uniteMesure) {
        this.uniteMesure = uniteMesure;
    }

    public DoubleFilter getStockDisponible() {
        return stockDisponible;
    }

    public DoubleFilter stockDisponible() {
        if (stockDisponible == null) {
            stockDisponible = new DoubleFilter();
        }
        return stockDisponible;
    }

    public void setStockDisponible(DoubleFilter stockDisponible) {
        this.stockDisponible = stockDisponible;
    }

    public LongFilter getProductId() {
        return productId;
    }

    public LongFilter productId() {
        if (productId == null) {
            productId = new LongFilter();
        }
        return productId;
    }

    public void setProductId(LongFilter productId) {
        this.productId = productId;
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
        final ProductvarianteCriteria that = (ProductvarianteCriteria) o;
        return (
            Objects.equals(id, that.id) &&
            Objects.equals(codebarre, that.codebarre) &&
            Objects.equals(productCode, that.productCode) &&
            Objects.equals(salePrice, that.salePrice) &&
            Objects.equals(uniteMesure, that.uniteMesure) &&
            Objects.equals(stockDisponible, that.stockDisponible) &&
            Objects.equals(productId, that.productId) &&
            Objects.equals(distinct, that.distinct)
        );
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, codebarre, productCode, salePrice, uniteMesure, stockDisponible, productId, distinct);
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "ProductvarianteCriteria{" +
            (id != null ? "id=" + id + ", " : "") +
            (codebarre != null ? "codebarre=" + codebarre + ", " : "") +
            (productCode != null ? "productCode=" + productCode + ", " : "") +
            (salePrice != null ? "salePrice=" + salePrice + ", " : "") +
            (uniteMesure != null ? "uniteMesure=" + uniteMesure + ", " : "") +
            (stockDisponible != null ? "stockDisponible=" + stockDisponible + ", " : "") +
            (productId != null ? "productId=" + productId + ", " : "") +
            (distinct != null ? "distinct=" + distinct + ", " : "") +
            "}";
    }
}
