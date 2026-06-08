package com.example.indentory_management_system;

import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.security.crypto.password.PasswordEncoder;

import com.example.indentory_management_system.Entity.Users;
import com.example.indentory_management_system.Entity.Supplier;
import com.example.indentory_management_system.Entity.warehouses;
import com.example.indentory_management_system.Repository.UserRepository;
import com.example.indentory_management_system.Repository.SupplierRepository;
import com.example.indentory_management_system.Repository.WarehouseRepository;

@SpringBootApplication
public class IndentoryManagementSystemApplication {

	public static void main(String[] args) {
		SpringApplication.run(IndentoryManagementSystemApplication.class, args);
	}

	@Bean
	public CommandLineRunner bootstrapData(UserRepository userRepository, PasswordEncoder passwordEncoder, SupplierRepository supplierRepository, WarehouseRepository warehouseRepository) {
		return args -> {
			if (userRepository.findByUsername("admin").isEmpty()) {
				// Seed Admin
				userRepository.save(Users.builder()
						.username("admin")
						.firstName("Admin")
						.lastName("User")
						.email("admin@ims.com")
						.password(passwordEncoder.encode("Raguladmin@7"))
						.phone_number("1234567890")
						.role("ADMIN")
						.build());
				System.out.println("Bootstrap: admin user created.");
			}

			// Get the admin user to associate with warehouses
			userRepository.findByUsername("admin").ifPresent(adminUser -> {
				// 1. Seed default Warehouse for Admin if not exists
				if(warehouseRepository.findBywarehouseCode("WH-MAIN").isEmpty()) {
					warehouseRepository.save(warehouses.builder()
							.name("Main Central Hub")
							.warehouseCode("WH-MAIN")
							.capacity(5000)
							.managerName("Admin User")
							.contactNumber("1234567890")
							.email("admin@ims.com")
							.isActive("active")
							.user(adminUser)
							.build());
					System.out.println("Bootstrap: default warehouse created.");
				}

				// 2. Migrate any old warehouses that have no owner
				warehouseRepository.findAll().forEach(w -> {
					if (w.getUser() == null) {
						w.setUser(adminUser);
						warehouseRepository.save(w);
						System.out.println("Bootstrap: Migrated old warehouse " + w.getWarehouseCode() + " to admin ownership.");
					}
				});
			});
			if (userRepository.findByUsername("manager").isEmpty()) {
				// Seed Manager
				userRepository.save(Users.builder()
						.username("manager")
						.firstName("Manager")
						.lastName("User")
						.email("manager@ims.com")
						.password(passwordEncoder.encode("Manager@1234"))
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
			if(userRepository.findByUsername("supplier").isEmpty()){
				// Seed Supplier
				Users supplierUser = userRepository.save(Users.builder()
						.username("supplier")
						.firstName("Supplier")
						.lastName("User")
						.email("supplier@ims.com")
						.password(passwordEncoder.encode("Supplier@123"))
						.phone_number("2233445566")
						.role("SUPPLIER")
						.build());
				System.out.println("Bootstrap: supplier user created.");

				// Seed actual Supplier business record
				Supplier supplierRecord = Supplier.builder()
						.supplierName("Global Supplies Inc.")
						.contactPerson("Supplier User")
						.supplier_email("supplier@ims.com")
						.supplierPhone("2233445566")
						.address("123 Logistics Way")
						.status(true)
						.user(supplierUser)
						.build();
				supplierRepository.save(supplierRecord);
				System.out.println("Bootstrap: supplier business record created.");
			}
		};
	}
}
