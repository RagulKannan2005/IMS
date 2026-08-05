package com.example.indentory_management_system;

import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.security.crypto.password.PasswordEncoder;
import java.util.Optional;

import com.example.indentory_management_system.Entity.Users;
import com.example.indentory_management_system.Entity.Categories;
import com.example.indentory_management_system.Entity.Supplier;
import com.example.indentory_management_system.Repository.UserRepository;
import com.example.indentory_management_system.Repository.CategoriesRepository;
import com.example.indentory_management_system.Repository.SupplierRepository;
import java.util.List;
import java.util.Arrays;

@SpringBootApplication
public class IndentoryManagementSystemApplication {

	public static void main(String[] args) {
		SpringApplication.run(IndentoryManagementSystemApplication.class, args);
	}

	@Bean
	public CommandLineRunner bootstrapData(UserRepository userRepository, CategoriesRepository categoriesRepository, SupplierRepository supplierRepository, PasswordEncoder passwordEncoder) {
		return args -> {
			Optional<Users> adminOpt = userRepository.findByUsername("admin");
			if (adminOpt.isEmpty()) {
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

			if (userRepository.findByUsername("supplier").isEmpty()) {
				// Seed Supplier user & Supplier entity
				Users supplierUser = userRepository.save(Users.builder()
						.username("supplier")
						.firstName("Default")
						.lastName("Supplier")
						.email("supplier@ims.com")
						.password(passwordEncoder.encode("supplier123"))
						.phone_number("9988776655")
						.role("SUPPLIER")
						.build());
				supplierRepository.save(Supplier.builder()
						.supplierName("Default Supplier Inc.")
						.contactPerson("Default Supplier")
						.supplier_email("supplier@ims.com")
						.supplierPhone("9988776655")
						.address("123 Supply Street")
						.status(true)
						.user(supplierUser)
						.build());
				System.out.println("Bootstrap: supplier user & entity created.");
			}
      
			List<String> defaultCategories = Arrays.asList(
				"Electronics", "Books", "Stationery", "Furniture", 
				"Clothing", "Toys", "Food & Beverage", 
				"Health & Beauty", "Automotive", "Others"
			);
			for (String catName : defaultCategories) {
				if (categoriesRepository.findByName(catName).isEmpty()) {
					categoriesRepository.save(Categories.builder().name(catName).description(catName + " category").active_status("Active").build());
					System.out.println("Bootstrap: default category '" + catName + "' created.");
				}
			}
		};
	}
}
