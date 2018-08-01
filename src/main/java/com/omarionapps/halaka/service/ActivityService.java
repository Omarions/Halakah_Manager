package com.omarionapps.halaka.service;

import com.omarionapps.halaka.model.*;
import com.omarionapps.halaka.repository.ActivityRepository;
import com.omarionapps.halaka.utils.StudentGender;
import com.omarionapps.halaka.utils.StudentStatus;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.*;
import java.util.stream.Collectors;

/**
 * Created by Omar on 29-Apr-17.
 */
@Service
public class ActivityService {
	private ActivityRepository activityRepository;
	private CourseService      courseService;
	private CountryService     countryService;
	private CertificateService certificateService;

	private long totalByStatus = 0;

	@Autowired
	public ActivityService(ActivityRepository activityRepository, CourseService courseService, CountryService countryService, CertificateService certificateService) {
		this.activityRepository = activityRepository;
		this.courseService = courseService;
		this.countryService = countryService;
		this.certificateService = certificateService;
	}

	/**
	 * Get count of activities
	 *
	 * @return count of activities
	 */
	public long getCount() {
		return this.findAllByOrderByName().size();
	}

	/**
	 * Get count of activities
	 *
	 * @return count of activities
	 */
	public long getCountByArchived(boolean isArchived) {

		return this.findAllByOrderByName().stream()
		           .filter(activity -> (activity.isArchived() == isArchived))
		           .count();
	}

	public long getTotalStudentsByActivityAndGender(Activity activity, StudentGender gender) {
		return findStudentsByActivity(activity)
				       .stream()
				       .filter(student -> student.getGender().equals(gender.toString()))
				       .count();
	}

	public long getTotalStudentsByActivity(Activity activity) {
		return findStudentsByActivity(activity).size();
	}

	public long getTotalStudentsByActivityByStatus(Activity activity, StudentStatus status) {

		return activity.getCourses().stream()
		               .flatMap(course -> course.getStudentTracks().stream())
		               .filter(track -> track.getStatus().equals(status.toString()))
		               .map(track -> track.getStudent())
		               .distinct()
		               .count();

	}

	public long getTotalStudentsTeachersByActivity(Activity activity) {
		return findTeachersByActivity(activity).stream().filter(teacher -> teacher.isCertifiedStudent()).count();
	}

	public long getTotalNationalitiesByActivity(Activity activity){
		return findStudentsByActivity(activity).stream().map(student -> student.getCountry()).distinct().count();
	}

	public long getTotalNationalitiesByActivityAndStatus(Activity activity, StudentStatus status){
		return findTracksByActivityAndStatus(activity, status).stream().map(st -> st.getStudent().getCountry()).distinct().count();
	}

	public long getTotalActiveStudentsForSTByActivity(Activity activity) {
		long totalStudying = activity.getCourses()
		                             .stream()
		                             .filter(course -> course.getTeacher().isCertifiedStudent())
		                             .flatMap(course -> course.getStudentTracks()
		                                                      .stream())
		                             .filter(st -> st.getStatus().equals(StudentStatus.STUDYING))
		                             .count();
		long totalTemp = activity.getCourses()
		                         .stream()
		                         .filter(course -> course.getTeacher().isCertifiedStudent())
		                         .flatMap(course -> course.getStudentTracks()
		                                                  .stream())
		                         .filter(st -> st.getStatus().equals(StudentStatus.TEMP_STOP))
		                         .count();

		long total = totalStudying + totalTemp;

		return total;
	}

	public Optional<Activity> findById(int id) {
		return activityRepository.findById(id);
	}

	public List<Activity> findAllByArchived(boolean isArchived) {
		List<Activity>     archivedActivities = new ArrayList<>();
		Iterable<Activity> activities         = activityRepository.findAll();
		activities.forEach((act) -> {
			if (act.isArchived() == isArchived)
				archivedActivities.add(act);
		});
		return archivedActivities;
	}

	public List<Activity> findAllByOrderByName() {
		List<Activity> activities = activityRepository.findAllByOrderByName();

		activities
				.forEach(activity -> activity.setTeachers(this.findTeachersByActivity(activity)));

		return activities;
	}

	public List<Certificate> findCertificatesByActivity(Activity activity) {
		return certificateService.findAllByOrderById()
		                         .stream()
		                         .filter(certificate -> certificate.getStudentTrack().getCourse().getActivity().equals(activity))
		                         .collect(Collectors.toList());
	}

	public Set<Student> findStudentsByActivityAndStatus(Activity activity, StudentStatus status) {
		return this.findStudentsByActivity(activity)
		           .stream()
		           .flatMap((student -> student.getStudentTracks().stream()))
		           .filter((st) -> st.getStatus().equals(status.toString()) && st.getCourse().getActivity().equals(activity))
		           .map(StudentTrack::getStudent)
		           .distinct()
		           .collect(Collectors.toSet());
	}

