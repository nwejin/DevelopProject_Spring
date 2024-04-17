package com.weatherable.weatherable.Entity;

import com.weatherable.weatherable.enums.*;
import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.CreationTimestamp;


import java.sql.Timestamp;

@Table(name = "closet")
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Getter
@Setter
@Entity
public class ClosetEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "user_id", nullable = false)
    private UserEntity userCloset;

    private MajorCategory majorCategory;

    private MiddleCategory middleCategory;

    private long price;

    private Double score;

    private Color color;

    private String size;

    private Thickness thickness;

    @Column(nullable = false)
    private String productName;

    private String brand;

    @Column(name = "image_path")
    private String imagePath;

    private Style style;

    private Season season;

    @Column(columnDefinition="tinyint(1) default 0")
    private boolean liked;

    @Column(columnDefinition="tinyint(1) default 1")
    private boolean active;

    @CreationTimestamp
    @Column(nullable = false, name = "created_at")
    private Timestamp createdAt;

}
