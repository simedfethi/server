package com.avanquest.scibscrm.domain;

import com.avanquest.scibscrm.domain.enumeration.Gender;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import java.io.Serializable;
import java.util.HashSet;
import java.util.Set;
import javax.persistence.*;
import javax.validation.constraints.*;
import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;

/**
 * A Employee.
 */
@Entity
@Table(name = "employee")
@Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
public class Employee implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "sequenceGenerator")
    @SequenceGenerator(name = "sequenceGenerator")
    @Column(name = "id")
    private Long id;

    @Size(max = 50)
    @Column(name = "employee_name", length = 50)
    private String employeeName;

    @NotNull
    @Enumerated(EnumType.STRING)
    @Column(name = "gender", nullable = false)
    private Gender gender;

    @NotNull
    @Column(name = "phone", nullable = false)
    private String phone;

    @Column(name = "address_line_1")
    private String addressLine1;

    @OneToOne(optional = false)
    @NotNull
    @JoinColumn(unique = true)
    private User utilisateur;

    @ManyToOne
    @JsonIgnoreProperties(value = { "entreprise" }, allowSetters = true)
    private Departement departement;

    @OneToMany(mappedBy = "createur")
    @Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
    @JsonIgnoreProperties(value = { "createur", "employeeIncluses", "transactionCRMS" }, allowSetters = true)
    private Set<Activite> activityAsses = new HashSet<>();

    @ManyToMany(mappedBy = "employeeIncluses")
    @Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
    @JsonIgnoreProperties(value = { "createur", "employeeIncluses", "transactionCRMS" }, allowSetters = true)
    private Set<Activite> activityEmps = new HashSet<>();

    // jhipster-needle-entity-add-field - JHipster will add fields here

    public Long getId() {
        return this.id;
    }

    public Employee id(Long id) {
        this.setId(id);
        return this;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getEmployeeName() {
        return this.employeeName;
    }

    public Employee employeeName(String employeeName) {
        this.setEmployeeName(employeeName);
        return this;
    }

    public void setEmployeeName(String employeeName) {
        this.employeeName = employeeName;
    }

    public Gender getGender() {
        return this.gender;
    }

    public Employee gender(Gender gender) {
        this.setGender(gender);
        return this;
    }

    public void setGender(Gender gender) {
        this.gender = gender;
    }

    public String getPhone() {
        return this.phone;
    }

    public Employee phone(String phone) {
        this.setPhone(phone);
        return this;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public String getAddressLine1() {
        return this.addressLine1;
    }

    public Employee addressLine1(String addressLine1) {
        this.setAddressLine1(addressLine1);
        return this;
    }

    public void setAddressLine1(String addressLine1) {
        this.addressLine1 = addressLine1;
    }

    public User getUtilisateur() {
        return this.utilisateur;
    }

    public void setUtilisateur(User user) {
        this.utilisateur = user;
    }

    public Employee utilisateur(User user) {
        this.setUtilisateur(user);
        return this;
    }

    public Departement getDepartement() {
        return this.departement;
    }

    public void setDepartement(Departement departement) {
        this.departement = departement;
    }

    public Employee departement(Departement departement) {
        this.setDepartement(departement);
        return this;
    }

    public Set<Activite> getActivityAsses() {
        return this.activityAsses;
    }

    public void setActivityAsses(Set<Activite> activites) {
        if (this.activityAsses != null) {
            this.activityAsses.forEach(i -> i.setCreateur(null));
        }
        if (activites != null) {
            activites.forEach(i -> i.setCreateur(this));
        }
        this.activityAsses = activites;
    }

    public Employee activityAsses(Set<Activite> activites) {
        this.setActivityAsses(activites);
        return this;
    }

    public Employee addActivityAss(Activite activite) {
        this.activityAsses.add(activite);
        activite.setCreateur(this);
        return this;
    }

    public Employee removeActivityAss(Activite activite) {
        this.activityAsses.remove(activite);
        activite.setCreateur(null);
        return this;
    }

    public Set<Activite> getActivityEmps() {
        return this.activityEmps;
    }

    public void setActivityEmps(Set<Activite> activites) {
        if (this.activityEmps != null) {
            this.activityEmps.forEach(i -> i.removeEmployeeInclus(this));
        }
        if (activites != null) {
            activites.forEach(i -> i.addEmployeeInclus(this));
        }
        this.activityEmps = activites;
    }

    public Employee activityEmps(Set<Activite> activites) {
        this.setActivityEmps(activites);
        return this;
    }

    public Employee addActivityEmp(Activite activite) {
        this.activityEmps.add(activite);
        activite.getEmployeeIncluses().add(this);
        return this;
    }

    public Employee removeActivityEmp(Activite activite) {
        this.activityEmps.remove(activite);
        activite.getEmployeeIncluses().remove(this);
        return this;
    }

    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof Employee)) {
            return false;
        }
        return id != null && id.equals(((Employee) o).id);
    }

    @Override
    public int hashCode() {
        // see https://vladmihalcea.com/how-to-implement-equals-and-hashcode-using-the-jpa-entity-identifier/
        return getClass().hashCode();
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "Employee{" +
            "id=" + getId() +
            ", employeeName='" + getEmployeeName() + "'" +
            ", gender='" + getGender() + "'" +
            ", phone='" + getPhone() + "'" +
            ", addressLine1='" + getAddressLine1() + "'" +
            "}";
    }
}
