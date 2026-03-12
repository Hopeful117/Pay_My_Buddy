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
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;

import java.math.BigDecimal;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@SpringBootTest
@AutoConfigureMockMvc
@Transactional
@ActiveProfiles("test")
public class RegisterControllerTest {
    @Autowired
    private MockMvc mockMvc;

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
    }

    /**
     * Tests the GET request to the "/register" endpoint, ensuring that the registration page is displayed correctly with the necessary model attributes.
     *
     * @throws Exception if an error occurs during the test execution
     */
    @Test
    public void testGetRegisterPage() throws Exception {
        mockMvc.perform(get("/register"))
                .andExpect(status().isOk())
                .andExpect(model().attributeExists("registerDTO"))
                .andExpect(view().name("register"));
    }

    /**
     * Tests the POST request to the "/register" endpoint, ensuring that a user can be registered successfully and that the appropriate flash message is set upon successful registration.
     *
     * @throws Exception if an error occurs during the test execution
     */
    @Test
    public void testRegisterUser() throws Exception {
        mockMvc.perform(post("/register")
                        .param("username", "testUser")
                        .param("email", "testEmail@mail.com")
                        .param("password", "testPassword"))
                .andExpect(status().is3xxRedirection())
                .andExpect(flash().attributeExists("success"));

    }

    @Test
    public void testRegisterAlreadyExistingUser() throws Exception {
        mockMvc.perform(post("/register")
                        .param("username", "diana")
                        .param("email", "diana@mail.com")
                        .param("password", "testPassword"))
                .andExpect(model().attributeExists("errors"));


    }
}
