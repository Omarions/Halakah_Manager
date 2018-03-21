package com.omarionapps.halaka.service;

import com.omarionapps.halaka.model.Country;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.format.Formatter;

import java.text.ParseException;
import java.util.Locale;

/**
 * Created by Omar on 21-Jul-17.
 */
public class CountryConverter implements Formatter<Country> {
    @Autowired
    private CountryService countryService;

    @Override
    public Country parse(String s, Locale locale) throws ParseException {
        if (null != s) {
            try {
                int id = Integer.valueOf(s);
                return countryService.findById(id);
            } catch (Exception e) {
                System.out.println(e.toString());
            }
        }

        return new Country();
    }

    @Override
    public String print(Country country, Locale locale) {
        return (country != null) ? String.valueOf(country.getId()) : "";
    }
}