	public Set<Student> findStudentsByActivity(Activity activity) {

		return activity.getCourses()
		               .stream()
		               .flatMap(course -> course.getStudentTracks().stream())
		               .map(StudentTrack::getStudent)
		               .distinct()
		               .collect(Collectors.toSet());

	}

	public Set<StudentTrack> findTracksByActivity(Activity activity) {
		return this.findStudentsByActivity(activity)
		           .stream()
		           .flatMap((student -> student.getStudentTracks().stream()))
		           .filter((st) -> st.getCourse().getActivity().equals(activity))
		           .collect(Collectors.toSet());
	}

	public Set<StudentTrack> findTracksByActivityAndStatus(Activity activity, StudentStatus status) {
		return this.findStudentsByActivity(activity)
		           .stream()
		           .flatMap((student -> student.getStudentTracks().stream()))
		           .filter((st) -> st.getStatus().equals(status.toString()) && st.getCourse().getActivity().equals(activity))
		           .collect(Collectors.toSet());
	}

	public Set<Teacher> findTeachersByActivity(Activity activity) {
		return courseService.findAllByOrderByName()
		                    .stream()
		                    .filter(course -> course.getActivity().equals(activity))
		                    .map(Course::getTeacher)
		                    .collect(Collectors.toSet());
	}

	/**
	 * Get map of activity id and each total students per that activity
	 * e.g map< activityID, total_students_of_that_activity>
	 *
	 * @return map of each activity and the count of student of that activity
	 */
	public Map<Integer, Long> mapStudentsCountWithActivityId() {
		return this.findAllByOrderByName().stream().collect(Collectors.toMap(
				Activity::getId,
				act -> getTotalStudentsByActivity(act)));
	}

	/**
	 * Get map of activity id and each total certificates per that activity
	 * e.g map< activityID, total_certificates_of_that_activity>
	 *
	 * @return map of each activity and the count of student of that activity
	 */
	public Map<Integer, Integer> mapCertsCountWithActivityId() {
		return this.findAllByOrderByName()
		           .stream()
		           .collect(Collectors.toMap(
				           Activity::getId,
				           act -> this.findCertificatesByActivity(act).size()));
	}

	public Map<Country, Integer> mapStudentsCountWithCountryByActivity(Activity activity) {
		Set<Student> students = findStudentsByActivity(activity);
		Map<Country, Integer> res = countryService.getCountryStudentsCountMapFromStudetns(students).entrySet()
		                                          .stream()
		                                          .sorted(Comparator.comparing(entry -> entry.getValue()))
		                                          .collect(Collectors.toMap(
				                                          item -> item.getKey(), item -> item.getValue())
		                                          );

	/*	System.out.println("Students count per country:-");
		res.entrySet().forEach(entry -> System.out.println("CountryKey: " + entry.getKey() + " , Student size: " +
				                                                   entry.getValue()));
*/
		return res;
	}


	public Map<Country, Set<Student>> mapStudentsWithCountryByActivity(Activity activity) {
		Set<Student> students = findStudentsByActivityAndStatus(activity, StudentStatus.WAITING);

		Map<Country, Set<Student>> res = countryService.groupStudentsByCountry(students);

		/*for (Country country : res.keySet()) {
			System.out.println("CountryKey: " + country.getEnglishName());
			System.out.println("StudentSet size: " + res.get(country).size());
			System.out.println("StudentSetValue: ");
			for (Student student : res.get(country)) {
				System.out.println("StudentItem: " + student.getId());
			}
		}*/
		return res;
	}

	/**
	 * Create map of key:value as key is the country code and the value is a set of map of student status as key and his
	 * count of students of this status as value for specified activity
	 * example: Map<String, Set<Map<String, Integer>>> map => <"EG", [Map<"WAITING", 3>, [Map<"STUDYING", 5>]>
	 *
	 * @param activity the activity to create the map for.
	 * @return map of country code as key and the value is a set of map with status as key and count of student for
	 * that status as value.
	 */
	public Map<String, Set<Map<String, Integer>>> getStudentsCountByCountryAndStatus(Activity activity) {
		//the result variable to be returned
		Map<String, Set<Map<String, Integer>>> result = new HashMap<>();
		//map to save each status and set of students in that status
		//e.g map<WAITING, Set<Student>> : means there are set of students in waiting status
		Map<String, Set<Student>> statusStudentsMap = new HashMap<>();
		//create the value map of that should be created in the set of values of the returned map.
		Map<String, Integer> setItem = new HashMap<>();
		//create the set value of each country code key in the returned map
		Set<Map<String, Integer>> countrySet = new HashSet<>();
		//loop over the statuses (e.g. "Waiting", "Studying",...etc.)
		for (StudentStatus status : StudentStatus.values()) {
			//get the set of students of each status.
			Set<Student> statusSet = this.findStudentsByActivityAndStatus(activity, status);
			//fill the students map with the status as the key and the set of students as the value.
			statusStudentsMap.put(status.toString(), statusSet);
			//loop over the values in the map of status students.
			for (Set<Student> studentSet : statusStudentsMap.values()) {
				studentSet.stream().forEach((student) -> {
					if (setItem.containsKey(status.toString())) {
						int count = setItem.get(status.toString());
						setItem.put(status.toString(), ++count);
					} else {
						setItem.put(status.toString(), 1);
					}
					countrySet.add(setItem);
					result.put(student.getCountry().getCode(), countrySet);
				});
			}
		}
		//System.out.println("Country Status counts: " + result);
		return result;
	}

