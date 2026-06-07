package com.example.indentory_management_system.Controller;

import java.util.HashMap;
import java.util.Map;

import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.example.indentory_management_system.Entity.Users;
import com.example.indentory_management_system.Repository.UserRepository;
import com.example.indentory_management_system.Service.JwtService;
import com.example.indentory_management_system.Service.UserService;
import com.example.indentory_management_system.dto.AuthRequest;
import com.example.indentory_management_system.dto.AuthResponse;
import com.example.indentory_management_system.dto.UserRequestdto;
import com.example.indentory_management_system.dto.UserResponsedto;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/api/v1/auth")
@RequiredArgsConstructor
public class AuthController {

    private final AuthenticationManager authenticationManager;
    private final UserDetailsService userDetailsService;
    private final UserRepository userRepository;
    private final JwtService jwtService;
    private final UserService userService;

    @PostMapping("/register")
    public ResponseEntity<UserResponsedto> register(@Valid @RequestBody UserRequestdto dto) {
        return ResponseEntity.status(201).body(userService.createUser(dto));
    }

    @PostMapping("/login")
    public ResponseEntity<AuthResponse> login(@Valid @RequestBody AuthRequest authRequest) {
        System.out.println("Login attempt - Email: " + authRequest.getEmail() + ", Password: " + authRequest.getPassword());
        try {
            authenticationManager.authenticate(
                    new UsernamePasswordAuthenticationToken(
                            authRequest.getEmail(),
                            authRequest.getPassword()
                    )
            );
            System.out.println("Authentication successful for: " + authRequest.getEmail());
        } catch (Exception e) {
            System.out.println("Authentication failed for: " + authRequest.getEmail() + " due to: " + e.getMessage());
            throw e;
        }

        Users user = userRepository.findByEmail(authRequest.getEmail())
                .orElseThrow(() -> new RuntimeException("User not found with email: " + authRequest.getEmail()));

        Map<String, Object> claims = new HashMap<>();
        Long supplierId = null;
        if (user.getSupplier() != null) {
            supplierId = user.getSupplier().getId();
            claims.put("supplierId", supplierId);
        }
        claims.put("role", user.getRole());

        final UserDetails userDetails = userDetailsService.loadUserByUsername(authRequest.getEmail());
        final String token = jwtService.generateToken(userDetails, claims);

        final String role = userDetails.getAuthorities().stream()
                .map(GrantedAuthority::getAuthority)
                .findFirst()
                .orElse("ROLE_EMPLOYEE")
                .replace("ROLE_", "");

        AuthResponse authResponse = AuthResponse.builder()
                .token(token)
                .username(userDetails.getUsername())
                .role(role)
                .supplierId(supplierId)
                .firstName(user.getFirstName())
                .lastName(user.getLastName())
                .email(user.getEmail())
                .build();

        return ResponseEntity.ok(authResponse);
    }
}
