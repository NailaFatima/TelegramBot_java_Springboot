package com.example.projecttelegrambot.controllers;

import com.example.projecttelegrambot.bot.Bot;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/bot")
public class BotController {
    private final Bot bot;
    @Autowired
    public BotController(Bot bot) {
        this.bot = bot;
    }
    @GetMapping("/ping")
    public String pingBot() {
        return "Telegram Bot is running";
    }


}