	public Map<Country, Set<StudentTrack>> mapTracksWithCountryByActivity(Activity activity) {
		Set<StudentTrack> tracks = findTracksByActivity(activity);

		Map<Country, Set<StudentTrack>> res = countryService.groupTracksByCountry(tracks);

		return res;
	}

	/**
	 * Group a map of country and set of tracks with course ID
	 * e.g. <courseID, Map<Country, Set<Tracks>>
	 *
	 * @param activity to search for
	 * @return a map with key is the course ID and the value is a map with country as key and set of tracks as value.
	 */
	public Map<Integer, Map<Country, Set<StudentTrack>>> mapCandidatesWithCourse_IdByActivity(Activity activity) {
		Map<Integer, Map<Country, Set<StudentTrack>>> candidates        = new HashMap<>();
		Map<Country, Set<StudentTrack>>               candidateMapValue = new HashMap<>();
		findCandidatesByActivity(activity)
				.entrySet()
				.stream()
				.filter(countrySetEntry -> countrySetEntry.getValue().size() > 0)
				.forEach(entry -> {
					entry.getValue().forEach(val -> {
						candidateMapValue.put(entry.getKey(), entry.getValue());
						candidates.put(val.getCourse().getId(), candidateMapValue);

					});
				});


		return candidates;
	}

	public Map<Country, Set<StudentTrack>> findCandidatesByActivity(Activity activity) {
		Map<Country, Set<StudentTrack>> allStatusesExceptWaiting = mapTracksExceptWaitingWithCountryByActivity
				                                                           (activity);
		Map<Country, Set<StudentTrack>> waitingStatus = mapTracksWithCountryByActivityAndStatus(activity, StudentStatus.WAITING);

		Map<Country, Set<StudentTrack>> candidates = new HashMap<>();

/*		System.out.println("Activity tracks grouped by Country:-");
		allStatusesExceptWaiting.entrySet().stream().sorted(Comparator.comparing(item -> item.getValue().size())).forEach(entry -> System.out.println("Country Key: " + entry.getKey().getEnglishName() + ", Tracks size per country: " + entry.getValue().size()));
		System.out.println("--------------------------------------------------");

		System.out.println("Activity waiting tracks grouped by Country:-");
		waitingStatus.entrySet().forEach(entry -> System.out.println("Country Key: " + entry.getKey().getEnglishName() + ", Tracks size per country: " + entry.getValue().size()));

		System.out.println("--------------------------------------------------");*/
		waitingStatus.entrySet()
		             .forEach(waitEntry -> {
			             allStatusesExceptWaiting.entrySet()
			                                     .stream().sorted(Comparator.comparing(allItem -> allItem.getValue().size()))
			                                     .forEach(allEntry -> {
				                                     if (allEntry.getKey().equals(waitEntry.getKey())) {
					                                     candidates.put(waitEntry.getKey(), waitEntry.getValue());
				                                     }
			                                     });
		             });


	/*	System.out.println("Activity candidate tracks grouped by Country:-");
		candidates.entrySet().stream().forEach(entry -> System.out.println("Country Key: " + entry.getKey().getEnglishName() + ", Tracks size per country: " + entry.getValue().size()));*/
		return candidates;
	}

	public Map<Country, Set<StudentTrack>> mapTracksExceptWaitingWithCountryByActivity(Activity activity) {
		Set<StudentTrack> tracks = findTracksByActivity(activity);
		Set<StudentTrack> filteredTracks = tracks.stream()
		                                         .filter(track -> !(track.getStatus().equals(StudentStatus.WAITING.toString())))
		                                         .collect(Collectors.toSet());

		Map<Country, Set<StudentTrack>> res = countryService.groupTracksByCountry(filteredTracks);

		return res;
	}

