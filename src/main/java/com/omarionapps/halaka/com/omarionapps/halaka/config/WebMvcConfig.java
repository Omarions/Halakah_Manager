package com.omarionapps.halaka.com.omarionapps.halaka.config;

import com.omarionapps.halaka.service.TeacherConverter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.format.FormatterRegistry;
import org.springframework.http.MediaType;
import org.springframework.http.converter.ByteArrayHttpMessageConverter;
import org.springframework.http.converter.HttpMessageConverter;
import org.springframework.security.crypto.factory.PasswordEncoderFactories;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;
import org.springframework.web.servlet.resource.PathResourceResolver;

import java.util.ArrayList;
import java.util.List;

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

	@Override
	public void addResourceHandlers(ResourceHandlerRegistry registry) {
		registry
				.addResourceHandler("/admin/students/images/**")
				.addResourceLocations("file://C://halaka//resources//images//")
				.setCachePeriod(3600)
				.resourceChain(true)
				.addResolver(new PathResourceResolver());
	}

	@Override
	public void configureMessageConverters(List<HttpMessageConverter<?>> converters) {
		final ByteArrayHttpMessageConverter arrayHttpMessageConverter = new ByteArrayHttpMessageConverter();
		final List<MediaType>               list                      = new ArrayList<>();
		list.add(MediaType.IMAGE_JPEG);
		list.add(MediaType.APPLICATION_OCTET_STREAM);
		arrayHttpMessageConverter.setSupportedMediaTypes(list);
		converters.add(arrayHttpMessageConverter);

		WebMvcConfigurer.super.configureMessageConverters(converters);
	}
}