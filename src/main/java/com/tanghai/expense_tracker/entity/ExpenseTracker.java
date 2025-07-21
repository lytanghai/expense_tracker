package com.tanghai.expense_tracker.entity;

import com.tanghai.expense_tracker.constant.Static;

import javax.persistence.*;
import java.math.BigDecimal;

@Entity
@Table(name = "expense_tracker")
public class ExpenseTracker {

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = Static.EXPENSE_TRACKER_SEQ)
    @SequenceGenerator(name = Static.EXPENSE_TRACKER_SEQ , sequenceName = Static.EXPENSE_TRACKER_SEQ, allocationSize = 1)
    private Integer id;

    @Column(name = "expense_date")
    private String expenseDate;

    @Column(name = "category")
    private String category;

    @Column(name = "item")
    private String item;

    @Column(name = "price")
    private BigDecimal price;

    @Column(name = "currency")
    private String currency;

    @Column(name = "converted_price")
    private BigDecimal convertedPrice;

    @Column(name = "converted_currency")
    private String convertedCurrency;

    @Column(name = "note")
    private String note;

    public ExpenseTracker() {
    }

    public ExpenseTracker(Integer id, String expenseDate, String category, String item, BigDecimal price, String currency, BigDecimal convertedPrice, String convertedCurrency, String note) {
        this.id = id;
        this.expenseDate = expenseDate;
        this.category = category;
        this.item = item;
        this.price = price;
        this.currency = currency;
        this.convertedPrice = convertedPrice;
        this.convertedCurrency = convertedCurrency;
        this.note = note;
    }

    public ExpenseTracker(Integer id, String item, String category, BigDecimal price, String expenseDate, String currency) {
        this.id = id;
        this.expenseDate = expenseDate;
        this.category = category;
        this.item = item;
        this.price = price;
        this.currency = currency;
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
