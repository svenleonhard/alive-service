package de.svenleonhard.alive.domain;

import static org.assertj.core.api.Assertions.assertThat;

import de.svenleonhard.alive.web.rest.TestUtil;
import org.junit.jupiter.api.Test;

public class DeviceNotAliveTest {

    @Test
    public void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(DeviceNotAlive.class);
        DeviceNotAlive deviceNotAlive1 = new DeviceNotAlive();
        deviceNotAlive1.setId(1L);
        DeviceNotAlive deviceNotAlive2 = new DeviceNotAlive();
        deviceNotAlive2.setId(deviceNotAlive1.getId());
        assertThat(deviceNotAlive1).isEqualTo(deviceNotAlive2);
        deviceNotAlive2.setId(2L);
        assertThat(deviceNotAlive1).isNotEqualTo(deviceNotAlive2);
        deviceNotAlive1.setId(null);
        assertThat(deviceNotAlive1).isNotEqualTo(deviceNotAlive2);
    }
}
