package com.weatherable.weatherable.Repository;

import com.weatherable.weatherable.Entity.CommentEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface CommentRepository extends JpaRepository<CommentEntity, Long> {

    @Modifying
    @Query(value = "update comment set active = false where id = :id", nativeQuery = true)
    void deleteComment(Long id);

    Optional<List<CommentEntity>> findByCodiCommentIdAndActive(Long codiIndex, boolean active);

    Optional<CommentEntity> findByIdAndActive(Long id, boolean active);



}
