package com.demo.personalfinancetracker.controller;

import com.demo.personalfinancetracker.dto.CategoryDTO;
import com.demo.personalfinancetracker.model.Category;
import com.demo.personalfinancetracker.model.TransactionType;
import com.demo.personalfinancetracker.service.CategoryService;
import jakarta.servlet.http.HttpSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/categories")
@CrossOrigin(
        allowCredentials = "true",
        origins = {
                "http://localhost:5173",
                "https://personal-transaction-management-sys.vercel.app"
        }
)
public class CategoryController {

    @Autowired
    private CategoryService categoryService;

    @GetMapping
    public ResponseEntity<?> getAllCategories(HttpSession session) {

        return ResponseEntity.ok(categoryService.getAllCategories(session));
    }

    @PostMapping
    public ResponseEntity<?> createCategory(@RequestBody Category category, HttpSession session) {
        try {

            CategoryDTO.Response savedCategory = categoryService.createCategory(category, session);
            return ResponseEntity.ok(savedCategory);
        } catch (RuntimeException ex) {
            return ResponseEntity.badRequest().body(ex.getMessage());
        }
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<?> deleteCategory(@PathVariable Long id, HttpSession session) {
        try {
            boolean deleted = categoryService.deleteCategory(id, session);
            return ResponseEntity.ok("Category deleted successfully");
        } catch (RuntimeException ex) {
            return ResponseEntity.badRequest().body(ex.getMessage());
        }
    }

    @GetMapping("/type/{type}")
    public ResponseEntity<?> getCategoriesByType(@PathVariable TransactionType type, HttpSession session) {
        return ResponseEntity.ok(categoryService.getCategoriesByType(type, session));
    }
}
