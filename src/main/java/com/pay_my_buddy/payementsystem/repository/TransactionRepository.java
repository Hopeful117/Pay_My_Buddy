package com.pay_my_buddy.payementsystem.repository;

import com.pay_my_buddy.payementsystem.model.Transaction;
import com.pay_my_buddy.payementsystem.model.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
/**
 * Repository interface for managing Transaction entities in the database.
 * This interface extends JpaRepository, providing CRUD operations and custom query methods for transactions.
 */
@Repository
public interface TransactionRepository extends JpaRepository<Transaction, Integer> {

    List<Transaction> getTransactionsBySender(User user);

}
