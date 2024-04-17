package com.weatherable.weatherable.DTO;

import com.weatherable.weatherable.enums.*;
import lombok.Builder;
import lombok.Data;

import java.util.List;

@Data
@Builder
public class ClothInfoDTO {

    private String id;
    private MajorCategory majorCategory;
    private MiddleCategory middleCategory;
    private long price;
    private String size;
    private List<String> season;
    private Thickness thickness;
    private String productName;
    private String brand;
    private String image_path;
}
