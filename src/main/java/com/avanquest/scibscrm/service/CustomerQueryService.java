package com.avanquest.scibscrm.service;

import com.avanquest.scibscrm.domain.*; // for static metamodels
import com.avanquest.scibscrm.domain.Customer;
import com.avanquest.scibscrm.repository.CustomerRepository;
import com.avanquest.scibscrm.service.criteria.CustomerCriteria;
import java.util.List;
import javax.persistence.criteria.JoinType;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import tech.jhipster.service.QueryService;

/**
 * Service for executing complex queries for {@link Customer} entities in the database.
 * The main input is a {@link CustomerCriteria} which gets converted to {@link Specification},
 * in a way that all the filters must apply.
 * It returns a {@link List} of {@link Customer} or a {@link Page} of {@link Customer} which fulfills the criteria.
 */
@Service
@Transactional(readOnly = true)
public class CustomerQueryService extends QueryService<Customer> {

    private final Logger log = LoggerFactory.getLogger(CustomerQueryService.class);

    private final CustomerRepository customerRepository;

    public CustomerQueryService(CustomerRepository customerRepository) {
        this.customerRepository = customerRepository;
    }

    /**
     * Return a {@link List} of {@link Customer} which matches the criteria from the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the matching entities.
     */
    @Transactional(readOnly = true)
    public List<Customer> findByCriteria(CustomerCriteria criteria) {
        log.debug("find by criteria : {}", criteria);
        final Specification<Customer> specification = createSpecification(criteria);
        return customerRepository.findAll(specification);
    }

    /**
     * Return a {@link Page} of {@link Customer} which matches the criteria from the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @param page The page, which should be returned.
     * @return the matching entities.
     */
    @Transactional(readOnly = true)
    public Page<Customer> findByCriteria(CustomerCriteria criteria, Pageable page) {
        log.debug("find by criteria : {}, page: {}", criteria, page);
        final Specification<Customer> specification = createSpecification(criteria);
        return customerRepository.findAll(specification, page);
    }

    /**
     * Return the number of matching entities in the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the number of matching entities.
     */
    @Transactional(readOnly = true)
    public long countByCriteria(CustomerCriteria criteria) {
        log.debug("count by criteria : {}", criteria);
        final Specification<Customer> specification = createSpecification(criteria);
        return customerRepository.count(specification);
    }

    /**
     * Function to convert {@link CustomerCriteria} to a {@link Specification}
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the matching {@link Specification} of the entity.
     */
    protected Specification<Customer> createSpecification(CustomerCriteria criteria) {
        Specification<Customer> specification = Specification.where(null);
        if (criteria != null) {
            // This has to be called first, because the distinct method returns null
            if (criteria.getDistinct() != null) {
                specification = specification.and(distinct(criteria.getDistinct()));
            }
            if (criteria.getId() != null) {
                specification = specification.and(buildRangeSpecification(criteria.getId(), Customer_.id));
            }
            if (criteria.getCustomerType() != null) {
                specification = specification.and(buildSpecification(criteria.getCustomerType(), Customer_.customerType));
            }
            if (criteria.getCompany() != null) {
                specification = specification.and(buildStringSpecification(criteria.getCompany(), Customer_.company));
            }
            if (criteria.getLastName() != null) {
                specification = specification.and(buildStringSpecification(criteria.getLastName(), Customer_.lastName));
            }
            if (criteria.getFirstName() != null) {
                specification = specification.and(buildStringSpecification(criteria.getFirstName(), Customer_.firstName));
            }
            if (criteria.getEmailAddress() != null) {
                specification = specification.and(buildStringSpecification(criteria.getEmailAddress(), Customer_.emailAddress));
            }
            if (criteria.getJobTitle() != null) {
                specification = specification.and(buildStringSpecification(criteria.getJobTitle(), Customer_.jobTitle));
            }
            if (criteria.getBusinessPhone() != null) {
                specification = specification.and(buildStringSpecification(criteria.getBusinessPhone(), Customer_.businessPhone));
            }
            if (criteria.getHomePhone() != null) {
                specification = specification.and(buildStringSpecification(criteria.getHomePhone(), Customer_.homePhone));
            }
            if (criteria.getMobilePhone() != null) {
                specification = specification.and(buildStringSpecification(criteria.getMobilePhone(), Customer_.mobilePhone));
            }
            if (criteria.getFaxNumber() != null) {
                specification = specification.and(buildStringSpecification(criteria.getFaxNumber(), Customer_.faxNumber));
            }
            if (criteria.getWilaya() != null) {
                specification = specification.and(buildStringSpecification(criteria.getWilaya(), Customer_.wilaya));
            }
            if (criteria.getDaira() != null) {
                specification = specification.and(buildStringSpecification(criteria.getDaira(), Customer_.daira));
            }
            if (criteria.getCodePostal() != null) {
                specification = specification.and(buildStringSpecification(criteria.getCodePostal(), Customer_.codePostal));
            }
            if (criteria.getCommune() != null) {
                specification = specification.and(buildStringSpecification(criteria.getCommune(), Customer_.commune));
            }
            if (criteria.getDejaClient() != null) {
                specification = specification.and(buildSpecification(criteria.getDejaClient(), Customer_.dejaClient));
            }
            if (criteria.getDateDerniereViste() != null) {
                specification = specification.and(buildRangeSpecification(criteria.getDateDerniereViste(), Customer_.dateDerniereViste));
            }
            if (criteria.getLatitude() != null) {
                specification = specification.and(buildRangeSpecification(criteria.getLatitude(), Customer_.latitude));
            }
            if (criteria.getLongitude() != null) {
                specification = specification.and(buildRangeSpecification(criteria.getLongitude(), Customer_.longitude));
            }
            if (criteria.getCommercialId() != null) {
                specification =
                    specification.and(
                        buildSpecification(
                            criteria.getCommercialId(),
                            root -> root.join(Customer_.commercial, JoinType.LEFT).get(Employee_.id)
                        )
                    );
            }
        }
        return specification;
    }
}
