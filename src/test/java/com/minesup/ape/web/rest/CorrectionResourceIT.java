package com.minesup.ape.web.rest;

import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import com.minesup.ape.IntegrationTest;
import com.minesup.ape.domain.Correction;
import com.minesup.ape.domain.Exercise;
import com.minesup.ape.repository.CorrectionRepository;
import com.minesup.ape.service.CorrectionService;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import java.util.concurrent.atomic.AtomicLong;
import javax.persistence.EntityManager;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.Base64Utils;

/**
 * Integration tests for the {@link CorrectionResource} REST controller.
 */
@IntegrationTest
@ExtendWith(MockitoExtension.class)
@AutoConfigureMockMvc
@WithMockUser
class CorrectionResourceIT {

    private static final String DEFAULT_CONTENT = "AAAAAAAAAA";
    private static final String UPDATED_CONTENT = "BBBBBBBBBB";

    private static final String ENTITY_API_URL = "/api/corrections";
    private static final String ENTITY_API_URL_ID = ENTITY_API_URL + "/{id}";

    private static Random random = new Random();
    private static AtomicLong count = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    @Autowired
    private CorrectionRepository correctionRepository;

    @Mock
    private CorrectionRepository correctionRepositoryMock;

    @Mock
    private CorrectionService correctionServiceMock;

    @Autowired
    private EntityManager em;

    @Autowired
    private MockMvc restCorrectionMockMvc;

    private Correction correction;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Correction createEntity(EntityManager em) {
        Correction correction = new Correction().content(DEFAULT_CONTENT);
        // Add required entity
        Exercise exercise;
        if (TestUtil.findAll(em, Exercise.class).isEmpty()) {
            exercise = ExerciseResourceIT.createEntity(em);
            em.persist(exercise);
            em.flush();
        } else {
            exercise = TestUtil.findAll(em, Exercise.class).get(0);
        }
        correction.setExercise(exercise);
        return correction;
    }

    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Correction createUpdatedEntity(EntityManager em) {
        Correction correction = new Correction().content(UPDATED_CONTENT);
        // Add required entity
        Exercise exercise;
        if (TestUtil.findAll(em, Exercise.class).isEmpty()) {
            exercise = ExerciseResourceIT.createUpdatedEntity(em);
            em.persist(exercise);
            em.flush();
        } else {
            exercise = TestUtil.findAll(em, Exercise.class).get(0);
        }
        correction.setExercise(exercise);
        return correction;
    }

    @BeforeEach
    public void initTest() {
        correction = createEntity(em);
    }

