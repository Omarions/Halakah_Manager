package com.omarionapps.halaka.service;

import com.omarionapps.halaka.model.Country;
import com.omarionapps.halaka.model.Student;
import com.omarionapps.halaka.model.StudentGender;
import com.omarionapps.halaka.repository.CountryRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

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

    public Iterable<Country> getAll() {
        return countryRepository.findAll();
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
        Get set of students that belong to a country (by its code)
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

    public Map<String, Integer> getCountryStudentsCountMapByGender(StudentGender gender) {
        Map<String, Integer> map = new HashMap<>();
        Set<Country> countries = getCountriesHasStudents();
        countries.forEach((country) -> {
            country.getStudents().stream()
                    .filter((s) -> s.getGender().equalsIgnoreCase(gender.name()))
                    .forEach((s) -> map.put(country.getEnglishName(), country.getStudents().size()));

        });

        return map;
    }

    public Set<Country> getCountriesHasStudents() {
        Set<Country> countries = new HashSet<>();
        getAll().forEach((country) -> {
            if (country.getStudents().size() > 0)
                countries.add(country);
        });

        return countries;
    }

    public Map<String, Map<String, Integer>> getCountryStudentsCountByGenderMap() {
        Map<String, Map<String, Integer>> map = new HashMap<>();

        Set<Country> countries = getCountriesHasStudents();
        countries.forEach((country) -> {
            Map<String, Integer> mapValue = new HashMap<>();
            mapValue.put(StudentGender.MALE.name(), 0);
            mapValue.put(StudentGender.FEMALE.name(), 0);
            country.getStudents()
                    .forEach((student) -> {
                        int malesCount = mapValue.get(StudentGender.MALE.name());
                        int femalesCount = mapValue.get(StudentGender.FEMALE.name());
                        if (student.getGender().equalsIgnoreCase(StudentGender.MALE.name()))
                            mapValue.put(StudentGender.MALE.name(), ++malesCount);
                        else
                            mapValue.put(StudentGender.FEMALE.name(), ++femalesCount);
                    });
            map.put(country.getArabicName(), mapValue);
        });

        return map;
    }

    public Iterable<Country> findAllByOrderByArabicNameAsc() {
        return countryRepository.findAllByOrderByArabicNameAsc();
    }

    public Country findById(int id) {
        return countryRepository.findOne(id);
    }
}
