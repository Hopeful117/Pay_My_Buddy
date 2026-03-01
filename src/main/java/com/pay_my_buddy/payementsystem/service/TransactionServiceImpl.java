package com.pay_my_buddy.payementsystem.service;

import com.pay_my_buddy.payementsystem.model.Transaction;
import com.pay_my_buddy.payementsystem.model.User;
import com.pay_my_buddy.payementsystem.repository.TransactionRepository;
import jakarta.transaction.Transactional;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.List;

@Service
@Slf4j
@AllArgsConstructor
public class TransactionServiceImpl implements TransactionService {
    private TransactionRepository transactionRepository;

    @Override
    @Transactional
    public void transfer(User sender, User receiver, String description, BigDecimal amount) {
        log.debug("Adding transaction from sender: {}, receiver: {},description{} amount: {}", sender, receiver, description, amount);
        try {

            final Transaction transaction = new Transaction(sender, receiver, description, normalizeAmount(amount));

            transactionRepository.save(transaction);
            log.info("Transaction added successfully");

        } catch (Exception exception) {
            throw new RuntimeException("Une erreur s'est produite lors de la transaction");


        }
    }

    @Override
    public List<Transaction> getTransactionsByUser(User user) {

        return transactionRepository.getTransactionsBySender(user);
    }

    @Override
    public List<Transaction> getTransactionsSentByUser(User user) {
        return getTransactionsByUser(user).stream().filter(transaction -> transaction.getSender().getId() == user.getId()).toList();

    }

    @Override
    public List<Transaction> getTransactionsReceivedByUserId(User user) {
        return getTransactionsByUser(user).stream().filter(transaction -> transaction.getReceiver().getId() == user.getId()).toList();

    }

    private BigDecimal normalizeAmount(BigDecimal amount) {
        return amount.setScale(2, RoundingMode.HALF_UP);
    }


}
