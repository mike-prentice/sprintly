package com.sprintly.web.rest;

import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import com.sprintly.IntegrationTest;
import com.sprintly.domain.Ranking;
import com.sprintly.repository.RankingRepository;
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
 * Integration tests for the {@link RankingResource} REST controller.
 */
@IntegrationTest
@ExtendWith(MockitoExtension.class)
@AutoConfigureMockMvc
@WithMockUser
class RankingResourceIT {

    private static final String DEFAULT_NAME = "AAAAAAAAAA";
    private static final String UPDATED_NAME = "BBBBBBBBBB";

    private static final Float DEFAULT_AVGPACE = 1F;
    private static final Float UPDATED_AVGPACE = 2F;

    private static final Integer DEFAULT_RANK = 1;
    private static final Integer UPDATED_RANK = 2;

    private static final String ENTITY_API_URL = "/api/rankings";
    private static final String ENTITY_API_URL_ID = ENTITY_API_URL + "/{id}";

    private static Random random = new Random();
    private static AtomicLong count = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    @Autowired
    private RankingRepository rankingRepository;

    @Mock
    private RankingRepository rankingRepositoryMock;

    @Autowired
    private EntityManager em;

    @Autowired
    private MockMvc restRankingMockMvc;

    private Ranking ranking;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Ranking createEntity(EntityManager em) {
        Ranking ranking = new Ranking().name(DEFAULT_NAME).avgpace(DEFAULT_AVGPACE).rank(DEFAULT_RANK);
        return ranking;
    }

    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Ranking createUpdatedEntity(EntityManager em) {
        Ranking ranking = new Ranking().name(UPDATED_NAME).avgpace(UPDATED_AVGPACE).rank(UPDATED_RANK);
        return ranking;
    }

    @BeforeEach
    public void initTest() {
        ranking = createEntity(em);
    }

