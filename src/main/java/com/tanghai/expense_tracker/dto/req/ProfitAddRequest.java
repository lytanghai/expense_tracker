package com.tanghai.expense_tracker.dto.req;

import com.sun.istack.NotNull;

import java.math.BigDecimal;
import java.util.Date;

public class ProfitAddRequest {
    @NotNull
    private Date date;

    @NotNull
    private BigDecimal pnl;

    @NotNull
    private String currency;

    @NotNull
    private String category;

    @NotNull
    private String pnlType;

    @NotNull
    private String note;

    public Date getDate() {
        return date;
    }

    public void setDate(Date date) {
        this.date = date;
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

    public String getNote() {
        return note;
    }

    public void setNote(String note) {
        this.note = note;
    }
}


