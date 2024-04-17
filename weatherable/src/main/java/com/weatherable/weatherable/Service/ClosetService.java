package com.weatherable.weatherable.Service;

import com.weatherable.weatherable.DTO.ClosetDTO;
import com.weatherable.weatherable.Entity.ClosetEntity;
import com.weatherable.weatherable.Entity.UserEntity;
import com.weatherable.weatherable.Repository.ClosetRepository;
import com.weatherable.weatherable.Repository.UserRepository;
import com.weatherable.weatherable.enums.MajorCategory;
import com.weatherable.weatherable.enums.MiddleCategory;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.crossstore.ChangeSetPersister;
import org.springframework.stereotype.Service;

import javax.security.auth.login.AccountNotFoundException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

@Service
public class ClosetService {

    @Autowired
    ClosetRepository closetRepository;

    @Autowired
    UserRepository userRepository;


    public List<ClosetDTO> getAllClothListByUserid(String userid) throws Exception {
        var userEntityOptional = userRepository.findByUseridAndActive(userid, true);
        if (userEntityOptional.isEmpty()) {
            throw new Exception("유저 정보가 없습니다.");
        }
        var userEntity = userEntityOptional.get();
        Optional<List<ClosetEntity>> closetEntitiesOptional = closetRepository.retrieveAllClothByUserIndex(userEntity.getId());
        List<ClosetDTO> result = new ArrayList<>();
        if (closetEntitiesOptional.isEmpty()) {
            throw new Exception("옷장 정보가 없습니다.");
        }

        List<ClosetEntity> closetEntityList = closetEntitiesOptional.get();
        for (ClosetEntity closetEntity : closetEntityList) {
            ClosetDTO closetDTO = ClosetDTO.builder()
                    .id(closetEntity.getId())
                    .productName(closetEntity.getProductName())
                    .user_id(closetEntity.getUserCloset().getId())
                    .userid(closetEntity.getUserCloset().getUserid())
                    .nickname(closetEntity.getUserCloset().getNickname())
                    .majorCategory(closetEntity.getMajorCategory())
                    .middleCategory(closetEntity.getMiddleCategory())
                    .price(closetEntity.getPrice())
                    .brand(closetEntity.getBrand())
                    .color(closetEntity.getColor())
                    .style(closetEntity.getStyle())
                    .size(closetEntity.getSize())
                    .liked(closetEntity.isLiked())
                    .season(closetEntity.getSeason())
                    .imagePath(closetEntity.getImagePath())
                    .thickness(closetEntity.getThickness())
                    .createdAt(closetEntity.getCreatedAt())
                    .build();
            result.add(closetDTO);
        }

        return result;
    }

    public List<ClosetDTO> getMyAllClothList(String userid) throws Exception {
        var userEntityOptional = userRepository.findByUseridAndActive(userid, true);
        if (userEntityOptional.isEmpty()) {
            throw new Exception("유저 정보가 없습니다.");
        }
        var userEntity = userEntityOptional.get();
        Optional<List<ClosetEntity>> closetEntitiesOptional = closetRepository.retrieveAllClothByUserIndex(userEntity.getId());
        List<ClosetDTO> result = new ArrayList<>();
        if (closetEntitiesOptional.isEmpty()) {
            throw new Exception("옷장에 옷이 없습니다.");
        }

        List<ClosetEntity> closetEntityList = closetEntitiesOptional.get();
        for (ClosetEntity closetEntity : closetEntityList) {
            ClosetDTO closetDTO = ClosetDTO.builder()
                    .id(closetEntity.getId())
                    .productName(closetEntity.getProductName())
                    .user_id(closetEntity.getUserCloset().getId())
                    .userid(closetEntity.getUserCloset().getUserid())
                    .nickname(closetEntity.getUserCloset().getNickname())
                    .majorCategory(closetEntity.getMajorCategory())
                    .middleCategory(closetEntity.getMiddleCategory())
                    .price(closetEntity.getPrice())
                    .liked(closetEntity.isLiked())
                    .brand(closetEntity.getBrand())
                    .color(closetEntity.getColor())
                    .style(closetEntity.getStyle())
                    .size(closetEntity.getSize())
                    .season(closetEntity.getSeason())
                    .imagePath(closetEntity.getImagePath())
                    .thickness(closetEntity.getThickness())
                    .createdAt(closetEntity.getCreatedAt())
                    .build();
            result.add(closetDTO);
        }

        return result;
    }


