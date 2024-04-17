package com.weatherable.weatherable.Service;

import com.weatherable.weatherable.DTO.ClosetDTO;
import com.weatherable.weatherable.DTO.CodiDTO;
import com.weatherable.weatherable.DTO.CodiDTOWithImage;
import com.weatherable.weatherable.Entity.ClosetEntity;
import com.weatherable.weatherable.Entity.CodiEntity;
import com.weatherable.weatherable.Entity.CodiLikeEntity;
import com.weatherable.weatherable.Entity.UserEntity;
import com.weatherable.weatherable.Repository.ClosetRepository;
import com.weatherable.weatherable.Repository.CodiLikeRepository;
import com.weatherable.weatherable.Repository.CodiRepository;
import com.weatherable.weatherable.Repository.UserRepository;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.security.auth.login.AccountNotFoundException;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
public class CodiService {

    @Autowired
    CodiRepository codiRepository;

    @Autowired
    UserRepository userRepository;

    @Autowired
    ClosetRepository closetRepository;

    @Autowired
    CodiLikeRepository codiLikeRepository;

    @Transactional
    public void deleteCodi(Long id) {
        codiRepository.deleteCodi(id);
    }

    @Transactional
    public void createCodi(CodiDTO codiDTO) throws AccountNotFoundException {
        Optional<UserEntity> userEntityOptional = userRepository.findByUseridAndActive(codiDTO.getUserid(), true);
        if (userEntityOptional.isEmpty()) {
            throw new AccountNotFoundException("유저 없음");
        }
        CodiEntity codiEntity = CodiEntity.builder()
                .userCodi(userEntityOptional.get())
                .topIndex(codiDTO.getTopIndex())
                .codiName(codiDTO.getCodiName())
                .bottomIndex(codiDTO.getBottomIndex())
                .active(true)
                .outerIndex(codiDTO.getOuterIndex())
                .shoesIndex(codiDTO.getShoesIndex())
                .accessoryIndex(codiDTO.getAccessoryIndex())
                .capIndex(codiDTO.getCapIndex())
                .codiDate(codiDTO.getCodiDate())
                .build();
        codiRepository.save(codiEntity);
    }

    @Transactional
    public void updateCodi(CodiDTO codiDTO) throws AccountNotFoundException {
        Optional<UserEntity> userEntityOptional = userRepository.findByUseridAndActive(codiDTO.getUserid(), true);
        if (userEntityOptional.isEmpty()) {
            throw new AccountNotFoundException("유저 없음");
        }
        CodiEntity codiEntity = CodiEntity.builder()
                .id(codiDTO.getId())
                .userCodi(userEntityOptional.get())
                .topIndex(codiDTO.getTopIndex())
                .bottomIndex(codiDTO.getBottomIndex())
                .outerIndex(codiDTO.getOuterIndex())
                .codiName(codiDTO.getCodiName())
                .shoesIndex(codiDTO.getShoesIndex())
                .active(true)
                .accessoryIndex(codiDTO.getAccessoryIndex())
                .capIndex(codiDTO.getCapIndex())
                .codiDate(codiDTO.getCodiDate())
                .build();
        codiRepository.save(codiEntity);
    }

