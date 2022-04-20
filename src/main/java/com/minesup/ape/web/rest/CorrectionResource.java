package com.minesup.ape.web.rest;

import com.minesup.ape.domain.Correction;
import com.minesup.ape.repository.CorrectionRepository;
import com.minesup.ape.service.CorrectionService;
import com.minesup.ape.web.rest.errors.BadRequestAlertException;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
import javax.validation.Valid;
import javax.validation.constraints.NotNull;
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
import tech.jhipster.web.util.HeaderUtil;
import tech.jhipster.web.util.PaginationUtil;
import tech.jhipster.web.util.ResponseUtil;

/**
 * REST controller for managing {@link com.minesup.ape.domain.Correction}.
 */
@RestController
@RequestMapping("/api")
public class CorrectionResource {

    private final Logger log = LoggerFactory.getLogger(CorrectionResource.class);

    private static final String ENTITY_NAME = "correction";

    @Value("${jhipster.clientApp.name}")
    private String applicationName;

    private final CorrectionService correctionService;

    private final CorrectionRepository correctionRepository;

    public CorrectionResource(CorrectionService correctionService, CorrectionRepository correctionRepository) {
        this.correctionService = correctionService;
        this.correctionRepository = correctionRepository;
    }

    /**
     * {@code POST  /corrections} : Create a new correction.
     *
     * @param correction the correction to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new correction, or with status {@code 400 (Bad Request)} if the correction has already an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping("/corrections")
    public ResponseEntity<Correction> createCorrection(@Valid @RequestBody Correction correction) throws URISyntaxException {
        log.debug("REST request to save Correction : {}", correction);
        if (correction.getId() != null) {
            throw new BadRequestAlertException("A new correction cannot already have an ID", ENTITY_NAME, "idexists");
        }
        Correction result = correctionService.save(correction);
        return ResponseEntity
            .created(new URI("/api/corrections/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(applicationName, true, ENTITY_NAME, result.getId().toString()))
            .body(result);
    }

    /**
     * {@code PUT  /corrections/:id} : Updates an existing correction.
     *
     * @param id the id of the correction to save.
     * @param correction the correction to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated correction,
     * or with status {@code 400 (Bad Request)} if the correction is not valid,
     * or with status {@code 500 (Internal Server Error)} if the correction couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PutMapping("/corrections/{id}")
    public ResponseEntity<Correction> updateCorrection(
        @PathVariable(value = "id", required = false) final Long id,
        @Valid @RequestBody Correction correction
    ) throws URISyntaxException {
        log.debug("REST request to update Correction : {}, {}", id, correction);
        if (correction.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, correction.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!correctionRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        Correction result = correctionService.update(correction);
        return ResponseEntity
            .ok()
            .headers(HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, correction.getId().toString()))
            .body(result);
    }

    /**
     * {@code PATCH  /corrections/:id} : Partial updates given fields of an existing correction, field will ignore if it is null
     *
     * @param id the id of the correction to save.
     * @param correction the correction to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated correction,
     * or with status {@code 400 (Bad Request)} if the correction is not valid,
     * or with status {@code 404 (Not Found)} if the correction is not found,
     * or with status {@code 500 (Internal Server Error)} if the correction couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PatchMapping(value = "/corrections/{id}", consumes = { "application/json", "application/merge-patch+json" })
    public ResponseEntity<Correction> partialUpdateCorrection(
        @PathVariable(value = "id", required = false) final Long id,
        @NotNull @RequestBody Correction correction
    ) throws URISyntaxException {
        log.debug("REST request to partial update Correction partially : {}, {}", id, correction);
        if (correction.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, correction.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!correctionRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        Optional<Correction> result = correctionService.partialUpdate(correction);

        return ResponseUtil.wrapOrNotFound(
            result,
            HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, correction.getId().toString())
        );
    }

    /**
     * {@code GET  /corrections} : get all the corrections.
     *
     * @param pageable the pagination information.
     * @param eagerload flag to eager load entities from relationships (This is applicable for many-to-many).
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of corrections in body.
     */
    @GetMapping("/corrections")
    public ResponseEntity<List<Correction>> getAllCorrections(
        @org.springdoc.api.annotations.ParameterObject Pageable pageable,
        @RequestParam(required = false, defaultValue = "true") boolean eagerload
    ) {
        log.debug("REST request to get a page of Corrections");
        Page<Correction> page;
        if (eagerload) {
            page = correctionService.findAllWithEagerRelationships(pageable);
        } else {
            page = correctionService.findAll(pageable);
        }
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(ServletUriComponentsBuilder.fromCurrentRequest(), page);
        return ResponseEntity.ok().headers(headers).body(page.getContent());
    }

    /**
     * {@code GET  /corrections/:id} : get the "id" correction.
     *
     * @param id the id of the correction to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the correction, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/corrections/{id}")
    public ResponseEntity<Correction> getCorrection(@PathVariable Long id) {
        log.debug("REST request to get Correction : {}", id);
        Optional<Correction> correction = correctionService.findOne(id);
        return ResponseUtil.wrapOrNotFound(correction);
    }

    /**
     * {@code DELETE  /corrections/:id} : delete the "id" correction.
     *
     * @param id the id of the correction to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @DeleteMapping("/corrections/{id}")
    public ResponseEntity<Void> deleteCorrection(@PathVariable Long id) {
        log.debug("REST request to delete Correction : {}", id);
        correctionService.delete(id);
        return ResponseEntity
            .noContent()
            .headers(HeaderUtil.createEntityDeletionAlert(applicationName, true, ENTITY_NAME, id.toString()))
            .build();
    }
}
