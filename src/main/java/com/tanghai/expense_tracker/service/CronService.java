package com.tanghai.expense_tracker.service;

import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

@Component
public class CronService {

    @Scheduled(cron = "0 */10 8-23 * * *")
    public void run() {
        System.out.println("Run by cron!");
    }
}
