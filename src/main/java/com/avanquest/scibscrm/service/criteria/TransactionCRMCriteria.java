package com.avanquest.scibscrm.service.criteria;

import com.avanquest.scibscrm.domain.enumeration.TransactionEtape;
import com.avanquest.scibscrm.domain.enumeration.TransactionSource;
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
import tech.jhipster.service.filter.LocalDateFilter;
import tech.jhipster.service.filter.LongFilter;
import tech.jhipster.service.filter.StringFilter;
import tech.jhipster.service.filter.ZonedDateTimeFilter;

/**
 * Criteria class for the {@link com.avanquest.scibscrm.domain.TransactionCRM} entity. This class is used
 * in {@link com.avanquest.scibscrm.web.rest.TransactionCRMResource} to receive all the possible filtering options from
 * the Http GET request parameters.
 * For example the following could be a valid request:
 * {@code /transaction-crms?id.greaterThan=5&attr1.contains=something&attr2.specified=false}
 * As Spring is unable to properly convert the types, unless specific {@link Filter} class are used, we need to use
 * fix type specific filters.
 */
@ParameterObject
public class TransactionCRMCriteria implements Serializable, Criteria {

    /**
     * Class for filtering TransactionEtape
     */
    public static class TransactionEtapeFilter extends Filter<TransactionEtape> {

        public TransactionEtapeFilter() {}

        public TransactionEtapeFilter(TransactionEtapeFilter filter) {
            super(filter);
        }

        @Override
        public TransactionEtapeFilter copy() {
            return new TransactionEtapeFilter(this);
        }
    }

    /**
     * Class for filtering TransactionSource
     */
    public static class TransactionSourceFilter extends Filter<TransactionSource> {

        public TransactionSourceFilter() {}

        public TransactionSourceFilter(TransactionSourceFilter filter) {
            super(filter);
        }

        @Override
        public TransactionSourceFilter copy() {
            return new TransactionSourceFilter(this);
        }
    }

    private static final long serialVersionUID = 1L;

    private LongFilter id;

    private StringFilter reference;

    private BigDecimalFilter montant;

    private TransactionEtapeFilter transactionEtape;

    private LocalDateFilter dateFin;

    private BooleanFilter transactionRecurrente;

    private ZonedDateTimeFilter creeLe;

    private ZonedDateTimeFilter dernierUpdate;

    private StringFilter telephone;

    private TransactionSourceFilter source;

    private BigDecimalFilter latitude;

    private BigDecimalFilter longitude;

    private LongFilter monnaieId;

    private LongFilter chargeAffaireId;

    private LongFilter clientId;

    private LongFilter activiteId;

    private Boolean distinct;

    public TransactionCRMCriteria() {}

    public TransactionCRMCriteria(TransactionCRMCriteria other) {
        this.id = other.id == null ? null : other.id.copy();
        this.reference = other.reference == null ? null : other.reference.copy();
        this.montant = other.montant == null ? null : other.montant.copy();
        this.transactionEtape = other.transactionEtape == null ? null : other.transactionEtape.copy();
        this.dateFin = other.dateFin == null ? null : other.dateFin.copy();
        this.transactionRecurrente = other.transactionRecurrente == null ? null : other.transactionRecurrente.copy();
        this.creeLe = other.creeLe == null ? null : other.creeLe.copy();
        this.dernierUpdate = other.dernierUpdate == null ? null : other.dernierUpdate.copy();
        this.telephone = other.telephone == null ? null : other.telephone.copy();
        this.source = other.source == null ? null : other.source.copy();
        this.latitude = other.latitude == null ? null : other.latitude.copy();
        this.longitude = other.longitude == null ? null : other.longitude.copy();
        this.monnaieId = other.monnaieId == null ? null : other.monnaieId.copy();
        this.chargeAffaireId = other.chargeAffaireId == null ? null : other.chargeAffaireId.copy();
        this.clientId = other.clientId == null ? null : other.clientId.copy();
        this.activiteId = other.activiteId == null ? null : other.activiteId.copy();
        this.distinct = other.distinct;
    }