    public List<CodiDTOWithImage> retrieveAllCodi(Long user_id) throws Exception {
        Optional<List<CodiEntity>> codiEntityOptional = codiRepository.findByActiveAndShowingOrderByCodiDateDesc(true, true);
        if (codiEntityOptional.isEmpty()) {
            throw new Exception("불러올 코디가 없습니다.");
        }
        List<CodiEntity> codiEntitityList = codiEntityOptional.get();
        List<CodiDTOWithImage> codiDTOList = new ArrayList<>();
        for (var codiEntity : codiEntitityList) {
            ClosetEntity closetTopEntity = closetRepository.getByIdAndActive(codiEntity.getTopIndex(), true).orElseGet(ClosetEntity::new);
            ClosetEntity closetBottomEntity = closetRepository.getByIdAndActive(codiEntity.getBottomIndex(), true).orElseGet(ClosetEntity::new);
            ClosetEntity closetOuterEntity = closetRepository.getByIdAndActive(codiEntity.getOuterIndex(), true).orElseGet(ClosetEntity::new);
            ClosetEntity closetShoesEntity = closetRepository.getByIdAndActive(codiEntity.getShoesIndex(), true).orElseGet(ClosetEntity::new);
            ClosetEntity closetAccessoryEntity = closetRepository.getByIdAndActive(codiEntity.getAccessoryIndex(), true).orElseGet(ClosetEntity::new);
            ClosetEntity closetCapEntity = closetRepository.getByIdAndActive(codiEntity.getCapIndex(), true).orElseGet(ClosetEntity::new);
            Long numberOfLikes = codiLikeRepository.countByCodiIndexId(codiEntity.getId());
            boolean doiLike = codiLikeRepository.existsByCodiIndexIdAndUserIndexId(codiEntity.getId(), user_id);

            var codiDTO = CodiDTOWithImage.builder()
                    .id(codiEntity.getId())
                    .codiDate(codiEntity.getCodiDate())
                    .userid(codiEntity.getUserCodi().getUserid())
                    .nickname(codiEntity.getUserCodi().getNickname())
                    .codiName(codiEntity.getCodiName())
                    .user_id(codiEntity.getUserCodi().getId())
                    .top(transformIntoClosetDTO(closetTopEntity))
                    .bottom(transformIntoClosetDTO(closetBottomEntity))
                    .outer(transformIntoClosetDTO(closetOuterEntity))
                    .shoes(transformIntoClosetDTO(closetShoesEntity))
                    .numberOfLikes(numberOfLikes)
                    .doILike(doiLike)
                    .accessory(transformIntoClosetDTO(closetAccessoryEntity))
                    .cap(transformIntoClosetDTO(closetCapEntity))
                    .build();
            codiDTOList.add(codiDTO);
        }
        return codiDTOList;

    }

    ClosetDTO transformIntoClosetDTO(ClosetEntity closetEntity) {
        return ClosetDTO.builder()
                .id(closetEntity.getId())
                .size(closetEntity.getSize())
                .style(closetEntity.getStyle())
                .middleCategory(closetEntity.getMiddleCategory())
                .price(closetEntity.getPrice())
                .season(closetEntity.getSeason())
                .imagePath(closetEntity.getImagePath())
                .brand(closetEntity.getBrand())
                .color(closetEntity.getColor())
                .majorCategory(closetEntity.getMajorCategory())
                .productName(closetEntity.getProductName())
                .thickness(closetEntity.getThickness())
                .build();
    }

    public List<CodiDTOWithImage> retrieveSomeOnesCodi(Long user_id, Long userIndex) throws Exception {
        Optional<List<CodiEntity>> codiEntityOptional =
                codiRepository.findByUserCodiIdAndActiveAndShowingOrderByCodiDateDesc(user_id, true, true);
        if (codiEntityOptional.isEmpty()) {
            throw new Exception("불러올 코디가 없습니다.");
        }
        List<CodiEntity> codiEntitityList = codiEntityOptional.get();
        List<CodiDTOWithImage> codiDTOList = new ArrayList<>();
        for (var codiEntity : codiEntitityList) {
            ClosetEntity closetTopEntity = closetRepository.getByIdAndActive(codiEntity.getTopIndex(), true).orElseGet(ClosetEntity::new);
            ClosetEntity closetBottomEntity = closetRepository.getByIdAndActive(codiEntity.getBottomIndex(), true).orElseGet(ClosetEntity::new);
            ClosetEntity closetOuterEntity = closetRepository.getByIdAndActive(codiEntity.getOuterIndex(), true).orElseGet(ClosetEntity::new);
            ClosetEntity closetShoesEntity = closetRepository.getByIdAndActive(codiEntity.getShoesIndex(), true).orElseGet(ClosetEntity::new);
            ClosetEntity closetAccessoryEntity = closetRepository.getByIdAndActive(codiEntity.getAccessoryIndex(), true).orElseGet(ClosetEntity::new);
            ClosetEntity closetCapEntity = closetRepository.getByIdAndActive(codiEntity.getCapIndex(), true).orElseGet(ClosetEntity::new);
            Long numberOfLikes = codiLikeRepository.countByCodiIndexId(codiEntity.getId());
            boolean doILike = codiLikeRepository.existsByCodiIndexIdAndUserIndexId(codiEntity.getId(), userIndex);
            var codiDTO = CodiDTOWithImage.builder()
                    .id(codiEntity.getId())
                    .codiDate(codiEntity.getCodiDate())
                    .userid(codiEntity.getUserCodi().getUserid())
                    .nickname(codiEntity.getUserCodi().getNickname())
                    .user_id(codiEntity.getUserCodi().getId())
                    .codiName(codiEntity.getCodiName())
                    .top(transformIntoClosetDTO(closetTopEntity))
                    .bottom(transformIntoClosetDTO(closetBottomEntity))
                    .numberOfLikes(numberOfLikes)
                    .doILike(doILike)
                    .outer(transformIntoClosetDTO(closetOuterEntity))
                    .shoes(transformIntoClosetDTO(closetShoesEntity))
                    .accessory(transformIntoClosetDTO(closetAccessoryEntity))
                    .cap(transformIntoClosetDTO(closetCapEntity))
                    .build();
            codiDTOList.add(codiDTO);
        }
        return codiDTOList;
    }

