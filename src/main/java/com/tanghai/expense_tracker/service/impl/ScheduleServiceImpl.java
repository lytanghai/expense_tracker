package com.tanghai.expense_tracker.service.impl;

import com.tanghai.expense_tracker.constant.Static;
import com.tanghai.expense_tracker.entity.ExpenseTracker;
import com.tanghai.expense_tracker.repository.ExpenseTrackerRepo;
import com.tanghai.expense_tracker.service.ScheduleService;
import com.tanghai.expense_tracker.service.TelegramService;
import com.tanghai.expense_tracker.util.DateUtil;
import com.tanghai.expense_tracker.util.MessageFormatter;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;

/**
 * 1. Alert to telegram every day at 10pm (daily)
 * 2  Alert to telegram monthly at the end of the month
 * 3. Alert and Auto clean up after before 3 months
 * */

@Service
public class ScheduleServiceImpl implements ScheduleService {

    private final ExpenseTrackerRepo expenseTrackerRepo;
    private final TelegramService telegramService;

    public ScheduleServiceImpl(ExpenseTrackerRepo expenseTrackerRepo, TelegramService telegramService) {
        this.expenseTrackerRepo = expenseTrackerRepo;
        this.telegramService = telegramService;
    }

    @Override
    @Scheduled(cron = "0 0 22 * * *") // ✅ 10:00 PM every day
    public void fetchDaily() {
        String[] range = DateUtil.getDayDateRange(new Date());
        List<ExpenseTracker> resultSet = expenseTrackerRepo.findAllByDateRange(range[0], range[1]);

        String current = DateUtil.format(new Date());

        List<ExpenseTracker> usdTransaction = resultSet.stream()
                .filter(i -> i.getCurrency().equals(Static.USD))
                .collect(Collectors.toList());

        List<ExpenseTracker> khrTransaction = resultSet.stream()
                .filter(i -> i.getCurrency().equals(Static.KHR))
                .collect(Collectors.toList());

        // Sum USD and KHR separately (safe even if list is empty)
        BigDecimal totalUsdAmountInTransaction = usdTransaction.stream()
                .map(ExpenseTracker::getPrice)
                .reduce(BigDecimal.ZERO, BigDecimal::add);

        BigDecimal totalKhrAmountInTransaction = khrTransaction.stream()
                .map(ExpenseTracker::getPrice)
                .reduce(BigDecimal.ZERO, BigDecimal::add);

        //call to telegram
        telegramService.sendMessage(MessageFormatter.dailySummaryReport(
                current.substring(0, 10),
                current.substring(11, 19).concat(" ").concat(DateUtil.getAmPm(current.substring(11, 19))),
                resultSet.size(),
                usdTransaction.size(),
                khrTransaction.size(),
                totalUsdAmountInTransaction,
                totalKhrAmountInTransaction,
                totalUsdAmountInTransaction.add(totalKhrAmountInTransaction.divide(Static.USD_TO_KHR_RATE, 2, RoundingMode.HALF_UP)),
                totalKhrAmountInTransaction.add(totalUsdAmountInTransaction.multiply(Static.USD_TO_KHR_RATE))
        ));
    }
    @Override
    @Scheduled(cron = "0 0 22 L * *") // 2. At 22:00 (10 PM) on the last day of every month
    public void fetchMonthly() {
        String[] range = DateUtil.getDayDateRange(new Date());
        List<ExpenseTracker> resultSet = expenseTrackerRepo.findAllByDateRange(range[0], range[1]);

        String current = DateUtil.format(new Date());

        List<ExpenseTracker> usdTransaction = resultSet.stream()
                .filter(i -> i.getCurrency().equals(Static.USD))
                .collect(Collectors.toList());

        List<ExpenseTracker> khrTransaction = resultSet.stream()
                .filter(i -> i.getCurrency().equals(Static.KHR))
                .collect(Collectors.toList());

        // Sum USD and KHR separately (safe even if list is empty)
        BigDecimal totalUsdAmountInTransaction = usdTransaction.stream()
                .map(ExpenseTracker::getPrice)
                .reduce(BigDecimal.ZERO, BigDecimal::add);

        BigDecimal totalKhrAmountInTransaction = khrTransaction.stream()
                .map(ExpenseTracker::getPrice)
                .reduce(BigDecimal.ZERO, BigDecimal::add);

        //call to telegram
        telegramService.sendMessage(MessageFormatter.dailyMonthlyReport(
                current.substring(0, 10),
                current.substring(11, 19).concat(" ").concat(DateUtil.getAmPm(current.substring(11, 19))),
                resultSet.size(),
                usdTransaction.size(),
                khrTransaction.size(),
                totalUsdAmountInTransaction,
                totalKhrAmountInTransaction,
                totalUsdAmountInTransaction.add(totalKhrAmountInTransaction.divide(Static.USD_TO_KHR_RATE, 2, RoundingMode.HALF_UP)),
                totalKhrAmountInTransaction.add(totalUsdAmountInTransaction.multiply(Static.USD_TO_KHR_RATE))
        ));

    }

    @Override
    @Scheduled(cron = "0 0 0 1 * *") // 3. Every 1st of the month at 12:00 AM
    @Transactional
    public void cleanup() {
        expenseTrackerRepo.deleteRecordsOlderThanThreeMonths();
        // Format date & time with AM/PM
        String current = DateUtil.format(new Date());

        //call to telegram
        telegramService.sendMessage(MessageFormatter.buildCleanupAlertMessage(
                current.substring(0, 10),
                current.substring(11, 19).concat(" ").concat(DateUtil.getAmPm(current.substring(11, 19)))));
    }

    @Override
    @Scheduled(fixedRate = 870000)
    public void wakeUp() {
        System.out.println("Wake up called");
    }
}
