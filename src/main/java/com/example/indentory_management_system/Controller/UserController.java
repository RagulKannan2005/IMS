package com.example.indentory_management_system.Controller;

import java.util.List;

import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.example.indentory_management_system.Entity.Users;
import com.example.indentory_management_system.Service.UserService;
import com.example.indentory_management_system.dto.UserMeResponseDto;
import com.example.indentory_management_system.dto.UserRequestdto;
import com.example.indentory_management_system.dto.UserResponsedto;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/api/v1/users")
@RequiredArgsConstructor
public class UserController {
    private final UserService userService;

    @PostMapping("/newuser")
    @PreAuthorize("hasAnyRole('ADMIN', 'MANAGER')")
    public ResponseEntity<UserResponsedto> createUser(@Valid @RequestBody UserRequestdto dto) {
        UserResponsedto user = userService.createUser(dto);
        return ResponseEntity.status(201).body(user);
    }

    @GetMapping("/allusers")
    @PreAuthorize("hasAnyRole('ADMIN', 'MANAGER')")
    public ResponseEntity<List<UserResponsedto>> getAllusers() {
        return ResponseEntity.ok().body(userService.getUserAllusers());
    }

    @PutMapping("/updateuser/{id}")
    @PreAuthorize("hasAnyRole('ADMIN', 'MANAGER')")
    public ResponseEntity<UserResponsedto> updateuser(@PathVariable Long id,
            @Valid @RequestBody UserRequestdto userRequestdto) {
        return ResponseEntity.ok().body(userService.updateuser(id, userRequestdto));
    }

    @DeleteMapping("/deleteuser/{id}")
    @PreAuthorize("hasAnyRole('ADMIN', 'MANAGER')")
    public ResponseEntity<UserResponsedto> deleteuser(@PathVariable Long id) {
        return ResponseEntity.ok().body(userService.deleteuser(id));
    }

    @GetMapping("/me")
    @PreAuthorize("isAuthenticated()")
    public ResponseEntity<UserMeResponseDto> getCurrentUser(Authentication authentication) {
        String email = authentication.getName(); // Spring Security username stores the email
        Users user = userService.findByEmail(email);
        UserMeResponseDto dto = UserMeResponseDto.builder()
                .id(user.getId())
                .username(user.getUsername())
                .email(user.getEmail())
                .role(user.getRole())
                .firstName(user.getFirstName())
                .lastName(user.getLastName())
                .build();
        return ResponseEntity.ok(dto);
    }
}
