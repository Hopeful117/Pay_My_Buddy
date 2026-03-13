package com.pay_my_buddy.payementsystem.service;

import com.pay_my_buddy.payementsystem.model.Transaction;
import com.pay_my_buddy.payementsystem.model.User;
import com.pay_my_buddy.payementsystem.repository.TransactionRepository;
import com.pay_my_buddy.payementsystem.repository.UserRepository;
import jakarta.transaction.Transactional;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.util.Assert;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.List;

@Service
@Slf4j
@AllArgsConstructor
/**
 * Service implementation for handling transactions between users.
 */
public class TransactionServiceImpl implements TransactionService {
    private TransactionRepository transactionRepository;
    private UserRepository userRepository;


    /**
     * Transfers a specified amount from the sender to the receiver with a description.
     *
     * @param sender      the user who is sending the amount
     * @param receiver    the user who is receiving the amount
     * @param description a description of the transaction
     * @param amount      the amount to be transferred
     * @throws IllegalArgumentException if the sender or receiver does not exist, if the description is empty, if the amount is null or if the amount is not greater than 0.
     */
    @Override
    @Transactional
    public void transfer(User sender, User receiver, String description, BigDecimal amount) {
        log.debug("Adding transaction from sender: {}, receiver: {},description{} amount: {}", sender, receiver, description, amount);


        if (description.isEmpty()) {
            throw new IllegalArgumentException("La description ne peut pas être vide");
        }

        if (amount == null) {
            throw new IllegalArgumentException("Le montant ne peut pas être null");
        }

        if (amount.compareTo(BigDecimal.ZERO) == 0) {
            throw new IllegalArgumentException("Le montant doit être supérieur à 0");
        }

        if(amount.compareTo(sender.getBalance()) > 0){
            throw new IllegalArgumentException("Fonds insuffisants");
        }


        if (!userRepository.existsByUsername(sender.getUsername())) {
            throw new IllegalArgumentException("Utilisateur non trouvé");

        }
        if (!userRepository.existsByUsername(receiver.getUsername())) {
            throw new IllegalArgumentException(("Utilisateur non trouvé"));
        }

        if(sender == receiver){
            throw new IllegalArgumentException("Auto-transactions non autorisées");
        }
//        try {
        BigDecimal normalizedAmount = normalizeAmount(amount);
        final Transaction transaction = new Transaction(sender, receiver, description, normalizedAmount);
        sender.setBalance(sender.getBalance().subtract(normalizedAmount));
        receiver.setBalance(receiver.getBalance().add(normalizedAmount));
        userRepository.save(sender);
        userRepository.save(receiver);

        transactionRepository.save(transaction);
        log.info("Transaction added successfully");

//        } catch (Exception exception) {
//            throw new RuntimeException("Une erreur s'est produite lors de la transaction", exception);
//
//
//        }
    }


    /**
     * Retrieves a list of transactions associated with a specific user.
     *
     * @param user the user for whom to retrieve transactions
     * @return a list of transactions associated with the specified user
     */
    @Override
    public List<Transaction> getTransactionsByUser(User user) {


        return transactionRepository.getTransactionsByUser(user);
    }

    /**
     * Retrieves a list of transactions sent by a specific user.
     *
     * @param user the user for whom to retrieve sent transactions
     * @return a list of transactions sent by the specified user
     */
    @Override
    public List<Transaction> getTransactionsSentByUser(User user) {
        return getTransactionsByUser(user).stream().filter(transaction -> transaction.getSender().getId() == user.getId()).toList();

    }

    /**
     * Retrieves a list of transactions received by a specific user.
     *
     * @param user the user for whom to retrieve received transactions
     * @return a list of transactions received by the specified user
     */
    @Override
    public List<Transaction> getTransactionsReceivedByUserId(User user) {
        return getTransactionsByUser(user).stream().filter(transaction -> transaction.getReceiver().getId() == user.getId()).toList();

    }

    /**
     * Normalizes the amount by setting the scale to 2 decimal places and rounding half up.
     *
     * @param amount the amount to be normalized
     * @return the normalized amount
     */
    private BigDecimal normalizeAmount(BigDecimal amount) {
        return amount.setScale(2, RoundingMode.HALF_UP);
    }


}
