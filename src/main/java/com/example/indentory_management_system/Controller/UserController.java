package com.example.indentory_management_system.Controller;

import java.util.List;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.example.indentory_management_system.Service.UserService;
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
    public ResponseEntity<UserResponsedto> createUser(@Valid @RequestBody UserRequestdto dto) {
        UserResponsedto user = userService.createUser(dto);
        return ResponseEntity.status(201).body(user);
    }

    @GetMapping("/allusers")
    public ResponseEntity<List<UserResponsedto>> getAllusers() {
        return ResponseEntity.ok().body(userService.getUserAllusers());
    }

    @PutMapping("/updateuser/{id}")
    public ResponseEntity<UserResponsedto> updateuser(@PathVariable Long id,
            @Valid @RequestBody UserRequestdto userRequestdto) {
        return ResponseEntity.ok().body(userService.updateuser(id, userRequestdto));
    }
}
