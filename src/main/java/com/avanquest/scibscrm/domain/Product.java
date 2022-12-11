package com.avanquest.scibscrm.domain;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import java.io.Serializable;
import java.util.HashSet;
import java.util.Set;
import javax.persistence.*;
import javax.validation.constraints.*;
import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;
import org.hibernate.annotations.Type;

/**
 * A Product.
 */
@Entity
@Table(name = "product")
@Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
public class Product implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "sequenceGenerator")
    @SequenceGenerator(name = "sequenceGenerator")
    @Column(name = "id")
    private Long id;

    @Size(max = 50)
    @Column(name = "designation", length = 50)
    private String designation;

    @Lob
    @Type(type = "org.hibernate.type.TextType")
    @Column(name = "description")
    private String description;

    @ManyToOne
    private ProductCategory categorie;

    @ManyToMany
    @JoinTable(
        name = "rel_product__productvariante",
        joinColumns = @JoinColumn(name = "product_id"),
        inverseJoinColumns = @JoinColumn(name = "productvariante_id")
    )
    @Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
    @JsonIgnoreProperties(value = { "products" }, allowSetters = true)
    private Set<Productvariante> productvariantes = new HashSet<>();

    // jhipster-needle-entity-add-field - JHipster will add fields here

    public Long getId() {
        return this.id;
    }

    public Product id(Long id) {
        this.setId(id);
        return this;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getDesignation() {
        return this.designation;
    }

    public Product designation(String designation) {
        this.setDesignation(designation);
        return this;
    }

    public void setDesignation(String designation) {
        this.designation = designation;
    }

    public String getDescription() {
        return this.description;
    }

    public Product description(String description) {
        this.setDescription(description);
        return this;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public ProductCategory getCategorie() {
        return this.categorie;
    }

    public void setCategorie(ProductCategory productCategory) {
        this.categorie = productCategory;
    }

    public Product categorie(ProductCategory productCategory) {
        this.setCategorie(productCategory);
        return this;
    }

    public Set<Productvariante> getProductvariantes() {
        return this.productvariantes;
    }

    public void setProductvariantes(Set<Productvariante> productvariantes) {
        this.productvariantes = productvariantes;
    }

    public Product productvariantes(Set<Productvariante> productvariantes) {
        this.setProductvariantes(productvariantes);
        return this;
    }

    public Product addProductvariante(Productvariante productvariante) {
        this.productvariantes.add(productvariante);
        productvariante.getProducts().add(this);
        return this;
    }

    public Product removeProductvariante(Productvariante productvariante) {
        this.productvariantes.remove(productvariante);
        productvariante.getProducts().remove(this);
        return this;
    }

    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof Product)) {
            return false;
        }
        return id != null && id.equals(((Product) o).id);
    }

    @Override
    public int hashCode() {
        // see https://vladmihalcea.com/how-to-implement-equals-and-hashcode-using-the-jpa-entity-identifier/
        return getClass().hashCode();
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "Product{" +
            "id=" + getId() +
            ", designation='" + getDesignation() + "'" +
            ", description='" + getDescription() + "'" +
            "}";
    }
}
