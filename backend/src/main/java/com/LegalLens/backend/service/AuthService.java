package com.LegalLens.backend.service;

import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.password.PasswordEncoder;

import com.LegalLens.backend.dto.JwtAuthenticationResponse;
import com.LegalLens.backend.dto.LoginRequest;
import com.LegalLens.backend.dto.RegisterRequest;
import com.LegalLens.backend.model.User;
import com.LegalLens.backend.repository.UserRepository;
import com.LegalLens.backend.security.JwtUtil;

public class AuthService {
    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final JwtUtil jwtutil;
    private final AuthenticationManager authenticationManager;
    public AuthService(UserRepository userRepository,PasswordEncoder passwordEncoder,JwtUtil jwtutil,AuthenticationManager authenticationManager){
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
        this.jwtutil = jwtutil;
        this.authenticationManager = authenticationManager;
    }

    public JwtAuthenticationResponse login(LoginRequest loginRequest){
        Authentication authentication = authenticationManager.authenticate(
            new UsernamePasswordAuthenticationToken(loginRequest.getUsername(), loginRequest.getPassword())
        );
        User user = userRepository.findByUsername(loginRequest.getUsername())
                .orElseThrow(() -> new RuntimeException("User not found"));
        UserDetails userDetails = (UserDetails) authentication.getPrincipal();
        String token = jwtutil.GenerateToken(userDetails);
        return new JwtAuthenticationResponse(token, user.getUsername(), user.getRole()); 
    }

    public User register(RegisterRequest registerRequest){
        if (userRepository.existsByUsername(registerRequest.getUsername())) {
            throw new RuntimeException("Username already exists");
        }
        if (userRepository.existsByEmail(registerRequest.getEmail())) {
            throw new RuntimeException("Email already exists");
        }

        User user = new User(
            registerRequest.getUsername(),
            passwordEncoder.encode(registerRequest.getPassword()),
            registerRequest.getRole(),
            registerRequest.getFullName(),
            registerRequest.getEmail()
        );

        User saved = userRepository.save(user);

        return saved;
    }

}
