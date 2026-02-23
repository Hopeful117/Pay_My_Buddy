package com.pay_my_buddy.payementsystem.service;

import com.pay_my_buddy.payementsystem.model.User;


import java.util.Optional;

public interface UserService {
    public void createUser(String username,String email,String password);
    public void deleteAndAnonymizeUser(int id);
    public void updateUserPasswordById(String password, int id);
    public Optional<User> findUserByEmail(String email);
    public Optional<User>findUserByUsername(String username);




}
