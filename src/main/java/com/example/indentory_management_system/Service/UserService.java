package com.example.indentory_management_system.Service;
import java.util.List;

import com.example.indentory_management_system.dto.UserRequestdto;
import com.example.indentory_management_system.dto.UserResponsedto;
public interface UserService {
    UserResponsedto createUser(UserRequestdto userRequestdto);
    List<UserResponsedto> getUserAllusers();
    UserResponsedto updateuser(Long id, UserRequestdto userRequestdto);
    UserResponsedto deleteuser(Long id);
    com.example.indentory_management_system.Entity.Users findByEmail(String email);
}
