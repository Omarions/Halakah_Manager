package com.omarionapps.halaka.controller;

import com.omarionapps.halaka.model.Course;
import com.omarionapps.halaka.model.Teacher;
import com.omarionapps.halaka.service.ActivityService;
import com.omarionapps.halaka.service.CourseService;
import com.omarionapps.halaka.service.StorageService;
import com.omarionapps.halaka.service.TeacherService;
import com.omarionapps.halaka.utils.LocationTag;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
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

/**
 * Created by Omar on 30-Apr-17.
 */
@Controller
public class TeacherController {
	Logger log = LoggerFactory.getLogger(this.getClass());
    @Autowired
    TeacherService teacherService;
    @Autowired
    ActivityService activityService;
    @Autowired
    CourseService courseService;
	@Autowired
	private StorageService storageService;

    @GetMapping("/admin/teachers")
    public ModelAndView getTeacherService() {
        ModelAndView modelAndView = new ModelAndView("admin/teacher-list");
        modelAndView.addObject("teachers", teacherService.findAllByArchive(false));
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
	    Optional<Teacher> optTeacher = teacherService.findOneById(teacherId);
	    Teacher           teacher    = optTeacher.get();
        ModelAndView modelAndView = new ModelAndView("admin/teacher-profile");
	    String imagePath = MvcUriComponentsBuilder
			    .fromMethodName(PhotoController.class, "getFile", teacher.getPhoto(), LocationTag.TEACHERS_STORE_LOC)
			    .build()
			    .toString();


	    modelAndView.addObject("imagePath", imagePath);
	    modelAndView.addObject("teacher", teacher);
        modelAndView.addObject("activities", activityService.findAllOrderByName());
        return modelAndView;

    }

	@GetMapping("/admin/teachers/teacher")
	public ModelAndView addTeacher() {
        ModelAndView modelAndView = new ModelAndView("admin/register-teacher");
        modelAndView.addObject("teacher", new Teacher());
        modelAndView.addObject("activities", activityService.findAllOrderByName());
        modelAndView.addObject("courses", activityService.getActivityCoursesMap());
        return modelAndView;
    }

    @GetMapping("/admin/teachers/teacher/archive")
    public String archiveTeacher(@RequestParam(value = "id") int id, RedirectAttributes redirectAttrs) {
	    Teacher teacher = teacherService.findOneById(id).get();
        teacher.setArchived(true);
        teacher.setArchivedDate(Date.valueOf(LocalDate.now()));
        Teacher archivedTeacher = teacherService.save(teacher);
        if (archivedTeacher != null)
            redirectAttrs.addFlashAttribute("message", "Teacher with ID( " + id + " ) was archived successfully");
        else
            redirectAttrs.addFlashAttribute("message", "Archiving teacher with ID( " + id + " ) is failed");

        return "redirect:/admin/teachers";
    }

	@PostMapping("/admin/teachers/teacher/{teacherId}")
	public String updateTeacher(@Valid Teacher teacher, @PathVariable(value = "teacherId") Integer teacherId, MultipartFile image, BindingResult bindingResult) {
		Optional<Teacher> optTeacher = teacherService.findOneById(teacherId);
		if (bindingResult.hasErrors()) {
			List<FieldError> fieldErrorList = bindingResult.getFieldErrors();
			BindingResult    bResult        = new BeanPropertyBindingResult(teacher, "teacher");
			for (FieldError fe : fieldErrorList) {
				String field = fe.getField();
				bResult.rejectValue(field, field + " is required");
			}
			bindingResult = bResult;
			return "redirect:/admin/teachers/teacher/" + teacher.getId();
		} else {
			Teacher updateTeacher = optTeacher.get();
			updateTeacher.setName(teacher.getName());
			updateTeacher.setTel(teacher.getTel());
			updateTeacher.setHireDate(teacher.getHireDate());
			updateTeacher.setActivity(teacher.getActivity());
			updateTeacher.setCourse(teacher.getCourse());
			updateTeacher.setEducation(teacher.getEducation());
			updateTeacher.setEmail(teacher.getEmail());
			updateTeacher.setJob(teacher.getJob());
			updateTeacher.setComments(teacher.getComments());

			for (Course course : teacher.getCourse()) {
				course.setTeacher(updateTeacher);
				courseService.save(course);
			}

			if (null == image) {
				System.out.println("file is null");
				updateTeacher.setPhoto(updateTeacher.getPhoto());
			} else {
				try {
					storageService.store(image, LocationTag.STUDENTS_STORE_LOC);
					storageService.deletePhotoByName(updateTeacher.getPhoto(), LocationTag.TEACHERS_STORE_LOC);
					updateTeacher.setPhoto(image.getOriginalFilename());
				} catch (Exception e) {
					log.error("Fails to Store the image!");
					log.error(e.toString());
				}
			}
			Teacher returnedTeacher = teacherService.save(updateTeacher);
			return "redirect:/admin/teachers/teacher/" + returnedTeacher.getId();
		}
	}
    @PostMapping("/admin/teachers/teacher")
    public String saveTeacher(@Valid Teacher teacher, BindingResult bindingResult) {
        if (bindingResult.hasErrors()) {
            List<FieldError> fieldErrorList = bindingResult.getFieldErrors();
            BindingResult bResult = new BeanPropertyBindingResult(teacher, "teacher");
            for (FieldError fe : fieldErrorList) {
                String field = fe.getField();
                bResult.rejectValue(field, field + " is required");
            }
            bindingResult = bResult;
            return (teacher.getId() == 0) ? "admin/register-teacher" : "redirect:/admin/teachers/teacher?id" + teacher.getId();
        } else {
            Teacher returnedTeacher = null;
            returnedTeacher = teacherService.save(teacher);
            for (Course course : teacher.getCourse()) {
                course.setTeacher(teacher);
                courseService.save(course);
            }

	        return "redirect:/admin/teachers/teacher/" + returnedTeacher.getId();
        }
    }
}
