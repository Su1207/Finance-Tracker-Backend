package com.demo.personalfinancetracker.service;

import com.demo.personalfinancetracker.dto.TransactionDTO;
import com.demo.personalfinancetracker.exception.ResourceNotFoundException;
import com.demo.personalfinancetracker.model.Category;
import com.demo.personalfinancetracker.model.Transaction;
import com.demo.personalfinancetracker.model.TransactionType;
import com.demo.personalfinancetracker.model.User;
import com.demo.personalfinancetracker.repository.CategoryRepository;
import com.demo.personalfinancetracker.repository.TransactionRepository;
import com.demo.personalfinancetracker.repository.UserRepository;
import jakarta.servlet.http.HttpSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.demo.personalfinancetracker.dto.MonthlyAnalyticsDTO;
import com.demo.personalfinancetracker.dto.MonthlyBreakdownDTO;
import java.time.LocalDateTime;
import java.time.Month;
import java.time.format.TextStyle;
import java.util.*;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class TransactionService {

    @Autowired private TransactionRepository transactionRepository;
    @Autowired private CategoryRepository categoryRepository;
    @Autowired private UserRepository userRepository;

    public TransactionDTO.Response createTransaction(TransactionDTO.Request request, Long userId) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new ResourceNotFoundException("User not found"));

        if (request.getAmount() == null || request.getAmount() <= 0) {
            throw new IllegalArgumentException("Amount must be positive");
        }
        if (request.getDate().isAfter(LocalDateTime.now())) {
            throw new IllegalArgumentException("Date cannot be in the future");
        }

        Category category = categoryRepository.findById(request.getCategoryId())
                .orElseThrow(() -> new ResourceNotFoundException("Category not found"));

        if (category.getUser() != null && !category.getUser().getId().equals(userId)) {
            throw new IllegalArgumentException("Category does not belong to the user");
        }

        Transaction transaction = new Transaction();
        transaction.setAmount(request.getAmount());
        transaction.setDate(request.getDate());
        transaction.setType(request.getType());
        transaction.setDescription(request.getDescription());
        transaction.setCategory(category);
        transaction.setUser(user);

        transaction = transactionRepository.save(transaction);

        return mapToResponse(transaction);
    }

    public List<TransactionDTO.Response> getTransactions(
            Long userId,
            LocalDateTime startDate,
            LocalDateTime endDate,
            Long categoryId,
            TransactionType type
    ) {
        List<Transaction> transactions = transactionRepository.findByUserIdOrderByDateDesc(userId);

        return transactions.stream()
                .filter(t -> (startDate == null || !t.getDate().isBefore(startDate)) &&
                        (endDate == null || !t.getDate().isAfter(endDate)) &&
                        (categoryId == null || (t.getCategory() != null && t.getCategory().getId().equals(categoryId))) &&
                        (type == null || t.getType() == type))
                .map(this::mapToResponse)
                .collect(Collectors.toList());
    }

    public TransactionDTO.Response updateTransaction(Long transactionId, TransactionDTO.Request request, Long userId) {
        Transaction transaction = transactionRepository.findById(transactionId)
                .orElseThrow(() -> new ResourceNotFoundException("Transaction not found"));

        if (!transaction.getUser().getId().equals(userId)) {
            throw new IllegalArgumentException("You can only update your own transactions");
        }

        Category category = categoryRepository.findById(request.getCategoryId())
                .orElseThrow(() -> new ResourceNotFoundException("Category not found"));

        if (category.getUser() != null && !category.getUser().getId().equals(userId)) {
            throw new IllegalArgumentException("Category does not belong to the user");
        }

        transaction.setDescription(request.getDescription());
        transaction.setAmount(request.getAmount());
        transaction.setType(request.getType());
        transaction.setCategory(category);

        transaction = transactionRepository.save(transaction);
        return mapToResponse(transaction);
    }

    public void deleteTransaction(Long transactionId, Long userId) {
        Transaction transaction = transactionRepository.findById(transactionId)
                .orElseThrow(() -> new ResourceNotFoundException("Transaction not found"));

        if (!transaction.getUser().getId().equals(userId)) {
            throw new IllegalArgumentException("You can only delete your own transactions");
        }

        transactionRepository.delete(transaction);
    }

    private TransactionDTO.Response mapToResponse(Transaction transaction) {
        TransactionDTO.Response response = new TransactionDTO.Response();
        response.setId(transaction.getId());
        response.setAmount(transaction.getAmount());
        response.setDate(transaction.getDate());
        response.setType(transaction.getType());
        response.setDescription(transaction.getDescription());
        response.setCategoryName(transaction.getCategory().getName());
        response.setCategoryId(transaction.getCategory().getId());
        return response;
    }

    public MonthlyAnalyticsDTO getMonthlyAnalytics(Long userId, int year, int month) {
        LocalDateTime start = LocalDateTime.of(year, month, 1, 0, 0);
        LocalDateTime end = start.withDayOfMonth(start.toLocalDate().lengthOfMonth()).withHour(23).withMinute(59).withSecond(59);

        List<Transaction> transactions = transactionRepository.findByUserIdAndDateBetween(userId, start, end);

        Map<String, Double> incomeByCategory = new HashMap<>();
        Map<String, Double> expenseByCategory = new HashMap<>();
        double totalIncome = 0, totalExpense = 0;

        for (Transaction txn : transactions) {
            String category = txn.getCategory() != null ? txn.getCategory().getName() : "Uncategorized";
            double amt = txn.getAmount();

            if (txn.getType() == TransactionType.INCOME) {
                incomeByCategory.merge(category, amt, Double::sum);
                totalIncome += amt;
            } else {
                expenseByCategory.merge(category, amt, Double::sum);
                totalExpense += amt;
            }
        }

        MonthlyAnalyticsDTO dto = new MonthlyAnalyticsDTO();
        dto.incomeByCategory = incomeByCategory;
        dto.expenseByCategory = expenseByCategory;
        dto.totalIncome = totalIncome;
        dto.totalExpense = totalExpense;
        dto.netSavings = totalIncome - totalExpense;

        return dto;
    }

    public List<MonthlyBreakdownDTO> getYearlyAnalytics(Long userId, int year) {
        List<MonthlyBreakdownDTO> result = new ArrayList<>();

        for (int month = 1; month <= 12; month++) {
            LocalDateTime start = LocalDateTime.of(year, month, 1, 0, 0);
            LocalDateTime end = start.withDayOfMonth(start.toLocalDate().lengthOfMonth()).withHour(23).withMinute(59).withSecond(59);

            List<Transaction> transactions = transactionRepository.findByUserIdAndDateBetween(userId, start, end);

            double income = 0, expense = 0;
            for (Transaction txn : transactions) {
                if (txn.getType() == TransactionType.INCOME) income += txn.getAmount();
                else expense += txn.getAmount();
            }

            String monthName = Month.of(month).getDisplayName(TextStyle.SHORT, Locale.ENGLISH);
            result.add(new MonthlyBreakdownDTO(monthName, income, expense));
        }

        return result;
    }
}
