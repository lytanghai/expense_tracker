package com.tanghai.expense_tracker.dto.res;

import java.math.BigDecimal;

public interface CurrencyTotalProjection {
    String getCurrency();
    BigDecimal getTotal();
}