    public List<CodiDTOWithImage> retrieveMyCodi(Long userIndex) throws Exception {
        Optional<List<CodiEntity>> codiEntityOptional =
                codiRepository.findByUserCodiIdAndActiveOrderByCodiDateDesc(userIndex, true);
        if (codiEntityOptional.isEmpty()) {
            throw new Exception("불러올 코디가 없습니다.");
        }
        List<CodiEntity> codiEntitityList = codiEntityOptional.get();
        List<CodiDTOWithImage> codiDTOList = new ArrayList<>();
        for (var codiEntity : codiEntitityList) {
            ClosetEntity closetTopEntity = closetRepository.getByIdAndActive(codiEntity.getTopIndex(), true).orElseGet(ClosetEntity::new);
            ClosetEntity closetBottomEntity = closetRepository.getByIdAndActive(codiEntity.getBottomIndex(), true).orElseGet(ClosetEntity::new);
            ClosetEntity closetOuterEntity = closetRepository.getByIdAndActive(codiEntity.getOuterIndex(), true).orElseGet(ClosetEntity::new);
            ClosetEntity closetShoesEntity = closetRepository.getByIdAndActive(codiEntity.getShoesIndex(), true).orElseGet(ClosetEntity::new);
            ClosetEntity closetAccessoryEntity = closetRepository.getByIdAndActive(codiEntity.getAccessoryIndex(), true).orElseGet(ClosetEntity::new);
            ClosetEntity closetCapEntity = closetRepository.getByIdAndActive(codiEntity.getCapIndex(), true).orElseGet(ClosetEntity::new);
            Long numberOfLikes = codiLikeRepository.countByCodiIndexId(codiEntity.getId());
            boolean doILike = codiLikeRepository.existsByCodiIndexIdAndUserIndexId(codiEntity.getId(), userIndex);
            var codiDTO = CodiDTOWithImage.builder()
                    .id(codiEntity.getId())
                    .codiDate(codiEntity.getCodiDate())
                    .userid(codiEntity.getUserCodi().getUserid())
                    .nickname(codiEntity.getUserCodi().getNickname())
                    .codiName(codiEntity.getCodiName())
                    .user_id(codiEntity.getUserCodi().getId())
                    .top(transformIntoClosetDTO(closetTopEntity))
                    .bottom(transformIntoClosetDTO(closetBottomEntity))
                    .outer(transformIntoClosetDTO(closetOuterEntity))
                    .numberOfLikes(numberOfLikes)
                    .doILike(doILike)
                    .shoes(transformIntoClosetDTO(closetShoesEntity))
                    .accessory(transformIntoClosetDTO(closetAccessoryEntity))
                    .cap(transformIntoClosetDTO(closetCapEntity))
                    .build();
            codiDTOList.add(codiDTO);
        }
        return codiDTOList;
    }

