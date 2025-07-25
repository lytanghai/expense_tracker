package com.tanghai.expense_tracker.entity;

import com.tanghai.expense_tracker.constant.Static;

import javax.persistence.*;
import java.math.BigDecimal;

@Entity
@Table(name = "profit_tracker")
public class ProfitTracker {

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = Static.PROFIT_TRACKER_SEQ)
    @SequenceGenerator(name = Static.PROFIT_TRACKER_SEQ , sequenceName = Static.PROFIT_TRACKER_SEQ, allocationSize = 1)
    private Integer id;

    @Column(name = "date")
    private String date;

    @Column(name = "category")
    private String category;

    @Column(name = "pnl")
    private BigDecimal pnl;

    @Column(name = "pnl_type")
    private String pnlType;

    @Column(name = "currency")
    private String currency;

    @Column(name = "converted_price")
    private BigDecimal convertedPrice;

    @Column(name = "converted_currency")
    private String convertedCurrency;

    @Column(name = "note")
    private String note;

    public ProfitTracker(Integer id, String pnlType, String category, BigDecimal pnl, String date, String currency) {
        this.id = id;
        this.pnlType = pnlType;
        this.category = category;
        this.pnl = pnl;
        this.date = date;
        this.currency = currency;
    }

    public ProfitTracker() {
    }

    public ProfitTracker(Integer id, String date, String category, BigDecimal pnl, String pnlType, String currency, BigDecimal convertedPrice, String convertedCurrency, String note) {
        this.id = id;
        this.date = date;
        this.category = category;
        this.pnl = pnl;
        this.pnlType = pnlType;
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

    public BigDecimal getPnl() {
        return pnl;
    }

    public void setPnl(BigDecimal pnl) {
        this.pnl = pnl;
    }

    public String getPnlType() {
        return pnlType;
    }

    public void setPnlType(String pnlType) {
        this.pnlType = pnlType;
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
