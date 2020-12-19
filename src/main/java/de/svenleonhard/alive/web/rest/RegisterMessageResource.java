package de.svenleonhard.alive.web.rest;

import de.svenleonhard.alive.domain.RegisterMessage;
import de.svenleonhard.alive.service.RegisterMessageQueryService;
import de.svenleonhard.alive.service.RegisterMessageService;
import de.svenleonhard.alive.service.dto.RegisterMessageCriteria;
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
 * REST controller for managing {@link de.svenleonhard.alive.domain.RegisterMessage}.
 */
@RestController
@RequestMapping("/api")
public class RegisterMessageResource {
    private final Logger log = LoggerFactory.getLogger(RegisterMessageResource.class);

    private static final String ENTITY_NAME = "registerMessage";

    @Value("${jhipster.clientApp.name}")
    private String applicationName;

    private final RegisterMessageService registerMessageService;

    private final RegisterMessageQueryService registerMessageQueryService;

    public RegisterMessageResource(RegisterMessageService registerMessageService, RegisterMessageQueryService registerMessageQueryService) {
        this.registerMessageService = registerMessageService;
        this.registerMessageQueryService = registerMessageQueryService;
    }

    /**
     * {@code POST  /register-messages} : Create a new registerMessage.
     *
     * @param registerMessage the registerMessage to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new registerMessage, or with status {@code 400 (Bad Request)} if the registerMessage has already an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping("/register-messages")
    public ResponseEntity<RegisterMessage> createRegisterMessage(@RequestBody RegisterMessage registerMessage) throws URISyntaxException {
        log.debug("REST request to save RegisterMessage : {}", registerMessage);
        if (registerMessage.getId() != null) {
            throw new BadRequestAlertException("A new registerMessage cannot already have an ID", ENTITY_NAME, "idexists");
        }
        RegisterMessage result = registerMessageService.save(registerMessage);
        return ResponseEntity
            .created(new URI("/api/register-messages/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(applicationName, true, ENTITY_NAME, result.getId().toString()))
            .body(result);
    }

    /**
     * {@code PUT  /register-messages} : Updates an existing registerMessage.
     *
     * @param registerMessage the registerMessage to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated registerMessage,
     * or with status {@code 400 (Bad Request)} if the registerMessage is not valid,
     * or with status {@code 500 (Internal Server Error)} if the registerMessage couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PutMapping("/register-messages")
    public ResponseEntity<RegisterMessage> updateRegisterMessage(@RequestBody RegisterMessage registerMessage) throws URISyntaxException {
        log.debug("REST request to update RegisterMessage : {}", registerMessage);
        if (registerMessage.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        RegisterMessage result = registerMessageService.save(registerMessage);
        return ResponseEntity
            .ok()
            .headers(HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, registerMessage.getId().toString()))
            .body(result);
    }

    /**
     * {@code GET  /register-messages} : get all the registerMessages.
     *
     * @param pageable the pagination information.
     * @param criteria the criteria which the requested entities should match.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of registerMessages in body.
     */
    @GetMapping("/register-messages")
    public ResponseEntity<List<RegisterMessage>> getAllRegisterMessages(RegisterMessageCriteria criteria, Pageable pageable) {
        log.debug("REST request to get RegisterMessages by criteria: {}", criteria);
        Page<RegisterMessage> page = registerMessageQueryService.findByCriteria(criteria, pageable);
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(ServletUriComponentsBuilder.fromCurrentRequest(), page);
        return ResponseEntity.ok().headers(headers).body(page.getContent());
    }

    /**
     * {@code GET  /register-messages/count} : count all the registerMessages.
     *
     * @param criteria the criteria which the requested entities should match.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the count in body.
     */
    @GetMapping("/register-messages/count")
    public ResponseEntity<Long> countRegisterMessages(RegisterMessageCriteria criteria) {
        log.debug("REST request to count RegisterMessages by criteria: {}", criteria);
        return ResponseEntity.ok().body(registerMessageQueryService.countByCriteria(criteria));
    }

    /**
     * {@code GET  /register-messages/:id} : get the "id" registerMessage.
     *
     * @param id the id of the registerMessage to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the registerMessage, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/register-messages/{id}")
    public ResponseEntity<RegisterMessage> getRegisterMessage(@PathVariable Long id) {
        log.debug("REST request to get RegisterMessage : {}", id);
        Optional<RegisterMessage> registerMessage = registerMessageService.findOne(id);
        return ResponseUtil.wrapOrNotFound(registerMessage);
    }

    /**
     * {@code DELETE  /register-messages/:id} : delete the "id" registerMessage.
     *
     * @param id the id of the registerMessage to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @DeleteMapping("/register-messages/{id}")
    public ResponseEntity<Void> deleteRegisterMessage(@PathVariable Long id) {
        log.debug("REST request to delete RegisterMessage : {}", id);
        registerMessageService.delete(id);
        return ResponseEntity
            .noContent()
            .headers(HeaderUtil.createEntityDeletionAlert(applicationName, true, ENTITY_NAME, id.toString()))
            .build();
    }
}
