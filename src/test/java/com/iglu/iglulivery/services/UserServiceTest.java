package com.iglu.iglulivery.services;

import com.iglu.iglulivery.entities.User;
import com.iglu.iglulivery.repositories.UserRepository;
import com.iglu.iglulivery.servicies.UserService;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class UserServiceTest {
    @Mock
    private UserRepository userRepository;

    @InjectMocks
    private UserService userService;

    @Test
    void getUserByEmail_WhenUserExists_ShouldReturnOptionalWithUser() {
        String email = "test@iglulivery.com";
        User mockUser = new User();
        mockUser.setEmail(email);
        when(userRepository.findByEmail(email)).thenReturn(Optional.of(mockUser));
        Optional<User> result = userService.getUserByEmail(email);
        assertTrue(result.isPresent());
        assertEquals(email, result.get().getEmail());
        verify(userRepository, times(1)).findByEmail(email);
    }

    @Test
    void getUserByEmail_WhenUserDoesNotExist_ShouldReturnEmptyOptional() {
        String email = "fantasma@iglulivery.com";
        when(userRepository.findByEmail(email)).thenReturn(Optional.empty());
        Optional<User> result = userService.getUserByEmail(email);
        assertTrue(result.isEmpty());
        verify(userRepository, times(1)).findByEmail(email);
    }
}
