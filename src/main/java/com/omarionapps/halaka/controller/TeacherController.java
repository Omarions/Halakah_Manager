package com.omarionapps.halaka.controller;

import com.omarionapps.halaka.model.Teacher;
import com.omarionapps.halaka.service.ActivityService;
import com.omarionapps.halaka.service.CourseService;
import com.omarionapps.halaka.service.StorageService;
import com.omarionapps.halaka.service.TeacherService;
import com.omarionapps.halaka.utils.LocationTag;
import com.omarionapps.halaka.utils.Utils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BeanPropertyBindingResult;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
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
import java.util.List;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;

/**
 * Created by Omar on 30-Apr-17.
 */
@Controller
public class TeacherController {
	Logger log = LoggerFactory.getLogger(this.getClass());
	@Autowired
	TeacherService  teacherService;
	@Autowired
	ActivityService activityService;
	@Autowired
	CourseService   courseService;
	@Autowired
	private StorageService storageService;

	@GetMapping("/admin/teachers")
	public ModelAndView getTeacherService() {
		ModelAndView modelAndView = new ModelAndView("admin/teacher-list");
		Set<Teacher> teachers     = teacherService.findAllByArchive(false).stream().collect(Collectors.toSet());
		teachers.stream().forEach(teacher -> {
			String photoUrl = MvcUriComponentsBuilder
					.fromMethodName(PhotoController.class, "getFile", teacher.getPhoto(), LocationTag.TEACHERS_STORE_LOC)
					.build()
					.toString();

			teacher.setPhotoUrl(photoUrl);
		});
		modelAndView.addObject("teachers", teachers);
		return modelAndView;
	}

	/**
	 * Process the get request (if id is sent, so get the profile or it means add new teacher)
	 *
	 * @param teacherId the teacher id to display his profile
	 * @return the teacher profile page if id is sent or register teacher page if not sent
	 */
	@GetMapping("/admin/teachers/teacher/{teacherId}")
	public ModelAndView getTeacherProfileView(@PathVariable(value = "teacherId") Integer teacherId) {
		Optional<Teacher> optTeacher   = teacherService.findOneById(teacherId);
		Teacher           teacher      = optTeacher.get();
		ModelAndView      modelAndView = new ModelAndView("admin/teacher-profile");
		String photoUrl = MvcUriComponentsBuilder
				.fromMethodName(PhotoController.class, "getFile", teacher.getPhoto(), LocationTag.TEACHERS_STORE_LOC)
				.build()
				.toString();

		teacher.setPhotoUrl(photoUrl);
		Teacher formTeacher = teacher;
		modelAndView.addObject("teacher", teacher);
		modelAndView.addObject("formTeacher", formTeacher);
		return modelAndView;

	}

	@GetMapping("/admin/teachers/teacher")
	public ModelAndView addTeacher() {
		ModelAndView modelAndView = new ModelAndView("admin/register-teacher");
		modelAndView.addObject("teacher", new Teacher());
		return modelAndView;
	}

	@GetMapping("/admin/teachers/teacher/{teacherId}/archive")
	public String archiveTeacher(@PathVariable(value = "teacherId") int teacherId, RedirectAttributes redirectAttrs) {
		Teacher teacher = teacherService.findOneById(teacherId).get();
		teacher.setArchived(true);
		teacher.setArchivedDate(Date.valueOf(LocalDate.now()));
		Teacher archivedTeacher = teacherService.save(teacher);
		if (archivedTeacher != null)
			redirectAttrs.addFlashAttribute("messageSuccess", "Teacher with ID( " + teacherId + " ) was archived successfully");
		else
			redirectAttrs.addFlashAttribute("messageError", "Archiving teacher with ID( " + teacherId + " ) is failed");

		return "redirect:/admin/teachers";
	}

