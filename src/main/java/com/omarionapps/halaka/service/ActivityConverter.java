package com.omarionapps.halaka.service;

import com.omarionapps.halaka.model.Activity;
import com.omarionapps.halaka.model.Teacher;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.format.Formatter;
import org.springframework.stereotype.Service;

import java.text.ParseException;
import java.util.Locale;

/**
 * Created by Omar on 02-Jun-17.
 */
@Service
public class ActivityConverter implements Formatter<Activity> {
    @Autowired
    ActivityService activityService;
    @Override
    public Activity parse(String s, Locale locale) throws ParseException {
        if(null != s){
            try {
                int id = Integer.valueOf(s);
                return activityService.findById(id);
            }catch (Exception e){
                System.out.println("Exception occurred in Activity Converter");
                System.out.println(e.toString());
            }
        }

        return new Activity();
    }

    @Override
    public String print(Activity activity, Locale locale) {
        return (activity != null)? String.valueOf(activity.getId()) : "";
    }
}
