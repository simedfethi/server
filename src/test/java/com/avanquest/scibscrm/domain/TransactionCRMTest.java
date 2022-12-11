package com.avanquest.scibscrm.domain;

import static org.assertj.core.api.Assertions.assertThat;

import com.avanquest.scibscrm.web.rest.TestUtil;
import org.junit.jupiter.api.Test;

class TransactionCRMTest {

    @Test
    void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(TransactionCRM.class);
        TransactionCRM transactionCRM1 = new TransactionCRM();
        transactionCRM1.setId(1L);
        TransactionCRM transactionCRM2 = new TransactionCRM();
        transactionCRM2.setId(transactionCRM1.getId());
        assertThat(transactionCRM1).isEqualTo(transactionCRM2);
        transactionCRM2.setId(2L);
        assertThat(transactionCRM1).isNotEqualTo(transactionCRM2);
        transactionCRM1.setId(null);
        assertThat(transactionCRM1).isNotEqualTo(transactionCRM2);
    }
}
