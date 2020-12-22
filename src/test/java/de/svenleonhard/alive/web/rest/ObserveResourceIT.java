package de.svenleonhard.alive.web.rest;

import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import de.svenleonhard.alive.AliveServiceApp;
import de.svenleonhard.alive.domain.Observe;
import de.svenleonhard.alive.domain.User;
import de.svenleonhard.alive.repository.ObserveRepository;
import de.svenleonhard.alive.service.ObserveQueryService;
import de.svenleonhard.alive.service.ObserveService;
import de.svenleonhard.alive.service.dto.ObserveCriteria;
import java.time.LocalDate;
import java.time.ZoneId;
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
 * Integration tests for the {@link ObserveResource} REST controller.
 */
@SpringBootTest(classes = AliveServiceApp.class)
@AutoConfigureMockMvc
@WithMockUser
public class ObserveResourceIT {
    private static final String DEFAULT_DESCRIPTION = "AAAAAAAAAA";
    private static final String UPDATED_DESCRIPTION = "BBBBBBBBBB";

    private static final LocalDate DEFAULT_STARTDATE = LocalDate.ofEpochDay(0L);
    private static final LocalDate UPDATED_STARTDATE = LocalDate.now(ZoneId.systemDefault());
    private static final LocalDate SMALLER_STARTDATE = LocalDate.ofEpochDay(-1L);

    @Autowired
    private ObserveRepository observeRepository;

    @Autowired
    private ObserveService observeService;

    @Autowired
    private ObserveQueryService observeQueryService;

    @Autowired
    private EntityManager em;

    @Autowired
    private MockMvc restObserveMockMvc;

    private Observe observe;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Observe createEntity(EntityManager em) {
        Observe observe = new Observe().description(DEFAULT_DESCRIPTION).startdate(DEFAULT_STARTDATE).user(UserResourceIT.create());
        return observe;
    }

    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Observe createUpdatedEntity(EntityManager em) {
        Observe observe = new Observe().description(UPDATED_DESCRIPTION).startdate(UPDATED_STARTDATE);
        return observe;
    }

    @BeforeEach
    public void initTest() {
        observe = createEntity(em);
    }

    @Test
    @Transactional
    public void createObserve() throws Exception {
        int databaseSizeBeforeCreate = observeRepository.findAll().size();
        // Create the Observe
        restObserveMockMvc
            .perform(post("/api/observes").contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(observe)))
            .andExpect(status().isCreated());

