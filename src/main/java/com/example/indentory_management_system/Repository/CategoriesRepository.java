package com.example.indentory_management_system.Repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import com.example.indentory_management_system.Entity.Categories;

public interface CategoriesRepository extends JpaRepository<Categories, Long> {

    @Query("SELECT c FROM Categories c WHERE c.active_status = 'active'")
    List<Categories> findByActiveStatusTrue();

    @Query("SELECT c FROM Categories c WHERE c.description = :description")
    Optional<Categories> findByDescription(String description);

    Optional<Categories> findByName(String name);

    List<Categories> findByDescriptionContainingIgnoreCase(String description);
}
