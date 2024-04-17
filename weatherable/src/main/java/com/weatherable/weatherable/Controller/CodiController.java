package com.weatherable.weatherable.Controller;

import com.weatherable.weatherable.DTO.ClosetDTO;
import com.weatherable.weatherable.DTO.CodiDTO;
import com.weatherable.weatherable.DTO.CodiDTOWithImage;
import com.weatherable.weatherable.DTO.CodiLikeDTO;
import com.weatherable.weatherable.Service.CodiLikeService;
import com.weatherable.weatherable.Service.CodiService;
import com.weatherable.weatherable.Service.JwtUtilsService;
import com.weatherable.weatherable.Service.UserService;
import com.weatherable.weatherable.enums.DefaultRes;
import com.weatherable.weatherable.enums.StatusCode;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.security.auth.login.AccountNotFoundException;
import java.sql.Time;
import java.sql.Timestamp;
import java.time.LocalDateTime;
import java.util.List;

@RestController
@RequestMapping("/codi")
public class CodiController {

    @Autowired
    CodiService codiService;

    @Autowired
    CodiLikeService codiLikeService;

    @Autowired
    UserService userService;

    @Autowired
    JwtUtilsService jwtUtilsService;


    @DeleteMapping("")
    public ResponseEntity<DefaultRes<String>> deleteCodi(@RequestBody CodiDTO codiDTO) {
        try {
            Long id = codiDTO.getId();
            codiService.deleteCodi(id);
            return new ResponseEntity<>(
                    DefaultRes.res(StatusCode.OK, "삭제 완료"),
                    HttpStatus.OK);
        } catch (Exception e) {
            return new ResponseEntity<>(
                    DefaultRes.res(StatusCode.BAD_REQUEST, e.getMessage()),
                    HttpStatus.BAD_REQUEST);
        }
    }

    @GetMapping("")
    public ResponseEntity<DefaultRes<List<CodiDTOWithImage>>> retrieveAllCodi(@RequestHeader("Authorization") String accessToken) {
        try {
            String userid = jwtUtilsService.retrieveUserid(accessToken);
            Long userIndex = userService.retrieveUserIndexByUserid(userid);
            List<CodiDTOWithImage> codiDTOList = codiService.retrieveAllCodi(userIndex);
            return new ResponseEntity<>(
                    DefaultRes.res(StatusCode.OK, "Codi Fetch 완료", codiDTOList),
                    HttpStatus.OK);
        } catch (Exception e) {
            return new ResponseEntity<>(
                    DefaultRes.res(StatusCode.BAD_REQUEST, e.getMessage()),
                    HttpStatus.BAD_REQUEST);
        }
    }

    @GetMapping("/like")
    public ResponseEntity<DefaultRes<List<CodiDTOWithImage>>> retrieveAllMyLikeCodi(@RequestHeader("Authorization") String accessToken) {
        try {
            String userid = jwtUtilsService.retrieveUserid(accessToken);
            Long userIndex = userService.retrieveUserIndexByUserid(userid);
            List<CodiDTOWithImage> codiDTOList = codiService.retrieveMyLikeCodi(userIndex);
            return new ResponseEntity<>(
                    DefaultRes.res(StatusCode.OK, "Like Codi Fetch 완료", codiDTOList),
                    HttpStatus.OK);
        } catch (Exception e) {
            return new ResponseEntity<>(
                    DefaultRes.res(StatusCode.BAD_REQUEST, e.getMessage()),
                    HttpStatus.BAD_REQUEST);
        }
    }

    @GetMapping("/user/{id}")
    public ResponseEntity<DefaultRes<List<CodiDTOWithImage>>> retrieveAllSomeonesCodi(@PathVariable Long id, @RequestHeader("Authorization") String accessToken) {
        try {
            String userid = jwtUtilsService.retrieveUserid(accessToken);
            Long userIndex = userService.retrieveUserIndexByUserid(userid);
            List<CodiDTOWithImage> codiDTOList = codiService.retrieveSomeOnesCodi(id, userIndex);
            return new ResponseEntity<>(
                    DefaultRes.res(StatusCode.OK, "Codi Fetch 완료", codiDTOList),
                    HttpStatus.OK);
        } catch (Exception e) {
            return new ResponseEntity<>(
                    DefaultRes.res(StatusCode.BAD_REQUEST, e.getMessage()),
                    HttpStatus.BAD_REQUEST);
        }
    }

