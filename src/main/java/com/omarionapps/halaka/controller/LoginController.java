package com.omarionapps.halaka.controller;

import com.omarionapps.halaka.service.UserServiceImpl;
import org.apache.catalina.servlet4preview.http.HttpServletRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.session.ExpiringSession;
import org.springframework.session.FindByIndexNameSessionRepository;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletResponse;

/**
 * Created by Omar on 22-Apr-17.
 */
@Controller
public class LoginController {
    @Autowired
    FindByIndexNameSessionRepository<? extends ExpiringSession> sessions;

    @Autowired
    private UserServiceImpl userService;

    @RequestMapping(value = {"/", "/login"}, method = RequestMethod.GET)
    public ModelAndView login(@RequestParam(value = "logout",	required = false) String logout,
                              HttpServletRequest request,
                              HttpServletResponse response){

        ModelAndView modelAndView = new ModelAndView();
        if(logout != null) {
            Authentication auth = SecurityContextHolder.getContext().getAuthentication();
            if (auth != null){
               //new SecurityContextLogoutHandler().logout(request, response, auth);
                Object principal = auth.getPrincipal();
                System.out.println("Principal: " + principal);
                String userEmail = null;
                if(principal instanceof UserDetails){

                    userEmail = ((UserDetails)principal).getUsername();
                    modelAndView.addObject("userEmail", userEmail);
                    System.out.println(principal);

                }

                modelAndView.setViewName("auth/lockscreen");
            }

        }else {

            modelAndView.setViewName("auth/login");
        }
        return modelAndView;
    }



}
