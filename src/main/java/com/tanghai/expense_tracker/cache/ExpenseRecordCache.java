package com.tanghai.expense_tracker.cache;

import com.tanghai.expense_tracker.entity.ExpenseTracker;
import com.tanghai.expense_tracker.util.DateUtil;

import java.time.Duration;
import java.time.Instant;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.*;

/**
 * Static internal cache to store expense records during runtime.
 */
public class ExpenseRecordCache {

    private ExpenseRecordCache() {
    }

    private static final long EXPIRY_MILLIS = Duration.ofHours(6).toMillis();

    private static class CacheEntry {
        ExpenseTracker data;
        long timestamp;

        CacheEntry(ExpenseTracker data) {
            this.data = data;
            this.timestamp = Instant.now().toEpochMilli();
        }

        boolean isExpired() {
            return Instant.now().toEpochMilli() - timestamp > EXPIRY_MILLIS;
        }
    }

    private static final Map<Integer, CacheEntry> expenseTrackerMap = new HashMap<>();

    public static void init(List<ExpenseTracker> records) {
        if (expenseTrackerMap.isEmpty()) {
            records.forEach(p -> expenseTrackerMap.put(p.getId(), new CacheEntry(p)));
        }
    }

    public static void add(ExpenseTracker expenseTracker) {
        expenseTrackerMap.put(expenseTracker.getId(), new CacheEntry(expenseTracker));}

    public static void removeById(Integer id) {
        expenseTrackerMap.remove(id);
    }

    public static void removeByDateRange(String startDateStr, String endDateStr) {
        expenseTrackerMap.entrySet().removeIf(entry -> {
            ExpenseTracker tracker = entry.getValue().data; // use .data if using CacheEntry
            LocalDateTime trackerDate = LocalDateTime.parse(tracker.getExpenseDate(), DateTimeFormatter.ofPattern(DateUtil.DATE_FORMAT_2));
            return !trackerDate.isBefore(DateUtil.getMonthRange(startDateStr)) && !trackerDate.isAfter(DateUtil.getMonthRange(endDateStr));
        });
    }

    public static ExpenseTracker getById(Integer id) {
        CacheEntry entry = expenseTrackerMap.get(id);
        if (entry == null || entry.isExpired()) {
            expenseTrackerMap.remove(id);
            return null;
        }
        return entry.data;
    }

    public static List<ExpenseTracker> getAll() {
        List<ExpenseTracker> list = new ArrayList<>();
        Iterator<Map.Entry<Integer, CacheEntry>> iterator = expenseTrackerMap.entrySet().iterator();
        while (iterator.hasNext()) {
            var entry = iterator.next();
            if (entry.getValue().isExpired()) {
                iterator.remove();
            } else {
                list.add(entry.getValue().data);
            }
        }
        return list;
    }

    public static boolean isEmpty() {
        cleanUp();
        return expenseTrackerMap.isEmpty();
    }

    public static void clear() {
        expenseTrackerMap.clear();
    }

    private static void cleanUp() {
        expenseTrackerMap.entrySet().removeIf(e -> e.getValue().isExpired());
    }
}