package com.minesup.ape.domain;

import static org.assertj.core.api.Assertions.assertThat;

import com.minesup.ape.web.rest.TestUtil;
import org.junit.jupiter.api.Test;

class CorrectionTest {

    @Test
    void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(Correction.class);
        Correction correction1 = new Correction();
        correction1.setId(1L);
        Correction correction2 = new Correction();
        correction2.setId(correction1.getId());
        assertThat(correction1).isEqualTo(correction2);
        correction2.setId(2L);
        assertThat(correction1).isNotEqualTo(correction2);
        correction1.setId(null);
        assertThat(correction1).isNotEqualTo(correction2);
    }
}
