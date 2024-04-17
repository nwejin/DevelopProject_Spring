package com.weatherable.weatherable.Service;

import com.weatherable.weatherable.DTO.AuthDTO;
import com.weatherable.weatherable.Repository.AuthRepository;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.security.Keys;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Service;

import javax.crypto.SecretKey;
import java.security.KeyPair;
import java.sql.Date;
import java.time.Instant;

@Component
public class JwtUtilsService {

    private final KeyPair keyPair; // RSA key pair
    @Autowired
    AuthRepository authRepository;

    @Autowired
    public JwtUtilsService(KeyPair keyPair) {
        this.keyPair = keyPair;
    }

    // 실제론 이 키 사용, 서버 시작하면 서명이 달라지기 때문에 하드코딩 사용
//    SecretKey key = Keys
//            .secretKeyFor(SignatureAlgorithm.HS256);

    @Value("${jwt.password.secretKey}")
    private String secretKey;



    // 토큰에서 클레임을 추출하는 함수
    public Claims extractAllClaims(String token) {
        token = token.substring(7);
        SecretKey key = Keys.hmacShaKeyFor(secretKey.getBytes());
        var jwtSubject = Jwts.parserBuilder().setSigningKey(key).build();
        var parseClaims = jwtSubject.parseClaimsJws(token).getBody();
        return parseClaims;
    }

    public String getUsername(String token) {
        return extractAllClaims(token).getSubject();
    }

    public String getIssuer(String token) {
        return extractAllClaims(token).getIssuer();
    }


    public String createRefreshToken(String userid) {
        SecretKey key = Keys.hmacShaKeyFor(secretKey.getBytes());
        // claims 생성
        var claims = Jwts.builder()
                .setIssuer("refresh")
                .setIssuedAt(Date.from(Instant.now()))
                .setExpiration(Date.from(Instant.now().plusSeconds(60*60*24*14)))
                .setSubject(userid)
                .claim("scope", "Refresh") // authority
                .signWith(key)
                .compact();

        return claims;
    }

    public String createAccessToken(String userid) {
        SecretKey key = Keys.hmacShaKeyFor(secretKey.getBytes());
        // claims 생성
        var claims = Jwts.builder()
                .setIssuer("access")
                .setIssuedAt(Date.from(Instant.now()))
                .setExpiration(Date.from(Instant.now().plusSeconds(60*60*24*14)))
                .setSubject(userid)
                .claim("scope", "ROLE_USER") // authority
                .signWith(key)
                .compact();

        return claims;
    }

    public boolean validateToken(String token) {
        var claims = extractAllClaims(token);
        String userid = claims.getSubject();
        String existingRefreshToken = getExistingRefreshToken(userid);
        boolean isValidToken = userid.equals(extractAllClaims("Bearer " + existingRefreshToken).getSubject());
        boolean isExpired = isTokenExpired(token);

        return !isExpired && isValidToken;
    }

    public boolean validateAccessToken(String token, UserDetails userDetails) {
        final String username = getUsername(token);

        return (username.equals(userDetails.getUsername()) && !isTokenExpired(token));
    }

    public String getExistingRefreshToken(String userid) {
        var userEntityOptional = authRepository.findByUserEntityUserid(userid);
        if (userEntityOptional.isEmpty()) {
            return "유저 없음";
        }

        return userEntityOptional.get().getRefreshToken();
    }

    public boolean isTokenExpired(String token) {
        var claims = extractAllClaims(token);
        var expiration = claims.getExpiration();
        return expiration.before(Date.from(Instant.now()));
    }

    public String retrieveUserid(String token) {
        var claims = extractAllClaims(token);
        return claims.getSubject();
    }

}
