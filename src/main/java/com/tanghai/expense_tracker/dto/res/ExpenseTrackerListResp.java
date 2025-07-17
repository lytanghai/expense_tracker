package com.tanghai.expense_tracker.dto.res;

import com.tanghai.expense_tracker.entity.ExpenseTracker;

import java.math.BigDecimal;
import java.util.List;

public class ExpenseTrackerListResp {
    private List<ExpenseTracker> result;
    private Integer totalItem;
    private BigDecimal totalAmountInUSD;
    private BigDecimal totalAmountInKHR;

    public List<ExpenseTracker> getResult() {
        return result;
    }

    public void setResult(List<ExpenseTracker> result) {
        this.result = result;
    }

    public Integer getTotalItem() {
        return totalItem;
    }

    public void setTotalItem(Integer totalItem) {
        this.totalItem = totalItem;
    }

    public BigDecimal getTotalAmountInUSD() {
        return totalAmountInUSD;
    }

    public void setTotalAmountInUSD(BigDecimal totalAmountInUSD) {
        this.totalAmountInUSD = totalAmountInUSD;
    }

    public BigDecimal getTotalAmountInKHR() {
        return totalAmountInKHR;
    }

    public void setTotalAmountInKHR(BigDecimal totalAmountInKHR) {
        this.totalAmountInKHR = totalAmountInKHR;
    }
}
