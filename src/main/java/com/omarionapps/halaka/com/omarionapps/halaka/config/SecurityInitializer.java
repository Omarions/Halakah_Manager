package com.omarionapps.halaka.com.omarionapps.halaka.config;

import org.springframework.security.access.SecurityConfig;
import org.springframework.security.web.context.AbstractSecurityWebApplicationInitializer;

/**
 * Created by Omar on 25-Apr-17.
 */
public class SecurityInitializer extends AbstractSecurityWebApplicationInitializer {
    public SecurityInitializer(){
        super(SecurityConfig.class, Config.class);
    }
}