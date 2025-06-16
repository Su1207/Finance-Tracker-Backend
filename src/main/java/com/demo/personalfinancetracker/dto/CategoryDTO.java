package com.demo.personalfinancetracker.dto;

import com.demo.personalfinancetracker.model.TransactionType;
import lombok.AllArgsConstructor;
import lombok.Data;

public class CategoryDTO {
    @Data
    @AllArgsConstructor
    public static class Response {
        private Long id;
        private String name;
        private TransactionType type;
        private boolean isDefault;
    }
}
