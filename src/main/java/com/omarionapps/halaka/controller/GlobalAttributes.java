package com.omarionapps.halaka.controller;

import com.omarionapps.halaka.service.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ModelAttribute;

import javax.servlet.ServletContext;

@ControllerAdvice
public class GlobalAttributes {
	@Autowired
	private ServletContext     servletContext;
	@Autowired
	private ActivityService    activityService;
	@Autowired
	private TeacherService     teacherService;
	@Autowired
	private StudentService     studentService;
	@Autowired
	private CourseService      courseService;
	@Autowired
	private CertificateService certificateService;
	@Autowired
	private EventService       eventService;

	private long archivedActivitiesCount;
	private long archivedCoursesCount;
	private long archivedTeachersCount;
	private long archivedStudentsCount;

	private long activeActivitiesCount;
	private long activeCoursesCount;
	private long activeTeachersCount;
	private long activeStudentsCount;
	private long eventsCount;

	private long certificatesCount;

	private long waitStudentsCount;
	private long studyStudentsCount;
	private long certifiedStudentsCount;

	@ModelAttribute
	public void addAttributes(Model model){
		archivedActivitiesCount = activityService.getCountByArchived(true);
		activeActivitiesCount = activityService.getCountByArchived(false);
		archivedCoursesCount = courseService.getCountByArchived(true);
		activeCoursesCount = courseService.getCountByArchived(false);
		archivedTeachersCount = teacherService.getCountByArchived(true);
		activeTeachersCount = teacherService.getCountByArchived(false);
		archivedStudentsCount = studentService.getCountByArchived(true);
		activeStudentsCount = studentService.getCountByArchived(false);
		certificatesCount = certificateService.getCount();
		eventsCount = eventService.getCount();

		model.addAttribute("archivedActivitiesCount", archivedActivitiesCount);
		model.addAttribute("activeActivitiesCount", activeActivitiesCount);
		model.addAttribute("archivedCoursesCount", archivedCoursesCount);
		model.addAttribute("activeCoursesCount", activeCoursesCount);
		model.addAttribute("archivedTeachersCount", archivedTeachersCount);
		model.addAttribute("activeTeachersCount", activeTeachersCount);
		model.addAttribute("archivedStudentsCount", archivedStudentsCount);
		model.addAttribute("activeStudentsCount", activeStudentsCount);
		model.addAttribute("eventsCount", eventsCount);
		model.addAttribute("certificatesCount", certificatesCount);

	}
}
