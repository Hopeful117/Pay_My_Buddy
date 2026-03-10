package com.pay_my_buddy.payementsystem.controllers;


import com.pay_my_buddy.payementsystem.DTO.TransactionDTO;
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
/**
 * Controller responsible for handling transaction-related operations, such as displaying the transfer page and processing transactions between users.
 */
@Controller
@AllArgsConstructor
@Slf4j
public class TransactionController {
    private final UserService userService;
    private final TransactionService transactionService;

    /**
     * Handles GET requests to the "/home" endpoint, displaying the transfer page with the user's connections and transactions.
     * @param model the model to hold attributes for the view
     * @return the name of the view to be rendered, or a redirect to the login page if an error occurs
     */
    @GetMapping("/home")
    public String getTransactionPage(Model model) {
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
/**
     * Handles POST requests to the "/transaction" endpoint, processing a transaction between users based on the provided TransactionDTO.
     * @param model the model to hold attributes for the view
     * @param transactionDTO the data transfer object containing transaction details
     * @param bindingResult the result of validating the TransactionDTO
     * @param redirectAttributes attributes for redirecting with messages
     * @return a redirect to the home page with success or error messages, or the home view if an error occurs during processing
     */
    @PostMapping("/transaction")
    public String Transaction(Model model, @Valid @ModelAttribute TransactionDTO transactionDTO, BindingResult bindingResult,
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
            transactionService.transfer(sender, transactionDTO.getReceiver(), transactionDTO.getDescription(), transactionDTO.getAmount());
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





