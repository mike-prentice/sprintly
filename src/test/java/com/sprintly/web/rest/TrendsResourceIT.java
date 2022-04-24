package com.sprintly.web.rest;

import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import com.sprintly.IntegrationTest;
import com.sprintly.domain.Trends;
import com.sprintly.repository.TrendsRepository;
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
 * Integration tests for the {@link TrendsResource} REST controller.
 */
@IntegrationTest
@ExtendWith(MockitoExtension.class)
@AutoConfigureMockMvc
@WithMockUser
class TrendsResourceIT {

    private static final Float DEFAULT_AVG_PACE = 1F;
    private static final Float UPDATED_AVG_PACE = 2F;

    private static final Float DEFAULT_AVGDISTANCE = 1F;
    private static final Float UPDATED_AVGDISTANCE = 2F;

    private static final String ENTITY_API_URL = "/api/trends";
    private static final String ENTITY_API_URL_ID = ENTITY_API_URL + "/{id}";

    private static Random random = new Random();
    private static AtomicLong count = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    @Autowired
    private TrendsRepository trendsRepository;

    @Mock
    private TrendsRepository trendsRepositoryMock;

    @Autowired
    private EntityManager em;

    @Autowired
    private MockMvc restTrendsMockMvc;

    private Trends trends;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Trends createEntity(EntityManager em) {
        Trends trends = new Trends().avgPace(DEFAULT_AVG_PACE).avgdistance(DEFAULT_AVGDISTANCE);
        return trends;
    }

    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Trends createUpdatedEntity(EntityManager em) {
        Trends trends = new Trends().avgPace(UPDATED_AVG_PACE).avgdistance(UPDATED_AVGDISTANCE);
        return trends;
    }

    @BeforeEach
    public void initTest() {
        trends = createEntity(em);
    }

