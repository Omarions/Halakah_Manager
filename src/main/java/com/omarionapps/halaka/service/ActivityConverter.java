package com.omarionapps.halaka.service;

import com.omarionapps.halaka.model.Activity;
import com.omarionapps.halaka.utils.Utils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.format.Formatter;
import org.springframework.stereotype.Service;

import java.util.Locale;

/**
 * Created by Omar on 02-Jun-17.
 */
@Service
public class ActivityConverter implements Formatter<Activity> {
    @Autowired
    ActivityService activityService;

    @Override
    public Activity parse(String s, Locale locale) {
        if(null != s){
            int id = Utils.convertToID(s);
	        return activityService.findById(id).get();
        }

        return new Activity();
    }

    @Override
    public String print(Activity activity, Locale locale) {
        return (activity != null)? String.valueOf(activity.getId()) : "";
    }
}
