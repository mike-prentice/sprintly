package com.sprintly.repository;

import com.sprintly.domain.Trends;
import java.util.List;
import java.util.Optional;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.*;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

/**
 * Spring Data SQL repository for the Trends entity.
 */
@Repository
public interface TrendsRepository extends JpaRepository<Trends, Long> {
    default Optional<Trends> findOneWithEagerRelationships(Long id) {
        return this.findOneWithToOneRelationships(id);
    }

    default List<Trends> findAllWithEagerRelationships() {
        return this.findAllWithToOneRelationships();
    }

    default Page<Trends> findAllWithEagerRelationships(Pageable pageable) {
        return this.findAllWithToOneRelationships(pageable);
    }

    @Query(
        value = "select distinct trends from Trends trends left join fetch trends.user",
        countQuery = "select count(distinct trends) from Trends trends"
    )
    Page<Trends> findAllWithToOneRelationships(Pageable pageable);

    @Query("select distinct trends from Trends trends left join fetch trends.user")
    List<Trends> findAllWithToOneRelationships();

    @Query("select trends from Trends trends left join fetch trends.user where trends.id =:id")
    Optional<Trends> findOneWithToOneRelationships(@Param("id") Long id);
}
