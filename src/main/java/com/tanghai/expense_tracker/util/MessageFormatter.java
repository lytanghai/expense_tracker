package com.tanghai.expense_tracker.util;

import java.util.Date;

public class MessageFormatter {

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
