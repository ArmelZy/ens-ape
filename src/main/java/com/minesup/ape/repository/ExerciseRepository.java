package com.minesup.ape.repository;

import com.minesup.ape.domain.Exercise;
import java.util.List;
import java.util.Optional;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.*;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

/**
 * Spring Data SQL repository for the Exercise entity.
 */
@Repository
public interface ExerciseRepository extends JpaRepository<Exercise, Long> {
    default Optional<Exercise> findOneWithEagerRelationships(Long id) {
        return this.findOneWithToOneRelationships(id);
    }

    default List<Exercise> findAllWithEagerRelationships() {
        return this.findAllWithToOneRelationships();
    }

    default Page<Exercise> findAllWithEagerRelationships(Pageable pageable) {
        return this.findAllWithToOneRelationships(pageable);
    }

    @Query(
        value = "select distinct exercise from Exercise exercise left join fetch exercise.course",
        countQuery = "select count(distinct exercise) from Exercise exercise"
    )
    Page<Exercise> findAllWithToOneRelationships(Pageable pageable);

    @Query("select distinct exercise from Exercise exercise left join fetch exercise.course")
    List<Exercise> findAllWithToOneRelationships();

    @Query("select exercise from Exercise exercise left join fetch exercise.course where exercise.id =:id")
    Optional<Exercise> findOneWithToOneRelationships(@Param("id") Long id);
}
