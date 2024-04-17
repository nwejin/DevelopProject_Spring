package com.weatherable.weatherable.Repository;

import com.weatherable.weatherable.Entity.CodiLikeEntity;
import jakarta.transaction.Transactional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface CodiLikeRepository extends JpaRepository<CodiLikeEntity, Long> {


    long countByCodiIndexId(Long codi_id);

    boolean existsByCodiIndexIdAndUserIndexId(Long codi_id, Long user_id);

    List<CodiLikeEntity> findByUserIndexId(Long user_id);


    @Transactional
    @Modifying
    @Query(value = "delete from codi_like where user_id= :user_id and codi_id = :codi_id", nativeQuery = true)
    void deleteByUserIndexAndCodiIndex(Long user_id, Long codi_id);

}
