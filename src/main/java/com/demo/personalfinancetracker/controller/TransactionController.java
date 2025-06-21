package com.demo.personalfinancetracker.controller;

import com.demo.personalfinancetracker.dto.MonthlyAnalyticsDTO;
import com.demo.personalfinancetracker.dto.MonthlyBreakdownDTO;
import com.demo.personalfinancetracker.dto.TransactionDTO;
import com.demo.personalfinancetracker.exception.ResourceNotFoundException;
import com.demo.personalfinancetracker.model.Transaction;
import com.demo.personalfinancetracker.model.TransactionType;
import com.demo.personalfinancetracker.service.TransactionService;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/transactions")
@CrossOrigin(
        allowCredentials = "true",
        origins = {
                "http://localhost:5173",
                "https://personal-transaction-management-sys.vercel.app"
        }
)
public class TransactionController {

    @Autowired
    private TransactionService transactionService;

    @PostMapping
    public ResponseEntity<?> createTransaction(@RequestBody TransactionDTO.Request request, HttpSession session) {
        Long userId = (Long) session.getAttribute("userId");
        if (userId == null) return ResponseEntity.status(401).body("Unauthorized");

        try {
            TransactionDTO.Response transaction = transactionService.createTransaction(request, userId);
            return ResponseEntity.ok(transaction);
        } catch (ResourceNotFoundException e) {
            return ResponseEntity.status(404).body(e.getMessage());
        } catch (IllegalArgumentException e) {
            return ResponseEntity.status(400).body(e.getMessage());
        } catch (Exception e) {
            return ResponseEntity.status(500).body("Failed to create transaction");
        }
    }

    @PutMapping("/{id}")
    public ResponseEntity<?> updateTransaction(
            @PathVariable Long id,
            @RequestBody TransactionDTO.Request request,
            HttpSession session) {

        Long userId = (Long) session.getAttribute("userId");
        if (userId == null) {
            return ResponseEntity.status(401).body("Unauthorized");
        }

        try {
            TransactionDTO.Response updatedTransaction = transactionService.updateTransaction(id, request, userId);
            return ResponseEntity.ok(updatedTransaction);
        } catch (ResourceNotFoundException e) {
            return ResponseEntity.status(404).body(e.getMessage());
        } catch (IllegalArgumentException e) {
            return ResponseEntity.status(403).body(e.getMessage());
        } catch (Exception e) {
            return ResponseEntity.status(500).body("Failed to update transaction");
        }
    }

    @GetMapping
    public ResponseEntity<?> getAllTransactions(
            HttpSession session,
            @RequestParam(required = false) LocalDateTime startDate,
            @RequestParam(required = false) LocalDateTime endDate,
            @RequestParam(required = false) Long categoryId,
            @RequestParam(required = false) TransactionType type
    ) {
        Long userId = (Long) session.getAttribute("userId");
        if (userId == null) return ResponseEntity.status(401).body("Unauthorized");

        try {
            List<TransactionDTO.Response> transactions = transactionService.getTransactions(userId, startDate, endDate, categoryId, type);
            return ResponseEntity.ok(transactions);
        } catch (IllegalArgumentException e) {
            return ResponseEntity.status(400).body("Invalid filter parameters: " + e.getMessage());
        } catch (Exception e) {
            return ResponseEntity.status(500).body("Failed to retrieve transactions");
        }
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<?> deleteTransaction(@PathVariable Long id, HttpSession session) {
        Long userId = (Long) session.getAttribute("userId");
        if (userId == null) return ResponseEntity.status(401).body("Unauthorized");

        try {
            transactionService.deleteTransaction(id, userId);
            return ResponseEntity.ok("Transaction deleted successfully");
        } catch (ResourceNotFoundException e) {
            return ResponseEntity.status(404).body(e.getMessage());
        } catch (IllegalArgumentException e) {
            return ResponseEntity.status(403).body(e.getMessage());
        } catch (Exception e) {
            return ResponseEntity.status(500).body("Failed to delete transaction");
        }
    }

    @GetMapping("/analytics/monthly")
    public ResponseEntity<?> getMonthlyReport(
            HttpSession session,
            @RequestParam int year,
            @RequestParam int month
    ) {
        Long userId = (Long) session.getAttribute("userId");
        if (userId == null) return ResponseEntity.status(401).body("Unauthorized");

        try {
            // Validate month and year parameters
            if (month < 1 || month > 12) {
                return ResponseEntity.status(400).body("Invalid month. Month must be between 1 and 12");
            }
            if (year < 1900 || year > LocalDateTime.now().getYear() + 1) {
                return ResponseEntity.status(400).body("Invalid year");
            }

            MonthlyAnalyticsDTO report = transactionService.getMonthlyAnalytics(userId, year, month);
            return ResponseEntity.ok(report);
        } catch (Exception e) {
            return ResponseEntity.status(500).body("Failed to generate monthly analytics");
        }
    }

    @GetMapping("/analytics/yearly")
    public ResponseEntity<?> getYearlyReport(
            HttpSession session,
            @RequestParam int year
    ) {
        Long userId = (Long) session.getAttribute("userId");
        if (userId == null) return ResponseEntity.status(401).body("Unauthorized");

        try {
            // Validate year parameter
            if (year < 1900 || year > LocalDateTime.now().getYear() + 1) {
                return ResponseEntity.status(400).body("Invalid year");
            }

            List<MonthlyBreakdownDTO> report = transactionService.getYearlyAnalytics(userId, year);
            return ResponseEntity.ok(report);
        } catch (Exception e) {
            return ResponseEntity.status(500).body("Failed to generate yearly analytics");
        }
    }
}