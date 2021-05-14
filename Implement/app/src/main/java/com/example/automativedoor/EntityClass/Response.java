package com.example.automativedoor.EntityClass;

public class Response {
    private int score;
    private String text;

    public Response(int score, String text) {
        this.score = score;
        this.text = text;
    }

    public int getScore() {
        return score;
    }

    public void setScore(int score) {
        this.score = score;
    }

    public String getText() {
        return text;
    }

    public void setText(String text) {
        this.text = text;
    }
}
