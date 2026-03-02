package com.pay_my_buddy.payementsystem.service;

import com.pay_my_buddy.payementsystem.DTO.UpdateDTO;
import com.pay_my_buddy.payementsystem.model.User;

import java.util.Optional;

public interface UserService {
    void createUser(String username, String email, String password);


    void updateUser(int id, UpdateDTO updateDTO);

    User getUserByEmail(String email);

    Optional<User> findUserByUsername(String username);

    void addConnection(int userId, int friendId);


}
