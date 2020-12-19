package de.svenleonhard.alive.domain;

import static org.assertj.core.api.Assertions.assertThat;

import de.svenleonhard.alive.web.rest.TestUtil;
import org.junit.jupiter.api.Test;

public class ObserveTest {

    @Test
    public void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(Observe.class);
        Observe observe1 = new Observe();
        observe1.setId(1L);
        Observe observe2 = new Observe();
        observe2.setId(observe1.getId());
        assertThat(observe1).isEqualTo(observe2);
        observe2.setId(2L);
        assertThat(observe1).isNotEqualTo(observe2);
        observe1.setId(null);
        assertThat(observe1).isNotEqualTo(observe2);
    }
}
