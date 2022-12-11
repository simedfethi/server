package com.avanquest.scibscrm.repository;

import com.avanquest.scibscrm.domain.TransactionCRM;
import java.util.List;
import java.util.Optional;
import org.springframework.data.domain.Page;

public interface TransactionCRMRepositoryWithBagRelationships {
    Optional<TransactionCRM> fetchBagRelationships(Optional<TransactionCRM> transactionCRM);

    List<TransactionCRM> fetchBagRelationships(List<TransactionCRM> transactionCRMS);

    Page<TransactionCRM> fetchBagRelationships(Page<TransactionCRM> transactionCRMS);
}
