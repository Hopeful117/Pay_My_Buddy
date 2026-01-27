package com.pay_my_buddy.payementsystem.service;

import com.pay_my_buddy.payementsystem.DTO.RequestUserDTO;
import com.pay_my_buddy.payementsystem.model.User;

public interface UserService {
    public Boolean createUser(User user);
    public Boolean updateIsActiveById(Boolean isActive, int id);
    public Boolean updateUserPasswordById(String password, int id);
    public String authenticateUser(String email, String password);
    public User getUserByEmail(String email);



}
