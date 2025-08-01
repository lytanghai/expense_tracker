package com.tanghai.expense_tracker.util;

import org.apache.commons.lang3.time.DateUtils;

import java.sql.Timestamp;
import java.text.DateFormat;
import java.text.FieldPosition;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.*;
import java.time.format.DateTimeFormatter;
import java.time.temporal.ChronoUnit;
import java.time.temporal.TemporalAdjusters;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;

/**
 * <dependency>
 *   <groupId>org.apache.commons</groupId>
 *   <artifactId>commons-lang3</artifactId>
 *   <version>3.8.1</version>
 * </dependency>
 * */

public final class DateUtil extends DateUtils {

    public static final String DATE_WITH_TIME_1 = "dd-MM-yyyy HH:mm:ss";
    public static final String DATE_FORMAT_2 = "yyyy-MM-dd'T'HH:mm:ss";
    public static final String DATE_FORMAT_3 = "yyyy-MM-dd";

    // Accepts "HH:mm:ss" or "H:mm:ss" (e.g. 7:05:09)
    private static final DateTimeFormatter FORMAT = DateTimeFormatter.ofPattern("H:mm:ss");

    /** Returns "AM" if 00:00:00–11:59:59, otherwise "PM". */
    public static String getAmPm(String timeStr) {
        LocalTime t = LocalTime.parse(timeStr, FORMAT);   // throws DateTimeParseException if bad format
        return t.getHour() < 12 ? "AM" : "PM";
    }

    public static LocalDateTime getMonthRange(String date) {
        return LocalDateTime.parse(date, DateTimeFormatter.ofPattern(DATE_FORMAT_2));
    }


    public static String[] getDayDateRange(Date date) {
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern(DATE_WITH_TIME_1);

        // Convert java.util.Date to LocalDate
        Instant instant = date.toInstant();
        LocalDate localDate = instant
                .atZone(ZoneId.of("Asia/Phnom_Penh"))
                .toLocalDate();

        // Start and end of the day
        LocalDateTime startOfDay = localDate.atStartOfDay();
        LocalDateTime endOfDay = localDate.atTime(23, 59, 59);

        return new String[]{
                startOfDay.format(formatter),
                endOfDay.format(formatter)
        };
    }

    public static String[] getDayDateRange(String dateOnly) {
        DateTimeFormatter inputFormatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
        DateTimeFormatter outputFormatter = DateTimeFormatter.ofPattern(DATE_WITH_TIME_1);

        LocalDate parsedDate = LocalDate.parse(dateOnly, inputFormatter);

        String start = parsedDate.atStartOfDay().format(outputFormatter); // 00:00:00
        String end = parsedDate.atTime(23, 59, 59).format(outputFormatter); // 23:59:59

        return new String[]{start, end};
    }

    public static String format(Date date) {
        return format(date, DATE_WITH_TIME_1);
    }


    public static String format(Date date, String format) {
        return format(date, format, null);
    }

    public static String format(Date date, String format, String defaultValue) {
        return date == null ? defaultValue : new SimpleDateFormat(format).format(date);
    }

    public static String convertToYearMonth(String dateStr) {
        return  DateTimeFormatter.ofPattern("yyyy-MM")
                .format(LocalDateTime.parse(dateStr, DateTimeFormatter.ofPattern(DATE_WITH_TIME_1)));
    }

    public static Date convertToDateWithMidnight(String dateStr, Boolean includeHour) {
        DateTimeFormatter formatter;

        if(includeHour) {
             formatter = DateTimeFormatter.ofPattern(DATE_WITH_TIME_1);
        } else {
             formatter = DateTimeFormatter.ofPattern(DATE_FORMAT_3);
        }

        // Parse to LocalDate
        LocalDate localDate = LocalDate.parse(dateStr, formatter);

        // Convert to java.util.Date at 00:00:00 in Asia/Phnom_Penh
        return Date.from(localDate.atStartOfDay(ZoneId.of("Asia/Phnom_Penh")).toInstant());
    }

}