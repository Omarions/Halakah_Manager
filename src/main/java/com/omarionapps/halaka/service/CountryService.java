package com.omarionapps.halaka.service;

import com.omarionapps.halaka.model.Activity;
import com.omarionapps.halaka.model.Country;
import com.omarionapps.halaka.model.Student;
import com.omarionapps.halaka.repository.CountryRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.*;

/**
 * Created by Omar on 08-May-17.
 */
@Service
public class CountryService {

    private CountryRepository countryRepository;

    @Autowired
    public CountryService(CountryRepository countryRepository){
        this.countryRepository = countryRepository;
    }

    public Iterable<Country> getAll(){
        return countryRepository.findAll();
    }

    public Map<String, Integer> getAllCountryCodeStudentsCountMap(){
        Iterable<Country> list = this.getAll();
        Map<String, Integer> map = new HashMap<>();

        list.forEach((country) -> {
            if(country.getStudents().size() > 0)
                map.put(country.getCode(), country.getStudents().size());
        });
        System.out.println("CountryCode Map" + map);
        return map;
    }

    public Map<String, Integer> getCountryCodeStudentsCountMapFromStudetns(Set<Student> students){
        Map<String, Integer> map = new HashMap<>();
        students.stream().forEach((student -> {
            String code = student.getCountry().getCode();
            if(map.containsKey(code)){
                int count = map.get(code);
                map.put(code, ++count);
            }else{
                map.put(code, 1);
            }

        }));
        return map;
    }

    /*
        Get activity set of students that belong to a country (by its code)
     */
    public Map<String, Set<Student>> getCountryCodeStudentsMap(Set<Student> students){
        Map<String, Set<Student>> map = new HashMap<>();
        students.stream().forEach((student) -> {
            String code = student.getCountry().getCode();
            if(map.containsKey(code)){

                Set<Student> countrySet = map.get(code);
                countrySet.add(student);
                map.put(student.getCountry().getCode(), countrySet);
            }else{
                Set<Student> newSet = new HashSet<>();
                newSet.add(student);
                map.put(student.getCountry().getCode(), newSet);
            }
        });
        return map;
    }

    public Iterable<Country> findAllByOrderByEnglishNameAsc() {
        return countryRepository.findAllByOrderByEnglishNameAsc();
    }
}
