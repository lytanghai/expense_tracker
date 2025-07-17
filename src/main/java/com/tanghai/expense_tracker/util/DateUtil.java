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

    public static String[] getMonthDateRange(String anyDateInMonth) {
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern(DATE_WITH_TIME_1);

        LocalDateTime date = LocalDateTime.parse(anyDateInMonth, formatter);

        LocalDateTime startOfMonth = date.withDayOfMonth(1).withHour(0).withMinute(0).withSecond(0);
        LocalDateTime endOfMonth = date.with(TemporalAdjusters.lastDayOfMonth()).withHour(23).withMinute(59).withSecond(59);

        return new String[]{
                startOfMonth.format(formatter),
                endOfMonth.format(formatter)
        };
    }

    public static String[] getDayDateRange(Date date) {
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern(DATE_WITH_TIME_1);

        // Convert java.util.Date to LocalDate
        Instant instant = date.toInstant();
        ZoneId zoneId = ZoneId.of("Asia/Phnom_Penh"); // Cambodia timezone
        LocalDate localDate = instant.atZone(zoneId).toLocalDate();

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
        DateTimeFormatter inputFormat = DateTimeFormatter.ofPattern(DATE_WITH_TIME_1);
        DateTimeFormatter outputFormat = DateTimeFormatter.ofPattern("yyyy-MM");

        LocalDateTime dateTime = LocalDateTime.parse(dateStr, inputFormat);
        return outputFormat.format(dateTime);
    }





}