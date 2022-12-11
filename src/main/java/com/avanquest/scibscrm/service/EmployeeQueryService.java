package com.avanquest.scibscrm.service;

import com.avanquest.scibscrm.domain.*; // for static metamodels
import com.avanquest.scibscrm.domain.Employee;
import com.avanquest.scibscrm.repository.EmployeeRepository;
import com.avanquest.scibscrm.service.criteria.EmployeeCriteria;
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
 * Service for executing complex queries for {@link Employee} entities in the database.
 * The main input is a {@link EmployeeCriteria} which gets converted to {@link Specification},
 * in a way that all the filters must apply.
 * It returns a {@link List} of {@link Employee} or a {@link Page} of {@link Employee} which fulfills the criteria.
 */
@Service
@Transactional(readOnly = true)
public class EmployeeQueryService extends QueryService<Employee> {

    private final Logger log = LoggerFactory.getLogger(EmployeeQueryService.class);

    private final EmployeeRepository employeeRepository;

    public EmployeeQueryService(EmployeeRepository employeeRepository) {
        this.employeeRepository = employeeRepository;
    }

    /**
     * Return a {@link List} of {@link Employee} which matches the criteria from the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the matching entities.
     */
    @Transactional(readOnly = true)
    public List<Employee> findByCriteria(EmployeeCriteria criteria) {
        log.debug("find by criteria : {}", criteria);
        final Specification<Employee> specification = createSpecification(criteria);
        return employeeRepository.findAll(specification);
    }

    /**
     * Return a {@link Page} of {@link Employee} which matches the criteria from the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @param page The page, which should be returned.
     * @return the matching entities.
     */
    @Transactional(readOnly = true)
    public Page<Employee> findByCriteria(EmployeeCriteria criteria, Pageable page) {
        log.debug("find by criteria : {}, page: {}", criteria, page);
        final Specification<Employee> specification = createSpecification(criteria);
        return employeeRepository.findAll(specification, page);
    }

    /**
     * Return the number of matching entities in the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the number of matching entities.
     */
    @Transactional(readOnly = true)
    public long countByCriteria(EmployeeCriteria criteria) {
        log.debug("count by criteria : {}", criteria);
        final Specification<Employee> specification = createSpecification(criteria);
        return employeeRepository.count(specification);
    }

    /**
     * Function to convert {@link EmployeeCriteria} to a {@link Specification}
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the matching {@link Specification} of the entity.
     */
    protected Specification<Employee> createSpecification(EmployeeCriteria criteria) {
        Specification<Employee> specification = Specification.where(null);
        if (criteria != null) {
            // This has to be called first, because the distinct method returns null
            if (criteria.getDistinct() != null) {
                specification = specification.and(distinct(criteria.getDistinct()));
            }
            if (criteria.getId() != null) {
                specification = specification.and(buildRangeSpecification(criteria.getId(), Employee_.id));
            }
            if (criteria.getEmployeeName() != null) {
                specification = specification.and(buildStringSpecification(criteria.getEmployeeName(), Employee_.employeeName));
            }
            if (criteria.getGender() != null) {
                specification = specification.and(buildSpecification(criteria.getGender(), Employee_.gender));
            }
            if (criteria.getPhone() != null) {
                specification = specification.and(buildStringSpecification(criteria.getPhone(), Employee_.phone));
            }
            if (criteria.getAddressLine1() != null) {
                specification = specification.and(buildStringSpecification(criteria.getAddressLine1(), Employee_.addressLine1));
            }
            if (criteria.getUtilisateurId() != null) {
                specification =
                    specification.and(
                        buildSpecification(
                            criteria.getUtilisateurId(),
                            root -> root.join(Employee_.utilisateur, JoinType.LEFT).get(User_.id)
                        )
                    );
            }
            if (criteria.getDepartementId() != null) {
                specification =
                    specification.and(
                        buildSpecification(
                            criteria.getDepartementId(),
                            root -> root.join(Employee_.departement, JoinType.LEFT).get(Departement_.id)
                        )
                    );
            }
            if (criteria.getActivityAssId() != null) {
                specification =
                    specification.and(
                        buildSpecification(
                            criteria.getActivityAssId(),
                            root -> root.join(Employee_.activityAsses, JoinType.LEFT).get(Activite_.id)
                        )
                    );
            }
            if (criteria.getActivityEmpId() != null) {
                specification =
                    specification.and(
                        buildSpecification(
                            criteria.getActivityEmpId(),
                            root -> root.join(Employee_.activityEmps, JoinType.LEFT).get(Activite_.id)
                        )
                    );
            }
        }
        return specification;
    }
}
