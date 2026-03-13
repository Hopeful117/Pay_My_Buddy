package com.pay_my_buddy.payementsystem.service;

import com.pay_my_buddy.payementsystem.model.Transaction;
import com.pay_my_buddy.payementsystem.model.User;

import java.math.BigDecimal;
import java.util.List;

/**
 * Service interface for handling transactions between users.
 */
public interface TransactionService {
    void transfer(User sender, User receiver, String description, BigDecimal amount);

    List<Transaction> getTransactionsByUser(User user);

    List<Transaction> getTransactionsSentByUser(User user);

    List<Transaction> getTransactionsReceivedByUserId(User user);
}
