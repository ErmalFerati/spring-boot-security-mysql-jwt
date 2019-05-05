package com.ermalferati.security.controller;

import com.ermalferati.security.payload.CreateUserRequest;
import com.ermalferati.security.service.UserService;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;

import static com.ermalferati.security.UserServiceTestUtil.buildValidCreateUserRequest;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@RunWith(SpringRunner.class)
@SpringBootTest
@AutoConfigureMockMvc
public class UserControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private UserService userService;

    private ObjectMapper objectMapper = new ObjectMapper();

    @Test
    public void create_MissingRequestBody_ReturnsBadRequest() throws Exception {
        mockMvc.perform(post("/api/users/register"))
                .andExpect(status().isBadRequest());
    }

    @Test
    @WithMockUser(roles = "ADMIN")
    public void createWithRoles_MissingRequestBody_ReturnsBadRequest() throws Exception {
        mockMvc.perform(post("/api/users/create"))
                .andExpect(status().isBadRequest());
    }

    @Test
    public void createWithRoles_MissingRequestBody_ReturnsNotAuthorized() throws Exception {
        mockMvc.perform(post("/api/users/create"))
                .andExpect(status().isUnauthorized());
    }

    @Test
    public void create_InvalidCreateUserRequestParameters_ReturnsBadRequest() throws Exception {
        String createUserRequest = buildInvalidCreateUserRequestContent();

        mockMvc.perform(post("/api/users/register")
                .contentType(MediaType.APPLICATION_JSON)
                .content(createUserRequest))
                .andExpect(status().isBadRequest());
    }

    @Test
    public void createWithRoles_InvalidCreateUserRequestParameters_ReturnsBadRequest() throws Exception {
        String createUserRequest = buildInvalidCreateUserRequestContent();

        mockMvc.perform(post("/api/users/register")
                .contentType(MediaType.APPLICATION_JSON)
                .content(createUserRequest))
                .andExpect(status().isBadRequest());
    }

    @Test
    public void create_ValidCreateUserRequestParameters_CreatesUser() throws Exception {
        String createUserRequest = buildValidCreateUserRequestContent();

        mockUserCreation();

        mockMvc.perform(post("/api/users/register")
                .contentType(MediaType.APPLICATION_JSON)
                .content(createUserRequest))
                .andExpect(status().isCreated());
    }

    private String buildValidCreateUserRequestContent() throws JsonProcessingException {
        CreateUserRequest createUserRequest = buildValidCreateUserRequest();
        return objectMapper.writeValueAsString(createUserRequest);
    }

    private String buildInvalidCreateUserRequestContent() throws JsonProcessingException {
        CreateUserRequest createUserRequest = new CreateUserRequest();
        return objectMapper.writeValueAsString(createUserRequest);
    }

    private void mockUserCreation() {
        when(userService.create(any())).thenReturn(null);
    }

}
