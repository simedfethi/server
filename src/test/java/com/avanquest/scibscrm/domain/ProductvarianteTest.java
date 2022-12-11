package com.avanquest.scibscrm.domain;

import static org.assertj.core.api.Assertions.assertThat;

import com.avanquest.scibscrm.web.rest.TestUtil;
import org.junit.jupiter.api.Test;

class ProductvarianteTest {

    @Test
    void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(Productvariante.class);
        Productvariante productvariante1 = new Productvariante();
        productvariante1.setId(1L);
        Productvariante productvariante2 = new Productvariante();
        productvariante2.setId(productvariante1.getId());
        assertThat(productvariante1).isEqualTo(productvariante2);
        productvariante2.setId(2L);
        assertThat(productvariante1).isNotEqualTo(productvariante2);
        productvariante1.setId(null);
        assertThat(productvariante1).isNotEqualTo(productvariante2);
    }
}
