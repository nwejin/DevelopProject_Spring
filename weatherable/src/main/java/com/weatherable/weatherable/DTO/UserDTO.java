package com.weatherable.weatherable.DTO;

import com.weatherable.weatherable.enums.Style;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import org.springframework.stereotype.Component;

@Data
@Builder
public class UserDTO {
    private Long id;
    private String userid;
    private String password;
    private String passwordConfirm;
    private String existingPassword;
    private String nickname;
    private String image_path;
    private Double height;
    private Double weight;
    private String imagePath;
    private String introduction;
    private Style favoriteStyle;


}
