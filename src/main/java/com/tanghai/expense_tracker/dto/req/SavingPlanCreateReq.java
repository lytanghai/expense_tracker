package com.tanghai.expense_tracker.dto.req;

import java.math.BigDecimal;

public class SavingPlanCreateReq {

    private String planName;
    private BigDecimal amount;
    private String amountCurrency;
    private BigDecimal initialAmount;
    private String initialAmountCurrency;
    private String targetDate;

    public String getPlanName() {
        return planName;
    }

    public void setPlanName(String planName) {
        this.planName = planName;
    }

    public BigDecimal getAmount() {
        return amount;
    }

    public void setAmount(BigDecimal amount) {
        this.amount = amount;
    }

    public String getAmountCurrency() {
        return amountCurrency;
    }

    public void setAmountCurrency(String amountCurrency) {
        this.amountCurrency = amountCurrency;
    }

    public BigDecimal getInitialAmount() {
        return initialAmount;
    }

    public void setInitialAmount(BigDecimal initialAmount) {
        this.initialAmount = initialAmount;
    }

    public String getInitialAmountCurrency() {
        return initialAmountCurrency;
    }

    public void setInitialAmountCurrency(String initialAmountCurrency) {
        this.initialAmountCurrency = initialAmountCurrency;
    }

    public String getTargetDate() {
        return targetDate;
    }

    public void setTargetDate(String targetDate) {
        this.targetDate = targetDate;
    }
}
