package com.tanghai.expense_tracker.controller;

import com.tanghai.expense_tracker.service.CronService;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/public/report")
public class ReportController {

    private final CronService cronService;

    public ReportController(CronService cronService) {
        this.cronService = cronService;
    }

    @GetMapping("/trigger-daily")
    public void sendReportDaily() {
        cronService.runAt10PM();
    }

    @GetMapping("/trigger-monthly")
    public void sendReportMonthly() {
        cronService.runLastDayOfMonthAt1030PM();
    }

    @GetMapping("/trigger-cleanup")
    public void cleanupReport() {
        cronService.runQuarterlyOnLastDay();
    }

    @GetMapping("/wakeup")
    public void wakeUp() {
        cronService.run();
    }

}
