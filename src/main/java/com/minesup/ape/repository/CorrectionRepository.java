package com.minesup.ape.repository;

import com.minesup.ape.domain.Correction;
import java.util.List;
import java.util.Optional;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.*;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

/**
 * Spring Data SQL repository for the Correction entity.
 */
@Repository
public interface CorrectionRepository extends JpaRepository<Correction, Long> {
    default Optional<Correction> findOneWithEagerRelationships(Long id) {
        return this.findOneWithToOneRelationships(id);
    }

    default List<Correction> findAllWithEagerRelationships() {
        return this.findAllWithToOneRelationships();
    }

    default Page<Correction> findAllWithEagerRelationships(Pageable pageable) {
        return this.findAllWithToOneRelationships(pageable);
    }

    @Query(
        value = "select distinct correction from Correction correction left join fetch correction.exercise",
        countQuery = "select count(distinct correction) from Correction correction"
    )
    Page<Correction> findAllWithToOneRelationships(Pageable pageable);

    @Query("select distinct correction from Correction correction left join fetch correction.exercise")
    List<Correction> findAllWithToOneRelationships();

    @Query("select correction from Correction correction left join fetch correction.exercise where correction.id =:id")
    Optional<Correction> findOneWithToOneRelationships(@Param("id") Long id);
}
