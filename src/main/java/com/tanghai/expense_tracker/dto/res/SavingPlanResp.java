package com.tanghai.expense_tracker.dto.res;

import java.math.BigDecimal;
import java.util.Date;

public class SavingPlanResp {
    private Integer id;
    private String planName;
    private BigDecimal targetAmount;
    private String targetCurrency;
    private BigDecimal currentAmount;
    private String currentAmountCurrency;
    private Date deadline;
    private Date createdAt;
    private String status;

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getPlanName() {
        return planName;
    }

    public void setPlanName(String planName) {
        this.planName = planName;
    }

    public BigDecimal getTargetAmount() {
        return targetAmount;
    }

    public void setTargetAmount(BigDecimal targetAmount) {
        this.targetAmount = targetAmount;
    }

    public String getTargetCurrency() {
        return targetCurrency;
    }

    public void setTargetCurrency(String targetCurrency) {
        this.targetCurrency = targetCurrency;
    }

    public BigDecimal getCurrentAmount() {
        return currentAmount;
    }

    public void setCurrentAmount(BigDecimal currentAmount) {
        this.currentAmount = currentAmount;
    }

    public String getCurrentAmountCurrency() {
        return currentAmountCurrency;
    }

    public void setCurrentAmountCurrency(String currentAmountCurrency) {
        this.currentAmountCurrency = currentAmountCurrency;
    }

    public Date getDeadline() {
        return deadline;
    }

    public void setDeadline(Date deadline) {
        this.deadline = deadline;
    }

    public Date getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(Date createdAt) {
        this.createdAt = createdAt;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }
}
