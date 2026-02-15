package com.pay_my_buddy.payementsystem.service;

import com.pay_my_buddy.payementsystem.model.User;
import org.springframework.security.core.Authentication;

public interface UserService {
    public boolean createUser(User user);
    public void deleteAndAnonymizeUser(int id);
    public void updateUserPasswordById(String password, int id);
    public User getUserByEmail(String email);




}
