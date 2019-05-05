package com.ermalferati.security.controller;

import com.ermalferati.security.model.UserPrincipal;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;

import static com.ermalferati.security.AuthenticationTestUtil.authenticateUser;
import static com.ermalferati.security.AuthenticationTestUtil.buildUserPrincipal;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@RunWith(SpringRunner.class)
@SpringBootTest
@AutoConfigureMockMvc
public class AuthenticationControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Test
    public void getCurrentUser_IsNotAuthenticated_ReturnsUnauthorizedRequest() throws Exception {
        mockMvc.perform(get("/api/auth")).andExpect(status().isUnauthorized());
    }

    @Test
    public void getCurrentUser_IsAuthenticated_ReturnsCurrentUser() throws Exception {
        UserPrincipal userPrincipal = buildUserPrincipal();
        authenticateUser(userPrincipal);

        mockMvc.perform(get("/api/auth"))
                .andExpect(status().isOk());
    }
}
