package com.omarionapps.halaka.service;

import com.omarionapps.halaka.model.Activity;
import com.omarionapps.halaka.model.Country;
import com.omarionapps.halaka.model.Student;
import com.omarionapps.halaka.model.StudentTrack;
import com.omarionapps.halaka.repository.CountryRepository;
import com.omarionapps.halaka.utils.StudentGender;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.*;
import java.util.stream.Collectors;

/**
 * Created by Omar on 08-May-17.
 */
@Service
public class CountryService {

	private CountryRepository   countryRepository;
	private StudentTrackService studentTrackService;

	@Autowired
	public CountryService(CountryRepository countryRepository, StudentTrackService studentTrackService) {
		this.countryRepository = countryRepository;
		this.studentTrackService = studentTrackService;
	}

	public Optional<Country> findById(int id) {
		return countryRepository.findById(id);
	}

	public Iterable<Country> getAll() {
		return countryRepository.findAll();
	}
	
	public List<Country> findAllByOrderByArabicNameAsc() {
		return countryRepository.findAllByOrderByArabicNameAsc();
	}

	public List<Country> findAllByOrderByEnglishNameAsc() {
		return countryRepository.findAllByOrderByEnglishNameAsc();
	}

	public Set<Country> getCountriesHasStudents() {
		Set<Country> countries = findAllByOrderByEnglishNameAsc()
				                         .stream()
				                         .filter(country -> country.getStudents().size() > 0)
				                         .sorted()
				                         .collect(Collectors.toSet());
		return countries;
	}

	public Map<String, Set<Student>> groupStudentsByCountryCode() {
		Iterable<Country>         list = this.getAll();
		Map<String, Set<Student>> map  = new HashMap<>();

		list.forEach((country) -> {
			if (country.getStudents().size() > 0)
				map.put(country.getCode(), country.getStudents());
		});
		return map;
	}

	public Map<String, Set<Student>> getCountryCodeStudentsMap(Set<Student> students) {
		Map<String, Set<Student>> map = new HashMap<>();
		students.forEach((student) -> {
			String code = student.getCountry().getCode();
			if (map.containsKey(code)) {

				Set<Student> countrySet = map.get(code);
				countrySet.add(student);
				map.put(student.getCountry().getCode(), countrySet);
			} else {
				Set<Student> newSet = new HashSet<>();
				newSet.add(student);
				map.put(student.getCountry().getCode(), newSet);
			}
		});
		return map;
	}

	public Map<String, Integer> getAllCountryCodeStudentsCountMap() {
		Iterable<Country>    list = this.getAll();
		Map<String, Integer> map  = new HashMap<>();

		list.forEach((country) -> {
			if (country.getStudents().size() > 0)
				map.put(country.getCode(), country.getStudents().size());
		});
		//System.out.println("CountryCode Map: " + map);
		return map;
	}

	public Map<String, Integer> getCountryStudentsCountMapByGender(StudentGender gender) {
		Map<String, Integer> map       = new HashMap<>();
		Set<Country>         countries = getCountriesHasStudents();
		countries.forEach((country) -> {
			country.getStudents().stream()
			       .filter((s) -> s.getGender().equalsIgnoreCase(gender.name()))
			       .forEach((s) -> map.put(country.getEnglishName(), country.getStudents().size()));

		});

		return map;
	}

