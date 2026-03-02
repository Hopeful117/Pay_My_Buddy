package com.pay_my_buddy.payementsystem.controllers;

import com.pay_my_buddy.payementsystem.DTO.UpdateDTO;
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

@Controller
@Slf4j
@AllArgsConstructor
public class ProfileController {
    private final UserService userService;

    @GetMapping("/profile")
    public String getProfilePage(Model model, Authentication authentication) {

        String email = authentication.getName();
        try {
            User user = userService.getUserByEmail(email);
            UpdateDTO updateDTO = new UpdateDTO();
            updateDTO.setUsername(user.getUsername());
            updateDTO.setEmail(user.getEmail());
            model.addAttribute("currentPage", "profile");
            model.addAttribute("updateDTO", updateDTO);

            return ("profile");
        } catch (Exception e) {
            return "redirect:/login";
        }


    }

    @PostMapping("/profile")
    public String modifyProfile(@Valid @ModelAttribute UpdateDTO updateDTO, Model model, Authentication authentication, BindingResult bindingResult, RedirectAttributes redirectAttributes) {
        if (bindingResult.hasErrors()) {
            final List<String> errors = bindingResult.getAllErrors()
                    .stream().map(DefaultMessageSourceResolvable::getDefaultMessage)
                    .toList();
            redirectAttributes.addAttribute("errors", errors);
            return "profile";
        }
        try {
            User user = userService.getUserByEmail(authentication.getName());
            final int id = user.getId();
            userService.updateUser(id, updateDTO);

            redirectAttributes.addFlashAttribute("success",
                    "Profil mis à jour avec succès !");

            return "redirect:/profile";


        } catch (Exception exception) {
            final List<String> errors = List.of(exception.getMessage());
            model.addAttribute("errors", errors);
            return "profile";
        }

    }


}
