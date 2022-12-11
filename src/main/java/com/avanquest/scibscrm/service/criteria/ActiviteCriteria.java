package com.avanquest.scibscrm.service.criteria;

import com.avanquest.scibscrm.domain.enumeration.Importance;
import com.avanquest.scibscrm.domain.enumeration.TypeActivite;
import java.io.Serializable;
import java.util.Objects;
import org.springdoc.api.annotations.ParameterObject;
import tech.jhipster.service.Criteria;
import tech.jhipster.service.filter.BooleanFilter;
import tech.jhipster.service.filter.DoubleFilter;
import tech.jhipster.service.filter.Filter;
import tech.jhipster.service.filter.FloatFilter;
import tech.jhipster.service.filter.InstantFilter;
import tech.jhipster.service.filter.IntegerFilter;
import tech.jhipster.service.filter.LocalDateFilter;
import tech.jhipster.service.filter.LongFilter;
import tech.jhipster.service.filter.StringFilter;

/**
 * Criteria class for the {@link com.avanquest.scibscrm.domain.Activite} entity. This class is used
 * in {@link com.avanquest.scibscrm.web.rest.ActiviteResource} to receive all the possible filtering options from
 * the Http GET request parameters.
 * For example the following could be a valid request:
 * {@code /activites?id.greaterThan=5&attr1.contains=something&attr2.specified=false}
 * As Spring is unable to properly convert the types, unless specific {@link Filter} class are used, we need to use
 * fix type specific filters.
 */
@ParameterObject
public class ActiviteCriteria implements Serializable, Criteria {

    /**
     * Class for filtering TypeActivite
     */
    public static class TypeActiviteFilter extends Filter<TypeActivite> {

        public TypeActiviteFilter() {}

        public TypeActiviteFilter(TypeActiviteFilter filter) {
            super(filter);
        }

        @Override
        public TypeActiviteFilter copy() {
            return new TypeActiviteFilter(this);
        }
    }

    /**
     * Class for filtering Importance
     */
    public static class ImportanceFilter extends Filter<Importance> {

        public ImportanceFilter() {}

        public ImportanceFilter(ImportanceFilter filter) {
            super(filter);
        }

        @Override
        public ImportanceFilter copy() {
            return new ImportanceFilter(this);
        }
    }

    private static final long serialVersionUID = 1L;

    private LongFilter id;

    private TypeActiviteFilter typeactivite;

    private StringFilter resume;

    private LocalDateFilter dateEcheance;

    private InstantFilter heureActivite;

    private ImportanceFilter importance;

    private LongFilter createurId;

    private LongFilter employeeInclusId;

    private LongFilter transactionCRMId;

    private Boolean distinct;

    public ActiviteCriteria() {}

    public ActiviteCriteria(ActiviteCriteria other) {
        this.id = other.id == null ? null : other.id.copy();
        this.typeactivite = other.typeactivite == null ? null : other.typeactivite.copy();
        this.resume = other.resume == null ? null : other.resume.copy();
        this.dateEcheance = other.dateEcheance == null ? null : other.dateEcheance.copy();
        this.heureActivite = other.heureActivite == null ? null : other.heureActivite.copy();
        this.importance = other.importance == null ? null : other.importance.copy();
        this.createurId = other.createurId == null ? null : other.createurId.copy();
        this.employeeInclusId = other.employeeInclusId == null ? null : other.employeeInclusId.copy();
        this.transactionCRMId = other.transactionCRMId == null ? null : other.transactionCRMId.copy();
        this.distinct = other.distinct;
    }

