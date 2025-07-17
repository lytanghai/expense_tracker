package com.tanghai.expense_tracker.service;

import com.tanghai.expense_tracker.entity.ExpenseTracker;
import com.tanghai.expense_tracker.util.ResponseBuilder;

import java.util.List;

public interface ScheduleService {
    ResponseBuilder<List<ExpenseTracker>> fetchMonthly();
    ResponseBuilder<List<ExpenseTracker>> fetchDaily(boolean enableCache);
    void cleanup();
}
