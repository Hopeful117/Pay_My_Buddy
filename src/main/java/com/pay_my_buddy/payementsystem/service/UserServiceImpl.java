package com.pay_my_buddy.payementsystem.service;

import com.pay_my_buddy.payementsystem.model.User;
import com.pay_my_buddy.payementsystem.repository.UserRepository;
import jakarta.transaction.Transactional;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
@Slf4j
@AllArgsConstructor
public class UserServiceImpl implements UserService {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder; // pour hasher les mots de passe


    // Création d'un utilisateur
    @Override
    public void createUser(final String username, final String email, final String password) {
        final String hash = passwordEncoder.encode(password); // Hash du mot de passe

        if (userRepository.findByUsername(username).isPresent()) {
            throw new IllegalArgumentException("Le username ou l'email sont déjà utilisés");
        }

        if (userRepository.findByEmail(email).isPresent()) {
            throw new IllegalArgumentException("Le username ou l'email sont déjà utilisés");
        }

        final User newUser = new User(username, email, hash, true);
        // Hash du mot de passe avant sauvegarde

        userRepository.save(newUser);
        log.info("User created successfully: {}", username);

    }

    // Désactivation et anonymisation d'un utilisateur
    @Override
    public void deleteAndAnonymizeUser(int id) {
        Optional<User> user = userRepository.findUserById(id);
        if (user.isEmpty()) {
            log.warn("User with id {} not found", id);
            throw new IllegalArgumentException("User not found");
        }

        userRepository.updateIsActiveById(false, id);
        userRepository.anonymizeUserById(id);
        log.info("User {} deactivated and anonymized", id);
    }

    // Mise à jour du mot de passe
    @Override
    @Transactional
    public void updateUserPasswordById(String newPassword, int id) {
        Optional<User> user = userRepository.findUserById(id);
        if (user.isEmpty()) {
            log.warn("User with id {} not found", id);
            throw new IllegalArgumentException("User not found");
        }

        // Vérification si le mot de passe est le même
        if (passwordEncoder.matches(newPassword, user.get().getPassword())) {
            log.warn("New password is the same as the old password for user {}", id);
            throw new IllegalArgumentException("Veuillez choisir un mot de passe différent");
        }

        userRepository.updateUserPasswordById(passwordEncoder.encode(newPassword), id);
        log.info("Password updated successfully for user {}", id);
    }

    // Récupération d'un utilisateur par email
    @Override
    public Optional<User> findUserByEmail(String email) {
        Optional<User> user = userRepository.findByEmail(email);
        if (user.isEmpty()) {
            log.warn("User with email {} not found", email);
            throw new IllegalArgumentException("User not found");
        }
        log.info("User found: {}", user.get().getEmail());
        return user;
    }

    @Override
    public Optional<User> findUserByUsername(String username) {
        Optional<User> user = userRepository.findByUsername(username);
        if (user.isEmpty()) {
            log.warn("User with username {} not found", username);
            throw new IllegalArgumentException("User not found");
        }
        log.info("User found: {}", user.get().getUsername());
        return user;

    }

}
