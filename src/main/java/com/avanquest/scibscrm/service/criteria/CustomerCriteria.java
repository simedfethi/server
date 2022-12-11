package com.avanquest.scibscrm.service.criteria;

import com.avanquest.scibscrm.domain.enumeration.CustomerType;
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

/**
 * Criteria class for the {@link com.avanquest.scibscrm.domain.Customer} entity. This class is used
 * in {@link com.avanquest.scibscrm.web.rest.CustomerResource} to receive all the possible filtering options from
 * the Http GET request parameters.
 * For example the following could be a valid request:
 * {@code /customers?id.greaterThan=5&attr1.contains=something&attr2.specified=false}
 * As Spring is unable to properly convert the types, unless specific {@link Filter} class are used, we need to use
 * fix type specific filters.
 */
@ParameterObject
public class CustomerCriteria implements Serializable, Criteria {

    /**
     * Class for filtering CustomerType
     */
    public static class CustomerTypeFilter extends Filter<CustomerType> {

        public CustomerTypeFilter() {}

        public CustomerTypeFilter(CustomerTypeFilter filter) {
            super(filter);
        }

        @Override
        public CustomerTypeFilter copy() {
            return new CustomerTypeFilter(this);
        }
    }

    private static final long serialVersionUID = 1L;

    private LongFilter id;

    private CustomerTypeFilter customerType;

    private StringFilter company;

    private StringFilter lastName;

    private StringFilter firstName;

    private StringFilter emailAddress;

    private StringFilter jobTitle;

    private StringFilter businessPhone;

    private StringFilter homePhone;

    private StringFilter mobilePhone;

    private StringFilter faxNumber;

    private StringFilter wilaya;

    private StringFilter daira;

    private StringFilter codePostal;

    private StringFilter commune;

    private BooleanFilter dejaClient;

    private LocalDateFilter dateDerniereViste;

    private BigDecimalFilter latitude;

    private BigDecimalFilter longitude;

    private LongFilter commercialId;

    private Boolean distinct;

    public CustomerCriteria() {}

    public CustomerCriteria(CustomerCriteria other) {
        this.id = other.id == null ? null : other.id.copy();
        this.customerType = other.customerType == null ? null : other.customerType.copy();
        this.company = other.company == null ? null : other.company.copy();
        this.lastName = other.lastName == null ? null : other.lastName.copy();
        this.firstName = other.firstName == null ? null : other.firstName.copy();
        this.emailAddress = other.emailAddress == null ? null : other.emailAddress.copy();
        this.jobTitle = other.jobTitle == null ? null : other.jobTitle.copy();
        this.businessPhone = other.businessPhone == null ? null : other.businessPhone.copy();
        this.homePhone = other.homePhone == null ? null : other.homePhone.copy();
        this.mobilePhone = other.mobilePhone == null ? null : other.mobilePhone.copy();
        this.faxNumber = other.faxNumber == null ? null : other.faxNumber.copy();
        this.wilaya = other.wilaya == null ? null : other.wilaya.copy();
        this.daira = other.daira == null ? null : other.daira.copy();
        this.codePostal = other.codePostal == null ? null : other.codePostal.copy();
        this.commune = other.commune == null ? null : other.commune.copy();
        this.dejaClient = other.dejaClient == null ? null : other.dejaClient.copy();
        this.dateDerniereViste = other.dateDerniereViste == null ? null : other.dateDerniereViste.copy();
        this.latitude = other.latitude == null ? null : other.latitude.copy();
        this.longitude = other.longitude == null ? null : other.longitude.copy();
        this.commercialId = other.commercialId == null ? null : other.commercialId.copy();
        this.distinct = other.distinct;
    }

