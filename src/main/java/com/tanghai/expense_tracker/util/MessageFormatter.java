package com.tanghai.expense_tracker.util;

import java.math.BigDecimal;
import java.util.Date;

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
                "------------------- Expense Tracker - Daily -------------------\n" +
                        "Date: %s\n" +
                        "Sent at: %s\n" +
                        "--------------------------------------------\n" +
                        "Number of Transactions: %d\n" +
                        "Number of Transactions in USD: %d\n" +
                        "Number of Transactions in KHR: %d\n" +
                        "\n" +
                        "(Total) Transactions in USD: %.0f USD\n" +
                        "(Total) Transactions in KHR: %.0f KHR\n" +
                        "---------------------------------------------\n" +
                        "Total Amount only USD: | %.1f USD |\n" +
                        "Total Amount only KHR: | %.0f KHR |\n" +
                        "----------------------------\n" +
                        "You have spent %s USD or %s KHR today!\n" +
                        "------------------- Expense Tracker - Daily -------------------",
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
                "------------------- Expense Tracker - Monthly -------------------\n" +
                "Date: %s\n" +
                "Sent at: %s\n" +
                "-------------------------------\n" +
                "Number of Transactions: %d\n" +
                "Transaction in USD: %d USD\n" +
                "Transaction in KHR: %,d KHR\n" +
                "\n" +
                "Total Transaction in USD: %,.0f USD\n" +
                "Total Transaction in KHR: %,d KHR\n" +
                "----------------------------\n" +
                "Total in USD: | %,.1f USD |\n" +
                "Total in KHR: | %,d KHR |\n" +
                "----------------------------\n" +
                "You have spent %,.1f USD or %,d KHR in this month!\n" +
                "------------------- Expense Tracker - Monthly -------------------",
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
                "Date: %s\n" +
                "Sent at: %s\n" +
                "Alert: Auto clean up the record before the last 3 months\n" +
                "Status: Completed!!!",
                date,
                time
        );
    }
    public static String formatAlertMessage(int step, String alertText, Date sendTime) {
        return String.format(
            "-----------Message---------------\n" +
            "Step: %d\n" +
            "%s\n" +
            "Send at: %s\n" +
            "_________________________",
            step,
            alertText,
            new java.text.SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(sendTime)
        );
    }

}
