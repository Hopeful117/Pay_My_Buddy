package com.pay_my_buddy.payementsystem.model;

import jakarta.persistence.*;

import lombok.Data;
import lombok.RequiredArgsConstructor;


import java.util.List;

@Entity
@Table(name="user")
@Data
@RequiredArgsConstructor
public class User {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name="id")
    private int id;

    @Column(name="username", nullable = false, unique = true)
    private String username;

    @Column(name="email", nullable = false, unique = true)
    private String email;

    @Column(name="password", nullable = false)
    private String password;

    @Column(name="is_active", nullable = false)
    private Boolean isActive;
    @ManyToMany
    @JoinTable(
        name = "user_connection",
        joinColumns = @JoinColumn(name = "user_id"),
        inverseJoinColumns = @JoinColumn(name = "connection_id")
    )
    private List<User> connections;
    public User(String username, String email, String password, Boolean isActive) {
        this.username = username;
        this.email = email;
        this.password = password;
        this.isActive = isActive;
    }

}
