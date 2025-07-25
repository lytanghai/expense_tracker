package com.tanghai.expense_tracker.dto.res;

import com.tanghai.expense_tracker.entity.ProfitTracker;

import java.math.BigDecimal;

public class ProfitResponse {
    private Integer id;
    private String date;
    private String category;
    private String pnlType;
    private BigDecimal pnl;
    private String currency;
    private BigDecimal convertedPrice;
    private String convertedCurrency;
    private String note;

    public ProfitResponse(ProfitTracker e) {
        this.id = e.getId();
        this.date = e.getDate();
        this.category = e.getCategory();
        this.pnlType = e.getPnlType();
        this.pnl = e.getPnl();
        this.currency = e.getCurrency();
        this.convertedPrice = e.getConvertedPrice();
        this.convertedCurrency = e.getConvertedCurrency();
        this.note = e.getNote();
    }

    public ProfitResponse(Integer id, String date, String category, String pnlType, BigDecimal pnl, String currency, BigDecimal convertedPrice, String convertedCurrency, String note) {
        this.id = id;
        this.date = date;
        this.category = category;
        this.pnlType = pnlType;
        this.pnl = pnl;
        this.currency = currency;
        this.convertedPrice = convertedPrice;
        this.convertedCurrency = convertedCurrency;
        this.note = note;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public String getCategory() {
        return category;
    }

    public void setCategory(String category) {
        this.category = category;
    }

    public String getPnlType() {
        return pnlType;
    }

    public void setPnlType(String pnlType) {
        this.pnlType = pnlType;
    }

    public BigDecimal getPnl() {
        return pnl;
    }

    public void setPnl(BigDecimal pnl) {
        this.pnl = pnl;
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
