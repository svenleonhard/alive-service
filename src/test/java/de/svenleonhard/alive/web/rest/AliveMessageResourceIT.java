package de.svenleonhard.alive.web.rest;

import static de.svenleonhard.alive.web.rest.TestUtil.sameInstant;
import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import de.svenleonhard.alive.AliveServiceApp;
import de.svenleonhard.alive.domain.AliveMessage;
import de.svenleonhard.alive.domain.User;
import de.svenleonhard.alive.repository.AliveMessageRepository;
import de.svenleonhard.alive.service.AliveMessageQueryService;
import de.svenleonhard.alive.service.AliveMessageService;
import de.svenleonhard.alive.service.dto.AliveMessageCriteria;
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
 * Integration tests for the {@link AliveMessageResource} REST controller.
 */
@SpringBootTest(classes = AliveServiceApp.class)
@AutoConfigureMockMvc
@WithMockUser
public class AliveMessageResourceIT {
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
    private AliveMessageRepository aliveMessageRepository;

    @Autowired
    private AliveMessageService aliveMessageService;

    @Autowired
    private AliveMessageQueryService aliveMessageQueryService;

    @Autowired
    private EntityManager em;

    @Autowired
    private MockMvc restAliveMessageMockMvc;

    private AliveMessage aliveMessage;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static AliveMessage createEntity(EntityManager em) {
        AliveMessage aliveMessage = new AliveMessage()
            .sendtime(DEFAULT_SENDTIME)
            .receivetime(DEFAULT_RECEIVETIME)
            .retrycount(DEFAULT_RETRYCOUNT);
        return aliveMessage;
    }

    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static AliveMessage createUpdatedEntity(EntityManager em) {
        AliveMessage aliveMessage = new AliveMessage()
            .sendtime(UPDATED_SENDTIME)
            .receivetime(UPDATED_RECEIVETIME)
            .retrycount(UPDATED_RETRYCOUNT);
        return aliveMessage;
    }

    @BeforeEach
    public void initTest() {
        aliveMessage = createEntity(em);
    }

    @Test
    @Transactional
    public void createAliveMessage() throws Exception {
        int databaseSizeBeforeCreate = aliveMessageRepository.findAll().size();
        // Create the AliveMessage
        restAliveMessageMockMvc
            .perform(
                post("/api/alive-messages").contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(aliveMessage))
            )
            .andExpect(status().isCreated());

