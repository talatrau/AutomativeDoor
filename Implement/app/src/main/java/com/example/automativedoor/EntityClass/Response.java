package com.example.automativedoor.EntityClass;

public class Response {
    private int score;
    private String text;
    private String email;

    public Response(int score, String text, String email) {
        this.score = score;
        this.text = text;
        this.email = email;
    }

    public int getScore() {
        return score;
    }

    public void setScore(int score) {
        this.score = score;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getText() {
        return text;
    }

    public void setText(String text) {
        this.text = text;
    }
}