    @Override
    public ActiviteCriteria copy() {
        return new ActiviteCriteria(this);
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

    public TypeActiviteFilter getTypeactivite() {
        return typeactivite;
    }

    public TypeActiviteFilter typeactivite() {
        if (typeactivite == null) {
            typeactivite = new TypeActiviteFilter();
        }
        return typeactivite;
    }

    public void setTypeactivite(TypeActiviteFilter typeactivite) {
        this.typeactivite = typeactivite;
    }

    public StringFilter getResume() {
        return resume;
    }

    public StringFilter resume() {
        if (resume == null) {
            resume = new StringFilter();
        }
        return resume;
    }

    public void setResume(StringFilter resume) {
        this.resume = resume;
    }

    public LocalDateFilter getDateEcheance() {
        return dateEcheance;
    }

    public LocalDateFilter dateEcheance() {
        if (dateEcheance == null) {
            dateEcheance = new LocalDateFilter();
        }
        return dateEcheance;
    }

    public void setDateEcheance(LocalDateFilter dateEcheance) {
        this.dateEcheance = dateEcheance;
    }

    public InstantFilter getHeureActivite() {
        return heureActivite;
    }

    public InstantFilter heureActivite() {
        if (heureActivite == null) {
            heureActivite = new InstantFilter();
        }
        return heureActivite;
    }

    public void setHeureActivite(InstantFilter heureActivite) {
        this.heureActivite = heureActivite;
    }

    public ImportanceFilter getImportance() {
        return importance;
    }

    public ImportanceFilter importance() {
        if (importance == null) {
            importance = new ImportanceFilter();
        }
        return importance;
    }

    public void setImportance(ImportanceFilter importance) {
        this.importance = importance;
    }

    public LongFilter getCreateurId() {
        return createurId;
    }

    public LongFilter createurId() {
        if (createurId == null) {
            createurId = new LongFilter();
        }
        return createurId;
    }

    public void setCreateurId(LongFilter createurId) {
        this.createurId = createurId;
    }

    public LongFilter getEmployeeInclusId() {
        return employeeInclusId;
    }

    public LongFilter employeeInclusId() {
        if (employeeInclusId == null) {
            employeeInclusId = new LongFilter();
        }
        return employeeInclusId;
    }

    public void setEmployeeInclusId(LongFilter employeeInclusId) {
        this.employeeInclusId = employeeInclusId;
    }

    public LongFilter getTransactionCRMId() {
        return transactionCRMId;
    }

    public LongFilter transactionCRMId() {
        if (transactionCRMId == null) {
            transactionCRMId = new LongFilter();
        }
        return transactionCRMId;
    }

    public void setTransactionCRMId(LongFilter transactionCRMId) {
        this.transactionCRMId = transactionCRMId;
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
        final ActiviteCriteria that = (ActiviteCriteria) o;
        return (
            Objects.equals(id, that.id) &&
            Objects.equals(typeactivite, that.typeactivite) &&
            Objects.equals(resume, that.resume) &&
            Objects.equals(dateEcheance, that.dateEcheance) &&
            Objects.equals(heureActivite, that.heureActivite) &&
            Objects.equals(importance, that.importance) &&
            Objects.equals(createurId, that.createurId) &&
            Objects.equals(employeeInclusId, that.employeeInclusId) &&
            Objects.equals(transactionCRMId, that.transactionCRMId) &&
            Objects.equals(distinct, that.distinct)
        );
    }

    @Override
    public int hashCode() {
        return Objects.hash(
            id,
            typeactivite,
            resume,
            dateEcheance,
            heureActivite,
            importance,
            createurId,
            employeeInclusId,
            transactionCRMId,
            distinct
        );
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "ActiviteCriteria{" +
            (id != null ? "id=" + id + ", " : "") +
            (typeactivite != null ? "typeactivite=" + typeactivite + ", " : "") +
            (resume != null ? "resume=" + resume + ", " : "") +
            (dateEcheance != null ? "dateEcheance=" + dateEcheance + ", " : "") +
            (heureActivite != null ? "heureActivite=" + heureActivite + ", " : "") +
            (importance != null ? "importance=" + importance + ", " : "") +
            (createurId != null ? "createurId=" + createurId + ", " : "") +
            (employeeInclusId != null ? "employeeInclusId=" + employeeInclusId + ", " : "") +
            (transactionCRMId != null ? "transactionCRMId=" + transactionCRMId + ", " : "") +
            (distinct != null ? "distinct=" + distinct + ", " : "") +
            "}";
    }
}
