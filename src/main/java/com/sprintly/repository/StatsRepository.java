package com.sprintly.repository;

import com.sprintly.domain.Stats;
import java.util.List;
import java.util.Optional;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.*;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

/**
 * Spring Data SQL repository for the Stats entity.
 */
@Repository
public interface StatsRepository extends JpaRepository<Stats, Long> {
    default Optional<Stats> findOneWithEagerRelationships(Long id) {
        return this.findOneWithToOneRelationships(id);
    }

    default List<Stats> findAllWithEagerRelationships() {
        return this.findAllWithToOneRelationships();
    }

    default Page<Stats> findAllWithEagerRelationships(Pageable pageable) {
        return this.findAllWithToOneRelationships(pageable);
    }

    @Query(
        value = "select distinct stats from Stats stats left join fetch stats.user",
        countQuery = "select count(distinct stats) from Stats stats"
    )
    Page<Stats> findAllWithToOneRelationships(Pageable pageable);

    @Query("select distinct stats from Stats stats left join fetch stats.user")
    List<Stats> findAllWithToOneRelationships();

    @Query("select stats from Stats stats left join fetch stats.user where stats.id =:id")
    Optional<Stats> findOneWithToOneRelationships(@Param("id") Long id);

    @Query("select stats from Stats stats left join fetch stats.user where stats.user.login =:#{principal.username}")
    List<Stats> findByUserIsCurrentUser();
}
