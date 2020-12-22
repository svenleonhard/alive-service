package de.svenleonhard.alive.web.rest;

import static de.svenleonhard.alive.web.rest.TestUtil.sameInstant;
import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import de.svenleonhard.alive.AliveServiceApp;
import de.svenleonhard.alive.domain.DeviceNotAlive;
import de.svenleonhard.alive.domain.User;
import de.svenleonhard.alive.repository.DeviceNotAliveRepository;
import de.svenleonhard.alive.service.DeviceNotAliveQueryService;
import de.svenleonhard.alive.service.DeviceNotAliveService;
import de.svenleonhard.alive.service.dto.DeviceNotAliveCriteria;
import java.time.Instant;
import java.time.ZoneId;
import java.time.ZoneOffset;
import java.time.ZonedDateTime;
import java.util.List;
import javax.persistence.EntityManager;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.transaction.annotation.Transactional;

/**
 * Integration tests for the {@link DeviceNotAliveResource} REST controller.
 */
@SpringBootTest(classes = AliveServiceApp.class)
@AutoConfigureMockMvc
@WithMockUser
public class DeviceNotAliveResourceIT {
    private static final ZonedDateTime DEFAULT_OCCURED = ZonedDateTime.ofInstant(Instant.ofEpochMilli(0L), ZoneOffset.UTC);
    private static final ZonedDateTime UPDATED_OCCURED = ZonedDateTime.now(ZoneId.systemDefault()).withNano(0);
    private static final ZonedDateTime SMALLER_OCCURED = ZonedDateTime.ofInstant(Instant.ofEpochMilli(-1L), ZoneOffset.UTC);

    private static final Boolean DEFAULT_CONFIRMED = false;
    private static final Boolean UPDATED_CONFIRMED = true;

    @Autowired
    private DeviceNotAliveRepository deviceNotAliveRepository;

    @Autowired
    private DeviceNotAliveService deviceNotAliveService;

    @Autowired
    private DeviceNotAliveQueryService deviceNotAliveQueryService;

    @Autowired
    private EntityManager em;

    @Autowired
    private MockMvc restDeviceNotAliveMockMvc;

    private DeviceNotAlive deviceNotAlive;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static DeviceNotAlive createEntity(EntityManager em) {
        DeviceNotAlive deviceNotAlive = new DeviceNotAlive()
            .occured(DEFAULT_OCCURED)
            .confirmed(DEFAULT_CONFIRMED)
            .user(UserResourceIT.create());
        return deviceNotAlive;
    }

    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static DeviceNotAlive createUpdatedEntity(EntityManager em) {
        DeviceNotAlive deviceNotAlive = new DeviceNotAlive().occured(UPDATED_OCCURED).confirmed(UPDATED_CONFIRMED);
        return deviceNotAlive;
    }

    @BeforeEach
    public void initTest() {
        deviceNotAlive = createEntity(em);
    }

    @Test
    @Transactional
    public void createDeviceNotAlive() throws Exception {
        int databaseSizeBeforeCreate = deviceNotAliveRepository.findAll().size();
        // Create the DeviceNotAlive
        restDeviceNotAliveMockMvc
            .perform(
                post("/api/device-not-alives")
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(deviceNotAlive))
            )
            .andExpect(status().isCreated());

