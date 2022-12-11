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
 * Criteria class for the {@link com.avanquest.scibscrm.domain.Company} entity. This class is used
 * in {@link com.avanquest.scibscrm.web.rest.CompanyResource} to receive all the possible filtering options from
 * the Http GET request parameters.
 * For example the following could be a valid request:
 * {@code /companies?id.greaterThan=5&attr1.contains=something&attr2.specified=false}
 * As Spring is unable to properly convert the types, unless specific {@link Filter} class are used, we need to use
 * fix type specific filters.
 */
@ParameterObject
public class CompanyCriteria implements Serializable, Criteria {

    private static final long serialVersionUID = 1L;

    private LongFilter id;

    private StringFilter company;

    private StringFilter lastName;

    private StringFilter firstName;

    private StringFilter emailAddress;

    private StringFilter jobTitle;

    private StringFilter businessPhone;

    private StringFilter homePhone;

    private StringFilter mobilePhone;

    private StringFilter faxNumber;

    private StringFilter city;

    private StringFilter stateProvince;

    private StringFilter zipPostalCode;

    private StringFilter countryRegion;

    private Boolean distinct;

    public CompanyCriteria() {}

    public CompanyCriteria(CompanyCriteria other) {
        this.id = other.id == null ? null : other.id.copy();
        this.company = other.company == null ? null : other.company.copy();
        this.lastName = other.lastName == null ? null : other.lastName.copy();
        this.firstName = other.firstName == null ? null : other.firstName.copy();
        this.emailAddress = other.emailAddress == null ? null : other.emailAddress.copy();
        this.jobTitle = other.jobTitle == null ? null : other.jobTitle.copy();
        this.businessPhone = other.businessPhone == null ? null : other.businessPhone.copy();
        this.homePhone = other.homePhone == null ? null : other.homePhone.copy();
        this.mobilePhone = other.mobilePhone == null ? null : other.mobilePhone.copy();
        this.faxNumber = other.faxNumber == null ? null : other.faxNumber.copy();
        this.city = other.city == null ? null : other.city.copy();
        this.stateProvince = other.stateProvince == null ? null : other.stateProvince.copy();
        this.zipPostalCode = other.zipPostalCode == null ? null : other.zipPostalCode.copy();
        this.countryRegion = other.countryRegion == null ? null : other.countryRegion.copy();
        this.distinct = other.distinct;
    }

