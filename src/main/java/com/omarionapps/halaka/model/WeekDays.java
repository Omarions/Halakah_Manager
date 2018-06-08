package com.omarionapps.halaka.model;

public enum WeekDays {
	السبت("السبت"), الأحد("الأحد"), الأثنين("الأثنين"), الثلاثاء("الثلاثاء"), الأربعاء("الأربعاء"),
	الخميس("الخميس"), الجمعة("الجمعة");
	private String day;

	WeekDays(String day) {
		this.day = day;
	}

	public String getDay() {
		return day;
	}
}
