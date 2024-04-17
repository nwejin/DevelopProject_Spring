package com.weatherable.weatherable.Controller;

import com.weatherable.weatherable.DTO.UserDTO;
import com.weatherable.weatherable.DTO.UserForMyPageDTO;
import com.weatherable.weatherable.Entity.UserEntity;
import com.weatherable.weatherable.Service.AuthService;
import com.weatherable.weatherable.Service.CustomUserDetailsService;
import com.weatherable.weatherable.Service.UserService;
import com.weatherable.weatherable.enums.DefaultRes;
import com.weatherable.weatherable.enums.StatusCode;
import io.jsonwebtoken.security.Keys;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.http.HttpStatusCode;
import org.springframework.http.ResponseEntity;
import org.springframework.security.oauth2.jwt.JwtClaimsSet;
import org.springframework.security.oauth2.jwt.JwtEncoder;
import org.springframework.security.oauth2.jwt.JwtEncoderParameters;
import org.springframework.web.bind.annotation.*;
import com.weatherable.weatherable.Service.JwtUtilsService;

import javax.crypto.SecretKey;
import java.time.Instant;
import java.util.List;

@RestController
public class AuthController {

    @Autowired
    UserService userService;

    @Autowired
    AuthService authService;

    @Autowired
    JwtUtilsService jwtUtilsService;

    private JwtEncoder jwtEncoder;


    @Autowired
    public AuthController(JwtEncoder jwtEncoder) {

        this.jwtEncoder = jwtEncoder;
    }


    @PostMapping("/signup")
    public ResponseEntity<DefaultRes<String>> insertUser(@RequestBody UserDTO userDTO) throws Exception {
        try {

            String result = userService.insertUser(userDTO);

            return new ResponseEntity<>(DefaultRes.res(StatusCode.CREATED, result), HttpStatus.CREATED);

        } catch (Exception e) {
            return new ResponseEntity<>(
                    DefaultRes.res(StatusCode.BAD_REQUEST, e.getMessage()),
                    HttpStatus.BAD_REQUEST);
        }
    }

    @PostMapping("/validation")
    public ResponseEntity<DefaultRes<Boolean>> useridValidation(@RequestBody UserDTO userDTO) {
        try {
            boolean result = userService.checkIdValidation(userDTO);
            boolean lengthVali = userDTO.getUserid().length() >= 4;
            if (result && lengthVali) {
                return new ResponseEntity<>(
                        DefaultRes.res(StatusCode.OK, "validation Info", userService.checkIdValidation(userDTO)),
                        HttpStatus.OK);
            }
            return new ResponseEntity<>(
                    DefaultRes.res(StatusCode.BAD_REQUEST, "이미 존재하는 아이디이거나 비밀번호가 4자 이상이 아닙니다.", userService.checkIdValidation(userDTO)),
                    HttpStatus.BAD_REQUEST);
        } catch (Exception e) {
            return new ResponseEntity<>(
                    DefaultRes.res(StatusCode.BAD_REQUEST, e.getMessage()),
                    HttpStatus.BAD_REQUEST);
        }
    }





    @PostMapping("/login")
    public ResponseEntity<DefaultRes<List<String>>> authenticate(@RequestBody UserDTO userDTO) throws Exception {
        try {
            String userid = userDTO.getUserid();
            String password = userDTO.getPassword();
            boolean result = userService.isLoginInfoEqual(userid, password);
            if (!result) {
                throw new Exception("아이디 혹은 비밀번호가 다릅니다.");
            }
            UserForMyPageDTO existingUserDTO = userService.getUserInfoForMyPage(userid);
            Long id = existingUserDTO.getId();
            String refreshToken = jwtUtilsService.createRefreshToken(userid);
            String accessToken = jwtUtilsService.createAccessToken(userid);

            authService.updateUserRefreshToken(refreshToken, id);

            return new ResponseEntity<>(
                    DefaultRes.res(StatusCode.OK, "0: refresh token, 1: access token", List.of(refreshToken, accessToken, userid)),
                    HttpStatus.OK);
        } catch (Exception e) {
            return new ResponseEntity<>(
                    DefaultRes.res(StatusCode.BAD_REQUEST, e.getMessage()),
                    HttpStatus.BAD_REQUEST);
        }
    }

    @Autowired
    CustomUserDetailsService customUserDetailsService;

    @PostMapping("/refresh")
    public ResponseEntity<DefaultRes<String>> getRefresh(@RequestHeader("Authorization") String refreshToken) {
        try {
        boolean result = jwtUtilsService.validateToken(refreshToken);
        if (!result) {
            throw new RuntimeException("Refresh Token Is Not Valid!");
        }
        String userid = jwtUtilsService.retrieveUserid(refreshToken);
        return new ResponseEntity<>(
                DefaultRes.res(StatusCode.OK, "access token", jwtUtilsService.createAccessToken(userid)),
                HttpStatus.OK);
        }catch (Exception e) {
            return new ResponseEntity<>(
                    DefaultRes.res(StatusCode.BAD_REQUEST, e.getMessage()),
                    HttpStatus.BAD_REQUEST);
        }
    }


    public String createAccessToken(String userid) {
        // claims 생성
        var claims = JwtClaimsSet.builder()
                .issuer("self")
                .issuedAt(Instant.now())
                .expiresAt(Instant.now().plusSeconds(60 * 15))
                .subject(userid)
                .claim("scope", "ROLE_USER") // authority
                .build();

        // jwtEncoderParameters 생성하고 이걸 인코딩하고 TokenValue 추출해서 return
        JwtEncoderParameters parameters = JwtEncoderParameters.from(claims);
        return jwtEncoder.encode(parameters).getTokenValue();
    }

}