        // Validate the AliveMessage in the database
        List<AliveMessage> aliveMessageList = aliveMessageRepository.findAll();
        assertThat(aliveMessageList).hasSize(databaseSizeBeforeCreate + 1);
        AliveMessage testAliveMessage = aliveMessageList.get(aliveMessageList.size() - 1);
        assertThat(testAliveMessage.getSendtime()).isEqualTo(DEFAULT_SENDTIME);
        assertThat(testAliveMessage.getReceivetime()).isEqualTo(DEFAULT_RECEIVETIME);
        assertThat(testAliveMessage.getRetrycount()).isEqualTo(DEFAULT_RETRYCOUNT);
    }

    @Test
    @Transactional
    public void createAliveMessageWithExistingId() throws Exception {
        int databaseSizeBeforeCreate = aliveMessageRepository.findAll().size();

        // Create the AliveMessage with an existing ID
        aliveMessage.setId(1L);

        // An entity with an existing ID cannot be created, so this API call must fail
        restAliveMessageMockMvc
            .perform(
                post("/api/alive-messages").contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(aliveMessage))
            )
            .andExpect(status().isBadRequest());

        // Validate the AliveMessage in the database
        List<AliveMessage> aliveMessageList = aliveMessageRepository.findAll();
        assertThat(aliveMessageList).hasSize(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    public void getAllAliveMessages() throws Exception {
        // Initialize the database
        aliveMessageRepository.saveAndFlush(aliveMessage);

        // Get all the aliveMessageList
        restAliveMessageMockMvc
            .perform(get("/api/alive-messages?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(aliveMessage.getId().intValue())))
            .andExpect(jsonPath("$.[*].sendtime").value(hasItem(sameInstant(DEFAULT_SENDTIME))))
            .andExpect(jsonPath("$.[*].receivetime").value(hasItem(sameInstant(DEFAULT_RECEIVETIME))))
            .andExpect(jsonPath("$.[*].retrycount").value(hasItem(DEFAULT_RETRYCOUNT)));
    }

    @Test
    @Transactional
    public void getAliveMessage() throws Exception {
        // Initialize the database
        aliveMessageRepository.saveAndFlush(aliveMessage);

        // Get the aliveMessage
        restAliveMessageMockMvc
            .perform(get("/api/alive-messages/{id}", aliveMessage.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.id").value(aliveMessage.getId().intValue()))
            .andExpect(jsonPath("$.sendtime").value(sameInstant(DEFAULT_SENDTIME)))
            .andExpect(jsonPath("$.receivetime").value(sameInstant(DEFAULT_RECEIVETIME)))
            .andExpect(jsonPath("$.retrycount").value(DEFAULT_RETRYCOUNT));
    }

    @Test
    @Transactional
    public void getAliveMessagesByIdFiltering() throws Exception {
        // Initialize the database
        aliveMessageRepository.saveAndFlush(aliveMessage);

        Long id = aliveMessage.getId();

        defaultAliveMessageShouldBeFound("id.equals=" + id);
        defaultAliveMessageShouldNotBeFound("id.notEquals=" + id);

        defaultAliveMessageShouldBeFound("id.greaterThanOrEqual=" + id);
        defaultAliveMessageShouldNotBeFound("id.greaterThan=" + id);

        defaultAliveMessageShouldBeFound("id.lessThanOrEqual=" + id);
        defaultAliveMessageShouldNotBeFound("id.lessThan=" + id);
    }

    @Test
    @Transactional
    public void getAllAliveMessagesBySendtimeIsEqualToSomething() throws Exception {
        // Initialize the database
        aliveMessageRepository.saveAndFlush(aliveMessage);

        // Get all the aliveMessageList where sendtime equals to DEFAULT_SENDTIME
        defaultAliveMessageShouldBeFound("sendtime.equals=" + DEFAULT_SENDTIME);

        // Get all the aliveMessageList where sendtime equals to UPDATED_SENDTIME
        defaultAliveMessageShouldNotBeFound("sendtime.equals=" + UPDATED_SENDTIME);
    }

    @Test
    @Transactional
    public void getAllAliveMessagesBySendtimeIsNotEqualToSomething() throws Exception {
        // Initialize the database
        aliveMessageRepository.saveAndFlush(aliveMessage);

        // Get all the aliveMessageList where sendtime not equals to DEFAULT_SENDTIME
        defaultAliveMessageShouldNotBeFound("sendtime.notEquals=" + DEFAULT_SENDTIME);

        // Get all the aliveMessageList where sendtime not equals to UPDATED_SENDTIME
        defaultAliveMessageShouldBeFound("sendtime.notEquals=" + UPDATED_SENDTIME);
    }

    @Test
    @Transactional
    public void getAllAliveMessagesBySendtimeIsInShouldWork() throws Exception {
        // Initialize the database
        aliveMessageRepository.saveAndFlush(aliveMessage);

        // Get all the aliveMessageList where sendtime in DEFAULT_SENDTIME or UPDATED_SENDTIME
        defaultAliveMessageShouldBeFound("sendtime.in=" + DEFAULT_SENDTIME + "," + UPDATED_SENDTIME);

        // Get all the aliveMessageList where sendtime equals to UPDATED_SENDTIME
        defaultAliveMessageShouldNotBeFound("sendtime.in=" + UPDATED_SENDTIME);
    }

    @Test
    @Transactional
    public void getAllAliveMessagesBySendtimeIsNullOrNotNull() throws Exception {
        // Initialize the database
        aliveMessageRepository.saveAndFlush(aliveMessage);

        // Get all the aliveMessageList where sendtime is not null
        defaultAliveMessageShouldBeFound("sendtime.specified=true");

        // Get all the aliveMessageList where sendtime is null
        defaultAliveMessageShouldNotBeFound("sendtime.specified=false");
    }

    @Test
    @Transactional
    public void getAllAliveMessagesBySendtimeIsGreaterThanOrEqualToSomething() throws Exception {
        // Initialize the database
        aliveMessageRepository.saveAndFlush(aliveMessage);

        // Get all the aliveMessageList where sendtime is greater than or equal to DEFAULT_SENDTIME
        defaultAliveMessageShouldBeFound("sendtime.greaterThanOrEqual=" + DEFAULT_SENDTIME);

        // Get all the aliveMessageList where sendtime is greater than or equal to UPDATED_SENDTIME
        defaultAliveMessageShouldNotBeFound("sendtime.greaterThanOrEqual=" + UPDATED_SENDTIME);
    }

    @Test
    @Transactional
    public void getAllAliveMessagesBySendtimeIsLessThanOrEqualToSomething() throws Exception {
        // Initialize the database
        aliveMessageRepository.saveAndFlush(aliveMessage);

        // Get all the aliveMessageList where sendtime is less than or equal to DEFAULT_SENDTIME
        defaultAliveMessageShouldBeFound("sendtime.lessThanOrEqual=" + DEFAULT_SENDTIME);

        // Get all the aliveMessageList where sendtime is less than or equal to SMALLER_SENDTIME
        defaultAliveMessageShouldNotBeFound("sendtime.lessThanOrEqual=" + SMALLER_SENDTIME);
    }

    @Test
    @Transactional
    public void getAllAliveMessagesBySendtimeIsLessThanSomething() throws Exception {
        // Initialize the database
        aliveMessageRepository.saveAndFlush(aliveMessage);

        // Get all the aliveMessageList where sendtime is less than DEFAULT_SENDTIME
        defaultAliveMessageShouldNotBeFound("sendtime.lessThan=" + DEFAULT_SENDTIME);

        // Get all the aliveMessageList where sendtime is less than UPDATED_SENDTIME
        defaultAliveMessageShouldBeFound("sendtime.lessThan=" + UPDATED_SENDTIME);
    }

    @Test
    @Transactional
    public void getAllAliveMessagesBySendtimeIsGreaterThanSomething() throws Exception {
        // Initialize the database
        aliveMessageRepository.saveAndFlush(aliveMessage);

        // Get all the aliveMessageList where sendtime is greater than DEFAULT_SENDTIME
        defaultAliveMessageShouldNotBeFound("sendtime.greaterThan=" + DEFAULT_SENDTIME);

        // Get all the aliveMessageList where sendtime is greater than SMALLER_SENDTIME
        defaultAliveMessageShouldBeFound("sendtime.greaterThan=" + SMALLER_SENDTIME);
    }

    @Test
    @Transactional
    public void getAllAliveMessagesByReceivetimeIsEqualToSomething() throws Exception {
        // Initialize the database
        aliveMessageRepository.saveAndFlush(aliveMessage);

        // Get all the aliveMessageList where receivetime equals to DEFAULT_RECEIVETIME
        defaultAliveMessageShouldBeFound("receivetime.equals=" + DEFAULT_RECEIVETIME);

        // Get all the aliveMessageList where receivetime equals to UPDATED_RECEIVETIME
        defaultAliveMessageShouldNotBeFound("receivetime.equals=" + UPDATED_RECEIVETIME);
    }

    @Test
    @Transactional
    public void getAllAliveMessagesByReceivetimeIsNotEqualToSomething() throws Exception {
        // Initialize the database
        aliveMessageRepository.saveAndFlush(aliveMessage);

        // Get all the aliveMessageList where receivetime not equals to DEFAULT_RECEIVETIME
        defaultAliveMessageShouldNotBeFound("receivetime.notEquals=" + DEFAULT_RECEIVETIME);

        // Get all the aliveMessageList where receivetime not equals to UPDATED_RECEIVETIME
        defaultAliveMessageShouldBeFound("receivetime.notEquals=" + UPDATED_RECEIVETIME);
    }

    @Test
    @Transactional
    public void getAllAliveMessagesByReceivetimeIsInShouldWork() throws Exception {
        // Initialize the database
        aliveMessageRepository.saveAndFlush(aliveMessage);

        // Get all the aliveMessageList where receivetime in DEFAULT_RECEIVETIME or UPDATED_RECEIVETIME
        defaultAliveMessageShouldBeFound("receivetime.in=" + DEFAULT_RECEIVETIME + "," + UPDATED_RECEIVETIME);

        // Get all the aliveMessageList where receivetime equals to UPDATED_RECEIVETIME
        defaultAliveMessageShouldNotBeFound("receivetime.in=" + UPDATED_RECEIVETIME);
    }

    @Test
    @Transactional
    public void getAllAliveMessagesByReceivetimeIsNullOrNotNull() throws Exception {
        // Initialize the database
        aliveMessageRepository.saveAndFlush(aliveMessage);

        // Get all the aliveMessageList where receivetime is not null
        defaultAliveMessageShouldBeFound("receivetime.specified=true");

        // Get all the aliveMessageList where receivetime is null
        defaultAliveMessageShouldNotBeFound("receivetime.specified=false");
    }

    @Test
    @Transactional
    public void getAllAliveMessagesByReceivetimeIsGreaterThanOrEqualToSomething() throws Exception {
        // Initialize the database
        aliveMessageRepository.saveAndFlush(aliveMessage);

        // Get all the aliveMessageList where receivetime is greater than or equal to DEFAULT_RECEIVETIME
        defaultAliveMessageShouldBeFound("receivetime.greaterThanOrEqual=" + DEFAULT_RECEIVETIME);

        // Get all the aliveMessageList where receivetime is greater than or equal to UPDATED_RECEIVETIME
        defaultAliveMessageShouldNotBeFound("receivetime.greaterThanOrEqual=" + UPDATED_RECEIVETIME);
    }

    @Test
    @Transactional
    public void getAllAliveMessagesByReceivetimeIsLessThanOrEqualToSomething() throws Exception {
        // Initialize the database
        aliveMessageRepository.saveAndFlush(aliveMessage);

        // Get all the aliveMessageList where receivetime is less than or equal to DEFAULT_RECEIVETIME
        defaultAliveMessageShouldBeFound("receivetime.lessThanOrEqual=" + DEFAULT_RECEIVETIME);

        // Get all the aliveMessageList where receivetime is less than or equal to SMALLER_RECEIVETIME
        defaultAliveMessageShouldNotBeFound("receivetime.lessThanOrEqual=" + SMALLER_RECEIVETIME);
    }

    @Test
    @Transactional
    public void getAllAliveMessagesByReceivetimeIsLessThanSomething() throws Exception {
        // Initialize the database
        aliveMessageRepository.saveAndFlush(aliveMessage);

        // Get all the aliveMessageList where receivetime is less than DEFAULT_RECEIVETIME
        defaultAliveMessageShouldNotBeFound("receivetime.lessThan=" + DEFAULT_RECEIVETIME);

        // Get all the aliveMessageList where receivetime is less than UPDATED_RECEIVETIME
        defaultAliveMessageShouldBeFound("receivetime.lessThan=" + UPDATED_RECEIVETIME);
    }

    @Test
    @Transactional
    public void getAllAliveMessagesByReceivetimeIsGreaterThanSomething() throws Exception {
        // Initialize the database
        aliveMessageRepository.saveAndFlush(aliveMessage);

        // Get all the aliveMessageList where receivetime is greater than DEFAULT_RECEIVETIME
        defaultAliveMessageShouldNotBeFound("receivetime.greaterThan=" + DEFAULT_RECEIVETIME);

        // Get all the aliveMessageList where receivetime is greater than SMALLER_RECEIVETIME
        defaultAliveMessageShouldBeFound("receivetime.greaterThan=" + SMALLER_RECEIVETIME);
    }

    @Test
    @Transactional
    public void getAllAliveMessagesByRetrycountIsEqualToSomething() throws Exception {
        // Initialize the database
        aliveMessageRepository.saveAndFlush(aliveMessage);

        // Get all the aliveMessageList where retrycount equals to DEFAULT_RETRYCOUNT
        defaultAliveMessageShouldBeFound("retrycount.equals=" + DEFAULT_RETRYCOUNT);

        // Get all the aliveMessageList where retrycount equals to UPDATED_RETRYCOUNT
        defaultAliveMessageShouldNotBeFound("retrycount.equals=" + UPDATED_RETRYCOUNT);
    }

    @Test
    @Transactional
    public void getAllAliveMessagesByRetrycountIsNotEqualToSomething() throws Exception {
        // Initialize the database
        aliveMessageRepository.saveAndFlush(aliveMessage);

        // Get all the aliveMessageList where retrycount not equals to DEFAULT_RETRYCOUNT
        defaultAliveMessageShouldNotBeFound("retrycount.notEquals=" + DEFAULT_RETRYCOUNT);

        // Get all the aliveMessageList where retrycount not equals to UPDATED_RETRYCOUNT
        defaultAliveMessageShouldBeFound("retrycount.notEquals=" + UPDATED_RETRYCOUNT);
    }

    @Test
    @Transactional
    public void getAllAliveMessagesByRetrycountIsInShouldWork() throws Exception {
        // Initialize the database
        aliveMessageRepository.saveAndFlush(aliveMessage);

        // Get all the aliveMessageList where retrycount in DEFAULT_RETRYCOUNT or UPDATED_RETRYCOUNT
        defaultAliveMessageShouldBeFound("retrycount.in=" + DEFAULT_RETRYCOUNT + "," + UPDATED_RETRYCOUNT);

        // Get all the aliveMessageList where retrycount equals to UPDATED_RETRYCOUNT
        defaultAliveMessageShouldNotBeFound("retrycount.in=" + UPDATED_RETRYCOUNT);
    }

    @Test
    @Transactional
    public void getAllAliveMessagesByRetrycountIsNullOrNotNull() throws Exception {
        // Initialize the database
        aliveMessageRepository.saveAndFlush(aliveMessage);

        // Get all the aliveMessageList where retrycount is not null
        defaultAliveMessageShouldBeFound("retrycount.specified=true");

        // Get all the aliveMessageList where retrycount is null
        defaultAliveMessageShouldNotBeFound("retrycount.specified=false");
    }

    @Test
    @Transactional
    public void getAllAliveMessagesByRetrycountIsGreaterThanOrEqualToSomething() throws Exception {
        // Initialize the database
        aliveMessageRepository.saveAndFlush(aliveMessage);

        // Get all the aliveMessageList where retrycount is greater than or equal to DEFAULT_RETRYCOUNT
        defaultAliveMessageShouldBeFound("retrycount.greaterThanOrEqual=" + DEFAULT_RETRYCOUNT);

        // Get all the aliveMessageList where retrycount is greater than or equal to UPDATED_RETRYCOUNT
        defaultAliveMessageShouldNotBeFound("retrycount.greaterThanOrEqual=" + UPDATED_RETRYCOUNT);
    }

    @Test
    @Transactional
    public void getAllAliveMessagesByRetrycountIsLessThanOrEqualToSomething() throws Exception {
        // Initialize the database
        aliveMessageRepository.saveAndFlush(aliveMessage);

        // Get all the aliveMessageList where retrycount is less than or equal to DEFAULT_RETRYCOUNT
        defaultAliveMessageShouldBeFound("retrycount.lessThanOrEqual=" + DEFAULT_RETRYCOUNT);

        // Get all the aliveMessageList where retrycount is less than or equal to SMALLER_RETRYCOUNT
        defaultAliveMessageShouldNotBeFound("retrycount.lessThanOrEqual=" + SMALLER_RETRYCOUNT);
    }

    @Test
    @Transactional
    public void getAllAliveMessagesByRetrycountIsLessThanSomething() throws Exception {
        // Initialize the database
        aliveMessageRepository.saveAndFlush(aliveMessage);

        // Get all the aliveMessageList where retrycount is less than DEFAULT_RETRYCOUNT
        defaultAliveMessageShouldNotBeFound("retrycount.lessThan=" + DEFAULT_RETRYCOUNT);

        // Get all the aliveMessageList where retrycount is less than UPDATED_RETRYCOUNT
        defaultAliveMessageShouldBeFound("retrycount.lessThan=" + UPDATED_RETRYCOUNT);
    }

    @Test
    @Transactional
    public void getAllAliveMessagesByRetrycountIsGreaterThanSomething() throws Exception {
        // Initialize the database
        aliveMessageRepository.saveAndFlush(aliveMessage);

        // Get all the aliveMessageList where retrycount is greater than DEFAULT_RETRYCOUNT
        defaultAliveMessageShouldNotBeFound("retrycount.greaterThan=" + DEFAULT_RETRYCOUNT);

        // Get all the aliveMessageList where retrycount is greater than SMALLER_RETRYCOUNT
        defaultAliveMessageShouldBeFound("retrycount.greaterThan=" + SMALLER_RETRYCOUNT);
    }

    @Test
    @Transactional
    public void getAllAliveMessagesByUserIsEqualToSomething() throws Exception {
        // Initialize the database
        aliveMessageRepository.saveAndFlush(aliveMessage);
        User user = UserResourceIT.createEntity(em);
        em.persist(user);
        em.flush();
        aliveMessage.setUser(user);
        aliveMessageRepository.saveAndFlush(aliveMessage);
        Long userId = user.getId();

        // Get all the aliveMessageList where user equals to userId
        defaultAliveMessageShouldBeFound("userId.equals=" + userId);

        // Get all the aliveMessageList where user equals to userId + 1
        defaultAliveMessageShouldNotBeFound("userId.equals=" + (userId + 1));
    }

    /**
     * Executes the search, and checks that the default entity is returned.
     */
    private void defaultAliveMessageShouldBeFound(String filter) throws Exception {
        restAliveMessageMockMvc
            .perform(get("/api/alive-messages?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(aliveMessage.getId().intValue())))
            .andExpect(jsonPath("$.[*].sendtime").value(hasItem(sameInstant(DEFAULT_SENDTIME))))
            .andExpect(jsonPath("$.[*].receivetime").value(hasItem(sameInstant(DEFAULT_RECEIVETIME))))
            .andExpect(jsonPath("$.[*].retrycount").value(hasItem(DEFAULT_RETRYCOUNT)));

        // Check, that the count call also returns 1
        restAliveMessageMockMvc
            .perform(get("/api/alive-messages/count?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(content().string("1"));
    }

    /**
     * Executes the search, and checks that the default entity is not returned.
     */
    private void defaultAliveMessageShouldNotBeFound(String filter) throws Exception {
        restAliveMessageMockMvc
            .perform(get("/api/alive-messages?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$").isArray())
            .andExpect(jsonPath("$").isEmpty());

        // Check, that the count call also returns 0
        restAliveMessageMockMvc
            .perform(get("/api/alive-messages/count?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(content().string("0"));
    }

    @Test
    @Transactional
    public void getNonExistingAliveMessage() throws Exception {
        // Get the aliveMessage
        restAliveMessageMockMvc.perform(get("/api/alive-messages/{id}", Long.MAX_VALUE)).andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    public void updateAliveMessage() throws Exception {
        // Initialize the database
        aliveMessageService.save(aliveMessage);

        int databaseSizeBeforeUpdate = aliveMessageRepository.findAll().size();

        // Update the aliveMessage
        AliveMessage updatedAliveMessage = aliveMessageRepository.findById(aliveMessage.getId()).get();
        // Disconnect from session so that the updates on updatedAliveMessage are not directly saved in db
        em.detach(updatedAliveMessage);
        updatedAliveMessage.sendtime(UPDATED_SENDTIME).receivetime(UPDATED_RECEIVETIME).retrycount(UPDATED_RETRYCOUNT);

        restAliveMessageMockMvc
            .perform(
                put("/api/alive-messages")
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(updatedAliveMessage))
            )
            .andExpect(status().isOk());

        // Validate the AliveMessage in the database
        List<AliveMessage> aliveMessageList = aliveMessageRepository.findAll();
        assertThat(aliveMessageList).hasSize(databaseSizeBeforeUpdate);
        AliveMessage testAliveMessage = aliveMessageList.get(aliveMessageList.size() - 1);
        assertThat(testAliveMessage.getSendtime()).isEqualTo(UPDATED_SENDTIME);
        assertThat(testAliveMessage.getReceivetime()).isEqualTo(UPDATED_RECEIVETIME);
        assertThat(testAliveMessage.getRetrycount()).isEqualTo(UPDATED_RETRYCOUNT);
    }

    @Test
    @Transactional
    public void updateNonExistingAliveMessage() throws Exception {
        int databaseSizeBeforeUpdate = aliveMessageRepository.findAll().size();

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restAliveMessageMockMvc
            .perform(
                put("/api/alive-messages").contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(aliveMessage))
            )
            .andExpect(status().isBadRequest());

        // Validate the AliveMessage in the database
        List<AliveMessage> aliveMessageList = aliveMessageRepository.findAll();
        assertThat(aliveMessageList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    public void deleteAliveMessage() throws Exception {
        // Initialize the database
        aliveMessageService.save(aliveMessage);

        int databaseSizeBeforeDelete = aliveMessageRepository.findAll().size();

        // Delete the aliveMessage
        restAliveMessageMockMvc
            .perform(delete("/api/alive-messages/{id}", aliveMessage.getId()).accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        List<AliveMessage> aliveMessageList = aliveMessageRepository.findAll();
        assertThat(aliveMessageList).hasSize(databaseSizeBeforeDelete - 1);
    }
}
