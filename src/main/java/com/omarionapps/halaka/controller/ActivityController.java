package com.omarionapps.halaka.controller;

import com.omarionapps.halaka.model.Activity;
import com.omarionapps.halaka.model.Course;
import com.omarionapps.halaka.model.Student;
import com.omarionapps.halaka.model.StudentStatus;
import com.omarionapps.halaka.service.*;
import com.omarionapps.halaka.utils.LocationTag;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.mvc.method.annotation.MvcUriComponentsBuilder;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import javax.validation.Valid;
import java.sql.Date;
import java.time.LocalDate;
import java.util.Map;
import java.util.Optional;
import java.util.Set;

/**
 * Created by Omar on 14-May-17.
 */
@Controller
public class ActivityController {
	Logger log = LoggerFactory.getLogger(this.getClass().getName());
	private ActivityService activityService;
	private TeacherService  teacherService;
	private CountryService  countryService;
	private CourseService   courseService;
	private StorageService  storageService;

	@Autowired
	public ActivityController(ActivityService activityService, TeacherService teacherService,
	                          CountryService countryService, CourseService courseService, StorageService storageService) {
		this.activityService = activityService;
		this.teacherService = teacherService;
		this.countryService = countryService;
		this.courseService = courseService;
		this.storageService = storageService;
	}

	@GetMapping("/admin/activities")
	public ModelAndView getAll() {
		ModelAndView model = new ModelAndView("admin/activity-list");
		model.addObject("activities", activityService.findAllByArchived(false));
		model.addObject("students", activityService.getTotalStudents());
		return model;
	}

	@PostMapping("amdin/activities/activity")
	public String addNewActivity(@Valid Activity activity, @RequestParam("image") MultipartFile image) {
		if (image.getOriginalFilename().isEmpty()) {
			System.out.println("Image: avatar5.png");
			activity.setLogo("avatar5.png");
		} else {
			try {
				storageService.store(image, LocationTag.ACTIVITY_STORE_LOC);
				activity.setLogo(image.getOriginalFilename());
			} catch (Exception e) {
				log.error("Fails to Store the image!");
				log.error(e.toString());
			}
		}
		Activity savedActivity = activityService.save(activity);

		return "redirect:/admin/activities/activity" + savedActivity.getId();
	}

