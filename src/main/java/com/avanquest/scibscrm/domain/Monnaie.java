package com.avanquest.scibscrm.domain;

import java.io.Serializable;
import javax.persistence.*;
import javax.validation.constraints.*;
import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;

/**
 * A Monnaie.
 */
@Entity
@Table(name = "monnaie")
@Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
public class Monnaie implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "sequenceGenerator")
    @SequenceGenerator(name = "sequenceGenerator")
    @Column(name = "id")
    private Long id;

    @Size(max = 50)
    @Column(name = "money_name", length = 50)
    private String moneyName;

    @Size(max = 50)
    @Column(name = "money_isocode", length = 50)
    private String moneyIsocode;

    // jhipster-needle-entity-add-field - JHipster will add fields here

    public Long getId() {
        return this.id;
    }

    public Monnaie id(Long id) {
        this.setId(id);
        return this;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getMoneyName() {
        return this.moneyName;
    }

    public Monnaie moneyName(String moneyName) {
        this.setMoneyName(moneyName);
        return this;
    }

    public void setMoneyName(String moneyName) {
        this.moneyName = moneyName;
    }

    public String getMoneyIsocode() {
        return this.moneyIsocode;
    }

    public Monnaie moneyIsocode(String moneyIsocode) {
        this.setMoneyIsocode(moneyIsocode);
        return this;
    }

    public void setMoneyIsocode(String moneyIsocode) {
        this.moneyIsocode = moneyIsocode;
    }

    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof Monnaie)) {
            return false;
        }
        return id != null && id.equals(((Monnaie) o).id);
    }

    @Override
    public int hashCode() {
        // see https://vladmihalcea.com/how-to-implement-equals-and-hashcode-using-the-jpa-entity-identifier/
        return getClass().hashCode();
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "Monnaie{" +
            "id=" + getId() +
            ", moneyName='" + getMoneyName() + "'" +
            ", moneyIsocode='" + getMoneyIsocode() + "'" +
            "}";
    }
}
