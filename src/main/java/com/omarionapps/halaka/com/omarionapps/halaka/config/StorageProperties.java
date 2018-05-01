package com.omarionapps.halaka.com.omarionapps.halaka.config;

import org.springframework.boot.context.properties.ConfigurationProperties;

@ConfigurationProperties("com.omarionapps.halaka")
public class StorageProperties {

	/**
	 * Folder location for storing files
	 */
	private String location = "/lib/dist/img/";

	public String getLocation() {
		return location;
	}

	public void setLocation(String location) {
		this.location = location;
	}

}