    public List<ClosetDTO> getMyAllLikeClothList(String userid) throws Exception {
        var userEntityOptional = userRepository.findByUseridAndActive(userid, true);
        if (userEntityOptional.isEmpty()) {
            throw new Exception("유저 정보가 없습니다.");
        }
        var userEntity = userEntityOptional.get();
        List<ClosetEntity> closetEntityList = closetRepository.findByUserClosetIdAndActiveAndLiked(userEntity.getId(), true, true);
        List<ClosetDTO> result = new ArrayList<>();
        if (closetEntityList.isEmpty()) {
            throw new Exception("옷장에 옷이 없습니다.");
        }

        for (ClosetEntity closetEntity : closetEntityList) {
            ClosetDTO closetDTO = ClosetDTO.builder()
                    .id(closetEntity.getId())
                    .productName(closetEntity.getProductName())
                    .user_id(closetEntity.getUserCloset().getId())
                    .userid(closetEntity.getUserCloset().getUserid())
                    .nickname(closetEntity.getUserCloset().getNickname())
                    .majorCategory(closetEntity.getMajorCategory())
                    .middleCategory(closetEntity.getMiddleCategory())
                    .price(closetEntity.getPrice())
                    .liked(closetEntity.isLiked())
                    .brand(closetEntity.getBrand())
                    .color(closetEntity.getColor())
                    .style(closetEntity.getStyle())
                    .size(closetEntity.getSize())
                    .season(closetEntity.getSeason())
                    .imagePath(closetEntity.getImagePath())
                    .thickness(closetEntity.getThickness())
                    .createdAt(closetEntity.getCreatedAt())
                    .build();
            result.add(closetDTO);
        }

        return result;
    }

    public List<ClosetDTO> retrieveAllClosetDTOByUserIndexAndMajorCategory(Long user_id, MajorCategory major) throws Exception {
        var closetEntityList = closetRepository.findByUserClosetIdAndMajorCategoryAndActive(user_id, major, true);
        if(closetEntityList.isEmpty()) {
            return new ArrayList<>();
        }
        List<ClosetDTO> result = new ArrayList<>();
        for (ClosetEntity closetEntity : closetEntityList) {
            ClosetDTO closetDTO = ClosetDTO.builder()
                    .id(closetEntity.getId())
                    .productName(closetEntity.getProductName())
                    .user_id(closetEntity.getUserCloset().getId())
                    .userid(closetEntity.getUserCloset().getUserid())
                    .nickname(closetEntity.getUserCloset().getNickname())
                    .majorCategory(closetEntity.getMajorCategory())
                    .middleCategory(closetEntity.getMiddleCategory())
                    .price(closetEntity.getPrice())
                    .liked(closetEntity.isLiked())
                    .brand(closetEntity.getBrand())
                    .color(closetEntity.getColor())
                    .style(closetEntity.getStyle())
                    .size(closetEntity.getSize())
                    .season(closetEntity.getSeason())
                    .imagePath(closetEntity.getImagePath())
                    .thickness(closetEntity.getThickness())
                    .createdAt(closetEntity.getCreatedAt())
                    .build();
            result.add(closetDTO);
        }

        return result;
    }

    public List<ClosetDTO> retrieveAllClosetDTOByUserIndexAndMiddleCategory(Long user_id, MiddleCategory middle) throws Exception {
        var closetEntityList = closetRepository.findByUserClosetIdAndMiddleCategoryAndActive(user_id, middle, true);
        if(closetEntityList.isEmpty()) {
            return new ArrayList<>();
        }
        List<ClosetDTO> result = new ArrayList<>();
        for (ClosetEntity closetEntity : closetEntityList) {
            ClosetDTO closetDTO = ClosetDTO.builder()
                    .id(closetEntity.getId())
                    .productName(closetEntity.getProductName())
                    .user_id(closetEntity.getUserCloset().getId())
                    .userid(closetEntity.getUserCloset().getUserid())
                    .nickname(closetEntity.getUserCloset().getNickname())
                    .majorCategory(closetEntity.getMajorCategory())
                    .middleCategory(closetEntity.getMiddleCategory())
                    .price(closetEntity.getPrice())
                    .liked(closetEntity.isLiked())
                    .brand(closetEntity.getBrand())
                    .color(closetEntity.getColor())
                    .style(closetEntity.getStyle())
                    .size(closetEntity.getSize())
                    .season(closetEntity.getSeason())
                    .imagePath(closetEntity.getImagePath())
                    .thickness(closetEntity.getThickness())
                    .createdAt(closetEntity.getCreatedAt())
                    .build();
            result.add(closetDTO);
        }

        return result;
    }
    @Transactional
    public String insertCloth(ClosetDTO closetDTO) throws Exception {
        var userEntityOptional = userRepository.findByUseridAndActive(closetDTO.getUserid(), true);
        if (userEntityOptional.isEmpty()) {
            throw new Exception("유저 정보가 없습니다.");
        }

        var userEntity = userEntityOptional.get();
        ClosetEntity closetEntity = ClosetEntity.builder()
                .majorCategory(closetDTO.getMajorCategory())
                .middleCategory(closetDTO.getMiddleCategory())
                .userCloset(userEntity)
                .imagePath(closetDTO.getImagePath())
                .style(closetDTO.getStyle())
                .size(closetDTO.getSize())
                .brand(closetDTO.getBrand())
                .score(closetDTO.getScore())
                .liked(false)
                .color(closetDTO.getColor())
                .season(closetDTO.getSeason())
                .productName(closetDTO.getProductName())
                .thickness(closetDTO.getThickness())
                .price(closetDTO.getPrice())
                .active(true)
                .build();

        ClosetEntity result = closetRepository.save(closetEntity);
        return result.getProductName() + "등록완료";
    }

