package com.pay_my_buddy.payementsystem.controllers;

import com.pay_my_buddy.payementsystem.model.User;
import com.pay_my_buddy.payementsystem.service.UserService;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.util.List;

@Controller
@Slf4j
@AllArgsConstructor
public class RelationController {
    private final  UserService userService;

    @GetMapping("/relations")
    public String getRelationPage(Model model) {
        model.addAttribute("currentPage", "relations");
        return "relations";
    }

    @PostMapping("/relations")
    public String addRelation(Model model, String email, RedirectAttributes redirectAttributes, Authentication authentication) {

        try {
            User user = userService.getUserByEmail(authentication.getName());
            User friend = userService.getUserByEmail(email);

            userService.addConnection(user.getId(), friend.getId());

            redirectAttributes.addFlashAttribute("success",
                    "Relation ajouté avec succès !");
            return "redirect:/relations";


        } catch (Exception exception) {
            final List<String> errors = List.of(exception.getMessage());
            model.addAttribute("errors", errors);
        }
        return "relations";
    }

}
