package com.weatherable.weatherable.Entity;

import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.CreationTimestamp;
import java.sql.Timestamp;

@Table(name = "comment")
@NoArgsConstructor
@AllArgsConstructor
@Getter
@Builder
@Entity
public class CommentEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;

    @ManyToOne
    @JoinColumn(name = "codi_id", nullable = false)
    private CodiEntity codiComment;

    @ManyToOne
    @JoinColumn(name = "user_id", nullable = false)
    private UserEntity userComment;

    @Column(nullable = false)
    private String content;

    @Column(columnDefinition="tinyint(1) default 1")
    private boolean active;

    @CreationTimestamp
    @Column(nullable = false, name = "created_at")
    private Timestamp createdAt;

}
