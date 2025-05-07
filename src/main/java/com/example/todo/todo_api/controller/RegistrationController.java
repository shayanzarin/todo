package com.example.todo.todo_api.controller;

import java.util.Collections;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.ui.Model;
import jakarta.validation.Valid;


import com.example.todo.todo_api.model.User;
import com.example.todo.todo_api.service.UserService;




@Controller
public class RegistrationController {
    private final UserService userService;
    
    private static final Logger logger = LoggerFactory.getLogger(RegistrationController.class);


    @Autowired
    public RegistrationController(UserService userService) {
        this.userService = userService;
    }

    @GetMapping("/register")
    public String showRegistrationForm(Model model) {
        model.addAttribute("user", new User());
        return "register";
    }

    @PostMapping("/register")
    public String registerUser(@Valid @ModelAttribute("user") User user, BindingResult result) {
        if (result.hasErrors()) {
            return "register";
        }
        user.setRoles(Collections.singleton("ROLE_USER"));
        userService.save(user);
        return "redirect:/login";
    }
    
    @GetMapping("/login")
    public String showLoginForm() {
        logger.info("Received GET request for /login");
        return "login";
    }


}
