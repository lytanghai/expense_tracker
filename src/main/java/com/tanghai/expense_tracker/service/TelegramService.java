package com.tanghai.expense_tracker.service;

import com.tanghai.expense_tracker.config.TelegramProperties;
import com.tanghai.expense_tracker.util.MessageFormatter;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;

@Service
public class TelegramService {

    private final TelegramProperties telegramProperties;
    private final RestTemplate restTemplate = new RestTemplate();

    public TelegramService(TelegramProperties telegramProperties) {
        this.telegramProperties = telegramProperties;
    }

    public String sendMessage(String message) {


        String url = "https://api.telegram.org/bot" +
                telegramProperties.getBotToken() + "/sendMessage";

        Map<String, String> request = new HashMap<>();
        request.put("chat_id", telegramProperties.getChatId());
        request.put("text", MessageFormatter.formatAlertMessage(1, message, new Date()));

        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);

        HttpEntity<Map<String, String>> entity = new HttpEntity<>(request, headers);

        ResponseEntity<String> response = restTemplate.postForEntity(url, entity, String.class);

        return response.getBody();
    }

}
