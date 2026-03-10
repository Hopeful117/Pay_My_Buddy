package com.pay_my_buddy.payementsystem.configuration;

import com.pay_my_buddy.payementsystem.repository.UserRepository;
import lombok.AllArgsConstructor;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * A custom implementation of the UserDetailsService interface to load user-specific data for authentication and authorization purposes.
 */
@Service
@AllArgsConstructor
public class CustomUserDetailsService implements UserDetailsService {
    private final UserRepository userRepository;

    /**
     * Loads the user details based on the provided email. It retrieves the user from the database and constructs a UserDetails object with the user's email, password, and authorities.
     * @param email the email of the user to be loaded
     * @return UserDetails object containing the user's email, password, and authorities.
     */
    @Override
    public UserDetails loadUserByUsername(String email) {
        return userRepository.findByEmail(email)
                .map(user -> new User(
                        user.getEmail(),
                        user.getPassword(),
                        List.of(new SimpleGrantedAuthority("AUTHENTICATED"))))
                .orElseThrow(() -> new UsernameNotFoundException("User not found"));

    }
}
