package com.pay_my_buddy.payementsystem.controllers;


import com.pay_my_buddy.payementsystem.model.Transaction;
import com.pay_my_buddy.payementsystem.model.User;
import com.pay_my_buddy.payementsystem.service.TransactionService;
import com.pay_my_buddy.payementsystem.service.UserService;
import lombok.AllArgsConstructor;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

import java.util.List;
import java.util.Optional;

@Controller
@AllArgsConstructor
public class TransferController {
    private final UserService userService;
    private final TransactionService transactionService;
    @GetMapping("/home")
    public String getTransferPage(Model model, Authentication authentification) {
        String email = authentification.getName();
        Optional<User> user = userService.findUserByEmail(email);
        if(user.isEmpty()) {
            return "redirect:/login";
        }
        List<User> user_connections = user.get().getConnections();
        List<Transaction> transactions = transactionService.getTransactionsByUserId(user.get().getId());
        model.addAttribute("transactions", transactions);
        model.addAttribute("connections", user_connections);
        model.addAttribute("currentPage", "home");
        return "home";
    }
}