	public Map<Country, Set<StudentTrack>> mapTracksWithCountryByActivityAndStatus(Activity activity, StudentStatus
			                                                                                                  status) {
		Set<StudentTrack> tracks = findTracksByActivityAndStatus(activity, status);

		Map<Country, Set<StudentTrack>> res = countryService.groupTracksByCountry(tracks);

		return res;
	}

	/**
	 * Create map of key:value as key is the country code and the value is map of student status  as key and his
	 * count of students of this status as value for specified activity
	 * example: Map<String, Map<String, Long>> map => <"EG", Map<"WAITING", 3>>
	 *
	 * @param activity the activity to create the map for.
	 * @return map of country code as key and the value is map of status as key and count of students for that status
	 * as value.
	 */
	public Map<String, Map<String, Long>> getCountryCodeStudentsStatusCountMap(Activity activity) {
		totalByStatus = 0;
		Map<String, Map<String, Long>> result = new HashMap<>();
		//get students of the activity
		Set<Student> students = this.findStudentsByActivity(activity);
		//map between country code and students
		Map<String, Set<Student>> countryStudents = countryService.getCountryCodeStudentsMap(students);

		for (String key : countryStudents.keySet()) {
			Map<String, Long> map = new HashMap<>();
			Set<Student>      set = countryStudents.get(key);
			//	System.out.println("Key: " + key + ", Value Size: " + set.size());
			set.forEach((student) -> {

				//	System.out.println("Code: " + key + ", Student ID: " + student.getId());
				for (StudentStatus status : StudentStatus.values()) {
					totalByStatus = (null == map.get(status.toString())) ? 0 : map.get(status.toString());
					map.put(status.toString(), totalByStatus);
					//	System.out.println("Initial Status: " + status + ", count:" + totalByStatus);
					student.getStudentTracks()
					       .stream()
					       .filter((st) -> st.getStatus().equalsIgnoreCase(status.toString())
							                       && st.getCourse().getActivity().getId() == activity.getId())
					       .forEach((track) -> {
						       map.put(status.toString(), ++totalByStatus);
						       //System.out.println("Existed Status: " + status + ", count:" + totalByStatus);
					       });
				}

			});

			//System.out.println("SET_MAP: " + map);
			result.put(key, map);
		}
		//System.out.println("Result: " + result);
		return result;
	}

	/**
	 * Create map of key:value as key is the activity ID and the value is map of teacher id as key and his name as value.
	 *
	 * @return map of activity and its teachers with their ids and names
	 */
	public Map<Integer, Map<Integer, String>> mapTeacherDetailsWithActivityId() {
		Map<Integer, Map<Integer, String>> result = new HashMap<>();
		Map<Integer, Set<Teacher>>         map    = this.mapTeachersWithActivityId();
		for (Integer key : map.keySet()) {
			Map<Integer, String> tMap = new HashMap<>();
			map.get(key).forEach((teacher) -> {
				tMap.put(teacher.getId(), teacher.getName());
				result.put(key, tMap);
			});
		}

		return result;
	}

	/**
	 * Create map of key:value as key is the activity ID and the value is set of teachers
	 *
	 * @return map of activity and its teachers
	 */
	public Map<Integer, Set<Teacher>> mapTeachersWithActivityId() {
		return findAllByOrderByName().stream()
		                             .collect(Collectors.toMap(
				                             Activity::getId,
				                             act -> findTeachersByActivity(act))
		                             );

	}

	/**
	 * Create map of key:value as key is the activity ID and the value is map of course id as key and its name as value.
	 *
	 * @return map of activity and its teachers with their ids and names
	 */
	public Map<Integer, Map<Integer, String>> mapCourseDetailsWithActivityId() {
		Map<Integer, Map<Integer, String>> result = new HashMap<>();
		Map<Integer, Set<Course>>          map    = this.mapCoursesWithActivityId();
		for (Integer key : map.keySet()) {
			Map<Integer, String> tMap = new HashMap<>();
			map.get(key).forEach((course) -> {
				tMap.put(course.getId(), course.getName() + " : " + course.getActivity().getName());
				result.put(key, tMap);
			});
		}
		//System.out.println("Activity Courses: " + result);
		return result;
	}

	/**
	 * Create map of key:value as key is the activity ID and the value is set of courses
	 *
	 * @return map of activity and its courses
	 */
	public Map<Integer, Set<Course>> mapCoursesWithActivityId() {
		Map<Integer, Set<Course>> map = new HashMap<>();

		this.findAllByOrderByName().forEach((activity) -> {
			Set<Course> courses = activity.getCourses();
			map.put(activity.getId(), courses);
		});

		return map;
	}

	public Activity save(Activity activity) {
		return activityRepository.save(activity);
	}

	public void delete(int id) {
		activityRepository.deleteById(id);
	}
}
