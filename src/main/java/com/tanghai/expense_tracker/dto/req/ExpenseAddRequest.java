package com.tanghai.expense_tracker.dto.req;

import com.sun.istack.NotNull;

import java.math.BigDecimal;
import java.util.Date;

public class ExpenseAddRequest {

    private Date createdAt;

    @NotNull
    private BigDecimal price;

    @NotNull
    private String currency;

    @NotNull
    private String category;

    @NotNull
    private String item;

    private String note;

    public Date getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(Date createdAt) {
        this.createdAt = createdAt;
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

    public String getNote() {
        return note;
    }

    public void setNote(String note) {
        this.note = note;
    }
}
