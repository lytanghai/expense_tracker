package com.tanghai.expense_tracker.util;

import java.math.BigDecimal;

public class ExchangeRateUtil {
    public static String convertAmount(String amount, String currency) {
        return currency.equals("USD")
                ? AmountUtil.getDisplayAmountKHR("KHR", AmountUtil.multiply(BigDecimal.valueOf(Long.parseLong(amount)),BigDecimal.valueOf(4000.0)))
                : AmountUtil.getDisplayAmountUSD("USD", AmountUtil.divide(BigDecimal.valueOf(Long.parseLong(amount)),BigDecimal.valueOf(4000.0)));
    }
}
