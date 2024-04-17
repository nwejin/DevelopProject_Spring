package com.weatherable.weatherable.Controller;

import com.weatherable.weatherable.DTO.ClosetDTO;
import com.weatherable.weatherable.Service.ClosetService;
import com.weatherable.weatherable.Service.JwtUtilsService;
import com.weatherable.weatherable.Service.S3Upload;
import com.weatherable.weatherable.Service.UserService;
import com.weatherable.weatherable.enums.DefaultRes;
import com.weatherable.weatherable.enums.MajorCategory;
import com.weatherable.weatherable.enums.MiddleCategory;
import com.weatherable.weatherable.enums.StatusCode;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;


import java.io.IOException;
import java.util.List;

@RestController
@RequestMapping("/closet")
public class ClosetController {

    @Autowired
    ClosetService closetService;

    @Autowired
    JwtUtilsService jwtUtilsService;

    @Autowired
    UserService userService;

    @Autowired
    S3Upload s3Upload;


    @GetMapping("")
    public ResponseEntity<DefaultRes<List<ClosetDTO>>> getClosetByUserid(@RequestHeader("Authorization") String accessToken) {
        try {
            String userid = jwtUtilsService.retrieveUserid(accessToken);
            List<ClosetDTO> result = closetService.getAllClothListByUserid(userid);
            return new ResponseEntity<>(
                    DefaultRes.res(StatusCode.OK, "Closet fetch 완료", result),
                    HttpStatus.OK);
        } catch (Exception e) {
            return new ResponseEntity<>(
                    DefaultRes.res(StatusCode.BAD_REQUEST, e.getMessage()),
                    HttpStatus.BAD_REQUEST);
        }
    }

    @PostMapping("")
    public ResponseEntity<DefaultRes<String>> insertCloth(@RequestHeader("Authorization") String accessToken, @RequestBody ClosetDTO closetDTO) {
        try {
            String userid = jwtUtilsService.retrieveUserid(accessToken);
            closetDTO.setUserid(userid);
            String result = closetService.insertCloth(closetDTO);
            return new ResponseEntity<>(
                    DefaultRes.res(StatusCode.CREATED, result),
                    HttpStatus.CREATED);
        } catch (Exception e) {
            return new ResponseEntity<>(
                    DefaultRes.res(StatusCode.BAD_REQUEST, e.getMessage()),
                    HttpStatus.BAD_REQUEST);
        }
    }
    @PostMapping("/image")
    public ResponseEntity<DefaultRes<String>> uploadImage(@RequestParam("image") MultipartFile imageFile) throws IOException {
        try {
            String imageUrl = s3Upload.saveImageFile(imageFile);
            return new ResponseEntity<>(
                    DefaultRes.res(StatusCode.CREATED, "image Url fetched!", imageUrl),
                    HttpStatus.CREATED);
        } catch (Exception e) {
            return new ResponseEntity<>(
                    DefaultRes.res(StatusCode.BAD_REQUEST, e.getMessage()),
                    HttpStatus.BAD_REQUEST);
        }
    }

    @GetMapping("/cloth/{id}")
    public ResponseEntity<DefaultRes<ClosetDTO>> getSingleClosetById(@PathVariable Long id) {
        try {
            ClosetDTO closetDTO = closetService.retrieveClosetClothById(id);
            return new ResponseEntity<>(
                    DefaultRes.res(StatusCode.OK, "Cloth fetch 완료", closetDTO),
                    HttpStatus.OK);
        } catch (Exception e) {
            return new ResponseEntity<>(
                    DefaultRes.res(StatusCode.BAD_REQUEST, e.getMessage()),
                    HttpStatus.BAD_REQUEST);
        }
    }

    @PatchMapping("/modify")
    public ResponseEntity<DefaultRes<String>> updateSingleClosetById(@RequestHeader("Authorization") String accessToken, @RequestBody ClosetDTO closetDTO) {
        try {
            String userid = jwtUtilsService.retrieveUserid(accessToken);
            closetDTO.setUserid(userid);
            String result = closetService.updateCloth(closetDTO);
            return new ResponseEntity<>(
                    DefaultRes.res(StatusCode.OK, result),
                    HttpStatus.OK);
        } catch (Exception e) {
            return new ResponseEntity<>(
                    DefaultRes.res(StatusCode.BAD_REQUEST, e.getMessage()),
                    HttpStatus.BAD_REQUEST);
        }
    }

