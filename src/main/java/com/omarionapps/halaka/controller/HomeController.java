package com.omarionapps.halaka.controller;

import com.omarionapps.halaka.model.StudentStatus;
import com.omarionapps.halaka.repository.UserRepository;
import com.omarionapps.halaka.service.*;
import org.apache.catalina.servlet4preview.http.HttpServletRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.SessionAttributes;
import org.springframework.web.servlet.ModelAndView;

/**
 * Created by Omar on 09-Apr-17.
 */
@Controller
@SessionAttributes("user")
public class HomeController {

    @Autowired
    UserRepository userRepository;
    @Autowired
    StudentService studentService;
    @Autowired
    CountryService countryService;
    @Autowired
    HouseService houseService;
    @Autowired
    ActivityService activityService;
    @Autowired
    TeacherService teacherService;
    @Autowired
    UserService userService;

    @RequestMapping("/")
    public String home(Model model){
        return "admin/index";
    }

    @RequestMapping(value="/admin/home", method = RequestMethod.GET)
    public ModelAndView home(HttpServletRequest request){
        ModelAndView modelAndView = new ModelAndView();
        modelAndView.addObject("user", userService.findUserByUserDetails());
        modelAndView.addObject("houseOccupied", houseService.getTotalOccupied());
        modelAndView.addObject("houseFree", houseService.getTotalFree());
        modelAndView.addObject("houseMaxCapacity", houseService.getTotalCapacity());
        //modelAndView.addObject("housesOccupy", houseService.findAllOrderById());
        modelAndView.addObject("mapCounts", countryService.getAllCountryCodeStudentsCountMap());
        modelAndView.addObject("totalWaits", studentService.getCountByStatus(StudentStatus.WAITING));
        modelAndView.addObject("totalStudy", studentService.getCountByStatus(StudentStatus.STUDYING));
        modelAndView.addObject("totalCertified", studentService.getCountByStatus(StudentStatus.CERTIFIED));
        modelAndView.addObject("activitiesCount", activityService.getCount());
        modelAndView.addObject("teachersCount", teacherService.getCount());
        modelAndView.setViewName("admin/index");
        return modelAndView;
    }
}