    @Override
    public TransactionCRMCriteria copy() {
        return new TransactionCRMCriteria(this);
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

    public StringFilter getReference() {
        return reference;
    }

    public StringFilter reference() {
        if (reference == null) {
            reference = new StringFilter();
        }
        return reference;
    }

    public void setReference(StringFilter reference) {
        this.reference = reference;
    }

    public BigDecimalFilter getMontant() {
        return montant;
    }

    public BigDecimalFilter montant() {
        if (montant == null) {
            montant = new BigDecimalFilter();
        }
        return montant;
    }

    public void setMontant(BigDecimalFilter montant) {
        this.montant = montant;
    }

    public TransactionEtapeFilter getTransactionEtape() {
        return transactionEtape;
    }

    public TransactionEtapeFilter transactionEtape() {
        if (transactionEtape == null) {
            transactionEtape = new TransactionEtapeFilter();
        }
        return transactionEtape;
    }

    public void setTransactionEtape(TransactionEtapeFilter transactionEtape) {
        this.transactionEtape = transactionEtape;
    }

    public LocalDateFilter getDateFin() {
        return dateFin;
    }

    public LocalDateFilter dateFin() {
        if (dateFin == null) {
            dateFin = new LocalDateFilter();
        }
        return dateFin;
    }

    public void setDateFin(LocalDateFilter dateFin) {
        this.dateFin = dateFin;
    }

    public BooleanFilter getTransactionRecurrente() {
        return transactionRecurrente;
    }

    public BooleanFilter transactionRecurrente() {
        if (transactionRecurrente == null) {
            transactionRecurrente = new BooleanFilter();
        }
        return transactionRecurrente;
    }

    public void setTransactionRecurrente(BooleanFilter transactionRecurrente) {
        this.transactionRecurrente = transactionRecurrente;
    }

    public ZonedDateTimeFilter getCreeLe() {
        return creeLe;
    }

    public ZonedDateTimeFilter creeLe() {
        if (creeLe == null) {
            creeLe = new ZonedDateTimeFilter();
        }
        return creeLe;
    }

    public void setCreeLe(ZonedDateTimeFilter creeLe) {
        this.creeLe = creeLe;
    }

    public ZonedDateTimeFilter getDernierUpdate() {
        return dernierUpdate;
    }

    public ZonedDateTimeFilter dernierUpdate() {
        if (dernierUpdate == null) {
            dernierUpdate = new ZonedDateTimeFilter();
        }
        return dernierUpdate;
    }

    public void setDernierUpdate(ZonedDateTimeFilter dernierUpdate) {
        this.dernierUpdate = dernierUpdate;
    }

    public StringFilter getTelephone() {
        return telephone;
    }

    public StringFilter telephone() {
        if (telephone == null) {
            telephone = new StringFilter();
        }
        return telephone;
    }

    public void setTelephone(StringFilter telephone) {
        this.telephone = telephone;
    }

    public TransactionSourceFilter getSource() {
        return source;
    }

    public TransactionSourceFilter source() {
        if (source == null) {
            source = new TransactionSourceFilter();
        }
        return source;
    }

    public void setSource(TransactionSourceFilter source) {
        this.source = source;
    }

    public BigDecimalFilter getLatitude() {
        return latitude;
    }

    public BigDecimalFilter latitude() {
        if (latitude == null) {
            latitude = new BigDecimalFilter();
        }
        return latitude;
    }

    public void setLatitude(BigDecimalFilter latitude) {
        this.latitude = latitude;
    }

    public BigDecimalFilter getLongitude() {
        return longitude;
    }

    public BigDecimalFilter longitude() {
        if (longitude == null) {
            longitude = new BigDecimalFilter();
        }
        return longitude;
    }

    public void setLongitude(BigDecimalFilter longitude) {
        this.longitude = longitude;
    }

    public LongFilter getMonnaieId() {
        return monnaieId;
    }

    public LongFilter monnaieId() {
        if (monnaieId == null) {
            monnaieId = new LongFilter();
        }
        return monnaieId;
    }

    public void setMonnaieId(LongFilter monnaieId) {
        this.monnaieId = monnaieId;
    }

    public LongFilter getChargeAffaireId() {
        return chargeAffaireId;
    }

    public LongFilter chargeAffaireId() {
        if (chargeAffaireId == null) {
            chargeAffaireId = new LongFilter();
        }
        return chargeAffaireId;
    }

    public void setChargeAffaireId(LongFilter chargeAffaireId) {
        this.chargeAffaireId = chargeAffaireId;
    }

    public LongFilter getClientId() {
        return clientId;
    }

    public LongFilter clientId() {
        if (clientId == null) {
            clientId = new LongFilter();
        }
        return clientId;
    }

    public void setClientId(LongFilter clientId) {
        this.clientId = clientId;
    }

    public LongFilter getActiviteId() {
        return activiteId;
    }

    public LongFilter activiteId() {
        if (activiteId == null) {
            activiteId = new LongFilter();
        }
        return activiteId;
    }

    public void setActiviteId(LongFilter activiteId) {
        this.activiteId = activiteId;
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
        final TransactionCRMCriteria that = (TransactionCRMCriteria) o;
        return (
            Objects.equals(id, that.id) &&
            Objects.equals(reference, that.reference) &&
            Objects.equals(montant, that.montant) &&
            Objects.equals(transactionEtape, that.transactionEtape) &&
            Objects.equals(dateFin, that.dateFin) &&
            Objects.equals(transactionRecurrente, that.transactionRecurrente) &&
            Objects.equals(creeLe, that.creeLe) &&
            Objects.equals(dernierUpdate, that.dernierUpdate) &&
            Objects.equals(telephone, that.telephone) &&
            Objects.equals(source, that.source) &&
            Objects.equals(latitude, that.latitude) &&
            Objects.equals(longitude, that.longitude) &&
            Objects.equals(monnaieId, that.monnaieId) &&
            Objects.equals(chargeAffaireId, that.chargeAffaireId) &&
            Objects.equals(clientId, that.clientId) &&
            Objects.equals(activiteId, that.activiteId) &&
            Objects.equals(distinct, that.distinct)
        );
    }

    @Override
    public int hashCode() {
        return Objects.hash(
            id,
            reference,
            montant,
            transactionEtape,
            dateFin,
            transactionRecurrente,
            creeLe,
            dernierUpdate,
            telephone,
            source,
            latitude,
            longitude,
            monnaieId,
            chargeAffaireId,
            clientId,
            activiteId,
            distinct
        );
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "TransactionCRMCriteria{" +
            (id != null ? "id=" + id + ", " : "") +
            (reference != null ? "reference=" + reference + ", " : "") +
            (montant != null ? "montant=" + montant + ", " : "") +
            (transactionEtape != null ? "transactionEtape=" + transactionEtape + ", " : "") +
            (dateFin != null ? "dateFin=" + dateFin + ", " : "") +
            (transactionRecurrente != null ? "transactionRecurrente=" + transactionRecurrente + ", " : "") +
            (creeLe != null ? "creeLe=" + creeLe + ", " : "") +
            (dernierUpdate != null ? "dernierUpdate=" + dernierUpdate + ", " : "") +
            (telephone != null ? "telephone=" + telephone + ", " : "") +
            (source != null ? "source=" + source + ", " : "") +
            (latitude != null ? "latitude=" + latitude + ", " : "") +
            (longitude != null ? "longitude=" + longitude + ", " : "") +
            (monnaieId != null ? "monnaieId=" + monnaieId + ", " : "") +
            (chargeAffaireId != null ? "chargeAffaireId=" + chargeAffaireId + ", " : "") +
            (clientId != null ? "clientId=" + clientId + ", " : "") +
            (activiteId != null ? "activiteId=" + activiteId + ", " : "") +
            (distinct != null ? "distinct=" + distinct + ", " : "") +
            "}";
    }
}
