package com.omarionapps.halaka.controller;

import com.omarionapps.halaka.model.Task;
import com.omarionapps.halaka.repository.UserRepository;
import com.omarionapps.halaka.service.*;
import com.omarionapps.halaka.utils.StudentStatus;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Controller;
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
    public String home() {
        return "admin/index";
    }

    @RequestMapping(value = "/admin/home", method = RequestMethod.GET)
    public ModelAndView getHomeView() {
        ModelAndView modelAndView = new ModelAndView("admin/index");
	    NumberFormat numberFormat = NumberFormat.getNumberInstance();
	    numberFormat.setMaximumFractionDigits(2);

	    double certifiedStudentsPercent = studentService.getStatusPercentage(StudentStatus.CERTIFIED);
	    int totalActiveStudents = studentTrackService.getTotalActiveStudents();
	    long totalWaitingStudents = studentService.getCountByStatus(StudentStatus.WAITING);
	    long totalCertified = studentService.getCountByStatus(StudentStatus.CERTIFIED);

	    double activeStudentsRate = studentTrackService.getActiveStudentsRate(30);
	    
	    String strActiveStudentsRate =  numberFormat.format(activeStudentsRate);
	    String formattedCertPercentage = numberFormat.format(certifiedStudentsPercent);
	    String countriesIncrementRate  = numberFormat.format(countryService.getCountriesIncrementRate(30));
	    String waitingIncrementRate = numberFormat.format(studentTrackService.getRateByStatus(StudentStatus.WAITING,
			    30));
	    String strCertificatesRate = numberFormat.format(eventService.getCertIncrementRate(LocalDate.now().getYear()));
	    
        modelAndView.addObject("user", userService.findUserByUserDetails());
        
	    modelAndView.addObject("countriesIncrementRate", countriesIncrementRate);
	    modelAndView.addObject("totalActiveStudents", totalActiveStudents);
	    modelAndView.addObject("activeStudentsRate", strActiveStudentsRate);
	    modelAndView.addObject("totalWaitingStudents", totalWaitingStudents);
	    modelAndView.addObject("waitingIncrementRate", waitingIncrementRate);
	    modelAndView.addObject("totalCertificates", eventService.getTotalCertificates());
	    modelAndView.addObject("certificatesRate",strCertificatesRate );
	    modelAndView.addObject("totalCertified", totalCertified);

	    modelAndView.addObject("certifiedStudentsPercent", formattedCertPercentage);

	    modelAndView.addObject("mapCounts", countryService.getAllCountryCodeStudentsCountMap());
	    modelAndView.addObject("countryStudentsChart", countryService.getCountryStudentsCountByGenderMap());
        modelAndView.addObject("houseOccupied", houseService.getTotalOccupied());
        modelAndView.addObject("houseFree", houseService.getTotalFree());
        modelAndView.addObject("houseMaxCapacity", houseService.getTotalCapacity());
	    // modelAndView.addObject("housesOccupy", houseService.findAllOrderById());

        modelAndView.addObject("task", new Task());
        
        return modelAndView;
    }
}
