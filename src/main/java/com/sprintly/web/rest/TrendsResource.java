package com.sprintly.web.rest;

import com.sprintly.domain.Trends;
import com.sprintly.repository.TrendsRepository;
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
 * REST controller for managing {@link com.sprintly.domain.Trends}.
 */
@RestController
@RequestMapping("/api")
@Transactional
public class TrendsResource {

    private final Logger log = LoggerFactory.getLogger(TrendsResource.class);

    private static final String ENTITY_NAME = "trends";

    @Value("${jhipster.clientApp.name}")
    private String applicationName;

    private final TrendsRepository trendsRepository;

    public TrendsResource(TrendsRepository trendsRepository) {
        this.trendsRepository = trendsRepository;
    }

    /**
     * {@code POST  /trends} : Create a new trends.
     *
     * @param trends the trends to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new trends, or with status {@code 400 (Bad Request)} if the trends has already an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping("/trends")
    public ResponseEntity<Trends> createTrends(@RequestBody Trends trends) throws URISyntaxException {
        log.debug("REST request to save Trends : {}", trends);
        if (trends.getId() != null) {
            throw new BadRequestAlertException("A new trends cannot already have an ID", ENTITY_NAME, "idexists");
        }
        Trends result = trendsRepository.save(trends);
        return ResponseEntity
            .created(new URI("/api/trends/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(applicationName, false, ENTITY_NAME, result.getId().toString()))
            .body(result);
    }

    /**
     * {@code PUT  /trends/:id} : Updates an existing trends.
     *
     * @param id the id of the trends to save.
     * @param trends the trends to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated trends,
     * or with status {@code 400 (Bad Request)} if the trends is not valid,
     * or with status {@code 500 (Internal Server Error)} if the trends couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PutMapping("/trends/{id}")
    public ResponseEntity<Trends> updateTrends(@PathVariable(value = "id", required = false) final Long id, @RequestBody Trends trends)
        throws URISyntaxException {
        log.debug("REST request to update Trends : {}, {}", id, trends);
        if (trends.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, trends.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!trendsRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        Trends result = trendsRepository.save(trends);
        return ResponseEntity
            .ok()
            .headers(HeaderUtil.createEntityUpdateAlert(applicationName, false, ENTITY_NAME, trends.getId().toString()))
            .body(result);
    }

    /**
     * {@code PATCH  /trends/:id} : Partial updates given fields of an existing trends, field will ignore if it is null
     *
     * @param id the id of the trends to save.
     * @param trends the trends to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated trends,
     * or with status {@code 400 (Bad Request)} if the trends is not valid,
     * or with status {@code 404 (Not Found)} if the trends is not found,
     * or with status {@code 500 (Internal Server Error)} if the trends couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PatchMapping(value = "/trends/{id}", consumes = { "application/json", "application/merge-patch+json" })
    public ResponseEntity<Trends> partialUpdateTrends(
        @PathVariable(value = "id", required = false) final Long id,
        @RequestBody Trends trends
    ) throws URISyntaxException {
        log.debug("REST request to partial update Trends partially : {}, {}", id, trends);
        if (trends.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, trends.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!trendsRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        Optional<Trends> result = trendsRepository
            .findById(trends.getId())
            .map(existingTrends -> {
                if (trends.getAvgPace() != null) {
                    existingTrends.setAvgPace(trends.getAvgPace());
                }
                if (trends.getDistancePerRun() != null) {
                    existingTrends.setDistancePerRun(trends.getDistancePerRun());
                }

                return existingTrends;
            })
            .map(trendsRepository::save);

        return ResponseUtil.wrapOrNotFound(
            result,
            HeaderUtil.createEntityUpdateAlert(applicationName, false, ENTITY_NAME, trends.getId().toString())
        );
    }

    /**
     * {@code GET  /trends} : get all the trends.
     *
     * @param eagerload flag to eager load entities from relationships (This is applicable for many-to-many).
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of trends in body.
     */
    @GetMapping("/trends")
    public List<Trends> getAllTrends(@RequestParam(required = false, defaultValue = "false") boolean eagerload) {
        log.debug("REST request to get all Trends");
        return trendsRepository.findAllWithEagerRelationships();
    }

    /**
     * {@code GET  /trends/:id} : get the "id" trends.
     *
     * @param id the id of the trends to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the trends, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/trends/{id}")
    public ResponseEntity<Trends> getTrends(@PathVariable Long id) {
        log.debug("REST request to get Trends : {}", id);
        Optional<Trends> trends = trendsRepository.findOneWithEagerRelationships(id);
        return ResponseUtil.wrapOrNotFound(trends);
    }

    /**
     * {@code DELETE  /trends/:id} : delete the "id" trends.
     *
     * @param id the id of the trends to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @DeleteMapping("/trends/{id}")
    public ResponseEntity<Void> deleteTrends(@PathVariable Long id) {
        log.debug("REST request to delete Trends : {}", id);
        trendsRepository.deleteById(id);
        return ResponseEntity
            .noContent()
            .headers(HeaderUtil.createEntityDeletionAlert(applicationName, false, ENTITY_NAME, id.toString()))
            .build();
    }
}
