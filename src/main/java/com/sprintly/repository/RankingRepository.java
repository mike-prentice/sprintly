package com.sprintly.repository;

import com.sprintly.domain.Ranking;
import java.util.List;
import java.util.Optional;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.*;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

/**
 * Spring Data SQL repository for the Ranking entity.
 */
@Repository
public interface RankingRepository extends JpaRepository<Ranking, Long> {
    default Optional<Ranking> findOneWithEagerRelationships(Long id) {
        return this.findOneWithToOneRelationships(id);
    }

    default List<Ranking> findAllWithEagerRelationships() {
        return this.findAllWithToOneRelationships();
    }

    default Page<Ranking> findAllWithEagerRelationships(Pageable pageable) {
        return this.findAllWithToOneRelationships(pageable);
    }

    @Query(
        value = "select distinct ranking from Ranking ranking left join fetch ranking.user",
        countQuery = "select count(distinct ranking) from Ranking ranking"
    )
    Page<Ranking> findAllWithToOneRelationships(Pageable pageable);

    @Query("select distinct ranking from Ranking ranking left join fetch ranking.user")
    List<Ranking> findAllWithToOneRelationships();

    @Query("select ranking from Ranking ranking left join fetch ranking.user where ranking.id =:id")
    Optional<Ranking> findOneWithToOneRelationships(@Param("id") Long id);
}
