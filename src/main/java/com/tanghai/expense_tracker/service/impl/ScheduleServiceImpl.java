package com.tanghai.expense_tracker.service.impl;

import com.tanghai.expense_tracker.cache.ExpenseRecordCache;
import com.tanghai.expense_tracker.entity.ExpenseTracker;
import com.tanghai.expense_tracker.repository.ExpenseTrackerRepo;
import com.tanghai.expense_tracker.service.ScheduleService;
import com.tanghai.expense_tracker.util.DateUtil;
import com.tanghai.expense_tracker.util.ResponseBuilder;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Date;
import java.util.List;

/**
 * 1. Alert to telegram every day at 10pm (daily)
 * 2  Alert to telegram monthly at the end of the month
 * 3. Alert and Auto clean up after before 3 months
 * */

@Service
public class ScheduleServiceImpl implements ScheduleService {

    private final ExpenseTrackerRepo expenseTrackerRepo;

    public ScheduleServiceImpl(ExpenseTrackerRepo expenseTrackerRepo) {
        this.expenseTrackerRepo = expenseTrackerRepo;
    }

    @Override
    @Scheduled(cron = "0 0 22 * * *") // ✅ 10:00 PM every day
    public ResponseBuilder<List<ExpenseTracker>> fetchDaily(boolean enableCache) {
        // If cache is enabled and already initialized, return from cache
        if (enableCache && !ExpenseRecordCache.isEmpty()) {
            return new ResponseBuilder<List<ExpenseTracker>>().success(ExpenseRecordCache.getAll());
        }

        // Else, fetch from DB and populate cache if needed
        String[] range = DateUtil.getDayDateRange(new Date());
        List<ExpenseTracker> result = expenseTrackerRepo.findAllByDateRange(range[0], range[1]);
        if (enableCache && !result.isEmpty()) {
            ExpenseRecordCache.init(result);
        }

        return new ResponseBuilder<List<ExpenseTracker>>().success(result);
    }

    @Override
    @Scheduled(cron = "0 0 22 L * *") // 2. At 22:00 (10 PM) on the last day of every month
    public ResponseBuilder<List<ExpenseTracker>> fetchMonthly() {
        String currentDate = DateUtil.format(new Date());
        String[] range = DateUtil.getMonthDateRange(currentDate);
        List<ExpenseTracker> result = expenseTrackerRepo.findAllByDateRange(range[0], range[1]);
        if(!result.isEmpty()) {
            return new ResponseBuilder<List<ExpenseTracker>>().success(result);
        }
        return null;
    }

    @Override
    @Scheduled(cron = "0 0 0 1 * *") // 3. Every 1st of the month at 12:00 AM
    @Transactional
    public void cleanup() {
        expenseTrackerRepo.deleteRecordsOlderThanThreeMonths();
    }
}
