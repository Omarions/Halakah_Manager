package com.omarionapps.halaka.utils;

public class Utils {
	public static int convertToID(String name) {
		int id = -1;
		try {
			id = Integer.parseInt(String.valueOf(name));
		} catch (NumberFormatException e) {
			System.out.println(e.toString());
		}
		return id;
	}
}
