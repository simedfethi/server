package com.avanquest.scibscrm.service;

import com.avanquest.scibscrm.domain.*; // for static metamodels
import com.avanquest.scibscrm.domain.Company;
import com.avanquest.scibscrm.repository.CompanyRepository;
import com.avanquest.scibscrm.service.criteria.CompanyCriteria;
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
 * Service for executing complex queries for {@link Company} entities in the database.
 * The main input is a {@link CompanyCriteria} which gets converted to {@link Specification},
 * in a way that all the filters must apply.
 * It returns a {@link List} of {@link Company} or a {@link Page} of {@link Company} which fulfills the criteria.
 */
@Service
@Transactional(readOnly = true)
public class CompanyQueryService extends QueryService<Company> {

    private final Logger log = LoggerFactory.getLogger(CompanyQueryService.class);

    private final CompanyRepository companyRepository;

    public CompanyQueryService(CompanyRepository companyRepository) {
        this.companyRepository = companyRepository;
    }

    /**
     * Return a {@link List} of {@link Company} which matches the criteria from the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the matching entities.
     */
    @Transactional(readOnly = true)
    public List<Company> findByCriteria(CompanyCriteria criteria) {
        log.debug("find by criteria : {}", criteria);
        final Specification<Company> specification = createSpecification(criteria);
        return companyRepository.findAll(specification);
    }

    /**
     * Return a {@link Page} of {@link Company} which matches the criteria from the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @param page The page, which should be returned.
     * @return the matching entities.
     */
    @Transactional(readOnly = true)
    public Page<Company> findByCriteria(CompanyCriteria criteria, Pageable page) {
        log.debug("find by criteria : {}, page: {}", criteria, page);
        final Specification<Company> specification = createSpecification(criteria);
        return companyRepository.findAll(specification, page);
    }

    /**
     * Return the number of matching entities in the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the number of matching entities.
     */
    @Transactional(readOnly = true)
    public long countByCriteria(CompanyCriteria criteria) {
        log.debug("count by criteria : {}", criteria);
        final Specification<Company> specification = createSpecification(criteria);
        return companyRepository.count(specification);
    }

    /**
     * Function to convert {@link CompanyCriteria} to a {@link Specification}
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the matching {@link Specification} of the entity.
     */
    protected Specification<Company> createSpecification(CompanyCriteria criteria) {
        Specification<Company> specification = Specification.where(null);
        if (criteria != null) {
            // This has to be called first, because the distinct method returns null
            if (criteria.getDistinct() != null) {
                specification = specification.and(distinct(criteria.getDistinct()));
            }
            if (criteria.getId() != null) {
                specification = specification.and(buildRangeSpecification(criteria.getId(), Company_.id));
            }
            if (criteria.getCompany() != null) {
                specification = specification.and(buildStringSpecification(criteria.getCompany(), Company_.company));
            }
            if (criteria.getLastName() != null) {
                specification = specification.and(buildStringSpecification(criteria.getLastName(), Company_.lastName));
            }
            if (criteria.getFirstName() != null) {
                specification = specification.and(buildStringSpecification(criteria.getFirstName(), Company_.firstName));
            }
            if (criteria.getEmailAddress() != null) {
                specification = specification.and(buildStringSpecification(criteria.getEmailAddress(), Company_.emailAddress));
            }
            if (criteria.getJobTitle() != null) {
                specification = specification.and(buildStringSpecification(criteria.getJobTitle(), Company_.jobTitle));
            }
            if (criteria.getBusinessPhone() != null) {
                specification = specification.and(buildStringSpecification(criteria.getBusinessPhone(), Company_.businessPhone));
            }
            if (criteria.getHomePhone() != null) {
                specification = specification.and(buildStringSpecification(criteria.getHomePhone(), Company_.homePhone));
            }
            if (criteria.getMobilePhone() != null) {
                specification = specification.and(buildStringSpecification(criteria.getMobilePhone(), Company_.mobilePhone));
            }
            if (criteria.getFaxNumber() != null) {
                specification = specification.and(buildStringSpecification(criteria.getFaxNumber(), Company_.faxNumber));
            }
            if (criteria.getCity() != null) {
                specification = specification.and(buildStringSpecification(criteria.getCity(), Company_.city));
            }
            if (criteria.getStateProvince() != null) {
                specification = specification.and(buildStringSpecification(criteria.getStateProvince(), Company_.stateProvince));
            }
            if (criteria.getZipPostalCode() != null) {
                specification = specification.and(buildStringSpecification(criteria.getZipPostalCode(), Company_.zipPostalCode));
            }
            if (criteria.getCountryRegion() != null) {
                specification = specification.and(buildStringSpecification(criteria.getCountryRegion(), Company_.countryRegion));
            }
        }
        return specification;
    }
}
