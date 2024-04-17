package com.weatherable.weatherable.DTO;

import lombok.Builder;
import lombok.Data;
import org.springframework.data.annotation.CreatedDate;

import java.sql.Timestamp;
import java.util.Date;

@Data
@Builder
public class CommentDTO {
    private Long id;
    private Long codi_Id;
    private String userid;
    private String nickname;
    private String content;
    private Timestamp createdAt;
}
