package com.omarionapps.halaka.controller;

import com.omarionapps.halaka.service.ActivityService;
import com.omarionapps.halaka.service.CourseService;
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

    public ArchiveController(ActivityService activityService, CourseService courseService) {
        this.activityService = activityService;
        this.courseService = courseService;
    }

    @GetMapping(value = "/admin/archive/activities")
    public ModelAndView getArchivedActivities() {
        ModelAndView modelAndView = new ModelAndView("admin/activity-list");
        modelAndView.addObject("activities", activityService.findAllByArchived(true));
        modelAndView.addObject("students", activityService.getTotalStudents());

        return modelAndView;
    }

    @GetMapping(value = "/admin/archive/courses")
    public ModelAndView getArchivedCourses() {
        ModelAndView modelAndView = new ModelAndView("admin/course-list");
        modelAndView.addObject("courses", courseService.findAllByArchive(true));
        return modelAndView;
    }
}
