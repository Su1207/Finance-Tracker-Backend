package com.demo.personalfinancetracker.config;

import com.demo.personalfinancetracker.model.Category;
import com.demo.personalfinancetracker.model.TransactionType;
import com.demo.personalfinancetracker.repository.CategoryRepository;
import jakarta.annotation.PostConstruct;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.Arrays;
import java.util.List;

@Component
public class CategoryInitializer {

    @Autowired
    private CategoryRepository categoryRepository;

    private static final List<Category> DEFAULT_CATEGORIES = Arrays.asList(
            new Category(null, "Salary", TransactionType.INCOME, true, null),
            new Category(null, "Food", TransactionType.EXPENSE, true, null),
            new Category(null, "Rent", TransactionType.EXPENSE, true, null),
            new Category(null, "Transportation", TransactionType.EXPENSE, true, null),
            new Category(null, "Entertainment", TransactionType.EXPENSE, true, null),
            new Category(null, "Healthcare", TransactionType.EXPENSE, true, null),
            new Category(null, "Utilities", TransactionType.EXPENSE, true, null)
    );

    @PostConstruct
    public void initializeDefaultCategories() {
        for (Category category : DEFAULT_CATEGORIES) {
            boolean exists = categoryRepository.existsByNameAndIsDefaultTrue(category.getName());
            if (!exists) {
                categoryRepository.save(category);
            }
        }
    }
}
