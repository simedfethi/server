package com.avanquest.scibscrm.repository;

import com.avanquest.scibscrm.domain.Activite;
import java.util.List;
import java.util.Optional;
import org.springframework.data.domain.Page;

public interface ActiviteRepositoryWithBagRelationships {
    Optional<Activite> fetchBagRelationships(Optional<Activite> activite);

    List<Activite> fetchBagRelationships(List<Activite> activites);

    Page<Activite> fetchBagRelationships(Page<Activite> activites);
}
