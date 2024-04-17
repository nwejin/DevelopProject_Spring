package com.weatherable.weatherable.Service;

import com.weatherable.weatherable.DTO.ClothInfoDTO;
import com.weatherable.weatherable.Model.ClothInfoEntity;
import com.weatherable.weatherable.Repository.ClothInfoRepository;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.crossstore.ChangeSetPersister;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;


@Service
public class ClothInfoService {


    @Autowired
    ClothInfoRepository clothInfoRepository;

    public List<ClothInfoDTO> getAllClothInfoList() throws Exception {
        PageRequest pageRequest = PageRequest.of(0, 20000);
        Page<ClothInfoEntity> page = clothInfoRepository.findAll(pageRequest);

        //List<ClothInfoEntity> clothInfoEntityList = clothInfoRepository.findAll();
        if (page.isEmpty()) {
            throw new Exception("옷 정보가 없습니다.");
        }
        List<ClothInfoDTO> result = new ArrayList<>();
        for (ClothInfoEntity clothInfoEntity : page) {
            ClothInfoDTO clothInfoDTO = ClothInfoDTO.builder()
                    .id(clothInfoEntity.getId())
                    .majorCategory(clothInfoEntity.getMajor_category())
                    .middleCategory(clothInfoEntity.getMiddle_category())
                    .price(clothInfoEntity.getPrice())
                    .thickness(clothInfoEntity.getThickness())
                    .brand(clothInfoEntity.getBrand())
                    .image_path(clothInfoEntity.getBig_img())
                    .productName(clothInfoEntity.getProductName())
                    .season(clothInfoEntity.getSeason())
                    .build();
            result.add(clothInfoDTO);
        }
        return result;
    }

    public ClothInfoDTO getClothInfoById(String id) throws ChangeSetPersister.NotFoundException {

        if (clothInfoRepository.findById(id).isEmpty()) {
            throw new ChangeSetPersister.NotFoundException();
        }
        ClothInfoEntity clothInfoEntity = clothInfoRepository.findById(id).get();

        ClothInfoDTO result = ClothInfoDTO.builder()
                .id(clothInfoEntity.getId())
                .majorCategory(clothInfoEntity.getMajor_category())
                .middleCategory(clothInfoEntity.getMiddle_category())
                .price(clothInfoEntity.getPrice())
                .thickness(clothInfoEntity.getThickness())
                .brand(clothInfoEntity.getBrand())
                .image_path(clothInfoEntity.getBig_img())
                .productName(clothInfoEntity.getProductName())
                .season(clothInfoEntity.getSeason())
                .build();

        return result;
    }

    public List<ClothInfoDTO> findByProductNameFromClothInfo(String keyWord) throws Exception {
        List<ClothInfoDTO> result = new ArrayList<>();
        List<ClothInfoEntity> clothInfoEntityList = clothInfoRepository.findByProductNameContainingIgnoreCase(keyWord);
        if (clothInfoEntityList.isEmpty()) {
            return result;
//            throw new Exception("검색 결과가 없습니다.");
        }

        for (ClothInfoEntity clothInfoEntity : clothInfoEntityList) {
            ClothInfoDTO clothInfoDTO = ClothInfoDTO.builder()
                    .id(clothInfoEntity.getId())
                    .majorCategory(clothInfoEntity.getMajor_category())
                    .middleCategory(clothInfoEntity.getMiddle_category())
                    .price(clothInfoEntity.getPrice())
                    .thickness(clothInfoEntity.getThickness())
                    .brand(clothInfoEntity.getBrand())
                    .image_path(clothInfoEntity.getBig_img())
                    .productName(clothInfoEntity.getProductName())
                    .season(clothInfoEntity.getSeason())
                    .build();
            result.add(clothInfoDTO);
        }

        return result;
    } public List<ClothInfoDTO> findByBrandFromClothInfo(String keyWord) throws Exception {

        List<ClothInfoEntity> clothInfoEntityList = clothInfoRepository.findByBrandIgnoreCase(keyWord);
        List<ClothInfoDTO> result = new ArrayList<>();
        if (clothInfoEntityList.isEmpty()) {
            return result;
//            throw new Exception("검색 결과가 없습니다.");
        }
        for (ClothInfoEntity clothInfoEntity : clothInfoEntityList) {
            ClothInfoDTO clothInfoDTO = ClothInfoDTO.builder()
                    .id(clothInfoEntity.getId())
                    .majorCategory(clothInfoEntity.getMajor_category())
                    .middleCategory(clothInfoEntity.getMiddle_category())
                    .price(clothInfoEntity.getPrice())
                    .thickness(clothInfoEntity.getThickness())
                    .brand(clothInfoEntity.getBrand())
                    .image_path(clothInfoEntity.getBig_img())
                    .productName(clothInfoEntity.getProductName())
                    .season(clothInfoEntity.getSeason())
                    .build();
            result.add(clothInfoDTO);
        }

        return result;
    }


}
