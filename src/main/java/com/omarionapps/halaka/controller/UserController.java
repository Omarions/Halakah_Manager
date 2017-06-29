package com.omarionapps.halaka.controller;

import com.omarionapps.halaka.model.User;
import com.omarionapps.halaka.service.UserServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

/**
 * Created by Omar on 24-Jun-17.
 */
@Controller
public class UserController {

    @Autowired
    private UserServiceImpl userService;

    @GetMapping(value = "/admin/user/updateStatus")
    public String updateStatus(@RequestParam(value = "email") String email, @RequestParam(value = "userStatus") String userStatus) {
        System.out.println("UserEmail: " + email + ", new Status: " + userStatus);
        User user = userService.findUserByEmail(email);
        user.setName(user.getName());
        user.setRoles(user.getRoles());
        user.setEmail(user.getEmail());
        user.setPassword(user.getPassword());
        switch (userStatus) {
            case "online":
                user.setStatus(true);
                break;
            case "offline":
                user.setStatus(false);
                break;
        }

        userService.saveUser(user);
        return "redirect:/admin/home";
    }
}
