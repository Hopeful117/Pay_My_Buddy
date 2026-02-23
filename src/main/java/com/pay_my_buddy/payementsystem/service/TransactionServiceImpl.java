package com.pay_my_buddy.payementsystem.service;

import com.pay_my_buddy.payementsystem.model.Transaction;
import com.pay_my_buddy.payementsystem.model.User;
import com.pay_my_buddy.payementsystem.repository.TransactionRepository;
import jakarta.transaction.Transactional;
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
    @Transactional
    public void transfer(User sender, User receiver, String description, BigDecimal amount) {
        log.debug("Adding transaction from sender: {}, receiver: {},description{} amount: {}", sender, receiver,description, amount);
        try {
            transactionRepository.addTransaction(sender.getId(), receiver.getId(),description, amount);
            log.info("Transaction added successfully");

        } catch (Exception exception) {
            log.error("Failed to add transaction",exception);
            throw new RuntimeException("Une erreur s'est produite lors de la transaction");


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

    @Override
    public List<Transaction> getTransactionsSentByUserId(int userId){
        return getTransactionsByUserId(userId).stream().filter(transaction -> transaction.getSender().getId()==userId).toList();

    }

    @Override
    public List<Transaction>getTransactionsReceivedByUserId(int userId){
        return getTransactionsByUserId(userId).stream().filter(transaction -> transaction.getReceiver().getId()==userId).toList();

    }


}
