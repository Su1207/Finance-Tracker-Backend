package com.demo.personalfinancetracker.repository;

import com.demo.personalfinancetracker.dto.CategoryDTO;
import com.demo.personalfinancetracker.model.Category;
import com.demo.personalfinancetracker.model.TransactionType;
import com.demo.personalfinancetracker.model.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface CategoryRepository extends JpaRepository<Category, Long> {
    List<Category> findByUserOrIsDefaultTrue(User user);
    boolean existsByNameAndIsDefaultTrue(String name);
    Optional<Category> findByIdAndUserOrIsDefaultTrue(Long id, User user);
    boolean existsByNameAndUser(String name, User user);
    List<Category> findByTypeAndUserOrIsDefaultTrue(TransactionType type, User user);
}
