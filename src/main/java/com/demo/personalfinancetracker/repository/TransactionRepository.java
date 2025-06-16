package com.demo.personalfinancetracker.repository;

import com.demo.personalfinancetracker.model.Category;
import com.demo.personalfinancetracker.model.Transaction;
import com.demo.personalfinancetracker.model.TransactionType;
import com.demo.personalfinancetracker.model.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.time.LocalDateTime;
import java.util.List;

public interface TransactionRepository extends JpaRepository<Transaction, Long> {

    List<Transaction> findByUserIdOrderByDateDesc(Long userId);

    List<Transaction> findByUserIdAndDateBetween(Long userId, LocalDateTime start, LocalDateTime end);

    List<Transaction> findByUserAndDateBetween(User user, LocalDateTime startDate, LocalDateTime endDate);

    List<Transaction> findByUserAndCategory(User user, Category category);

    List<Transaction> findByUserAndType(User user, TransactionType type);

    List<Transaction> findByUserAndDateBetweenAndType(User user, LocalDateTime startDate, LocalDateTime endDate, TransactionType type);
}
