package com.tanghai.expense_tracker.cache;

import com.tanghai.expense_tracker.entity.ExpenseTracker;

import java.util.*;

/**
 * Static internal cache to store expense records during runtime.
 */
public class ExpenseRecordCache {

    private static Map<Integer, ExpenseTracker> expenseTrackerHashMap = new HashMap<>();

    public static void init(List<ExpenseTracker> records) {
        if (expenseTrackerHashMap.isEmpty()) {
            expenseTrackerHashMap = new HashMap<>();
            records.forEach(p -> expenseTrackerHashMap.put(p.getId(), p));
        }
    }

    public static void add(ExpenseTracker expenseTracker) {
        if (!expenseTrackerHashMap.containsKey(expenseTracker.getId())) {
            expenseTrackerHashMap.put(expenseTracker.getId(), expenseTracker);
        }
    }

    public static ExpenseTracker getById(Integer id) {
        return expenseTrackerHashMap.get(id);
    }

    public static List<ExpenseTracker> getAll() {
        return new ArrayList<>(expenseTrackerHashMap.values());
    }

    public static boolean isEmpty() {
        return expenseTrackerHashMap.isEmpty();
    }

    public static void clear() {
        expenseTrackerHashMap.clear();
    }
}
