package com.avanquest.scibscrm.domain;

import static org.assertj.core.api.Assertions.assertThat;

import com.avanquest.scibscrm.web.rest.TestUtil;
import org.junit.jupiter.api.Test;

class MonnaieTest {

    @Test
    void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(Monnaie.class);
        Monnaie monnaie1 = new Monnaie();
        monnaie1.setId(1L);
        Monnaie monnaie2 = new Monnaie();
        monnaie2.setId(monnaie1.getId());
        assertThat(monnaie1).isEqualTo(monnaie2);
        monnaie2.setId(2L);
        assertThat(monnaie1).isNotEqualTo(monnaie2);
        monnaie1.setId(null);
        assertThat(monnaie1).isNotEqualTo(monnaie2);
    }
}
