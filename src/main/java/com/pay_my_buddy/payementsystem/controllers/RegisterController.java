package com.pay_my_buddy.payementsystem.controllers;

import com.pay_my_buddy.payementsystem.model.User;
import com.pay_my_buddy.payementsystem.service.UserService;
import lombok.AllArgsConstructor;
import org.springframework.http.ResponseEntity;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;



@Controller
@AllArgsConstructor
public class RegisterController {
    private UserService userService;

    @GetMapping("/register")
    public String getRegisterPage(Model model) {
        model.addAttribute("user", new User());
        return "register";
    }

    @PostMapping("/register")
    public ResponseEntity registerUser(@ModelAttribute User user) {

        if (userService.createUser(user)) {
            {
                return ResponseEntity.ok().body("User registered successfully");

            }

        }
        return ResponseEntity.badRequest().body("User registration failed");

    }
}
