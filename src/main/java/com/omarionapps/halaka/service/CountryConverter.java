package com.omarionapps.halaka.service;

import com.omarionapps.halaka.model.Country;
import com.omarionapps.halaka.utils.Utils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.format.Formatter;

import java.util.Locale;

/**
 * Created by Omar on 21-Jul-17.
 */
public class CountryConverter implements Formatter<Country> {
    @Autowired
    private CountryService countryService;

    @Override
    public Country parse(String s, Locale locale) {
        if (null != s) {
            int id = Utils.convertToID(s);
            return countryService.findById(id);
        }

        return new Country();
    }

    @Override
    public String print(Country country, Locale locale) {
        return (country != null) ? String.valueOf(country.getId()) : "";
    }
}
