package com.avanquest.scibscrm.repository;

import com.avanquest.scibscrm.domain.Productvariante;
import org.springframework.data.jpa.repository.*;
import org.springframework.stereotype.Repository;

/**
 * Spring Data SQL repository for the Productvariante entity.
 */
@SuppressWarnings("unused")
@Repository
public interface ProductvarianteRepository extends JpaRepository<Productvariante, Long>, JpaSpecificationExecutor<Productvariante> {}
