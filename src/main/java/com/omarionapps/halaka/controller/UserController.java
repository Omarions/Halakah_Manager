package com.omarionapps.halaka.controller;

import com.omarionapps.halaka.model.Task;
import com.omarionapps.halaka.model.User;
import com.omarionapps.halaka.service.TaskService;
import com.omarionapps.halaka.service.UserServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BeanPropertyBindingResult;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

import javax.validation.Valid;

/**
 * Created by Omar on 24-Jun-17.
 */
@Controller
public class UserController {

    @Autowired
    private UserServiceImpl userService;
    @Autowired
    private TaskService taskService;

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

    @PostMapping("/admin/user/tasks/task")
    public String createTask(@Valid Task task, BindingResult bindingResult, Model model) {
        if (bindingResult.hasErrors()) {
            BindingResult br = new BeanPropertyBindingResult(task, "task");
            return "";
        } else {
            Task savedTask = null;
            savedTask = taskService.save(task);
            model.addAttribute("task", savedTask);
            return "redirect:/admin/home";
        }

    }
}
