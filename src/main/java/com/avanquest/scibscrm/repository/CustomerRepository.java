package com.avanquest.scibscrm.repository;

import com.avanquest.scibscrm.domain.Customer;
import java.util.List;
import java.util.Optional;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.*;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

/**
 * Spring Data SQL repository for the Customer entity.
 */
@Repository
public interface CustomerRepository extends JpaRepository<Customer, Long>, JpaSpecificationExecutor<Customer> {
    default Optional<Customer> findOneWithEagerRelationships(Long id) {
        return this.findOneWithToOneRelationships(id);
    }

    default List<Customer> findAllWithEagerRelationships() {
        return this.findAllWithToOneRelationships();
    }

    default Page<Customer> findAllWithEagerRelationships(Pageable pageable) {
        return this.findAllWithToOneRelationships(pageable);
    }

    @Query(
        value = "select distinct customer from Customer customer left join fetch customer.commercial",
        countQuery = "select count(distinct customer) from Customer customer"
    )
    Page<Customer> findAllWithToOneRelationships(Pageable pageable);

    @Query("select distinct customer from Customer customer left join fetch customer.commercial")
    List<Customer> findAllWithToOneRelationships();

    @Query("select customer from Customer customer left join fetch customer.commercial where customer.id =:id")
    Optional<Customer> findOneWithToOneRelationships(@Param("id") Long id);
}
