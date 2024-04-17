package com.weatherable.weatherable.Service;

import com.weatherable.weatherable.DTO.CodiLikeDTO;
import com.weatherable.weatherable.Entity.CodiEntity;
import com.weatherable.weatherable.Entity.CodiLikeEntity;
import com.weatherable.weatherable.Entity.UserEntity;
import com.weatherable.weatherable.Repository.CodiLikeRepository;
import com.weatherable.weatherable.Repository.CodiRepository;
import com.weatherable.weatherable.Repository.UserRepository;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class CodiLikeService {

    @Autowired
    CodiLikeRepository codiLikeRepository;

    @Autowired
    UserRepository userRepository;

    @Autowired
    CodiRepository codiRepository;

    public long getCodiLikeCount(Long codiIndex) {
        return codiLikeRepository.countByCodiIndexId(codiIndex);
    }

    @Transactional
    public void likeToggle(CodiLikeDTO codiLikeDTO) throws Exception {
        Long codi_id = codiLikeDTO.getCodi_id();
        Long user_id = codiLikeDTO.getUser_id();

        if (codiLikeRepository.existsByCodiIndexIdAndUserIndexId(codi_id, user_id)) {
            codiLikeRepository.deleteByUserIndexAndCodiIndex(user_id, codi_id);
            return;
        }

        Optional<CodiEntity> codiEntityOptional = codiRepository.findById(codi_id);
        Optional<UserEntity> userEntityOptional = userRepository.findById(user_id);
        if (codiEntityOptional.isEmpty() || userEntityOptional.isEmpty()) {
            throw  new Exception("오류가 발생했습니다.");
        }
        CodiEntity codiEntity = codiEntityOptional.get();
        UserEntity userEntity = userEntityOptional.get();
        CodiLikeEntity codiLikeEntity = CodiLikeEntity.builder()
                .codiIndex(codiEntity)
                .userIndex(userEntity)
                .build();
        codiLikeRepository.save(codiLikeEntity);
    }


}




