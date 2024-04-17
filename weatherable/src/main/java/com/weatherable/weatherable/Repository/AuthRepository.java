package com.weatherable.weatherable.Repository;

import com.weatherable.weatherable.Entity.AuthEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface AuthRepository extends JpaRepository<AuthEntity, Long> {
    Optional<AuthEntity> findByUserEntityId(Long id);
    Optional<AuthEntity> findByUserEntityUserid(String userid);

    @Modifying
    @Query(value = "update auth set refresh_token = :refreshToken where user_id = :id", nativeQuery = true)
    void updateRefreshToken(String refreshToken, Long id);
}
