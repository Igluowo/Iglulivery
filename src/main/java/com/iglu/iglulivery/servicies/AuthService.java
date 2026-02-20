package com.iglu.iglulivery.servicies;

import com.iglu.iglulivery.auth.AuthResponse;
import com.iglu.iglulivery.auth.LoginRequest;
import com.iglu.iglulivery.dto.RegisterRequest;
import com.iglu.iglulivery.entities.User;
import com.iglu.iglulivery.repositories.UserRepository;
import com.iglu.iglulivery.security.JwtUtils;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class AuthService {
    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final JwtUtils jwtUtils;
    private final AuthenticationManager authenticationManager;

    public AuthResponse register(RegisterRequest request) {
        User user = new User();
        user.setUsername(request.getUsername());
        user.setEmail(request.getEmail());
        user.setPassword(passwordEncoder.encode(request.getPassword()));
        user.setRole(request.getRole());
        userRepository.save(user);
        var jwtToken = jwtUtils.generateToken(new com.iglu.iglulivery.security.CustomUserDetails(user));
        return AuthResponse.builder()
                .token(jwtToken)
                .build();
    }

    public AuthResponse login(LoginRequest request) {
        authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(
                        request.getEmail(), request.getPassword()
                )
        );

        var user = userRepository.findByEmail(request.getEmail()).orElseThrow();

        var jwtToken = jwtUtils.generateToken(new com.iglu.iglulivery.security.CustomUserDetails(user));

        return AuthResponse.builder()
                .token(jwtToken)
                .build();
    }
}
