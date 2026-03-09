package com.pay_my_buddy.payementsystem.controllers;

import com.pay_my_buddy.payementsystem.model.User;
import com.pay_my_buddy.payementsystem.repository.UserRepository;
import com.pay_my_buddy.payementsystem.service.UserService;
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

import java.util.Optional;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

/**
 * Test class for the TransferController, responsible for handling money transfer operations.
 * This class uses Spring Boot's testing framework to perform integration tests on the controller's endpoints.
 */
@SpringBootTest
@Transactional
@AutoConfigureMockMvc
@ActiveProfiles("test")
public class TransactionControllerTest {
    @Autowired
    private MockMvc mockMvc;

    @Autowired
    UserRepository userRepository;

    @Autowired
    PasswordEncoder passwordEncoder;

    @Autowired
    UserService userService;

    @BeforeEach
    void setup() {
        User user = new User();
        user.setUsername("diana");
        user.setEmail("diana@mail.com");
        user.setPassword(passwordEncoder.encode("password"));


        userRepository.save(user);

        User user2 = new User();
        user2.setUsername("alice");
        user2.setEmail("alice@email.com");
        user2.setPassword(passwordEncoder.encode("password"));
        userRepository.save(user2);

        userService.addConnection(user.getId(), user2.getId());
    }

    /**
     * Tests the GET request to the "/home" endpoint, ensuring that the home page is displayed correctly for an authenticated user and that the necessary model attributes are present.
     *
     * @throws Exception if an error occurs during the test execution
     */
    @Test
    @WithMockUser(username = "diana@mail.com")
    public void testHomePage() throws Exception {
        mockMvc.perform(get("/home")
                        .with(org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.user("diana@mail.com")))
                .andExpect(status().isOk())
                .andExpect(model().attributeExists("transactions"))
                .andExpect(model().attributeExists("connections"))
                .andExpect(model().attributeExists("currentPage"));

    }
/**
     * Tests the GET request to the "/home" endpoint without authentication, ensuring that the user is redirected to the login page.
     *
     * @throws Exception if an error occurs during the test execution
     */
    @Test
    public void testHomePageWithoutAuth() throws Exception {
        mockMvc.perform(get("/home"))
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl("/login"));
    }

    /**
     * Tests the POST request to the "/transfer" endpoint, ensuring that a transaction can be added successfully and that the appropriate flash message is set upon successful addition.
     *
     * @throws Exception if an error occurs during the test execution
     */
    @Test
    @WithMockUser(username = "diana@mail.com")
    public void testAddTransaction() throws Exception {
        Optional<User> receiver = userRepository.findByEmail("alice@email.com");
        String receiverId= String.valueOf(receiver.get().getId());
        mockMvc.perform(post("/transaction")
                        .with(org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.user("diana@mail.com"))
                        .param("receiver", receiverId)
                        .param("description", "Test transaction")
                        .param("amount", "100")
                )
                .andExpect(status().is3xxRedirection())
                .andExpect(flash().attributeExists("success"))
                .andExpect(redirectedUrl("/home"));


    }

    /**
     * Tests the POST request to the "/transfer" endpoint with invalid parameters, ensuring that the appropriate error messages are set in the model when the transaction addition fails.
     *
     * @throws Exception if an error occurs during the test execution
     */
    @Test
    @WithMockUser(username = "diana@mail.com")
    public void testAddTransactionWithInvalidParam() throws Exception {
        mockMvc.perform(post("/transaction")
                        .with(org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.user("diana@example.com"))
                        .param("receiver", "157463763737637")
                        .param("description", "failed test")
                        .param("amount", "100")
                )
                .andExpect(model().attributeExists("errors"));
    }
}