    @Override
    public CustomerCriteria copy() {
        return new CustomerCriteria(this);
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

    public CustomerTypeFilter getCustomerType() {
        return customerType;
    }

    public CustomerTypeFilter customerType() {
        if (customerType == null) {
            customerType = new CustomerTypeFilter();
        }
        return customerType;
    }

    public void setCustomerType(CustomerTypeFilter customerType) {
        this.customerType = customerType;
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

    public StringFilter getWilaya() {
        return wilaya;
    }

    public StringFilter wilaya() {
        if (wilaya == null) {
            wilaya = new StringFilter();
        }
        return wilaya;
    }

    public void setWilaya(StringFilter wilaya) {
        this.wilaya = wilaya;
    }

    public StringFilter getDaira() {
        return daira;
    }

    public StringFilter daira() {
        if (daira == null) {
            daira = new StringFilter();
        }
        return daira;
    }

    public void setDaira(StringFilter daira) {
        this.daira = daira;
    }

    public StringFilter getCodePostal() {
        return codePostal;
    }

    public StringFilter codePostal() {
        if (codePostal == null) {
            codePostal = new StringFilter();
        }
        return codePostal;
    }

    public void setCodePostal(StringFilter codePostal) {
        this.codePostal = codePostal;
    }

    public StringFilter getCommune() {
        return commune;
    }

    public StringFilter commune() {
        if (commune == null) {
            commune = new StringFilter();
        }
        return commune;
    }

    public void setCommune(StringFilter commune) {
        this.commune = commune;
    }

    public BooleanFilter getDejaClient() {
        return dejaClient;
    }

    public BooleanFilter dejaClient() {
        if (dejaClient == null) {
            dejaClient = new BooleanFilter();
        }
        return dejaClient;
    }

    public void setDejaClient(BooleanFilter dejaClient) {
        this.dejaClient = dejaClient;
    }

    public LocalDateFilter getDateDerniereViste() {
        return dateDerniereViste;
    }

    public LocalDateFilter dateDerniereViste() {
        if (dateDerniereViste == null) {
            dateDerniereViste = new LocalDateFilter();
        }
        return dateDerniereViste;
    }

    public void setDateDerniereViste(LocalDateFilter dateDerniereViste) {
        this.dateDerniereViste = dateDerniereViste;
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

    public LongFilter getCommercialId() {
        return commercialId;
    }

    public LongFilter commercialId() {
        if (commercialId == null) {
            commercialId = new LongFilter();
        }
        return commercialId;
    }

    public void setCommercialId(LongFilter commercialId) {
        this.commercialId = commercialId;
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
        final CustomerCriteria that = (CustomerCriteria) o;
        return (
            Objects.equals(id, that.id) &&
            Objects.equals(customerType, that.customerType) &&
            Objects.equals(company, that.company) &&
            Objects.equals(lastName, that.lastName) &&
            Objects.equals(firstName, that.firstName) &&
            Objects.equals(emailAddress, that.emailAddress) &&
            Objects.equals(jobTitle, that.jobTitle) &&
            Objects.equals(businessPhone, that.businessPhone) &&
            Objects.equals(homePhone, that.homePhone) &&
            Objects.equals(mobilePhone, that.mobilePhone) &&
            Objects.equals(faxNumber, that.faxNumber) &&
            Objects.equals(wilaya, that.wilaya) &&
            Objects.equals(daira, that.daira) &&
            Objects.equals(codePostal, that.codePostal) &&
            Objects.equals(commune, that.commune) &&
            Objects.equals(dejaClient, that.dejaClient) &&
            Objects.equals(dateDerniereViste, that.dateDerniereViste) &&
            Objects.equals(latitude, that.latitude) &&
            Objects.equals(longitude, that.longitude) &&
            Objects.equals(commercialId, that.commercialId) &&
            Objects.equals(distinct, that.distinct)
        );
    }

    @Override
    public int hashCode() {
        return Objects.hash(
            id,
            customerType,
            company,
            lastName,
            firstName,
            emailAddress,
            jobTitle,
            businessPhone,
            homePhone,
            mobilePhone,
            faxNumber,
            wilaya,
            daira,
            codePostal,
            commune,
            dejaClient,
            dateDerniereViste,
            latitude,
            longitude,
            commercialId,
            distinct
        );
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "CustomerCriteria{" +
            (id != null ? "id=" + id + ", " : "") +
            (customerType != null ? "customerType=" + customerType + ", " : "") +
            (company != null ? "company=" + company + ", " : "") +
            (lastName != null ? "lastName=" + lastName + ", " : "") +
            (firstName != null ? "firstName=" + firstName + ", " : "") +
            (emailAddress != null ? "emailAddress=" + emailAddress + ", " : "") +
            (jobTitle != null ? "jobTitle=" + jobTitle + ", " : "") +
            (businessPhone != null ? "businessPhone=" + businessPhone + ", " : "") +
            (homePhone != null ? "homePhone=" + homePhone + ", " : "") +
            (mobilePhone != null ? "mobilePhone=" + mobilePhone + ", " : "") +
            (faxNumber != null ? "faxNumber=" + faxNumber + ", " : "") +
            (wilaya != null ? "wilaya=" + wilaya + ", " : "") +
            (daira != null ? "daira=" + daira + ", " : "") +
            (codePostal != null ? "codePostal=" + codePostal + ", " : "") +
            (commune != null ? "commune=" + commune + ", " : "") +
            (dejaClient != null ? "dejaClient=" + dejaClient + ", " : "") +
            (dateDerniereViste != null ? "dateDerniereViste=" + dateDerniereViste + ", " : "") +
            (latitude != null ? "latitude=" + latitude + ", " : "") +
            (longitude != null ? "longitude=" + longitude + ", " : "") +
            (commercialId != null ? "commercialId=" + commercialId + ", " : "") +
            (distinct != null ? "distinct=" + distinct + ", " : "") +
            "}";
    }
}
