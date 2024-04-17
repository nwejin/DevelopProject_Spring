package com.weatherable.weatherable.Repository;

import com.weatherable.weatherable.Entity.ClosetEntity;
import com.weatherable.weatherable.Entity.UserSizeEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface UserSizeRepository extends JpaRepository<UserSizeEntity, Long> {

    @Query(value = "select * from user_size where user_id = :userIndex", nativeQuery = true)
    Optional<UserSizeEntity> retrieveUserSizeByUserIndex(Long userIndex);



}
