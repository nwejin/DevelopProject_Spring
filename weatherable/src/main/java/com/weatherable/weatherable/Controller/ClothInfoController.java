package com.weatherable.weatherable.Controller;

import com.weatherable.weatherable.DTO.ClothInfoDTO;
import com.weatherable.weatherable.Service.ClothInfoService;
import com.weatherable.weatherable.enums.DefaultRes;
import com.weatherable.weatherable.enums.StatusCode;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

@RestController
@RequestMapping("/clothinfo")
public class ClothInfoController {

    @Autowired
    ClothInfoService clothInfoService;

    @GetMapping("")
    public ResponseEntity<DefaultRes<List<ClothInfoDTO>>> getAllClothInfo(){
        try {
            List<ClothInfoDTO> result = clothInfoService.getAllClothInfoList();
            return new ResponseEntity<>(
                    DefaultRes.res(StatusCode.OK, "ClothInfo Get 완료", result),
                    HttpStatus.OK);
        } catch (Exception e) {
            return new ResponseEntity<>(
                    DefaultRes.res(StatusCode.BAD_REQUEST, e.getMessage()),
                    HttpStatus.BAD_REQUEST);
        }
    }

    @GetMapping("/cloth/{id}")
    public ResponseEntity<DefaultRes<ClothInfoDTO>> getSingleClothInfoById(@PathVariable String id) {
        try {
            ClothInfoDTO clothInfoDTO = clothInfoService.getClothInfoById(id);
            return new ResponseEntity<>(
                    DefaultRes.res(StatusCode.OK, "ClothInfo Get 완료", clothInfoDTO),
                    HttpStatus.OK);
        } catch (Exception e) {
            return new ResponseEntity<>(
                    DefaultRes.res(StatusCode.BAD_REQUEST, e.getMessage()),
                    HttpStatus.BAD_REQUEST);
        }
    }

    @GetMapping("/search")
    public ResponseEntity<DefaultRes<List<ClothInfoDTO>>> getSearchDataFromClothInfo(@RequestParam String keyWord){
        try {
            List<ClothInfoDTO> resultByProductName = clothInfoService.findByProductNameFromClothInfo(keyWord);
            List<ClothInfoDTO> resultByBrand = clothInfoService.findByBrandFromClothInfo(keyWord);
            List<ClothInfoDTO> result = Stream.concat(resultByBrand.stream(), resultByProductName.stream()).collect(Collectors.toList());
            return new ResponseEntity<>(
                    DefaultRes.res(StatusCode.OK, "ClothInfo search 완료", result),
                    HttpStatus.OK);
        } catch (Exception e) {
            return new ResponseEntity<>(
                    DefaultRes.res(StatusCode.BAD_REQUEST, e.getMessage()),
                    HttpStatus.BAD_REQUEST);
        }
    }


}