    @PatchMapping("")
    public ResponseEntity<DefaultRes<String>> toggleLike(@RequestBody ClosetDTO closetDTO) {
        try {
            if (closetDTO.isLiked()) {
                closetService.unlikeCloth(closetDTO.getId());
            } else {
                closetService.likeCloth(closetDTO.getId());
            }
            return new ResponseEntity<>(
                    DefaultRes.res(StatusCode.OK, "좋아요 토글 완료"),
                    HttpStatus.OK);
        } catch (Exception e) {
            return new ResponseEntity<>(
                    DefaultRes.res(StatusCode.BAD_REQUEST, e.getMessage()),
                    HttpStatus.BAD_REQUEST);
        }
    }


    @DeleteMapping("/{id}")
    public ResponseEntity<DefaultRes<String>> deleteSingleClosetById(@PathVariable Long id) {
        try {
        closetService.deleteCloth(id);
        return new ResponseEntity<>(
                DefaultRes.res(StatusCode.OK, "삭제 완료"),
                HttpStatus.OK);

        } catch (Exception e) {
            return new ResponseEntity<>(
                    DefaultRes.res(StatusCode.BAD_REQUEST, e.getMessage()),
                    HttpStatus.BAD_REQUEST);
        }
    }

    @GetMapping("/major")
    public ResponseEntity<DefaultRes<List<ClosetDTO>>> retrieveByMajorCategory(@RequestParam MajorCategory major, @RequestHeader("Authorization") String accessToken) {
        try {
            String userid = jwtUtilsService.retrieveUserid(accessToken);
            Long userIndex = userService.retrieveUserIndexByUserid(userid);
            var closetDTOList = closetService.retrieveAllClosetDTOByUserIndexAndMajorCategory(userIndex, major);
            return new ResponseEntity<>(
                    DefaultRes.res(StatusCode.OK, "Cloth fetch 완료", closetDTOList),
                    HttpStatus.OK);
        } catch (Exception e) {
            return new ResponseEntity<>(
                    DefaultRes.res(StatusCode.BAD_REQUEST, e.getMessage()),
                    HttpStatus.BAD_REQUEST);
        }
    }

    @GetMapping("/middle")
    public ResponseEntity<DefaultRes<List<ClosetDTO>>> retrieveByMiddleCategory(@RequestParam MiddleCategory middle, @RequestHeader("Authorization") String accessToken) {
        try {
            String userid = jwtUtilsService.retrieveUserid(accessToken);
            Long userIndex = userService.retrieveUserIndexByUserid(userid);
            var closetDTOList = closetService.retrieveAllClosetDTOByUserIndexAndMiddleCategory(userIndex, middle);
            return new ResponseEntity<>(
                    DefaultRes.res(StatusCode.OK, "Cloth fetch 완료", closetDTOList),
                    HttpStatus.OK);
        } catch (Exception e) {
            return new ResponseEntity<>(
                    DefaultRes.res(StatusCode.BAD_REQUEST, e.getMessage()),
                    HttpStatus.BAD_REQUEST);
        }
    }

    @GetMapping("/like")
    public ResponseEntity<DefaultRes<List<ClosetDTO>>> retrieveByLike(@RequestHeader("Authorization") String accessToken) {
        try {
            String userid = jwtUtilsService.retrieveUserid(accessToken);
            var closetDTOList = closetService.getMyAllLikeClothList(userid);
            return new ResponseEntity<>(
                    DefaultRes.res(StatusCode.OK, "like Cloth fetch 완료", closetDTOList),
                    HttpStatus.OK);
        } catch (Exception e) {
            return new ResponseEntity<>(
                    DefaultRes.res(StatusCode.BAD_REQUEST, e.getMessage()),
                    HttpStatus.BAD_REQUEST);
        }
    }


}
