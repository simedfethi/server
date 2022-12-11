package com.avanquest.scibscrm.repository;

import com.avanquest.scibscrm.domain.TransactionCRM;
import java.util.List;
import java.util.Optional;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.*;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

/**
 * Spring Data SQL repository for the TransactionCRM entity.
 */
@Repository
public interface TransactionCRMRepository
    extends TransactionCRMRepositoryWithBagRelationships, JpaRepository<TransactionCRM, Long>, JpaSpecificationExecutor<TransactionCRM> {
    default Optional<TransactionCRM> findOneWithEagerRelationships(Long id) {
        return this.fetchBagRelationships(this.findOneWithToOneRelationships(id));
    }

    default List<TransactionCRM> findAllWithEagerRelationships() {
        return this.fetchBagRelationships(this.findAllWithToOneRelationships());
    }

    default Page<TransactionCRM> findAllWithEagerRelationships(Pageable pageable) {
        return this.fetchBagRelationships(this.findAllWithToOneRelationships(pageable));
    }

    @Query(
        value = "select distinct transactionCRM from TransactionCRM transactionCRM left join fetch transactionCRM.monnaie left join fetch transactionCRM.chargeAffaire left join fetch transactionCRM.client",
        countQuery = "select count(distinct transactionCRM) from TransactionCRM transactionCRM"
    )
    Page<TransactionCRM> findAllWithToOneRelationships(Pageable pageable);

    @Query(
        "select distinct transactionCRM from TransactionCRM transactionCRM left join fetch transactionCRM.monnaie left join fetch transactionCRM.chargeAffaire left join fetch transactionCRM.client"
    )
    List<TransactionCRM> findAllWithToOneRelationships();

    @Query(
        "select transactionCRM from TransactionCRM transactionCRM left join fetch transactionCRM.monnaie left join fetch transactionCRM.chargeAffaire left join fetch transactionCRM.client where transactionCRM.id =:id"
    )
    Optional<TransactionCRM> findOneWithToOneRelationships(@Param("id") Long id);
}
