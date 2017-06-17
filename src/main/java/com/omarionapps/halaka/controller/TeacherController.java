package com.omarionapps.halaka.controller;

import com.omarionapps.halaka.service.TeacherService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.ModelAndView;

/**
 * Created by Omar on 30-Apr-17.
 */
@Controller
public class TeacherController {

    @Autowired
    TeacherService teacherService;

    @GetMapping("/admin/teachers")
    public ModelAndView getTeacherService() {
        ModelAndView modelAndView = new ModelAndView("admin/teacher-list");
        modelAndView.addObject("teachers", teacherService.findAllByOrderByID());
        return modelAndView;
    }

    @GetMapping("/admin/teachers/teacher")
    public ModelAndView getTeacherProfile(@RequestParam(value = "id") int id){
        ModelAndView modelAndView = new ModelAndView("admin/teacher-profile");
        modelAndView.addObject("teacher", teacherService.findOneById(id));
        return modelAndView;
    }
}
