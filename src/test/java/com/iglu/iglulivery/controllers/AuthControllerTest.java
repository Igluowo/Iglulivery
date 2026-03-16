package com.iglu.iglulivery.controllers;

import com.iglu.iglulivery.auth.AuthResponse;
import com.iglu.iglulivery.auth.LoginRequest;
import com.iglu.iglulivery.dto.RegisterRequest;
import com.iglu.iglulivery.security.JwtUtils;
import com.iglu.iglulivery.servicies.AuthService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.webmvc.test.autoconfigure.AutoConfigureMockMvc;
import org.springframework.http.MediaType;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;
import tools.jackson.databind.ObjectMapper;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc(addFilters = false)
public class AuthControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @MockitoBean
    private AuthService authService;

    @Test
    void login_WithValidCredentials_ShouldReturn200AndToken() throws Exception {
        LoginRequest loginRequest = new LoginRequest();
        loginRequest.setEmail("cliente@iglulivery.com");
        loginRequest.setPassword("123456");

        AuthResponse mockResponse = new AuthResponse();
        mockResponse.setToken("fake-jwt-token");
        when(authService.login(any(LoginRequest.class))).thenReturn(mockResponse);
        mockMvc.perform(post("/api/auth/login")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(loginRequest)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.token").value("fake-jwt-token"));
    }

    @Test
    void register_WithValidData_ShouldReturn200AndToken() throws Exception {
        RegisterRequest registerRequest = new RegisterRequest();
        registerRequest.setUsername("New User");
        registerRequest.setEmail("new@iglulivery.com");
        registerRequest.setPassword("secure123");
        AuthResponse mockResponse = new AuthResponse();
        mockResponse.setToken("new-fake-jwt-token");
        when(authService.register(any(RegisterRequest.class))).thenReturn(mockResponse);
        mockMvc.perform(post("/api/auth/register")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(registerRequest)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.token").value("new-fake-jwt-token"));
    }
}
