package com.avanquest.scibscrm.domain;

import com.avanquest.scibscrm.domain.enumeration.CustomerType;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import java.io.Serializable;
import java.math.BigDecimal;
import java.time.LocalDate;
import javax.persistence.*;
import javax.validation.constraints.*;
import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;
import org.hibernate.annotations.Type;

/**
 * A Customer.
 */
@Entity
@Table(name = "customer")
@Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
public class Customer implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "sequenceGenerator")
    @SequenceGenerator(name = "sequenceGenerator")
    @Column(name = "id")
    private Long id;

    @Enumerated(EnumType.STRING)
    @Column(name = "customer_type")
    private CustomerType customerType;

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
    @Column(name = "addresse")
    private String addresse;

    @Size(max = 50)
    @Column(name = "wilaya", length = 50)
    private String wilaya;

    @Size(max = 50)
    @Column(name = "daira", length = 50)
    private String daira;

    @Size(max = 15)
    @Column(name = "code_postal", length = 15)
    private String codePostal;

    @Size(max = 50)
    @Column(name = "commune", length = 50)
    private String commune;

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

    @Column(name = "deja_client")
    private Boolean dejaClient;

    @Column(name = "date_derniere_viste")
    private LocalDate dateDerniereViste;

    @Column(name = "latitude", precision = 21, scale = 2)
    private BigDecimal latitude;

    @Column(name = "longitude", precision = 21, scale = 2)
    private BigDecimal longitude;

    @ManyToOne
    @JsonIgnoreProperties(value = { "utilisateur", "departement", "activityAsses", "activityEmps" }, allowSetters = true)
    private Employee commercial;

    // jhipster-needle-entity-add-field - JHipster will add fields here

    public Long getId() {
        return this.id;
    }

    public Customer id(Long id) {
        this.setId(id);
        return this;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public CustomerType getCustomerType() {
        return this.customerType;
    }

    public Customer customerType(CustomerType customerType) {
        this.setCustomerType(customerType);
        return this;
    }

    public void setCustomerType(CustomerType customerType) {
        this.customerType = customerType;
    }

    public String getCompany() {
        return this.company;
    }

    public Customer company(String company) {
        this.setCompany(company);
        return this;
    }

    public void setCompany(String company) {
        this.company = company;
    }

    public String getLastName() {
        return this.lastName;
    }

    public Customer lastName(String lastName) {
        this.setLastName(lastName);
        return this;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public String getFirstName() {
        return this.firstName;
    }

    public Customer firstName(String firstName) {
        this.setFirstName(firstName);
        return this;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public String getEmailAddress() {
        return this.emailAddress;
    }

    public Customer emailAddress(String emailAddress) {
        this.setEmailAddress(emailAddress);
        return this;
    }

    public void setEmailAddress(String emailAddress) {
        this.emailAddress = emailAddress;
    }

    public String getJobTitle() {
        return this.jobTitle;
    }

    public Customer jobTitle(String jobTitle) {
        this.setJobTitle(jobTitle);
        return this;
    }

    public void setJobTitle(String jobTitle) {
        this.jobTitle = jobTitle;
    }

    public String getBusinessPhone() {
        return this.businessPhone;
    }

    public Customer businessPhone(String businessPhone) {
        this.setBusinessPhone(businessPhone);
        return this;
    }

    public void setBusinessPhone(String businessPhone) {
        this.businessPhone = businessPhone;
    }

    public String getHomePhone() {
        return this.homePhone;
    }

    public Customer homePhone(String homePhone) {
        this.setHomePhone(homePhone);
        return this;
    }

    public void setHomePhone(String homePhone) {
        this.homePhone = homePhone;
    }

    public String getMobilePhone() {
        return this.mobilePhone;
    }

    public Customer mobilePhone(String mobilePhone) {
        this.setMobilePhone(mobilePhone);
        return this;
    }

    public void setMobilePhone(String mobilePhone) {
        this.mobilePhone = mobilePhone;
    }

    public String getFaxNumber() {
        return this.faxNumber;
    }

    public Customer faxNumber(String faxNumber) {
        this.setFaxNumber(faxNumber);
        return this;
    }

    public void setFaxNumber(String faxNumber) {
        this.faxNumber = faxNumber;
    }

    public String getAddresse() {
        return this.addresse;
    }

    public Customer addresse(String addresse) {
        this.setAddresse(addresse);
        return this;
    }

    public void setAddresse(String addresse) {
        this.addresse = addresse;
    }

    public String getWilaya() {
        return this.wilaya;
    }

    public Customer wilaya(String wilaya) {
        this.setWilaya(wilaya);
        return this;
    }

    public void setWilaya(String wilaya) {
        this.wilaya = wilaya;
    }

    public String getDaira() {
        return this.daira;
    }

    public Customer daira(String daira) {
        this.setDaira(daira);
        return this;
    }

    public void setDaira(String daira) {
        this.daira = daira;
    }

    public String getCodePostal() {
        return this.codePostal;
    }

    public Customer codePostal(String codePostal) {
        this.setCodePostal(codePostal);
        return this;
    }

    public void setCodePostal(String codePostal) {
        this.codePostal = codePostal;
    }

    public String getCommune() {
        return this.commune;
    }

    public Customer commune(String commune) {
        this.setCommune(commune);
        return this;
    }

    public void setCommune(String commune) {
        this.commune = commune;
    }

    public String getWebPage() {
        return this.webPage;
    }

    public Customer webPage(String webPage) {
        this.setWebPage(webPage);
        return this;
    }

    public void setWebPage(String webPage) {
        this.webPage = webPage;
    }

    public String getNotes() {
        return this.notes;
    }

    public Customer notes(String notes) {
        this.setNotes(notes);
        return this;
    }

    public void setNotes(String notes) {
        this.notes = notes;
    }

    public byte[] getAttachments() {
        return this.attachments;
    }

    public Customer attachments(byte[] attachments) {
        this.setAttachments(attachments);
        return this;
    }

    public void setAttachments(byte[] attachments) {
        this.attachments = attachments;
    }

    public String getAttachmentsContentType() {
        return this.attachmentsContentType;
    }

    public Customer attachmentsContentType(String attachmentsContentType) {
        this.attachmentsContentType = attachmentsContentType;
        return this;
    }

    public void setAttachmentsContentType(String attachmentsContentType) {
        this.attachmentsContentType = attachmentsContentType;
    }

    public Boolean getDejaClient() {
        return this.dejaClient;
    }

    public Customer dejaClient(Boolean dejaClient) {
        this.setDejaClient(dejaClient);
        return this;
    }

    public void setDejaClient(Boolean dejaClient) {
        this.dejaClient = dejaClient;
    }

    public LocalDate getDateDerniereViste() {
        return this.dateDerniereViste;
    }

    public Customer dateDerniereViste(LocalDate dateDerniereViste) {
        this.setDateDerniereViste(dateDerniereViste);
        return this;
    }

    public void setDateDerniereViste(LocalDate dateDerniereViste) {
        this.dateDerniereViste = dateDerniereViste;
    }

    public BigDecimal getLatitude() {
        return this.latitude;
    }

    public Customer latitude(BigDecimal latitude) {
        this.setLatitude(latitude);
        return this;
    }

    public void setLatitude(BigDecimal latitude) {
        this.latitude = latitude;
    }

    public BigDecimal getLongitude() {
        return this.longitude;
    }

    public Customer longitude(BigDecimal longitude) {
        this.setLongitude(longitude);
        return this;
    }

    public void setLongitude(BigDecimal longitude) {
        this.longitude = longitude;
    }

    public Employee getCommercial() {
        return this.commercial;
    }

    public void setCommercial(Employee employee) {
        this.commercial = employee;
    }

    public Customer commercial(Employee employee) {
        this.setCommercial(employee);
        return this;
    }

    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof Customer)) {
            return false;
        }
        return id != null && id.equals(((Customer) o).id);
    }

    @Override
    public int hashCode() {
        // see https://vladmihalcea.com/how-to-implement-equals-and-hashcode-using-the-jpa-entity-identifier/
        return getClass().hashCode();
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "Customer{" +
            "id=" + getId() +
            ", customerType='" + getCustomerType() + "'" +
            ", company='" + getCompany() + "'" +
            ", lastName='" + getLastName() + "'" +
            ", firstName='" + getFirstName() + "'" +
            ", emailAddress='" + getEmailAddress() + "'" +
            ", jobTitle='" + getJobTitle() + "'" +
            ", businessPhone='" + getBusinessPhone() + "'" +
            ", homePhone='" + getHomePhone() + "'" +
            ", mobilePhone='" + getMobilePhone() + "'" +
            ", faxNumber='" + getFaxNumber() + "'" +
            ", addresse='" + getAddresse() + "'" +
            ", wilaya='" + getWilaya() + "'" +
            ", daira='" + getDaira() + "'" +
            ", codePostal='" + getCodePostal() + "'" +
            ", commune='" + getCommune() + "'" +
            ", webPage='" + getWebPage() + "'" +
            ", notes='" + getNotes() + "'" +
            ", attachments='" + getAttachments() + "'" +
            ", attachmentsContentType='" + getAttachmentsContentType() + "'" +
            ", dejaClient='" + getDejaClient() + "'" +
            ", dateDerniereViste='" + getDateDerniereViste() + "'" +
            ", latitude=" + getLatitude() +
            ", longitude=" + getLongitude() +
            "}";
    }
}
