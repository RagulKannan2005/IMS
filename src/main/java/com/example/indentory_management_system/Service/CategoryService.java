package com.example.indentory_management_system.Service;

import java.util.List;
import com.example.indentory_management_system.dto.*;

public interface CategoryService {
    CategoryResponsedto createCategory(CategoryRequestdto categoryRequestdto);

    // CategoryResponsedto getCategoryById(Long id);
    List<CategoryResponsedto> getAllCategories();

    List<CategoryResponsedto> getActiveCategories();

    CategoryResponsedto updateCategory(Long id, CategoryRequestdto categoryRequestdto);

    CategoryResponsedto deleteCategory(Long id);

    CategoryResponsedto getbycategorydescription(String description);
}
