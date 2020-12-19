package de.svenleonhard.alive.web.rest;

import static de.svenleonhard.alive.web.rest.TestUtil.sameInstant;
import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import de.svenleonhard.alive.AliveServiceApp;
import de.svenleonhard.alive.domain.RegisterMessage;
import de.svenleonhard.alive.domain.User;
import de.svenleonhard.alive.repository.RegisterMessageRepository;
import de.svenleonhard.alive.service.RegisterMessageQueryService;
import de.svenleonhard.alive.service.RegisterMessageService;
import de.svenleonhard.alive.service.dto.RegisterMessageCriteria;
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
 * Integration tests for the {@link RegisterMessageResource} REST controller.
 */
@SpringBootTest(classes = AliveServiceApp.class)
@AutoConfigureMockMvc
@WithMockUser
public class RegisterMessageResourceIT {
    private static final ZonedDateTime DEFAULT_SENDTIME = ZonedDateTime.ofInstant(Instant.ofEpochMilli(0L), ZoneOffset.UTC);
    private static final ZonedDateTime UPDATED_SENDTIME = ZonedDateTime.now(ZoneId.systemDefault()).withNano(0);
    private static final ZonedDateTime SMALLER_SENDTIME = ZonedDateTime.ofInstant(Instant.ofEpochMilli(-1L), ZoneOffset.UTC);

    private static final ZonedDateTime DEFAULT_RECEIVETIME = ZonedDateTime.ofInstant(Instant.ofEpochMilli(0L), ZoneOffset.UTC);
    private static final ZonedDateTime UPDATED_RECEIVETIME = ZonedDateTime.now(ZoneId.systemDefault()).withNano(0);
    private static final ZonedDateTime SMALLER_RECEIVETIME = ZonedDateTime.ofInstant(Instant.ofEpochMilli(-1L), ZoneOffset.UTC);

    private static final Integer DEFAULT_RETRYCOUNT = 1;
    private static final Integer UPDATED_RETRYCOUNT = 2;
    private static final Integer SMALLER_RETRYCOUNT = 1 - 1;

    @Autowired
    private RegisterMessageRepository registerMessageRepository;

    @Autowired
    private RegisterMessageService registerMessageService;

    @Autowired
    private RegisterMessageQueryService registerMessageQueryService;

    @Autowired
    private EntityManager em;

    @Autowired
    private MockMvc restRegisterMessageMockMvc;

    private RegisterMessage registerMessage;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static RegisterMessage createEntity(EntityManager em) {
        RegisterMessage registerMessage = new RegisterMessage()
            .sendtime(DEFAULT_SENDTIME)
            .receivetime(DEFAULT_RECEIVETIME)
            .retrycount(DEFAULT_RETRYCOUNT);
        return registerMessage;
    }

    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static RegisterMessage createUpdatedEntity(EntityManager em) {
        RegisterMessage registerMessage = new RegisterMessage()
            .sendtime(UPDATED_SENDTIME)
            .receivetime(UPDATED_RECEIVETIME)
            .retrycount(UPDATED_RETRYCOUNT);
        return registerMessage;
    }

    @BeforeEach
    public void initTest() {
        registerMessage = createEntity(em);
    }

    @Test
    @Transactional
    public void createRegisterMessage() throws Exception {
        int databaseSizeBeforeCreate = registerMessageRepository.findAll().size();
        // Create the RegisterMessage
        restRegisterMessageMockMvc
            .perform(
                post("/api/register-messages")
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(registerMessage))
            )
            .andExpect(status().isCreated());

