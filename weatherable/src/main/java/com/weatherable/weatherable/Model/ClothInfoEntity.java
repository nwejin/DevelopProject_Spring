package com.weatherable.weatherable.Model;


import com.weatherable.weatherable.enums.MajorCategory;
import com.weatherable.weatherable.enums.MiddleCategory;
import com.weatherable.weatherable.enums.Thickness;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.mongodb.core.mapping.Field;

import java.util.List;

@Document(collection = "clothes")
@NoArgsConstructor
@AllArgsConstructor
@Data
public class ClothInfoEntity {
    @Id
    private String id;

    private MajorCategory major_category;

    private MiddleCategory middle_category;

    private long price;

    private Thickness thickness;

    private List<String> season;

    @Field("product_name")
    private String productName;

    private String brand;

    private String small_img;

    private String big_img;
}
