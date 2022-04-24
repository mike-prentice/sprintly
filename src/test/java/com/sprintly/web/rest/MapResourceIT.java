package com.sprintly.web.rest;

import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import com.sprintly.IntegrationTest;
import com.sprintly.domain.Map;
import com.sprintly.repository.MapRepository;
import java.util.List;
import java.util.Random;
import java.util.concurrent.atomic.AtomicLong;
import javax.persistence.EntityManager;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.transaction.annotation.Transactional;

/**
 * Integration tests for the {@link MapResource} REST controller.
 */
@IntegrationTest
@AutoConfigureMockMvc
@WithMockUser
class MapResourceIT {

    private static final String ENTITY_API_URL = "/api/maps";
    private static final String ENTITY_API_URL_ID = ENTITY_API_URL + "/{id}";

    private static Random random = new Random();
    private static AtomicLong count = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    @Autowired
    private MapRepository mapRepository;

    @Autowired
    private EntityManager em;

    @Autowired
    private MockMvc restMapMockMvc;

    private Map map;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Map createEntity(EntityManager em) {
        Map map = new Map();
        return map;
    }

    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Map createUpdatedEntity(EntityManager em) {
        Map map = new Map();
        return map;
    }

    @BeforeEach
    public void initTest() {
        map = createEntity(em);
    }

