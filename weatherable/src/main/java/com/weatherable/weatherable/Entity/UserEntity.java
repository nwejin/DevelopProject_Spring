package com.weatherable.weatherable.Entity;

import com.weatherable.weatherable.enums.Style;
import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.CreationTimestamp;

import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.List;

@Table(name = "user")
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Getter
@Entity
public class UserEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;

    @Column(nullable = false, unique = true)
    private String userid;

    @Column(nullable = false, length = 1000)
    private String password;

    @Column(nullable = false)
    private String nickname;

    @Column(length = 10000, name = "image_path")
    private String imagePath;

    @Column(columnDefinition = "Text")
    private String introduction;

    @CreationTimestamp
    @Column(nullable = false, name = "created_at")
    private Timestamp createdAt;

    private Double height;
    private Double weight;

    private final String role = "USER";

    @Column(columnDefinition="tinyint(1) default 1")
    private boolean active;

    @Column(name = "favorite_style")
    private Style favoriteStyle;

    @OneToOne(mappedBy = "userEntity")
    private AuthEntity authEntity;

    @OneToMany(mappedBy = "userCodi", cascade = CascadeType.ALL, orphanRemoval = true)
    private final List<CodiEntity> userCodie = new ArrayList<>();

    @OneToMany(mappedBy = "userComment", cascade = CascadeType.ALL, orphanRemoval = true)
    private final List<CommentEntity> comment = new ArrayList<>();

    @OneToMany(mappedBy = "userCloset", cascade = CascadeType.ALL, orphanRemoval = true)
    private final List<ClosetEntity> closet = new ArrayList<>();

    @OneToMany(mappedBy = "userIndex", cascade = CascadeType.ALL, orphanRemoval = true)
    private final List<CodiLikeEntity> userLookBookLikes = new ArrayList<>();

    @OneToOne(mappedBy = "userEntity", cascade = CascadeType.ALL, orphanRemoval = true)
    private UserSizeEntity userSizeEntity;



}
