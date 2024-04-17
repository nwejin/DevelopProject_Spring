package com.weatherable.weatherable.Repository;

import com.weatherable.weatherable.Entity.CodiEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.sql.Timestamp;
import java.util.List;
import java.util.Optional;

@Repository
public interface CodiRepository extends JpaRepository<CodiEntity, Long> {

    @Modifying
    @Query(value = "update codi set active = false where id = :id", nativeQuery = true)
    void deleteCodi(Long id);

    @Modifying
    @Query(value = "update codi set showing = true where id = :id", nativeQuery = true)
    void showCodi(Long id);


    @Modifying
    @Query(value = "update codi set showing = false where id = :id", nativeQuery = true)
    void hideCodi(Long id);

    Optional<List<CodiEntity>> findByUserCodiIdAndActiveOrderByCodiDateDesc(Long id, boolean active);
    Optional<List<CodiEntity>> findByUserCodiIdAndActiveAndShowingOrderByCodiDateDesc(Long id, boolean active, boolean show);

    Optional<List<CodiEntity>> findByActiveAndShowingOrderByCodiDateDesc(boolean active, boolean showing);

    List<CodiEntity> findByUserCodiIdAndActiveAndCodiDate(Long id, boolean active, Timestamp codiDate);

    Optional<CodiEntity> getByIdAndActive(Long id, boolean access);

    Optional<CodiEntity> getByIdAndActiveAndShowing(Long id, boolean access, boolean showing);


}