    @Test
    @Transactional
    void createMap() throws Exception {
        int databaseSizeBeforeCreate = mapRepository.findAll().size();
        // Create the Map
        restMapMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(map)))
            .andExpect(status().isCreated());

        // Validate the Map in the database
        List<Map> mapList = mapRepository.findAll();
        assertThat(mapList).hasSize(databaseSizeBeforeCreate + 1);
        Map testMap = mapList.get(mapList.size() - 1);
    }

    @Test
    @Transactional
    void createMapWithExistingId() throws Exception {
        // Create the Map with an existing ID
        map.setId(1L);

        int databaseSizeBeforeCreate = mapRepository.findAll().size();

        // An entity with an existing ID cannot be created, so this API call must fail
        restMapMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(map)))
            .andExpect(status().isBadRequest());

        // Validate the Map in the database
        List<Map> mapList = mapRepository.findAll();
        assertThat(mapList).hasSize(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    void getAllMaps() throws Exception {
        // Initialize the database
        mapRepository.saveAndFlush(map);

        // Get all the mapList
        restMapMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(map.getId().intValue())));
    }

    @Test
    @Transactional
    void getMap() throws Exception {
        // Initialize the database
        mapRepository.saveAndFlush(map);

        // Get the map
        restMapMockMvc
            .perform(get(ENTITY_API_URL_ID, map.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.id").value(map.getId().intValue()));
    }

    @Test
    @Transactional
    void getNonExistingMap() throws Exception {
        // Get the map
        restMapMockMvc.perform(get(ENTITY_API_URL_ID, Long.MAX_VALUE)).andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    void putNewMap() throws Exception {
        // Initialize the database
        mapRepository.saveAndFlush(map);

        int databaseSizeBeforeUpdate = mapRepository.findAll().size();

        // Update the map
        Map updatedMap = mapRepository.findById(map.getId()).get();
        // Disconnect from session so that the updates on updatedMap are not directly saved in db
        em.detach(updatedMap);

        restMapMockMvc
            .perform(
                put(ENTITY_API_URL_ID, updatedMap.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(updatedMap))
            )
            .andExpect(status().isOk());

        // Validate the Map in the database
        List<Map> mapList = mapRepository.findAll();
        assertThat(mapList).hasSize(databaseSizeBeforeUpdate);
        Map testMap = mapList.get(mapList.size() - 1);
    }

    @Test
    @Transactional
    void putNonExistingMap() throws Exception {
        int databaseSizeBeforeUpdate = mapRepository.findAll().size();
        map.setId(count.incrementAndGet());

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restMapMockMvc
            .perform(
                put(ENTITY_API_URL_ID, map.getId()).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(map))
            )
            .andExpect(status().isBadRequest());

        // Validate the Map in the database
        List<Map> mapList = mapRepository.findAll();
        assertThat(mapList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithIdMismatchMap() throws Exception {
        int databaseSizeBeforeUpdate = mapRepository.findAll().size();
        map.setId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restMapMockMvc
            .perform(
                put(ENTITY_API_URL_ID, count.incrementAndGet())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(map))
            )
            .andExpect(status().isBadRequest());

        // Validate the Map in the database
        List<Map> mapList = mapRepository.findAll();
        assertThat(mapList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithMissingIdPathParamMap() throws Exception {
        int databaseSizeBeforeUpdate = mapRepository.findAll().size();
        map.setId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restMapMockMvc
            .perform(put(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(map)))
            .andExpect(status().isMethodNotAllowed());

        // Validate the Map in the database
        List<Map> mapList = mapRepository.findAll();
        assertThat(mapList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void partialUpdateMapWithPatch() throws Exception {
        // Initialize the database
        mapRepository.saveAndFlush(map);

        int databaseSizeBeforeUpdate = mapRepository.findAll().size();

        // Update the map using partial update
        Map partialUpdatedMap = new Map();
        partialUpdatedMap.setId(map.getId());

        restMapMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedMap.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedMap))
            )
            .andExpect(status().isOk());

        // Validate the Map in the database
        List<Map> mapList = mapRepository.findAll();
        assertThat(mapList).hasSize(databaseSizeBeforeUpdate);
        Map testMap = mapList.get(mapList.size() - 1);
    }

    @Test
    @Transactional
    void fullUpdateMapWithPatch() throws Exception {
        // Initialize the database
        mapRepository.saveAndFlush(map);

        int databaseSizeBeforeUpdate = mapRepository.findAll().size();

        // Update the map using partial update
        Map partialUpdatedMap = new Map();
        partialUpdatedMap.setId(map.getId());

        restMapMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedMap.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedMap))
            )
            .andExpect(status().isOk());

        // Validate the Map in the database
        List<Map> mapList = mapRepository.findAll();
        assertThat(mapList).hasSize(databaseSizeBeforeUpdate);
        Map testMap = mapList.get(mapList.size() - 1);
    }

    @Test
    @Transactional
    void patchNonExistingMap() throws Exception {
        int databaseSizeBeforeUpdate = mapRepository.findAll().size();
        map.setId(count.incrementAndGet());

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restMapMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, map.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(map))
            )
            .andExpect(status().isBadRequest());

        // Validate the Map in the database
        List<Map> mapList = mapRepository.findAll();
        assertThat(mapList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithIdMismatchMap() throws Exception {
        int databaseSizeBeforeUpdate = mapRepository.findAll().size();
        map.setId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restMapMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, count.incrementAndGet())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(map))
            )
            .andExpect(status().isBadRequest());

        // Validate the Map in the database
        List<Map> mapList = mapRepository.findAll();
        assertThat(mapList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithMissingIdPathParamMap() throws Exception {
        int databaseSizeBeforeUpdate = mapRepository.findAll().size();
        map.setId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restMapMockMvc
            .perform(patch(ENTITY_API_URL).contentType("application/merge-patch+json").content(TestUtil.convertObjectToJsonBytes(map)))
            .andExpect(status().isMethodNotAllowed());

        // Validate the Map in the database
        List<Map> mapList = mapRepository.findAll();
        assertThat(mapList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void deleteMap() throws Exception {
        // Initialize the database
        mapRepository.saveAndFlush(map);

        int databaseSizeBeforeDelete = mapRepository.findAll().size();

        // Delete the map
        restMapMockMvc.perform(delete(ENTITY_API_URL_ID, map.getId()).accept(MediaType.APPLICATION_JSON)).andExpect(status().isNoContent());

        // Validate the database contains one less item
        List<Map> mapList = mapRepository.findAll();
        assertThat(mapList).hasSize(databaseSizeBeforeDelete - 1);
    }
}