    @Transactional
    public String updateCloth(ClosetDTO closetDTO) throws Exception {
        var userEntityOptional = userRepository.findByUseridAndActive(closetDTO.getUserid(), true);
        if (userEntityOptional.isEmpty()) {
            throw new Exception("유저 정보가 없습니다.");
        }
        var userEntity = userEntityOptional.get();

        ClosetEntity closetEntity = closetRepository.findById(closetDTO.getId())
                .orElseThrow(() -> new Exception("해당하는 아이템을 찾을 수 없습니다."));

        closetEntity.setProductName(closetDTO.getProductName());
        closetEntity.setSize(closetDTO.getSize());
        closetEntity.setPrice(closetDTO.getPrice());

        ClosetEntity result = closetRepository.save(closetEntity);
        return result.getProductName() + "수정 완료";
    }

@Transactional
    public void deleteCloth(Long id) {
        closetRepository.deleteCloset(id);
    }
    @Transactional
    public void likeCloth(Long id) {
        closetRepository.likeCloset(id);
    }
    @Transactional
    public void unlikeCloth(Long id) {
        closetRepository.unlikeCloset(id);
    }


    public ClosetDTO retrieveClosetClothById(Long id) throws ChangeSetPersister.NotFoundException {
        if (closetRepository.getByIdAndActive(id, true).isEmpty()) {
            throw new ChangeSetPersister.NotFoundException();
        }
        ClosetEntity closetEntity = closetRepository.getByIdAndActive(id, true).get();

        ClosetDTO closetDTO = ClosetDTO.builder()
                .id(closetEntity.getId())
                .user_id(closetEntity.getUserCloset().getId())
                .size(closetEntity.getSize())
                .style(closetEntity.getStyle())
                .middleCategory(closetEntity.getMiddleCategory())
                .price(closetEntity.getPrice())
                .season(closetEntity.getSeason())
                .imagePath(closetEntity.getImagePath())
                .brand(closetEntity.getBrand())
                .liked(closetEntity.isLiked())
                .createdAt(closetEntity.getCreatedAt())
                .color(closetEntity.getColor())
                .majorCategory(closetEntity.getMajorCategory())
                .productName(closetEntity.getProductName())
                .thickness(closetEntity.getThickness())
                .userid(closetEntity.getUserCloset().getUserid())
                .nickname(closetEntity.getUserCloset().getNickname())
                .build();
        return closetDTO;
    }

    public ClosetDTO retrieveClosetClothByProductName(String productName, Long user_id) {
        if(productName.contains(",")) {
            productName = productName.substring(0,productName.charAt(','));
        }

        var closetEntityList = closetRepository.findByProductNameAndUserClosetIdAndActive(productName, user_id, true);
        if(closetEntityList.isEmpty()) {
            return ClosetDTO.builder()
                    .productName("없음")
                    .build();
        }
        var closetEntity = closetEntityList.get(0);
        ClosetDTO closetDTO = ClosetDTO.builder()
                .id(closetEntity.getId())
                .user_id(closetEntity.getUserCloset().getId())
                .size(closetEntity.getSize())
                .style(closetEntity.getStyle())
                .middleCategory(closetEntity.getMiddleCategory())
                .price(closetEntity.getPrice())
                .season(closetEntity.getSeason())
                .imagePath(closetEntity.getImagePath())
                .brand(closetEntity.getBrand())
                .liked(closetEntity.isLiked())
                .createdAt(closetEntity.getCreatedAt())
                .color(closetEntity.getColor())
                .majorCategory(closetEntity.getMajorCategory())
                .productName(closetEntity.getProductName())
                .thickness(closetEntity.getThickness())
                .userid(closetEntity.getUserCloset().getUserid())
                .nickname(closetEntity.getUserCloset().getNickname())
                .build();
        return closetDTO;
    }

    public List<ClosetDTO> processGPTResponse(String response, Long user_id) {
        List<String> responseList = Arrays.stream(response.split(":|\\n")).toList();
        List<ClosetDTO> closetDTOList = new ArrayList<>();
        for ( int i = 1; i < responseList.size(); i+= 2) {
            closetDTOList.add(retrieveClosetClothByProductName(responseList.get(i).trim(), user_id));
        }
        return closetDTOList;
    }

}
