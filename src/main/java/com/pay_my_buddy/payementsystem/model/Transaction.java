package com.pay_my_buddy.payementsystem.model;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.Setter;

import java.math.BigDecimal;
/**
 * Entity class representing a financial transaction between two users in the payment system.
 * This class is mapped to the "transactions" table in the database and contains information about the sender, receiver, amount, and description of the transaction.
 */
@Entity
@Table(name = "transactions")
@Getter
@Setter
@RequiredArgsConstructor

public class Transaction {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private int id;

    @ManyToOne
    @JoinColumn(name = "sender", nullable = false)
    private User sender;
    @ManyToOne
    @JoinColumn(name = "receiver", nullable = false)
    private User receiver;

    @Column(name = "amount", nullable = false)
    private BigDecimal amount;

    @Column(name = "description", nullable = false)
    private String description;

    public Transaction(User sender, User receiver, String description, BigDecimal amount) {
        this.sender = sender;
        this.receiver = receiver;
        this.amount = amount;
        this.description = description;
    }


}
