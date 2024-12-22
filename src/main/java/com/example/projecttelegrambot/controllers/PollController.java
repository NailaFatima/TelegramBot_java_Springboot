package com.example.projecttelegrambot.controllers;
import com.example.projecttelegrambot.bot.Bot;
import com.example.projecttelegrambot.services.PollServices;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Lazy;
import org.springframework.stereotype.Controller;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.InlineKeyboardMarkup;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.buttons.InlineKeyboardButton;

import java.util.ArrayList;
import java.util.List;

@Controller
public class PollController {

    @Autowired
    @Lazy
    private PollServices pollService;

    @Autowired
    @Lazy
    private Bot bot;


    public void handlePollCommand(String chatId, String message) {
        // Example command: /poll What is your favorite color? Option1,Option2,Option3
        String[] parts = message.split(" ", 2);
        if (parts.length < 2) {
            sendMessage(chatId, "Usage: /poll <question> <option1,option2,...>");
            return;
        }

        String[] questionAndOptions = parts[1].split("\\?");
        if (questionAndOptions.length < 2) {
            sendMessage(chatId, "Invalid format. Use: /poll <question>? <option1,option2,...>");
            return;
        }

        String question = questionAndOptions[0].trim();
        String[] options = questionAndOptions[1].split(",");
        Long pollId = pollService.createPoll(chatId, question, options);

        sendPoll(chatId, pollId, question, options);
    }

    public void sendPoll(String chatId, Long pollId, String question, String[] options) {
        SendMessage message = new SendMessage();
        message.setChatId(chatId);
        message.setText(question);

        InlineKeyboardMarkup keyboardMarkup = new InlineKeyboardMarkup();
        List<List<InlineKeyboardButton>> rows = new ArrayList<>();

        for (int i = 0; i < options.length; i++) {
            InlineKeyboardButton button = new InlineKeyboardButton();
            button.setText(options[i]);
            button.setCallbackData(pollId + ":option" + i);
            rows.add(List.of(button));
        }

        keyboardMarkup.setKeyboard(rows);
        message.setReplyMarkup(keyboardMarkup);

        // Send poll
        try {
            bot.execute(message);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void sendMessage(String chatId, String text) {
        // Simplified sendMessage implementation

        bot.sendMessage(chatId, text);
        //new Bot().sendMessage(chatId, text);
    }
}
