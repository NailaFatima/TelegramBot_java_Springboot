package com.example.projecttelegrambot.configs;

import com.example.projecttelegrambot.bot.Bot;
import com.example.projecttelegrambot.services.PollServices;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.telegram.telegrambots.meta.TelegramBotsApi;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;
import org.telegram.telegrambots.updatesreceivers.DefaultBotSession;

@Configuration
public class BotConfig {
    private final Bot bot;

    public BotConfig(Bot bot) {
        this.bot = bot;
    }
    @Bean
    public TelegramBotsApi telegramBotsApi () throws TelegramApiException {
        TelegramBotsApi botsApi = new TelegramBotsApi(DefaultBotSession.class);
        botsApi.registerBot(bot);
        System.out.println("Telegram Bot registered successfully!");
        return botsApi;
    }

    @Bean
    public PollServices pollServices() {
        return new PollServices();
    }
}
