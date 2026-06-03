package com.example.indentory_management_system;

import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.security.crypto.password.PasswordEncoder;

import com.example.indentory_management_system.Entity.Users;
import com.example.indentory_management_system.Repository.UserRepository;

@SpringBootApplication
public class IndentoryManagementSystemApplication {

	public static void main(String[] args) {
		SpringApplication.run(IndentoryManagementSystemApplication.class, args);
	}

	@Bean
	public CommandLineRunner bootstrapData(UserRepository userRepository, PasswordEncoder passwordEncoder) {
		return args -> {
			if (userRepository.findByUsername("admin").isEmpty()) {
				// Seed Admin
				userRepository.save(Users.builder()
						.username("admin")
						.firstName("Admin")
						.lastName("User")
						.email("admin@ims.com")
						.password(passwordEncoder.encode("admin123"))
						.phone_number("1234567890")
						.role("ADMIN")
						.build());
				System.out.println("Bootstrap: admin user created.");
			}
			
			if (userRepository.findByUsername("manager").isEmpty()) {
				// Seed Manager
				userRepository.save(Users.builder()
						.username("manager")
						.firstName("Manager")
						.lastName("User")
						.email("manager@ims.com")
						.password(passwordEncoder.encode("manager123"))
						.phone_number("0987654321")
						.role("MANAGER")
						.build());
				System.out.println("Bootstrap: manager user created.");
			}

			if (userRepository.findByUsername("staff").isEmpty()) {
				// Seed Staff
				userRepository.save(Users.builder()
						.username("staff")
						.firstName("Staff")
						.lastName("User")
						.email("staff@ims.com")
						.password(passwordEncoder.encode("staff123"))
						.phone_number("1122334455")
						.role("STAFF")
						.build());
				System.out.println("Bootstrap: staff user created.");
			}
		};
	}
}