        // Validate the RegisterMessage in the database
        List<RegisterMessage> registerMessageList = registerMessageRepository.findAll();
        assertThat(registerMessageList).hasSize(databaseSizeBeforeCreate + 1);
        RegisterMessage testRegisterMessage = registerMessageList.get(registerMessageList.size() - 1);
        assertThat(testRegisterMessage.getSendtime()).isEqualTo(DEFAULT_SENDTIME);
        assertThat(testRegisterMessage.getReceivetime()).isEqualTo(DEFAULT_RECEIVETIME);
        assertThat(testRegisterMessage.getRetrycount()).isEqualTo(DEFAULT_RETRYCOUNT);
    }

    @Test
    @Transactional
    public void createRegisterMessageWithExistingId() throws Exception {
        int databaseSizeBeforeCreate = registerMessageRepository.findAll().size();

        // Create the RegisterMessage with an existing ID
        registerMessage.setId(1L);

        // An entity with an existing ID cannot be created, so this API call must fail
        restRegisterMessageMockMvc
            .perform(
                post("/api/register-messages")
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(registerMessage))
            )
            .andExpect(status().isBadRequest());

        // Validate the RegisterMessage in the database
        List<RegisterMessage> registerMessageList = registerMessageRepository.findAll();
        assertThat(registerMessageList).hasSize(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    public void getAllRegisterMessages() throws Exception {
        // Initialize the database
        registerMessageRepository.saveAndFlush(registerMessage);

        // Get all the registerMessageList
        restRegisterMessageMockMvc
            .perform(get("/api/register-messages?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(registerMessage.getId().intValue())))
            .andExpect(jsonPath("$.[*].sendtime").value(hasItem(sameInstant(DEFAULT_SENDTIME))))
            .andExpect(jsonPath("$.[*].receivetime").value(hasItem(sameInstant(DEFAULT_RECEIVETIME))))
            .andExpect(jsonPath("$.[*].retrycount").value(hasItem(DEFAULT_RETRYCOUNT)));
    }

    @Test
    @Transactional
    public void getRegisterMessage() throws Exception {
        // Initialize the database
        registerMessageRepository.saveAndFlush(registerMessage);

        // Get the registerMessage
        restRegisterMessageMockMvc
            .perform(get("/api/register-messages/{id}", registerMessage.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.id").value(registerMessage.getId().intValue()))
            .andExpect(jsonPath("$.sendtime").value(sameInstant(DEFAULT_SENDTIME)))
            .andExpect(jsonPath("$.receivetime").value(sameInstant(DEFAULT_RECEIVETIME)))
            .andExpect(jsonPath("$.retrycount").value(DEFAULT_RETRYCOUNT));
    }

    @Test
    @Transactional
    public void getRegisterMessagesByIdFiltering() throws Exception {
        // Initialize the database
        registerMessageRepository.saveAndFlush(registerMessage);

        Long id = registerMessage.getId();

        defaultRegisterMessageShouldBeFound("id.equals=" + id);
        defaultRegisterMessageShouldNotBeFound("id.notEquals=" + id);

        defaultRegisterMessageShouldBeFound("id.greaterThanOrEqual=" + id);
        defaultRegisterMessageShouldNotBeFound("id.greaterThan=" + id);

        defaultRegisterMessageShouldBeFound("id.lessThanOrEqual=" + id);
        defaultRegisterMessageShouldNotBeFound("id.lessThan=" + id);
    }

    @Test
    @Transactional
    public void getAllRegisterMessagesBySendtimeIsEqualToSomething() throws Exception {
        // Initialize the database
        registerMessageRepository.saveAndFlush(registerMessage);

        // Get all the registerMessageList where sendtime equals to DEFAULT_SENDTIME
        defaultRegisterMessageShouldBeFound("sendtime.equals=" + DEFAULT_SENDTIME);

        // Get all the registerMessageList where sendtime equals to UPDATED_SENDTIME
        defaultRegisterMessageShouldNotBeFound("sendtime.equals=" + UPDATED_SENDTIME);
    }

    @Test
    @Transactional
    public void getAllRegisterMessagesBySendtimeIsNotEqualToSomething() throws Exception {
        // Initialize the database
        registerMessageRepository.saveAndFlush(registerMessage);

        // Get all the registerMessageList where sendtime not equals to DEFAULT_SENDTIME
        defaultRegisterMessageShouldNotBeFound("sendtime.notEquals=" + DEFAULT_SENDTIME);

        // Get all the registerMessageList where sendtime not equals to UPDATED_SENDTIME
        defaultRegisterMessageShouldBeFound("sendtime.notEquals=" + UPDATED_SENDTIME);
    }

    @Test
    @Transactional
    public void getAllRegisterMessagesBySendtimeIsInShouldWork() throws Exception {
        // Initialize the database
        registerMessageRepository.saveAndFlush(registerMessage);

        // Get all the registerMessageList where sendtime in DEFAULT_SENDTIME or UPDATED_SENDTIME
        defaultRegisterMessageShouldBeFound("sendtime.in=" + DEFAULT_SENDTIME + "," + UPDATED_SENDTIME);

        // Get all the registerMessageList where sendtime equals to UPDATED_SENDTIME
        defaultRegisterMessageShouldNotBeFound("sendtime.in=" + UPDATED_SENDTIME);
    }

    @Test
    @Transactional
    public void getAllRegisterMessagesBySendtimeIsNullOrNotNull() throws Exception {
        // Initialize the database
        registerMessageRepository.saveAndFlush(registerMessage);

        // Get all the registerMessageList where sendtime is not null
        defaultRegisterMessageShouldBeFound("sendtime.specified=true");

        // Get all the registerMessageList where sendtime is null
        defaultRegisterMessageShouldNotBeFound("sendtime.specified=false");
    }

    @Test
    @Transactional
    public void getAllRegisterMessagesBySendtimeIsGreaterThanOrEqualToSomething() throws Exception {
        // Initialize the database
        registerMessageRepository.saveAndFlush(registerMessage);

        // Get all the registerMessageList where sendtime is greater than or equal to DEFAULT_SENDTIME
        defaultRegisterMessageShouldBeFound("sendtime.greaterThanOrEqual=" + DEFAULT_SENDTIME);

        // Get all the registerMessageList where sendtime is greater than or equal to UPDATED_SENDTIME
        defaultRegisterMessageShouldNotBeFound("sendtime.greaterThanOrEqual=" + UPDATED_SENDTIME);
    }

    @Test
    @Transactional
    public void getAllRegisterMessagesBySendtimeIsLessThanOrEqualToSomething() throws Exception {
        // Initialize the database
        registerMessageRepository.saveAndFlush(registerMessage);

        // Get all the registerMessageList where sendtime is less than or equal to DEFAULT_SENDTIME
        defaultRegisterMessageShouldBeFound("sendtime.lessThanOrEqual=" + DEFAULT_SENDTIME);

        // Get all the registerMessageList where sendtime is less than or equal to SMALLER_SENDTIME
        defaultRegisterMessageShouldNotBeFound("sendtime.lessThanOrEqual=" + SMALLER_SENDTIME);
    }

    @Test
    @Transactional
    public void getAllRegisterMessagesBySendtimeIsLessThanSomething() throws Exception {
        // Initialize the database
        registerMessageRepository.saveAndFlush(registerMessage);

        // Get all the registerMessageList where sendtime is less than DEFAULT_SENDTIME
        defaultRegisterMessageShouldNotBeFound("sendtime.lessThan=" + DEFAULT_SENDTIME);

        // Get all the registerMessageList where sendtime is less than UPDATED_SENDTIME
        defaultRegisterMessageShouldBeFound("sendtime.lessThan=" + UPDATED_SENDTIME);
    }

    @Test
    @Transactional
    public void getAllRegisterMessagesBySendtimeIsGreaterThanSomething() throws Exception {
        // Initialize the database
        registerMessageRepository.saveAndFlush(registerMessage);

        // Get all the registerMessageList where sendtime is greater than DEFAULT_SENDTIME
        defaultRegisterMessageShouldNotBeFound("sendtime.greaterThan=" + DEFAULT_SENDTIME);

        // Get all the registerMessageList where sendtime is greater than SMALLER_SENDTIME
        defaultRegisterMessageShouldBeFound("sendtime.greaterThan=" + SMALLER_SENDTIME);
    }

    @Test
    @Transactional
    public void getAllRegisterMessagesByReceivetimeIsEqualToSomething() throws Exception {
        // Initialize the database
        registerMessageRepository.saveAndFlush(registerMessage);

        // Get all the registerMessageList where receivetime equals to DEFAULT_RECEIVETIME
        defaultRegisterMessageShouldBeFound("receivetime.equals=" + DEFAULT_RECEIVETIME);

        // Get all the registerMessageList where receivetime equals to UPDATED_RECEIVETIME
        defaultRegisterMessageShouldNotBeFound("receivetime.equals=" + UPDATED_RECEIVETIME);
    }

    @Test
    @Transactional
    public void getAllRegisterMessagesByReceivetimeIsNotEqualToSomething() throws Exception {
        // Initialize the database
        registerMessageRepository.saveAndFlush(registerMessage);

        // Get all the registerMessageList where receivetime not equals to DEFAULT_RECEIVETIME
        defaultRegisterMessageShouldNotBeFound("receivetime.notEquals=" + DEFAULT_RECEIVETIME);

        // Get all the registerMessageList where receivetime not equals to UPDATED_RECEIVETIME
        defaultRegisterMessageShouldBeFound("receivetime.notEquals=" + UPDATED_RECEIVETIME);
    }

    @Test
    @Transactional
    public void getAllRegisterMessagesByReceivetimeIsInShouldWork() throws Exception {
        // Initialize the database
        registerMessageRepository.saveAndFlush(registerMessage);

        // Get all the registerMessageList where receivetime in DEFAULT_RECEIVETIME or UPDATED_RECEIVETIME
        defaultRegisterMessageShouldBeFound("receivetime.in=" + DEFAULT_RECEIVETIME + "," + UPDATED_RECEIVETIME);

        // Get all the registerMessageList where receivetime equals to UPDATED_RECEIVETIME
        defaultRegisterMessageShouldNotBeFound("receivetime.in=" + UPDATED_RECEIVETIME);
    }

    @Test
    @Transactional
    public void getAllRegisterMessagesByReceivetimeIsNullOrNotNull() throws Exception {
        // Initialize the database
        registerMessageRepository.saveAndFlush(registerMessage);

        // Get all the registerMessageList where receivetime is not null
        defaultRegisterMessageShouldBeFound("receivetime.specified=true");

        // Get all the registerMessageList where receivetime is null
        defaultRegisterMessageShouldNotBeFound("receivetime.specified=false");
    }

    @Test
    @Transactional
    public void getAllRegisterMessagesByReceivetimeIsGreaterThanOrEqualToSomething() throws Exception {
        // Initialize the database
        registerMessageRepository.saveAndFlush(registerMessage);

        // Get all the registerMessageList where receivetime is greater than or equal to DEFAULT_RECEIVETIME
        defaultRegisterMessageShouldBeFound("receivetime.greaterThanOrEqual=" + DEFAULT_RECEIVETIME);

        // Get all the registerMessageList where receivetime is greater than or equal to UPDATED_RECEIVETIME
        defaultRegisterMessageShouldNotBeFound("receivetime.greaterThanOrEqual=" + UPDATED_RECEIVETIME);
    }

    @Test
    @Transactional
    public void getAllRegisterMessagesByReceivetimeIsLessThanOrEqualToSomething() throws Exception {
        // Initialize the database
        registerMessageRepository.saveAndFlush(registerMessage);

        // Get all the registerMessageList where receivetime is less than or equal to DEFAULT_RECEIVETIME
        defaultRegisterMessageShouldBeFound("receivetime.lessThanOrEqual=" + DEFAULT_RECEIVETIME);

        // Get all the registerMessageList where receivetime is less than or equal to SMALLER_RECEIVETIME
        defaultRegisterMessageShouldNotBeFound("receivetime.lessThanOrEqual=" + SMALLER_RECEIVETIME);
    }

    @Test
    @Transactional
    public void getAllRegisterMessagesByReceivetimeIsLessThanSomething() throws Exception {
        // Initialize the database
        registerMessageRepository.saveAndFlush(registerMessage);

        // Get all the registerMessageList where receivetime is less than DEFAULT_RECEIVETIME
        defaultRegisterMessageShouldNotBeFound("receivetime.lessThan=" + DEFAULT_RECEIVETIME);

        // Get all the registerMessageList where receivetime is less than UPDATED_RECEIVETIME
        defaultRegisterMessageShouldBeFound("receivetime.lessThan=" + UPDATED_RECEIVETIME);
    }

    @Test
    @Transactional
    public void getAllRegisterMessagesByReceivetimeIsGreaterThanSomething() throws Exception {
        // Initialize the database
        registerMessageRepository.saveAndFlush(registerMessage);

        // Get all the registerMessageList where receivetime is greater than DEFAULT_RECEIVETIME
        defaultRegisterMessageShouldNotBeFound("receivetime.greaterThan=" + DEFAULT_RECEIVETIME);

        // Get all the registerMessageList where receivetime is greater than SMALLER_RECEIVETIME
        defaultRegisterMessageShouldBeFound("receivetime.greaterThan=" + SMALLER_RECEIVETIME);
    }

    @Test
    @Transactional
    public void getAllRegisterMessagesByRetrycountIsEqualToSomething() throws Exception {
        // Initialize the database
        registerMessageRepository.saveAndFlush(registerMessage);

        // Get all the registerMessageList where retrycount equals to DEFAULT_RETRYCOUNT
        defaultRegisterMessageShouldBeFound("retrycount.equals=" + DEFAULT_RETRYCOUNT);

        // Get all the registerMessageList where retrycount equals to UPDATED_RETRYCOUNT
        defaultRegisterMessageShouldNotBeFound("retrycount.equals=" + UPDATED_RETRYCOUNT);
    }

    @Test
    @Transactional
    public void getAllRegisterMessagesByRetrycountIsNotEqualToSomething() throws Exception {
        // Initialize the database
        registerMessageRepository.saveAndFlush(registerMessage);

        // Get all the registerMessageList where retrycount not equals to DEFAULT_RETRYCOUNT
        defaultRegisterMessageShouldNotBeFound("retrycount.notEquals=" + DEFAULT_RETRYCOUNT);

        // Get all the registerMessageList where retrycount not equals to UPDATED_RETRYCOUNT
        defaultRegisterMessageShouldBeFound("retrycount.notEquals=" + UPDATED_RETRYCOUNT);
    }

    @Test
    @Transactional
    public void getAllRegisterMessagesByRetrycountIsInShouldWork() throws Exception {
        // Initialize the database
        registerMessageRepository.saveAndFlush(registerMessage);

        // Get all the registerMessageList where retrycount in DEFAULT_RETRYCOUNT or UPDATED_RETRYCOUNT
        defaultRegisterMessageShouldBeFound("retrycount.in=" + DEFAULT_RETRYCOUNT + "," + UPDATED_RETRYCOUNT);

        // Get all the registerMessageList where retrycount equals to UPDATED_RETRYCOUNT
        defaultRegisterMessageShouldNotBeFound("retrycount.in=" + UPDATED_RETRYCOUNT);
    }

    @Test
    @Transactional
    public void getAllRegisterMessagesByRetrycountIsNullOrNotNull() throws Exception {
        // Initialize the database
        registerMessageRepository.saveAndFlush(registerMessage);

        // Get all the registerMessageList where retrycount is not null
        defaultRegisterMessageShouldBeFound("retrycount.specified=true");

        // Get all the registerMessageList where retrycount is null
        defaultRegisterMessageShouldNotBeFound("retrycount.specified=false");
    }

    @Test
    @Transactional
    public void getAllRegisterMessagesByRetrycountIsGreaterThanOrEqualToSomething() throws Exception {
        // Initialize the database
        registerMessageRepository.saveAndFlush(registerMessage);

        // Get all the registerMessageList where retrycount is greater than or equal to DEFAULT_RETRYCOUNT
        defaultRegisterMessageShouldBeFound("retrycount.greaterThanOrEqual=" + DEFAULT_RETRYCOUNT);

        // Get all the registerMessageList where retrycount is greater than or equal to UPDATED_RETRYCOUNT
        defaultRegisterMessageShouldNotBeFound("retrycount.greaterThanOrEqual=" + UPDATED_RETRYCOUNT);
    }

    @Test
    @Transactional
    public void getAllRegisterMessagesByRetrycountIsLessThanOrEqualToSomething() throws Exception {
        // Initialize the database
        registerMessageRepository.saveAndFlush(registerMessage);

        // Get all the registerMessageList where retrycount is less than or equal to DEFAULT_RETRYCOUNT
        defaultRegisterMessageShouldBeFound("retrycount.lessThanOrEqual=" + DEFAULT_RETRYCOUNT);

        // Get all the registerMessageList where retrycount is less than or equal to SMALLER_RETRYCOUNT
        defaultRegisterMessageShouldNotBeFound("retrycount.lessThanOrEqual=" + SMALLER_RETRYCOUNT);
    }

    @Test
    @Transactional
    public void getAllRegisterMessagesByRetrycountIsLessThanSomething() throws Exception {
        // Initialize the database
        registerMessageRepository.saveAndFlush(registerMessage);

        // Get all the registerMessageList where retrycount is less than DEFAULT_RETRYCOUNT
        defaultRegisterMessageShouldNotBeFound("retrycount.lessThan=" + DEFAULT_RETRYCOUNT);

        // Get all the registerMessageList where retrycount is less than UPDATED_RETRYCOUNT
        defaultRegisterMessageShouldBeFound("retrycount.lessThan=" + UPDATED_RETRYCOUNT);
    }

    @Test
    @Transactional
    public void getAllRegisterMessagesByRetrycountIsGreaterThanSomething() throws Exception {
        // Initialize the database
        registerMessageRepository.saveAndFlush(registerMessage);

        // Get all the registerMessageList where retrycount is greater than DEFAULT_RETRYCOUNT
        defaultRegisterMessageShouldNotBeFound("retrycount.greaterThan=" + DEFAULT_RETRYCOUNT);

        // Get all the registerMessageList where retrycount is greater than SMALLER_RETRYCOUNT
        defaultRegisterMessageShouldBeFound("retrycount.greaterThan=" + SMALLER_RETRYCOUNT);
    }

    @Test
    @Transactional
    public void getAllRegisterMessagesByUserIsEqualToSomething() throws Exception {
        // Initialize the database
        registerMessageRepository.saveAndFlush(registerMessage);
        User user = UserResourceIT.createEntity(em);
        em.persist(user);
        em.flush();
        registerMessage.setUser(user);
        registerMessageRepository.saveAndFlush(registerMessage);
        Long userId = user.getId();

        // Get all the registerMessageList where user equals to userId
        defaultRegisterMessageShouldBeFound("userId.equals=" + userId);

        // Get all the registerMessageList where user equals to userId + 1
        defaultRegisterMessageShouldNotBeFound("userId.equals=" + (userId + 1));
    }

    /**
     * Executes the search, and checks that the default entity is returned.
     */
    private void defaultRegisterMessageShouldBeFound(String filter) throws Exception {
        restRegisterMessageMockMvc
            .perform(get("/api/register-messages?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(registerMessage.getId().intValue())))
            .andExpect(jsonPath("$.[*].sendtime").value(hasItem(sameInstant(DEFAULT_SENDTIME))))
            .andExpect(jsonPath("$.[*].receivetime").value(hasItem(sameInstant(DEFAULT_RECEIVETIME))))
            .andExpect(jsonPath("$.[*].retrycount").value(hasItem(DEFAULT_RETRYCOUNT)));

        // Check, that the count call also returns 1
        restRegisterMessageMockMvc
            .perform(get("/api/register-messages/count?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(content().string("1"));
    }

    /**
     * Executes the search, and checks that the default entity is not returned.
     */
    private void defaultRegisterMessageShouldNotBeFound(String filter) throws Exception {
        restRegisterMessageMockMvc
            .perform(get("/api/register-messages?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$").isArray())
            .andExpect(jsonPath("$").isEmpty());

        // Check, that the count call also returns 0
        restRegisterMessageMockMvc
            .perform(get("/api/register-messages/count?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(content().string("0"));
    }

    @Test
    @Transactional
    public void getNonExistingRegisterMessage() throws Exception {
        // Get the registerMessage
        restRegisterMessageMockMvc.perform(get("/api/register-messages/{id}", Long.MAX_VALUE)).andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    public void updateRegisterMessage() throws Exception {
        // Initialize the database
        registerMessageService.save(registerMessage);

        int databaseSizeBeforeUpdate = registerMessageRepository.findAll().size();

        // Update the registerMessage
        RegisterMessage updatedRegisterMessage = registerMessageRepository.findById(registerMessage.getId()).get();
        // Disconnect from session so that the updates on updatedRegisterMessage are not directly saved in db
        em.detach(updatedRegisterMessage);
        updatedRegisterMessage.sendtime(UPDATED_SENDTIME).receivetime(UPDATED_RECEIVETIME).retrycount(UPDATED_RETRYCOUNT);

        restRegisterMessageMockMvc
            .perform(
                put("/api/register-messages")
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(updatedRegisterMessage))
            )
            .andExpect(status().isOk());

        // Validate the RegisterMessage in the database
        List<RegisterMessage> registerMessageList = registerMessageRepository.findAll();
        assertThat(registerMessageList).hasSize(databaseSizeBeforeUpdate);
        RegisterMessage testRegisterMessage = registerMessageList.get(registerMessageList.size() - 1);
        assertThat(testRegisterMessage.getSendtime()).isEqualTo(UPDATED_SENDTIME);
        assertThat(testRegisterMessage.getReceivetime()).isEqualTo(UPDATED_RECEIVETIME);
        assertThat(testRegisterMessage.getRetrycount()).isEqualTo(UPDATED_RETRYCOUNT);
    }

    @Test
    @Transactional
    public void updateNonExistingRegisterMessage() throws Exception {
        int databaseSizeBeforeUpdate = registerMessageRepository.findAll().size();

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restRegisterMessageMockMvc
            .perform(
                put("/api/register-messages")
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(registerMessage))
            )
            .andExpect(status().isBadRequest());

        // Validate the RegisterMessage in the database
        List<RegisterMessage> registerMessageList = registerMessageRepository.findAll();
        assertThat(registerMessageList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    public void deleteRegisterMessage() throws Exception {
        // Initialize the database
        registerMessageService.save(registerMessage);

        int databaseSizeBeforeDelete = registerMessageRepository.findAll().size();

        // Delete the registerMessage
        restRegisterMessageMockMvc
            .perform(delete("/api/register-messages/{id}", registerMessage.getId()).accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        List<RegisterMessage> registerMessageList = registerMessageRepository.findAll();
        assertThat(registerMessageList).hasSize(databaseSizeBeforeDelete - 1);
    }
}
