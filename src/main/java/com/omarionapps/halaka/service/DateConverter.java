package com.omarionapps.halaka.service;

import org.springframework.format.Formatter;
import org.springframework.stereotype.Service;

import java.sql.Date;
import java.text.ParseException;
import java.util.Locale;

/**
 * Created by Omar on 04-Jun-17.
 */
@Service
public class DateConverter implements Formatter<Date> {
    @Override
    public Date parse(String s, Locale locale) throws ParseException {
        if(null == s){
            return Date.valueOf("2020-2-20");
        }
        return Date.valueOf(s);
    }

    @Override
    public String print(Date date, Locale locale) {
        if(null == date){
            return "";
        }
        return date.toString();
    }
}
