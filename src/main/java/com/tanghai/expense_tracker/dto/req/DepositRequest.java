package com.tanghai.expense_tracker.dto.req;

import java.math.BigDecimal;

public class DepositRequest {
    private Integer planId;
    private BigDecimal amount;
    private String currency;

    public Integer getPlanId() {
        return planId;
    }

    public void setPlanId(Integer savingPlanId) {
        this.planId = savingPlanId;
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
}
