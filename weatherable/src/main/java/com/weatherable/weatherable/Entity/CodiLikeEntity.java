package com.weatherable.weatherable.Entity;

import jakarta.persistence.*;
import lombok.*;

@Table(name = "codi_like")
@NoArgsConstructor
@AllArgsConstructor
@Getter
@Builder
@Entity
public class CodiLikeEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;

    @ManyToOne
    @JoinColumn(name = "user_id", nullable = false)
    private UserEntity userIndex;


    @ManyToOne
    @JoinColumn(name = "codi_id", nullable = false)
    private CodiEntity codiIndex;
}