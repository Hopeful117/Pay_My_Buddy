package com.pay_my_buddy.payementsystem.model;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.Setter;
import lombok.experimental.Accessors;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
/**
 * Entity class representing a user in the payment system.
 * This class is mapped to the "users" table in the database and contains information about the user's username, email, password, active status, and connections with other users.
 */
@Entity
@Table(name = "users")
@Getter
@Setter
@Accessors(chain = true)
@RequiredArgsConstructor
public class User {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private int id;

    @Column(name = "username", nullable = false, unique = true)
    private String username;

    @Column(name = "email", nullable = false, unique = true)
    private String email;

    @Column(name = "password", nullable = false)
    private String password;

    @Column(name = "is_active", nullable = false)
    private Boolean isActive = true;

    @Column(name= "balance", nullable = false)
    private BigDecimal balance = BigDecimal.ZERO;

    @ManyToMany
    @JoinTable(
            name = "user_connections",
            joinColumns = @JoinColumn(name = "user_id"),
            inverseJoinColumns = @JoinColumn(name = "connection_id")
    )
    private List<User> connections = new ArrayList<>();


    public User(String username, String email, String password) {
        this.username = username;
        this.email = email;
        this.password = password;
    }

}
