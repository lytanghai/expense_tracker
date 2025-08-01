package com.tanghai.expense_tracker.dto.res;

import java.math.BigDecimal;
import java.util.Date;

public class DepositHistory {
    private Integer id;
    private BigDecimal amount;
    private String currency;
    private Date depositedDate;

    public DepositHistory(Integer id, BigDecimal amount, String currency, Date depositedDate) {
        this.id = id;
        this.amount = amount;
        this.currency = currency;
        this.depositedDate = depositedDate;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public BigDecimal getAmount() {
        return amount;
    }

    public void setAmount(BigDecimal amount) {
        this.amount = amount;
    }

    public String getCurrency() {
        return currency;
    }

    public void setCurrency(String currency) {
        this.currency = currency;
    }

    public Date getDepositedDate() {
        return depositedDate;
    }

    public void setDepositedDate(Date depositedDate) {
        this.depositedDate = depositedDate;
    }
}
