package com.avanquest.scibscrm.domain;

import com.avanquest.scibscrm.domain.enumeration.TransactionEtape;
import com.avanquest.scibscrm.domain.enumeration.TransactionSource;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import java.io.Serializable;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.ZonedDateTime;
import java.util.HashSet;
import java.util.Set;
import javax.persistence.*;
import javax.validation.constraints.*;
import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;
import org.hibernate.annotations.Type;

/**
 * A TransactionCRM.
 */
@Entity
@Table(name = "transaction_crm")
@Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
public class TransactionCRM implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "sequenceGenerator")
    @SequenceGenerator(name = "sequenceGenerator")
    @Column(name = "id")
    private Long id;

    @Size(max = 50)
    @Column(name = "reference", length = 50)
    private String reference;

    @NotNull
    @DecimalMin(value = "0")
    @Column(name = "montant", precision = 21, scale = 2, nullable = false)
    private BigDecimal montant;

    @Enumerated(EnumType.STRING)
    @Column(name = "transaction_etape")
    private TransactionEtape transactionEtape;

    @Column(name = "date_fin")
    private LocalDate dateFin;

    @Column(name = "transaction_recurrente")
    private Boolean transactionRecurrente;

    @Column(name = "cree_le")
    private ZonedDateTime creeLe;

    @Column(name = "dernier_update")
    private ZonedDateTime dernierUpdate;

    @Size(max = 50)
    @Column(name = "telephone", length = 50)
    private String telephone;

    @Enumerated(EnumType.STRING)
    @Column(name = "source")
    private TransactionSource source;

    @Lob
    @Type(type = "org.hibernate.type.TextType")
    @Column(name = "adresse")
    private String adresse;

    @Lob
    @Type(type = "org.hibernate.type.TextType")
    @Column(name = "notes")
    private String notes;

    @Column(name = "latitude", precision = 21, scale = 2)
    private BigDecimal latitude;

    @Column(name = "longitude", precision = 21, scale = 2)
    private BigDecimal longitude;

    @ManyToOne
    private Monnaie monnaie;

    @ManyToOne
    @JsonIgnoreProperties(value = { "utilisateur", "departement", "activityAsses", "activityEmps" }, allowSetters = true)
    private Employee chargeAffaire;

    @ManyToOne
    @JsonIgnoreProperties(value = { "commercial" }, allowSetters = true)
    private Customer client;

    @ManyToMany
    @JoinTable(
        name = "rel_transaction_crm__activite",
        joinColumns = @JoinColumn(name = "transaction_crm_id"),
        inverseJoinColumns = @JoinColumn(name = "activite_id")
    )
    @Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
    @JsonIgnoreProperties(value = { "createur", "employeeIncluses", "transactionCRMS" }, allowSetters = true)
    private Set<Activite> activites = new HashSet<>();

    // jhipster-needle-entity-add-field - JHipster will add fields here

    public Long getId() {
        return this.id;
    }

    public TransactionCRM id(Long id) {
        this.setId(id);
        return this;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getReference() {
        return this.reference;
    }

    public TransactionCRM reference(String reference) {
        this.setReference(reference);
        return this;
    }

    public void setReference(String reference) {
        this.reference = reference;
    }

    public BigDecimal getMontant() {
        return this.montant;
    }

    public TransactionCRM montant(BigDecimal montant) {
        this.setMontant(montant);
        return this;
    }

    public void setMontant(BigDecimal montant) {
        this.montant = montant;
    }

    public TransactionEtape getTransactionEtape() {
        return this.transactionEtape;
    }

    public TransactionCRM transactionEtape(TransactionEtape transactionEtape) {
        this.setTransactionEtape(transactionEtape);
        return this;
    }

    public void setTransactionEtape(TransactionEtape transactionEtape) {
        this.transactionEtape = transactionEtape;
    }

    public LocalDate getDateFin() {
        return this.dateFin;
    }

    public TransactionCRM dateFin(LocalDate dateFin) {
        this.setDateFin(dateFin);
        return this;
    }

    public void setDateFin(LocalDate dateFin) {
        this.dateFin = dateFin;
    }

    public Boolean getTransactionRecurrente() {
        return this.transactionRecurrente;
    }

    public TransactionCRM transactionRecurrente(Boolean transactionRecurrente) {
        this.setTransactionRecurrente(transactionRecurrente);
        return this;
    }

    public void setTransactionRecurrente(Boolean transactionRecurrente) {
        this.transactionRecurrente = transactionRecurrente;
    }

    public ZonedDateTime getCreeLe() {
        return this.creeLe;
    }

    public TransactionCRM creeLe(ZonedDateTime creeLe) {
        this.setCreeLe(creeLe);
        return this;
    }

    public void setCreeLe(ZonedDateTime creeLe) {
        this.creeLe = creeLe;
    }

    public ZonedDateTime getDernierUpdate() {
        return this.dernierUpdate;
    }

    public TransactionCRM dernierUpdate(ZonedDateTime dernierUpdate) {
        this.setDernierUpdate(dernierUpdate);
        return this;
    }

    public void setDernierUpdate(ZonedDateTime dernierUpdate) {
        this.dernierUpdate = dernierUpdate;
    }

    public String getTelephone() {
        return this.telephone;
    }

    public TransactionCRM telephone(String telephone) {
        this.setTelephone(telephone);
        return this;
    }

    public void setTelephone(String telephone) {
        this.telephone = telephone;
    }

    public TransactionSource getSource() {
        return this.source;
    }

    public TransactionCRM source(TransactionSource source) {
        this.setSource(source);
        return this;
    }

    public void setSource(TransactionSource source) {
        this.source = source;
    }

    public String getAdresse() {
        return this.adresse;
    }

    public TransactionCRM adresse(String adresse) {
        this.setAdresse(adresse);
        return this;
    }

    public void setAdresse(String adresse) {
        this.adresse = adresse;
    }

    public String getNotes() {
        return this.notes;
    }

    public TransactionCRM notes(String notes) {
        this.setNotes(notes);
        return this;
    }

    public void setNotes(String notes) {
        this.notes = notes;
    }

    public BigDecimal getLatitude() {
        return this.latitude;
    }

    public TransactionCRM latitude(BigDecimal latitude) {
        this.setLatitude(latitude);
        return this;
    }

    public void setLatitude(BigDecimal latitude) {
        this.latitude = latitude;
    }

    public BigDecimal getLongitude() {
        return this.longitude;
    }

    public TransactionCRM longitude(BigDecimal longitude) {
        this.setLongitude(longitude);
        return this;
    }

    public void setLongitude(BigDecimal longitude) {
        this.longitude = longitude;
    }

    public Monnaie getMonnaie() {
        return this.monnaie;
    }

    public void setMonnaie(Monnaie monnaie) {
        this.monnaie = monnaie;
    }

    public TransactionCRM monnaie(Monnaie monnaie) {
        this.setMonnaie(monnaie);
        return this;
    }

    public Employee getChargeAffaire() {
        return this.chargeAffaire;
    }

    public void setChargeAffaire(Employee employee) {
        this.chargeAffaire = employee;
    }

    public TransactionCRM chargeAffaire(Employee employee) {
        this.setChargeAffaire(employee);
        return this;
    }

    public Customer getClient() {
        return this.client;
    }

    public void setClient(Customer customer) {
        this.client = customer;
    }

    public TransactionCRM client(Customer customer) {
        this.setClient(customer);
        return this;
    }

    public Set<Activite> getActivites() {
        return this.activites;
    }

    public void setActivites(Set<Activite> activites) {
        this.activites = activites;
    }

    public TransactionCRM activites(Set<Activite> activites) {
        this.setActivites(activites);
        return this;
    }

    public TransactionCRM addActivite(Activite activite) {
        this.activites.add(activite);
        activite.getTransactionCRMS().add(this);
        return this;
    }

    public TransactionCRM removeActivite(Activite activite) {
        this.activites.remove(activite);
        activite.getTransactionCRMS().remove(this);
        return this;
    }

    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof TransactionCRM)) {
            return false;
        }
        return id != null && id.equals(((TransactionCRM) o).id);
    }

    @Override
    public int hashCode() {
        // see https://vladmihalcea.com/how-to-implement-equals-and-hashcode-using-the-jpa-entity-identifier/
        return getClass().hashCode();
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "TransactionCRM{" +
            "id=" + getId() +
            ", reference='" + getReference() + "'" +
            ", montant=" + getMontant() +
            ", transactionEtape='" + getTransactionEtape() + "'" +
            ", dateFin='" + getDateFin() + "'" +
            ", transactionRecurrente='" + getTransactionRecurrente() + "'" +
            ", creeLe='" + getCreeLe() + "'" +
            ", dernierUpdate='" + getDernierUpdate() + "'" +
            ", telephone='" + getTelephone() + "'" +
            ", source='" + getSource() + "'" +
            ", adresse='" + getAdresse() + "'" +
            ", notes='" + getNotes() + "'" +
            ", latitude=" + getLatitude() +
            ", longitude=" + getLongitude() +
            "}";
    }
}
