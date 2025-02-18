package com.example.U4_S7_L2_ese.config;

import com.cloudinary.Cloudinary;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.HashMap;
import java.util.Map;

@Configuration
public class CloudinaryConfig {

    @Bean
    public Cloudinary uploader(){
        Map<String, String> config = new HashMap<>();
        config.put("cloud_name", "doa6f01nr");
        config.put("api_key", "276879937193924");
        config.put("api_secret", "yDW_rrX0QJNKSnalyK1rqX5Dml0");
        return new Cloudinary(config);
    }
}
