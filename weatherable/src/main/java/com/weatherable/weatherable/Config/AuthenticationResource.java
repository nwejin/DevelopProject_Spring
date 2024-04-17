package com.weatherable.weatherable.Config;

import com.weatherable.weatherable.Service.CustomUserDetailsService;
import com.weatherable.weatherable.Service.JwtUtilsService;
import com.weatherable.weatherable.utils.JwtAccessDeniedHandler;
import com.weatherable.weatherable.utils.JwtAuthenticationEntryPoint;
import com.weatherable.weatherable.utils.JwtRequestFilter;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.config.annotation.web.configurers.HeadersConfigurer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.CorsConfigurationSource;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;

import java.util.List;


@Configuration
@EnableWebSecurity
@RequiredArgsConstructor
public class AuthenticationResource {

    @Value("${python.data.uri}")
    String pythonURI;

    @Value("${front.data.uri}")
    String frontURI;

    private final JwtRequestFilter jwtRequestFilter;

    private final JwtAuthenticationEntryPoint jwtAuthenticationEntryPoint;

    private final JwtAccessDeniedHandler jwtAccessDeniedHandler;

    @Bean
    SecurityFilterChain SecurityFilterChain(HttpSecurity http) throws Exception {

        // HTTP 요청에 대한 권한 부여 적용

        http.authorizeHttpRequests((requests) ->
                requests
                        .requestMatchers("/login", "/signup", "/refresh", "/validation").permitAll()
                        .requestMatchers("/**").hasRole("USER")
                        .anyRequest().authenticated());

        // HTTP 세션에 사용할 정책을 STATELESS로 설정하기 (REST API에서 설정해야 함.)
        // 스프링 부트 기본 옵션에서는 세션을 이용해서 로그인 로그아웃을 설정함.
        http.sessionManagement(session -> session.sessionCreationPolicy(SessionCreationPolicy.STATELESS));
        http.httpBasic(AbstractHttpConfigurer::disable);
        http.cors(httpSecurityCorsConfigurer -> httpSecurityCorsConfigurer.configurationSource(corsConfigurationSource()));
        // csrf 사용 해제
        http.csrf(AbstractHttpConfigurer::disable);
        http.headers(headersConfigurer -> headersConfigurer.frameOptions(HeadersConfigurer.FrameOptionsConfig::sameOrigin));
        http.addFilterBefore(jwtRequestFilter, UsernamePasswordAuthenticationFilter.class);
        http.exceptionHandling((exceptionConfig) ->
                exceptionConfig.accessDeniedHandler(jwtAccessDeniedHandler).authenticationEntryPoint(jwtAuthenticationEntryPoint)
        );
        return http.build();
    }

    @Bean
    public CorsConfigurationSource corsConfigurationSource() {
        CorsConfiguration corsConfiguration = new CorsConfiguration();
        //Make the below setting as * to allow connection from any hos
        corsConfiguration.setAllowedOrigins(List.of("http://3.36.128.237:9000", "http://localhost:9000", "http://3.36.128.237:3000", "http://localhost:3000",
                "https://3.36.128.237:9000", "https://localhost:9000", "https://3.36.128.237:3000", "https://localhost:3000", pythonURI, frontURI
        ));
        corsConfiguration.setAllowedMethods(List.of("*"));
        corsConfiguration.setAllowCredentials(true);
        corsConfiguration.setAllowedHeaders(List.of("*"));
        corsConfiguration.setMaxAge(3600L);
        UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
        source.registerCorsConfiguration("/**", corsConfiguration);
        return source;
    }
}
