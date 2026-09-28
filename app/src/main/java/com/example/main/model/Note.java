package com.example.main.model;

public class Note {
    private long id;
    private long userId;
    private String title;
    private String content;
    private long createdAt;

    public Note(long userId, String title, String content) {
        this.userId = userId;
        this.title = title;
        this.content = content;
        this.createdAt = System.currentTimeMillis();
    }

    public Note(long id, long userId, String title, String content, long createdAt) {
        this.id = id;
        this.userId = userId;
        this.title = title;
        this.content = content;
        this.createdAt = createdAt;
    }

    public long getId() { return id; }
    public long getUserId() { return userId; }
    public String getTitle() { return title; }
    public void setTitle(String title) { this.title = title; }
    public String getContent() { return content; }
    public void setContent(String content) { this.content = content; }
    public long getCreatedAt() { return createdAt; }
}
