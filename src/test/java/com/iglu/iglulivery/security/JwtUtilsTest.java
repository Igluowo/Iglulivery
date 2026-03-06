package com.iglu.iglulivery.security;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.test.util.ReflectionTestUtils;

import java.util.ArrayList;

import static org.junit.jupiter.api.Assertions.*;

public class JwtUtilsTest {
    private JwtUtils jwtUtils;
    private UserDetails mockUser;

    @BeforeEach
    void setUp() {
        jwtUtils = new JwtUtils();
        String base64SecretKey = "VGhpcy1pcy1hLXNlY3JldC1rZXktdGhhdC1tdXN0LWJlLWF0LWxlYXN0LTMyLWJ5dGVzLWxvbmc=";
        ReflectionTestUtils.setField(jwtUtils, "secretKey", base64SecretKey);
        ReflectionTestUtils.setField(jwtUtils, "jwtExpiration", 1000 * 60 * 60);
        mockUser = new User("juan@iglulivery.com", "password123", new ArrayList<>());
    }

    @Test
    void generateToken_ShouldReturnValidToken() {
        String token = jwtUtils.generateToken(mockUser);
        assertNotNull(token);
        assertFalse(token.isEmpty());
        assertEquals(3, token.split("\\.").length);
    }

    @Test
    void extractUsername_ShouldReturnCorrectEmail() {
        String token = jwtUtils.generateToken(mockUser);
        String extractedUsername = jwtUtils.extractUsername(token);
        assertEquals("juan@iglulivery.com", extractedUsername);
    }

    @Test
    void isTokenValid_WithCorrectUser_ShouldReturnTrue() {
        String token = jwtUtils.generateToken(mockUser);
        boolean isValid = jwtUtils.isTokenValid(token, mockUser);
        assertTrue(isValid);
    }

    @Test
    void isTokenValid_WithDifferentUser_ShouldReturnFalse() {
        String token = jwtUtils.generateToken(mockUser);
        UserDetails stranger = new User("pedro@iglulivery.com", "hacker", new ArrayList<>());
        boolean isValid = jwtUtils.isTokenValid(token, stranger);
        assertFalse(isValid);
    }
}
