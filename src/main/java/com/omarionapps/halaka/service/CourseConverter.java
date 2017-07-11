package com.omarionapps.halaka.service;

import com.omarionapps.halaka.model.Course;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.format.Formatter;

import java.text.ParseException;
import java.util.Locale;

/**
 * Created by Omar on 08-Jul-17.
 */
public class CourseConverter implements Formatter<Course> {
    @Autowired
    private CourseService courseService;

    @Override
    public Course parse(String s, Locale locale) throws ParseException {
        if (null != s) {
            try {
                int id = Integer.valueOf(s);
                Course course = courseService.findById(id);
                if (null != course)
                    return course;
            } catch (NumberFormatException ex) {
                System.out.println("Exception occurred in Course Converter");
                System.out.println(ex.toString());
            }
        }


        return new Course();
    }

    @Override
    public String print(Course course, Locale locale) {
        return (course != null) ? String.valueOf(course.getId()) : "";
    }
}
