package com.weatherable.weatherable.DTO;

import lombok.Builder;
import lombok.Data;

import java.sql.Timestamp;
import java.time.LocalDateTime;

@Data
@Builder
public class CodiDTOWithImage {
    private long id;
    private Long user_id;
    private String codiName;
    private String userid;
    private String nickname;
    private ClosetDTO top;
    private ClosetDTO bottom;
    private ClosetDTO outer;
    private ClosetDTO shoes;
    private ClosetDTO accessory;
    private ClosetDTO cap;
    private Long numberOfLikes;
    private boolean doILike;
    private LocalDateTime codiDate;
}
