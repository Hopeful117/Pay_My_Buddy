package com.pay_my_buddy.payementsystem.configuration;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
/**
 * Configuration class for Spring Security, responsible for setting up authentication and authorization rules for the application.
 * This class defines the security filter chain, password encoder, and authentication manager to manage user authentication and access control.
 */
@Configuration
@EnableWebSecurity
public class SpringSecurityConfig {

    @Autowired
    private CustomUserDetailsService customUserDetailsService;
/**
     * Configures the security filter chain for the application, defining the rules for authentication and authorization.
     *
     * @param http the HttpSecurity object used to configure security settings
     * @return a SecurityFilterChain object that defines the security configuration for the application
     * @throws Exception if an error occurs during the configuration process
     */
    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
        http.csrf(AbstractHttpConfigurer::disable);
        return http.authorizeHttpRequests(auth -> {
                    auth.requestMatchers("/swagger-ui.html", "/swagger-ui/**", "/v3/api-docs/**").permitAll();
                    auth.requestMatchers("/register", "/login", "/**.css/**", "/js/**").permitAll();
                    auth.anyRequest().authenticated();
                }).formLogin(form -> form.loginPage("/login").usernameParameter("email").defaultSuccessUrl("/home", true).permitAll())
                .build();
    }
/**
     * Defines a bean for the password encoder, using BCryptPasswordEncoder to securely hash user passwords.
     *
     * @return a PasswordEncoder object that can be used to encode and verify user passwords
     */
    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }
/**
     * Configures the authentication manager for the application, setting up the user details service and password encoder.
     *
     * @param http            the HttpSecurity object used to configure security settings
     * @param passwordEncoder the PasswordEncoder bean used to encode and verify user passwords
     * @return an AuthenticationManager object that manages user authentication for the application
     * @throws Exception if an error occurs during the configuration process
     */
    @Bean
    public AuthenticationManager authentificationManager(HttpSecurity http, PasswordEncoder passwordEncoder) throws Exception {
        AuthenticationManagerBuilder authenticationManagerBuilder = http.getSharedObject(AuthenticationManagerBuilder.class);
        authenticationManagerBuilder.userDetailsService(customUserDetailsService).passwordEncoder(passwordEncoder);
        return authenticationManagerBuilder.build();


    }


}

