package com.example.indentory_management_system.ServiceImp;

import org.springframework.stereotype.Service;

import com.example.indentory_management_system.Repository.CategoriesRepository;
import com.example.indentory_management_system.Repository.UserRepository;
import com.example.indentory_management_system.Service.CategoryService;
import com.example.indentory_management_system.dto.*;
import com.example.indentory_management_system.Entity.Categories;
import com.example.indentory_management_system.Entity.Users;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.context.SecurityContextHolder;
import java.time.*;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class CategoryServiceImp implements CategoryService {
    private final CategoriesRepository categoriesRepository;
    private final UserRepository userRepository;

    @Override
    public CategoryResponsedto createCategory(CategoryRequestdto dto) {
        String username = SecurityContextHolder.getContext().getAuthentication().getName();
        Users currentUser = userRepository.findByUsername(username)
                .orElseThrow(() -> new RuntimeException("Current authenticated user not found"));

        Categories category = Categories.builder()
                .name(dto.getName())
                .description(dto.getDescription())
                .active_status(dto.getActive_status())
                .created_at(LocalDate.now())
                .updated_at(LocalDate.now())
                .user(currentUser)
                .build();
        Categories savedCategory = categoriesRepository.save(category);
        return mapToResponseDto(savedCategory);
    }

    @Override
    public List<CategoryResponsedto> getAllCategories() {
        List<Categories> categories = categoriesRepository.findAll();
        return categories.stream()
                .map(this::mapToResponseDto)
                .collect(Collectors.toList());
    }

    @Override
    public List<CategoryResponsedto> getActiveCategories() {
        List<Categories> categories = categoriesRepository.findByActiveStatusTrue();
        return categories.stream()
                .map(this::mapToResponseDto)
                .collect(Collectors.toList());
    }

    @Override
    public CategoryResponsedto updateCategory(Long id, CategoryRequestdto categoryRequestdto) {
        Categories category = categoriesRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Category not found"));
        category.setName(categoryRequestdto.getName());
        category.setDescription(categoryRequestdto.getDescription());
        category.setActive_status(categoryRequestdto.getActive_status());
        category.setUpdated_at(LocalDate.now());
        Categories updatedCategory = categoriesRepository.save(category);
        return mapToResponseDto(updatedCategory);
    }

    @Override
    public CategoryResponsedto deleteCategory(Long id) {
        Categories category = categoriesRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Category not found"));
        categoriesRepository.delete(category);
        return mapToResponseDto(category);
    }

    @Override
    public CategoryResponsedto getbycategorydescription(String description) {
        List<Categories> categories = categoriesRepository
                .findByDescriptionContainingIgnoreCase(description);
        if (categories.isEmpty()) {
            throw new RuntimeException("Category not found");
        }
        return mapToResponseDto(categories.get(0));
    }

    private CategoryResponsedto mapToResponseDto(Categories category) {
        return CategoryResponsedto.builder()
                .id(category.getId())
                .name(category.getName())
                .description(category.getDescription())
                .active_status(category.getActive_status())
                .build();
    }

}
