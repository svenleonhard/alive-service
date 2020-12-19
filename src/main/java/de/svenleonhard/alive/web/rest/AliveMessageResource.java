package de.svenleonhard.alive.web.rest;

import de.svenleonhard.alive.domain.AliveMessage;
import de.svenleonhard.alive.service.AliveMessageQueryService;
import de.svenleonhard.alive.service.AliveMessageService;
import de.svenleonhard.alive.service.dto.AliveMessageCriteria;
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
 * REST controller for managing {@link de.svenleonhard.alive.domain.AliveMessage}.
 */
@RestController
@RequestMapping("/api")
public class AliveMessageResource {
    private final Logger log = LoggerFactory.getLogger(AliveMessageResource.class);

    private static final String ENTITY_NAME = "aliveMessage";

    @Value("${jhipster.clientApp.name}")
    private String applicationName;

    private final AliveMessageService aliveMessageService;

    private final AliveMessageQueryService aliveMessageQueryService;

    public AliveMessageResource(AliveMessageService aliveMessageService, AliveMessageQueryService aliveMessageQueryService) {
        this.aliveMessageService = aliveMessageService;
        this.aliveMessageQueryService = aliveMessageQueryService;
    }

    /**
     * {@code POST  /alive-messages} : Create a new aliveMessage.
     *
     * @param aliveMessage the aliveMessage to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new aliveMessage, or with status {@code 400 (Bad Request)} if the aliveMessage has already an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping("/alive-messages")
    public ResponseEntity<AliveMessage> createAliveMessage(@RequestBody AliveMessage aliveMessage) throws URISyntaxException {
        log.debug("REST request to save AliveMessage : {}", aliveMessage);
        if (aliveMessage.getId() != null) {
            throw new BadRequestAlertException("A new aliveMessage cannot already have an ID", ENTITY_NAME, "idexists");
        }
        AliveMessage result = aliveMessageService.save(aliveMessage);
        return ResponseEntity
            .created(new URI("/api/alive-messages/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(applicationName, true, ENTITY_NAME, result.getId().toString()))
            .body(result);
    }

    /**
     * {@code PUT  /alive-messages} : Updates an existing aliveMessage.
     *
     * @param aliveMessage the aliveMessage to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated aliveMessage,
     * or with status {@code 400 (Bad Request)} if the aliveMessage is not valid,
     * or with status {@code 500 (Internal Server Error)} if the aliveMessage couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PutMapping("/alive-messages")
    public ResponseEntity<AliveMessage> updateAliveMessage(@RequestBody AliveMessage aliveMessage) throws URISyntaxException {
        log.debug("REST request to update AliveMessage : {}", aliveMessage);
        if (aliveMessage.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        AliveMessage result = aliveMessageService.save(aliveMessage);
        return ResponseEntity
            .ok()
            .headers(HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, aliveMessage.getId().toString()))
            .body(result);
    }

    /**
     * {@code GET  /alive-messages} : get all the aliveMessages.
     *
     * @param pageable the pagination information.
     * @param criteria the criteria which the requested entities should match.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of aliveMessages in body.
     */
    @GetMapping("/alive-messages")
    public ResponseEntity<List<AliveMessage>> getAllAliveMessages(AliveMessageCriteria criteria, Pageable pageable) {
        log.debug("REST request to get AliveMessages by criteria: {}", criteria);
        Page<AliveMessage> page = aliveMessageQueryService.findByCriteria(criteria, pageable);
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(ServletUriComponentsBuilder.fromCurrentRequest(), page);
        return ResponseEntity.ok().headers(headers).body(page.getContent());
    }

    /**
     * {@code GET  /alive-messages/count} : count all the aliveMessages.
     *
     * @param criteria the criteria which the requested entities should match.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the count in body.
     */
    @GetMapping("/alive-messages/count")
    public ResponseEntity<Long> countAliveMessages(AliveMessageCriteria criteria) {
        log.debug("REST request to count AliveMessages by criteria: {}", criteria);
        return ResponseEntity.ok().body(aliveMessageQueryService.countByCriteria(criteria));
    }

    /**
     * {@code GET  /alive-messages/:id} : get the "id" aliveMessage.
     *
     * @param id the id of the aliveMessage to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the aliveMessage, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/alive-messages/{id}")
    public ResponseEntity<AliveMessage> getAliveMessage(@PathVariable Long id) {
        log.debug("REST request to get AliveMessage : {}", id);
        Optional<AliveMessage> aliveMessage = aliveMessageService.findOne(id);
        return ResponseUtil.wrapOrNotFound(aliveMessage);
    }

    /**
     * {@code DELETE  /alive-messages/:id} : delete the "id" aliveMessage.
     *
     * @param id the id of the aliveMessage to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @DeleteMapping("/alive-messages/{id}")
    public ResponseEntity<Void> deleteAliveMessage(@PathVariable Long id) {
        log.debug("REST request to delete AliveMessage : {}", id);
        aliveMessageService.delete(id);
        return ResponseEntity
            .noContent()
            .headers(HeaderUtil.createEntityDeletionAlert(applicationName, true, ENTITY_NAME, id.toString()))
            .build();
    }
}
