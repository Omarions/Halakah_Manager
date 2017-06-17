package com.omarionapps.halaka.controller;

import com.omarionapps.halaka.model.Student;
import com.omarionapps.halaka.service.ActivityService;
import com.omarionapps.halaka.service.CountryService;
import com.omarionapps.halaka.service.CourseService;
import com.omarionapps.halaka.service.StudentService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;

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
    public StudentController(StudentService studentService, ActivityService activityService, CourseService courseService, CountryService countryService){
        this.studentService = studentService;
        this.activityService = activityService;
        this.courseService = courseService;
        this.countryService = countryService;
    }

    @GetMapping("/admin/students")
    public ModelAndView getStudentsList(){
        ModelAndView model = new ModelAndView("admin/student-list");
        Iterable<Student> students = studentService.getAll();
        model.addObject("students", students);

        return model;
    }

    @GetMapping("/admin/students/student")
    public ModelAndView getStudent(@RequestParam(value="id") int id){
        ModelAndView modelAndView = new ModelAndView("admin/student-profile");
        Student student = studentService.getById(id);

        modelAndView.addObject("student", student);
        return modelAndView;
    }

    @PostMapping("/admin/students/student")
    public String registerStudent(@ModelAttribute Student student){

        studentService.save(student);
        return "admin/student-list";
    }


}
