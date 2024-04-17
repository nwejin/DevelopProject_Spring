package com.weatherable.weatherable.utils;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.weatherable.weatherable.Service.CustomUserDetailsService;
import com.weatherable.weatherable.Service.JwtUtilsService;
import com.weatherable.weatherable.enums.DefaultRes;
import io.jsonwebtoken.ExpiredJwtException;
import io.jsonwebtoken.JwtException;
import io.jsonwebtoken.MalformedJwtException;
import io.jsonwebtoken.UnsupportedJwtException;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;
import java.security.SignatureException;

@RequiredArgsConstructor
@Component
public class JwtRequestFilter extends OncePerRequestFilter {

    private final ObjectMapper objectMapper;
    private final CustomUserDetailsService customUserDetailsService;
    private final JwtUtilsService jwtUtilsService;


    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {
        final String accessToken = request.getHeader("Authorization");
        String username = null;
        try {
            if (accessToken != null) {
                username = jwtUtilsService.getUsername(accessToken);
                String issuer = jwtUtilsService.getIssuer(accessToken);
                if (!issuer.equals("access")) {
                    // 리프레시 토큰일 경우
                    if(issuer.equals("refresh")) {
                        try {
                            boolean result = jwtUtilsService.validateToken(accessToken);
                            if (!result) {
                                throw new RuntimeException("Refresh Token Is Not Valid!");
                            }
                            String userid = jwtUtilsService.retrieveUserid(accessToken);
                            sendErrorResponse(response, jwtUtilsService.createAccessToken(userid));
                            return;
                        }catch (Exception e) {
                            sendErrorResponse(response, "refresh 토큰이 만료되었습니다.");
                            return;
                        }
                    }
                    sendErrorResponse(response, "액세스 토큰이 아닙니다.");
                    return;
                }
            }
            if (username != null) {
                UserDetails userDetails = customUserDetailsService.loadUserByUsername(username);

                if (jwtUtilsService.validateAccessToken(accessToken, userDetails)) {
                    // 사용자 인증정보 담을 토큰 생성
                    UsernamePasswordAuthenticationToken usernamePasswordAuthenticationToken = new UsernamePasswordAuthenticationToken(userDetails, null, userDetails.getAuthorities());
                    // 사용자 인증 세부 설정
                    usernamePasswordAuthenticationToken.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));
                    // securityContext는 현재 스레드의 보안 정보를 저장하는 역할을 함
                    // SecurityContextHolder.getContext()로 SecurityContext에 접근하고 Authentication에 접근하여 방금 만든 토큰을 넣음
                    SecurityContextHolder.getContext().setAuthentication(usernamePasswordAuthenticationToken);
                    // SecurityContext는 해당 요청에서만 유지되고, 해당 요청의 다른 로직에서 Authentication 객체가 필요할 때 사용되다가
                    // 클라이언트의 요청을 모두 처리하고 응답을 리턴하는 어느 시점에 더이상 Authentication 객체가 필요 없을 때 자동으로 삭제됨
                }

            }
            filterChain.doFilter(request, response);
        } catch (MalformedJwtException e) {
            sendErrorResponse(response, "손상된 토큰");
        } catch (ExpiredJwtException e) {
            sendErrorResponse(response, "만료된 토큰");
        } catch (UnsupportedJwtException e) {
            sendErrorResponse(response, "지원되지 않는 토큰");
        } catch (JwtException e) {
            sendErrorResponse(response, e.getMessage());
        }

    }

    private void sendErrorResponse(HttpServletResponse response, String message) throws IOException {
        response.setCharacterEncoding("utf-8");
        response.setStatus(HttpStatus.UNAUTHORIZED.value());
        response.setContentType(MediaType.APPLICATION_JSON_VALUE);
        response.getWriter().write(objectMapper.writeValueAsString(DefaultRes.res(HttpStatus.UNAUTHORIZED.value(), message)));
    }

}

