package com.pay_my_buddy.payementsystem.controllers;


import com.pay_my_buddy.payementsystem.DTO.TransferDTO;
import com.pay_my_buddy.payementsystem.model.Transaction;
import com.pay_my_buddy.payementsystem.model.User;
import com.pay_my_buddy.payementsystem.service.TransactionService;
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

@Controller
@AllArgsConstructor
@Slf4j
public class TransferController {
    private final UserService userService;
    private final TransactionService transactionService;


    @GetMapping("/home")
    public String getTransferPage(Model model) {
        try {
            Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
            String email = authentication.getName();
            User user = userService.getUserByEmail(email);
            List<User> user_connections = user.getConnections();
            List<Transaction> transactions = transactionService.getTransactionsSentByUser(user);
            model.addAttribute("transactions", transactions);
            model.addAttribute("connections", user_connections);
            model.addAttribute("currentPage", "home");
            return "home";
        } catch (Exception e) {
            return "redirect:/login";

        }
    }

    @PostMapping("/transfer")
    public String Transfer(Model model, @Valid @ModelAttribute TransferDTO transferDTO, BindingResult bindingResult,
                           RedirectAttributes redirectAttributes) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if (bindingResult.hasErrors()) {
            final List<String> errors = bindingResult.getAllErrors()
                    .stream().map(DefaultMessageSourceResolvable::getDefaultMessage)
                    .toList();
            redirectAttributes.addAttribute("errors", errors);
            return "redirect:/home";
        }
        try {
            User sender = userService.getUserByEmail(authentication.getName());
            transactionService.transfer(sender, transferDTO.getReceiver(), transferDTO.getDescription(), transferDTO.getAmount());
            redirectAttributes.addFlashAttribute("success",
                    "Transfert effectué avec succès !");

            return "redirect:/home";

        } catch (Exception exception) {
            final List<String> errors = List.of(exception.getMessage());
            model.addAttribute("errors", errors);
            return "home";
        }
    }
}