        // Validate the Observe in the database
        List<Observe> observeList = observeRepository.findAll();
        assertThat(observeList).hasSize(databaseSizeBeforeCreate + 1);
        Observe testObserve = observeList.get(observeList.size() - 1);
        assertThat(testObserve.getDescription()).isEqualTo(DEFAULT_DESCRIPTION);
        assertThat(testObserve.getStartdate()).isEqualTo(DEFAULT_STARTDATE);
    }

    @Test
    @Transactional
    public void createObserveWithExistingId() throws Exception {
        int databaseSizeBeforeCreate = observeRepository.findAll().size();

        // Create the Observe with an existing ID
        observe.setId(1L);

        // An entity with an existing ID cannot be created, so this API call must fail
        restObserveMockMvc
            .perform(post("/api/observes").contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(observe)))
            .andExpect(status().isBadRequest());

        // Validate the Observe in the database
        List<Observe> observeList = observeRepository.findAll();
        assertThat(observeList).hasSize(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    public void getAllObserves() throws Exception {
        // Initialize the database
        observeRepository.saveAndFlush(observe);

        // Get all the observeList
        restObserveMockMvc
            .perform(get("/api/observes?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(observe.getId().intValue())))
            .andExpect(jsonPath("$.[*].description").value(hasItem(DEFAULT_DESCRIPTION)))
            .andExpect(jsonPath("$.[*].startdate").value(hasItem(DEFAULT_STARTDATE.toString())));
    }

    @Test
    @Transactional
    public void getObserve() throws Exception {
        // Initialize the database
        observeRepository.saveAndFlush(observe);

        // Get the observe
        restObserveMockMvc
            .perform(get("/api/observes/{id}", observe.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.id").value(observe.getId().intValue()))
            .andExpect(jsonPath("$.description").value(DEFAULT_DESCRIPTION))
            .andExpect(jsonPath("$.startdate").value(DEFAULT_STARTDATE.toString()));
    }

    @Test
    @Transactional
    public void getObservesByIdFiltering() throws Exception {
        // Initialize the database
        observeRepository.saveAndFlush(observe);

        Long id = observe.getId();

        defaultObserveShouldBeFound("id.equals=" + id);
        defaultObserveShouldNotBeFound("id.notEquals=" + id);

        defaultObserveShouldBeFound("id.greaterThanOrEqual=" + id);
        defaultObserveShouldNotBeFound("id.greaterThan=" + id);

        defaultObserveShouldBeFound("id.lessThanOrEqual=" + id);
        defaultObserveShouldNotBeFound("id.lessThan=" + id);
    }

    @Test
    @Transactional
    public void getAllObservesByDescriptionIsEqualToSomething() throws Exception {
        // Initialize the database
        observeRepository.saveAndFlush(observe);

        // Get all the observeList where description equals to DEFAULT_DESCRIPTION
        defaultObserveShouldBeFound("description.equals=" + DEFAULT_DESCRIPTION);

        // Get all the observeList where description equals to UPDATED_DESCRIPTION
        defaultObserveShouldNotBeFound("description.equals=" + UPDATED_DESCRIPTION);
    }

    @Test
    @Transactional
    public void getAllObservesByDescriptionIsNotEqualToSomething() throws Exception {
        // Initialize the database
        observeRepository.saveAndFlush(observe);

        // Get all the observeList where description not equals to DEFAULT_DESCRIPTION
        defaultObserveShouldNotBeFound("description.notEquals=" + DEFAULT_DESCRIPTION);

        // Get all the observeList where description not equals to UPDATED_DESCRIPTION
        defaultObserveShouldBeFound("description.notEquals=" + UPDATED_DESCRIPTION);
    }

    @Test
    @Transactional
    public void getAllObservesByDescriptionIsInShouldWork() throws Exception {
        // Initialize the database
        observeRepository.saveAndFlush(observe);

        // Get all the observeList where description in DEFAULT_DESCRIPTION or UPDATED_DESCRIPTION
        defaultObserveShouldBeFound("description.in=" + DEFAULT_DESCRIPTION + "," + UPDATED_DESCRIPTION);

        // Get all the observeList where description equals to UPDATED_DESCRIPTION
        defaultObserveShouldNotBeFound("description.in=" + UPDATED_DESCRIPTION);
    }

    @Test
    @Transactional
    public void getAllObservesByDescriptionIsNullOrNotNull() throws Exception {
        // Initialize the database
        observeRepository.saveAndFlush(observe);

        // Get all the observeList where description is not null
        defaultObserveShouldBeFound("description.specified=true");

        // Get all the observeList where description is null
        defaultObserveShouldNotBeFound("description.specified=false");
    }

    @Test
    @Transactional
    public void getAllObservesByDescriptionContainsSomething() throws Exception {
        // Initialize the database
        observeRepository.saveAndFlush(observe);

        // Get all the observeList where description contains DEFAULT_DESCRIPTION
        defaultObserveShouldBeFound("description.contains=" + DEFAULT_DESCRIPTION);

        // Get all the observeList where description contains UPDATED_DESCRIPTION
        defaultObserveShouldNotBeFound("description.contains=" + UPDATED_DESCRIPTION);
    }

    @Test
    @Transactional
    public void getAllObservesByDescriptionNotContainsSomething() throws Exception {
        // Initialize the database
        observeRepository.saveAndFlush(observe);

        // Get all the observeList where description does not contain DEFAULT_DESCRIPTION
        defaultObserveShouldNotBeFound("description.doesNotContain=" + DEFAULT_DESCRIPTION);

        // Get all the observeList where description does not contain UPDATED_DESCRIPTION
        defaultObserveShouldBeFound("description.doesNotContain=" + UPDATED_DESCRIPTION);
    }

    @Test
    @Transactional
    public void getAllObservesByStartdateIsEqualToSomething() throws Exception {
        // Initialize the database
        observeRepository.saveAndFlush(observe);

        // Get all the observeList where startdate equals to DEFAULT_STARTDATE
        defaultObserveShouldBeFound("startdate.equals=" + DEFAULT_STARTDATE);

        // Get all the observeList where startdate equals to UPDATED_STARTDATE
        defaultObserveShouldNotBeFound("startdate.equals=" + UPDATED_STARTDATE);
    }

    @Test
    @Transactional
    public void getAllObservesByStartdateIsNotEqualToSomething() throws Exception {
        // Initialize the database
        observeRepository.saveAndFlush(observe);

        // Get all the observeList where startdate not equals to DEFAULT_STARTDATE
        defaultObserveShouldNotBeFound("startdate.notEquals=" + DEFAULT_STARTDATE);

        // Get all the observeList where startdate not equals to UPDATED_STARTDATE
        defaultObserveShouldBeFound("startdate.notEquals=" + UPDATED_STARTDATE);
    }

    @Test
    @Transactional
    public void getAllObservesByStartdateIsInShouldWork() throws Exception {
        // Initialize the database
        observeRepository.saveAndFlush(observe);

        // Get all the observeList where startdate in DEFAULT_STARTDATE or UPDATED_STARTDATE
        defaultObserveShouldBeFound("startdate.in=" + DEFAULT_STARTDATE + "," + UPDATED_STARTDATE);

        // Get all the observeList where startdate equals to UPDATED_STARTDATE
        defaultObserveShouldNotBeFound("startdate.in=" + UPDATED_STARTDATE);
    }

    @Test
    @Transactional
    public void getAllObservesByStartdateIsNullOrNotNull() throws Exception {
        // Initialize the database
        observeRepository.saveAndFlush(observe);

        // Get all the observeList where startdate is not null
        defaultObserveShouldBeFound("startdate.specified=true");

        // Get all the observeList where startdate is null
        defaultObserveShouldNotBeFound("startdate.specified=false");
    }

    @Test
    @Transactional
    public void getAllObservesByStartdateIsGreaterThanOrEqualToSomething() throws Exception {
        // Initialize the database
        observeRepository.saveAndFlush(observe);

        // Get all the observeList where startdate is greater than or equal to DEFAULT_STARTDATE
        defaultObserveShouldBeFound("startdate.greaterThanOrEqual=" + DEFAULT_STARTDATE);

        // Get all the observeList where startdate is greater than or equal to UPDATED_STARTDATE
        defaultObserveShouldNotBeFound("startdate.greaterThanOrEqual=" + UPDATED_STARTDATE);
    }

    @Test
    @Transactional
    public void getAllObservesByStartdateIsLessThanOrEqualToSomething() throws Exception {
        // Initialize the database
        observeRepository.saveAndFlush(observe);

        // Get all the observeList where startdate is less than or equal to DEFAULT_STARTDATE
        defaultObserveShouldBeFound("startdate.lessThanOrEqual=" + DEFAULT_STARTDATE);

        // Get all the observeList where startdate is less than or equal to SMALLER_STARTDATE
        defaultObserveShouldNotBeFound("startdate.lessThanOrEqual=" + SMALLER_STARTDATE);
    }

    @Test
    @Transactional
    public void getAllObservesByStartdateIsLessThanSomething() throws Exception {
        // Initialize the database
        observeRepository.saveAndFlush(observe);

        // Get all the observeList where startdate is less than DEFAULT_STARTDATE
        defaultObserveShouldNotBeFound("startdate.lessThan=" + DEFAULT_STARTDATE);

        // Get all the observeList where startdate is less than UPDATED_STARTDATE
        defaultObserveShouldBeFound("startdate.lessThan=" + UPDATED_STARTDATE);
    }

    @Test
    @Transactional
    public void getAllObservesByStartdateIsGreaterThanSomething() throws Exception {
        // Initialize the database
        observeRepository.saveAndFlush(observe);

        // Get all the observeList where startdate is greater than DEFAULT_STARTDATE
        defaultObserveShouldNotBeFound("startdate.greaterThan=" + DEFAULT_STARTDATE);

        // Get all the observeList where startdate is greater than SMALLER_STARTDATE
        defaultObserveShouldBeFound("startdate.greaterThan=" + SMALLER_STARTDATE);
    }

    @Test
    @Transactional
    public void getAllObservesByUserIsEqualToSomething() throws Exception {
        // Initialize the database
        observeRepository.saveAndFlush(observe);
        User user = UserResourceIT.createEntity(em);
        em.persist(user);
        em.flush();
        observe.setUser(user);
        observeRepository.saveAndFlush(observe);
        Long userId = user.getId();

        // Get all the observeList where user equals to userId
        defaultObserveShouldNotBeFound("userId.equals=" + userId);

        // Get all the observeList where user equals to userId + 1
        defaultObserveShouldNotBeFound("userId.equals=" + (userId + 1));
    }

    /**
     * Executes the search, and checks that the default entity is returned.
     */
    private void defaultObserveShouldBeFound(String filter) throws Exception {
        restObserveMockMvc
            .perform(get("/api/observes?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(observe.getId().intValue())))
            .andExpect(jsonPath("$.[*].description").value(hasItem(DEFAULT_DESCRIPTION)))
            .andExpect(jsonPath("$.[*].startdate").value(hasItem(DEFAULT_STARTDATE.toString())));

        // Check, that the count call also returns 1
        restObserveMockMvc
            .perform(get("/api/observes/count?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(content().string("1"));
    }

    /**
     * Executes the search, and checks that the default entity is not returned.
     */
    private void defaultObserveShouldNotBeFound(String filter) throws Exception {
        restObserveMockMvc
            .perform(get("/api/observes?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$").isArray())
            .andExpect(jsonPath("$").isEmpty());

        // Check, that the count call also returns 0
        restObserveMockMvc
            .perform(get("/api/observes/count?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(content().string("0"));
    }

    @Test
    @Transactional
    public void getNonExistingObserve() throws Exception {
        // Get the observe
        restObserveMockMvc.perform(get("/api/observes/{id}", Long.MAX_VALUE)).andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    public void updateObserve() throws Exception {
        // Initialize the database
        observeService.save(observe);

        int databaseSizeBeforeUpdate = observeRepository.findAll().size();

        // Update the observe
        Observe updatedObserve = observeRepository.findById(observe.getId()).get();
        // Disconnect from session so that the updates on updatedObserve are not directly saved in db
        em.detach(updatedObserve);
        updatedObserve.description(UPDATED_DESCRIPTION).startdate(UPDATED_STARTDATE);

        restObserveMockMvc
            .perform(
                put("/api/observes").contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(updatedObserve))
            )
            .andExpect(status().isOk());

        // Validate the Observe in the database
        List<Observe> observeList = observeRepository.findAll();
        assertThat(observeList).hasSize(databaseSizeBeforeUpdate);
        Observe testObserve = observeList.get(observeList.size() - 1);
        assertThat(testObserve.getDescription()).isEqualTo(UPDATED_DESCRIPTION);
        assertThat(testObserve.getStartdate()).isEqualTo(UPDATED_STARTDATE);
    }

    @Test
    @Transactional
    public void updateNonExistingObserve() throws Exception {
        int databaseSizeBeforeUpdate = observeRepository.findAll().size();

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restObserveMockMvc
            .perform(put("/api/observes").contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(observe)))
            .andExpect(status().isBadRequest());

        // Validate the Observe in the database
        List<Observe> observeList = observeRepository.findAll();
        assertThat(observeList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    public void deleteObserve() throws Exception {
        // Initialize the database
        observeService.save(observe);

        int databaseSizeBeforeDelete = observeRepository.findAll().size();

        // Delete the observe
        restObserveMockMvc
            .perform(delete("/api/observes/{id}", observe.getId()).accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        List<Observe> observeList = observeRepository.findAll();
        assertThat(observeList).hasSize(databaseSizeBeforeDelete - 1);
    }
}
