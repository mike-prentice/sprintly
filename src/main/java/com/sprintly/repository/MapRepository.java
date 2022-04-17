package com.sprintly.repository;

import com.sprintly.domain.Map;
import java.util.List;
import java.util.Optional;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.*;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

/**
 * Spring Data SQL repository for the Map entity.
 */
@Repository
public interface MapRepository extends JpaRepository<Map, Long> {
    default Optional<Map> findOneWithEagerRelationships(Long id) {
        return this.findOneWithToOneRelationships(id);
    }

    default List<Map> findAllWithEagerRelationships() {
        return this.findAllWithToOneRelationships();
    }

    default Page<Map> findAllWithEagerRelationships(Pageable pageable) {
        return this.findAllWithToOneRelationships(pageable);
    }

    @Query(value = "select distinct map from Map map left join fetch map.stats", countQuery = "select count(distinct map) from Map map")
    Page<Map> findAllWithToOneRelationships(Pageable pageable);

    @Query("select distinct map from Map map left join fetch map.stats")
    List<Map> findAllWithToOneRelationships();

    @Query("select map from Map map left join fetch map.stats where map.id =:id")
    Optional<Map> findOneWithToOneRelationships(@Param("id") Long id);
}
