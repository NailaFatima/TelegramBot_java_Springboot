package com.example.projecttelegrambot.services;
import com.example.projecttelegrambot.models.Poll;
import org.jvnet.hk2.annotations.Service;

import java.util.HashMap;

@Service
public class PollServices {
    private final HashMap<Long, Poll> Polls = new HashMap<>();
    private int idCounter = 0;

    public Long createPoll(String chatId, String question, String[] options) {

        Poll poll = new Poll();
        poll.setQuestion(question);
        poll.setOptions(options);
        poll.setCreatorChatId(chatId);
        poll.setClosed(false);

        //String pollId = UUID.randomUUID().toString();
        poll.setId(idCounter++);
        Polls.put(poll.getId(), poll);

        return poll.getId();
    }
    public Poll getPoll(Long pollId) {
        return Polls.get(pollId);
    }
}
