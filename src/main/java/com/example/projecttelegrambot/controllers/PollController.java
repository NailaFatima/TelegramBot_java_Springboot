package com.example.projecttelegrambot.controllers;
import com.example.projecttelegrambot.bot.Bot;
import com.example.projecttelegrambot.services.PollServices;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Lazy;
import org.springframework.stereotype.Controller;
import org.telegram.telegrambots.meta.api.methods.polls.SendPoll;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.methods.updatingmessages.EditMessageReplyMarkup;
import org.telegram.telegrambots.meta.api.objects.CallbackQuery;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.InlineKeyboardMarkup;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.ReplyKeyboardMarkup;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.buttons.InlineKeyboardButton;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.buttons.KeyboardButton;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.buttons.KeyboardRow;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Controller
public class PollController {

    @Autowired
    @Lazy
    private PollServices pollService;

    @Autowired
    @Lazy
    private Bot bot;

    private final Map<Long, Map<String, Integer>> pollVotes = new HashMap<>();
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

        pollVotes.put(pollId, new HashMap<>());
        for (String option : options) {
            pollVotes.get(pollId).put(option, 0);
        }

        sendPoll(chatId, pollId, question, options);
    }

    public void sendPoll(String chatId, Long pollId, String question, String[] options) {
        SendMessage message = new SendMessage();
        message.setChatId(chatId);
        message.setText(question);


//        ReplyKeyboardMarkup keyboardMarkup = new ReplyKeyboardMarkup();
//        keyboardMarkup.setOneTimeKeyboard(true);

//        ReplyKeyboardMarkup keyboardMarkup = new ReplyKeyboardMarkup();
//        keyboardMarkup.setOneTimeKeyboard(true); // Enable one_time_keyboard
//
//        List<KeyboardRow> keyboard = new ArrayList<>();
//        KeyboardRow row = new KeyboardRow();
//        row.add(new KeyboardButton("Option 1"));
//        row.add(new KeyboardButton("Option 2"));
//        keyboard.add(row);
//        keyboardMarkup.setKeyboard(keyboard);
//        message.setReplyMarkup(keyboardMarkup);

        InlineKeyboardMarkup keyboardMarkup = new InlineKeyboardMarkup();
        List<List<InlineKeyboardButton>> rows = new ArrayList<>();

        for (int i = 0; i < options.length; i++) {
            InlineKeyboardButton button = new InlineKeyboardButton();
            EditMessageReplyMarkup editMessageReplyMarkup = new EditMessageReplyMarkup();
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
    public void handleCallbackQuery(CallbackQuery callbackQuery) {
        String callbackData = callbackQuery.getData();
        String chatId = callbackQuery.getMessage().getChatId().toString();

        String[] parts = callbackData.split(":");
        if (parts.length != 2) {
            sendMessage(chatId, "Invalid callback data.");
            return;
        }

        Long pollId = Long.valueOf(parts[0]);
        String optionIndexStr = parts[1].replace("option", "");

        try {
            int optionIndex = Integer.parseInt(optionIndexStr);
            String[] options = pollService.getPoll(pollId).getOptions();
            if (optionIndex >= options.length) {
                sendMessage(chatId, "Invalid option selected.");
                return;
            }

            String selectedOption = options[optionIndex];


            pollVotes.get(pollId).merge(selectedOption, 1, Integer::sum);
            bot.execute(new SendMessage(chatId, "You voted for: " + selectedOption));

            // Show updated results
            showPollResults(chatId, pollId, options);
        } catch (Exception e) {
            e.printStackTrace();
            sendMessage(chatId, "An error occurred while processing your vote.");
        }
    }

    private void showPollResults(String chatId, Long pollId, String[] options) {
        StringBuilder results = new StringBuilder("Poll Results:\n");

        Map<String, Integer> votes = pollVotes.get(pollId);
        for (String option : options) {
            results.append(option).append(": ").append(votes.get(option)).append(" votes\n");
        }

        sendMessage(chatId, results.toString());
    }


    private void sendMessage(String chatId, String text) {
        // Simplified sendMessage implementation

        bot.sendMessage(chatId, text);
        //new Bot().sendMessage(chatId, text);
    }
}
