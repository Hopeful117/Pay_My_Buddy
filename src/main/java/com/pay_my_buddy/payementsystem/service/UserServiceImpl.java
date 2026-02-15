package com.pay_my_buddy.payementsystem.service;

import com.pay_my_buddy.payementsystem.model.User;
import com.pay_my_buddy.payementsystem.repository.UserRepository;
import jakarta.transaction.Transactional;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.Authentication;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
@Slf4j
@AllArgsConstructor
public class UserServiceImpl implements UserService {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder; // pour hasher les mots de passe


    // Création d'un utilisateur
    @Transactional
    public boolean createUser(User user)
{
        String username = user.getUsername();
        String password = user.getPassword();
        String email = user.getEmail();
        String hash=passwordEncoder.encode(password); // Hash du mot de passe
        if (userRepository.findByUsername(username) != null)
        { log.warn("User with username {} already exists", username);
            return false;
        }

        User newUser = new User(username, email,hash,true);
        // Hash du mot de passe avant sauvegarde

        userRepository.save(newUser);
        log.info("User created successfully: {}", user.getUsername());
        return true;
    }

    // Désactivation et anonymisation d'un utilisateur
    @Transactional
    public void deleteAndAnonymizeUser(int id) {
        User user = userRepository.findUserById(id);
        if (user == null) {
            log.warn("User with id {} not found", id);
            throw new IllegalArgumentException("User not found");
        }

        userRepository.updateIsActiveById(false, id);
        userRepository.anonymizeUserById(id);
        log.info("User {} deactivated and anonymized", id);
    }

    // Mise à jour du mot de passe
    @Transactional
    public void updateUserPasswordById(String newPassword, int id) {
        User user = userRepository.findUserById(id);
        if (user == null) {
            log.warn("User with id {} not found", id);
            throw new IllegalArgumentException("User not found");
        }

        // Vérification si le mot de passe est le même
        if (passwordEncoder.matches(newPassword, user.getPassword())) {
            log.warn("New password is the same as the old password for user {}", id);
            throw new IllegalArgumentException("New password must be different");
        }

        userRepository.updateUserPasswordById(passwordEncoder.encode(newPassword), id);
        log.info("Password updated successfully for user {}", id);
    }

    // Récupération d'un utilisateur par email
    @Transactional
    public User getUserByEmail(String email) {
        User user = userRepository.findByEmail(email);
        if (user == null) {
            log.warn("User with email {} not found", email);
            throw new IllegalArgumentException("User not found");
        }
        log.info("User found: {}", user.getEmail());
        return user;
    }

}
