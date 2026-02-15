package com.pay_my_buddy.payementsystem.controllers;

import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.security.core.Authentication;

@Controller
@Slf4j
@AllArgsConstructor
/**
 * Contrôleur pour gérer les requêtes liées à la connexion des utilisateurs.
 */

public class LoginController {
    /**
     * Gère les requêtes GET pour afficher la page de connexion.
     *
     * @return Le nom de la vue de la page de connexion.
     */
    @GetMapping("/login")
    public String getLoginPage() {
        log.info("Login page accessed");
        return "login";
    }
    /**
     * Gère les requêtes POST pour traiter la connexion de l'utilisateur.
     *
     * @param authentication L'objet d'authentification contenant les détails de l'utilisateur connecté.
     * @return Un message indiquant le succès de la connexion.
     */

    }

