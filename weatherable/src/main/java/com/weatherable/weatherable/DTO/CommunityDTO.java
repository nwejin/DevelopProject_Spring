package com.weatherable.weatherable.DTO;

import lombok.Builder;
import lombok.Data;

import java.sql.Timestamp;

@Data
@Builder
public class CommunityDTO {
    private Long id;
    private Long user_id;
    private String nickname;
    private Long[] likes;
    private String content;
    private Timestamp createdAt;
    private Long[] codi_info;
}
