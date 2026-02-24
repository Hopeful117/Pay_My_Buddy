package com.pay_my_buddy.payementsystem.controllers;

import com.pay_my_buddy.payementsystem.DTO.RegisterDTO;
import com.pay_my_buddy.payementsystem.model.User;
import com.pay_my_buddy.payementsystem.service.UserService;
import jakarta.validation.Valid;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.support.DefaultMessageSourceResolvable;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.util.List;
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
        RegisterDTO registerDTO = new RegisterDTO();
        registerDTO.setUsername(user.get().getUsername());
        registerDTO.setEmail(user.get().getEmail());
        model.addAttribute("currentPage", "profile");
        model.addAttribute("registerDTO", registerDTO);

        return ("profile");


    }

    @PostMapping("/profile")
    public String modifyProfile(@Valid @ModelAttribute RegisterDTO registerDTO ,Model model, Authentication authentication, BindingResult bindingResult, RedirectAttributes redirectAttributes) {
        if (bindingResult.hasErrors()) {
            final List<String> errors = bindingResult.getAllErrors()
                    .stream().map(DefaultMessageSourceResolvable::getDefaultMessage)
                    .toList();
            redirectAttributes.addAttribute("errors", errors);
            return "profile";
        }
        try {
            Optional<User> user = userService.findUserByEmail(authentication.getName());
            if (user.isPresent()) {
                userService.updateUserPasswordById(registerDTO.getPassword(), user.get().getId());

                redirectAttributes.addFlashAttribute("success",
                        "Mot de passe changé avec succès !");
            }
            return "redirect:/profile";


        } catch (Exception exception) {
            final List<String> errors = List.of(exception.getMessage());
            model.addAttribute("errors", errors);
            return "profile";
        }

    }


}
