package com.omarionapps.halaka.service;

import com.omarionapps.halaka.model.Teacher;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.format.Formatter;
import org.springframework.stereotype.Service;

import java.util.Locale;

/**
 * Created by Omar on 02-Jun-17.
 */
@Service
public class TeacherConverter implements Formatter<Teacher> {
    @Autowired
    TeacherService teacherService;
    @Override
    public Teacher parse(String s, Locale locale) {
        if(null != s){
            try {
                int id = Integer.valueOf(s);
	            return teacherService.findOneById(id).get();
            }catch (Exception e){
	            //System.out.println("Exception occurred in Teacher Converter");
	            //System.out.println(e.toString());
            }
        }

        return new Teacher();
    }

    @Override
    public String print(Teacher teacher, Locale locale) {
        return (teacher != null)? String.valueOf(teacher.getId()) : "";
    }
}
