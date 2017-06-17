package com.omarionapps.halaka.com.omarionapps.halaka.config;

import com.omarionapps.halaka.service.TeacherConverter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.format.FormatterRegistry;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurerAdapter;

/**
 * Created by Omar on 22-Apr-17.
 */
@Configuration
public class WebMvcConfig extends WebMvcConfigurerAdapter {
    //Teacher Converter
    @Autowired
    private TeacherConverter teacherConverter;

    @Bean
    public BCryptPasswordEncoder passwordEncoder(){
        BCryptPasswordEncoder bCryptPasswordEncoder = new BCryptPasswordEncoder();
        return bCryptPasswordEncoder;
    }

    @Override
    public void addFormatters(FormatterRegistry registry) {
        registry.addFormatter(teacherConverter);
    }
}
