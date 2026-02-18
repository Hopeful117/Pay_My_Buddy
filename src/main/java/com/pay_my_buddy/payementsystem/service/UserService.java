package com.pay_my_buddy.payementsystem.service;

import com.pay_my_buddy.payementsystem.model.User;
import org.springframework.security.core.Authentication;

import java.util.Optional;

public interface UserService {
    public boolean createUser(User user);
    public void deleteAndAnonymizeUser(int id);
    public void updateUserPasswordById(String password, int id);
    public Optional<User> findUserByEmail(String email);




}
