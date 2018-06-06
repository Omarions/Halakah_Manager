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
import org.springframework.validation.BeanPropertyBindingResult;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import javax.validation.Valid;
import java.sql.Date;
import java.time.LocalDate;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;
import java.util.stream.Stream;

/**
 * Created by Omar on 12-May-17.
 */
@Controller
public class CourseController {
	private CourseService   courseService;
	private ActivityService activityService;
	private CountryService  countryService;
	private TeacherService  teacherService;

	@Autowired
	public CourseController(CourseService courseService, ActivityService activityService, CountryService countryService, TeacherService teacherService) {
		this.courseService = courseService;
		this.activityService = activityService;
		this.countryService = countryService;
		this.teacherService = teacherService;
	}

	/**
	 * Process the get request for courses list
	 *
	 * @return the page of all courses list
	 */
	@GetMapping("/admin/courses")
	public ModelAndView getCourses() {
		ModelAndView modelAndView = new ModelAndView("admin/course-list");
		modelAndView.addObject("courses", courseService.findAllByArchive(false));
		return modelAndView;
	}

	/**
	 * Process the get course request, according to id value return the appropriate page
	 * if it's null it means that the user want to create new course, otherwise it shows the page of the required course.
	 *
	 * @param courseId the request parameter that sent with the request
	 * @return the page of appropriate request based on request param that sent
	 */
	@GetMapping("/admin/courses/course/{courseId}")
	public ModelAndView getCourseProfileView(@PathVariable(value = "courseId") Integer courseId) {
		ModelAndView                   modelAndView      = new ModelAndView("admin/course-profile");
		Course                         course            = courseService.findById(courseId).get();
		Set<Student>                   students          = courseService.getStudentsByCourse(course);
		Map<String, Map<String, Long>> countryStudents   = courseService.getCountryCodeStudentsStatusCountMap(course);
		long                           totalStudents     = course.getStudentTracks().size();
		long                           totalStudying     = courseService.totalStudentsByStatus(course, StudentStatus.STUDYING);
		long                           totalWaiting      = courseService.totalStudentsByStatus(course, StudentStatus.WAITING);
		long                           totalCertified    = courseService.totalStudentsByStatus(course, StudentStatus.CERTIFIED);
		long                           totalFired        = courseService.totalStudentsByStatus(course, StudentStatus.FIRED);
		long                           totalTempStopped  = courseService.totalStudentsByStatus(course, StudentStatus.TEMP_STOP);
		long                           totalFinalStopped = courseService.totalStudentsByStatus(course, StudentStatus.FINAL_STOP);

		modelAndView.addObject("course", course);
		modelAndView.addObject("mapCounts", countryService.getCountryCodeStudentsCountMapFromStudetns(students));
		modelAndView.addObject("countryStudents", countryStudents);
		modelAndView.addObject("studyingStudents", courseService.getStudentsByStatus(course, StudentStatus.STUDYING));
		modelAndView.addObject("waitingStudents", courseService.getStudentsByStatus(course, StudentStatus.WAITING));
		modelAndView.addObject("actTeachers", activityService.getTeachersByActivity(course.getActivity().getId()));
		modelAndView.addObject("totalStudents", totalStudents);
		modelAndView.addObject("totalStudying", totalStudying);
		modelAndView.addObject("totalWaiting", totalWaiting);
		modelAndView.addObject("totalCertified", totalCertified);
		modelAndView.addObject("totalFired", totalFired);
		modelAndView.addObject("totalTempStopped", totalTempStopped);
		modelAndView.addObject("totalFinalStopped", totalFinalStopped);
		modelAndView.addObject("teachers", teacherService.findAllByOrderByName());
		modelAndView.addObject("activities", activityService.findAllByArchived(false));
		

		return modelAndView;
	}

	/**
	 * Process the get course request, according to id value return the appropriate page
	 * if it's null it means that the user want to create new course, otherwise it shows the page of the required course.
	 *
	 * @return the page of appropriate request based on request param that sent
	 */
	@GetMapping("/admin/courses/course")
	public ModelAndView getRegisterCourseView() {
		ModelAndView model = new ModelAndView("admin/register-course");

		model.addObject("course", new Course());
		model.addObject("activities", activityService.findAllOrderByName());
		model.addObject("teachers", teacherService.findAllByOrderByName());
		return model;
	}

