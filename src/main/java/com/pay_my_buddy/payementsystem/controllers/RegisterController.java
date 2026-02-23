package com.pay_my_buddy.payementsystem.controllers;

import com.pay_my_buddy.payementsystem.DTO.RegisterDTO;
import com.pay_my_buddy.payementsystem.model.User;
import com.pay_my_buddy.payementsystem.service.UserService;
import jakarta.validation.Valid;
import lombok.AllArgsConstructor;
import org.springframework.context.support.DefaultMessageSourceResolvable;
import org.springframework.http.ResponseEntity;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.util.List;


@Controller
@AllArgsConstructor
public class RegisterController {
    private UserService userService;

    @GetMapping("/register")
    public String getRegisterPage(Model model) {
        model.addAttribute("registerDTO", new RegisterDTO());
        return "register";
    }

    @PostMapping("/register")
    public String registerUser(@Valid @ModelAttribute RegisterDTO registerDTO, BindingResult bindingResult,
                               RedirectAttributes redirectAttributes,Model model) {

        if (bindingResult.hasErrors()) {
            final List<String> errors = bindingResult.getAllErrors()
                    .stream().map(DefaultMessageSourceResolvable::getDefaultMessage)
                    .toList();
            redirectAttributes.addAttribute("errors", errors);
            return "register";
        }

        try {
            userService.createUser(registerDTO.getUsername(), registerDTO.getEmail(), registerDTO.getPassword());

            redirectAttributes.addFlashAttribute("success",
                    "Compte créé avec succès !");

            return "redirect:/login";

        } catch (Exception exception) {
            final List<String> errors = List.of(exception.getMessage());
            model.addAttribute("errors", errors);
            return "register";
        }
    }
}

