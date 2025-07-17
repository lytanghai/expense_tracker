package com.tanghai.expense_tracker.util;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.text.DecimalFormat;
import java.text.NumberFormat;
import java.util.Currency;
import java.util.Locale;

public class AmountUtil {

    private static final int DEFAULT_SCALE = 2;
    private static final RoundingMode DEFAULT_ROUNDING = RoundingMode.HALF_EVEN;

    // 1. Convert string to BigDecimal safely
    public static BigDecimal parse(String amount) {
        try {
            return new BigDecimal(amount).setScale(DEFAULT_SCALE, DEFAULT_ROUNDING);
        } catch (NumberFormatException e) {
            return BigDecimal.ZERO;
        }
    }

    // 2. Format amount to currency string (e.g., $1,000.00)
    public static String formatCurrency(BigDecimal amount, Locale locale) {
        NumberFormat format = NumberFormat.getCurrencyInstance(locale);
        return format.format(amount);
    }

    // 3. Round amount to given scale
    public static BigDecimal round(BigDecimal amount, int scale) {
        return amount.setScale(scale, DEFAULT_ROUNDING);
    }

    // 4. Add two amounts
    public static BigDecimal add(BigDecimal a, BigDecimal b) {
        return a.add(b).setScale(DEFAULT_SCALE, DEFAULT_ROUNDING);
    }

    // 5. Subtract two amounts
    public static BigDecimal subtract(BigDecimal a, BigDecimal b) {
        return a.subtract(b).setScale(DEFAULT_SCALE, DEFAULT_ROUNDING);
    }

    // 6. Multiply amount (e.g., for tax or percentage)
    public static BigDecimal multiply(BigDecimal a, BigDecimal multiplier) {
        return a.multiply(multiplier).setScale(DEFAULT_SCALE, DEFAULT_ROUNDING);
    }

    // 7. Divide with protection
    public static BigDecimal divide(BigDecimal a, BigDecimal b) {
        if (b.compareTo(BigDecimal.ZERO) == 0) return BigDecimal.ZERO;
        return a.divide(b, DEFAULT_SCALE, DEFAULT_ROUNDING);
    }

    // 8. Check if amount is zero
    public static boolean isZero(BigDecimal amount) {
        return amount.compareTo(BigDecimal.ZERO) == 0;
    }

    // 9. Compare two amounts (returns -1, 0, 1)
    public static int compare(BigDecimal a, BigDecimal b) {
        return a.compareTo(b);
    }

    // 10. Calculate percentage (e.g., discount)
    public static BigDecimal percentage(BigDecimal base, BigDecimal percent) {
        return multiply(base, percent.divide(BigDecimal.valueOf(100)));
    }

    // 11. Min of two amounts
    public static BigDecimal min(BigDecimal a, BigDecimal b) {
        return a.min(b);
    }

    // 12. Max of two amounts
    public static BigDecimal max(BigDecimal a, BigDecimal b) {
        return a.max(b);
    }

    // 13. Ensure non-negative amount
    public static BigDecimal ensurePositive(BigDecimal amount) {
        return amount.compareTo(BigDecimal.ZERO) < 0 ? BigDecimal.ZERO : amount;
    }

    // 14. Convert to plain string (no scientific notation)
    public static String toPlainString(BigDecimal amount) {
        return amount.setScale(DEFAULT_SCALE, DEFAULT_ROUNDING).toPlainString();
    }

    // 15. Format with currency symbol (manual)
    public static String formatWithCurrency(BigDecimal amount, String currencyCode) {
        Currency currency = Currency.getInstance(currencyCode);
        NumberFormat formatter = NumberFormat.getCurrencyInstance();
        formatter.setCurrency(currency);
        return formatter.format(amount);
    }


    public static String amtRoundDown(String amount) {
        Double doubleAmt = Double.parseDouble(amount);
        NumberFormat formatter = new DecimalFormat("#0.00");
        formatter.setRoundingMode(RoundingMode.DOWN);
        return formatter.format(doubleAmt);
    }

    public static String amtRoundUp(String amount) {
        Double doubleAmt = Double.parseDouble(amount);
        NumberFormat formatter = new DecimalFormat("#0.00");
        formatter.setRoundingMode(RoundingMode.UP);
        return formatter.format(doubleAmt);
    }

    public static String getDisplayAmountUSD(String ccy, BigDecimal amount) {
        String strAmount = amount.setScale(2, RoundingMode.DOWN).toString();
        StringBuilder stringBuilder = new StringBuilder(strAmount.substring(strAmount.indexOf(".")));
        strAmount = strAmount.substring(0, strAmount.indexOf("."));
        int length = strAmount.length();
        while (length > 3) {
            stringBuilder.insert(0, "," + strAmount.substring(length - 3, length));

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
            stringBuilder.insert(0, "," + strAmount.substring(length - 3, length));
            length -= 3;
        }
        stringBuilder.insert(0, strAmount.substring(0, length));

        return ccy + " " + stringBuilder;
    }
}
