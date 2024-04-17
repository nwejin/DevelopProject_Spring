package com.weatherable.weatherable.Entity;

import com.weatherable.weatherable.enums.Style;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.CreationTimestamp;

import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.List;

@Table(name = "user_size")
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Getter
@Entity
public class UserSizeEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;


    @OneToOne
    @JoinColumn(name = "user_id", nullable = false)
    private UserEntity userEntity;

    //    t1 : 상의 총장
    private Double t1;

    //    t2: 상의 가슴단면
    private Double t2;

    //    t3: 상의 어깨너비
    private Double t3;

    //    t4: 상의 소매길이
    private Double t4;

    //    b1: 하의 총장
    private Double b1;

    //    b2: 하의 밑위길이
    private Double b2;

    //    b3: 하의 허리단면
    private Double b3;

    //    b4: 하의 엉덩이단면
    private Double b4;

    private Double b5;

    private Double b6;

    //    o1: 아우터 총장
    private Double o1;

    //    o2: 아우터 가슴단면
    private Double o2;

    //    o3: 아우터 어깨너비
    private Double o3;

    //    o4: 아우터 소매길이
    private Double o4;

    //    s1: 신발 발길이
    private Double s1;

    //    s2: 신발 발볼
    private Double s2;

}
