package com.tanghai.expense_tracker.util;

import com.tanghai.expense_tracker.constant.Static;

import java.math.BigDecimal;
import java.math.RoundingMode;

public class ExchangeRateUtil {

    private ExchangeRateUtil() {
    }

    public static String convertAmount(String amount, String currency) {
        BigDecimal value = new BigDecimal(amount);
        return currency.equals(Static.USD)
                ? AmountUtil.getDisplayAmountKHR(Static.KHR, value.multiply(Static.USD_TO_KHR_RATE))
                : AmountUtil.getDisplayAmountUSD(Static.USD, value.divide(Static.USD_TO_KHR_RATE, 2, RoundingMode.HALF_UP));
    }
}
