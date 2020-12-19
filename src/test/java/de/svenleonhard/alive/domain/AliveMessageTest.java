package de.svenleonhard.alive.domain;

import static org.assertj.core.api.Assertions.assertThat;

import de.svenleonhard.alive.web.rest.TestUtil;
import org.junit.jupiter.api.Test;

public class AliveMessageTest {

    @Test
    public void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(AliveMessage.class);
        AliveMessage aliveMessage1 = new AliveMessage();
        aliveMessage1.setId(1L);
        AliveMessage aliveMessage2 = new AliveMessage();
        aliveMessage2.setId(aliveMessage1.getId());
        assertThat(aliveMessage1).isEqualTo(aliveMessage2);
        aliveMessage2.setId(2L);
        assertThat(aliveMessage1).isNotEqualTo(aliveMessage2);
        aliveMessage1.setId(null);
        assertThat(aliveMessage1).isNotEqualTo(aliveMessage2);
    }
}
