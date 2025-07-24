package com.tanghai.expense_tracker.service;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;

import java.time.LocalDate;

@Component
public class CronService {

    @Value("${backend_server.web_url}")
    private String URL;

    private final Logger log = LoggerFactory.getLogger(CronService.class);
    private final ScheduleService scheduleService;
    private final RestTemplate restTemplate;

    public CronService(ScheduleService scheduleService, RestTemplate restTemplate) {
        this.scheduleService = scheduleService;
        this.restTemplate = restTemplate;
    }

    /** Trigger cron to prevent server sleep */
    @Scheduled(cron = "0 */10 8-23 * * *")
    public void run() {
        restTemplate.postForObject(URL, null, String.class);
        log.info("Run by cron!");
    }

    /** Trigger cron daily report */
    @Scheduled(cron = "0 0 22 * * *")
    public void runAt10PM() {
        scheduleService.fetchDaily();
    }

    @Scheduled(cron = "0 30 22 28-31 * *")
    public void runLastDayOfMonthAt1030PM() {
        LocalDate today = LocalDate.now();
        LocalDate lastDay = today.withDayOfMonth(today.lengthOfMonth());

        if (today.equals(lastDay)) {
            log.info("Running on last day of the month at 10:30 PM");
            scheduleService.fetchMonthly();
        }
    }

    @Scheduled(cron = "0 30 22 28-31 * *") // 10:30 PM on days 28-31 every month
    public void runQuarterlyOnLastDay() {
        LocalDate today = LocalDate.now();
        LocalDate lastDay = today.withDayOfMonth(today.lengthOfMonth());

        int month = today.getMonthValue();
        boolean isQuarterMonth = (month == 3 || month == 6 || month == 9 || month == 12);

        if (today.equals(lastDay) && isQuarterMonth) {
            log.info("Running quarterly task on last day at 10:30 PM");
            scheduleService.cleanup();
        }
    }
}
