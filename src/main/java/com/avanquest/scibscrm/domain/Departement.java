package com.avanquest.scibscrm.domain;

import java.io.Serializable;
import javax.persistence.*;
import javax.validation.constraints.*;
import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;

/**
 * A Departement.
 */
@Entity
@Table(name = "departement")
@Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
public class Departement implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "sequenceGenerator")
    @SequenceGenerator(name = "sequenceGenerator")
    @Column(name = "id")
    private Long id;

    @Size(max = 50)
    @Column(name = "department_name", length = 50)
    private String departmentName;

    @Size(max = 10)
    @Column(name = "department_code", length = 10)
    private String departmentCode;

    @ManyToOne(optional = false)
    @NotNull
    private Company entreprise;

    // jhipster-needle-entity-add-field - JHipster will add fields here

    public Long getId() {
        return this.id;
    }

    public Departement id(Long id) {
        this.setId(id);
        return this;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getDepartmentName() {
        return this.departmentName;
    }

    public Departement departmentName(String departmentName) {
        this.setDepartmentName(departmentName);
        return this;
    }

    public void setDepartmentName(String departmentName) {
        this.departmentName = departmentName;
    }

    public String getDepartmentCode() {
        return this.departmentCode;
    }

    public Departement departmentCode(String departmentCode) {
        this.setDepartmentCode(departmentCode);
        return this;
    }

    public void setDepartmentCode(String departmentCode) {
        this.departmentCode = departmentCode;
    }

    public Company getEntreprise() {
        return this.entreprise;
    }

    public void setEntreprise(Company company) {
        this.entreprise = company;
    }

    public Departement entreprise(Company company) {
        this.setEntreprise(company);
        return this;
    }

    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof Departement)) {
            return false;
        }
        return id != null && id.equals(((Departement) o).id);
    }

    @Override
    public int hashCode() {
        // see https://vladmihalcea.com/how-to-implement-equals-and-hashcode-using-the-jpa-entity-identifier/
        return getClass().hashCode();
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "Departement{" +
            "id=" + getId() +
            ", departmentName='" + getDepartmentName() + "'" +
            ", departmentCode='" + getDepartmentCode() + "'" +
            "}";
    }
}
