package com.omarionapps.halaka.com.omarionapps.halaka.config;

import com.omarionapps.halaka.service.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import javax.servlet.ServletContext;

/**
 * Created by Omar on 05-Jul-17.
 */
@Component
public class ApplicationAttributes {
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

    @Autowired
    public void setArchivedActivitiesCount() {
        archivedActivitiesCount = activityService.getCountByArchived(true);
        servletContext.setAttribute("archivedActivitiesCount", archivedActivitiesCount);
    }

    @Autowired
    public void setActiveActivitiesCount() {
        activeActivitiesCount = activityService.getCountByArchived(false);
        servletContext.setAttribute("activeActivitiesCount", activeActivitiesCount);
    }

    @Autowired
    public void setArchivedCoursesCount() {
        archivedCoursesCount = courseService.getCountByArchived(true);
        servletContext.setAttribute("archivedCoursesCount", archivedCoursesCount);
    }

    @Autowired
    public void setActiveCoursesCount() {
        activeCoursesCount = courseService.getCountByArchived(false);
        servletContext.setAttribute("activeCoursesCount", activeCoursesCount);
    }

    @Autowired
    public void setArchivedTeachersCount() {
        archivedTeachersCount = teacherService.getCountByArchived(true);
        servletContext.setAttribute("archivedTeachersCount", archivedTeachersCount);
    }

    @Autowired
    public void setActiveTeachersCount() {
        activeTeachersCount = teacherService.getCountByArchived(false);
        servletContext.setAttribute("activeTeachersCount", activeTeachersCount);
    }

    @Autowired
    public void setArchivedStudentsCount() {
        archivedStudentsCount = studentService.getCountByArchived(true);
        servletContext.setAttribute("archivedStudentsCount", archivedStudentsCount);
    }

    @Autowired
    public void setActiveStudentsCount() {
        activeStudentsCount = studentService.getCountByArchived(false);
        servletContext.setAttribute("activeStudentsCount", activeStudentsCount);
    }

	@Autowired
	public void setEventsCount() {
		eventsCount = eventService.getCount();
		servletContext.setAttribute("eventsCount", eventsCount);
	}
    
    @Autowired
    public void setCertificatesCount() {
        certificatesCount = certificateService.getCount();
        servletContext.setAttribute("certificatesCount", certificatesCount);
    }


    /*
    @Autowired
    public void setWaitStudentsCount(){
        waitStudentsCount =  studentService.getCountByStatus(StudentStatus.WAITING, false);
        servletContext.setAttribute("waitStudentsCount", waitStudentsCount);
    }

    @Autowired
    public void setStudyStudentsCount(){
        studyStudentsCount =  studentService.getCountByStatus(StudentStatus.STUDYING, false);
        servletContext.setAttribute("studyStudentsCount", studyStudentsCount);
    }

    @Autowired
    public void setCertifiedstudentsCount(){
        certifiedStudentsCount =  studentService.getCountByStatus(StudentStatus.CERTIFIED, false);
        servletContext.setAttribute("certifiedStudentsCount", certifiedStudentsCount);
    }
    */
}
