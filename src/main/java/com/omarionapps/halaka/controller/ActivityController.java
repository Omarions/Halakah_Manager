package com.omarionapps.halaka.controller;

import com.omarionapps.halaka.model.Activity;
import com.omarionapps.halaka.model.Course;
import com.omarionapps.halaka.model.Student;
import com.omarionapps.halaka.model.StudentStatus;
import com.omarionapps.halaka.service.ActivityService;
import com.omarionapps.halaka.service.CountryService;
import com.omarionapps.halaka.service.CourseService;
import com.omarionapps.halaka.service.StorageService;
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

import java.sql.Date;
import java.time.LocalDate;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.Set;

/**
 * Created by Omar on 14-May-17.
 */
@Controller
public class ActivityController {
	private Logger log = LoggerFactory.getLogger(this.getClass().getName());
	private ActivityService activityService;
	private CountryService  countryService;
	private CourseService   courseService;
	private StorageService  storageService;

	@Autowired
	public ActivityController(ActivityService activityService,
	                          CountryService countryService, CourseService courseService, StorageService storageService) {
		this.activityService = activityService;
		this.countryService = countryService;
		this.courseService = courseService;
		this.storageService = storageService;
	}

	@GetMapping("/admin/activities")
	public ModelAndView getAll() {
		ModelAndView   model      = new ModelAndView("admin/activity-list");
		List<Activity> activities = activityService.findAllByArchived(false);
		activities.stream().forEach(activity -> {
			String logoUrl = MvcUriComponentsBuilder
					.fromMethodName(PhotoController.class, "getFile", activity.getLogo(), LocationTag.ACTIVITY_STORE_LOC)
					.build()
					.toString();

			activity.setLogoUrl(logoUrl);
		});
		model.addObject("activities", activities);
		model.addObject("students", activityService.getTotalStudents());
		return model;
	}

	@GetMapping("/admin/activities/activity/{activityId}")
	public ModelAndView getProfileView(@PathVariable(value = "activityId") Integer activityId) {
		ModelAndView       modelAndView     = new ModelAndView("admin/activity-profile");
		Optional<Activity> optActivity      = activityService.findById(activityId);
		Activity           activity         = optActivity.get();
		Set<Student>       activityStudents = activityService.getStudentsByActivity(activity);

		activity.setTeacher(activityService.getTeachersByActivity(activity));

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

			Set<Student> waitStudents         = activityService.getStudentsByActivityByStatus(activity, StudentStatus.WAITING);
			Set<Student> studyStudents        = activityService.getStudentsByActivityByStatus(activity, StudentStatus.STUDYING);
			Set<Student> certifiedStudents    = activityService.getStudentsByActivityByStatus(activity, StudentStatus.CERTIFIED);
			Set<Student> tempStoppedStudents  = activityService.getStudentsByActivityByStatus(activity, StudentStatus.TEMP_STOP);
			Set<Student> finalStoppedStudents = activityService.getStudentsByActivityByStatus(activity, StudentStatus.FINAL_STOP);
			Set<Student> firedStudents        = activityService.getStudentsByActivityByStatus(activity, StudentStatus.FIRED);

			String logoUrl = MvcUriComponentsBuilder
					.fromMethodName(PhotoController.class, "getFile", activity.getLogo(), LocationTag.ACTIVITY_STORE_LOC)
					.build()
					.toString();

			activity.setLogoUrl(logoUrl);

			modelAndView.addObject("activity", activity);
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
		Optional<Activity> optActivity = activityService.findById(activityId);
		if (optActivity.isPresent()) {
			Activity newActivity = optActivity.get();
			newActivity.setName(activity.getName());
			newActivity.setComments(activity.getComments());
			newActivity.setCourses(activity.getCourses());
			newActivity.setArchived(activity.isArchived());

			if (null == logoFile) {
				//	System.out.println("file is null");
				newActivity.setLogo(activity.getLogo());
			} else {
				try {
					storageService.store(logoFile, LocationTag.ACTIVITY_STORE_LOC);
					storageService.deletePhotoByName(activity.getLogo(), LocationTag.ACTIVITY_STORE_LOC);
					newActivity.setLogo(logoFile.getOriginalFilename());
				} catch (Exception e) {
					log.error("Fails to Store the image!");
					log.error(e.toString());
				}
			}
			Activity returnedActivity = activityService.save(newActivity);

			return "redirect:/admin/activities/activity/" + returnedActivity.getId();
		}
		return "redirect:/admin/activities/activity/" + activityId;
	}

	@PostMapping("/admin/activities/activity")
	public String addActivity(Activity activity, @RequestParam("logoFile") MultipartFile logoFile) {

		if (null == logoFile) {
			//System.out.println("file is null");
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
	 * @param activityId            the activity ID to be archived
	 * @param redirectAttrs the message to be send with the link to be directed to after archiving success.
	 * @return the link to be directed to after success.
	 */
	@GetMapping("/admin/activities/activity/{activityId}/archive")
	public String archiveActivity(@PathVariable(value = "activityId") int activityId, RedirectAttributes redirectAttrs) {
		Optional<Activity> optActivity = activityService.findById(activityId);
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
			redirectAttrs.addFlashAttribute("messageSuccess", "Activity with ID( " + activityId + " ) was archived " +
					"successfully");
		} else {
			redirectAttrs.addFlashAttribute("messageError", "An error happens while archiving the activity with ID(" +
					" " +
					activityId + " )");
		}

		return "redirect:/admin/activities";
	}
}
