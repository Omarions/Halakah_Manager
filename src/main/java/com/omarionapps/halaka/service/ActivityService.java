package com.omarionapps.halaka.service;

import com.omarionapps.halaka.model.*;
import com.omarionapps.halaka.repository.ActivityRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.*;

/**
 * Created by Omar on 29-Apr-17.
 */
@Service
public class ActivityService {
    long totalStudents = 0;
    long totalByStatus = 0;
    int countByCountry = 0;
    long totalActivities = 0;
    private ActivityRepository activityRepository;
    private CourseService courseService;
    private CountryService countryService;

    @Autowired
    public ActivityService(ActivityRepository activityRepository, CourseService courseService, CountryService countryService){
        this.activityRepository = activityRepository;
        this.courseService = courseService;
        this.countryService = countryService;
    }

    public List<Activity> findAllByArchived(boolean isArchived) {
        List<Activity> archivedActivities = new ArrayList<>();
        Iterable<Activity> activities = activityRepository.findAll();
        activities.forEach((act) -> {
            if (act.isArchived() == isArchived)
                archivedActivities.add(act);
        });
        return archivedActivities;
    }

    /** Get map of activity id and each total students per that activity
     * e.g map< activityID, number_of_student_of_that_activity>
     *  @return map of each activity and the count of student of that activity
     */
    public Map<Integer, Long> getTotalStudents(){
        Map<Integer, Long> map = new HashMap<>();
        Iterable<Activity> activities = this.findAllOrderByName();
        activities.forEach((activity) -> map.put(activity.getId(), this.getTotalStudentsByActivity(activity.getId())));

        return map;
    }

    public Iterable<Activity> findAllOrderByName() {
        return activityRepository.findAllByOrderByName();
    }

    /**
     * Get total number of students in an activity
     * @param activityId the activity Id to get its number of students
     * @return get the count of students of an activity
     */
    public long getTotalStudentsByActivity(int activityId) {
        Activity activity = this.findById(activityId);
        totalStudents = 0;
        activity.getCourses().stream().forEach((course) -> {
            totalStudents += courseService.totalStudentsByCourse(course.getId());
        });

        return totalStudents;
    }

    public Activity findById(int id) {
        return activityRepository.findOne(id);
    }

    /**
     * Get count of activities
     * @return count of activities
     */
    public long getCount(){
        totalActivities = 0;
        Iterable<Activity> activities = this.findAllOrderByName();
        activities.forEach(activity -> totalActivities++ );
        return totalActivities;
    }

    /**
     * Get count of activities
     * @return count of activities
     */
    public long getCountByArchived(boolean isArchived) {
        totalActivities = 0;
        Iterable<Activity> activities = this.findAllOrderByName();
        activities.forEach(activity -> {
            if (activity.isArchived() == isArchived)
                totalActivities++;
        });
        return totalActivities;
    }

    public long getTotalStudentsByActivity(Activity activity){
        totalStudents = 0;
        activity.getCourses().stream().forEach((course) -> {
            totalStudents += courseService.totalStudentsByCourse(course.getId());
        });

        return totalStudents;
    }

    public long getTotalStudentsByStatus(int id, StudentStatus status){
        Activity activity = this.findById(id);
        totalByStatus = 0;
        activity.getCourses().stream().forEach((course) -> {
            totalByStatus += courseService.totalStudentsByStatus(course.getId(), status);
        });

        return totalByStatus;
    }

    public long getTotalStudentsByStatus(Activity activity, StudentStatus status){
        totalByStatus = 0;
        activity.getCourses().stream().forEach((course) -> {
            totalByStatus += courseService.totalStudentsByStatus(course.getId(), status);
        });

        return totalByStatus;
    }

    public long totalCourseStudentsByStatus(int id,int courseId, StudentStatus status){
        Activity activity = this.findById(id);
        totalByStatus = 0;
        activity.getCourses().stream().filter((c)-> c.getId() == courseId).forEach((course) -> {
            totalByStatus = courseService.totalStudentsByStatus(course.getId(), status);
        });

        return totalByStatus;
    }

    public long totalCourseStudentsByStatus(Activity activity,int courseId, StudentStatus status){
        totalByStatus = 0;
        activity.getCourses().stream().filter((c)-> c.getId() == courseId).forEach((course) -> {
            totalByStatus = courseService.totalStudentsByStatus(course.getId(), status);
        });

        return totalByStatus;
    }

