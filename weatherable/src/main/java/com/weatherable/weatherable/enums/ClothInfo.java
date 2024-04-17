package com.weatherable.weatherable.enums;

import jakarta.persistence.Column;
import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class ClothInfo {

    private MajorCategory majorCategory;

    private MiddleCategory middleCategory;

    private long price;

    private Color color;

    private String size;

    private Thickness thickness;

    private String productName;

    private String brand;

    @Column(name = "small_image_path")
    private String smallImagePath;

    @Column(name = "big_image_path")
    private String bigImagePath;

    private Style style;

    private Season season;
}
