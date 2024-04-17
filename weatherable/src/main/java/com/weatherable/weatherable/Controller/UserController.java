package com.weatherable.weatherable.Controller;

import com.weatherable.weatherable.DTO.UserDTO;
import com.weatherable.weatherable.DTO.UserForMyPageDTO;
import com.weatherable.weatherable.DTO.UserSizeDTO;
import com.weatherable.weatherable.Entity.UserEntity;
import com.weatherable.weatherable.Service.*;
import com.weatherable.weatherable.enums.DefaultRes;
import com.weatherable.weatherable.enums.StatusCode;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;

@RestController
@RequestMapping("/user")
public class UserController {

    @Autowired
    UserService userService;

    @Autowired
    S3Upload s3Upload;

    @Autowired
    JwtUtilsService jwtUtilsService;

    @Autowired
    ClosetService closetService;

    @Autowired
    CodiService codiService;


    @GetMapping("")
    public ResponseEntity<DefaultRes<UserForMyPageDTO>> getUserByUserid(@RequestHeader("Authorization") String accessToken){
        try {
            String userid = jwtUtilsService.retrieveUserid(accessToken);
            Long userIndex = userService.retrieveUserIndexByUserid(userid);
            UserForMyPageDTO result = userService.getUserInfoForMyPage(userid);
            var closetDTOList = closetService.getMyAllClothList(userid);
            var codiDTOList = codiService.retrieveMyCodi(userIndex);
            result.setClosetDTOList(closetDTOList);
            result.setCodiDTOList(codiDTOList);
            return new ResponseEntity<>(
                    DefaultRes.res(StatusCode.OK, "유저 데이터 fetch 완료", result),
                    HttpStatus.OK);
        } catch (Exception e) {
            return new ResponseEntity<>(
                    DefaultRes.res(StatusCode.BAD_REQUEST, e.getMessage()),
                    HttpStatus.BAD_REQUEST);
        }
    }

    @PatchMapping("/nickname")
    public ResponseEntity<DefaultRes<String>> updateUserNickname(@RequestBody UserDTO userDTO, @RequestHeader("Authorization") String accessToken) {
        try {
            String userid = jwtUtilsService.retrieveUserid(accessToken);
            Long userIndex = userService.retrieveUserIndexByUserid(userid);
            String result = userService.changeUserNickname(userDTO.getNickname(), userIndex);
            return new ResponseEntity<>(
                    DefaultRes.res(StatusCode.OK, result),
                    HttpStatus.OK);
        } catch (Exception e) {
            return new ResponseEntity<>(
                    DefaultRes.res(StatusCode.BAD_REQUEST, e.getMessage()),
                    HttpStatus.BAD_REQUEST);
        }
    }

    @PatchMapping("/physical")
    public ResponseEntity<DefaultRes<String>> updateUserHeightAndWeight(@RequestBody UserDTO userDTO, @RequestHeader("Authorization") String accessToken) {
        try {
            String userid = jwtUtilsService.retrieveUserid(accessToken);
            Long userIndex = userService.retrieveUserIndexByUserid(userid);
            String result = userService.changeUserHeightAndWeight(userDTO.getHeight(), userDTO.getWeight(), userIndex);
            return new ResponseEntity<>(
                    DefaultRes.res(StatusCode.OK, result),
                    HttpStatus.OK);
        } catch (Exception e) {
            return new ResponseEntity<>(
                    DefaultRes.res(StatusCode.BAD_REQUEST, e.getMessage()),
                    HttpStatus.BAD_REQUEST);
        }
    }

    @Value("${cloud.aws.default.imgPath}")
    private String defaultImgPath;

    @PatchMapping("/image")
    public ResponseEntity<DefaultRes<String>> updateUserImagePath(@RequestHeader("Authorization") String accessToken, @RequestParam("imageFile") MultipartFile imageFile) throws IOException {
        try {
            String userid = jwtUtilsService.retrieveUserid(accessToken);
            Long userIndex = userService.retrieveUserIndexByUserid(userid);
            String imagePath = s3Upload.saveImageFile(imageFile);
            String result = userService.changeUserImagePath(imagePath, userIndex);
            return new ResponseEntity<>(
                    DefaultRes.res(StatusCode.OK, result),
                    HttpStatus.OK);
        } catch (Exception e) {
            return new ResponseEntity<>(
                    DefaultRes.res(StatusCode.BAD_REQUEST, e.getMessage()),
                    HttpStatus.BAD_REQUEST);
        }
    }

    @PatchMapping("/image/initial")
    public ResponseEntity<DefaultRes<String>> updateUserImagePathToDefault(@RequestHeader("Authorization") String accessToken) {
        try {
            String userid = jwtUtilsService.retrieveUserid(accessToken);
            Long userIndex = userService.retrieveUserIndexByUserid(userid);
            String result = userService.changeUserImagePath(defaultImgPath, userIndex);
            return new ResponseEntity<>(
                    DefaultRes.res(StatusCode.OK, result),
                    HttpStatus.OK);
        } catch (Exception e) {
            return new ResponseEntity<>(
                    DefaultRes.res(StatusCode.BAD_REQUEST, e.getMessage()),
                    HttpStatus.BAD_REQUEST);
        }
    }

