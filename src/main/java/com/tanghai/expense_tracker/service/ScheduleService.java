package com.tanghai.expense_tracker.service;

public interface ScheduleService {
    void fetchMonthly();
    void fetchDaily();
    void cleanup();
}
