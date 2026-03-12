package com.pay_my_buddy.payementsystem.controllers;

import com.pay_my_buddy.payementsystem.model.User;
import com.pay_my_buddy.payementsystem.repository.UserRepository;
import jakarta.transaction.Transactional;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.webmvc.test.autoconfigure.AutoConfigureMockMvc;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;

import java.math.BigDecimal;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

/**
 * Test class for RelationController, testing the relation page and adding relations.
 */
@SpringBootTest
@Transactional
@AutoConfigureMockMvc
@ActiveProfiles("test")
public class RelationControllerTest {
    @Autowired
    MockMvc mockMvc;

    @Autowired
    UserRepository userRepository;

    @Autowired
    PasswordEncoder passwordEncoder;



    @BeforeEach
    void setup() {
        User user = new User();
        user.setUsername("diana");
        user.setEmail("diana@mail.com");
        user.setPassword(passwordEncoder.encode("password"));
        user.setBalance(BigDecimal.valueOf(1000));

        userRepository.save(user);

        User user2 = new User();
        user2.setUsername("alice");
        user2.setEmail("alice@email.com");
        user2.setPassword(passwordEncoder.encode("password"));
        user2.setBalance(BigDecimal.valueOf(1000));
        userRepository.save(user2);


    }


    /**
     * Test the relation page access with authentication.
     * Expects a successful response, the presence of the "currentPage" attribute in the model, and the correct view name.
     */
    @Test
    @WithMockUser(username = "diana@mail.com")
    public void testRelationPage() throws Exception {
        mockMvc.perform(get("/relations")
                        .with(org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.user("diana@mail.com")))
                .andExpect(status().isOk())
                .andExpect(model().attributeExists("currentPage"))
                .andExpect(view().name("relations"));


    }

    /**
     * Test the relation page access without authentication.
     * Expects a redirection to the login page.
     */
    @Test
    public void testRelationPageWithoutAuth() throws Exception {
        mockMvc.perform(get("/relations"))
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl("/login"));
    }

    /**
     * Test adding a relation with valid parameters.
     * Expects a redirection to the relations page and the presence of a success message in the flash attributes.
     */
    @Test
    @WithMockUser(username = "diana@mail.com")
    public void testAddRelation() throws Exception {
        mockMvc.perform(post("/relations")
                        .with(org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.user("diana@mail.com"))
                        .param("email", "alice@email.com"))
                .andExpect(status().is3xxRedirection())
                .andExpect(flash().attributeExists("success"));


    }

    /**
     * Test adding a relation with invalid parameters (e.g., non-existent email).
     * Expects the presence of an "errors" attribute in the model.
     */
    @Test
    @WithMockUser(username = "diana@example.com", password = "pwdtest")
    public void testAddRelationWithInvalidParameters() throws Exception {
        mockMvc.perform(post("/relations")
                        .with(org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.user("diana@mail.com"))
                        .param("email", "jeanne@example.com"))

                .andExpect(model().attributeExists("errors"));


    }

}