        // Validate the DeviceNotAlive in the database
        List<DeviceNotAlive> deviceNotAliveList = deviceNotAliveRepository.findAll();
        assertThat(deviceNotAliveList).hasSize(databaseSizeBeforeCreate + 1);
        DeviceNotAlive testDeviceNotAlive = deviceNotAliveList.get(deviceNotAliveList.size() - 1);
        assertThat(testDeviceNotAlive.getOccured()).isEqualTo(DEFAULT_OCCURED);
        assertThat(testDeviceNotAlive.isConfirmed()).isEqualTo(DEFAULT_CONFIRMED);
    }

    @Test
    @Transactional
    public void createDeviceNotAliveWithExistingId() throws Exception {
        int databaseSizeBeforeCreate = deviceNotAliveRepository.findAll().size();

        // Create the DeviceNotAlive with an existing ID
        deviceNotAlive.setId(1L);

        // An entity with an existing ID cannot be created, so this API call must fail
        restDeviceNotAliveMockMvc
            .perform(
                post("/api/device-not-alives")
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(deviceNotAlive))
            )
            .andExpect(status().isBadRequest());

        // Validate the DeviceNotAlive in the database
        List<DeviceNotAlive> deviceNotAliveList = deviceNotAliveRepository.findAll();
        assertThat(deviceNotAliveList).hasSize(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    public void getAllDeviceNotAlives() throws Exception {
        // Initialize the database
        deviceNotAliveRepository.saveAndFlush(deviceNotAlive);

        // Get all the deviceNotAliveList
        restDeviceNotAliveMockMvc
            .perform(get("/api/device-not-alives?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(deviceNotAlive.getId().intValue())))
            .andExpect(jsonPath("$.[*].occured").value(hasItem(sameInstant(DEFAULT_OCCURED))))
            .andExpect(jsonPath("$.[*].confirmed").value(hasItem(DEFAULT_CONFIRMED.booleanValue())));
    }

    @Test
    @Transactional
    public void getDeviceNotAlive() throws Exception {
        // Initialize the database
        deviceNotAliveRepository.saveAndFlush(deviceNotAlive);

        // Get the deviceNotAlive
        restDeviceNotAliveMockMvc
            .perform(get("/api/device-not-alives/{id}", deviceNotAlive.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.id").value(deviceNotAlive.getId().intValue()))
            .andExpect(jsonPath("$.occured").value(sameInstant(DEFAULT_OCCURED)))
            .andExpect(jsonPath("$.confirmed").value(DEFAULT_CONFIRMED.booleanValue()));
    }

    @Test
    @Transactional
    public void getDeviceNotAlivesByIdFiltering() throws Exception {
        // Initialize the database
        deviceNotAliveRepository.saveAndFlush(deviceNotAlive);

        Long id = deviceNotAlive.getId();

        defaultDeviceNotAliveShouldBeFound("id.equals=" + id);
        defaultDeviceNotAliveShouldNotBeFound("id.notEquals=" + id);

        defaultDeviceNotAliveShouldBeFound("id.greaterThanOrEqual=" + id);
        defaultDeviceNotAliveShouldNotBeFound("id.greaterThan=" + id);

        defaultDeviceNotAliveShouldBeFound("id.lessThanOrEqual=" + id);
        defaultDeviceNotAliveShouldNotBeFound("id.lessThan=" + id);
    }

    @Test
    @Transactional
    public void getAllDeviceNotAlivesByOccuredIsEqualToSomething() throws Exception {
        // Initialize the database
        deviceNotAliveRepository.saveAndFlush(deviceNotAlive);

        // Get all the deviceNotAliveList where occured equals to DEFAULT_OCCURED
        defaultDeviceNotAliveShouldBeFound("occured.equals=" + DEFAULT_OCCURED);

        // Get all the deviceNotAliveList where occured equals to UPDATED_OCCURED
        defaultDeviceNotAliveShouldNotBeFound("occured.equals=" + UPDATED_OCCURED);
    }

    @Test
    @Transactional
    public void getAllDeviceNotAlivesByOccuredIsNotEqualToSomething() throws Exception {
        // Initialize the database
        deviceNotAliveRepository.saveAndFlush(deviceNotAlive);

        // Get all the deviceNotAliveList where occured not equals to DEFAULT_OCCURED
        defaultDeviceNotAliveShouldNotBeFound("occured.notEquals=" + DEFAULT_OCCURED);

        // Get all the deviceNotAliveList where occured not equals to UPDATED_OCCURED
        defaultDeviceNotAliveShouldBeFound("occured.notEquals=" + UPDATED_OCCURED);
    }

    @Test
    @Transactional
    public void getAllDeviceNotAlivesByOccuredIsInShouldWork() throws Exception {
        // Initialize the database
        deviceNotAliveRepository.saveAndFlush(deviceNotAlive);

        // Get all the deviceNotAliveList where occured in DEFAULT_OCCURED or UPDATED_OCCURED
        defaultDeviceNotAliveShouldBeFound("occured.in=" + DEFAULT_OCCURED + "," + UPDATED_OCCURED);

        // Get all the deviceNotAliveList where occured equals to UPDATED_OCCURED
        defaultDeviceNotAliveShouldNotBeFound("occured.in=" + UPDATED_OCCURED);
    }

    @Test
    @Transactional
    public void getAllDeviceNotAlivesByOccuredIsNullOrNotNull() throws Exception {
        // Initialize the database
        deviceNotAliveRepository.saveAndFlush(deviceNotAlive);

        // Get all the deviceNotAliveList where occured is not null
        defaultDeviceNotAliveShouldBeFound("occured.specified=true");

        // Get all the deviceNotAliveList where occured is null
        defaultDeviceNotAliveShouldNotBeFound("occured.specified=false");
    }

    @Test
    @Transactional
    public void getAllDeviceNotAlivesByOccuredIsGreaterThanOrEqualToSomething() throws Exception {
        // Initialize the database
        deviceNotAliveRepository.saveAndFlush(deviceNotAlive);

        // Get all the deviceNotAliveList where occured is greater than or equal to DEFAULT_OCCURED
        defaultDeviceNotAliveShouldBeFound("occured.greaterThanOrEqual=" + DEFAULT_OCCURED);

        // Get all the deviceNotAliveList where occured is greater than or equal to UPDATED_OCCURED
        defaultDeviceNotAliveShouldNotBeFound("occured.greaterThanOrEqual=" + UPDATED_OCCURED);
    }

    @Test
    @Transactional
    public void getAllDeviceNotAlivesByOccuredIsLessThanOrEqualToSomething() throws Exception {
        // Initialize the database
        deviceNotAliveRepository.saveAndFlush(deviceNotAlive);

        // Get all the deviceNotAliveList where occured is less than or equal to DEFAULT_OCCURED
        defaultDeviceNotAliveShouldBeFound("occured.lessThanOrEqual=" + DEFAULT_OCCURED);

        // Get all the deviceNotAliveList where occured is less than or equal to SMALLER_OCCURED
        defaultDeviceNotAliveShouldNotBeFound("occured.lessThanOrEqual=" + SMALLER_OCCURED);
    }

    @Test
    @Transactional
    public void getAllDeviceNotAlivesByOccuredIsLessThanSomething() throws Exception {
        // Initialize the database
        deviceNotAliveRepository.saveAndFlush(deviceNotAlive);

        // Get all the deviceNotAliveList where occured is less than DEFAULT_OCCURED
        defaultDeviceNotAliveShouldNotBeFound("occured.lessThan=" + DEFAULT_OCCURED);

        // Get all the deviceNotAliveList where occured is less than UPDATED_OCCURED
        defaultDeviceNotAliveShouldBeFound("occured.lessThan=" + UPDATED_OCCURED);
    }

    @Test
    @Transactional
    public void getAllDeviceNotAlivesByOccuredIsGreaterThanSomething() throws Exception {
        // Initialize the database
        deviceNotAliveRepository.saveAndFlush(deviceNotAlive);

        // Get all the deviceNotAliveList where occured is greater than DEFAULT_OCCURED
        defaultDeviceNotAliveShouldNotBeFound("occured.greaterThan=" + DEFAULT_OCCURED);

        // Get all the deviceNotAliveList where occured is greater than SMALLER_OCCURED
        defaultDeviceNotAliveShouldBeFound("occured.greaterThan=" + SMALLER_OCCURED);
    }

    @Test
    @Transactional
    public void getAllDeviceNotAlivesByConfirmedIsEqualToSomething() throws Exception {
        // Initialize the database
        deviceNotAliveRepository.saveAndFlush(deviceNotAlive);

        // Get all the deviceNotAliveList where confirmed equals to DEFAULT_CONFIRMED
        defaultDeviceNotAliveShouldBeFound("confirmed.equals=" + DEFAULT_CONFIRMED);

        // Get all the deviceNotAliveList where confirmed equals to UPDATED_CONFIRMED
        defaultDeviceNotAliveShouldNotBeFound("confirmed.equals=" + UPDATED_CONFIRMED);
    }

    @Test
    @Transactional
    public void getAllDeviceNotAlivesByConfirmedIsNotEqualToSomething() throws Exception {
        // Initialize the database
        deviceNotAliveRepository.saveAndFlush(deviceNotAlive);

        // Get all the deviceNotAliveList where confirmed not equals to DEFAULT_CONFIRMED
        defaultDeviceNotAliveShouldNotBeFound("confirmed.notEquals=" + DEFAULT_CONFIRMED);

        // Get all the deviceNotAliveList where confirmed not equals to UPDATED_CONFIRMED
        defaultDeviceNotAliveShouldBeFound("confirmed.notEquals=" + UPDATED_CONFIRMED);
    }

    @Test
    @Transactional
    public void getAllDeviceNotAlivesByConfirmedIsInShouldWork() throws Exception {
        // Initialize the database
        deviceNotAliveRepository.saveAndFlush(deviceNotAlive);

        // Get all the deviceNotAliveList where confirmed in DEFAULT_CONFIRMED or UPDATED_CONFIRMED
        defaultDeviceNotAliveShouldBeFound("confirmed.in=" + DEFAULT_CONFIRMED + "," + UPDATED_CONFIRMED);

        // Get all the deviceNotAliveList where confirmed equals to UPDATED_CONFIRMED
        defaultDeviceNotAliveShouldNotBeFound("confirmed.in=" + UPDATED_CONFIRMED);
    }

    @Test
    @Transactional
    public void getAllDeviceNotAlivesByConfirmedIsNullOrNotNull() throws Exception {
        // Initialize the database
        deviceNotAliveRepository.saveAndFlush(deviceNotAlive);

        // Get all the deviceNotAliveList where confirmed is not null
        defaultDeviceNotAliveShouldBeFound("confirmed.specified=true");

        // Get all the deviceNotAliveList where confirmed is null
        defaultDeviceNotAliveShouldNotBeFound("confirmed.specified=false");
    }

    @Test
    @Transactional
    public void getAllDeviceNotAlivesByUserIsEqualToSomething() throws Exception {
        // Initialize the database
        deviceNotAliveRepository.saveAndFlush(deviceNotAlive);
        User user = UserResourceIT.createEntity(em);
        em.persist(user);
        em.flush();
        deviceNotAlive.setUser(user);
        deviceNotAliveRepository.saveAndFlush(deviceNotAlive);
        Long userId = user.getId();

        // Get all the deviceNotAliveList where user equals to userId
        defaultDeviceNotAliveShouldNotBeFound("userId.equals=" + userId);

        // Get all the deviceNotAliveList where user equals to userId + 1
        defaultDeviceNotAliveShouldNotBeFound("userId.equals=" + (userId + 1));
    }

    /**
     * Executes the search, and checks that the default entity is returned.
     */
    private void defaultDeviceNotAliveShouldBeFound(String filter) throws Exception {
        restDeviceNotAliveMockMvc
            .perform(get("/api/device-not-alives?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(deviceNotAlive.getId().intValue())))
            .andExpect(jsonPath("$.[*].occured").value(hasItem(sameInstant(DEFAULT_OCCURED))))
            .andExpect(jsonPath("$.[*].confirmed").value(hasItem(DEFAULT_CONFIRMED.booleanValue())));

        // Check, that the count call also returns 1
        restDeviceNotAliveMockMvc
            .perform(get("/api/device-not-alives/count?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(content().string("1"));
    }

    /**
     * Executes the search, and checks that the default entity is not returned.
     */
    private void defaultDeviceNotAliveShouldNotBeFound(String filter) throws Exception {
        restDeviceNotAliveMockMvc
            .perform(get("/api/device-not-alives?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$").isArray())
            .andExpect(jsonPath("$").isEmpty());

        // Check, that the count call also returns 0
        restDeviceNotAliveMockMvc
            .perform(get("/api/device-not-alives/count?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(content().string("0"));
    }

    @Test
    @Transactional
    public void getNonExistingDeviceNotAlive() throws Exception {
        // Get the deviceNotAlive
        restDeviceNotAliveMockMvc.perform(get("/api/device-not-alives/{id}", Long.MAX_VALUE)).andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    public void updateDeviceNotAlive() throws Exception {
        // Initialize the database
        deviceNotAliveService.save(deviceNotAlive);

        int databaseSizeBeforeUpdate = deviceNotAliveRepository.findAll().size();

        // Update the deviceNotAlive
        DeviceNotAlive updatedDeviceNotAlive = deviceNotAliveRepository.findById(deviceNotAlive.getId()).get();
        // Disconnect from session so that the updates on updatedDeviceNotAlive are not directly saved in db
        em.detach(updatedDeviceNotAlive);
        updatedDeviceNotAlive.occured(UPDATED_OCCURED).confirmed(UPDATED_CONFIRMED);

        restDeviceNotAliveMockMvc
            .perform(
                put("/api/device-not-alives")
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(updatedDeviceNotAlive))
            )
            .andExpect(status().isOk());

        // Validate the DeviceNotAlive in the database
        List<DeviceNotAlive> deviceNotAliveList = deviceNotAliveRepository.findAll();
        assertThat(deviceNotAliveList).hasSize(databaseSizeBeforeUpdate);
        DeviceNotAlive testDeviceNotAlive = deviceNotAliveList.get(deviceNotAliveList.size() - 1);
        assertThat(testDeviceNotAlive.getOccured()).isEqualTo(UPDATED_OCCURED);
        assertThat(testDeviceNotAlive.isConfirmed()).isEqualTo(UPDATED_CONFIRMED);
    }

    @Test
    @Transactional
    public void updateNonExistingDeviceNotAlive() throws Exception {
        int databaseSizeBeforeUpdate = deviceNotAliveRepository.findAll().size();

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restDeviceNotAliveMockMvc
            .perform(
                put("/api/device-not-alives")
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(deviceNotAlive))
            )
            .andExpect(status().isBadRequest());

        // Validate the DeviceNotAlive in the database
        List<DeviceNotAlive> deviceNotAliveList = deviceNotAliveRepository.findAll();
        assertThat(deviceNotAliveList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    public void deleteDeviceNotAlive() throws Exception {
        // Initialize the database
        deviceNotAliveService.save(deviceNotAlive);

        int databaseSizeBeforeDelete = deviceNotAliveRepository.findAll().size();

        // Delete the deviceNotAlive
        restDeviceNotAliveMockMvc
            .perform(delete("/api/device-not-alives/{id}", deviceNotAlive.getId()).accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        List<DeviceNotAlive> deviceNotAliveList = deviceNotAliveRepository.findAll();
        assertThat(deviceNotAliveList).hasSize(databaseSizeBeforeDelete - 1);
    }
}
