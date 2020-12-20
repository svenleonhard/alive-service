package de.svenleonhard.alive.web.rest;

import de.svenleonhard.alive.domain.DeviceNotAlive;
import de.svenleonhard.alive.service.DeviceNotAliveQueryService;
import de.svenleonhard.alive.service.DeviceNotAliveService;
import de.svenleonhard.alive.service.dto.DeviceNotAliveCriteria;
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
 * REST controller for managing {@link de.svenleonhard.alive.domain.DeviceNotAlive}.
 */
@RestController
@RequestMapping("/api")
public class DeviceNotAliveResource {
    private final Logger log = LoggerFactory.getLogger(DeviceNotAliveResource.class);

    private static final String ENTITY_NAME = "deviceNotAlive";

    @Value("${jhipster.clientApp.name}")
    private String applicationName;

    private final DeviceNotAliveService deviceNotAliveService;

    private final DeviceNotAliveQueryService deviceNotAliveQueryService;

    public DeviceNotAliveResource(DeviceNotAliveService deviceNotAliveService, DeviceNotAliveQueryService deviceNotAliveQueryService) {
        this.deviceNotAliveService = deviceNotAliveService;
        this.deviceNotAliveQueryService = deviceNotAliveQueryService;
    }

    /**
     * {@code POST  /device-not-alives} : Create a new deviceNotAlive.
     *
     * @param deviceNotAlive the deviceNotAlive to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new deviceNotAlive, or with status {@code 400 (Bad Request)} if the deviceNotAlive has already an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping("/device-not-alives")
    public ResponseEntity<DeviceNotAlive> createDeviceNotAlive(@RequestBody DeviceNotAlive deviceNotAlive) throws URISyntaxException {
        log.debug("REST request to save DeviceNotAlive : {}", deviceNotAlive);
        if (deviceNotAlive.getId() != null) {
            throw new BadRequestAlertException("A new deviceNotAlive cannot already have an ID", ENTITY_NAME, "idexists");
        }
        DeviceNotAlive result = deviceNotAliveService.save(deviceNotAlive);
        return ResponseEntity
            .created(new URI("/api/device-not-alives/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(applicationName, true, ENTITY_NAME, result.getId().toString()))
            .body(result);
    }

    /**
     * {@code PUT  /device-not-alives} : Updates an existing deviceNotAlive.
     *
     * @param deviceNotAlive the deviceNotAlive to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated deviceNotAlive,
     * or with status {@code 400 (Bad Request)} if the deviceNotAlive is not valid,
     * or with status {@code 500 (Internal Server Error)} if the deviceNotAlive couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PutMapping("/device-not-alives")
    public ResponseEntity<DeviceNotAlive> updateDeviceNotAlive(@RequestBody DeviceNotAlive deviceNotAlive) throws URISyntaxException {
        log.debug("REST request to update DeviceNotAlive : {}", deviceNotAlive);
        if (deviceNotAlive.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        DeviceNotAlive result = deviceNotAliveService.save(deviceNotAlive);
        return ResponseEntity
            .ok()
            .headers(HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, deviceNotAlive.getId().toString()))
            .body(result);
    }

    /**
     * {@code GET  /device-not-alives} : get all the deviceNotAlives.
     *
     * @param pageable the pagination information.
     * @param criteria the criteria which the requested entities should match.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of deviceNotAlives in body.
     */
    @GetMapping("/device-not-alives")
    public ResponseEntity<List<DeviceNotAlive>> getAllDeviceNotAlives(DeviceNotAliveCriteria criteria, Pageable pageable) {
        log.debug("REST request to get DeviceNotAlives by criteria: {}", criteria);
        Page<DeviceNotAlive> page = deviceNotAliveQueryService.findByCriteria(criteria, pageable);
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(ServletUriComponentsBuilder.fromCurrentRequest(), page);
        return ResponseEntity.ok().headers(headers).body(page.getContent());
    }

    /**
     * {@code GET  /device-not-alives/count} : count all the deviceNotAlives.
     *
     * @param criteria the criteria which the requested entities should match.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the count in body.
     */
    @GetMapping("/device-not-alives/count")
    public ResponseEntity<Long> countDeviceNotAlives(DeviceNotAliveCriteria criteria) {
        log.debug("REST request to count DeviceNotAlives by criteria: {}", criteria);
        return ResponseEntity.ok().body(deviceNotAliveQueryService.countByCriteria(criteria));
    }

    /**
     * {@code GET  /device-not-alives/:id} : get the "id" deviceNotAlive.
     *
     * @param id the id of the deviceNotAlive to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the deviceNotAlive, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/device-not-alives/{id}")
    public ResponseEntity<DeviceNotAlive> getDeviceNotAlive(@PathVariable Long id) {
        log.debug("REST request to get DeviceNotAlive : {}", id);
        Optional<DeviceNotAlive> deviceNotAlive = deviceNotAliveService.findOne(id);
        return ResponseUtil.wrapOrNotFound(deviceNotAlive);
    }

    /**
     * {@code DELETE  /device-not-alives/:id} : delete the "id" deviceNotAlive.
     *
     * @param id the id of the deviceNotAlive to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @DeleteMapping("/device-not-alives/{id}")
    public ResponseEntity<Void> deleteDeviceNotAlive(@PathVariable Long id) {
        log.debug("REST request to delete DeviceNotAlive : {}", id);
        deviceNotAliveService.delete(id);
        return ResponseEntity
            .noContent()
            .headers(HeaderUtil.createEntityDeletionAlert(applicationName, true, ENTITY_NAME, id.toString()))
            .build();
    }
}
