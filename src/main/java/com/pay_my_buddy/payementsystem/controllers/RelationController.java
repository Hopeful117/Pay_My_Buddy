package com.pay_my_buddy.payementsystem.controllers;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
@Slf4j
public class RelationController {

    @GetMapping("/relations")
    public String getRelationPage(Model model){
        model.addAttribute("currentPage","relations");
        return "relations";
    }
}
