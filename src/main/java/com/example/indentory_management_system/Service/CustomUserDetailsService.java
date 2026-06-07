package com.example.indentory_management_system.Service;
import java.util.Collections;

import com.example.indentory_management_system.Entity.Users;
import com.example.indentory_management_system.Repository.UserRepository;

import lombok.RequiredArgsConstructor;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.*;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class CustomUserDetailsService implements UserDetailsService {

    private final UserRepository userRepository;

    @Override
    public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException {
        System.out.println("CustomUserDetailsService loading user by email: " + email);
        Users user = userRepository.findByEmail(email)
                .orElseThrow(() -> {
                    System.out.println("User not found in repository for email: " + email);
                    return new UsernameNotFoundException("User not found: " + email);
                });

        System.out.println("User found: " + user.getEmail() + ", Password hash: " + user.getPassword() + ", Role: " + user.getRole());
        return new org.springframework.security.core.userdetails.User(
                user.getEmail(),
                user.getPassword(),
                Collections.singletonList(
                        new SimpleGrantedAuthority("ROLE_" + user.getRole().toUpperCase())
                )
        );
    }
}