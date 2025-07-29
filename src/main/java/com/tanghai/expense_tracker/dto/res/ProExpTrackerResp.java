package com.tanghai.expense_tracker.dto.res;

import java.math.BigDecimal;

public class ProExpTrackerResp {
    private String currencyKhr;
    private BigDecimal totalKhr;
    private String currencyUsd;
    private BigDecimal totalUsd;

    public String getCurrencyKhr() {
        return currencyKhr;
    }

    public void setCurrencyKhr(String currencyKhr) {
        this.currencyKhr = currencyKhr;
    }

    public BigDecimal getTotalKhr() {
        return totalKhr;
    }

    public void setTotalKhr(BigDecimal totalKhr) {
        this.totalKhr = totalKhr;
    }

    public String getCurrencyUsd() {
        return currencyUsd;
    }

    public void setCurrencyUsd(String currencyUsd) {
        this.currencyUsd = currencyUsd;
    }

    public BigDecimal getTotalUsd() {
        return totalUsd;
    }

    public void setTotalUsd(BigDecimal totalUsd) {
        this.totalUsd = totalUsd;
    }
}
