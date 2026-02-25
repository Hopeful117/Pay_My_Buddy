package com.pay_my_buddy.payementsystem.controllers;

import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

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


}

