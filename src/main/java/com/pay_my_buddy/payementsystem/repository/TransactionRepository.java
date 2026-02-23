package com.pay_my_buddy.payementsystem.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import com.pay_my_buddy.payementsystem.model.Transaction;

import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;

@Repository
public interface TransactionRepository  extends JpaRepository<Transaction,Integer> {
    @Query(value=("CALL list_transactions(:userId)"),nativeQuery = true)
    List<Transaction> getTransactionsByUserId(@Param("userId")int userId);

    @Modifying
    @Query(value=("CALL add_transaction(:sender, :receiver,:description, :amount)"), nativeQuery = true)
    void addTransaction(@Param("sender")int senderId, @Param("receiver")int receiverId,@Param("description")String description, @Param("amount")BigDecimal amount);
}