    public Map<Integer, Integer> getTeacherCoursesByActivity(int activityId){
        Map<Integer, Integer> map = new HashMap<>();
        int count = 0;
        Activity activity = this.findById(activityId);
        Set<Teacher> teachers = activity.getTeacher();
        Set<Course> courses = activity.getCourses();
        for (Teacher teacher : teachers){
            for(Course course : teacher.getCourse()){
                if(course.getActivity().getId() == activityId){
                    count++;
                    map.put(teacher.getId(), count);
                }
            }
            count = 0;
        }
        return map;
    }

    public Set<Teacher> getTeachersByActivity(int activityId){
        Activity activity = this.findById(activityId);
        return activity.getTeacher();
    }

    public Set<Teacher> getTeachersByActivity(Activity activity){
        return activity.getTeacher();
    }

    public Set<Course> getCoursesByActivity(int activityId){
        Activity activity = this.findById(activityId);
        return activity.getCourses();
    }

    public Set<Course> getCoursesByActivity(Activity activity){
        return activity.getCourses();
    }

    public Set<Student> getStudentsByActivityByStatus(int activityId, StudentStatus status){
        Set<Student> studentsByStatus = new HashSet<>();
        Set<Student> activityStudents = this.getStudentsByActivity(activityId);
        activityStudents
                .stream()
                .forEach((student) ->
                        student.getStudentTracks()
                                .stream()
                                .filter((st) -> st.getStatus().equalsIgnoreCase(status.toString()) && st.getCourse().getActivity().getId() == activityId)
                                .forEach(track -> studentsByStatus.add(track.getStudent())));

        return studentsByStatus;
    }

    public Set<Student> getStudentsByActivity(int activityId) {
        Activity activity = this.findById(activityId);
        Set<Student> students = new HashSet<>();
        activity.getCourses().forEach((course) -> {
            course.getStudentTracks().forEach((studentTrack -> students.add(studentTrack.getStudent())));
        });

        return students;
    }

    public Map<String, Integer> getStudentsCountByCountry(Activity activity){
        Set<Student> students = this.getStudentsByActivity(activity);
        return countryService.getCountryCodeStudentsCountMapFromStudetns(students);
    }

    public Set<Student> getStudentsByActivity(Activity activity) {
        Set<Student> students = new HashSet<>();
        activity.getCourses().forEach((course) -> {
            course.getStudentTracks().forEach((studentTrack -> students.add(studentTrack.getStudent())));
        });

        return students;
    }

    public Map<String, Set<Map<String, Integer>>> getStudentsCountByCountryAndStatus(Activity activity) {
        //the result variable to be returned
        Map<String, Set<Map<String, Integer>>> result = new HashMap<>();
        //map to save each status and numbers of students in that status
        //e.g map<WAITING, 4> : means there are 4 students in waiting status
        Map<String, Set<Student>> statusStudentsMap = new HashMap<>();
        //create the value map of that should be created in the set of values of the returned map.
        Map<String, Integer> setItem = new HashMap<>();
        //create the set value of each country code key in the returned map
        Set<Map<String, Integer>> countrySet = new HashSet<>();
        //loop over the statuses (e.g. "Waiting", "Studying",...etc.)
        for(StudentStatus status : StudentStatus.values()){
            //get the set of students of each status.
            Set<Student> statusSet = this.getStudentsByActivityByStatus(activity, status);
            //fill the students map with the status as the key and the set of students as the value.
            statusStudentsMap.put(status.toString(), statusSet);
            //loop over the values in the map of status students.
            for(Set<Student> studentSet : statusStudentsMap.values()){
                studentSet.stream().forEach((student) -> {
                    if(setItem.containsKey(status.toString())){
                        int count = setItem.get(status.toString());
                        setItem.put(status.toString(), ++count);
                    }else{
                        setItem.put(status.toString(), 1);
                    }
                    countrySet.add(setItem);
                    result.put(student.getCountry().getCode(), countrySet);
                });
            }
        }
        System.out.println("Country Status counts: " + result);
       return result;
    }

