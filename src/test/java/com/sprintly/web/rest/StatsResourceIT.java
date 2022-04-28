package com.sprintly.web.rest;

import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import com.sprintly.IntegrationTest;
import com.sprintly.domain.Stats;
import com.sprintly.repository.StatsRepository;
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

/**
 * Integration tests for the {@link StatsResource} REST controller.
 */
@IntegrationTest
@ExtendWith(MockitoExtension.class)
@AutoConfigureMockMvc
@WithMockUser
class StatsResourceIT {

    private static final Float DEFAULT_DISTANCE = 1F;
    private static final Float UPDATED_DISTANCE = 2F;

    private static final Float DEFAULT_TIME = 1F;
    private static final Float UPDATED_TIME = 2F;

    private static final Float DEFAULT_AVGPACE = 1F;
    private static final Float UPDATED_AVGPACE = 2F;

    private static final String ENTITY_API_URL = "/api/stats";
    private static final String ENTITY_API_URL_ID = ENTITY_API_URL + "/{id}";

    private static Random random = new Random();
    private static AtomicLong count = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    @Autowired
    private StatsRepository statsRepository;

    @Mock
    private StatsRepository statsRepositoryMock;

    @Autowired
    private EntityManager em;

    @Autowired
    private MockMvc restStatsMockMvc;

    private Stats stats;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Stats createEntity(EntityManager em) {
        Stats stats = new Stats().distance(DEFAULT_DISTANCE).time(DEFAULT_TIME).avgpace(DEFAULT_AVGPACE);
        return stats;
    }

    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Stats createUpdatedEntity(EntityManager em) {
        Stats stats = new Stats().distance(UPDATED_DISTANCE).time(UPDATED_TIME).avgpace(UPDATED_AVGPACE);
        return stats;
    }

    @BeforeEach
    public void initTest() {
        stats = createEntity(em);
    }