    @Test
    @Transactional
    void createRanking() throws Exception {
        int databaseSizeBeforeCreate = rankingRepository.findAll().size();
        // Create the Ranking
        restRankingMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(ranking)))
            .andExpect(status().isCreated());

        // Validate the Ranking in the database
        List<Ranking> rankingList = rankingRepository.findAll();
        assertThat(rankingList).hasSize(databaseSizeBeforeCreate + 1);
        Ranking testRanking = rankingList.get(rankingList.size() - 1);
        assertThat(testRanking.getName()).isEqualTo(DEFAULT_NAME);
        assertThat(testRanking.getAvgpace()).isEqualTo(DEFAULT_AVGPACE);
        assertThat(testRanking.getRank()).isEqualTo(DEFAULT_RANK);
    }

    @Test
    @Transactional
    void createRankingWithExistingId() throws Exception {
        // Create the Ranking with an existing ID
        ranking.setId(1L);

        int databaseSizeBeforeCreate = rankingRepository.findAll().size();

        // An entity with an existing ID cannot be created, so this API call must fail
        restRankingMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(ranking)))
            .andExpect(status().isBadRequest());

        // Validate the Ranking in the database
        List<Ranking> rankingList = rankingRepository.findAll();
        assertThat(rankingList).hasSize(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    void getAllRankings() throws Exception {
        // Initialize the database
        rankingRepository.saveAndFlush(ranking);

        // Get all the rankingList
        restRankingMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(ranking.getId().intValue())))
            .andExpect(jsonPath("$.[*].name").value(hasItem(DEFAULT_NAME)))
            .andExpect(jsonPath("$.[*].avgpace").value(hasItem(DEFAULT_AVGPACE.doubleValue())))
            .andExpect(jsonPath("$.[*].rank").value(hasItem(DEFAULT_RANK)));
    }

    @SuppressWarnings({ "unchecked" })
    void getAllRankingsWithEagerRelationshipsIsEnabled() throws Exception {
        when(rankingRepositoryMock.findAllWithEagerRelationships(any())).thenReturn(new PageImpl(new ArrayList<>()));

        restRankingMockMvc.perform(get(ENTITY_API_URL + "?eagerload=true")).andExpect(status().isOk());

        verify(rankingRepositoryMock, times(1)).findAllWithEagerRelationships(any());
    }

    @SuppressWarnings({ "unchecked" })
    void getAllRankingsWithEagerRelationshipsIsNotEnabled() throws Exception {
        when(rankingRepositoryMock.findAllWithEagerRelationships(any())).thenReturn(new PageImpl(new ArrayList<>()));

        restRankingMockMvc.perform(get(ENTITY_API_URL + "?eagerload=true")).andExpect(status().isOk());

        verify(rankingRepositoryMock, times(1)).findAllWithEagerRelationships(any());
    }

    @Test
    @Transactional
    void getRanking() throws Exception {
        // Initialize the database
        rankingRepository.saveAndFlush(ranking);

        // Get the ranking
        restRankingMockMvc
            .perform(get(ENTITY_API_URL_ID, ranking.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.id").value(ranking.getId().intValue()))
            .andExpect(jsonPath("$.name").value(DEFAULT_NAME))
            .andExpect(jsonPath("$.avgpace").value(DEFAULT_AVGPACE.doubleValue()))
            .andExpect(jsonPath("$.rank").value(DEFAULT_RANK));
    }

    @Test
    @Transactional
    void getNonExistingRanking() throws Exception {
        // Get the ranking
        restRankingMockMvc.perform(get(ENTITY_API_URL_ID, Long.MAX_VALUE)).andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    void putNewRanking() throws Exception {
        // Initialize the database
        rankingRepository.saveAndFlush(ranking);

        int databaseSizeBeforeUpdate = rankingRepository.findAll().size();

        // Update the ranking
        Ranking updatedRanking = rankingRepository.findById(ranking.getId()).get();
        // Disconnect from session so that the updates on updatedRanking are not directly saved in db
        em.detach(updatedRanking);
        updatedRanking.name(UPDATED_NAME).avgpace(UPDATED_AVGPACE).rank(UPDATED_RANK);

        restRankingMockMvc
            .perform(
                put(ENTITY_API_URL_ID, updatedRanking.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(updatedRanking))
            )
            .andExpect(status().isOk());

        // Validate the Ranking in the database
        List<Ranking> rankingList = rankingRepository.findAll();
        assertThat(rankingList).hasSize(databaseSizeBeforeUpdate);
        Ranking testRanking = rankingList.get(rankingList.size() - 1);
        assertThat(testRanking.getName()).isEqualTo(UPDATED_NAME);
        assertThat(testRanking.getAvgpace()).isEqualTo(UPDATED_AVGPACE);
        assertThat(testRanking.getRank()).isEqualTo(UPDATED_RANK);
    }

    @Test
    @Transactional
    void putNonExistingRanking() throws Exception {
        int databaseSizeBeforeUpdate = rankingRepository.findAll().size();
        ranking.setId(count.incrementAndGet());

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restRankingMockMvc
            .perform(
                put(ENTITY_API_URL_ID, ranking.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(ranking))
            )
            .andExpect(status().isBadRequest());

        // Validate the Ranking in the database
        List<Ranking> rankingList = rankingRepository.findAll();
        assertThat(rankingList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithIdMismatchRanking() throws Exception {
        int databaseSizeBeforeUpdate = rankingRepository.findAll().size();
        ranking.setId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restRankingMockMvc
            .perform(
                put(ENTITY_API_URL_ID, count.incrementAndGet())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(ranking))
            )
            .andExpect(status().isBadRequest());

        // Validate the Ranking in the database
        List<Ranking> rankingList = rankingRepository.findAll();
        assertThat(rankingList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithMissingIdPathParamRanking() throws Exception {
        int databaseSizeBeforeUpdate = rankingRepository.findAll().size();
        ranking.setId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restRankingMockMvc
            .perform(put(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(ranking)))
            .andExpect(status().isMethodNotAllowed());

        // Validate the Ranking in the database
        List<Ranking> rankingList = rankingRepository.findAll();
        assertThat(rankingList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void partialUpdateRankingWithPatch() throws Exception {
        // Initialize the database
        rankingRepository.saveAndFlush(ranking);

        int databaseSizeBeforeUpdate = rankingRepository.findAll().size();

        // Update the ranking using partial update
        Ranking partialUpdatedRanking = new Ranking();
        partialUpdatedRanking.setId(ranking.getId());

        partialUpdatedRanking.avgpace(UPDATED_AVGPACE);

        restRankingMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedRanking.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedRanking))
            )
            .andExpect(status().isOk());

        // Validate the Ranking in the database
        List<Ranking> rankingList = rankingRepository.findAll();
        assertThat(rankingList).hasSize(databaseSizeBeforeUpdate);
        Ranking testRanking = rankingList.get(rankingList.size() - 1);
        assertThat(testRanking.getName()).isEqualTo(DEFAULT_NAME);
        assertThat(testRanking.getAvgpace()).isEqualTo(UPDATED_AVGPACE);
        assertThat(testRanking.getRank()).isEqualTo(DEFAULT_RANK);
    }

    @Test
    @Transactional
    void fullUpdateRankingWithPatch() throws Exception {
        // Initialize the database
        rankingRepository.saveAndFlush(ranking);

        int databaseSizeBeforeUpdate = rankingRepository.findAll().size();

        // Update the ranking using partial update
        Ranking partialUpdatedRanking = new Ranking();
        partialUpdatedRanking.setId(ranking.getId());

        partialUpdatedRanking.name(UPDATED_NAME).avgpace(UPDATED_AVGPACE).rank(UPDATED_RANK);

        restRankingMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedRanking.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedRanking))
            )
            .andExpect(status().isOk());

        // Validate the Ranking in the database
        List<Ranking> rankingList = rankingRepository.findAll();
        assertThat(rankingList).hasSize(databaseSizeBeforeUpdate);
        Ranking testRanking = rankingList.get(rankingList.size() - 1);
        assertThat(testRanking.getName()).isEqualTo(UPDATED_NAME);
        assertThat(testRanking.getAvgpace()).isEqualTo(UPDATED_AVGPACE);
        assertThat(testRanking.getRank()).isEqualTo(UPDATED_RANK);
    }

    @Test
    @Transactional
    void patchNonExistingRanking() throws Exception {
        int databaseSizeBeforeUpdate = rankingRepository.findAll().size();
        ranking.setId(count.incrementAndGet());

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restRankingMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, ranking.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(ranking))
            )
            .andExpect(status().isBadRequest());

        // Validate the Ranking in the database
        List<Ranking> rankingList = rankingRepository.findAll();
        assertThat(rankingList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithIdMismatchRanking() throws Exception {
        int databaseSizeBeforeUpdate = rankingRepository.findAll().size();
        ranking.setId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restRankingMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, count.incrementAndGet())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(ranking))
            )
            .andExpect(status().isBadRequest());

        // Validate the Ranking in the database
        List<Ranking> rankingList = rankingRepository.findAll();
        assertThat(rankingList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithMissingIdPathParamRanking() throws Exception {
        int databaseSizeBeforeUpdate = rankingRepository.findAll().size();
        ranking.setId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restRankingMockMvc
            .perform(patch(ENTITY_API_URL).contentType("application/merge-patch+json").content(TestUtil.convertObjectToJsonBytes(ranking)))
            .andExpect(status().isMethodNotAllowed());

        // Validate the Ranking in the database
        List<Ranking> rankingList = rankingRepository.findAll();
        assertThat(rankingList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void deleteRanking() throws Exception {
        // Initialize the database
        rankingRepository.saveAndFlush(ranking);

        int databaseSizeBeforeDelete = rankingRepository.findAll().size();

        // Delete the ranking
        restRankingMockMvc
            .perform(delete(ENTITY_API_URL_ID, ranking.getId()).accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        List<Ranking> rankingList = rankingRepository.findAll();
        assertThat(rankingList).hasSize(databaseSizeBeforeDelete - 1);
    }
}
