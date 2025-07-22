package com.tanghai.expense_tracker.service;

import com.tanghai.expense_tracker.component.TelegramProperties;
import com.tanghai.expense_tracker.constant.Static;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.util.HashMap;
import java.util.Map;

@Service
public class TelegramService {

    private final TelegramProperties telegramProperties;
    private final RestTemplate restTemplate = new RestTemplate();

    public TelegramService(TelegramProperties telegramProperties) {
        this.telegramProperties = telegramProperties;
    }

    public void sendMessage(String message) {

        Map<String, String> request = new HashMap<>();
        request.put(Static.CHAT_ID, telegramProperties.getChatId());
        request.put(Static.TEXT, message);

        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);

        HttpEntity<Map<String, String>> entity = new HttpEntity<>(request, headers);

        if(telegramProperties.isEnable()) {
            restTemplate.postForEntity(
                    Static.TELEGRAM_BOT_URL + telegramProperties.getBotToken() + Static.SEND_MESSAGE,
                    entity,
                    String.class);
        }
    }

}
