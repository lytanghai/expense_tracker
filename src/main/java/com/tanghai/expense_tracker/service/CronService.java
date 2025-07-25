package com.tanghai.expense_tracker.service;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.time.LocalDate;

@Service
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
    public void run() {
        restTemplate.postForObject(URL, null, String.class);
        log.info("Run by cron!");
    }

    /** Trigger cron daily report */
    public void runAt10PM() {
        scheduleService.fetchDaily();
    }

    public void runLastDayOfMonthAt1030PM() {
        LocalDate today = LocalDate.now();
        LocalDate lastDay = today.withDayOfMonth(today.lengthOfMonth());

        if (today.equals(lastDay)) {
            log.info("Running on last day of the month at 10:30 PM");
            scheduleService.fetchMonthly();
        }
    }

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
