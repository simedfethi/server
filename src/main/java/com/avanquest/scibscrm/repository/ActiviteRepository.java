package com.avanquest.scibscrm.repository;

import com.avanquest.scibscrm.domain.Activite;
import java.util.List;
import java.util.Optional;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.*;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

/**
 * Spring Data SQL repository for the Activite entity.
 */
@Repository
public interface ActiviteRepository
    extends ActiviteRepositoryWithBagRelationships, JpaRepository<Activite, Long>, JpaSpecificationExecutor<Activite> {
    default Optional<Activite> findOneWithEagerRelationships(Long id) {
        return this.fetchBagRelationships(this.findOneWithToOneRelationships(id));
    }

    default List<Activite> findAllWithEagerRelationships() {
        return this.fetchBagRelationships(this.findAllWithToOneRelationships());
    }

    default Page<Activite> findAllWithEagerRelationships(Pageable pageable) {
        return this.fetchBagRelationships(this.findAllWithToOneRelationships(pageable));
    }

    @Query(
        value = "select distinct activite from Activite activite left join fetch activite.createur",
        countQuery = "select count(distinct activite) from Activite activite"
    )
    Page<Activite> findAllWithToOneRelationships(Pageable pageable);

    @Query("select distinct activite from Activite activite left join fetch activite.createur")
    List<Activite> findAllWithToOneRelationships();

    @Query("select activite from Activite activite left join fetch activite.createur where activite.id =:id")
    Optional<Activite> findOneWithToOneRelationships(@Param("id") Long id);
}
