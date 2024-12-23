package com.example.projecttelegrambot.bot;

import com.example.projecttelegrambot.controllers.PollController;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Lazy;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Service;
import org.telegram.telegrambots.bots.TelegramLongPollingBot;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.Update;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;

@Component
public class Bot extends TelegramLongPollingBot {

    @Autowired
    @Lazy
    private PollController pollController;

    @Value("${telegram.bot.token}")
    private String botToken;
    @Override
    public String getBotUsername() {
        return "DostiVen_Bot";
    }

    @Override
    public String getBotToken() {
        return botToken;
    }

    @Override
    public void onUpdateReceived(Update update) {
        if (update.hasMessage() && update.getMessage().hasText()) {
            String msg = update.getMessage().getText();
            var user = update.getMessage().getFrom();
            String chatId = update.getMessage().getChatId().toString();

            if(msg.startsWith("/poll")) {
                pollController.handlePollCommand(chatId, msg);
                //pollController.sendPoll(chatId, 1L, "What is your favorite color?", new String[]{"Red", "Green", "Blue"});
            }

            else{
                sendMessage(chatId, "Hello, How can I help? ");
            }

        }else if(update.hasCallbackQuery()){
            pollController.handleCallbackQuery(update.getCallbackQuery());
        }
    }
    public void sendMessage(String chatId, String responseMessage) {
        SendMessage sendMessage = new SendMessage();
        sendMessage.setChatId(chatId);
        sendMessage.setText(responseMessage);

        try {
            execute(sendMessage);
            //System.out.println("Message sent: " + responseMessage);
        } catch (TelegramApiException e) {
            System.err.println(e.getMessage());
            e.printStackTrace();
        }

    }
}