    @Test
    @Transactional
    void createCorrection() throws Exception {
        int databaseSizeBeforeCreate = correctionRepository.findAll().size();
        // Create the Correction
        restCorrectionMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(correction)))
            .andExpect(status().isCreated());

        // Validate the Correction in the database
        List<Correction> correctionList = correctionRepository.findAll();
        assertThat(correctionList).hasSize(databaseSizeBeforeCreate + 1);
        Correction testCorrection = correctionList.get(correctionList.size() - 1);
        assertThat(testCorrection.getContent()).isEqualTo(DEFAULT_CONTENT);
    }

    @Test
    @Transactional
    void createCorrectionWithExistingId() throws Exception {
        // Create the Correction with an existing ID
        correction.setId(1L);

        int databaseSizeBeforeCreate = correctionRepository.findAll().size();

        // An entity with an existing ID cannot be created, so this API call must fail
        restCorrectionMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(correction)))
            .andExpect(status().isBadRequest());

        // Validate the Correction in the database
        List<Correction> correctionList = correctionRepository.findAll();
        assertThat(correctionList).hasSize(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    void getAllCorrections() throws Exception {
        // Initialize the database
        correctionRepository.saveAndFlush(correction);

        // Get all the correctionList
        restCorrectionMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(correction.getId().intValue())))
            .andExpect(jsonPath("$.[*].content").value(hasItem(DEFAULT_CONTENT.toString())));
    }

    @SuppressWarnings({ "unchecked" })
    void getAllCorrectionsWithEagerRelationshipsIsEnabled() throws Exception {
        when(correctionServiceMock.findAllWithEagerRelationships(any())).thenReturn(new PageImpl(new ArrayList<>()));

        restCorrectionMockMvc.perform(get(ENTITY_API_URL + "?eagerload=true")).andExpect(status().isOk());

        verify(correctionServiceMock, times(1)).findAllWithEagerRelationships(any());
    }

    @SuppressWarnings({ "unchecked" })
    void getAllCorrectionsWithEagerRelationshipsIsNotEnabled() throws Exception {
        when(correctionServiceMock.findAllWithEagerRelationships(any())).thenReturn(new PageImpl(new ArrayList<>()));

        restCorrectionMockMvc.perform(get(ENTITY_API_URL + "?eagerload=true")).andExpect(status().isOk());

        verify(correctionServiceMock, times(1)).findAllWithEagerRelationships(any());
    }

    @Test
    @Transactional
    void getCorrection() throws Exception {
        // Initialize the database
        correctionRepository.saveAndFlush(correction);

        // Get the correction
        restCorrectionMockMvc
            .perform(get(ENTITY_API_URL_ID, correction.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.id").value(correction.getId().intValue()))
            .andExpect(jsonPath("$.content").value(DEFAULT_CONTENT.toString()));
    }

    @Test
    @Transactional
    void getNonExistingCorrection() throws Exception {
        // Get the correction
        restCorrectionMockMvc.perform(get(ENTITY_API_URL_ID, Long.MAX_VALUE)).andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    void putNewCorrection() throws Exception {
        // Initialize the database
        correctionRepository.saveAndFlush(correction);

        int databaseSizeBeforeUpdate = correctionRepository.findAll().size();

        // Update the correction
        Correction updatedCorrection = correctionRepository.findById(correction.getId()).get();
        // Disconnect from session so that the updates on updatedCorrection are not directly saved in db
        em.detach(updatedCorrection);
        updatedCorrection.content(UPDATED_CONTENT);

        restCorrectionMockMvc
            .perform(
                put(ENTITY_API_URL_ID, updatedCorrection.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(updatedCorrection))
            )
            .andExpect(status().isOk());

        // Validate the Correction in the database
        List<Correction> correctionList = correctionRepository.findAll();
        assertThat(correctionList).hasSize(databaseSizeBeforeUpdate);
        Correction testCorrection = correctionList.get(correctionList.size() - 1);
        assertThat(testCorrection.getContent()).isEqualTo(UPDATED_CONTENT);
    }

    @Test
    @Transactional
    void putNonExistingCorrection() throws Exception {
        int databaseSizeBeforeUpdate = correctionRepository.findAll().size();
        correction.setId(count.incrementAndGet());

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restCorrectionMockMvc
            .perform(
                put(ENTITY_API_URL_ID, correction.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(correction))
            )
            .andExpect(status().isBadRequest());

        // Validate the Correction in the database
        List<Correction> correctionList = correctionRepository.findAll();
        assertThat(correctionList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithIdMismatchCorrection() throws Exception {
        int databaseSizeBeforeUpdate = correctionRepository.findAll().size();
        correction.setId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restCorrectionMockMvc
            .perform(
                put(ENTITY_API_URL_ID, count.incrementAndGet())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(correction))
            )
            .andExpect(status().isBadRequest());

        // Validate the Correction in the database
        List<Correction> correctionList = correctionRepository.findAll();
        assertThat(correctionList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithMissingIdPathParamCorrection() throws Exception {
        int databaseSizeBeforeUpdate = correctionRepository.findAll().size();
        correction.setId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restCorrectionMockMvc
            .perform(put(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(correction)))
            .andExpect(status().isMethodNotAllowed());

        // Validate the Correction in the database
        List<Correction> correctionList = correctionRepository.findAll();
        assertThat(correctionList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void partialUpdateCorrectionWithPatch() throws Exception {
        // Initialize the database
        correctionRepository.saveAndFlush(correction);

        int databaseSizeBeforeUpdate = correctionRepository.findAll().size();

        // Update the correction using partial update
        Correction partialUpdatedCorrection = new Correction();
        partialUpdatedCorrection.setId(correction.getId());

        restCorrectionMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedCorrection.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedCorrection))
            )
            .andExpect(status().isOk());

        // Validate the Correction in the database
        List<Correction> correctionList = correctionRepository.findAll();
        assertThat(correctionList).hasSize(databaseSizeBeforeUpdate);
        Correction testCorrection = correctionList.get(correctionList.size() - 1);
        assertThat(testCorrection.getContent()).isEqualTo(DEFAULT_CONTENT);
    }

    @Test
    @Transactional
    void fullUpdateCorrectionWithPatch() throws Exception {
        // Initialize the database
        correctionRepository.saveAndFlush(correction);

        int databaseSizeBeforeUpdate = correctionRepository.findAll().size();

        // Update the correction using partial update
        Correction partialUpdatedCorrection = new Correction();
        partialUpdatedCorrection.setId(correction.getId());

        partialUpdatedCorrection.content(UPDATED_CONTENT);

        restCorrectionMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedCorrection.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedCorrection))
            )
            .andExpect(status().isOk());

        // Validate the Correction in the database
        List<Correction> correctionList = correctionRepository.findAll();
        assertThat(correctionList).hasSize(databaseSizeBeforeUpdate);
        Correction testCorrection = correctionList.get(correctionList.size() - 1);
        assertThat(testCorrection.getContent()).isEqualTo(UPDATED_CONTENT);
    }

    @Test
    @Transactional
    void patchNonExistingCorrection() throws Exception {
        int databaseSizeBeforeUpdate = correctionRepository.findAll().size();
        correction.setId(count.incrementAndGet());

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restCorrectionMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, correction.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(correction))
            )
            .andExpect(status().isBadRequest());

        // Validate the Correction in the database
        List<Correction> correctionList = correctionRepository.findAll();
        assertThat(correctionList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithIdMismatchCorrection() throws Exception {
        int databaseSizeBeforeUpdate = correctionRepository.findAll().size();
        correction.setId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restCorrectionMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, count.incrementAndGet())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(correction))
            )
            .andExpect(status().isBadRequest());

        // Validate the Correction in the database
        List<Correction> correctionList = correctionRepository.findAll();
        assertThat(correctionList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithMissingIdPathParamCorrection() throws Exception {
        int databaseSizeBeforeUpdate = correctionRepository.findAll().size();
        correction.setId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restCorrectionMockMvc
            .perform(
                patch(ENTITY_API_URL).contentType("application/merge-patch+json").content(TestUtil.convertObjectToJsonBytes(correction))
            )
            .andExpect(status().isMethodNotAllowed());

        // Validate the Correction in the database
        List<Correction> correctionList = correctionRepository.findAll();
        assertThat(correctionList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void deleteCorrection() throws Exception {
        // Initialize the database
        correctionRepository.saveAndFlush(correction);

        int databaseSizeBeforeDelete = correctionRepository.findAll().size();

        // Delete the correction
        restCorrectionMockMvc
            .perform(delete(ENTITY_API_URL_ID, correction.getId()).accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        List<Correction> correctionList = correctionRepository.findAll();
        assertThat(correctionList).hasSize(databaseSizeBeforeDelete - 1);
    }
}
