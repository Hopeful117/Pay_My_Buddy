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
 * Test class for RelationController, testing the relation page and adding relations.
 */
@SpringBootTest
@Transactional
@AutoConfigureMockMvc
public class RelationControllerTest {
    @Autowired
    MockMvc mockMvc;


    /**
     * Test the relation page access with authentication.
     * Expects a successful response, the presence of the "currentPage" attribute in the model, and the correct view name.
     */
    @Test
    @WithMockUser(username = "diana@example.com", password = "pwdtest")
    public void testRelationPage() throws Exception {
        mockMvc.perform(get("/relations")
                        .with(org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.user("diana@example.com")))
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
    @WithMockUser(username = "diana@example.com", password = "pwdtest")
    public void testAddRelation() throws Exception {
        mockMvc.perform(post("/relations")
                        .with(org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.user("diana@example.com"))
                        .param("email", "alice@example.com"))
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
                        .with(org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.user("diana@example.com"))
                        .param("email", "jeanne@example.com"))

                .andExpect(model().attributeExists("errors"));


    }

}
