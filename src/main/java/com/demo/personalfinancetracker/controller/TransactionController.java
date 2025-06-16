package com.demo.personalfinancetracker.controller;

import com.demo.personalfinancetracker.dto.MonthlyAnalyticsDTO;
import com.demo.personalfinancetracker.dto.MonthlyBreakdownDTO;
import com.demo.personalfinancetracker.dto.TransactionDTO;
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

        TransactionDTO.Response transaction = transactionService.createTransaction(request, userId);
        return ResponseEntity.ok(transaction);
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

        List<TransactionDTO.Response> transactions = transactionService.getTransactions(userId, startDate, endDate, categoryId, type);
        return ResponseEntity.ok(transactions);
    }

    public ResponseEntity<?> deleteTransaction(@PathVariable Long id, HttpSession session) {
        Long userId = (Long) session.getAttribute("userId");
        if (userId == null) return ResponseEntity.status(401).body("Unauthorized");

        transactionService.deleteTransaction(id, userId);
        return ResponseEntity.ok("Transaction deleted successfully");
    }

    @GetMapping("/analytics/monthly")
    public ResponseEntity<MonthlyAnalyticsDTO> getMonthlyReport(
            HttpSession session,
            @RequestParam int year,
            @RequestParam int month
    ) {
        Long  userId = (Long) session.getAttribute("userId");
        MonthlyAnalyticsDTO report = transactionService.getMonthlyAnalytics(userId, year, month);
        return ResponseEntity.ok(report);
    }

    @GetMapping("/analytics/yearly")
    public ResponseEntity<List<MonthlyBreakdownDTO>> getYearlyReport(
            HttpSession session,
            @RequestParam int year
    ) {
        Long  userId = (Long) session.getAttribute("userId");
        List<MonthlyBreakdownDTO> report = transactionService.getYearlyAnalytics(userId, year);
        return ResponseEntity.ok(report);
    }

}
