package kr.unlike.redis.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

import com.fasterxml.jackson.databind.ObjectMapper;

@Configuration
public class WebMvcConfig implements WebMvcConfigurer {


    @Bean
    public ObjectMapper jsonMapper() {
        ObjectMapper jsonMapper = new ObjectMapper();
        return jsonMapper;
    }
}
