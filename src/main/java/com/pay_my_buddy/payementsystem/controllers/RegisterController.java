package com.pay_my_buddy.payementsystem.controllers;

import com.pay_my_buddy.payementsystem.DTO.RegisterDTO;
import com.pay_my_buddy.payementsystem.service.UserService;
import jakarta.validation.Valid;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.support.DefaultMessageSourceResolvable;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.util.List;

/**
 * Controller for handling user registration.
 */
@Controller
@AllArgsConstructor
@Slf4j
public class RegisterController {
    private UserService userService;

    /**
     * Displays the registration page.
     *
     * @param model the model to hold attributes for the view
     * @return the name of the registration view
     */
    @GetMapping("/register")
    public String getRegisterPage(Model model) {
        log.info("Accessing registration page");
        model.addAttribute("registerDTO", new RegisterDTO());
        return "register";
    }

    /**
     * Handles the user registration process.
     *
     * @param registerDTO        the data transfer object containing registration details
     * @param bindingResult      the result of validation checks on the registration data
     * @param redirectAttributes attributes for flash messages during redirection
     * @param model              the model to hold attributes for the view in case of errors
     * @return a redirect to the login page on success, or the registration view on failure
     */
    @PostMapping("/register")
    public String registerUser(@Valid @ModelAttribute RegisterDTO registerDTO, BindingResult bindingResult,
                               RedirectAttributes redirectAttributes, Model model) {
        log.info("Processing registration for user: {}", registerDTO.getUsername());
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