    public Set<Student> getStudentsByActivityByStatus(Activity activity, StudentStatus status) {
        Set<Student> studentsByStatus = new HashSet<>();
        Set<Student> activityStudents = this.getStudentsByActivity(activity);
        activityStudents
                .stream()
                .forEach((student) ->
                        student.getStudentTracks()
                                .stream()
                                .filter((st) -> st.getStatus().equalsIgnoreCase(status.toString()) && st.getCourse().getActivity().getId() == activity.getId())
                                .forEach(track -> studentsByStatus.add(track.getStudent())));

        return studentsByStatus;
    }

    public Map<String, Map<String, Long>> getCountryCodeStudentsStatusCountMap(Activity activity){
        totalByStatus = 0;
        Map<String, Map<String, Long>> result = new HashMap<>();
        Set<Student> students = this.getStudentsByActivity(activity);
        Map<String, Set<Student>> countryStudents = countryService.getCountryCodeStudentsMap(students);

        Set<Map<String, Long>> setMap = new HashSet<>();
        for(String key : countryStudents.keySet()){
            Map<String, Long> map = new HashMap<>();
            Set<Student> set = countryStudents.get(key);
            System.out.println("Key: " + key + ", Value Size: " + set.size());
            set.stream().forEach((student) -> {

                System.out.println("Code: " + key + ", Student ID: " + student.getId());
                for (StudentStatus status : StudentStatus.values()){
                    totalByStatus = (null == map.get(status.toString())) ? 0 : map.get(status.toString());
                    map.put(status.toString(), totalByStatus);
                    System.out.println("Initial Status: " + status + ", count:" + totalByStatus);
                    student.getStudentTracks()
                            .stream()
                            .filter((st) ->st.getStatus().equalsIgnoreCase(status.toString())
                                    && st.getCourse().getActivity().getId() == activity.getId())
                            .forEach((track) -> {
                                map.put(status.toString(), ++totalByStatus);
                                System.out.println("Existed Status: " + status + ", count:" + totalByStatus);
                            });
                }

            });

            System.out.println("SET_MAP: " + map);
            result.put(key, map);
        }
        System.out.println("Result: " + result);
        return result;
    }

    /**
     * Create map of key:value as key is the activity ID and the value is map of teacher id as key and his name as value.
     *
     * @return map of activity and its teachers with their ids and names
     */
    public Map<Integer, Map<Integer, String>> getActivityTeachersMap(){
        Map<Integer, Map<Integer, String>> result = new HashMap<>();
        Map<Integer, Set<Teacher>> map = this.getActivityTeachers();
        for(Integer key : map.keySet()){
            Map<Integer, String> tMap = new HashMap<>();
            map.get(key).stream().forEach((teacher) -> {
                tMap.put(teacher.getId(), teacher.getName());
                result.put(key, tMap);
            });
        }

        return result;
    }

    public Map<Integer, Set<Teacher>> getActivityTeachers() {
        Map<Integer, Set<Teacher>> map = new HashMap<>();

        this.findAllOrderByName().forEach((activity) -> {
            Set<Teacher> teachers = activity.getTeacher();
            map.put(activity.getId(), teachers);
        });

        return map;
    }

    /**
     * Create map of key:value as key is the activity ID and the value is map of course id as key and its name as value.
     *
     * @return map of activity and its teachers with their ids and names
     */
    public Map<Integer, Map<Integer, String>> getActivityCoursesMap() {
        Map<Integer, Map<Integer, String>> result = new HashMap<>();
        Map<Integer, Set<Course>> map = this.getActivityCourses();
        for (Integer key : map.keySet()) {
            Map<Integer, String> tMap = new HashMap<>();
            map.get(key).stream().forEach((course) -> {
                tMap.put(course.getId(), course.getName() + " : " + course.getActivity().getName());
                result.put(key, tMap);
            });
        }
        System.out.println("Activity Courses: " + result);
        return result;
    }

    public Map<Integer, Set<Course>> getActivityCourses() {
        Map<Integer, Set<Course>> map = new HashMap<>();

        this.findAllOrderByName().forEach((activity) -> {
            Set<Course> courses = activity.getCourses();
            map.put(activity.getId(), courses);
        });

        return map;
    }

    public Activity save(Activity activity) {
        return activityRepository.save(activity);
    }

    public void delete(int id) {
        activityRepository.delete(id);
    }
}
