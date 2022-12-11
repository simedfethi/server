package com.avanquest.scibscrm.service.impl;

import com.avanquest.scibscrm.domain.Company;
import com.avanquest.scibscrm.repository.CompanyRepository;
import com.avanquest.scibscrm.service.CompanyService;
import java.util.Optional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * Service Implementation for managing {@link Company}.
 */
@Service
@Transactional
public class CompanyServiceImpl implements CompanyService {

    private final Logger log = LoggerFactory.getLogger(CompanyServiceImpl.class);

    private final CompanyRepository companyRepository;

    public CompanyServiceImpl(CompanyRepository companyRepository) {
        this.companyRepository = companyRepository;
    }

    @Override
    public Company save(Company company) {
        log.debug("Request to save Company : {}", company);
        return companyRepository.save(company);
    }

    @Override
    public Company update(Company company) {
        log.debug("Request to save Company : {}", company);
        return companyRepository.save(company);
    }

    @Override
    public Optional<Company> partialUpdate(Company company) {
        log.debug("Request to partially update Company : {}", company);

        return companyRepository
            .findById(company.getId())
            .map(existingCompany -> {
                if (company.getCompany() != null) {
                    existingCompany.setCompany(company.getCompany());
                }
                if (company.getLastName() != null) {
                    existingCompany.setLastName(company.getLastName());
                }
                if (company.getFirstName() != null) {
                    existingCompany.setFirstName(company.getFirstName());
                }
                if (company.getEmailAddress() != null) {
                    existingCompany.setEmailAddress(company.getEmailAddress());
                }
                if (company.getJobTitle() != null) {
                    existingCompany.setJobTitle(company.getJobTitle());
                }
                if (company.getBusinessPhone() != null) {
                    existingCompany.setBusinessPhone(company.getBusinessPhone());
                }
                if (company.getHomePhone() != null) {
                    existingCompany.setHomePhone(company.getHomePhone());
                }
                if (company.getMobilePhone() != null) {
                    existingCompany.setMobilePhone(company.getMobilePhone());
                }
                if (company.getFaxNumber() != null) {
                    existingCompany.setFaxNumber(company.getFaxNumber());
                }
                if (company.getAddress() != null) {
                    existingCompany.setAddress(company.getAddress());
                }
                if (company.getCity() != null) {
                    existingCompany.setCity(company.getCity());
                }
                if (company.getStateProvince() != null) {
                    existingCompany.setStateProvince(company.getStateProvince());
                }
                if (company.getZipPostalCode() != null) {
                    existingCompany.setZipPostalCode(company.getZipPostalCode());
                }
                if (company.getCountryRegion() != null) {
                    existingCompany.setCountryRegion(company.getCountryRegion());
                }
                if (company.getWebPage() != null) {
                    existingCompany.setWebPage(company.getWebPage());
                }
                if (company.getNotes() != null) {
                    existingCompany.setNotes(company.getNotes());
                }
                if (company.getAttachments() != null) {
                    existingCompany.setAttachments(company.getAttachments());
                }
                if (company.getAttachmentsContentType() != null) {
                    existingCompany.setAttachmentsContentType(company.getAttachmentsContentType());
                }

                return existingCompany;
            })
            .map(companyRepository::save);
    }

    @Override
    @Transactional(readOnly = true)
    public Page<Company> findAll(Pageable pageable) {
        log.debug("Request to get all Companies");
        return companyRepository.findAll(pageable);
    }

    @Override
    @Transactional(readOnly = true)
    public Optional<Company> findOne(Long id) {
        log.debug("Request to get Company : {}", id);
        return companyRepository.findById(id);
    }

    @Override
    public void delete(Long id) {
        log.debug("Request to delete Company : {}", id);
        companyRepository.deleteById(id);
    }
}
