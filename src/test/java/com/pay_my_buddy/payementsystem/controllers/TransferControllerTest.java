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

@SpringBootTest
@Transactional
@AutoConfigureMockMvc
public class TransferControllerTest {
    @Autowired
    private MockMvc mockMvc;

    @Test
    @WithMockUser(username = "diana@example.com", password = "pwdtest")
    public void testHomePage() throws Exception {
        mockMvc.perform(get("/home")
                        .with(org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.user("diana@example.com")))
                .andExpect(status().isOk())
                .andExpect(model().attributeExists("transactions"))
                .andExpect(model().attributeExists("connections"))
                .andExpect(model().attributeExists("currentPage"));

    }

    @Test
    public void testHomePageWithoutAuth() throws Exception {
        mockMvc.perform(get("/home"))
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl("/login"));
    }

    @Test
    @WithMockUser(username = "diana@example.com", password = "pwdtest")
    public void testAddTransaction() throws Exception {
        mockMvc.perform(post("/transfer")
                        .with(org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.user("diana@example.com"))
                        .param("receiver", "1")
                        .param("description", "Test transaction")
                        .param("amount", "100")
                )
                .andExpect(status().is3xxRedirection())
                .andExpect(flash().attributeExists("success"))
                .andExpect(redirectedUrl("/home"));


    }

    @Test
    @WithMockUser(username = "diana@example.com", password = "pwdtest")
    public void testAddTransactionWithInvalidParam() throws Exception {
        mockMvc.perform(post("/transfer")
                        .with(org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.user("diana@example.com"))
                        .param("receiver", "157463763737637")
                        .param("description", "failed test")
                        .param("amount", "100")
                )
                .andExpect(model().attributeExists("errors"));
    }
}
