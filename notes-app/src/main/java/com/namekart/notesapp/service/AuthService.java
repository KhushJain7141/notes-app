package com.namekart.notesapp.service;

import com.namekart.notesapp.DTO.LoginRequestDTO;
import com.namekart.notesapp.DTO.RegisterRequestDTO;
import com.namekart.notesapp.model.User;
import com.namekart.notesapp.repository.UserRepository;
import com.namekart.notesapp.util.JwtUtil;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.HashSet;
import java.util.Optional;
import java.util.Set;

@Service
public class AuthService {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final UserService userService;
    private final JwtUtil jwtUtil;
    private final Set<String> blacklistedTokens = new HashSet<>();

    public AuthService(PasswordEncoder passwordEncoder, UserService userService, JwtUtil jwtUtil,UserRepository userRepository) {
        this.userRepository=userRepository;
        this.userService = userService;
        this.passwordEncoder = passwordEncoder;
        this.jwtUtil = jwtUtil;
    }
    public Optional<String> authenticate(LoginRequestDTO loginRequestDTO) {
        Optional<String> token = userService.findByEmail(loginRequestDTO.getEmail()).filter(u->passwordEncoder.matches(loginRequestDTO.getPassword(),u.getPassword())).map(u->jwtUtil.generateToken(u.getEmail()));
        return token;
    }
    public String register(RegisterRequestDTO request) {
        if (userRepository.findByEmail(request.getEmail()).isPresent()) {
            throw new RuntimeException("User with this email already exists");
        }

        User user = new User();
        user.setEmail(request.getEmail());
        user.setPassword(passwordEncoder.encode(request.getPassword()));

        userRepository.save(user);

        return jwtUtil.generateToken(user.getEmail());
    }

    public boolean logout(String token) {
        if (jwtUtil.validateToken(token)) {
            blacklistedTokens.add(token);
            return true;
        }
        return false;
    }

    public boolean isTokenBlacklisted(String token) {
        return blacklistedTokens.contains(token);
    }

}


