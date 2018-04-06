package com.omarionapps.halaka.controller;

import com.omarionapps.halaka.model.Activity;
import com.omarionapps.halaka.model.Course;
import com.omarionapps.halaka.model.Student;
import com.omarionapps.halaka.model.StudentStatus;
import com.omarionapps.halaka.service.ActivityService;
import com.omarionapps.halaka.service.CountryService;
import com.omarionapps.halaka.service.CourseService;
import com.omarionapps.halaka.service.TeacherService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import javax.validation.Valid;
import java.sql.Date;
import java.time.LocalDate;
import java.util.Map;
import java.util.Set;

/**
 * Created by Omar on 14-May-17.
 */
@Controller
public class ActivityController {
    private ActivityService activityService;
    private TeacherService teacherService;
    private CountryService countryService;
    private CourseService courseService;

    @Autowired
    public ActivityController(ActivityService activityService, TeacherService teacherService,
                              CountryService countryService, CourseService courseService) {
        this.activityService = activityService;
        this.teacherService = teacherService;
        this.countryService = countryService;
        this.courseService = courseService;
    }

    @GetMapping("/admin/activities")
    public ModelAndView getAll() {
        ModelAndView model = new ModelAndView("admin/activity-list");
        model.addObject("activities", activityService.findAllByArchived(false));
        model.addObject("students", activityService.getTotalStudents());
        return model;
    }

    @GetMapping("/admin/activities/activity")
    public ModelAndView processActivity(@RequestParam(value = "id", required = false) Integer id) {
        return (null == id) ? addActivity() : getActivity(id);
    }

    private ModelAndView addActivity() {
        ModelAndView model = new ModelAndView("admin/register-activity");
        model.addObject("activity", new Activity());
        return model;
    }

    private ModelAndView getActivity(int id) {
        ModelAndView modelAndView = new ModelAndView("admin/activity-profile");
        Activity activity = activityService.findById(id);
        Set<Student> activityStudents = activityService.getStudentsByActivity(activity);
        System.out.println("activity students: " + activityStudents.size());
        modelAndView.addObject("mapCounts", countryService.getCountryCodeStudentsCountMapFromStudetns(activityStudents));

        if (id != 7) {
            long totalStudents = activityService.getTotalStudentsByActivity(activity);
            long totalStudying = activityService.getTotalStudentsByStatus(activity, StudentStatus.STUDYING);
            long totalWaiting = activityService.getTotalStudentsByStatus(activity, StudentStatus.WAITING);
            long totalCertified = activityService.getTotalStudentsByStatus(activity, StudentStatus.CERTIFIED);
            long totalFired = activityService.getTotalStudentsByStatus(activity, StudentStatus.FIRED);
            long totalTempStopped = activityService.getTotalStudentsByStatus(activity, StudentStatus.TEMP_STOP);
            long totalFinalStopped = activityService.getTotalStudentsByStatus(activity, StudentStatus.FINAL_STOP);
            Map<String, Map<String, Long>> countryStudents = activityService.getCountryCodeStudentsStatusCountMap(activity);

            Set<Student> waitStudents = (activityService.getStudentsByActivityByStatus(activity, StudentStatus.WAITING).size() > 0)
                    ? activityService.getStudentsByActivityByStatus(activity, StudentStatus.WAITING) : null;
            Set<Student> studyStudents = (activityService.getStudentsByActivityByStatus(activity, StudentStatus.STUDYING).size() > 0)
                    ? activityService.getStudentsByActivityByStatus(activity, StudentStatus.STUDYING) : null;
            Set<Student> certifiedStudents = (activityService.getStudentsByActivityByStatus(activity, StudentStatus.CERTIFIED).size() > 0)
                    ? activityService.getStudentsByActivityByStatus(activity, StudentStatus.CERTIFIED) : null;
            Set<Student> tempStoppedStudents = (activityService.getStudentsByActivityByStatus(activity, StudentStatus.TEMP_STOP).size() > 0)
                    ? activityService.getStudentsByActivityByStatus(activity, StudentStatus.TEMP_STOP) : null;
            Set<Student> finalStoppedStudents = (activityService.getStudentsByActivityByStatus(activity, StudentStatus.FINAL_STOP).size() > 0)
                    ? activityService.getStudentsByActivityByStatus(activity, StudentStatus.FINAL_STOP) : null;
            Set<Student> firedStudents = (activityService.getStudentsByActivityByStatus(activity, StudentStatus.FIRED).size() > 0)
                    ? activityService.getStudentsByActivityByStatus(activity, StudentStatus.FIRED) : null;

            modelAndView.addObject("activity", activity);
            modelAndView.addObject("teachers", activityService.getTeachersByActivity(activity.getId()));
            modelAndView.addObject("teacherCourses", activityService.getTeacherCoursesByActivity(activity.getId()));
            modelAndView.addObject("courses", activityService.getCoursesByActivity(activity.getId()));
            modelAndView.addObject("students", activityStudents);
            modelAndView.addObject("countryStudents", countryStudents);
            modelAndView.addObject("waitingStudents", waitStudents);
            modelAndView.addObject("studyingStudents", studyStudents);
            modelAndView.addObject("certifiedStudents", certifiedStudents);
            modelAndView.addObject("tempStoppedStudents", tempStoppedStudents);
            modelAndView.addObject("finalStoppedStudents", finalStoppedStudents);
            modelAndView.addObject("firedStudents", firedStudents);
            modelAndView.addObject("totalStudents", totalStudents);
            modelAndView.addObject("totalStudying", totalStudying);
            modelAndView.addObject("totalWaiting", totalWaiting);
            modelAndView.addObject("totalCertified", totalCertified);
            modelAndView.addObject("totalFired", totalFired);
            modelAndView.addObject("totalTempStopped", totalTempStopped);
            modelAndView.addObject("totalFinalStopped", totalFinalStopped);

            return modelAndView;
        } else {
            modelAndView = new ModelAndView("admin/housing-profile");
            return modelAndView;
        }

    }

    @PostMapping("/admin/activities/activity")
    public String updateActivity(@Valid Activity activity, Model model) {
        Activity returnedActivity = null;
        if (activity.getId() != 0) {
            activity.setTeacher(activityService.getTeachersByActivity(activity));
        }
        returnedActivity = activityService.save(activity);
        model.addAttribute("activity", returnedActivity);
        return "redirect:/admin/activities/activity?id=" + returnedActivity.getId();
    }

    /**
     * Send the activity to archive
     *
     * @param id            the activity ID to be archived
     * @param redirectAttrs the message to be send with the link to be directed to after archiving success.
     * @return the link to be directed to after success.
     */
    @GetMapping("/admin/activities/activity/delete")
    public String archiveActivity(@RequestParam(value = "id") int id, RedirectAttributes redirectAttrs) {
        Activity activity = activityService.findById(id);
        activity.setArchived(true);
        activity.setArchivedDate(Date.valueOf(LocalDate.now()));
        Activity archivedActivity = activityService.save(activity);
        if (archivedActivity != null) {
            Set<Course> courses = archivedActivity.getCourses();
            courses.forEach((course) -> {
                course.setArchived(true);
                course.setArchivedDate(Date.valueOf(LocalDate.now()));
                courseService.save(course);
            });
	        redirectAttrs.addFlashAttribute("message", "Activity with ID( " + id + " ) was archived successfully");
        } else {
	        redirectAttrs.addFlashAttribute("message", "An error happens while archiving the activity with ID( " +
			        id + " )");
        }

        return "redirect:/admin/activities";
    }
}