    @Test
    @Transactional
    void createStats() throws Exception {
        int databaseSizeBeforeCreate = statsRepository.findAll().size();
        // Create the Stats
        restStatsMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(stats)))
            .andExpect(status().isCreated());

        // Validate the Stats in the database
        List<Stats> statsList = statsRepository.findAll();
        assertThat(statsList).hasSize(databaseSizeBeforeCreate + 1);
        Stats testStats = statsList.get(statsList.size() - 1);
        assertThat(testStats.getDistance()).isEqualTo(DEFAULT_DISTANCE);
        assertThat(testStats.getTime()).isEqualTo(DEFAULT_TIME);
        assertThat(testStats.getAvgpace()).isEqualTo(DEFAULT_AVGPACE);
    }

    @Test
    @Transactional
    void createStatsWithExistingId() throws Exception {
        // Create the Stats with an existing ID
        stats.setId(1L);

        int databaseSizeBeforeCreate = statsRepository.findAll().size();

        // An entity with an existing ID cannot be created, so this API call must fail
        restStatsMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(stats)))
            .andExpect(status().isBadRequest());

        // Validate the Stats in the database
        List<Stats> statsList = statsRepository.findAll();
        assertThat(statsList).hasSize(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    void getAllStats() throws Exception {
        // Initialize the database
        statsRepository.saveAndFlush(stats);

        // Get all the statsList
        restStatsMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(stats.getId().intValue())))
            .andExpect(jsonPath("$.[*].distance").value(hasItem(DEFAULT_DISTANCE.doubleValue())))
            .andExpect(jsonPath("$.[*].time").value(hasItem(DEFAULT_TIME.doubleValue())))
            .andExpect(jsonPath("$.[*].avgpace").value(hasItem(DEFAULT_AVGPACE.doubleValue())));
    }

    @SuppressWarnings({ "unchecked" })
    void getAllStatsWithEagerRelationshipsIsEnabled() throws Exception {
        when(statsRepositoryMock.findAllWithEagerRelationships(any())).thenReturn(new PageImpl(new ArrayList<>()));

        restStatsMockMvc.perform(get(ENTITY_API_URL + "?eagerload=true")).andExpect(status().isOk());

        verify(statsRepositoryMock, times(1)).findAllWithEagerRelationships(any());
    }

    @SuppressWarnings({ "unchecked" })
    void getAllStatsWithEagerRelationshipsIsNotEnabled() throws Exception {
        when(statsRepositoryMock.findAllWithEagerRelationships(any())).thenReturn(new PageImpl(new ArrayList<>()));

        restStatsMockMvc.perform(get(ENTITY_API_URL + "?eagerload=true")).andExpect(status().isOk());

        verify(statsRepositoryMock, times(1)).findAllWithEagerRelationships(any());
    }

    @Test
    @Transactional
    void getStats() throws Exception {
        // Initialize the database
        statsRepository.saveAndFlush(stats);

        // Get the stats
        restStatsMockMvc
            .perform(get(ENTITY_API_URL_ID, stats.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.id").value(stats.getId().intValue()))
            .andExpect(jsonPath("$.distance").value(DEFAULT_DISTANCE.doubleValue()))
            .andExpect(jsonPath("$.time").value(DEFAULT_TIME.doubleValue()))
            .andExpect(jsonPath("$.avgpace").value(DEFAULT_AVGPACE.doubleValue()));
    }

    @Test
    @Transactional
    void getNonExistingStats() throws Exception {
        // Get the stats
        restStatsMockMvc.perform(get(ENTITY_API_URL_ID, Long.MAX_VALUE)).andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    void putNewStats() throws Exception {
        // Initialize the database
        statsRepository.saveAndFlush(stats);

        int databaseSizeBeforeUpdate = statsRepository.findAll().size();

        // Update the stats
        Stats updatedStats = statsRepository.findById(stats.getId()).get();
        // Disconnect from session so that the updates on updatedStats are not directly saved in db
        em.detach(updatedStats);
        updatedStats.distance(UPDATED_DISTANCE).time(UPDATED_TIME).avgpace(UPDATED_AVGPACE);

        restStatsMockMvc
            .perform(
                put(ENTITY_API_URL_ID, updatedStats.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(updatedStats))
            )
            .andExpect(status().isOk());

        // Validate the Stats in the database
        List<Stats> statsList = statsRepository.findAll();
        assertThat(statsList).hasSize(databaseSizeBeforeUpdate);
        Stats testStats = statsList.get(statsList.size() - 1);
        assertThat(testStats.getDistance()).isEqualTo(UPDATED_DISTANCE);
        assertThat(testStats.getTime()).isEqualTo(UPDATED_TIME);
        assertThat(testStats.getAvgpace()).isEqualTo(UPDATED_AVGPACE);
    }

    @Test
    @Transactional
    void putNonExistingStats() throws Exception {
        int databaseSizeBeforeUpdate = statsRepository.findAll().size();
        stats.setId(count.incrementAndGet());

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restStatsMockMvc
            .perform(
                put(ENTITY_API_URL_ID, stats.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(stats))
            )
            .andExpect(status().isBadRequest());

        // Validate the Stats in the database
        List<Stats> statsList = statsRepository.findAll();
        assertThat(statsList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithIdMismatchStats() throws Exception {
        int databaseSizeBeforeUpdate = statsRepository.findAll().size();
        stats.setId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restStatsMockMvc
            .perform(
                put(ENTITY_API_URL_ID, count.incrementAndGet())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(stats))
            )
            .andExpect(status().isBadRequest());

        // Validate the Stats in the database
        List<Stats> statsList = statsRepository.findAll();
        assertThat(statsList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithMissingIdPathParamStats() throws Exception {
        int databaseSizeBeforeUpdate = statsRepository.findAll().size();
        stats.setId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restStatsMockMvc
            .perform(put(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(stats)))
            .andExpect(status().isMethodNotAllowed());

        // Validate the Stats in the database
        List<Stats> statsList = statsRepository.findAll();
        assertThat(statsList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void partialUpdateStatsWithPatch() throws Exception {
        // Initialize the database
        statsRepository.saveAndFlush(stats);

        int databaseSizeBeforeUpdate = statsRepository.findAll().size();

        // Update the stats using partial update
        Stats partialUpdatedStats = new Stats();
        partialUpdatedStats.setId(stats.getId());

        partialUpdatedStats.distance(UPDATED_DISTANCE);

        restStatsMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedStats.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedStats))
            )
            .andExpect(status().isOk());

        // Validate the Stats in the database
        List<Stats> statsList = statsRepository.findAll();
        assertThat(statsList).hasSize(databaseSizeBeforeUpdate);
        Stats testStats = statsList.get(statsList.size() - 1);
        assertThat(testStats.getDistance()).isEqualTo(UPDATED_DISTANCE);
        assertThat(testStats.getTime()).isEqualTo(DEFAULT_TIME);
        assertThat(testStats.getAvgpace()).isEqualTo(DEFAULT_AVGPACE);
    }

    @Test
    @Transactional
    void fullUpdateStatsWithPatch() throws Exception {
        // Initialize the database
        statsRepository.saveAndFlush(stats);

        int databaseSizeBeforeUpdate = statsRepository.findAll().size();

        // Update the stats using partial update
        Stats partialUpdatedStats = new Stats();
        partialUpdatedStats.setId(stats.getId());

        partialUpdatedStats.distance(UPDATED_DISTANCE).time(UPDATED_TIME).avgpace(UPDATED_AVGPACE);

        restStatsMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedStats.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedStats))
            )
            .andExpect(status().isOk());

        // Validate the Stats in the database
        List<Stats> statsList = statsRepository.findAll();
        assertThat(statsList).hasSize(databaseSizeBeforeUpdate);
        Stats testStats = statsList.get(statsList.size() - 1);
        assertThat(testStats.getDistance()).isEqualTo(UPDATED_DISTANCE);
        assertThat(testStats.getTime()).isEqualTo(UPDATED_TIME);
        assertThat(testStats.getAvgpace()).isEqualTo(UPDATED_AVGPACE);
    }

    @Test
    @Transactional
    void patchNonExistingStats() throws Exception {
        int databaseSizeBeforeUpdate = statsRepository.findAll().size();
        stats.setId(count.incrementAndGet());

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restStatsMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, stats.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(stats))
            )
            .andExpect(status().isBadRequest());

        // Validate the Stats in the database
        List<Stats> statsList = statsRepository.findAll();
        assertThat(statsList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithIdMismatchStats() throws Exception {
        int databaseSizeBeforeUpdate = statsRepository.findAll().size();
        stats.setId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restStatsMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, count.incrementAndGet())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(stats))
            )
            .andExpect(status().isBadRequest());

        // Validate the Stats in the database
        List<Stats> statsList = statsRepository.findAll();
        assertThat(statsList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithMissingIdPathParamStats() throws Exception {
        int databaseSizeBeforeUpdate = statsRepository.findAll().size();
        stats.setId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restStatsMockMvc
            .perform(patch(ENTITY_API_URL).contentType("application/merge-patch+json").content(TestUtil.convertObjectToJsonBytes(stats)))
            .andExpect(status().isMethodNotAllowed());

        // Validate the Stats in the database
        List<Stats> statsList = statsRepository.findAll();
        assertThat(statsList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void deleteStats() throws Exception {
        // Initialize the database
        statsRepository.saveAndFlush(stats);

        int databaseSizeBeforeDelete = statsRepository.findAll().size();

        // Delete the stats
        restStatsMockMvc
            .perform(delete(ENTITY_API_URL_ID, stats.getId()).accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        List<Stats> statsList = statsRepository.findAll();
        assertThat(statsList).hasSize(databaseSizeBeforeDelete - 1);
    }
}