    public List<CodiDTOWithImage> retrieveMyLikeCodi(Long userIndex) throws Exception {
        List<CodiLikeEntity> codiLikeEntityList =
                codiLikeRepository.findByUserIndexId(userIndex);
        if (codiLikeEntityList.isEmpty()) {
            throw new Exception("불러올 코디가 없습니다.");
        }

        List<CodiDTOWithImage> codiDTOList = new ArrayList<>();
        for (var codiLikeEntity : codiLikeEntityList) {
            var codiEntity = codiLikeEntity.getCodiIndex();
            ClosetEntity closetTopEntity = closetRepository.getByIdAndActive(codiEntity.getTopIndex(), true).orElseGet(ClosetEntity::new);
            ClosetEntity closetBottomEntity = closetRepository.getByIdAndActive(codiEntity.getBottomIndex(), true).orElseGet(ClosetEntity::new);
            ClosetEntity closetOuterEntity = closetRepository.getByIdAndActive(codiEntity.getOuterIndex(), true).orElseGet(ClosetEntity::new);
            ClosetEntity closetShoesEntity = closetRepository.getByIdAndActive(codiEntity.getShoesIndex(), true).orElseGet(ClosetEntity::new);
            ClosetEntity closetAccessoryEntity = closetRepository.getByIdAndActive(codiEntity.getAccessoryIndex(), true).orElseGet(ClosetEntity::new);
            ClosetEntity closetCapEntity = closetRepository.getByIdAndActive(codiEntity.getCapIndex(), true).orElseGet(ClosetEntity::new);
            Long numberOfLikes = codiLikeRepository.countByCodiIndexId(codiEntity.getId());
            boolean doILike = codiLikeRepository.existsByCodiIndexIdAndUserIndexId(codiEntity.getId(), userIndex);
            var codiDTO = CodiDTOWithImage.builder()
                    .id(codiEntity.getId())
                    .codiDate(codiEntity.getCodiDate())
                    .userid(codiEntity.getUserCodi().getUserid())
                    .nickname(codiEntity.getUserCodi().getNickname())
                    .codiName(codiEntity.getCodiName())
                    .user_id(codiEntity.getUserCodi().getId())
                    .top(transformIntoClosetDTO(closetTopEntity))
                    .bottom(transformIntoClosetDTO(closetBottomEntity))
                    .outer(transformIntoClosetDTO(closetOuterEntity))
                    .numberOfLikes(numberOfLikes)
                    .doILike(doILike)
                    .shoes(transformIntoClosetDTO(closetShoesEntity))
                    .accessory(transformIntoClosetDTO(closetAccessoryEntity))
                    .cap(transformIntoClosetDTO(closetCapEntity))
                    .build();
            codiDTOList.add(codiDTO);
        }
        return codiDTOList;
    }

