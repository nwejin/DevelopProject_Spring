package com.weatherable.weatherable.DTO;

import lombok.Builder;
import lombok.Data;

import java.time.LocalDateTime;

@Data
@Builder
public class CodiDTO {
    private long id;
    private Long user_id;
    private Long codiLikeCount;
    private String userid;
    private String nickname;
    private Long topIndex;
    private Long bottomIndex;
    private Long outerIndex;
    private String codiName;
    private Long shoesIndex;
    private Long accessoryIndex;
    private Long capIndex;
    private LocalDateTime codiDate;
}
