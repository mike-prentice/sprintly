package com.sprintly.domain;

import static org.assertj.core.api.Assertions.assertThat;

import com.sprintly.web.rest.TestUtil;
import org.junit.jupiter.api.Test;

class StatsTest {

    @Test
    void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(Stats.class);
        Stats stats1 = new Stats();
        stats1.setId(1L);
        Stats stats2 = new Stats();
        stats2.setId(stats1.getId());
        assertThat(stats1).isEqualTo(stats2);
        stats2.setId(2L);
        assertThat(stats1).isNotEqualTo(stats2);
        stats1.setId(null);
        assertThat(stats1).isNotEqualTo(stats2);
    }
}
