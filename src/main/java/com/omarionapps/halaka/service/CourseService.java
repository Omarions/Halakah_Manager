package com.omarionapps.halaka.service;

import com.omarionapps.halaka.model.*;
import com.omarionapps.halaka.repository.CourseRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.*;
import java.util.stream.Collectors;

/**
 * Created by Omar on 30-Apr-17.
 */
@Service
public class CourseService {

	private CourseRepository courseRepository;
	private CountryService   countryService;
	private long             totalByCourse;
	private int              totalCourses;

	@Autowired
	public CourseService(CourseRepository courseRepository, CountryService countryService) {
		this.courseRepository = courseRepository;
		this.countryService = countryService;
	}

	public long getCountByArchived(boolean isArchived) {
		return findAllByArchive(isArchived).size();
	}

	public Set<Course> findAllByArchive(boolean isArchived) {
		Set<Course>      archivedCourses = new HashSet<>();
		Iterable<Course> courses         = findAllByOrderByName();
		courses.forEach((course) -> {
			if (course.isArchived() == isArchived)
				archivedCourses.add(course);
		});

		return archivedCourses;
	}

	/**
	 * Get all courses ordered by name
	 *
	 * @return Itrable of course ordered by name
	 */
	public List<Course> findAllByOrderByName() {
		return courseRepository.findAllByOrderByName();
	}

	/**
	 * Get the total number of students related to the course with specified ID
	 *
	 * @param course the course you look for
	 * @return the total number of students for a specified course
	 */
	public int totalStudentsByCourse(Course course) {
		return course.getStudentTracks().size();
	}

	/**
	 * Get Course by its ID
	 *
	 * @param id the id of the course to search for
	 * @return the course with the specified ID
	 */
	public Optional<Course> findById(int id) {
		return courseRepository.findById(id);
	}

	/**
	 * Get the total number of students related to the course with specified ID
	 *
	 * @param courseId the id of course you look for
	 * @return the total number of students for a specified course
	 */
	public int totalStudentsByCourse(int courseId) {
		Optional<Course> course = this.findById(courseId);

		return totalStudentsByCourse(course.get());
	}

	/**
	 * Get the total number of students by status that related to the course with specified ID
	 *
	 * @param course the course you look for
	 * @return the total number of students with specified status for a specified course
	 */
	public long totalStudentsByStatus(Course course, StudentStatus status) {
		return course.getStudentTracks().stream().filter((st) -> st.getStatus().equals(status.toString())).count();
	}

	/**
	 * Get the set of students by status that related to the course with specified ID
	 *
	 * @param courseId the id of course you look for
	 * @return a set of students with specified status for a specified course
	 */
	public Set<Student> getStudentsByStatus(int courseId, StudentStatus status) {
		Optional<Course> course = this.findById(courseId);

		return getStudentsByStatus(course.get(), status);
	}

	/**
	 * Get the set of students by status that related to the course with specified ID
	 *
	 * @param course the course you look for
	 * @return a set of students with specified status for a specified course
	 */
	public Set<Student> getStudentsByStatus(Course course, StudentStatus status) {
		return course.getStudentTracks().stream().filter(st -> st.getStatus().equals(status.toString())).map(st -> st.getStudent()).collect(Collectors.toSet());
	}

	public Set<Country> getCountriesByCourse(Course course, Set<Country> countries) {
		Set<Country> countriesSet = countries.stream()
		                                     .flatMap(country -> country.getStudents().stream())
		                                     .flatMap(student -> student.getStudentTracks().stream())
		                                     .filter(st -> st.getCourse().equals(course))
		                                     .map(StudentTrack::getStudent)
		                                     .map(Student::getCountry)
		                                     .collect(Collectors.toSet());

		System.out.println("Count of countries per course( " + course.getId() + " ) = " + countriesSet.size());
		countriesSet.forEach(country -> System.out.println("course ID: " + course.getId() + ", country: " + country.getEnglishName()));
		return countriesSet;
	}

	public Map<Integer, Map<String, Set<Student>>> getCandidatesMap() {
		Map<Integer, Map<String, Set<Student>>> map = new HashMap<>();
		map = findAllByArchive(false).stream()
		                             .collect(Collectors.toMap(
				                             course -> course.getId(),
				                             course -> getCourseCandidatesMapByCourse(course)
		                             ));
		return map;
	}

	public Map<String, Set<Student>> getCourseCandidatesMapByCourse(Course course) {
		Set<Student>              courseWaitingStudents = getStudentsByStatus(course, StudentStatus.WAITING);
		Map<String, Set<Student>> countryMap            = countryService.getCountryCodeStudentsMap(courseWaitingStudents);
		countryMap.keySet().stream().sorted(Comparator.comparing(key -> countryMap.get(key).size()));

		System.out.println("Sorted Country Map: " + countryMap);

		return countryMap;
	}

	public Map<String, Map<String, Long>> getCountryCodeStudentsStatusCountMap(Course course) {
		totalByCourse = 0;
		Map<String, Map<String, Long>> result          = new HashMap<>();
		Set<Student>                   students        = this.getStudentsByCourse(course);
		Map<String, Set<Student>>      countryStudents = countryService.getCountryCodeStudentsMap(students);

		Set<Map<String, Long>> setMap = new HashSet<>();
		for (String key : countryStudents.keySet()) {
			Map<String, Long> map = new HashMap<>();
			Set<Student>      set = countryStudents.get(key);
			//System.out.println("Key: " + key + ", Value Size: " + set.size());
			set.stream().forEach((student) -> {

				// System.out.println("Code: " + key + ", Student ID: " + student.getId());
				for (StudentStatus status : StudentStatus.values()) {
					totalByCourse = (null == map.get(status.toString())) ? 0 : map.get(status.toString());
					map.put(status.toString(), totalByCourse);
					//System.out.println("Initial Status: " + status + ", count:" + totalByCourse);
					student.getStudentTracks()
					       .stream()
					       .filter((st) -> st.getStatus().equalsIgnoreCase(status.toString())
							                       && st.getCourse().getId() == course.getId())
					       .forEach((track) -> {
						       map.put(status.toString(), ++totalByCourse);
						       //System.out.println("Existed Status: " + status + ", count:" + totalByCourse);
					       });
				}

			});

			//System.out.println("SET_MAP: " + map);
			result.put(key, map);
		}
		//System.out.println("Result: " + result);
		return result;
	}

	public Set<Student> getStudentsByCourse(Course course) {
		return course.getStudentTracks().stream().map((st) -> st.getStudent()).collect(Collectors.toSet());
	}

	public Map<String, List<Course>> getActivityCoursesByDayMap(Activity activity) {
		Map<String, List<Course>> map = new HashMap<>();

		for (WeekDays day : WeekDays.values()) {

			List<Course> filteredCourses = new ArrayList<>();
			List<Course> courses         = courseRepository.findCoursesByActivity(activity);
			for (Course course : courses) {
				if (course.getDays().contains(day.toString()))
					filteredCourses.add(course);
			}
			filteredCourses.stream().sorted(Comparator.comparing(course -> course.getStartTime()));
			map.put(day.toString(), filteredCourses);
		}
		System.out.println("CoursesDayMap: " + map);
		return map;
	}

	/**
	 * Save the course to the DB
	 *
	 * @param course the course to be saved
	 * @return the saved course.
	 */
	public Course save(Course course) {
		return courseRepository.save(course);
	}

	/**
	 * Delete a specified course
	 *
	 * @param id the id of the course to be deleted
	 */
	public void delete(int id) {
		courseRepository.deleteById(id);
	}


}
