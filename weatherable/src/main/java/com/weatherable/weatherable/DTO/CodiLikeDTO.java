package com.weatherable.weatherable.DTO;

import com.weatherable.weatherable.Entity.CodiEntity;
import com.weatherable.weatherable.Entity.UserEntity;
import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class CodiLikeDTO {

    private Long id;
    private Long user_id;


    private Long codi_id;
}
