package com.pay_my_buddy.payementsystem.service;

import com.pay_my_buddy.payementsystem.DTO.RegisterDTO;
import com.pay_my_buddy.payementsystem.model.User;

import java.util.Optional;

public interface UserService {
    void createUser(String username, String email, String password);


    void updateUser(RegisterDTO registerDTO);

    User getUserByEmail(String email);

    Optional<User> findUserByUsername(String username);

    void addConnection(int userId, int friendId);


}
