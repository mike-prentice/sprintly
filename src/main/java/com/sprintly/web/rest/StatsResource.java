package com.sprintly.web.rest;

import com.sprintly.domain.Stats;
import com.sprintly.repository.StatsRepository;
import com.sprintly.web.rest.errors.BadRequestAlertException;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseEntity;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;
import tech.jhipster.web.util.HeaderUtil;
import tech.jhipster.web.util.ResponseUtil;

/**
 * REST controller for managing {@link com.sprintly.domain.Stats}.
 */
@RestController
@RequestMapping("/api")
@Transactional
public class StatsResource {

    private final Logger log = LoggerFactory.getLogger(StatsResource.class);

    private static final String ENTITY_NAME = "stats";

    @Value("${jhipster.clientApp.name}")
    private String applicationName;

    private final StatsRepository statsRepository;

    public StatsResource(StatsRepository statsRepository) {
        this.statsRepository = statsRepository;
    }

    /**
     * {@code POST  /stats} : Create a new stats.
     *
     * @param stats the stats to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new stats, or with status {@code 400 (Bad Request)} if the stats has already an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping("/stats")
    public ResponseEntity<Stats> createStats(@RequestBody Stats stats) throws URISyntaxException {
        log.debug("REST request to save Stats : {}", stats);
        if (stats.getId() != null) {
            throw new BadRequestAlertException("A new stats cannot already have an ID", ENTITY_NAME, "idexists");
        }
        Stats result = statsRepository.save(stats);
        return ResponseEntity
            .created(new URI("/api/stats/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(applicationName, false, ENTITY_NAME, result.getId().toString()))
            .body(result);
    }

    /**
     * {@code PUT  /stats/:id} : Updates an existing stats.
     *
     * @param id the id of the stats to save.
     * @param stats the stats to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated stats,
     * or with status {@code 400 (Bad Request)} if the stats is not valid,
     * or with status {@code 500 (Internal Server Error)} if the stats couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PutMapping("/stats/{id}")
    public ResponseEntity<Stats> updateStats(@PathVariable(value = "id", required = false) final Long id, @RequestBody Stats stats)
        throws URISyntaxException {
        log.debug("REST request to update Stats : {}, {}", id, stats);
        if (stats.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, stats.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!statsRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        Stats result = statsRepository.save(stats);
        return ResponseEntity
            .ok()
            .headers(HeaderUtil.createEntityUpdateAlert(applicationName, false, ENTITY_NAME, stats.getId().toString()))
            .body(result);
    }

    /**
     * {@code PATCH  /stats/:id} : Partial updates given fields of an existing stats, field will ignore if it is null
     *
     * @param id the id of the stats to save.
     * @param stats the stats to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated stats,
     * or with status {@code 400 (Bad Request)} if the stats is not valid,
     * or with status {@code 404 (Not Found)} if the stats is not found,
     * or with status {@code 500 (Internal Server Error)} if the stats couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PatchMapping(value = "/stats/{id}", consumes = { "application/json", "application/merge-patch+json" })
    public ResponseEntity<Stats> partialUpdateStats(@PathVariable(value = "id", required = false) final Long id, @RequestBody Stats stats)
        throws URISyntaxException {
        log.debug("REST request to partial update Stats partially : {}, {}", id, stats);
        if (stats.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, stats.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!statsRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        Optional<Stats> result = statsRepository
            .findById(stats.getId())
            .map(existingStats -> {
                if (stats.getDistance() != null) {
                    existingStats.setDistance(stats.getDistance());
                }
                if (stats.getTime() != null) {
                    existingStats.setTime(stats.getTime());
                }
                if (stats.getAvgpace() != null) {
                    existingStats.setAvgpace(stats.getAvgpace());
                }

                return existingStats;
            })
            .map(statsRepository::save);

        return ResponseUtil.wrapOrNotFound(
            result,
            HeaderUtil.createEntityUpdateAlert(applicationName, false, ENTITY_NAME, stats.getId().toString())
        );
    }

    /**
     * {@code GET  /stats} : get all the stats.
     *
     * @param eagerload flag to eager load entities from relationships (This is applicable for many-to-many).
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of stats in body.
     */
    @GetMapping("/stats")
    public List<Stats> getAllStats(@RequestParam(required = false, defaultValue = "false") boolean eagerload) {
        log.debug("REST request to get all Stats");
        return statsRepository.findByUserIsCurrentUser();
    }

    /**
     * {@code GET  /stats/:id} : get the "id" stats.
     *
     * @param id the id of the stats to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the stats, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/stats/{id}")
    public ResponseEntity<Stats> getStats(@PathVariable Long id) {
        log.debug("REST request to get Stats : {}", id);
        Optional<Stats> stats = statsRepository.findOneWithEagerRelationships(id);
        return ResponseUtil.wrapOrNotFound(stats);
    }

    /**
     * {@code DELETE  /stats/:id} : delete the "id" stats.
     *
     * @param id the id of the stats to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @DeleteMapping("/stats/{id}")
    public ResponseEntity<Void> deleteStats(@PathVariable Long id) {
        log.debug("REST request to delete Stats : {}", id);
        statsRepository.deleteById(id);
        return ResponseEntity
            .noContent()
            .headers(HeaderUtil.createEntityDeletionAlert(applicationName, false, ENTITY_NAME, id.toString()))
            .build();
    }
}
