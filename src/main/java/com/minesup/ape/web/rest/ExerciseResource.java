package com.minesup.ape.web.rest;

import com.minesup.ape.domain.Exercise;
import com.minesup.ape.repository.ExerciseRepository;
import com.minesup.ape.service.ExerciseService;
import com.minesup.ape.web.rest.errors.BadRequestAlertException;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
import java.util.stream.StreamSupport;
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
 * REST controller for managing {@link com.minesup.ape.domain.Exercise}.
 */
@RestController
@RequestMapping("/api")
public class ExerciseResource {

    private final Logger log = LoggerFactory.getLogger(ExerciseResource.class);

    private static final String ENTITY_NAME = "exercise";

    @Value("${jhipster.clientApp.name}")
    private String applicationName;

    private final ExerciseService exerciseService;

    private final ExerciseRepository exerciseRepository;

    public ExerciseResource(ExerciseService exerciseService, ExerciseRepository exerciseRepository) {
        this.exerciseService = exerciseService;
        this.exerciseRepository = exerciseRepository;
    }

    /**
     * {@code POST  /exercises} : Create a new exercise.
     *
     * @param exercise the exercise to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new exercise, or with status {@code 400 (Bad Request)} if the exercise has already an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping("/exercises")
    public ResponseEntity<Exercise> createExercise(@Valid @RequestBody Exercise exercise) throws URISyntaxException {
        log.debug("REST request to save Exercise : {}", exercise);
        if (exercise.getId() != null) {
            throw new BadRequestAlertException("A new exercise cannot already have an ID", ENTITY_NAME, "idexists");
        }
        Exercise result = exerciseService.save(exercise);
        return ResponseEntity
            .created(new URI("/api/exercises/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(applicationName, true, ENTITY_NAME, result.getId().toString()))
            .body(result);
    }

    /**
     * {@code PUT  /exercises/:id} : Updates an existing exercise.
     *
     * @param id the id of the exercise to save.
     * @param exercise the exercise to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated exercise,
     * or with status {@code 400 (Bad Request)} if the exercise is not valid,
     * or with status {@code 500 (Internal Server Error)} if the exercise couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PutMapping("/exercises/{id}")
    public ResponseEntity<Exercise> updateExercise(
        @PathVariable(value = "id", required = false) final Long id,
        @Valid @RequestBody Exercise exercise
    ) throws URISyntaxException {
        log.debug("REST request to update Exercise : {}, {}", id, exercise);
        if (exercise.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, exercise.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!exerciseRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        Exercise result = exerciseService.update(exercise);
        return ResponseEntity
            .ok()
            .headers(HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, exercise.getId().toString()))
            .body(result);
    }

    /**
     * {@code PATCH  /exercises/:id} : Partial updates given fields of an existing exercise, field will ignore if it is null
     *
     * @param id the id of the exercise to save.
     * @param exercise the exercise to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated exercise,
     * or with status {@code 400 (Bad Request)} if the exercise is not valid,
     * or with status {@code 404 (Not Found)} if the exercise is not found,
     * or with status {@code 500 (Internal Server Error)} if the exercise couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PatchMapping(value = "/exercises/{id}", consumes = { "application/json", "application/merge-patch+json" })
    public ResponseEntity<Exercise> partialUpdateExercise(
        @PathVariable(value = "id", required = false) final Long id,
        @NotNull @RequestBody Exercise exercise
    ) throws URISyntaxException {
        log.debug("REST request to partial update Exercise partially : {}, {}", id, exercise);
        if (exercise.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, exercise.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!exerciseRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        Optional<Exercise> result = exerciseService.partialUpdate(exercise);

        return ResponseUtil.wrapOrNotFound(
            result,
            HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, exercise.getId().toString())
        );
    }

    /**
     * {@code GET  /exercises} : get all the exercises.
     *
     * @param pageable the pagination information.
     * @param eagerload flag to eager load entities from relationships (This is applicable for many-to-many).
     * @param filter the filter of the request.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of exercises in body.
     */
    @GetMapping("/exercises")
    public ResponseEntity<List<Exercise>> getAllExercises(
        @org.springdoc.api.annotations.ParameterObject Pageable pageable,
        @RequestParam(required = false) String filter,
        @RequestParam(required = false, defaultValue = "true") boolean eagerload
    ) {
        if ("correction-is-null".equals(filter)) {
            log.debug("REST request to get all Exercises where correction is null");
            return new ResponseEntity<>(exerciseService.findAllWhereCorrectionIsNull(), HttpStatus.OK);
        }
        log.debug("REST request to get a page of Exercises");
        Page<Exercise> page;
        if (eagerload) {
            page = exerciseService.findAllWithEagerRelationships(pageable);
        } else {
            page = exerciseService.findAll(pageable);
        }
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(ServletUriComponentsBuilder.fromCurrentRequest(), page);
        return ResponseEntity.ok().headers(headers).body(page.getContent());
    }

    /**
     * {@code GET  /exercises/:id} : get the "id" exercise.
     *
     * @param id the id of the exercise to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the exercise, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/exercises/{id}")
    public ResponseEntity<Exercise> getExercise(@PathVariable Long id) {
        log.debug("REST request to get Exercise : {}", id);
        Optional<Exercise> exercise = exerciseService.findOne(id);
        return ResponseUtil.wrapOrNotFound(exercise);
    }

    /**
     * {@code DELETE  /exercises/:id} : delete the "id" exercise.
     *
     * @param id the id of the exercise to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @DeleteMapping("/exercises/{id}")
    public ResponseEntity<Void> deleteExercise(@PathVariable Long id) {
        log.debug("REST request to delete Exercise : {}", id);
        exerciseService.delete(id);
        return ResponseEntity
            .noContent()
            .headers(HeaderUtil.createEntityDeletionAlert(applicationName, true, ENTITY_NAME, id.toString()))
            .build();
    }
}
