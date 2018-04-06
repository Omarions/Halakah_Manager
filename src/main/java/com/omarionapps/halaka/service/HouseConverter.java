package com.omarionapps.halaka.service;

import com.omarionapps.halaka.model.House;
import com.omarionapps.halaka.utils.Utils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.format.Formatter;
import org.springframework.stereotype.Service;

import java.util.Locale;

/**
 * Created by Omar on 02-Jun-17.
 */
@Service
public class HouseConverter implements Formatter<House> {
	@Autowired
	HouseService houseService;

	@Override
	public House parse(String s, Locale locale) {
		if (null != s) {
			int id = Utils.convertToID(s);
			return houseService.findById(id);
		}

		return new House();
	}

	@Override
	public String print(House house, Locale locale) {
		return (house != null) ? String.valueOf(house.getId()) : "";
	}
}
