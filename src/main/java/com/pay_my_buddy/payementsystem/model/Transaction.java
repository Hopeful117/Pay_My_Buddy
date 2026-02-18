package com.pay_my_buddy.payementsystem.model;

import jakarta.persistence.*;
import lombok.Data;
import lombok.RequiredArgsConstructor;

import java.math.BigDecimal;

@Entity
@Table(name="transaction")
@Data
@RequiredArgsConstructor
public class Transaction {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name="id")
    private int id;

    @ManyToOne
    @JoinColumn(name = "sender", nullable = false)
    private User sender;
    @ManyToOne
    @JoinColumn(name = "receiver", nullable = false)
    private User receiver;

    private BigDecimal amount;
    private String description;

    public Transaction(User sender, User receiver, BigDecimal amount, String description) {
        this.sender = sender;
        this.receiver = receiver;
        this.amount = amount;
        this.description = description;
    }


}
