package com.omarionapps.halaka.controller;

import com.omarionapps.halaka.model.Student;
import com.omarionapps.halaka.model.StudentTrack;
import com.omarionapps.halaka.service.ActivityService;
import com.omarionapps.halaka.service.CountryService;
import com.omarionapps.halaka.service.CourseService;
import com.omarionapps.halaka.service.StudentService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
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
 * Created by Omar on 09-Apr-17.
 */
@Controller
public class StudentController {
    Logger logger = LoggerFactory.getLogger(StudentController.class);
    private StudentService studentService;
    private ActivityService activityService;
    private CourseService courseService;
    private CountryService countryService;

    @Autowired
    public StudentController(StudentService studentService, ActivityService activityService, CourseService courseService, CountryService countryService) {
        this.studentService = studentService;
        this.activityService = activityService;
        this.courseService = courseService;
        this.countryService = countryService;
    }

    @GetMapping("/admin/students")
    public ModelAndView getStudentsList() {
        ModelAndView model = new ModelAndView("admin/student-list");
        Iterable<Student> students = studentService.getAll();
        model.addObject("students", students);

        return model;
    }

    @GetMapping("/admin/students/student")
    public ModelAndView processStudentRequest(@RequestParam(value = "id", required = false) Integer id) {
        return (null == id) ? addStudent() : getStudent(id);
    }

    /**
     * Create model for add course page
     *
     * @return model of page with its attributes
     */
    private ModelAndView addStudent() {
        ModelAndView model = new ModelAndView("admin/register-student");

        model.addObject("student", new Student());
        model.addObject("studentTrack", new StudentTrack());
        model.addObject("countries", countryService.findAllByOrderByEnglishNameAsc());
        return model;
    }

    /**
     * Create the page of details of a specific course
     *
     * @param id the id of required course to get its details
     * @return model of the page with its details of the course.
     */
    private ModelAndView getStudent(int id) {
        ModelAndView modelAndView = new ModelAndView("admin/student-profile");
        Student student = studentService.getById(id);

        modelAndView.addObject("student", student);
        modelAndView.addObject("countries", countryService.findAllByOrderByEnglishNameAsc());
        return modelAndView;
    }

    /**
     * Process the get request of delete operation
     *
     * @param id            the course id to be deleted
     * @param redirectAttrs to send message of success or failure of the operation
     * @return redirect to the same page of courses list with new info and delete operation result message
     */
    @GetMapping("/admin/students/student/archive")
    public String deleteCourse(@RequestParam(value = "id") int id, RedirectAttributes redirectAttrs) {
        Student student = studentService.getById(id);
        student.setArchived(true);
        student.setArchivedDate(Date.valueOf(LocalDate.now()));
        studentService.save(student);
        redirectAttrs.addFlashAttribute("message", "Student with ID( " + id + " ) was archived successfully");
        return "redirect:/admin/students";
    }

    /**
     * Process the post request of updating existing course or creating new one
     *
     * @param student the student to be updated or created
     * @return redirect to the saved student profile page
     */
    @PostMapping("/admin/students/student")
    public String saveStudent(@Valid Student student, @Valid StudentTrack studentTrack, BindingResult bindingResult, Model model) {

        if (bindingResult.hasErrors()) {

            List<FieldError> fErrors = bindingResult.getFieldErrors();
            BindingResult bResultStudent = new BeanPropertyBindingResult(student, "student");

            for (FieldError error : fErrors) {
                System.out.println("Error message: " + error);
                String field = error.getField();
                System.out.println("Field : " + field + ", Error Code: " + error.getCode());

                bResultStudent.rejectValue(field, field + " is required");

            }

            return (student.getId() == 0) ? "admin/register-student" : "redirect:/admin/students/student?id=" + student.getId();

        } else {
            Student returnedStudent = null;
            System.out.println(student);
            returnedStudent = studentService.save(student);
            return "redirect:/admin/students/student?id=" + returnedStudent.getId();
        }

    }
}
