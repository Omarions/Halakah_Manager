package com.omarionapps.halaka.controller;

import com.omarionapps.halaka.model.Task;
import com.omarionapps.halaka.repository.UserRepository;
import com.omarionapps.halaka.service.*;
import com.omarionapps.halaka.utils.StudentStatus;
import org.apache.catalina.servlet4preview.http.HttpServletRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.SessionAttributes;
import org.springframework.web.servlet.ModelAndView;

import java.text.NumberFormat;
import java.time.LocalDate;

/**
 * Created by Omar on 09-Apr-17.
 */
@Controller
@SessionAttributes("user")
public class HomeController {

	@Autowired
	UserRepository      userRepository;
	@Autowired
	CountryService      countryService;
	@Autowired
	HouseService        houseService;
	@Autowired
	UserService         userService;
	@Autowired
	StudentService      studentService;
	@Autowired
	StudentTrackService studentTrackService;
	@Autowired
	EventService        eventService;
	@Autowired
	PasswordEncoder     passwordEncoder;

    @RequestMapping("/")
    public String home(Model model) {

        return "admin/index";
    }

    @RequestMapping(value = "/admin/home", method = RequestMethod.GET)
    public ModelAndView home(HttpServletRequest request) {
        ModelAndView modelAndView = new ModelAndView();
	    NumberFormat numberFormat = NumberFormat.getNumberInstance();
	    numberFormat.setMaximumFractionDigits(2);

	    double certifiedStudentsPercent = (Double.valueOf(studentService.getCountByStatus(StudentStatus.CERTIFIED)) /
			                                       Double.valueOf(studentService.getTotalStudents())) * 100;
	    
	    String formattedCertPercentage = numberFormat.format(certifiedStudentsPercent);
	    String countriesIncrementRate  = numberFormat.format(countryService.getCountriesIncrementRate(30));
	    String waitingIncrementRate = numberFormat.format(studentTrackService.getRateByStatus(StudentStatus.WAITING,
			    30));

	    int totalActiveStudents = studentTrackService.findAllByStatus(StudentStatus.STUDYING).size() + studentTrackService.findAllByStatus(StudentStatus.TEMP_STOP).size();

	    double activeStudentsRate = studentTrackService.getRateByStatus(StudentStatus.STUDYING, 30) +
			                                studentTrackService.getRateByStatus(StudentStatus.TEMP_STOP, 30);

	    long totalCertified = studentService.getCountByStatus(StudentStatus.CERTIFIED);
        modelAndView.addObject("user", userService.findUserByUserDetails());
        modelAndView.addObject("countryStudentsChart", countryService.getCountryStudentsCountByGenderMap());
	    modelAndView.addObject("countriesIncrementRate", countriesIncrementRate);
	    modelAndView.addObject("totalActiveStudents", totalActiveStudents);
	    modelAndView.addObject("activeStudentsRate", numberFormat.format(activeStudentsRate));
	    modelAndView.addObject("totalWaitingStudents", studentTrackService.findAllByStatus(StudentStatus.WAITING).size());
	    modelAndView.addObject("waitingIncrementRate", waitingIncrementRate);
	    modelAndView.addObject("totalCertificates", eventService.getTotalCertificates());
	    modelAndView.addObject("totalCertified", totalCertified);
	    modelAndView.addObject("certificatesRate", numberFormat.format(eventService.getCertIncrementRate(LocalDate.now().getYear())));
        modelAndView.addObject("houseOccupied", houseService.getTotalOccupied());
        modelAndView.addObject("houseFree", houseService.getTotalFree());
        modelAndView.addObject("houseMaxCapacity", houseService.getTotalCapacity());
	    // modelAndView.addObject("housesOccupy", houseService.findAllOrderById());
        modelAndView.addObject("mapCounts", countryService.getAllCountryCodeStudentsCountMap());
	    modelAndView.addObject("certifiedStudentsPercent", formattedCertPercentage);
        modelAndView.addObject("task", new Task());
        modelAndView.setViewName("admin/index");


        return modelAndView;
    }
}