    @PostMapping("")
    public ResponseEntity<DefaultRes<String>> insertCodi(@RequestBody CodiDTO codiDTO, @RequestHeader("Authorization") String accessToken) {
        try {
            String userid = jwtUtilsService.retrieveUserid(accessToken);
            codiDTO.setUserid(userid);
            codiService.createCodi(codiDTO);
            return new ResponseEntity<>(
                    DefaultRes.res(StatusCode.CREATED, "Codi 등록 완료"),
                    HttpStatus.CREATED);
        } catch (Exception e) {
            return new ResponseEntity<>(
                    DefaultRes.res(StatusCode.BAD_REQUEST, e.getMessage()),
                    HttpStatus.BAD_REQUEST);
        }
    }

    @PostMapping("/like")
    public ResponseEntity<DefaultRes<String>> toggleLike(@RequestBody CodiLikeDTO codiLikeDTO, @RequestHeader("Authorization") String accessToken) {
        try {
            String userid = jwtUtilsService.retrieveUserid(accessToken);
            Long userIndex = userService.retrieveUserIndexByUserid(userid);
            codiLikeDTO.setUser_id(userIndex);
            codiLikeService.likeToggle(codiLikeDTO);
            return new ResponseEntity<>(
                    DefaultRes.res(StatusCode.OK, "Codi 좋아요 완료"),
                    HttpStatus.OK);
        } catch (Exception e) {
            return new ResponseEntity<>(
                    DefaultRes.res(StatusCode.BAD_REQUEST, e.getMessage()),
                    HttpStatus.BAD_REQUEST);
        }
    }

    @PutMapping("")
    public ResponseEntity<DefaultRes<String>> updateCodi(CodiDTO codiDTO, @RequestHeader("Authorization") String accessToken) {
        try {
            String userid = jwtUtilsService.retrieveUserid(accessToken);
            codiDTO.setUserid(userid);
            codiService.updateCodi(codiDTO);
            return new ResponseEntity<>(
                    DefaultRes.res(StatusCode.OK, "Codi 수정 완료"),
                    HttpStatus.OK);
        } catch (Exception e) {
            return new ResponseEntity<>(
                    DefaultRes.res(StatusCode.BAD_REQUEST, e.getMessage()),
                    HttpStatus.BAD_REQUEST);
        }
    }

    @GetMapping("/{id}")
    public ResponseEntity<DefaultRes<CodiDTOWithImage>> retrieveSingleCodi(@PathVariable Long id, @RequestHeader("Authorization") String accessToken) {
        try {
            String userid = jwtUtilsService.retrieveUserid(accessToken);
            Long userIndex = userService.retrieveUserIndexByUserid(userid);
            CodiDTOWithImage codiDTO = codiService.retrieveSingleCodi(id, userIndex);
            return new ResponseEntity<>(
                    DefaultRes.res(StatusCode.OK, "Codi fetch 완료", codiDTO),
                    HttpStatus.OK);
        } catch (Exception e) {
            return new ResponseEntity<>(
                    DefaultRes.res(StatusCode.BAD_REQUEST, e.getMessage()),
                    HttpStatus.BAD_REQUEST);
        }
    }

    @GetMapping("/mycodi")
    public ResponseEntity<DefaultRes<List<CodiDTOWithImage>>> retrieveMyCodi(@RequestHeader("Authorization") String accessToken) {
        try {
            String userid = jwtUtilsService.retrieveUserid(accessToken);
            Long userIndex = userService.retrieveUserIndexByUserid(userid);
            var codiDTOWithImageList = codiService.retrieveMyCodi(userIndex);
            return new ResponseEntity<>(
                    DefaultRes.res(StatusCode.OK, "Codi fetch 완료", codiDTOWithImageList),
                    HttpStatus.OK);
        } catch (Exception e) {
            return new ResponseEntity<>(
                    DefaultRes.res(StatusCode.BAD_REQUEST, e.getMessage()),
                    HttpStatus.BAD_REQUEST);
        }
    }

    @GetMapping("/date")
    public ResponseEntity<DefaultRes<List<CodiDTOWithImage>>> retrieveCodiListWithDate(@RequestHeader("Authorization") String accessToken, @RequestParam LocalDateTime selectedDate) {
        try {
            Timestamp timestamp = Timestamp.valueOf(selectedDate);
            String userid = jwtUtilsService.retrieveUserid(accessToken);
            Long userIndex = userService.retrieveUserIndexByUserid(userid);
            var codiDTOList = codiService.retrieveByUser_idAndCodiDate(userIndex, timestamp);
            return new ResponseEntity<>(
                    DefaultRes.res(StatusCode.OK, "Codi fetch 완료", codiDTOList),
                    HttpStatus.OK);
        } catch (Exception e) {
            return new ResponseEntity<>(
                    DefaultRes.res(StatusCode.BAD_REQUEST, e.getMessage()),
                    HttpStatus.BAD_REQUEST);
        }
    }


}
