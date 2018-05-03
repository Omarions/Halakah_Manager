package com.omarionapps.halaka.controller;

import com.omarionapps.halaka.model.Course;
import com.omarionapps.halaka.model.Teacher;
import com.omarionapps.halaka.service.ActivityService;
import com.omarionapps.halaka.service.CourseService;
import com.omarionapps.halaka.service.TeacherService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BeanPropertyBindingResult;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import javax.validation.Valid;
import java.sql.Date;
import java.time.LocalDate;
import java.util.List;

/**
 * Created by Omar on 30-Apr-17.
 */
@Controller
public class TeacherController {

    @Autowired
    TeacherService teacherService;
    @Autowired
    ActivityService activityService;
    @Autowired
    CourseService courseService;

    @GetMapping("/admin/teachers")
    public ModelAndView getTeacherService() {
        ModelAndView modelAndView = new ModelAndView("admin/teacher-list");
        modelAndView.addObject("teachers", teacherService.findAllByArchive(false));
        return modelAndView;
    }

    /**
     * Process the get request (if id is sent, so get the profile or it means add new teacher)
     *
     * @param id the teacher id to display his profile
     * @return the teacher profile page if id is sent or register teacher page if not sent
     */
    @GetMapping("/admin/teachers/teacher")
    public ModelAndView processTeacher(@RequestParam(value = "id", required = false) Integer id) {
        if (id != null)
            return getTeacher(id);
        else
            return addTeacher();
    }

    private ModelAndView getTeacher(int id){
        ModelAndView modelAndView = new ModelAndView("admin/teacher-profile");
        modelAndView.addObject("teacher", teacherService.findOneById(id));
        modelAndView.addObject("activities", activityService.findAllOrderByName());
        return modelAndView;
    }

    private ModelAndView addTeacher() {
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

    @PostMapping("/admin/teachers/teacher")
    public String saveTeacher(@Valid Teacher teacher, BindingResult bindingResult, Model model) {
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

            return "redirect:/admin/teachers/teacher?id=" + returnedTeacher.getId();
        }
    }
}
