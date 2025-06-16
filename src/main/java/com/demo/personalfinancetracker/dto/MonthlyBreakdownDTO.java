package com.demo.personalfinancetracker.dto;

public class MonthlyBreakdownDTO {
    public String month;
    public double income;
    public double expense;
    public double savings;

    public MonthlyBreakdownDTO(String month, double income, double expense) {
        this.month = month;
        this.income = income;
        this.expense = expense;
        this.savings = income - expense;
    }
}
