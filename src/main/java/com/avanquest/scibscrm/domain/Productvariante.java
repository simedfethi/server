package com.avanquest.scibscrm.domain;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import java.io.Serializable;
import java.math.BigDecimal;
import java.util.HashSet;
import java.util.Set;
import javax.persistence.*;
import javax.validation.constraints.*;
import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;

/**
 * A Productvariante.
 */
@Entity
@Table(name = "productvariante")
@Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
public class Productvariante implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "sequenceGenerator")
    @SequenceGenerator(name = "sequenceGenerator")
    @Column(name = "id")
    private Long id;

    @Lob
    @Column(name = "picture")
    private byte[] picture;

    @Column(name = "picture_content_type")
    private String pictureContentType;

    @Column(name = "codebarre")
    private String codebarre;

    @Size(max = 50)
    @Column(name = "product_code", length = 50)
    private String productCode;

    @Column(name = "sale_price", precision = 21, scale = 2)
    private BigDecimal salePrice;

    @Size(max = 50)
    @Column(name = "unite_mesure", length = 50)
    private String uniteMesure;

    @Column(name = "stock_disponible")
    private Double stockDisponible;

    @ManyToMany(mappedBy = "productvariantes")
    @Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
    @JsonIgnoreProperties(value = { "categorie", "productvariantes" }, allowSetters = true)
    private Set<Product> products = new HashSet<>();

    // jhipster-needle-entity-add-field - JHipster will add fields here

    public Long getId() {
        return this.id;
    }

    public Productvariante id(Long id) {
        this.setId(id);
        return this;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public byte[] getPicture() {
        return this.picture;
    }

    public Productvariante picture(byte[] picture) {
        this.setPicture(picture);
        return this;
    }

    public void setPicture(byte[] picture) {
        this.picture = picture;
    }

    public String getPictureContentType() {
        return this.pictureContentType;
    }

    public Productvariante pictureContentType(String pictureContentType) {
        this.pictureContentType = pictureContentType;
        return this;
    }

    public void setPictureContentType(String pictureContentType) {
        this.pictureContentType = pictureContentType;
    }

    public String getCodebarre() {
        return this.codebarre;
    }

    public Productvariante codebarre(String codebarre) {
        this.setCodebarre(codebarre);
        return this;
    }

    public void setCodebarre(String codebarre) {
        this.codebarre = codebarre;
    }

    public String getProductCode() {
        return this.productCode;
    }

    public Productvariante productCode(String productCode) {
        this.setProductCode(productCode);
        return this;
    }

    public void setProductCode(String productCode) {
        this.productCode = productCode;
    }

    public BigDecimal getSalePrice() {
        return this.salePrice;
    }

    public Productvariante salePrice(BigDecimal salePrice) {
        this.setSalePrice(salePrice);
        return this;
    }

    public void setSalePrice(BigDecimal salePrice) {
        this.salePrice = salePrice;
    }

    public String getUniteMesure() {
        return this.uniteMesure;
    }

    public Productvariante uniteMesure(String uniteMesure) {
        this.setUniteMesure(uniteMesure);
        return this;
    }

    public void setUniteMesure(String uniteMesure) {
        this.uniteMesure = uniteMesure;
    }

    public Double getStockDisponible() {
        return this.stockDisponible;
    }

    public Productvariante stockDisponible(Double stockDisponible) {
        this.setStockDisponible(stockDisponible);
        return this;
    }

    public void setStockDisponible(Double stockDisponible) {
        this.stockDisponible = stockDisponible;
    }

    public Set<Product> getProducts() {
        return this.products;
    }

    public void setProducts(Set<Product> products) {
        if (this.products != null) {
            this.products.forEach(i -> i.removeProductvariante(this));
        }
        if (products != null) {
            products.forEach(i -> i.addProductvariante(this));
        }
        this.products = products;
    }

    public Productvariante products(Set<Product> products) {
        this.setProducts(products);
        return this;
    }

    public Productvariante addProduct(Product product) {
        this.products.add(product);
        product.getProductvariantes().add(this);
        return this;
    }

    public Productvariante removeProduct(Product product) {
        this.products.remove(product);
        product.getProductvariantes().remove(this);
        return this;
    }

    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof Productvariante)) {
            return false;
        }
        return id != null && id.equals(((Productvariante) o).id);
    }

    @Override
    public int hashCode() {
        // see https://vladmihalcea.com/how-to-implement-equals-and-hashcode-using-the-jpa-entity-identifier/
        return getClass().hashCode();
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "Productvariante{" +
            "id=" + getId() +
            ", picture='" + getPicture() + "'" +
            ", pictureContentType='" + getPictureContentType() + "'" +
            ", codebarre='" + getCodebarre() + "'" +
            ", productCode='" + getProductCode() + "'" +
            ", salePrice=" + getSalePrice() +
            ", uniteMesure='" + getUniteMesure() + "'" +
            ", stockDisponible=" + getStockDisponible() +
            "}";
    }
}
