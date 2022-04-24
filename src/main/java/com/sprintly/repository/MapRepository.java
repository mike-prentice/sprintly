package com.sprintly.repository;

import com.sprintly.domain.Map;
import org.springframework.data.jpa.repository.*;
import org.springframework.stereotype.Repository;

/**
 * Spring Data SQL repository for the Map entity.
 */
@SuppressWarnings("unused")
@Repository
public interface MapRepository extends JpaRepository<Map, Long> {}
