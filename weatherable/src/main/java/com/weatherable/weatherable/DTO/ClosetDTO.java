package com.weatherable.weatherable.DTO;

import com.weatherable.weatherable.enums.*;
import lombok.Builder;
import lombok.Data;

import java.sql.Timestamp;

@Data
@Builder
public class ClosetDTO {
    private Long id;

    private Long user_id;

    private String userid;

    private String nickname;

    private double score;

    private MajorCategory majorCategory;

    private MiddleCategory middleCategory;

    private long price;

    private Color color;

    private boolean liked;

    private String size;

    private Thickness thickness;

    private String productName;

    private String brand;

    private String imagePath;

    private Style style;

    private Season season;

    private Timestamp createdAt;

}
