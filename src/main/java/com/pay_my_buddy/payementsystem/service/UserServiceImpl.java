package com.pay_my_buddy.payementsystem.service;

import com.pay_my_buddy.payementsystem.DTO.UpdateDTO;
import com.pay_my_buddy.payementsystem.model.User;
import com.pay_my_buddy.payementsystem.repository.UserRepository;
import jakarta.transaction.Transactional;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.Set;

@Service
@Slf4j
@AllArgsConstructor

/**
 * Service implementation for managing user-related operations.
 */
public class UserServiceImpl implements UserService {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;


    /**
     * Creates a new user with the provided username, email, and password.
     *
     * @param username the username of the new user
     * @param email    the email of the new user
     * @param password the password of the new user
     */
    @Override
    public void createUser(final String username, final String email, final String password) {

        if (userRepository.existsByUsername(username)) {
            throw new IllegalArgumentException("Le username ou l'email sont déjà utilisés");
        }

        if (userRepository.existsByEmail(email)) {
            throw new IllegalArgumentException("Le username ou l'email sont déjà utilisés");
        }

        final String hash = passwordEncoder.encode(password); // Hash du mot de passe

        final User newUser = new User(username, email, hash);

        try {
            userRepository.save(newUser);
            log.info("User created successfully: {}", username);
        } catch (RuntimeException e) {
            throw new RuntimeException("Une erreur s'est produite lors de la création de l'utilisateur");
        }


    }

    /**
     * Updates the user information based on the provided UpdateDTO.
     *
     * @param id        the ID of the user to update
     * @param updateDTO the DTO containing the updated user information
     */
    @Override
    @Transactional
    public void updateUser(final int id, final UpdateDTO updateDTO) {
        final User user = userRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("User not found"));

        if (!updateDTO.getEmail().equals(user.getEmail())) {
            if (userRepository.existsByEmail(updateDTO.getEmail())) {
                throw new IllegalArgumentException("Veuillez utiliser un email différent");
            }
            user.setEmail(updateDTO.getEmail());
        }
        if (!updateDTO.getUsername().equals(user.getUsername())) {
            if (userRepository.existsByUsername(updateDTO.getUsername())) {
                throw new IllegalArgumentException("Le nom d'utilisateur est déjà utilisé");
            }
            user.setUsername(updateDTO.getUsername());

        }
        if (!updateDTO.getPassword().isBlank()) {
            user.setPassword(passwordEncoder.encode(updateDTO.getPassword()));
        }

        try {
            userRepository.save(user);
        } catch (RuntimeException e) {
            throw new RuntimeException("Une erreur s'est produite lors de la mise à jour de l'utilisateur");
        }

        log.info("User updated successfully for user {}", user.getId());
    }

    /**
     * Retrieves a user by their email.
     *
     * @param email the email of the user to retrieve
     * @return the User object corresponding to the provided email
     */
    @Override
    public User getUserByEmail(String email) {
        log.info("email : " + email);
        final User user = userRepository.findByEmail(email)
                .orElseThrow(() -> new IllegalArgumentException("User not found"));

        log.info("User found: {}", user.getEmail());
        return user;
    }

    /**
     * Retrieves a user by their username.
     *
     * @param username the username of the user to retrieve
     * @return the User object corresponding to the provided username
     */
    @Override
    public User getUserByUsername(String username) {
        final User user = userRepository.findByUsername(username).orElseThrow(() -> new IllegalArgumentException("User not found"));

        log.info("User found: {}", user.getUsername());
        return user;

    }

    /**
     * Adds a connection between two users.
     *
     * @param userId   the ID of the user who wants to add a connection
     * @param friendId the ID of the user to be added as a connection
     */
    @Override
    @Transactional
    public void addConnection(int userId, int friendId) {
        final User user = userRepository.findById(userId)
                .orElseThrow(() -> new IllegalArgumentException("Utilisateur non trouvé"));

        final boolean alreadyFriend = user.getConnections()
                .stream().anyMatch(f -> f.getId() == friendId);

        if (alreadyFriend) {
            throw new IllegalArgumentException("L'utilisateur est déjà dans vos relations");
        }


        final User friend = userRepository.findById(friendId)
                .orElseThrow(() -> new IllegalArgumentException("Utilisateur non trouvé"));

        try {
            user.getConnections().add(friend);
            friend.getConnections().add(user);

            userRepository.saveAll(Set.of(user, friend));
            log.info("Relation {} added successfully to user account", friend.getUsername());
        } catch (Exception e) {
            throw new RuntimeException("Une erreur s'est produite lors de l'ajout de la relation");
        }

    }

}
