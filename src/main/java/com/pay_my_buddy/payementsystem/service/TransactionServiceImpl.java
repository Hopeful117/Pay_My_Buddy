package com.pay_my_buddy.payementsystem.service;

import com.pay_my_buddy.payementsystem.model.Transaction;
import com.pay_my_buddy.payementsystem.repository.TransactionRepository;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.List;

@Service
@Slf4j
@AllArgsConstructor
public class TransactionServiceImpl implements TransactionService {
    private TransactionRepository transactionRepository;

    @Override
    public boolean addTransaction(int senderId, int receiverId, BigDecimal amount) {
        log.debug("Adding transaction from senderId: {}, receiverId: {}, amount: {}", senderId, receiverId, amount);
        try {
            transactionRepository.addTransaction(senderId, receiverId, amount);
            log.info("Transaction added successfully");
            return true;
        } catch (Exception e) {
            log.warn("Failed to add transaction");

            return false;
        }
    }

    @Override
    public List<Transaction> getTransactionsByUserId(int userId) {
        log.debug("Retrieving transactions for userId: {}", userId);
        List<Transaction>transactions= transactionRepository.getTransactionsByUserId(userId);
        if ( transactions.isEmpty()) {
            log.info("No transactions found for userId: {}", userId);
        } else {
            log.info("Retrieved {} transactions for userId: {}", transactions.size(), userId);
        }
        return transactions;
    }


}
