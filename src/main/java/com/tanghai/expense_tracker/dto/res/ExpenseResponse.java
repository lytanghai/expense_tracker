package com.tanghai.expense_tracker.dto.res;

import com.tanghai.expense_tracker.entity.ExpenseTracker;

import java.math.BigDecimal;

public class ExpenseResponse {
    private Integer id;
    private String expenseDate;
    private String category;
    private String item;
    private BigDecimal price;
    private String currency;
    private BigDecimal convertedPrice;
    private String convertedCurrency;
    private String note;

    public ExpenseResponse(ExpenseTracker e) {
        this.id = e.getId();
        this.expenseDate = e.getExpenseDate();
        this.category = e.getCategory();
        this.item = e.getItem();
        this.price = e.getPrice();
        this.currency = e.getCurrency();
        this.convertedPrice = e.getConvertedPrice();
        this.convertedCurrency = e.getConvertedCurrency();
        this.note = e.getNote();
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getExpenseDate() {
        return expenseDate;
    }

    public void setExpenseDate(String expenseDate) {
        this.expenseDate = expenseDate;
    }

    public String getCategory() {
        return category;
    }

    public void setCategory(String category) {
        this.category = category;
    }

    public String getItem() {
        return item;
    }

    public void setItem(String item) {
        this.item = item;
    }

    public BigDecimal getPrice() {
        return price;
    }

    public void setPrice(BigDecimal price) {
        this.price = price;
    }

    public String getCurrency() {
        return currency;
    }

    public void setCurrency(String currency) {
        this.currency = currency;
    }

    public BigDecimal getConvertedPrice() {
        return convertedPrice;
    }

    public void setConvertedPrice(BigDecimal convertedPrice) {
        this.convertedPrice = convertedPrice;
    }

    public String getConvertedCurrency() {
        return convertedCurrency;
    }

    public void setConvertedCurrency(String convertedCurrency) {
        this.convertedCurrency = convertedCurrency;
    }

    public String getNote() {
        return note;
    }

    public void setNote(String note) {
        this.note = note;
    }
}
