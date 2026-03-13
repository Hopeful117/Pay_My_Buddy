package com.pay_my_buddy.payementsystem.controllers;

import com.pay_my_buddy.payementsystem.model.User;
import com.pay_my_buddy.payementsystem.service.UserService;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.util.List;


/**
 * Contrôleur pour gérer les requêtes liées aux relations entre utilisateurs.
 */
@Controller
@Slf4j
@AllArgsConstructor
public class RelationController {
    private final UserService userService;

    /**
     * Gère les requêtes GET pour afficher la page des relations.
     *
     * @param model Le modèle pour passer des données à la vue.
     * @return Le nom de la vue de la page des relations.
     */
    @GetMapping("/relations")
    public String getRelationPage(Model model) {
        log.info("Affichage de la page des relations");
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if(authentication == null || !authentication.isAuthenticated()) {
            log.warn("Utilisateur non authentifié tenté d'accéder à la page des relations");
            return "redirect:/login";
        }
        model.addAttribute("currentPage", "relations");
        return "relations";
    }

    /**
     * Gère les requêtes POST pour ajouter une relation entre utilisateurs.
     *
     * @param model              Le modèle pour passer des données à la vue.
     * @param email              L'email de l'utilisateur à ajouter comme relation.
     * @param redirectAttributes Les attributs de redirection pour passer des messages après l'ajout de la relation.
     * @return Une redirection vers la page des relations ou la même page en cas d'erreur.
     */
    @PostMapping("/relations")
    public String addRelation(Model model, String email, RedirectAttributes redirectAttributes) {
        if(email == null || email.isEmpty()) {
            log.warn("Email de l'utilisateur à ajouter est vide");
            redirectAttributes.addFlashAttribute("errors", "L'email ne peut pas être vide");
            return "redirect:/relations";
        }

        try {
            Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
            if (authentication == null || !authentication.isAuthenticated()) {
                log.warn("Utilisateur non authentifié tenté d'ajouter une relation");
                return "redirect:/login";
            }
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
