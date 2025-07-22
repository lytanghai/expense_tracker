package com.tanghai.expense_tracker.controller;

import com.tanghai.expense_tracker.service.ScheduleService;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/report")
public class ReportController {

    private final ScheduleService scheduleService;

    public ReportController(ScheduleService scheduleService) {
        this.scheduleService = scheduleService;
    }

    @GetMapping("/trigger-daily")
    public void sendReportDaily() {
        scheduleService.fetchDaily();
    }

    @GetMapping("/trigger-monthly")
    public void sendReportMonthly() {
        scheduleService.fetchMonthly();
    }

    @GetMapping("/trigger-cleanup")
    public void cleanupReport() {
        scheduleService.cleanup();
    }

}
