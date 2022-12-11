package com.avanquest.scibscrm.service.criteria;

import java.io.Serializable;
import java.util.Objects;
import org.springdoc.api.annotations.ParameterObject;
import tech.jhipster.service.Criteria;
import tech.jhipster.service.filter.BooleanFilter;
import tech.jhipster.service.filter.DoubleFilter;
import tech.jhipster.service.filter.Filter;
import tech.jhipster.service.filter.FloatFilter;
import tech.jhipster.service.filter.IntegerFilter;
import tech.jhipster.service.filter.LongFilter;
import tech.jhipster.service.filter.StringFilter;

/**
 * Criteria class for the {@link com.avanquest.scibscrm.domain.Departement} entity. This class is used
 * in {@link com.avanquest.scibscrm.web.rest.DepartementResource} to receive all the possible filtering options from
 * the Http GET request parameters.
 * For example the following could be a valid request:
 * {@code /departements?id.greaterThan=5&attr1.contains=something&attr2.specified=false}
 * As Spring is unable to properly convert the types, unless specific {@link Filter} class are used, we need to use
 * fix type specific filters.
 */
@ParameterObject
public class DepartementCriteria implements Serializable, Criteria {

    private static final long serialVersionUID = 1L;

    private LongFilter id;

    private StringFilter departmentName;

    private StringFilter departmentCode;

    private LongFilter entrepriseId;

    private Boolean distinct;

    public DepartementCriteria() {}

    public DepartementCriteria(DepartementCriteria other) {
        this.id = other.id == null ? null : other.id.copy();
        this.departmentName = other.departmentName == null ? null : other.departmentName.copy();
        this.departmentCode = other.departmentCode == null ? null : other.departmentCode.copy();
        this.entrepriseId = other.entrepriseId == null ? null : other.entrepriseId.copy();
        this.distinct = other.distinct;
    }

    @Override
    public DepartementCriteria copy() {
        return new DepartementCriteria(this);
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

    public StringFilter getDepartmentName() {
        return departmentName;
    }

    public StringFilter departmentName() {
        if (departmentName == null) {
            departmentName = new StringFilter();
        }
        return departmentName;
    }

    public void setDepartmentName(StringFilter departmentName) {
        this.departmentName = departmentName;
    }

    public StringFilter getDepartmentCode() {
        return departmentCode;
    }

    public StringFilter departmentCode() {
        if (departmentCode == null) {
            departmentCode = new StringFilter();
        }
        return departmentCode;
    }

    public void setDepartmentCode(StringFilter departmentCode) {
        this.departmentCode = departmentCode;
    }

    public LongFilter getEntrepriseId() {
        return entrepriseId;
    }

    public LongFilter entrepriseId() {
        if (entrepriseId == null) {
            entrepriseId = new LongFilter();
        }
        return entrepriseId;
    }

    public void setEntrepriseId(LongFilter entrepriseId) {
        this.entrepriseId = entrepriseId;
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
        final DepartementCriteria that = (DepartementCriteria) o;
        return (
            Objects.equals(id, that.id) &&
            Objects.equals(departmentName, that.departmentName) &&
            Objects.equals(departmentCode, that.departmentCode) &&
            Objects.equals(entrepriseId, that.entrepriseId) &&
            Objects.equals(distinct, that.distinct)
        );
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, departmentName, departmentCode, entrepriseId, distinct);
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "DepartementCriteria{" +
            (id != null ? "id=" + id + ", " : "") +
            (departmentName != null ? "departmentName=" + departmentName + ", " : "") +
            (departmentCode != null ? "departmentCode=" + departmentCode + ", " : "") +
            (entrepriseId != null ? "entrepriseId=" + entrepriseId + ", " : "") +
            (distinct != null ? "distinct=" + distinct + ", " : "") +
            "}";
    }
}
