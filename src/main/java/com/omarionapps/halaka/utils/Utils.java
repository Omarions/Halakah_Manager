package com.omarionapps.halaka.utils;

import com.omarionapps.halaka.controller.PhotoController;
import org.springframework.web.servlet.mvc.method.annotation.MvcUriComponentsBuilder;

import java.time.LocalDate;
import java.time.ZoneId;
import java.util.Date;

public class Utils {
	public static int convertToID(String name) {
		int id = -1;
		try {
			id = Integer.parseInt(String.valueOf(name));
		} catch (NumberFormatException e) {
			//System.out.println(e.toString());
		}
		return id;
	}

	public static String buildPhotoUrl(String photoName, LocationTag locationTag) {
		String photoUrl = MvcUriComponentsBuilder
				                  .fromMethodName(PhotoController.class, "getFile", photoName, locationTag)
				                  .build()
				                  .toString();

		return photoUrl;

	}

	public static Date convertLocalDate(LocalDate localDate) {
		Date date = Date.from(localDate.atStartOfDay(ZoneId.systemDefault()).toInstant());

		return date;
	}
}
