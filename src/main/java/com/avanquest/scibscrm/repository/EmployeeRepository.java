package com.avanquest.scibscrm.repository;

import com.avanquest.scibscrm.domain.Employee;
import java.util.List;
import java.util.Optional;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.*;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

/**
 * Spring Data SQL repository for the Employee entity.
 */
@Repository
public interface EmployeeRepository extends JpaRepository<Employee, Long>, JpaSpecificationExecutor<Employee> {
    default Optional<Employee> findOneWithEagerRelationships(Long id) {
        return this.findOneWithToOneRelationships(id);
    }

    default List<Employee> findAllWithEagerRelationships() {
        return this.findAllWithToOneRelationships();
    }

    default Page<Employee> findAllWithEagerRelationships(Pageable pageable) {
        return this.findAllWithToOneRelationships(pageable);
    }

    @Query(
        value = "select distinct employee from Employee employee left join fetch employee.utilisateur left join fetch employee.departement",
        countQuery = "select count(distinct employee) from Employee employee"
    )
    Page<Employee> findAllWithToOneRelationships(Pageable pageable);

    @Query("select distinct employee from Employee employee left join fetch employee.utilisateur left join fetch employee.departement")
    List<Employee> findAllWithToOneRelationships();

    @Query(
        "select employee from Employee employee left join fetch employee.utilisateur left join fetch employee.departement where employee.id =:id"
    )
    Optional<Employee> findOneWithToOneRelationships(@Param("id") Long id);
}
