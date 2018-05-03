package com.omarionapps.halaka.com.omarionapps.halaka.config;

import com.omarionapps.halaka.service.TeacherConverter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.format.FormatterRegistry;
import org.springframework.security.crypto.factory.PasswordEncoderFactories;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

/**
 * Created by Omar on 22-Apr-17.
 */
@Configuration
public class WebMvcConfig implements WebMvcConfigurer {
    //Teacher Converter
    @Autowired
    private TeacherConverter teacherConverter;


    @Bean
    public PasswordEncoder passwordEncoder() {
        return PasswordEncoderFactories.createDelegatingPasswordEncoder();
    }

    @Override
    public void addFormatters(FormatterRegistry registry) {
        registry.addFormatter(teacherConverter);
    }
}