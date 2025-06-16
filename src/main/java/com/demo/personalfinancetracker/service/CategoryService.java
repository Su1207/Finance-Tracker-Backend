package com.demo.personalfinancetracker.service;

import com.demo.personalfinancetracker.dto.CategoryDTO;
import com.demo.personalfinancetracker.model.Category;
import com.demo.personalfinancetracker.model.TransactionType;
import com.demo.personalfinancetracker.model.User;
import com.demo.personalfinancetracker.repository.CategoryRepository;
import com.demo.personalfinancetracker.repository.UserRepository;
import jakarta.servlet.http.HttpSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class CategoryService {

    @Autowired
    private CategoryRepository categoryRepository;

    @Autowired
    private UserRepository userRepository;

    private User getSessionUser(HttpSession session) {
        Long userId = (Long) session.getAttribute("userId");
        return userId == null ? null : userRepository.findById(userId).orElse(null);
    }

    public List<CategoryDTO.Response> getAllCategories(HttpSession session) {
        User user = getSessionUser(session);
        List<Category> categories = categoryRepository.findByUserOrIsDefaultTrue(user);

        return categories.stream()
                .map(c -> new CategoryDTO.Response(
                        c.getId(),
                        c.getName(),
                        c.getType(),
                        c.isDefault()
                ))
                .collect(Collectors.toList());
    }


    public CategoryDTO.Response createCategory(Category category, HttpSession session) {
        User user = getSessionUser(session);
        if (categoryRepository.existsByNameAndUser(category.getName(), user)) {
            throw new RuntimeException("Category name already exists for this user.");
        }
        category.setUser(user);
        category.setDefault(false);

        Category savedCategory = categoryRepository.save(category);

        // Return DTO to omit user details
        return new CategoryDTO.Response(
                savedCategory.getId(),
                savedCategory.getName(),
                savedCategory.getType(),
                savedCategory.isDefault()
        );
    }


    public boolean deleteCategory(Long categoryId, HttpSession session) {
        User user = getSessionUser(session);
        Optional<Category> optionalCategory = categoryRepository.findByIdAndUserOrIsDefaultTrue(categoryId, user);

        if (optionalCategory.isPresent()) {
            Category category = optionalCategory.get();
            if (category.isDefault()) {
                throw new RuntimeException("Cannot delete default category.");
            }
            categoryRepository.delete(category);
            return true;
        } else {
            throw new RuntimeException("Category not found or not accessible.");
        }
    }

    public List<Category> getCategoriesByType(TransactionType type, HttpSession session) {
        User user = getSessionUser(session);
        return categoryRepository.findByTypeAndUserOrIsDefaultTrue(type, user);
    }
}
