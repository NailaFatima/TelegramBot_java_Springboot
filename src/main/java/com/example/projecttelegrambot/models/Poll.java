package com.example.projecttelegrambot.models;

public class Poll extends BaseModel {
    private String question;
   private String[] options;

    public String getQuestion() {
        return question;
    }

    public void setQuestion(String question) {
        this.question = question;
    }

    public String[] getOptions() {
        return options;
    }

    public void setOptions(String[] options) {
        this.options = options;
    }

    public boolean isClosed() {
        return isClosed;
    }

    public void setClosed(boolean closed) {
        isClosed = closed;
    }

    public String getCreatorChatId() {
        return creatorChatId;
    }

    public void setCreatorChatId(String creatorChatId) {
        this.creatorChatId = creatorChatId;
    }

    private boolean isClosed;
    private String creatorChatId;
}
