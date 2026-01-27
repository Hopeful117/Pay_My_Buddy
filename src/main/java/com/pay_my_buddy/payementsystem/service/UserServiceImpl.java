package com.pay_my_buddy.payementsystem.service;


import com.pay_my_buddy.payementsystem.model.User;
import com.pay_my_buddy.payementsystem.repository.UserRepository;
import jakarta.transaction.Transactional;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.stereotype.Service;

@Service
public class UserServiceImpl implements UserService {
    @Autowired
    private final UserRepository userRepository;
    @Autowired
    private final TokenService tokenService;
    
    private static Logger LOGGER= LogManager.getLogger(UserServiceImpl.class);
    public UserServiceImpl(UserRepository userRepository,TokenService tokenService){
        this.userRepository = userRepository;
        this.tokenService=tokenService;

    }
    @Modifying
    @Transactional
    public Boolean createUser(User user) {
        try{
            userRepository.save(user);
            LOGGER.info("User created sucessfully");
            return true;
        } catch (Exception e){
            LOGGER.error("Failed to create User",e);
            throw new RuntimeException(e);

        }

    }
    @Transactional
    public Boolean updateIsActiveById(Boolean isActive, int id) {
        User user = userRepository.findUserById(id);
        try {

            if (user == null || !user.getIsActive() ) {
                LOGGER.error("User not found or profile is inactive");
                return false;

            }
            LOGGER.info("Account status updated successfully");
            userRepository.updateIsActiveById(isActive, id);
            userRepository.deleteUserById(id);
            return true;

        }catch(Exception e) {
            LOGGER.error("Failed to update account status",e);
            throw new RuntimeException(e);
        }

    }
    @Transactional
    public Boolean updateUserPasswordById(String password, int id) {
        try {
            User user = userRepository.findUserById(id);
            if(user==null){
                LOGGER.error("User not found");
                return false;
            }
            if(user.getPassword().equals(password)){
                LOGGER.error("Old password is the same as the new password");
                return false;
            }
            LOGGER.info("Password updated sucessfully");
            userRepository.updateUserPasswordById(password, id);
            return true;
        }catch(Exception e){
            LOGGER.error("Error updating password");
            throw new RuntimeException(e);
        }
    }
    @Transactional
    public String authenticateUser(String email,String password){
        try{
            User user = userRepository.findByEmail(email);
            if(user==null){
                LOGGER.error("User not found");

            }
            if (!user.getPassword().equals(password)){
                LOGGER.error("Invalid password");

            }

            return tokenService.generateToken(user.getEmail());


        } catch (Exception e) {
            LOGGER.error("Error authenticating user",e);
            throw new RuntimeException(e);
        }


    }
    @Transactional
    public User getUserByEmail(String email){
        try{
            User user = userRepository.findByEmail(email);
            if(user==null){
                LOGGER.error("User not found");
            }
            LOGGER.info("User found",user);
            return user;
        } catch (Exception e) {
            LOGGER.error("Error finding user",e);
            throw new RuntimeException(e);
        }
    }


}

