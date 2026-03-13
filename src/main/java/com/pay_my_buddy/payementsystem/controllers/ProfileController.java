package com.pay_my_buddy.payementsystem.controllers;

import com.pay_my_buddy.payementsystem.DTO.UpdateDTO;
import com.pay_my_buddy.payementsystem.model.User;
import com.pay_my_buddy.payementsystem.service.UserService;
import jakarta.validation.Valid;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.support.DefaultMessageSourceResolvable;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.util.List;

/**
 * Controller responsible for handling user profile-related operations, such as displaying the profile page and updating user information.
 */
@Controller
@Slf4j
@AllArgsConstructor
public class ProfileController {
    private final UserService userService;

    /**
     * Handles GET requests to the "/profile" endpoint, displaying the user's profile page with their current information.
     *
     * @param model the Model object used to pass data to the view
     * @return the name of the view to be rendered (profile page)
     */
    @GetMapping("/profile")
    public String getProfilePage(Model model) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if (authentication == null || !authentication.isAuthenticated()) {
            log.warn("Unauthenticated user attempted to access the profile page.");
            return "redirect:/login";
        }
        User user = userService.getUserByEmail(authentication.getName());
        log.info("User {} accessed their profile page.", user.getEmail());
        try {

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

    /**
     * Handles POST requests to the "/profile" endpoint, allowing users to update their profile information.
     *
     * @param updateDTO          the DTO containing the updated user information
     * @param model              the Model object used to pass data to the view
     * @param bindingResult      the BindingResult object used to check for validation errors
     * @param redirectAttributes the RedirectAttributes object used to pass flash attributes during redirection
     * @return the name of the view to be rendered (profile page or redirect to profile page)
     */
    @PostMapping("/profile")
    public String modifyProfile(@Valid @ModelAttribute UpdateDTO updateDTO, Model model, BindingResult bindingResult, RedirectAttributes redirectAttributes) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if (authentication == null || !authentication.isAuthenticated()) {
            log.warn("Unauthenticated user attempted to update their profile.");
            return "redirect:/login";
        }
        log.info("User {} is attempting to update their profile.", authentication.getName());
        if (bindingResult.hasErrors()) {
            final List<String> errors = bindingResult.getAllErrors()
                    .stream().map(DefaultMessageSourceResolvable::getDefaultMessage)
                    .toList();
            redirectAttributes.addFlashAttribute("errors", errors);
            return "redirect:/profile";
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