    @PatchMapping("/introduction")
    public ResponseEntity<DefaultRes<String>> updateUserIntroduction(@RequestBody UserDTO userDTO, @RequestHeader("Authorization") String accessToken) {
        try {
            String userid = jwtUtilsService.retrieveUserid(accessToken);
            Long userIndex = userService.retrieveUserIndexByUserid(userid);
            String result = userService.changeUserIntroduction(userDTO.getIntroduction(), userIndex);
            return new ResponseEntity<>(
                    DefaultRes.res(StatusCode.OK, result),
                    HttpStatus.OK);
        } catch (Exception e) {
            return new ResponseEntity<>(
                    DefaultRes.res(StatusCode.BAD_REQUEST, e.getMessage()),
                    HttpStatus.BAD_REQUEST);
        }
    }

    @PutMapping("/size")
    public ResponseEntity<DefaultRes<String>> putUserSize(@RequestBody UserSizeDTO userSizeDTO, @RequestHeader("Authorization") String accessToken) {
        try {
            String userid = jwtUtilsService.retrieveUserid(accessToken);
            Long userIndex = userService.retrieveUserIndexByUserid(userid);
            userService.insertUserSize(userIndex, userSizeDTO);
            return new ResponseEntity<>(
                    DefaultRes.res(StatusCode.OK, "사이즈 정보가 변경되었습니다."),
                    HttpStatus.OK);

        } catch (Exception e) {
            return new ResponseEntity<>(
                    DefaultRes.res(StatusCode.BAD_REQUEST, e.getMessage()),
                    HttpStatus.BAD_REQUEST);
        }
    }

    @PatchMapping("/password")
    public ResponseEntity<DefaultRes<String>> updateUserPassword(@RequestBody UserDTO userDTO, @RequestHeader("Authorization") String accessToken) throws Exception {
        try {
            String userid = jwtUtilsService.retrieveUserid(accessToken);
            Long userIndex = userService.retrieveUserIndexByUserid(userid);
            String insertedExistingPassword = userDTO.getExistingPassword();
            String existingPassword = userService.retrieveExistingPasswordById(userIndex);
            boolean checkExistingPassword = userService.matchPassword(insertedExistingPassword, existingPassword);
            if(!checkExistingPassword) {
                return new ResponseEntity<>(
                        DefaultRes.res(StatusCode.BAD_REQUEST, "현재 비밀번호가 다릅니다."),
                        HttpStatus.BAD_REQUEST);
            }
            boolean inputPasswordsAreEquals = userDTO.getPassword().equals(userDTO.getPasswordConfirm());
            if (!inputPasswordsAreEquals) {
                return new ResponseEntity<>(
                        DefaultRes.res(StatusCode.BAD_REQUEST, "비밀번호와 비밀번호 확인이 다릅니다."),
                        HttpStatus.BAD_REQUEST);
            }
            boolean arePasswordsEquals = userService.matchPassword(userDTO.getPassword(), existingPassword);
            if (arePasswordsEquals) {
                return new ResponseEntity<>(
                        DefaultRes.res(StatusCode.BAD_REQUEST, "이전 패스워드가 동일합니다."),
                        HttpStatus.BAD_REQUEST);
            }
            String result = userService.changeUserPassword(userDTO.getPassword(), userIndex);
            return new ResponseEntity<>(
                    DefaultRes.res(StatusCode.OK, result),
                    HttpStatus.OK);
        } catch (Exception e) {
            return new ResponseEntity<>(
                    DefaultRes.res(StatusCode.BAD_REQUEST, e.getMessage()),
                    HttpStatus.BAD_REQUEST);
        }
    }

    @PatchMapping("/style")
    public ResponseEntity<DefaultRes<String>> updateUserStyle(@RequestBody UserDTO userDTO, @RequestHeader("Authorization") String accessToken) {
        try {
            String userid = jwtUtilsService.retrieveUserid(accessToken);
            Long userIndex = userService.retrieveUserIndexByUserid(userid);
            String result = userService.changeUserStyle(userDTO.getFavoriteStyle(), userIndex);
            return new ResponseEntity<>(
                    DefaultRes.res(StatusCode.OK, result),
                    HttpStatus.OK);

        } catch (Exception e) {
            return new ResponseEntity<>(
                    DefaultRes.res(StatusCode.BAD_REQUEST, e.getMessage()),
                    HttpStatus.BAD_REQUEST);
        }
    }

    @DeleteMapping("")
    public ResponseEntity<DefaultRes<String>> deleteUserAccount(@RequestHeader("Authorization") String accessToken) {
        try {
            String userid = jwtUtilsService.retrieveUserid(accessToken);
            Long userIndex = userService.retrieveUserIndexByUserid(userid);
            String result = userService.deleteUserAccount(userIndex);
            return new ResponseEntity<>(
                    DefaultRes.res(StatusCode.OK, result),
                    HttpStatus.OK);
        } catch (Exception e) {
            return new ResponseEntity<>(
                    DefaultRes.res(StatusCode.BAD_REQUEST, e.getMessage()),
                    HttpStatus.BAD_REQUEST);
        }
    }
}
