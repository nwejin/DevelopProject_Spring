package com.weatherable.weatherable.DTO;

import com.weatherable.weatherable.Entity.UserEntity;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class AuthDTO {

    private Long id;

    private String refreshToken;

    private Long user_id;
}
