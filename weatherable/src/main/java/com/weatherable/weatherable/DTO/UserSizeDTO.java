package com.weatherable.weatherable.DTO;

import com.weatherable.weatherable.Entity.UserEntity;
import jakarta.persistence.*;
import lombok.Builder;
import lombok.Data;

import java.sql.Timestamp;

@Data
@Builder
public class UserSizeDTO {

    private long id;
    private Long user_id;
    private Double t1;
    private Double t2;
    private Double t3;
    private Double t4;
    private Double b1;
    private Double b2;
    private Double b3;
    private Double b4;
    private Double b5;
    private Double b6;
    private Double o1;
    private Double o2;
    private Double o3;
    private Double o4;
    private Double s1;
    private Double s2;

}
