package com.sprintly.domain;

import static org.assertj.core.api.Assertions.assertThat;

import com.sprintly.web.rest.TestUtil;
import org.junit.jupiter.api.Test;

class MapTest {

    @Test
    void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(Map.class);
        Map map1 = new Map();
        map1.setId(1L);
        Map map2 = new Map();
        map2.setId(map1.getId());
        assertThat(map1).isEqualTo(map2);
        map2.setId(2L);
        assertThat(map1).isNotEqualTo(map2);
        map1.setId(null);
        assertThat(map1).isNotEqualTo(map2);
    }
}