	/**
	 * Process the get course request, according to id value return the appropriate page
	 * if it's null it means that the user want to create new course, otherwise it shows the page of the required course.
	 *
	 * @return the page of appropriate request based on request param that sent
	 */
	@GetMapping("/admin/courses/course/activity/{activityId}")
	public ModelAndView getRegisterActivityCourseView(@PathVariable(value = "activityId") Integer activityId) {
		ModelAndView       model       = new ModelAndView("admin/register-course");
		Optional<Activity> optActivity = activityService.findById(activityId);
		Activity           activity    = optActivity.get();
		List<Activity>     activities  = Stream.of(activity).collect(Collectors.toList());
		model.addObject("course", new Course());
		model.addObject("activities", activities);
		model.addObject("teachers", teacherService.findAllByOrderByName());
		return model;
	}

	/**
	 * Send the course to archive
	 *
	 * @param courseId      the course ID to be archived
	 * @param redirectAttrs the message to be send with the link to be directed to after archiving success.
	 * @return the link to be directed to after success.
	 */
	@GetMapping("/admin/courses/course/{courseId}/archive")
	public String archiveCourse(@PathVariable(value = "courseId") int courseId, RedirectAttributes redirectAttrs) {
		Course course = courseService.findById(courseId).get();
		course.setArchived(true);
		course.setArchivedDate(Date.valueOf(LocalDate.now()));
		Course archivedCourse = courseService.save(course);
		if (null != archivedCourse) {
			redirectAttrs.addFlashAttribute("messageSuccess", "Course with ID( " + courseId + " ) was archived " +
					"successfully");
		} else {
			redirectAttrs.addFlashAttribute("messageError", "An error happens while archiving the course with ID( " +
					courseId +
					"" + " )");
		}

		return "redirect:/admin/courses";
	}


	/**
	 * Process the post request of updating existing course or creating new one
	 *
	 * @param course the course to be updated
	 * @return redirect to the saved course profile page
	 */
	@PostMapping("/admin/courses/course")
	public String registerCourse(@Valid Course course, BindingResult bindingResult, Model model) {

		if (bindingResult.hasErrors()) {

			List<FieldError> fErrors = bindingResult.getFieldErrors();
			BindingResult    bResult = new BeanPropertyBindingResult(course, "course");
			for (FieldError error : fErrors) {
				String field = error.getField();
				bResult.rejectValue(field, field + " is required");
			}
			bindingResult = bResult;

			model.addAttribute("activities", activityService.findAllByArchived(false));
			model.addAttribute("teachers", teacherService.findAllByArchive(false));
			model.addAttribute("bindingResult", bindingResult);
			return "admin/register-course";

		} else {
			Course returnedCourse = courseService.save(course);
			return "redirect:/admin/courses/course/" + returnedCourse.getId();
		}


	}


	/**
	 * Process the post request of updating existing course or creating new one
	 *
	 * @param course the course to be updated
	 * @return redirect to the saved course profile page
	 */
	@PostMapping("/admin/courses/course/{courseId}")
	public String updateCourse(@PathVariable(value = "courseId") Integer courseId, Course course, BindingResult
			bindingResult, Model model) {
		ModelAndView modelAndView = new ModelAndView("admin/course-profile");
		if (bindingResult.hasErrors()) {

			List<FieldError> fErrors = bindingResult.getFieldErrors();
			BindingResult    bResult = new BeanPropertyBindingResult(course, "course");
			for (FieldError error : fErrors) {
				String field = error.getField();
				bResult.rejectValue(field, field + " is required");
			}
			bindingResult = bResult;

			model.addAttribute("teachers", teacherService.findAllByArchive(false));
			model.addAttribute("bindingResult", bindingResult);

			return "redirect:/admin/courses/course/" + courseId;

		} else {
			Course updatedCourse = courseService.findById(courseId).get();
			updatedCourse.setName(course.getName());
			updatedCourse.setTeacher(course.getTeacher());
			updatedCourse.setDays(course.getDays());
			updatedCourse.setStartTime(course.getStartTime());
			updatedCourse.setEndTime(course.getEndTime());
			updatedCourse.setStartDate(course.getStartDate());
			updatedCourse.setEndDate(course.getEndDate());

			Course returnedCourse = courseService.save(updatedCourse);

			model.addAttribute("teachers", teacherService.findAllByArchive(false));
			return "redirect:/admin/courses/course/" + returnedCourse.getId();
		}


	}

}
