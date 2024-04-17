package com.weatherable.weatherable.Entity;

import jakarta.persistence.*;
import lombok.Builder;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
@Table(name = "auth")
@Entity
public class AuthEntity {

    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Id
    private Long id;

    @Column(name = "refresh_token", length = 10000)
    private String refreshToken;

    @OneToOne
    @JoinColumn(name = "user_id")
    private UserEntity userEntity;

    @Builder
    public AuthEntity(String refreshToken, UserEntity usersEntity) {
        this.refreshToken = refreshToken;
        this.userEntity = usersEntity;
    }


}
