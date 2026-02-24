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
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.security.Principal;
import java.util.List;
import java.util.Optional;

@Controller
@AllArgsConstructor
@Slf4j
public class TransferController {
    private final UserService userService;
    private final TransactionService transactionService;


    @GetMapping("/home")
    public String getTransferPage(Model model, Authentication authentication) {
        String email = authentication.getName();
        Optional<User> user = userService.findUserByEmail(email);
        if (user.isEmpty()) {
            return "redirect:/login";
        }
        List<User> user_connections = user.get().getConnections();
        List<Transaction> transactions = transactionService.getTransactionsSentByUserId(user.get().getId());
        model.addAttribute("transactions", transactions);
        model.addAttribute("connections", user_connections);
        model.addAttribute("currentPage", "home");
        return "home";
    }

    @PostMapping("/transfer")
    public String Transfer(Model model, Principal principal, @Valid @ModelAttribute TransferDTO transferDTO, BindingResult bindingResult,
                           RedirectAttributes redirectAttributes) {
        if (bindingResult.hasErrors()) {
            final List<String> errors = bindingResult.getAllErrors()
                    .stream().map(DefaultMessageSourceResolvable::getDefaultMessage)
                    .toList();
            redirectAttributes.addAttribute("errors", errors);
            return "redirect:/home";
        }
        try {
            Optional<User> sender = userService.findUserByEmail(principal.getName());
            sender.ifPresent(user -> transactionService.transfer(user, transferDTO.getReceiver(), transferDTO.getDescription(), transferDTO.getAmount()));
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





