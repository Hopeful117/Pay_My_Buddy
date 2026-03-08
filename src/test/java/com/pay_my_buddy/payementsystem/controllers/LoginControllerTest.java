package com.pay_my_buddy.payementsystem.controllers;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.webmvc.test.autoconfigure.WebMvcTest;
import org.springframework.test.web.servlet.MockMvc;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.view;

/**
 * Test class for the LoginController, responsible for handling user login operations.
 * This class uses Spring Boot's testing framework to perform integration tests on the controller's endpoints.
 */
@WebMvcTest(LoginController.class)
public class LoginControllerTest {

    @Autowired
    private MockMvc mockMvc;

/**
     * Tests the GET request to the "/login" endpoint, ensuring that the login page is displayed correctly.
     *
     * @throws Exception if an error occurs during the test execution
     */
    @Test
    public void testLoginPage() throws Exception {

        mockMvc.perform(get("/login"))
                .andExpect(status().isOk())
                .andExpect(view().name("login"));
    }


}