    @Test
    @Transactional
    void createTrends() throws Exception {
        int databaseSizeBeforeCreate = trendsRepository.findAll().size();
        // Create the Trends
        restTrendsMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(trends)))
            .andExpect(status().isCreated());

        // Validate the Trends in the database
        List<Trends> trendsList = trendsRepository.findAll();
        assertThat(trendsList).hasSize(databaseSizeBeforeCreate + 1);
        Trends testTrends = trendsList.get(trendsList.size() - 1);
        assertThat(testTrends.getAvgPace()).isEqualTo(DEFAULT_AVG_PACE);
        assertThat(testTrends.getAvgdistance()).isEqualTo(DEFAULT_AVGDISTANCE);
    }

    @Test
    @Transactional
    void createTrendsWithExistingId() throws Exception {
        // Create the Trends with an existing ID
        trends.setId(1L);

        int databaseSizeBeforeCreate = trendsRepository.findAll().size();

        // An entity with an existing ID cannot be created, so this API call must fail
        restTrendsMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(trends)))
            .andExpect(status().isBadRequest());

        // Validate the Trends in the database
        List<Trends> trendsList = trendsRepository.findAll();
        assertThat(trendsList).hasSize(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    void getAllTrends() throws Exception {
        // Initialize the database
        trendsRepository.saveAndFlush(trends);

        // Get all the trendsList
        restTrendsMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(trends.getId().intValue())))
            .andExpect(jsonPath("$.[*].avgPace").value(hasItem(DEFAULT_AVG_PACE.doubleValue())))
            .andExpect(jsonPath("$.[*].avgdistance").value(hasItem(DEFAULT_AVGDISTANCE.doubleValue())));
    }

    @SuppressWarnings({ "unchecked" })
    void getAllTrendsWithEagerRelationshipsIsEnabled() throws Exception {
        when(trendsRepositoryMock.findAllWithEagerRelationships(any())).thenReturn(new PageImpl(new ArrayList<>()));

        restTrendsMockMvc.perform(get(ENTITY_API_URL + "?eagerload=true")).andExpect(status().isOk());

        verify(trendsRepositoryMock, times(1)).findAllWithEagerRelationships(any());
    }

    @SuppressWarnings({ "unchecked" })
    void getAllTrendsWithEagerRelationshipsIsNotEnabled() throws Exception {
        when(trendsRepositoryMock.findAllWithEagerRelationships(any())).thenReturn(new PageImpl(new ArrayList<>()));

        restTrendsMockMvc.perform(get(ENTITY_API_URL + "?eagerload=true")).andExpect(status().isOk());

        verify(trendsRepositoryMock, times(1)).findAllWithEagerRelationships(any());
    }

    @Test
    @Transactional
    void getTrends() throws Exception {
        // Initialize the database
        trendsRepository.saveAndFlush(trends);

        // Get the trends
        restTrendsMockMvc
            .perform(get(ENTITY_API_URL_ID, trends.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.id").value(trends.getId().intValue()))
            .andExpect(jsonPath("$.avgPace").value(DEFAULT_AVG_PACE.doubleValue()))
            .andExpect(jsonPath("$.avgdistance").value(DEFAULT_AVGDISTANCE.doubleValue()));
    }

    @Test
    @Transactional
    void getNonExistingTrends() throws Exception {
        // Get the trends
        restTrendsMockMvc.perform(get(ENTITY_API_URL_ID, Long.MAX_VALUE)).andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    void putNewTrends() throws Exception {
        // Initialize the database
        trendsRepository.saveAndFlush(trends);

        int databaseSizeBeforeUpdate = trendsRepository.findAll().size();

        // Update the trends
        Trends updatedTrends = trendsRepository.findById(trends.getId()).get();
        // Disconnect from session so that the updates on updatedTrends are not directly saved in db
        em.detach(updatedTrends);
        updatedTrends.avgPace(UPDATED_AVG_PACE).avgdistance(UPDATED_AVGDISTANCE);

        restTrendsMockMvc
            .perform(
                put(ENTITY_API_URL_ID, updatedTrends.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(updatedTrends))
            )
            .andExpect(status().isOk());

        // Validate the Trends in the database
        List<Trends> trendsList = trendsRepository.findAll();
        assertThat(trendsList).hasSize(databaseSizeBeforeUpdate);
        Trends testTrends = trendsList.get(trendsList.size() - 1);
        assertThat(testTrends.getAvgPace()).isEqualTo(UPDATED_AVG_PACE);
        assertThat(testTrends.getAvgdistance()).isEqualTo(UPDATED_AVGDISTANCE);
    }

    @Test
    @Transactional
    void putNonExistingTrends() throws Exception {
        int databaseSizeBeforeUpdate = trendsRepository.findAll().size();
        trends.setId(count.incrementAndGet());

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restTrendsMockMvc
            .perform(
                put(ENTITY_API_URL_ID, trends.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(trends))
            )
            .andExpect(status().isBadRequest());

        // Validate the Trends in the database
        List<Trends> trendsList = trendsRepository.findAll();
        assertThat(trendsList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithIdMismatchTrends() throws Exception {
        int databaseSizeBeforeUpdate = trendsRepository.findAll().size();
        trends.setId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restTrendsMockMvc
            .perform(
                put(ENTITY_API_URL_ID, count.incrementAndGet())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(trends))
            )
            .andExpect(status().isBadRequest());

        // Validate the Trends in the database
        List<Trends> trendsList = trendsRepository.findAll();
        assertThat(trendsList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithMissingIdPathParamTrends() throws Exception {
        int databaseSizeBeforeUpdate = trendsRepository.findAll().size();
        trends.setId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restTrendsMockMvc
            .perform(put(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(trends)))
            .andExpect(status().isMethodNotAllowed());

        // Validate the Trends in the database
        List<Trends> trendsList = trendsRepository.findAll();
        assertThat(trendsList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void partialUpdateTrendsWithPatch() throws Exception {
        // Initialize the database
        trendsRepository.saveAndFlush(trends);

        int databaseSizeBeforeUpdate = trendsRepository.findAll().size();

        // Update the trends using partial update
        Trends partialUpdatedTrends = new Trends();
        partialUpdatedTrends.setId(trends.getId());

        restTrendsMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedTrends.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedTrends))
            )
            .andExpect(status().isOk());

        // Validate the Trends in the database
        List<Trends> trendsList = trendsRepository.findAll();
        assertThat(trendsList).hasSize(databaseSizeBeforeUpdate);
        Trends testTrends = trendsList.get(trendsList.size() - 1);
        assertThat(testTrends.getAvgPace()).isEqualTo(DEFAULT_AVG_PACE);
        assertThat(testTrends.getAvgdistance()).isEqualTo(DEFAULT_AVGDISTANCE);
    }

    @Test
    @Transactional
    void fullUpdateTrendsWithPatch() throws Exception {
        // Initialize the database
        trendsRepository.saveAndFlush(trends);

        int databaseSizeBeforeUpdate = trendsRepository.findAll().size();

        // Update the trends using partial update
        Trends partialUpdatedTrends = new Trends();
        partialUpdatedTrends.setId(trends.getId());

        partialUpdatedTrends.avgPace(UPDATED_AVG_PACE).avgdistance(UPDATED_AVGDISTANCE);

        restTrendsMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedTrends.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedTrends))
            )
            .andExpect(status().isOk());

        // Validate the Trends in the database
        List<Trends> trendsList = trendsRepository.findAll();
        assertThat(trendsList).hasSize(databaseSizeBeforeUpdate);
        Trends testTrends = trendsList.get(trendsList.size() - 1);
        assertThat(testTrends.getAvgPace()).isEqualTo(UPDATED_AVG_PACE);
        assertThat(testTrends.getAvgdistance()).isEqualTo(UPDATED_AVGDISTANCE);
    }

    @Test
    @Transactional
    void patchNonExistingTrends() throws Exception {
        int databaseSizeBeforeUpdate = trendsRepository.findAll().size();
        trends.setId(count.incrementAndGet());

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restTrendsMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, trends.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(trends))
            )
            .andExpect(status().isBadRequest());

        // Validate the Trends in the database
        List<Trends> trendsList = trendsRepository.findAll();
        assertThat(trendsList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithIdMismatchTrends() throws Exception {
        int databaseSizeBeforeUpdate = trendsRepository.findAll().size();
        trends.setId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restTrendsMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, count.incrementAndGet())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(trends))
            )
            .andExpect(status().isBadRequest());

        // Validate the Trends in the database
        List<Trends> trendsList = trendsRepository.findAll();
        assertThat(trendsList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithMissingIdPathParamTrends() throws Exception {
        int databaseSizeBeforeUpdate = trendsRepository.findAll().size();
        trends.setId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restTrendsMockMvc
            .perform(patch(ENTITY_API_URL).contentType("application/merge-patch+json").content(TestUtil.convertObjectToJsonBytes(trends)))
            .andExpect(status().isMethodNotAllowed());

        // Validate the Trends in the database
        List<Trends> trendsList = trendsRepository.findAll();
        assertThat(trendsList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void deleteTrends() throws Exception {
        // Initialize the database
        trendsRepository.saveAndFlush(trends);

        int databaseSizeBeforeDelete = trendsRepository.findAll().size();

        // Delete the trends
        restTrendsMockMvc
            .perform(delete(ENTITY_API_URL_ID, trends.getId()).accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        List<Trends> trendsList = trendsRepository.findAll();
        assertThat(trendsList).hasSize(databaseSizeBeforeDelete - 1);
    }
}
