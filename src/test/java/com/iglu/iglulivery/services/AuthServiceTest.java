package com.iglu.iglulivery.services;

import com.iglu.iglulivery.auth.AuthResponse;
import com.iglu.iglulivery.auth.LoginRequest;
import com.iglu.iglulivery.dto.RegisterRequest;
import com.iglu.iglulivery.entities.User;
import com.iglu.iglulivery.repositories.UserRepository;
import com.iglu.iglulivery.security.JwtUtils;
import com.iglu.iglulivery.servicies.AuthService;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.util.Objects;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class AuthServiceTest {
    @Mock
    private UserRepository userRepository;

    @Mock
    private PasswordEncoder passwordEncoder;

    @Mock
    private JwtUtils jwtUtils;

    @Mock
    private AuthenticationManager authenticationManager;

    @InjectMocks
    private AuthService authService;

    @Test
    void login_WithValidCredentials_ShouldReturnToken() {
        LoginRequest request = new LoginRequest();
        request.setEmail("usuario@test.com");
        request.setPassword("123456");
        User mockUser = new User();
        mockUser.setEmail("usuario@test.com");
        when(userRepository.findByEmail("usuario@test.com")).thenReturn(Optional.of(mockUser));
        when(jwtUtils.generateToken(any(org.springframework.security.core.userdetails.UserDetails.class)))
                .thenReturn("false-jwt-token-but-valid");
        AuthResponse response = authService.login(request);
        assertNotNull(response);
        assertEquals("false-jwt-token-but-valid", response.getToken());
        verify(authenticationManager, times(1))
                .authenticate(any(UsernamePasswordAuthenticationToken.class));
    }

    @Test
    void register_WithNewUser_ShouldSaveAndReturnToken() {
        RegisterRequest request = new RegisterRequest();
        request.setUsername("New");
        request.setEmail("new@test.com");
        request.setPassword("secreta");
        User savedUser = new User();
        savedUser.setEmail("new@test.com");
        when(userRepository.findByEmail("new@test.com")).thenReturn(Optional.empty());
        when(passwordEncoder.encode("secreta")).thenReturn("encrypted-secreta");
        when(userRepository.save(any(User.class))).thenReturn(savedUser);
        when(jwtUtils.generateToken(any(org.springframework.security.core.userdetails.UserDetails.class)))
                .thenReturn("register-token");
        AuthResponse response = authService.register(request);
        assertNotNull(response);
        assertEquals("register-token", response.getToken());
        verify(userRepository, times(1)).save(any(User.class));
    }

}
