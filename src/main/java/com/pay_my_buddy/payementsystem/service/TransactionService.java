package com.pay_my_buddy.payementsystem.service;

import com.pay_my_buddy.payementsystem.model.Transaction;
import com.pay_my_buddy.payementsystem.model.User;

import java.math.BigDecimal;
import java.util.List;


public interface TransactionService {
    void transfer(User sender, User receiver, String description, BigDecimal amount);

    List<Transaction> getTransactionsByUserId(int userId);

    List<Transaction> getTransactionsSentByUserId(int userId);

    List<Transaction> getTransactionsReceivedByUserId(int userId);
}
