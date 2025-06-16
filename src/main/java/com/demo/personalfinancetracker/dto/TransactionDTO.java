package com.demo.personalfinancetracker.dto;

import com.demo.personalfinancetracker.model.TransactionType;
import lombok.Data;

import java.time.LocalDateTime;

public class TransactionDTO {

    @Data
    public static class Request {
        private String description;
        private Double amount;
        private LocalDateTime date;
        private TransactionType type;
        private Long categoryId;
    }

    @Data
    public static class Response {
        private Long id;
        private String description;
        private Double amount;
        private LocalDateTime date;
        private TransactionType type;
        private String categoryName;
        private Long categoryId;
    }
}
