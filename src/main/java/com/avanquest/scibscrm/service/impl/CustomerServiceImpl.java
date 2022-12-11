package com.avanquest.scibscrm.service.impl;

import com.avanquest.scibscrm.domain.Customer;
import com.avanquest.scibscrm.repository.CustomerRepository;
import com.avanquest.scibscrm.service.CustomerService;
import java.util.Optional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * Service Implementation for managing {@link Customer}.
 */
@Service
@Transactional
public class CustomerServiceImpl implements CustomerService {

    private final Logger log = LoggerFactory.getLogger(CustomerServiceImpl.class);

    private final CustomerRepository customerRepository;

    public CustomerServiceImpl(CustomerRepository customerRepository) {
        this.customerRepository = customerRepository;
    }

    @Override
    public Customer save(Customer customer) {
        log.debug("Request to save Customer : {}", customer);
        return customerRepository.save(customer);
    }

    @Override
    public Customer update(Customer customer) {
        log.debug("Request to save Customer : {}", customer);
        return customerRepository.save(customer);
    }

    @Override
    public Optional<Customer> partialUpdate(Customer customer) {
        log.debug("Request to partially update Customer : {}", customer);

        return customerRepository
            .findById(customer.getId())
            .map(existingCustomer -> {
                if (customer.getCustomerType() != null) {
                    existingCustomer.setCustomerType(customer.getCustomerType());
                }
                if (customer.getCompany() != null) {
                    existingCustomer.setCompany(customer.getCompany());
                }
                if (customer.getLastName() != null) {
                    existingCustomer.setLastName(customer.getLastName());
                }
                if (customer.getFirstName() != null) {
                    existingCustomer.setFirstName(customer.getFirstName());
                }
                if (customer.getEmailAddress() != null) {
                    existingCustomer.setEmailAddress(customer.getEmailAddress());
                }
                if (customer.getJobTitle() != null) {
                    existingCustomer.setJobTitle(customer.getJobTitle());
                }
                if (customer.getBusinessPhone() != null) {
                    existingCustomer.setBusinessPhone(customer.getBusinessPhone());
                }
                if (customer.getHomePhone() != null) {
                    existingCustomer.setHomePhone(customer.getHomePhone());
                }
                if (customer.getMobilePhone() != null) {
                    existingCustomer.setMobilePhone(customer.getMobilePhone());
                }
                if (customer.getFaxNumber() != null) {
                    existingCustomer.setFaxNumber(customer.getFaxNumber());
                }
                if (customer.getAddresse() != null) {
                    existingCustomer.setAddresse(customer.getAddresse());
                }
                if (customer.getWilaya() != null) {
                    existingCustomer.setWilaya(customer.getWilaya());
                }
                if (customer.getDaira() != null) {
                    existingCustomer.setDaira(customer.getDaira());
                }
                if (customer.getCodePostal() != null) {
                    existingCustomer.setCodePostal(customer.getCodePostal());
                }
                if (customer.getCommune() != null) {
                    existingCustomer.setCommune(customer.getCommune());
                }
                if (customer.getWebPage() != null) {
                    existingCustomer.setWebPage(customer.getWebPage());
                }
                if (customer.getNotes() != null) {
                    existingCustomer.setNotes(customer.getNotes());
                }
                if (customer.getAttachments() != null) {
                    existingCustomer.setAttachments(customer.getAttachments());
                }
                if (customer.getAttachmentsContentType() != null) {
                    existingCustomer.setAttachmentsContentType(customer.getAttachmentsContentType());
                }
                if (customer.getDejaClient() != null) {
                    existingCustomer.setDejaClient(customer.getDejaClient());
                }
                if (customer.getDateDerniereViste() != null) {
                    existingCustomer.setDateDerniereViste(customer.getDateDerniereViste());
                }
                if (customer.getLatitude() != null) {
                    existingCustomer.setLatitude(customer.getLatitude());
                }
                if (customer.getLongitude() != null) {
                    existingCustomer.setLongitude(customer.getLongitude());
                }

                return existingCustomer;
            })
            .map(customerRepository::save);
    }

    @Override
    @Transactional(readOnly = true)
    public Page<Customer> findAll(Pageable pageable) {
        log.debug("Request to get all Customers");
        return customerRepository.findAll(pageable);
    }

    public Page<Customer> findAllWithEagerRelationships(Pageable pageable) {
        return customerRepository.findAllWithEagerRelationships(pageable);
    }

    @Override
    @Transactional(readOnly = true)
    public Optional<Customer> findOne(Long id) {
        log.debug("Request to get Customer : {}", id);
        return customerRepository.findOneWithEagerRelationships(id);
    }

    @Override
    public void delete(Long id) {
        log.debug("Request to delete Customer : {}", id);
        customerRepository.deleteById(id);
    }
}
