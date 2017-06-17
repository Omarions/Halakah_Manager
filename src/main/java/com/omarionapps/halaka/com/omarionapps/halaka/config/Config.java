package com.omarionapps.halaka.com.omarionapps.halaka.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.redis.connection.lettuce.LettuceConnectionFactory;
import org.springframework.session.data.redis.config.annotation.web.http.EnableRedisHttpSession;

/**
 * Created by Omar on 25-Apr-17.
 */
@Configuration
@EnableRedisHttpSession
public class Config {
    @Bean
    public LettuceConnectionFactory connectionFactory(){
        LettuceConnectionFactory lcf = new LettuceConnectionFactory();

        return lcf;
    }
}
