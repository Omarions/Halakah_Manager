package com.omarionapps.halaka.controller;

import com.omarionapps.halaka.service.ActivityService;
import com.omarionapps.halaka.service.CourseService;
import com.omarionapps.halaka.service.StudentService;
import com.omarionapps.halaka.service.TeacherService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.servlet.ModelAndView;

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
        ModelAndView modelAndView = new ModelAndView("admin/activity-list");
        modelAndView.addObject("activities", activityService.findAllByArchived(true));
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
        modelAndView.addObject("students", studentService.findAllByArchive(true));
        modelAndView.addObject("isArchived", true);
        return modelAndView;
    }

    @GetMapping(value = "/admin/archive/teachers")
    public ModelAndView getArchivedTeachers() {
        ModelAndView modelAndView = new ModelAndView("admin/teacher-list");
        modelAndView.addObject("teachers", teacherService.findAllByArchive(true));
        modelAndView.addObject("isArchived", true);
        return modelAndView;
    }
}
