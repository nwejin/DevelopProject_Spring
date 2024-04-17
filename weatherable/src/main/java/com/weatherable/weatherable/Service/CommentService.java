package com.weatherable.weatherable.Service;

import com.weatherable.weatherable.DTO.CommentDTO;
import com.weatherable.weatherable.Entity.CodiEntity;
import com.weatherable.weatherable.Entity.CommentEntity;
import com.weatherable.weatherable.Entity.UserEntity;
import com.weatherable.weatherable.Repository.CodiRepository;
import com.weatherable.weatherable.Repository.CommentRepository;
import com.weatherable.weatherable.Repository.UserRepository;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
public class CommentService {

    @Autowired
    CommentRepository commentRepository;

    @Autowired
    CodiRepository codiRepository;

    @Autowired
    UserRepository userRepository;

    @Transactional
    public String deleteComment(Long id) {
        commentRepository.deleteComment(id);
        return "삭제 완료";
    }

    public CommentDTO retrieveSingleComment(Long commentIndex) throws Exception {
        Optional<CommentEntity> commentEntityOptional = commentRepository.findByIdAndActive(commentIndex, true);
        if (commentEntityOptional.isEmpty()) {
            throw new Exception("댓글이 없습니다.");
        }
        CommentEntity commentEntity = commentEntityOptional.get();
        CommentDTO commentDTO = CommentDTO.builder()
                .id(commentEntity.getId())
                .codi_Id(commentEntity.getCodiComment().getId())
                .userid(commentEntity.getUserComment().getUserid())
                .nickname(commentEntity.getUserComment().getNickname())
                .content(commentEntity.getContent())
                .createdAt(commentEntity.getCreatedAt())
                .build();
        return commentDTO;
    }


    public List<CommentDTO> retrieveAllComment(Long codiIndex) throws Exception {
        Optional<List<CommentEntity>> commentEntitiesOptional = commentRepository.findByCodiCommentIdAndActive(codiIndex, true);
        if (commentEntitiesOptional.isEmpty()) {
            throw new Exception("댓글이 없습니다.");
        }
        List<CommentEntity> commentEntities = commentEntitiesOptional.get();
        List<CommentDTO> commentDTOList = new ArrayList<>();
        for (var commentEntity : commentEntities) {
            CommentDTO commentDTO = CommentDTO.builder()
                    .id(commentEntity.getId())
                    .codi_Id(commentEntity.getCodiComment().getId())
                    .userid(commentEntity.getUserComment().getUserid())
                    .nickname(commentEntity.getUserComment().getNickname())
                    .content(commentEntity.getContent())
                    .createdAt(commentEntity.getCreatedAt())
                    .build();
            commentDTOList.add(commentDTO);
        }
        return commentDTOList;
    }
    @Transactional
    public void insertComment(CommentDTO commentDTO) throws Exception {

        Optional<CodiEntity> codiEntityOptional = codiRepository.getByIdAndActiveAndShowing(commentDTO.getCodi_Id(), true, true);
        Optional<UserEntity> userEntityOptional = userRepository.findByUseridAndActive(commentDTO.getUserid(), true);
        if (codiEntityOptional.isEmpty() || userEntityOptional.isEmpty()) {
            throw new Exception("오류가 발생했습니다.");
        }
        CodiEntity codiEntity = codiEntityOptional.get();
        UserEntity userEntity = userEntityOptional.get();

        CommentEntity commentEntity = CommentEntity.builder()
                .codiComment(codiEntity)
                .userComment(userEntity)
                .active(true)
                .content(commentDTO.getContent())
                .build();

        commentRepository.save(commentEntity);
    }
    @Transactional
    public void updateComment(CommentDTO commentDTO) throws Exception {
        Optional<CodiEntity> codiEntityOptional = codiRepository.getByIdAndActiveAndShowing(commentDTO.getCodi_Id(), true, true);
        Optional<UserEntity> userEntityOptional = userRepository.findByUseridAndActive(commentDTO.getUserid(), true);
        if (codiEntityOptional.isEmpty() || userEntityOptional.isEmpty()) {
            throw new Exception("오류가 발생했습니다.");
        }
        CodiEntity codiEntity = codiEntityOptional.get();
        UserEntity userEntity = userEntityOptional.get();

        CommentEntity commentEntity = CommentEntity.builder()
                .id(commentDTO.getId())
                .codiComment(codiEntity)
                .userComment(userEntity)
                .active(true)
                .content(commentDTO.getContent())
                .createdAt(commentDTO.getCreatedAt())
                .build();
    }


}
