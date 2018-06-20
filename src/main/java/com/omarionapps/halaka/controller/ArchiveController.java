package com.omarionapps.halaka.controller;

import com.omarionapps.halaka.model.Activity;
import com.omarionapps.halaka.model.Student;
import com.omarionapps.halaka.model.Teacher;
import com.omarionapps.halaka.service.ActivityService;
import com.omarionapps.halaka.service.CourseService;
import com.omarionapps.halaka.service.StudentService;
import com.omarionapps.halaka.service.TeacherService;
import com.omarionapps.halaka.utils.LocationTag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.mvc.method.annotation.MvcUriComponentsBuilder;

import java.util.List;
import java.util.Set;

/**
 * Process all requests to archive part
 * Created by Omar on 05-Jul-17.
 */
@Controller
public class ArchiveController {
    private ActivityService activityService;
    private CourseService courseService;
    private StudentService studentService;
    private TeacherService teacherService;

    @Autowired
    public ArchiveController(ActivityService activityService, CourseService courseService,
                             StudentService studentService, TeacherService teacherService) {
        this.activityService = activityService;
        this.courseService = courseService;
        this.studentService = studentService;
        this.teacherService = teacherService;
    }

    @GetMapping(value = "/admin/archive/activities")
    public ModelAndView getArchivedActivities() {
	    ModelAndView   modelAndView = new ModelAndView("admin/activity-list");
	    List<Activity> activities   = activityService.findAllByArchived(true);
	    activities.stream().forEach(activity -> {
		    String activityLogoUrl = MvcUriComponentsBuilder
				    .fromMethodName(PhotoController.class, "getFile", activity.getLogo(), LocationTag.ACTIVITY_STORE_LOC)
				    .build()
				    .toString();

		    activity.setLogoUrl(activityLogoUrl);
	    });
	    modelAndView.addObject("activities", activities);
        modelAndView.addObject("students", activityService.getTotalStudents());
        modelAndView.addObject("isArchived", true);
        return modelAndView;
    }

    @GetMapping(value = "/admin/archive/courses")
    public ModelAndView getArchivedCourses() {
        ModelAndView modelAndView = new ModelAndView("admin/course-list");
        modelAndView.addObject("courses", courseService.findAllByArchive(true));
        modelAndView.addObject("isArchived", true);
        return modelAndView;
    }

    @GetMapping(value = "/admin/archive/students")
    public ModelAndView getArchivedStudents() {
        ModelAndView modelAndView = new ModelAndView("admin/student-list");
	    Set<Student> students     = studentService.findAllByArchive(true);
	    students.stream().forEach(student -> {
		    String photoUrl = MvcUriComponentsBuilder
				    .fromMethodName(PhotoController.class, "getFile", student.getPhoto(), LocationTag.STUDENTS_STORE_LOC)
				    .build()
				    .toString();

		    student.setPhotoUrl(photoUrl);
	    });
	    modelAndView.addObject("students", students);
        modelAndView.addObject("isArchived", true);
        return modelAndView;
    }

    @GetMapping(value = "/admin/archive/teachers")
    public ModelAndView getArchivedTeachers() {
        ModelAndView modelAndView = new ModelAndView("admin/teacher-list");
	    Set<Teacher> teachers     = teacherService.findAllByArchive(true);
	    teachers.stream().forEach(teacher -> {
		    String photoUrl = MvcUriComponentsBuilder
				    .fromMethodName(PhotoController.class, "getFile", teacher.getPhoto(), LocationTag.TEACHERS_STORE_LOC)
				    .build()
				    .toString();

		    teacher.setPhotoUrl(photoUrl);
	    });
	    modelAndView.addObject("teachers", teachers);
        modelAndView.addObject("isArchived", true);
        return modelAndView;
    }
}
