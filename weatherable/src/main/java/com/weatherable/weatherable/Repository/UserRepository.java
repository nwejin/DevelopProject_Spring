package com.weatherable.weatherable.Repository;

import com.weatherable.weatherable.Entity.ClosetEntity;
import com.weatherable.weatherable.Entity.UserEntity;
import com.weatherable.weatherable.enums.Style;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface UserRepository extends JpaRepository<UserEntity, Long> {

    Optional<UserEntity> findByUseridAndActive(String userid, boolean active);

    Optional<UserEntity> findByUserid(String userid);
    Optional<UserEntity> findById(long id);

    boolean existsByUseridAndPassword(String userid, String hashedPassword);

    boolean existsByNicknameAndId(String nickname, Long id);
    boolean existsById(long id);

    @Modifying
    @Query(value = "update user set nickname = :nickname where id = :id", nativeQuery = true)
     void updateNickname(String nickname, Long id);

    @Modifying
    @Query(value = "update user set height = :height, weight = :weight where id = :id", nativeQuery = true)
    void updateHeightAndWeight(Double height, Double weight, Long id);

    @Modifying
    @Query(value = "update user set image_path = :imagePath where id = :id", nativeQuery = true)
    void updateImagePath(String imagePath, Long id);

    @Modifying
    @Query(value = "update user set introduction = :introduction where id = :id", nativeQuery = true)
    void updateIntroduction(String introduction, Long id);

    @Modifying
    @Query(value = "update user set password = :password where id = :id", nativeQuery = true)
    void updatePassword(String password, Long id);

    @Modifying
    @Query(value = "update user set favorite_style = :style where id = :id", nativeQuery = true)
    void updateStyle(Style style, Long id);

    @Modifying
    @Query(value = "update user set nickname = :nickname, active = false where id = :id", nativeQuery = true)
    void deleteUser(Long id, String nickname);
}
