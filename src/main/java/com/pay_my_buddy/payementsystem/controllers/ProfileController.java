package com.pay_my_buddy.payementsystem.controllers;

import com.pay_my_buddy.payementsystem.model.User;
import com.pay_my_buddy.payementsystem.service.UserService;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

import java.security.Principal;
import java.util.Optional;

@Controller
@Slf4j
@AllArgsConstructor
public class ProfileController {
    private final UserService userService;

    @GetMapping("/profile")
    public String getProfilePage(Model model, Authentication authentication) {
        String email = authentication.getName();
        Optional<User> user = userService.findUserByEmail(email);
        if (user.isEmpty()) {
            return "redirect:/login";
        }
        {
            model.addAttribute("currentPage", "profile");
            return ("profile");

        }
    }
}
