package com.omarionapps.halaka;

import com.omarionapps.halaka.service.StorageService;
import org.springframework.boot.context.event.ApplicationReadyEvent;
import org.springframework.context.ApplicationListener;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;

@Component
public class ApplicationStartup implements ApplicationListener<ApplicationReadyEvent> {

	@Resource
	StorageService storageService;

	@Override
	public void onApplicationEvent(ApplicationReadyEvent applicationReadyEvent) {
		//Put the code you want to excute first at application startup
		//	PasswordEncoder passwordEncoder = PasswordEncoderFactories.createDelegatingPasswordEncoder();
		//	System.out.println("Encrypted Password: "  + passwordEncoder.encode("omar123"));
		//	System.out.println("Encrypted Password: "  + passwordEncoder.encode("adnan123"));
		//	System.out.println("Encrypted Password: "  + passwordEncoder.encode("ismail123"));

		storageService.init();
		return;
	}
}
