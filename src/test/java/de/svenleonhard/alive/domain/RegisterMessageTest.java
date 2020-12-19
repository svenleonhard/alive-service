package de.svenleonhard.alive.domain;

import static org.assertj.core.api.Assertions.assertThat;

import de.svenleonhard.alive.web.rest.TestUtil;
import org.junit.jupiter.api.Test;

public class RegisterMessageTest {

    @Test
    public void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(RegisterMessage.class);
        RegisterMessage registerMessage1 = new RegisterMessage();
        registerMessage1.setId(1L);
        RegisterMessage registerMessage2 = new RegisterMessage();
        registerMessage2.setId(registerMessage1.getId());
        assertThat(registerMessage1).isEqualTo(registerMessage2);
        registerMessage2.setId(2L);
        assertThat(registerMessage1).isNotEqualTo(registerMessage2);
        registerMessage1.setId(null);
        assertThat(registerMessage1).isNotEqualTo(registerMessage2);
    }
}
