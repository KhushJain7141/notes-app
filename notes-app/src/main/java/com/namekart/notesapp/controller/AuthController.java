package com.namekart.notesapp.controller;
import com.namekart.notesapp.DTO.AuthResponseDTO;
import com.namekart.notesapp.DTO.LoginRequestDTO;
import com.namekart.notesapp.DTO.LoginResponseDTO;
import com.namekart.notesapp.DTO.RegisterRequestDTO;
import com.namekart.notesapp.service.AuthService;
import io.swagger.v3.oas.annotations.Operation;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Optional;

@RestController
@RequestMapping("/api/users")
public class AuthController {
    private final AuthService authService;

    public AuthController(AuthService authService) {
        this.authService = authService;
    }

    @Operation(summary = "Generate token on user login")
    @PostMapping("/login")
    public ResponseEntity<LoginResponseDTO> login(@RequestBody LoginRequestDTO loginRequestDTO) {
        Optional<String> tokenOptional = authService.authenticate(loginRequestDTO);
        if (tokenOptional.isEmpty()) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
        }
        String token = tokenOptional.get();
        return ResponseEntity.ok(new LoginResponseDTO(token));
    }




    @Operation(summary = "Logout user and invalidate JWT")
    @PostMapping("/logout")
    public ResponseEntity<String> logout(@RequestHeader(value = "Authorization", required = false) String token) {
        if (token == null || token.isBlank()) {
            return ResponseEntity.badRequest().body("Missing Authorization header");
        }

        if (token.startsWith("Bearer ")) {
            token = token.substring(7);
        }

        boolean success = authService.logout(token);
        if (success) {
            return ResponseEntity.ok("Logged out successfully");
        } else {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Invalid or expired token");
        }
    }

    @Operation(summary = "To register a new user")
    @PostMapping("/register")
    public ResponseEntity<AuthResponseDTO> register(@RequestBody RegisterRequestDTO registerRequestDTO) {
        try {
            String token = authService.register(registerRequestDTO);
            return ResponseEntity.status(HttpStatus.CREATED).body(new AuthResponseDTO(token));
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).build();
        }
    }

}
