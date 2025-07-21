package com.tanghai.expense_tracker.service;

import com.tanghai.expense_tracker.config.TelegramProperties;
import com.tanghai.expense_tracker.constant.Static;
import com.tanghai.expense_tracker.util.MessageFormatter;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.util.UriComponentsBuilder;

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

    public void sendMessage(String message) {
        String telegramBotUrl = UriComponentsBuilder
                .fromHttpUrl(Static.TELEGRAM_BOT_URL)
                .path(telegramProperties.getBotToken())
                .path(Static.TELEGRAM_BOT_URL)
                .toUriString();

        Map<String, String> request = new HashMap<>();
        request.put(Static.CHAT_ID, telegramProperties.getChatId());
        request.put(Static.TEXT, message);

        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);

        HttpEntity<Map<String, String>> entity = new HttpEntity<>(request, headers);

        if(telegramProperties.isEnable()) {
            restTemplate.postForEntity(telegramBotUrl, entity, String.class);
        }
    }

}
