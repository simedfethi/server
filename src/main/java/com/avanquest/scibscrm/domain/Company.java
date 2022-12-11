package com.avanquest.scibscrm.domain;

import java.io.Serializable;
import javax.persistence.*;
import javax.validation.constraints.*;
import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;
import org.hibernate.annotations.Type;

/**
 * A Company.
 */
@Entity
@Table(name = "company")
@Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
public class Company implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "sequenceGenerator")
    @SequenceGenerator(name = "sequenceGenerator")
    @Column(name = "id")
    private Long id;

    @Size(max = 50)
    @Column(name = "company", length = 50)
    private String company;

    @Size(max = 50)
    @Column(name = "last_name", length = 50)
    private String lastName;

    @Size(max = 50)
    @Column(name = "first_name", length = 50)
    private String firstName;

    @Size(max = 50)
    @Column(name = "email_address", length = 50)
    private String emailAddress;

    @Size(max = 50)
    @Column(name = "job_title", length = 50)
    private String jobTitle;

    @Size(max = 25)
    @Column(name = "business_phone", length = 25)
    private String businessPhone;

    @Size(max = 25)
    @Column(name = "home_phone", length = 25)
    private String homePhone;

    @Size(max = 25)
    @Column(name = "mobile_phone", length = 25)
    private String mobilePhone;

    @Size(max = 25)
    @Column(name = "fax_number", length = 25)
    private String faxNumber;

    @Lob
    @Type(type = "org.hibernate.type.TextType")
    @Column(name = "address")
    private String address;

    @Size(max = 50)
    @Column(name = "city", length = 50)
    private String city;

    @Size(max = 50)
    @Column(name = "state_province", length = 50)
    private String stateProvince;

    @Size(max = 15)
    @Column(name = "zip_postal_code", length = 15)
    private String zipPostalCode;

    @Size(max = 50)
    @Column(name = "country_region", length = 50)
    private String countryRegion;

    @Lob
    @Type(type = "org.hibernate.type.TextType")
    @Column(name = "web_page")
    private String webPage;

    @Lob
    @Type(type = "org.hibernate.type.TextType")
    @Column(name = "notes")
    private String notes;

    @Lob
    @Column(name = "attachments")
    private byte[] attachments;

    @Column(name = "attachments_content_type")
    private String attachmentsContentType;

    // jhipster-needle-entity-add-field - JHipster will add fields here

    public Long getId() {
        return this.id;
    }

    public Company id(Long id) {
        this.setId(id);
        return this;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getCompany() {
        return this.company;
    }

    public Company company(String company) {
        this.setCompany(company);
        return this;
    }

    public void setCompany(String company) {
        this.company = company;
    }

    public String getLastName() {
        return this.lastName;
    }

    public Company lastName(String lastName) {
        this.setLastName(lastName);
        return this;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public String getFirstName() {
        return this.firstName;
    }

    public Company firstName(String firstName) {
        this.setFirstName(firstName);
        return this;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public String getEmailAddress() {
        return this.emailAddress;
    }

    public Company emailAddress(String emailAddress) {
        this.setEmailAddress(emailAddress);
        return this;
    }

    public void setEmailAddress(String emailAddress) {
        this.emailAddress = emailAddress;
    }

    public String getJobTitle() {
        return this.jobTitle;
    }

    public Company jobTitle(String jobTitle) {
        this.setJobTitle(jobTitle);
        return this;
    }

    public void setJobTitle(String jobTitle) {
        this.jobTitle = jobTitle;
    }

    public String getBusinessPhone() {
        return this.businessPhone;
    }

    public Company businessPhone(String businessPhone) {
        this.setBusinessPhone(businessPhone);
        return this;
    }

    public void setBusinessPhone(String businessPhone) {
        this.businessPhone = businessPhone;
    }

    public String getHomePhone() {
        return this.homePhone;
    }

    public Company homePhone(String homePhone) {
        this.setHomePhone(homePhone);
        return this;
    }

    public void setHomePhone(String homePhone) {
        this.homePhone = homePhone;
    }

    public String getMobilePhone() {
        return this.mobilePhone;
    }

    public Company mobilePhone(String mobilePhone) {
        this.setMobilePhone(mobilePhone);
        return this;
    }

    public void setMobilePhone(String mobilePhone) {
        this.mobilePhone = mobilePhone;
    }

    public String getFaxNumber() {
        return this.faxNumber;
    }

    public Company faxNumber(String faxNumber) {
        this.setFaxNumber(faxNumber);
        return this;
    }

    public void setFaxNumber(String faxNumber) {
        this.faxNumber = faxNumber;
    }

    public String getAddress() {
        return this.address;
    }

    public Company address(String address) {
        this.setAddress(address);
        return this;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getCity() {
        return this.city;
    }

    public Company city(String city) {
        this.setCity(city);
        return this;
    }

    public void setCity(String city) {
        this.city = city;
    }

    public String getStateProvince() {
        return this.stateProvince;
    }

    public Company stateProvince(String stateProvince) {
        this.setStateProvince(stateProvince);
        return this;
    }

    public void setStateProvince(String stateProvince) {
        this.stateProvince = stateProvince;
    }

    public String getZipPostalCode() {
        return this.zipPostalCode;
    }

    public Company zipPostalCode(String zipPostalCode) {
        this.setZipPostalCode(zipPostalCode);
        return this;
    }

    public void setZipPostalCode(String zipPostalCode) {
        this.zipPostalCode = zipPostalCode;
    }

    public String getCountryRegion() {
        return this.countryRegion;
    }

    public Company countryRegion(String countryRegion) {
        this.setCountryRegion(countryRegion);
        return this;
    }

    public void setCountryRegion(String countryRegion) {
        this.countryRegion = countryRegion;
    }

    public String getWebPage() {
        return this.webPage;
    }

    public Company webPage(String webPage) {
        this.setWebPage(webPage);
        return this;
    }

    public void setWebPage(String webPage) {
        this.webPage = webPage;
    }

    public String getNotes() {
        return this.notes;
    }

    public Company notes(String notes) {
        this.setNotes(notes);
        return this;
    }

    public void setNotes(String notes) {
        this.notes = notes;
    }

    public byte[] getAttachments() {
        return this.attachments;
    }

    public Company attachments(byte[] attachments) {
        this.setAttachments(attachments);
        return this;
    }

    public void setAttachments(byte[] attachments) {
        this.attachments = attachments;
    }

    public String getAttachmentsContentType() {
        return this.attachmentsContentType;
    }

    public Company attachmentsContentType(String attachmentsContentType) {
        this.attachmentsContentType = attachmentsContentType;
        return this;
    }

    public void setAttachmentsContentType(String attachmentsContentType) {
        this.attachmentsContentType = attachmentsContentType;
    }

    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof Company)) {
            return false;
        }
        return id != null && id.equals(((Company) o).id);
    }

    @Override
    public int hashCode() {
        // see https://vladmihalcea.com/how-to-implement-equals-and-hashcode-using-the-jpa-entity-identifier/
        return getClass().hashCode();
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "Company{" +
            "id=" + getId() +
            ", company='" + getCompany() + "'" +
            ", lastName='" + getLastName() + "'" +
            ", firstName='" + getFirstName() + "'" +
            ", emailAddress='" + getEmailAddress() + "'" +
            ", jobTitle='" + getJobTitle() + "'" +
            ", businessPhone='" + getBusinessPhone() + "'" +
            ", homePhone='" + getHomePhone() + "'" +
            ", mobilePhone='" + getMobilePhone() + "'" +
            ", faxNumber='" + getFaxNumber() + "'" +
            ", address='" + getAddress() + "'" +
            ", city='" + getCity() + "'" +
            ", stateProvince='" + getStateProvince() + "'" +
            ", zipPostalCode='" + getZipPostalCode() + "'" +
            ", countryRegion='" + getCountryRegion() + "'" +
            ", webPage='" + getWebPage() + "'" +
            ", notes='" + getNotes() + "'" +
            ", attachments='" + getAttachments() + "'" +
            ", attachmentsContentType='" + getAttachmentsContentType() + "'" +
            "}";
    }
}
