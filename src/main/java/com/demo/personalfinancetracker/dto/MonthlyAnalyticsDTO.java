package com.demo.personalfinancetracker.dto;

import java.util.Map;

public class MonthlyAnalyticsDTO {
    public Map<String, Double> incomeByCategory;
    public Map<String, Double> expenseByCategory;
    public double totalIncome;
    public double totalExpense;
    public double netSavings;

    public MonthlyAnalyticsDTO() {}
}
