package com.tanghai.expense_tracker.util;

import com.tanghai.expense_tracker.constant.Static;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.text.DecimalFormat;

public class AmountUtil {

    public AmountUtil() {
    }

    public static String formatAmount(String amountStr) {
        try {
            double amount = Double.parseDouble(amountStr);
            DecimalFormat formatter = new DecimalFormat("#,##0.00");
            return formatter.format(amount);
        } catch (NumberFormatException e) {
            return "Invalid number";
        }
    }
    public static String convertAmount(String amount, String currency) {
        BigDecimal value = new BigDecimal(amount);
        return currency.equals(Static.USD)
                ? AmountUtil.getDisplayAmountKHR(Static.KHR, value.multiply(Static.USD_TO_KHR_RATE))
                : AmountUtil.getDisplayAmountUSD(Static.USD, value.divide(Static.USD_TO_KHR_RATE, 2, RoundingMode.HALF_UP));
    }


    public static String getDisplayAmountUSD(String ccy, BigDecimal amount) {
        String strAmount = amount.setScale(2, RoundingMode.DOWN).toString();
        StringBuilder stringBuilder = new StringBuilder(strAmount.substring(strAmount.indexOf(Static.PERIOD)));
        strAmount = strAmount.substring(0, strAmount.indexOf(Static.PERIOD));
        int length = strAmount.length();
        while (length > 3) {
            stringBuilder.insert(0, Static.COMMA + strAmount.substring(length - 3, length));

            length -= 3;
        }
        stringBuilder.insert(0, strAmount.substring(0, length));
        return ccy + " " + stringBuilder;
    }

    public static String getDisplayAmountKHR(String ccy, BigDecimal amount) {
        String strAmount = amount.setScale(0, RoundingMode.DOWN).toString();
        StringBuilder stringBuilder = new StringBuilder();
        int length = strAmount.length();
        while (length > 3) {
            stringBuilder.insert(0, Static.COMMA + strAmount.substring(length - 3, length));
            length -= 3;
        }
        stringBuilder.insert(0, strAmount.substring(0, length));

        return ccy + " " + stringBuilder;
    }
}
