package com.pay_my_buddy.payementsystem.controllers;

import jakarta.transaction.Transactional;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.webmvc.test.autoconfigure.AutoConfigureMockMvc;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

/**
 * Test class for the ProfileController, responsible for handling user profile-related operations.
 * This class uses Spring Boot's testing framework to perform integration tests on the controller's endpoints.
 */
@SpringBootTest
@AutoConfigureMockMvc
@Transactional
public class ProfileControllerTest {


    @Autowired
    MockMvc mockMvc;

    /**
     * Tests the GET request to the "/profile" endpoint, ensuring that the profile page is displayed correctly for an authenticated user.
     *
     * @throws Exception if an error occurs during the test execution
     */
    @Test
    @WithMockUser(username = "diana@example.com", password = "pwdtest")
    public void testProfilePage() throws Exception {

        mockMvc.perform(get("/profile")
                        .with(org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.user("diana@example.com")))
                .andExpect(status().isOk())
                .andExpect(model().attributeExists("updateDTO"))
                .andExpect(model().attributeExists("currentPage"))
                .andExpect(view().name("profile"));

    }

    /**
     * Tests the GET request to the "/profile" endpoint without authentication, ensuring that the user is redirected to the login page.
     *
     * @throws Exception if an error occurs during the test execution
     */
    @Test
    public void testProfilePageWithoutAuthentication() throws Exception {
        mockMvc.perform(get("/profile"))
                .andExpect(status().is3xxRedirection())
                .andExpect((redirectedUrl("/login")))
        ;

    }


    /**
     * Tests the POST request to the "/profile" endpoint, ensuring that the user's profile information is updated successfully for an authenticated user.
     *
     * @throws Exception if an error occurs during the test execution
     */
    @Test
    @WithMockUser(username = "diana@example.com", password = "pwdtest")
    public void testUpdateProfile() throws Exception {


        mockMvc.perform(post("/profile")
                        .with(org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.user("diana@example.com"))
                        .param("username", "diana")
                        .param("email", "diana@example.com")
                        .param("password", "newpassword"))
                .andExpect(status().is3xxRedirection())
                .andExpect(flash().attributeExists("success"));


    }

    /**
     * Tests the POST request to the "/profile" endpoint with invalid input, ensuring that validation errors are handled correctly and the user is informed of the issues.
     *
     * @throws Exception if an error occurs during the test execution
     */
    @Test
    @WithMockUser(username = "diana@example.com", password = "pwdtest")
    public void testUpdateProfileWithInvalidArgument() throws Exception {

        mockMvc.perform(post("/profile")
                        .with(org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.user("diana@example.com"))
                        .param("username", "alice")
                        .param("email", "diana@example.com")
                        .param("password", "newpassword"))
                .andExpect(model().attributeExists("errors"));


    }


}
