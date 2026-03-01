package com.pay_my_buddy.payementsystem.service;

import com.pay_my_buddy.payementsystem.DTO.RegisterDTO;
import com.pay_my_buddy.payementsystem.model.User;
import com.pay_my_buddy.payementsystem.repository.UserRepository;
import jakarta.transaction.Transactional;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.Optional;
import java.util.Set;

@Service
@Slf4j
@AllArgsConstructor
public class UserServiceImpl implements UserService {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder; // pour hasher les mots de passe


    // Création d'un utilisateur
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
        // Hash du mot de passe avant sauvegarde

        userRepository.save(newUser);
        log.info("User created successfully: {}", username);

    }


    @Override
    @Transactional
    public void updateUser(final RegisterDTO registerDTO) {
        Optional<User> optionalUser = userRepository.findByEmail(registerDTO.getEmail());
        if (optionalUser.isEmpty()) {
            throw new IllegalArgumentException("User not found");
        }

        final User user = optionalUser.get();
        if (!registerDTO.getEmail().equals(user.getEmail())) {
            if (userRepository.existsByEmail(registerDTO.getEmail())) {
                throw new IllegalArgumentException("Veuillez utiliser un email différent");
            }
            user.setEmail(registerDTO.getEmail());
        }
        if (!registerDTO.getUsername().equals(user.getUsername())) {
            if (userRepository.existsByUsername(registerDTO.getUsername())) {
                throw new IllegalArgumentException("Le nom d'utilisateur est déjà utilisé");
            }
            user.setUsername(registerDTO.getUsername());

        }
        user.setPassword(passwordEncoder.encode(registerDTO.getPassword()));


        userRepository.save(user);


        log.info("User updated successfully for user {}", user.getId());
    }

    // Récupération d'un utilisateur par email
    @Override
    public User getUserByEmail(String email) {
        User user = userRepository.findByEmail(email)
                .orElseThrow(() -> new IllegalArgumentException("User not found"));

        log.info("User found: {}", user.getEmail());
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

        try{
        user.getConnections().add(friend);
        friend.getConnections().add(user);

        userRepository.saveAll(Set.of(user, friend));
        log.info("Relation {} added successfully to user account", friend.getUsername());
        } catch (Exception e) {
            throw new RuntimeException("Une erreur s'est produite lors de l'ajout de la relation");
        }

    }

}
