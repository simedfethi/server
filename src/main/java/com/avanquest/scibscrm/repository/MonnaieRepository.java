package com.avanquest.scibscrm.repository;

import com.avanquest.scibscrm.domain.Monnaie;
import org.springframework.data.jpa.repository.*;
import org.springframework.stereotype.Repository;

/**
 * Spring Data SQL repository for the Monnaie entity.
 */
@SuppressWarnings("unused")
@Repository
public interface MonnaieRepository extends JpaRepository<Monnaie, Long>, JpaSpecificationExecutor<Monnaie> {}
