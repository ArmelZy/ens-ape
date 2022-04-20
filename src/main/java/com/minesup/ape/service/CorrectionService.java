package com.minesup.ape.service;

import com.minesup.ape.domain.Correction;
import com.minesup.ape.repository.CorrectionRepository;
import java.util.Optional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * Service Implementation for managing {@link Correction}.
 */
@Service
@Transactional
public class CorrectionService {

    private final Logger log = LoggerFactory.getLogger(CorrectionService.class);

    private final CorrectionRepository correctionRepository;

    public CorrectionService(CorrectionRepository correctionRepository) {
        this.correctionRepository = correctionRepository;
    }

    /**
     * Save a correction.
     *
     * @param correction the entity to save.
     * @return the persisted entity.
     */
    public Correction save(Correction correction) {
        log.debug("Request to save Correction : {}", correction);
        return correctionRepository.save(correction);
    }

    /**
     * Update a correction.
     *
     * @param correction the entity to save.
     * @return the persisted entity.
     */
    public Correction update(Correction correction) {
        log.debug("Request to save Correction : {}", correction);
        return correctionRepository.save(correction);
    }

    /**
     * Partially update a correction.
     *
     * @param correction the entity to update partially.
     * @return the persisted entity.
     */
    public Optional<Correction> partialUpdate(Correction correction) {
        log.debug("Request to partially update Correction : {}", correction);

        return correctionRepository
            .findById(correction.getId())
            .map(existingCorrection -> {
                if (correction.getContent() != null) {
                    existingCorrection.setContent(correction.getContent());
                }

                return existingCorrection;
            })
            .map(correctionRepository::save);
    }

    /**
     * Get all the corrections.
     *
     * @param pageable the pagination information.
     * @return the list of entities.
     */
    @Transactional(readOnly = true)
    public Page<Correction> findAll(Pageable pageable) {
        log.debug("Request to get all Corrections");
        return correctionRepository.findAll(pageable);
    }

    /**
     * Get all the corrections with eager load of many-to-many relationships.
     *
     * @return the list of entities.
     */
    public Page<Correction> findAllWithEagerRelationships(Pageable pageable) {
        return correctionRepository.findAllWithEagerRelationships(pageable);
    }

    /**
     * Get one correction by id.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    @Transactional(readOnly = true)
    public Optional<Correction> findOne(Long id) {
        log.debug("Request to get Correction : {}", id);
        return correctionRepository.findOneWithEagerRelationships(id);
    }

    /**
     * Delete the correction by id.
     *
     * @param id the id of the entity.
     */
    public void delete(Long id) {
        log.debug("Request to delete Correction : {}", id);
        correctionRepository.deleteById(id);
    }
}