	@PostMapping("/admin/teachers/teacher/{teacherId}")
	public ModelAndView updateTeacher(@Valid Teacher formTeacher, BindingResult bindingResult, @PathVariable(value = "teacherId") Integer teacherId, @RequestParam("image") MultipartFile image, Model model) {
		ModelAndView      modelAndView  = new ModelAndView("admin/teacher-profile");
		Optional<Teacher> optTeacher    = teacherService.findOneById(teacherId);
		Teacher           updateTeacher = optTeacher.get();
		if (bindingResult.hasErrors()) {
			System.out.println("There are errors!");
			
			List<FieldError> fieldErrorList = bindingResult.getFieldErrors();
			BindingResult    bResult        = new BeanPropertyBindingResult(formTeacher, "formTeacher");
			for (FieldError fe : fieldErrorList) {
				String field = fe.getField();
				bResult.rejectValue(field, field + " is required");
				System.out.println("The " + field + " has error");
			}
			System.out.println(bResult.getFieldError("tel").getRejectedValue().toString());
			//	bindingResult = bResult;
			//modelAndView.addObject("bindingResult", bindingResult);
			String photoUrl     = Utils.buildPhotoUrl(updateTeacher.getPhoto(), LocationTag.TEACHERS_STORE_LOC);
			String formPhotoUrl = Utils.buildPhotoUrl(formTeacher.getPhoto(), LocationTag.TEACHERS_STORE_LOC);
			updateTeacher.setPhotoUrl(photoUrl);
			formTeacher.setPhotoUrl(formPhotoUrl);

			modelAndView.addObject("teacher", updateTeacher);
			modelAndView.addObject("formTeacher", formTeacher);
			return modelAndView;
			//return "redirect:/admin/teachers/teacher/" + teacherId;
		} else {
			//

			updateTeacher.setName(formTeacher.getName());
			updateTeacher.setGender(formTeacher.getGender());
			updateTeacher.setTel(formTeacher.getTel());
			updateTeacher.setHireDate(formTeacher.getHireDate());
			updateTeacher.setEducation(formTeacher.getEducation());
			updateTeacher.setEmail(formTeacher.getEmail());
			updateTeacher.setAddress(formTeacher.getAddress());
			updateTeacher.setJob(formTeacher.getJob());
			updateTeacher.setResume(formTeacher.getResume());
			updateTeacher.setComments(formTeacher.getComments());

			if (null != image || !image.isEmpty()) {
				try {
					storageService.store(image, LocationTag.TEACHERS_STORE_LOC);
					storageService.deletePhotoByName(updateTeacher.getPhoto(), LocationTag.TEACHERS_STORE_LOC);
					updateTeacher.setPhoto(image.getOriginalFilename());
				} catch (Exception e) {
					log.error("Fails to Store the image!");
					log.error(e.toString());
				}
			}
			Teacher returnedTeacher = teacherService.save(updateTeacher);
			String photoUrl = MvcUriComponentsBuilder
					                  .fromMethodName(PhotoController.class, "getFile", updateTeacher.getPhoto(), LocationTag.TEACHERS_STORE_LOC)
					                  .build()
					                  .toString();

			updateTeacher.setPhotoUrl(photoUrl);
			formTeacher.setPhotoUrl(photoUrl);
			modelAndView.addObject("teacher", returnedTeacher);
			modelAndView.addObject("formTeacher", returnedTeacher);
			return modelAndView;
			//return "redirect:/admin/teachers/teacher/" + returnedTeacher.getId();
		}
	}

	@PostMapping("/admin/teachers/teacher")
	public String saveTeacher(@Valid Teacher teacher, BindingResult bindingResult, @RequestParam("image") MultipartFile image) {
		if (bindingResult.hasErrors()) {
		/*	List<FieldError> fieldErrorList = bindingResult.getFieldErrors();
			BindingResult    bResult        = new BeanPropertyBindingResult(teacher, "teacher");
			for (FieldError fe : fieldErrorList) {
				String field = fe.getField();
				bResult.rejectValue(field, field + " is required");
			}
			bindingResult = bResult;*/
			return "admin/register-teacher";
		} else {
			Teacher returnedTeacher = null;
			if (image.getOriginalFilename().isEmpty()) {
				//	System.out.println("Image: avatar5.png");
				teacher.setPhoto("avatar5.png");
			} else {
				try {
					storageService.store(image, LocationTag.TEACHERS_STORE_LOC);
					teacher.setPhoto(image.getOriginalFilename());
				} catch (Exception e) {
					log.error("Fails to Store the image!");
					log.error(e.toString());
				}
			}
			returnedTeacher = teacherService.save(teacher);
			/*for (Course course : teacher.getCourses()) {
				course.setTeacher(teacher);
				courseService.save(course);
			}
*/
			return "redirect:/admin/teachers/teacher/" + returnedTeacher.getId();
		}
	}
}
