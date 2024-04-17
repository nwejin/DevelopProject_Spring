package com.weatherable.weatherable.Config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Configuration
public class WebMvcConfig implements WebMvcConfigurer {

    @Value("${python.data.uri}")
    String pythonURI;

    @Value("${front.data.uri}")
    String frontURI;

    @Override
    public void addCorsMappings(CorsRegistry registry) {
        registry.addMapping("/**")
                .allowedOrigins("https://3.36.128.237", "http://localhost:9000", "http://localhost:3000",
                         pythonURI, frontURI)
                .allowedMethods("GET","POST","PUT","PATCH","DELETE")
                .allowedHeaders("*")
                .allowCredentials(true);
    }
}
