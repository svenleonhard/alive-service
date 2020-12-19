package de.svenleonhard.alive.web.rest;

import de.svenleonhard.alive.domain.Observe;
import de.svenleonhard.alive.service.ObserveQueryService;
import de.svenleonhard.alive.service.ObserveService;
import de.svenleonhard.alive.service.dto.ObserveCriteria;
import de.svenleonhard.alive.web.rest.errors.BadRequestAlertException;
import io.github.jhipster.web.util.HeaderUtil;
import io.github.jhipster.web.util.PaginationUtil;
import io.github.jhipster.web.util.ResponseUtil;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.List;
import java.util.Optional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

/**
 * REST controller for managing {@link de.svenleonhard.alive.domain.Observe}.
 */
@RestController
@RequestMapping("/api")
public class ObserveResource {
    private final Logger log = LoggerFactory.getLogger(ObserveResource.class);

    private static final String ENTITY_NAME = "observe";

    @Value("${jhipster.clientApp.name}")
    private String applicationName;

    private final ObserveService observeService;

    private final ObserveQueryService observeQueryService;

    public ObserveResource(ObserveService observeService, ObserveQueryService observeQueryService) {
        this.observeService = observeService;
        this.observeQueryService = observeQueryService;
    }

    /**
     * {@code POST  /observes} : Create a new observe.
     *
     * @param observe the observe to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new observe, or with status {@code 400 (Bad Request)} if the observe has already an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping("/observes")
    public ResponseEntity<Observe> createObserve(@RequestBody Observe observe) throws URISyntaxException {
        log.debug("REST request to save Observe : {}", observe);
        if (observe.getId() != null) {
            throw new BadRequestAlertException("A new observe cannot already have an ID", ENTITY_NAME, "idexists");
        }
        Observe result = observeService.save(observe);
        return ResponseEntity
            .created(new URI("/api/observes/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(applicationName, true, ENTITY_NAME, result.getId().toString()))
            .body(result);
    }

    /**
     * {@code PUT  /observes} : Updates an existing observe.
     *
     * @param observe the observe to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated observe,
     * or with status {@code 400 (Bad Request)} if the observe is not valid,
     * or with status {@code 500 (Internal Server Error)} if the observe couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PutMapping("/observes")
    public ResponseEntity<Observe> updateObserve(@RequestBody Observe observe) throws URISyntaxException {
        log.debug("REST request to update Observe : {}", observe);
        if (observe.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        Observe result = observeService.save(observe);
        return ResponseEntity
            .ok()
            .headers(HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, observe.getId().toString()))
            .body(result);
    }

    /**
     * {@code GET  /observes} : get all the observes.
     *
     * @param pageable the pagination information.
     * @param criteria the criteria which the requested entities should match.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of observes in body.
     */
    @GetMapping("/observes")
    public ResponseEntity<List<Observe>> getAllObserves(ObserveCriteria criteria, Pageable pageable) {
        log.debug("REST request to get Observes by criteria: {}", criteria);
        Page<Observe> page = observeQueryService.findByCriteria(criteria, pageable);
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(ServletUriComponentsBuilder.fromCurrentRequest(), page);
        return ResponseEntity.ok().headers(headers).body(page.getContent());
    }

    /**
     * {@code GET  /observes/count} : count all the observes.
     *
     * @param criteria the criteria which the requested entities should match.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the count in body.
     */
    @GetMapping("/observes/count")
    public ResponseEntity<Long> countObserves(ObserveCriteria criteria) {
        log.debug("REST request to count Observes by criteria: {}", criteria);
        return ResponseEntity.ok().body(observeQueryService.countByCriteria(criteria));
    }

    /**
     * {@code GET  /observes/:id} : get the "id" observe.
     *
     * @param id the id of the observe to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the observe, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/observes/{id}")
    public ResponseEntity<Observe> getObserve(@PathVariable Long id) {
        log.debug("REST request to get Observe : {}", id);
        Optional<Observe> observe = observeService.findOne(id);
        return ResponseUtil.wrapOrNotFound(observe);
    }

    /**
     * {@code DELETE  /observes/:id} : delete the "id" observe.
     *
     * @param id the id of the observe to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @DeleteMapping("/observes/{id}")
    public ResponseEntity<Void> deleteObserve(@PathVariable Long id) {
        log.debug("REST request to delete Observe : {}", id);
        observeService.delete(id);
        return ResponseEntity
            .noContent()
            .headers(HeaderUtil.createEntityDeletionAlert(applicationName, true, ENTITY_NAME, id.toString()))
            .build();
    }
}