	@GetMapping("/admin/activities/activity/{activityId}")
	public ModelAndView getProfileView(@PathVariable(value = "activityId") Integer activityId) {
		ModelAndView       modelAndView     = new ModelAndView("admin/activity-profile");
		Optional<Activity> optActivity      = activityService.findById(activityId);
		Activity           activity         = optActivity.get();
		Set<Student>       activityStudents = activityService.getStudentsByActivity(activity);

		String imagePath = MvcUriComponentsBuilder
				.fromMethodName(PhotoController.class, "getFile", activity.getLogo(), LocationTag.ACTIVITY_STORE_LOC).build().toString();

		System.out.println("activity students: " + activityService.getTeachersByActivity(activity).size());
		System.out.println("Teachers: " + activityService.getTeachersByActivity(activity));
		modelAndView.addObject("imagePath", imagePath);
		modelAndView.addObject("mapCounts", countryService.getCountryCodeStudentsCountMapFromStudetns(activityStudents));

		if (activityId != 7) {
			long                           totalStudents     = activityService.getTotalStudentsByActivity(activity);
			long                           totalStudying     = activityService.getTotalStudentsByStatus(activity, StudentStatus.STUDYING);
			long                           totalWaiting      = activityService.getTotalStudentsByStatus(activity, StudentStatus.WAITING);
			long                           totalCertified    = activityService.getTotalStudentsByStatus(activity, StudentStatus.CERTIFIED);
			long                           totalFired        = activityService.getTotalStudentsByStatus(activity, StudentStatus.FIRED);
			long                           totalTempStopped  = activityService.getTotalStudentsByStatus(activity, StudentStatus.TEMP_STOP);
			long                           totalFinalStopped = activityService.getTotalStudentsByStatus(activity, StudentStatus.FINAL_STOP);
			Map<String, Map<String, Long>> countryStudents   = activityService.getCountryCodeStudentsStatusCountMap(activity);

			int waitingStudentsCount = activityService.getStudentsByActivityByStatus(activity, StudentStatus.WAITING).size();
			int studyingStudentsCount = activityService.getStudentsByActivityByStatus(activity, StudentStatus.STUDYING)
					.size();
			int certifiedStudentsCount = activityService.getStudentsByActivityByStatus(activity, StudentStatus.CERTIFIED)
					.size();
			int tempStoppedStudentsCount = activityService.getStudentsByActivityByStatus(activity, StudentStatus.TEMP_STOP)
					.size();
			int finalStoppedStudentsCount = activityService.getStudentsByActivityByStatus(activity, StudentStatus.STUDYING)
					.size();
			int firedStudentsCount = activityService.getStudentsByActivityByStatus(activity, StudentStatus.FIRED).size();

			Set<Student> waitStudents         = (waitingStudentsCount > 0) ? activityService.getStudentsByActivityByStatus(activity, StudentStatus.WAITING) : null;
			Set<Student> studyStudents        = (studyingStudentsCount > 0) ? activityService.getStudentsByActivityByStatus(activity, StudentStatus.STUDYING) : null;
			Set<Student> certifiedStudents    = (certifiedStudentsCount > 0) ? activityService.getStudentsByActivityByStatus(activity, StudentStatus.CERTIFIED) : null;
			Set<Student> tempStoppedStudents  = (tempStoppedStudentsCount > 0) ? activityService.getStudentsByActivityByStatus(activity, StudentStatus.TEMP_STOP) : null;
			Set<Student> finalStoppedStudents = (finalStoppedStudentsCount > 0) ? activityService.getStudentsByActivityByStatus(activity, StudentStatus.FINAL_STOP) : null;
			Set<Student> firedStudents        = (firedStudentsCount > 0) ? activityService.getStudentsByActivityByStatus(activity, StudentStatus.FIRED) : null;

			modelAndView.addObject("activity", activity);
			modelAndView.addObject("teacherCourses", activityService.getTeacherCoursesByActivity(activity.getId()));
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

	@GetMapping("/admin/activities/activity")
	public ModelAndView getAddActivityView() {
		ModelAndView model = new ModelAndView("admin/register-activity");
		model.addObject("activity", new Activity());
		return model;
	}

	@PostMapping("/admin/activities/activity/{activityId}")
	public String updateActivity(Activity activity, @PathVariable(value = "activityId") Integer activityId, @RequestParam("logoFile") MultipartFile logoFile) {
		Optional<Activity> optActivity = activityService.findById(activity.getId());
		Activity           newActivity = optActivity.get();
		newActivity.setName(activity.getName());
		newActivity.setComments(activity.getComments());


		if (activity.getId() != 0) {
			//newActivity.setTeacher(activityService.getTeachersByActivity(activity));
			newActivity.setCourses(activity.getCourses());
			newActivity.setArchived(activity.isArchived());
		}
		if (null == logoFile) {
			System.out.println("file is null");
			newActivity.setLogo(activity.getLogo());
		} else {
			try {
				storageService.store(logoFile, LocationTag.ACTIVITY_STORE_LOC);
				storageService.deletePhotoByName(activity.getLogo(), LocationTag.ACTIVITY_STORE_LOC);
				System.out.println("Image Logo" + logoFile.getOriginalFilename());
				newActivity.setLogo(logoFile.getOriginalFilename());
			} catch (Exception e) {
				log.error("Fails to Store the image!");
				log.error(e.toString());
			}
		}
		Activity returnedActivity = activityService.save(newActivity);
		System.out.println("Updated Activity:" + returnedActivity);
		return "redirect:/admin/activities/activity/" + returnedActivity.getId();
	}

	@PostMapping("/admin/activities/activity")
	public String addActivity(Activity activity, @RequestParam("logoFile") MultipartFile logoFile) {

		if (null == logoFile) {
			System.out.println("file is null");
			activity.setLogo("");
		} else {
			try {
				storageService.store(logoFile, LocationTag.ACTIVITY_STORE_LOC);
				activity.setLogo(logoFile.getOriginalFilename());
			} catch (Exception e) {
				log.error("Fails to Store the image!");
				log.error(e.toString());
			}
		}
		Activity returnedActivity = activityService.save(activity);
		return "redirect:/admin/activities/activity/" + returnedActivity.getId();
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
		Optional<Activity> optActivity = activityService.findById(id);
		Activity           activity    = optActivity.get();
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