	public Map<String, Integer> getCountryCodeStudentsCountMapFromStudetns(Set<Student> students) {
		Map<String, Integer> map = new HashMap<>();
		students.forEach((student -> {
			String code = student.getCountry().getCode();
			if (map.containsKey(code)) {
				int count = map.get(code);
				map.put(code, ++count);
			} else {
				map.put(code, 1);
			}

		}));
		return map;
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
				       int malesCount   = mapValue.get(StudentGender.MALE.name());
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

	public Map<Country, Integer> getCountryStudentsCountMapFromStudetns(Set<Student> students) {
		Map<Country, Integer> map = new HashMap<>();
		students.forEach((student -> {
			Country country = student.getCountry();
			if (map.containsKey(country)) {
				int count = map.get(country);
				map.put(country, ++count);
			} else {
				map.put(country, 1);
			}

		}));
		return map;
	}

	public Map<Country, Set<Student>> groupStudentsByCountry(Set<Student> students) {
		Map<Country, Set<Student>> map = new HashMap<>();

		students.forEach((student -> {
			Country country = student.getCountry();
			if (map.containsKey(country)) {
				Set<Student> existSet = map.get(country);
				existSet.add(student);
				map.put(country, existSet);
			} else {
				Set<Student> newSet = new HashSet<>();
				newSet.add(student);
				map.put(country, newSet);
			}


		}));
		return map;
	}

	public Map<Country, Set<StudentTrack>> groupTracksByCountry(Set<StudentTrack> tracks) {
		Map<Country, Set<StudentTrack>> map = new HashMap<>();

		tracks.forEach((track -> {
			Country country = track.getStudent().getCountry();
			if (map.containsKey(country)) {
				Set<StudentTrack> existSet = map.get(country);
				existSet.add(track);
				map.put(country, existSet);
			} else {
				Set<StudentTrack> newSet = new HashSet<>();
				newSet.add(track);
				map.put(country, newSet);
			}
		}));

		return map.entrySet()
		          .stream()
		          .collect(Collectors.toMap(item -> item.getKey(), item -> item.getValue()));

	}

	public double getCountriesIncrementRate(int days) {
		LocalDate start = LocalDate.now().minusDays(days);
		LocalDate end   = LocalDate.now();

		//System.out.println("Start LocalDate: " + start.toString());
		//System.out.println("End LocalDate: " + end);

		Set<StudentTrack> allTracks      = studentTrackService.findAllByOrderByRegisterDate();
		Set<StudentTrack> filteredTracks = studentTrackService.findAllByRegisterDateBetween(start, end);
		//System.out.println("Filtered Tracks: " + filteredTracks);

		double totalCountries = groupTracksByCountry(allTracks).keySet().size();
		//System.out.println("Total Countries: " + totalCountries);
		double filterCountriesCount = groupTracksByCountry(filteredTracks).keySet().size();
		//System.out.println("Total Countries in period: " + filterCountriesCount);
		double rate = (filterCountriesCount / totalCountries) * 100;

		return rate;
	}

	/**
	 * Get increment rate of countries registered in some period (days) for specific activity
	 * @param days the period to get rate for
	 * @param activity  to get the rate for
	 * @return  double value of increment rate
	 */
	public double getCountriesIncrementRate(int days, Activity activity) {
		LocalDate start = LocalDate.now().minusDays(days);
		LocalDate end   = LocalDate.now();
		
		Set<StudentTrack> allTracks      = studentTrackService.findAllByOrderByRegisterDate();
		Set<StudentTrack> activityTracks = allTracks.stream()
		                                            .filter(track -> track.getCourse().getActivity().equals(activity))
		                                            .collect(Collectors.toSet());
		Set<StudentTrack> filteredTracks = studentTrackService.findAllByRegisterDateBetween(start, end);
		Set<StudentTrack> filteredActivityTracks = filteredTracks.stream()
		                                                         .filter(track -> track.getCourse().getActivity().equals(activity))
		                                                         .collect(Collectors.toSet());

		//System.out.println("Filtered Tracks: " + filteredTracks);

		double totalCountries = groupTracksByCountry(activityTracks).keySet().size();
		//System.out.println("Total Countries: " + totalCountries);
		double filterCountriesCount = groupTracksByCountry(filteredActivityTracks).keySet().size();
		//System.out.println("Total Countries in period: " + filterCountriesCount);
		double rate = (filterCountriesCount / totalCountries) * 100;

		return rate;
	}

	public long getCountryCountByActivity(Activity activity){
		  return studentTrackService.findAllByOrderByRegisterDate()
		                            .stream()
		                            .filter(track -> track.getCourse().getActivity().equals(activity))
		                            .count();
	}
}
