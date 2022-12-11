package com.avanquest.scibscrm.domain;

import com.avanquest.scibscrm.domain.enumeration.Importance;
import com.avanquest.scibscrm.domain.enumeration.TypeActivite;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import java.io.Serializable;
import java.time.Instant;
import java.time.LocalDate;
import java.util.HashSet;
import java.util.Set;
import javax.persistence.*;
import javax.validation.constraints.*;
import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;
import org.hibernate.annotations.Type;

/**
 * A Activite.
 */
@Entity
@Table(name = "activite")
@Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
public class Activite implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "sequenceGenerator")
    @SequenceGenerator(name = "sequenceGenerator")
    @Column(name = "id")
    private Long id;

    @NotNull
    @Enumerated(EnumType.STRING)
    @Column(name = "typeactivite", nullable = false)
    private TypeActivite typeactivite;

    @Column(name = "resume")
    private String resume;

    @Column(name = "date_echeance")
    private LocalDate dateEcheance;

    @Column(name = "heure_activite")
    private Instant heureActivite;

    @Enumerated(EnumType.STRING)
    @Column(name = "importance")
    private Importance importance;

    @Lob
    @Type(type = "org.hibernate.type.TextType")
    @Column(name = "note")
    private String note;

    @ManyToOne
    @JsonIgnoreProperties(value = { "utilisateur", "departement", "activityAsses", "activityEmps" }, allowSetters = true)
    private Employee createur;

    @ManyToMany
    @JoinTable(
        name = "rel_activite__employee_inclus",
        joinColumns = @JoinColumn(name = "activite_id"),
        inverseJoinColumns = @JoinColumn(name = "employee_inclus_id")
    )
    @Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
    @JsonIgnoreProperties(value = { "utilisateur", "departement", "activityAsses", "activityEmps" }, allowSetters = true)
    private Set<Employee> employeeIncluses = new HashSet<>();

    @ManyToMany(mappedBy = "activites")
    @Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
    @JsonIgnoreProperties(value = { "monnaie", "chargeAffaire", "client", "activites" }, allowSetters = true)
    private Set<TransactionCRM> transactionCRMS = new HashSet<>();

    // jhipster-needle-entity-add-field - JHipster will add fields here

    public Long getId() {
        return this.id;
    }

    public Activite id(Long id) {
        this.setId(id);
        return this;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public TypeActivite getTypeactivite() {
        return this.typeactivite;
    }

    public Activite typeactivite(TypeActivite typeactivite) {
        this.setTypeactivite(typeactivite);
        return this;
    }

    public void setTypeactivite(TypeActivite typeactivite) {
        this.typeactivite = typeactivite;
    }

    public String getResume() {
        return this.resume;
    }

    public Activite resume(String resume) {
        this.setResume(resume);
        return this;
    }

    public void setResume(String resume) {
        this.resume = resume;
    }

    public LocalDate getDateEcheance() {
        return this.dateEcheance;
    }

    public Activite dateEcheance(LocalDate dateEcheance) {
        this.setDateEcheance(dateEcheance);
        return this;
    }

    public void setDateEcheance(LocalDate dateEcheance) {
        this.dateEcheance = dateEcheance;
    }

    public Instant getHeureActivite() {
        return this.heureActivite;
    }

    public Activite heureActivite(Instant heureActivite) {
        this.setHeureActivite(heureActivite);
        return this;
    }

    public void setHeureActivite(Instant heureActivite) {
        this.heureActivite = heureActivite;
    }

    public Importance getImportance() {
        return this.importance;
    }

    public Activite importance(Importance importance) {
        this.setImportance(importance);
        return this;
    }

    public void setImportance(Importance importance) {
        this.importance = importance;
    }

    public String getNote() {
        return this.note;
    }

    public Activite note(String note) {
        this.setNote(note);
        return this;
    }

    public void setNote(String note) {
        this.note = note;
    }

    public Employee getCreateur() {
        return this.createur;
    }

    public void setCreateur(Employee employee) {
        this.createur = employee;
    }

    public Activite createur(Employee employee) {
        this.setCreateur(employee);
        return this;
    }

    public Set<Employee> getEmployeeIncluses() {
        return this.employeeIncluses;
    }

    public void setEmployeeIncluses(Set<Employee> employees) {
        this.employeeIncluses = employees;
    }

    public Activite employeeIncluses(Set<Employee> employees) {
        this.setEmployeeIncluses(employees);
        return this;
    }

    public Activite addEmployeeInclus(Employee employee) {
        this.employeeIncluses.add(employee);
        employee.getActivityEmps().add(this);
        return this;
    }

    public Activite removeEmployeeInclus(Employee employee) {
        this.employeeIncluses.remove(employee);
        employee.getActivityEmps().remove(this);
        return this;
    }

    public Set<TransactionCRM> getTransactionCRMS() {
        return this.transactionCRMS;
    }

    public void setTransactionCRMS(Set<TransactionCRM> transactionCRMS) {
        if (this.transactionCRMS != null) {
            this.transactionCRMS.forEach(i -> i.removeActivite(this));
        }
        if (transactionCRMS != null) {
            transactionCRMS.forEach(i -> i.addActivite(this));
        }
        this.transactionCRMS = transactionCRMS;
    }

    public Activite transactionCRMS(Set<TransactionCRM> transactionCRMS) {
        this.setTransactionCRMS(transactionCRMS);
        return this;
    }

    public Activite addTransactionCRM(TransactionCRM transactionCRM) {
        this.transactionCRMS.add(transactionCRM);
        transactionCRM.getActivites().add(this);
        return this;
    }

    public Activite removeTransactionCRM(TransactionCRM transactionCRM) {
        this.transactionCRMS.remove(transactionCRM);
        transactionCRM.getActivites().remove(this);
        return this;
    }

    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof Activite)) {
            return false;
        }
        return id != null && id.equals(((Activite) o).id);
    }

    @Override
    public int hashCode() {
        // see https://vladmihalcea.com/how-to-implement-equals-and-hashcode-using-the-jpa-entity-identifier/
        return getClass().hashCode();
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "Activite{" +
            "id=" + getId() +
            ", typeactivite='" + getTypeactivite() + "'" +
            ", resume='" + getResume() + "'" +
            ", dateEcheance='" + getDateEcheance() + "'" +
            ", heureActivite='" + getHeureActivite() + "'" +
            ", importance='" + getImportance() + "'" +
            ", note='" + getNote() + "'" +
            "}";
    }
}
