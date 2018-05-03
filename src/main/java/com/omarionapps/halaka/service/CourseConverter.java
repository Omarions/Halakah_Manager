package com.omarionapps.halaka.service;

import com.omarionapps.halaka.model.Course;
import com.omarionapps.halaka.utils.Utils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.format.Formatter;

import java.util.Locale;

/**
 * Created by Omar on 08-Jul-17.
 */
public class CourseConverter implements Formatter<Course> {
	@Autowired
	private CourseService courseService;

	@Override
	public Course parse(String s, Locale locale) {
		if (null != s) {
			int    id     = Utils.convertToID(s);
			Course course = courseService.findById(id).get();
			if (null != course)
				return course;
		}


		return new Course();
	}

	@Override
	public String print(Course course, Locale locale) {
		return (course != null) ? String.valueOf(course.getId()) : "";
	}
}
