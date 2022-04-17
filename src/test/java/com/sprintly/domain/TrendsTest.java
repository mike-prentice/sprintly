package com.sprintly.domain;

import static org.assertj.core.api.Assertions.assertThat;

import com.sprintly.web.rest.TestUtil;
import org.junit.jupiter.api.Test;

class TrendsTest {

    @Test
    void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(Trends.class);
        Trends trends1 = new Trends();
        trends1.setId(1L);
        Trends trends2 = new Trends();
        trends2.setId(trends1.getId());
        assertThat(trends1).isEqualTo(trends2);
        trends2.setId(2L);
        assertThat(trends1).isNotEqualTo(trends2);
        trends1.setId(null);
        assertThat(trends1).isNotEqualTo(trends2);
    }
}
