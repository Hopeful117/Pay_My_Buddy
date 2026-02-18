package com.pay_my_buddy.payementsystem.service;

import com.pay_my_buddy.payementsystem.model.Transaction;

import java.math.BigDecimal;
import java.util.List;


public interface TransactionService {
    boolean addTransaction(int senderId, int receiverId, BigDecimal amount);
    List<Transaction> getTransactionsByUserId(int userId);
}
