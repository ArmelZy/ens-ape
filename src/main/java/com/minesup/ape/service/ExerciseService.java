package com.minesup.ape.service;

import com.minesup.ape.domain.Exercise;
import com.minesup.ape.repository.ExerciseRepository;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;
import java.util.stream.StreamSupport;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * Service Implementation for managing {@link Exercise}.
 */
@Service
@Transactional
public class ExerciseService {

    private final Logger log = LoggerFactory.getLogger(ExerciseService.class);

    private final ExerciseRepository exerciseRepository;

    public ExerciseService(ExerciseRepository exerciseRepository) {
        this.exerciseRepository = exerciseRepository;
    }

    /**
     * Save a exercise.
     *
     * @param exercise the entity to save.
     * @return the persisted entity.
     */
    public Exercise save(Exercise exercise) {
        log.debug("Request to save Exercise : {}", exercise);
        return exerciseRepository.save(exercise);
    }

    /**
     * Update a exercise.
     *
     * @param exercise the entity to save.
     * @return the persisted entity.
     */
    public Exercise update(Exercise exercise) {
        log.debug("Request to save Exercise : {}", exercise);
        return exerciseRepository.save(exercise);
    }

    /**
     * Partially update a exercise.
     *
     * @param exercise the entity to update partially.
     * @return the persisted entity.
     */
    public Optional<Exercise> partialUpdate(Exercise exercise) {
        log.debug("Request to partially update Exercise : {}", exercise);

        return exerciseRepository
            .findById(exercise.getId())
            .map(existingExercise -> {
                if (exercise.getTitle() != null) {
                    existingExercise.setTitle(exercise.getTitle());
                }
                if (exercise.getContent() != null) {
                    existingExercise.setContent(exercise.getContent());
                }
                if (exercise.getMark() != null) {
                    existingExercise.setMark(exercise.getMark());
                }

                return existingExercise;
            })
            .map(exerciseRepository::save);
    }

    /**
     * Get all the exercises.
     *
     * @param pageable the pagination information.
     * @return the list of entities.
     */
    @Transactional(readOnly = true)
    public Page<Exercise> findAll(Pageable pageable) {
        log.debug("Request to get all Exercises");
        return exerciseRepository.findAll(pageable);
    }

    /**
     * Get all the exercises with eager load of many-to-many relationships.
     *
     * @return the list of entities.
     */
    public Page<Exercise> findAllWithEagerRelationships(Pageable pageable) {
        return exerciseRepository.findAllWithEagerRelationships(pageable);
    }

    /**
     *  Get all the exercises where Correction is {@code null}.
     *  @return the list of entities.
     */
    @Transactional(readOnly = true)
    public List<Exercise> findAllWhereCorrectionIsNull() {
        log.debug("Request to get all exercises where Correction is null");
        return StreamSupport
            .stream(exerciseRepository.findAll().spliterator(), false)
            .filter(exercise -> exercise.getCorrection() == null)
            .collect(Collectors.toList());
    }

    /**
     * Get one exercise by id.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    @Transactional(readOnly = true)
    public Optional<Exercise> findOne(Long id) {
        log.debug("Request to get Exercise : {}", id);
        return exerciseRepository.findOneWithEagerRelationships(id);
    }

    /**
     * Delete the exercise by id.
     *
     * @param id the id of the entity.
     */
    public void delete(Long id) {
        log.debug("Request to delete Exercise : {}", id);
        exerciseRepository.deleteById(id);
    }
}
