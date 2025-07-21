package com.tanghai.expense_tracker.constant;

import java.math.BigDecimal;

public class Static {

    // URL
    public static final String TELEGRAM_BOT_URL = "https://api.telegram.org/bot";

    //URI
    public static final String SEND_MESSAGE = "/sendMessage";

    //Static VALUE
    public static final String CHAT_ID = "chat_id";
    public static final String TEXT = "text";
    public static final String EXPENSE_TRACKER_SEQ = "expense_tracker_id_seq";

    //COMMON
    public static final String COMMA = ",";
    public static final String EMPTY = "";
    public static final String PERIOD = ".";
    public static final String USD = "USD";
    public static final String KHR = "KHR";

    public static final BigDecimal USD_TO_KHR_RATE = new BigDecimal("4000");

}