    public CodiDTOWithImage retrieveSingleCodi(Long id, Long user_id) throws Exception {
        Optional<CodiEntity> codiEntityOptional =
                codiRepository.getByIdAndActive(id, true);
        if (codiEntityOptional.isEmpty()) {
            throw new Exception("불러올 코디가 없습니다.");
        }
        CodiEntity codiEntity = codiEntityOptional.get();
        ClosetEntity closetTopEntity = closetRepository.getByIdAndActive(codiEntity.getTopIndex(), true).orElseGet(ClosetEntity::new);
        ClosetEntity closetBottomEntity = closetRepository.getByIdAndActive(codiEntity.getBottomIndex(), true).orElseGet(ClosetEntity::new);
        ClosetEntity closetOuterEntity = closetRepository.getByIdAndActive(codiEntity.getOuterIndex(), true).orElseGet(ClosetEntity::new);
        ClosetEntity closetShoesEntity = closetRepository.getByIdAndActive(codiEntity.getShoesIndex(), true).orElseGet(ClosetEntity::new);
        ClosetEntity closetAccessoryEntity = closetRepository.getByIdAndActive(codiEntity.getAccessoryIndex(), true).orElseGet(ClosetEntity::new);
        ClosetEntity closetCapEntity = closetRepository.getByIdAndActive(codiEntity.getCapIndex(), true).orElseGet(ClosetEntity::new);
        Long numberOfLikes = codiLikeRepository.countByCodiIndexId(codiEntity.getId());
        boolean doILike = codiLikeRepository.existsByCodiIndexIdAndUserIndexId(codiEntity.getId(), user_id);
        CodiDTOWithImage codiDTO = CodiDTOWithImage.builder()
                .id(codiEntity.getId())
                .codiDate(codiEntity.getCodiDate())
                .userid(codiEntity.getUserCodi().getUserid())
                .nickname(codiEntity.getUserCodi().getNickname())
                .user_id(codiEntity.getUserCodi().getId())
                .top(transformIntoClosetDTO(closetTopEntity))
                .codiName(codiEntity.getCodiName())
                .bottom(transformIntoClosetDTO(closetBottomEntity))
                .outer(transformIntoClosetDTO(closetOuterEntity))
                .numberOfLikes(numberOfLikes)
                .doILike(doILike)
                .shoes(transformIntoClosetDTO(closetShoesEntity))
                .accessory(transformIntoClosetDTO(closetAccessoryEntity))
                .cap(transformIntoClosetDTO(closetCapEntity))
                .build();
        return codiDTO;
    }

    public List<CodiDTOWithImage> retrieveByUser_idAndCodiDate(Long user_id, Timestamp timestamp) {
        var codiEntityList = codiRepository.findByUserCodiIdAndActiveAndCodiDate(user_id, true, timestamp);

        List<CodiDTOWithImage> codiDTOList = new ArrayList<>();
        for (var codiEntity : codiEntityList) {
            ClosetEntity closetTopEntity = closetRepository.getByIdAndActive(codiEntity.getTopIndex(), true).orElseGet(ClosetEntity::new);
            ClosetEntity closetBottomEntity = closetRepository.getByIdAndActive(codiEntity.getBottomIndex(), true).orElseGet(ClosetEntity::new);
            ClosetEntity closetOuterEntity = closetRepository.getByIdAndActive(codiEntity.getOuterIndex(), true).orElseGet(ClosetEntity::new);
            ClosetEntity closetShoesEntity = closetRepository.getByIdAndActive(codiEntity.getShoesIndex(), true).orElseGet(ClosetEntity::new);
            ClosetEntity closetAccessoryEntity = closetRepository.getByIdAndActive(codiEntity.getAccessoryIndex(), true).orElseGet(ClosetEntity::new);
            ClosetEntity closetCapEntity = closetRepository.getByIdAndActive(codiEntity.getCapIndex(), true).orElseGet(ClosetEntity::new);
            Long numberOfLikes = codiLikeRepository.countByCodiIndexId(codiEntity.getId());
            boolean doILike = codiLikeRepository.existsByCodiIndexIdAndUserIndexId(codiEntity.getId(), user_id);
            var codiDTO = CodiDTOWithImage.builder()
                    .id(codiEntity.getId())
                    .codiDate(codiEntity.getCodiDate())
                    .userid(codiEntity.getUserCodi().getUserid())
                    .nickname(codiEntity.getUserCodi().getNickname())
                    .codiName(codiEntity.getCodiName())
                    .user_id(codiEntity.getUserCodi().getId())
                    .top(transformIntoClosetDTO(closetTopEntity))
                    .bottom(transformIntoClosetDTO(closetBottomEntity))
                    .outer(transformIntoClosetDTO(closetOuterEntity))
                    .numberOfLikes(numberOfLikes)
                    .doILike(doILike)
                    .shoes(transformIntoClosetDTO(closetShoesEntity))
                    .accessory(transformIntoClosetDTO(closetAccessoryEntity))
                    .cap(transformIntoClosetDTO(closetCapEntity))
                    .build();
            codiDTOList.add(codiDTO);
        }
        return codiDTOList;
    }

}




