package com.avanquest.scibscrm.service.criteria;

import com.avanquest.scibscrm.domain.enumeration.Gender;
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
 * Criteria class for the {@link com.avanquest.scibscrm.domain.Employee} entity. This class is used
 * in {@link com.avanquest.scibscrm.web.rest.EmployeeResource} to receive all the possible filtering options from
 * the Http GET request parameters.
 * For example the following could be a valid request:
 * {@code /employees?id.greaterThan=5&attr1.contains=something&attr2.specified=false}
 * As Spring is unable to properly convert the types, unless specific {@link Filter} class are used, we need to use
 * fix type specific filters.
 */
@ParameterObject
public class EmployeeCriteria implements Serializable, Criteria {

    /**
     * Class for filtering Gender
     */
    public static class GenderFilter extends Filter<Gender> {

        public GenderFilter() {}

        public GenderFilter(GenderFilter filter) {
            super(filter);
        }

        @Override
        public GenderFilter copy() {
            return new GenderFilter(this);
        }
    }

    private static final long serialVersionUID = 1L;

    private LongFilter id;

    private StringFilter employeeName;

    private GenderFilter gender;

    private StringFilter phone;

    private StringFilter addressLine1;

    private LongFilter utilisateurId;

    private LongFilter departementId;

    private LongFilter activityAssId;

    private LongFilter activityEmpId;

    private Boolean distinct;

    public EmployeeCriteria() {}

    public EmployeeCriteria(EmployeeCriteria other) {
        this.id = other.id == null ? null : other.id.copy();
        this.employeeName = other.employeeName == null ? null : other.employeeName.copy();
        this.gender = other.gender == null ? null : other.gender.copy();
        this.phone = other.phone == null ? null : other.phone.copy();
        this.addressLine1 = other.addressLine1 == null ? null : other.addressLine1.copy();
        this.utilisateurId = other.utilisateurId == null ? null : other.utilisateurId.copy();
        this.departementId = other.departementId == null ? null : other.departementId.copy();
        this.activityAssId = other.activityAssId == null ? null : other.activityAssId.copy();
        this.activityEmpId = other.activityEmpId == null ? null : other.activityEmpId.copy();
        this.distinct = other.distinct;
    }

    @Override
    public EmployeeCriteria copy() {
        return new EmployeeCriteria(this);
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

    public StringFilter getEmployeeName() {
        return employeeName;
    }

    public StringFilter employeeName() {
        if (employeeName == null) {
            employeeName = new StringFilter();
        }
        return employeeName;
    }

    public void setEmployeeName(StringFilter employeeName) {
        this.employeeName = employeeName;
    }

    public GenderFilter getGender() {
        return gender;
    }

    public GenderFilter gender() {
        if (gender == null) {
            gender = new GenderFilter();
        }
        return gender;
    }

    public void setGender(GenderFilter gender) {
        this.gender = gender;
    }

    public StringFilter getPhone() {
        return phone;
    }

    public StringFilter phone() {
        if (phone == null) {
            phone = new StringFilter();
        }
        return phone;
    }

    public void setPhone(StringFilter phone) {
        this.phone = phone;
    }

    public StringFilter getAddressLine1() {
        return addressLine1;
    }

    public StringFilter addressLine1() {
        if (addressLine1 == null) {
            addressLine1 = new StringFilter();
        }
        return addressLine1;
    }

    public void setAddressLine1(StringFilter addressLine1) {
        this.addressLine1 = addressLine1;
    }

    public LongFilter getUtilisateurId() {
        return utilisateurId;
    }

    public LongFilter utilisateurId() {
        if (utilisateurId == null) {
            utilisateurId = new LongFilter();
        }
        return utilisateurId;
    }

    public void setUtilisateurId(LongFilter utilisateurId) {
        this.utilisateurId = utilisateurId;
    }

    public LongFilter getDepartementId() {
        return departementId;
    }

    public LongFilter departementId() {
        if (departementId == null) {
            departementId = new LongFilter();
        }
        return departementId;
    }

    public void setDepartementId(LongFilter departementId) {
        this.departementId = departementId;
    }

    public LongFilter getActivityAssId() {
        return activityAssId;
    }

    public LongFilter activityAssId() {
        if (activityAssId == null) {
            activityAssId = new LongFilter();
        }
        return activityAssId;
    }

    public void setActivityAssId(LongFilter activityAssId) {
        this.activityAssId = activityAssId;
    }

    public LongFilter getActivityEmpId() {
        return activityEmpId;
    }

    public LongFilter activityEmpId() {
        if (activityEmpId == null) {
            activityEmpId = new LongFilter();
        }
        return activityEmpId;
    }

    public void setActivityEmpId(LongFilter activityEmpId) {
        this.activityEmpId = activityEmpId;
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
        final EmployeeCriteria that = (EmployeeCriteria) o;
        return (
            Objects.equals(id, that.id) &&
            Objects.equals(employeeName, that.employeeName) &&
            Objects.equals(gender, that.gender) &&
            Objects.equals(phone, that.phone) &&
            Objects.equals(addressLine1, that.addressLine1) &&
            Objects.equals(utilisateurId, that.utilisateurId) &&
            Objects.equals(departementId, that.departementId) &&
            Objects.equals(activityAssId, that.activityAssId) &&
            Objects.equals(activityEmpId, that.activityEmpId) &&
            Objects.equals(distinct, that.distinct)
        );
    }

    @Override
    public int hashCode() {
        return Objects.hash(
            id,
            employeeName,
            gender,
            phone,
            addressLine1,
            utilisateurId,
            departementId,
            activityAssId,
            activityEmpId,
            distinct
        );
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "EmployeeCriteria{" +
            (id != null ? "id=" + id + ", " : "") +
            (employeeName != null ? "employeeName=" + employeeName + ", " : "") +
            (gender != null ? "gender=" + gender + ", " : "") +
            (phone != null ? "phone=" + phone + ", " : "") +
            (addressLine1 != null ? "addressLine1=" + addressLine1 + ", " : "") +
            (utilisateurId != null ? "utilisateurId=" + utilisateurId + ", " : "") +
            (departementId != null ? "departementId=" + departementId + ", " : "") +
            (activityAssId != null ? "activityAssId=" + activityAssId + ", " : "") +
            (activityEmpId != null ? "activityEmpId=" + activityEmpId + ", " : "") +
            (distinct != null ? "distinct=" + distinct + ", " : "") +
            "}";
    }
}
