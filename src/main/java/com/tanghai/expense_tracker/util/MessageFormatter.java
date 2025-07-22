package com.tanghai.expense_tracker.util;

import java.math.BigDecimal;

public class MessageFormatter {

    public static String dailySummaryReport(String date,
                                            String time,
                                            int numTransactions,
                                            int usdTransaction,
                                            int khrTransaction,
                                            BigDecimal totalUsdTransaction,
                                            BigDecimal totalKhrTransaction,
                                            BigDecimal totalUsd,
                                            BigDecimal totalKhr) {
        return String.format(
                "*--- Expense Tracker - Daily ---*\n" +
                        "*Date:* %s\n" +
                        "*Sent at:* %s\n" +
                        "------------------------\n" +
                        "*Number of Transactions:* %d\n" +
                        "• USD Transactions: %d\n" +
                        "• KHR Transactions: %d\n" +
                        "*Total Transactions:*\n" +
                        "• USD: %.0f USD\n" +
                        "• KHR: %.0f KHR\n" +
                        "------------------------\n" +
                        "*Total Amount (USD Only):* `%.1f USD`\n" +
                        "*Total Amount (KHR Only):* `%.0f KHR`\n" +
                        "------------------------\n\n" +
                        "💸 *You have spent:* `%s USD` or `%s KHR` today!",
                date,
                time,
                numTransactions,
                usdTransaction,
                khrTransaction,
                totalUsdTransaction.doubleValue(),
                totalKhrTransaction.doubleValue(),
                totalUsd.doubleValue(),
                totalKhr.doubleValue(),
                AmountUtil.formatAmount(String.valueOf(totalUsd)),
                AmountUtil.formatAmount(String.valueOf(totalKhr))
        );
    }

    public static String dailyMonthlyReport(
            String date,
            String time,
            int numTransactions,
            int usdTransaction,
            int khrTransaction,
            BigDecimal totalUsdTransaction,
            BigDecimal totalKhrTransaction,
            BigDecimal totalUsd,
            BigDecimal totalKhr
    ) {
        return String.format(
                "*---------- Expense Tracker - Monthly ----------*\n" +
                    "*Date:* %s\n" +
                    "*Sent at:* %s\n" +
                    "-------------------------------\n" +
                    "*Number of Transactions:* %d\n" +
                    "• Transaction in USD: %d USD\n" +
                    "• Transaction in KHR: %,d KHR\n" +
                    "*Total Transaction:*\n" +
                    "• USD: %,.0f USD\n" +
                    "• KHR: %,d KHR\n" +
                    "----------------------------\n" +
                    "*Total in USD:* `%,.1f USD`\n" +
                    "*Total in KHR:* `%,d KHR`\n" +
                    "----------------------------\n" +
                    "💸 *You have spent:* `%,.1f USD` or `%,d KHR` in this month!\n" +
                "*-------------- Expense Tracker - Monthly --------------*",
                date,
                time,
                numTransactions,
                usdTransaction,
                khrTransaction,
                totalUsdTransaction.doubleValue(),
                totalKhrTransaction.intValue(),
                totalUsd.doubleValue(),
                totalKhr.intValue(),
                totalUsd.doubleValue(),
                totalKhr.intValue()
        );
    }

    public static String buildCleanupAlertMessage(String date, String time) {
        return String.format(
                "*Date:* %s\n" +
                "*Sent at:* %s\n\n" +
                "⚠️ *Alert:* Auto cleanup of records before the last 3 months.\n" +
                "✅    *Status:* Completed!!!",
                date,
                time
        );
    }

}
