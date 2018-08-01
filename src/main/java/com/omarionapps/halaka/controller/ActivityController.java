package com.omarionapps.halaka.controller;

import com.omarionapps.halaka.model.*;
import com.omarionapps.halaka.service.*;
import com.omarionapps.halaka.utils.LocationTag;
import com.omarionapps.halaka.utils.StudentStatus;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
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
import java.text.NumberFormat;
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

	private ActivityService     activityService;
	private CountryService      countryService;
	private CourseService       courseService;
	private StorageService      storageService;
	private StudentTrackService studentTrackService;
	private EventService        eventService;

	@Autowired
	public ActivityController(ActivityService activityService, CountryService countryService,
	                          CourseService courseService, StorageService storageService,
	                          StudentTrackService studentTrackService, EventService eventService) {

		this.activityService = activityService;
		this.countryService = countryService;
		this.courseService = courseService;
		this.storageService = storageService;
		this.studentTrackService = studentTrackService;
		this.eventService = eventService;
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
		model.addObject("studentsCount", activityService.mapStudentsCountWithActivityId());
		model.addObject("certificates", activityService.mapCertsCountWithActivityId());
		return model;
	}

	@GetMapping("/admin/activities/activity/{activityId}")
	public ModelAndView getProfileView(@PathVariable(value = "activityId") Integer activityId) {
		ModelAndView modelAndView = new ModelAndView("admin/activity-profile");
		NumberFormat numberFormat = NumberFormat.getNumberInstance();
		numberFormat.setMaximumFractionDigits(2);

		Optional<Activity> optActivity      = activityService.findById(activityId);
		Activity           activity         = optActivity.get();
		Set<Student>       activityStudents = activityService.findStudentsByActivity(activity);

		activity.setTeachers(activityService.findTeachersByActivity(activity));

		if (activityId != 7) {
			long totalStudents = activityService.getTotalStudentsByActivity(activity);
			long totalStudyingStudents = activityService.getTotalStudentsByActivityByStatus(activity, StudentStatus
					                                                                                          .STUDYING);
			long totalFiredStudents = activityService.getTotalStudentsByActivityByStatus(activity, StudentStatus
					                                                                                       .FIRED);
			long totalTempStoppedStudents = activityService.getTotalStudentsByActivityByStatus(activity, StudentStatus
					                                                                                             .TEMP_STOP);
			long totalFinalStoppedStudents = activityService.getTotalStudentsByActivityByStatus(activity, StudentStatus
					                                                                                              .FINAL_STOP);

			Map<String, Map<String, Long>> countryStudents = activityService.getCountryCodeStudentsStatusCountMap(activity);

			//Load activity's logo
			String logoUrl = MvcUriComponentsBuilder
					                 .fromMethodName(PhotoController.class, "getFile", activity.getLogo(), LocationTag.ACTIVITY_STORE_LOC)
					                 .build()
					                 .toString();
			activity.setLogoUrl(logoUrl);

			//get candidates for each course in the activity
			Map<Integer, Map<Country, Set<StudentTrack>>> candidates = activityService
					                                                           .mapCandidatesWithCourse_IdByActivity
							                                                            (activity);
			System.out.println("Candidates: " + candidates.keySet());
			//Load students' photos
			candidates.entrySet().forEach(courseMapEntry -> {
				courseMapEntry.getValue().entrySet().forEach(countryMapEntry -> {
					countryMapEntry.getValue().forEach(track -> {
						String photoUrl = MvcUriComponentsBuilder
								                  .fromMethodName(PhotoController.class, "getFile", track.getStudent().getPhoto(),
										                  LocationTag.STUDENTS_STORE_LOC)
								                  .build()
								                  .toString();

						track.getStudent().setPhotoUrl(photoUrl);
					});
				});
			});

			long   totalCountries         = countryService.getCountryCountByActivity(activity);
			String countriesIncrementRate = numberFormat.format(countryService.getCountriesIncrementRate(30, activity));

			long totalActiveStudents = totalStudyingStudents + totalTempStoppedStudents;
			double activeStudentsRate = studentTrackService.getRateByStatusAndActivity(StudentStatus.STUDYING, 30, activity) +
					                            studentTrackService.getRateByStatusAndActivity(StudentStatus.TEMP_STOP, 30, activity);
			String strActiveStudentsRate = numberFormat.format(activeStudentsRate);

			long   totalWaitingStudents = activityService.getTotalStudentsByActivityByStatus(activity, StudentStatus.WAITING);
			String waitingIncrementRate = numberFormat.format(studentTrackService.getRateByStatusAndActivity(StudentStatus.WAITING, 30, activity));

			long totalCertificates = activityService.findCertificatesByActivity(activity).size();
			long totalCertifiedStudents = activityService.getTotalStudentsByActivityByStatus(activity, StudentStatus
					                                                                                           .CERTIFIED);
			String strCertificatesRate = numberFormat.format(eventService.getCertIncrementRate(LocalDate.now()
			                                                                                            .getYear(),
					activity));

			modelAndView.addObject("activity", activity);

			modelAndView.addObject("totalCountries", totalCountries);
			modelAndView.addObject("countriesIncrementRate", countriesIncrementRate);
			modelAndView.addObject("totalActiveStudents", totalActiveStudents);
			modelAndView.addObject("activeStudentsRate", strActiveStudentsRate);
			modelAndView.addObject("waitingIncrementRate", waitingIncrementRate);
			modelAndView.addObject("totalCertificates", totalCertificates);
			modelAndView.addObject("certificatesRate", strCertificatesRate);

			modelAndView.addObject("mapCounts", countryService.getCountryCodeStudentsCountMapFromStudetns(activityStudents));
			modelAndView.addObject("countryStudents", countryStudents);
			modelAndView.addObject("coursesTimeline", courseService.groupCoursesByDay(activity));
			modelAndView.addObject("candidates", candidates);
			modelAndView.addObject("students", activityStudents);
			modelAndView.addObject("totalStudents", totalStudents);
			modelAndView.addObject("totalStudying", totalStudyingStudents);
			modelAndView.addObject("totalWaiting", totalWaitingStudents);
			modelAndView.addObject("totalCertified", totalCertifiedStudents);
			modelAndView.addObject("totalFired", totalFiredStudents);
			modelAndView.addObject("totalTempStopped", totalTempStoppedStudents);
			modelAndView.addObject("totalFinalStopped", totalFinalStoppedStudents);

			/*
			Set<Student> waitStudents         = activityService.findStudentsByActivityAndStatus(activity, StudentStatus.WAITING);
			Set<Student> studyStudents        = activityService.findStudentsByActivityAndStatus(activity, StudentStatus.STUDYING);
			Set<Student> certifiedStudents    = activityService.findStudentsByActivityAndStatus(activity, StudentStatus.CERTIFIED);
			Set<Student> tempStoppedStudents  = activityService.findStudentsByActivityAndStatus(activity, StudentStatus.TEMP_STOP);
			Set<Student> finalStoppedStudents = activityService.findStudentsByActivityAndStatus(activity, StudentStatus.FINAL_STOP);
			Set<Student> firedStudents        = activityService.findStudentsByActivityAndStatus(activity, StudentStatus.FIRED);
			
			modelAndView.addObject("waitingStudents", waitStudents);
			modelAndView.addObject("studyingStudents", studyStudents);
			modelAndView.addObject("certifiedStudents", certifiedStudents);
			modelAndView.addObject("tempStoppedStudents", tempStoppedStudents);
			modelAndView.addObject("finalStoppedStudents", finalStoppedStudents);
			modelAndView.addObject("firedStudents", firedStudents);
			*/

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

	/**
	 * To add new activity.
	 * Note:-
	 * To make input validation working, BindingResult must be after @Valid object.
	 *
	 * @param activity      to be added
	 * @param bindingResult to activate inpute validation
	 * @param logoFile      the activity logo image file
	 * @param model         to hold any data to view page
	 * @return the path of redirection.
	 */
	@PostMapping("/admin/activities/activity")
	public String addActivity(@Valid Activity activity, BindingResult bindingResult, @RequestParam("logoFile") MultipartFile logoFile, Model model) {
		if (bindingResult.hasErrors()) {
			model.addAttribute("activity", activity);
			return "admin/register-activity";
		}
		if (null == logoFile) {
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
	 * @param activityId    the activity ID to be archived
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