    @Override
    public CompanyCriteria copy() {
        return new CompanyCriteria(this);
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

    public StringFilter getCompany() {
        return company;
    }

    public StringFilter company() {
        if (company == null) {
            company = new StringFilter();
        }
        return company;
    }

    public void setCompany(StringFilter company) {
        this.company = company;
    }

    public StringFilter getLastName() {
        return lastName;
    }

    public StringFilter lastName() {
        if (lastName == null) {
            lastName = new StringFilter();
        }
        return lastName;
    }

    public void setLastName(StringFilter lastName) {
        this.lastName = lastName;
    }

    public StringFilter getFirstName() {
        return firstName;
    }

    public StringFilter firstName() {
        if (firstName == null) {
            firstName = new StringFilter();
        }
        return firstName;
    }

    public void setFirstName(StringFilter firstName) {
        this.firstName = firstName;
    }

    public StringFilter getEmailAddress() {
        return emailAddress;
    }

    public StringFilter emailAddress() {
        if (emailAddress == null) {
            emailAddress = new StringFilter();
        }
        return emailAddress;
    }

    public void setEmailAddress(StringFilter emailAddress) {
        this.emailAddress = emailAddress;
    }

    public StringFilter getJobTitle() {
        return jobTitle;
    }

    public StringFilter jobTitle() {
        if (jobTitle == null) {
            jobTitle = new StringFilter();
        }
        return jobTitle;
    }

    public void setJobTitle(StringFilter jobTitle) {
        this.jobTitle = jobTitle;
    }

    public StringFilter getBusinessPhone() {
        return businessPhone;
    }

    public StringFilter businessPhone() {
        if (businessPhone == null) {
            businessPhone = new StringFilter();
        }
        return businessPhone;
    }

    public void setBusinessPhone(StringFilter businessPhone) {
        this.businessPhone = businessPhone;
    }

    public StringFilter getHomePhone() {
        return homePhone;
    }

    public StringFilter homePhone() {
        if (homePhone == null) {
            homePhone = new StringFilter();
        }
        return homePhone;
    }

    public void setHomePhone(StringFilter homePhone) {
        this.homePhone = homePhone;
    }

    public StringFilter getMobilePhone() {
        return mobilePhone;
    }

    public StringFilter mobilePhone() {
        if (mobilePhone == null) {
            mobilePhone = new StringFilter();
        }
        return mobilePhone;
    }

    public void setMobilePhone(StringFilter mobilePhone) {
        this.mobilePhone = mobilePhone;
    }

    public StringFilter getFaxNumber() {
        return faxNumber;
    }

    public StringFilter faxNumber() {
        if (faxNumber == null) {
            faxNumber = new StringFilter();
        }
        return faxNumber;
    }

    public void setFaxNumber(StringFilter faxNumber) {
        this.faxNumber = faxNumber;
    }

    public StringFilter getCity() {
        return city;
    }

    public StringFilter city() {
        if (city == null) {
            city = new StringFilter();
        }
        return city;
    }

    public void setCity(StringFilter city) {
        this.city = city;
    }

    public StringFilter getStateProvince() {
        return stateProvince;
    }

    public StringFilter stateProvince() {
        if (stateProvince == null) {
            stateProvince = new StringFilter();
        }
        return stateProvince;
    }

    public void setStateProvince(StringFilter stateProvince) {
        this.stateProvince = stateProvince;
    }

    public StringFilter getZipPostalCode() {
        return zipPostalCode;
    }

    public StringFilter zipPostalCode() {
        if (zipPostalCode == null) {
            zipPostalCode = new StringFilter();
        }
        return zipPostalCode;
    }

    public void setZipPostalCode(StringFilter zipPostalCode) {
        this.zipPostalCode = zipPostalCode;
    }

    public StringFilter getCountryRegion() {
        return countryRegion;
    }

    public StringFilter countryRegion() {
        if (countryRegion == null) {
            countryRegion = new StringFilter();
        }
        return countryRegion;
    }

    public void setCountryRegion(StringFilter countryRegion) {
        this.countryRegion = countryRegion;
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
        final CompanyCriteria that = (CompanyCriteria) o;
        return (
            Objects.equals(id, that.id) &&
            Objects.equals(company, that.company) &&
            Objects.equals(lastName, that.lastName) &&
            Objects.equals(firstName, that.firstName) &&
            Objects.equals(emailAddress, that.emailAddress) &&
            Objects.equals(jobTitle, that.jobTitle) &&
            Objects.equals(businessPhone, that.businessPhone) &&
            Objects.equals(homePhone, that.homePhone) &&
            Objects.equals(mobilePhone, that.mobilePhone) &&
            Objects.equals(faxNumber, that.faxNumber) &&
            Objects.equals(city, that.city) &&
            Objects.equals(stateProvince, that.stateProvince) &&
            Objects.equals(zipPostalCode, that.zipPostalCode) &&
            Objects.equals(countryRegion, that.countryRegion) &&
            Objects.equals(distinct, that.distinct)
        );
    }

    @Override
    public int hashCode() {
        return Objects.hash(
            id,
            company,
            lastName,
            firstName,
            emailAddress,
            jobTitle,
            businessPhone,
            homePhone,
            mobilePhone,
            faxNumber,
            city,
            stateProvince,
            zipPostalCode,
            countryRegion,
            distinct
        );
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "CompanyCriteria{" +
            (id != null ? "id=" + id + ", " : "") +
            (company != null ? "company=" + company + ", " : "") +
            (lastName != null ? "lastName=" + lastName + ", " : "") +
            (firstName != null ? "firstName=" + firstName + ", " : "") +
            (emailAddress != null ? "emailAddress=" + emailAddress + ", " : "") +
            (jobTitle != null ? "jobTitle=" + jobTitle + ", " : "") +
            (businessPhone != null ? "businessPhone=" + businessPhone + ", " : "") +
            (homePhone != null ? "homePhone=" + homePhone + ", " : "") +
            (mobilePhone != null ? "mobilePhone=" + mobilePhone + ", " : "") +
            (faxNumber != null ? "faxNumber=" + faxNumber + ", " : "") +
            (city != null ? "city=" + city + ", " : "") +
            (stateProvince != null ? "stateProvince=" + stateProvince + ", " : "") +
            (zipPostalCode != null ? "zipPostalCode=" + zipPostalCode + ", " : "") +
            (countryRegion != null ? "countryRegion=" + countryRegion + ", " : "") +
            (distinct != null ? "distinct=" + distinct + ", " : "") +
            "}";
    }
}
