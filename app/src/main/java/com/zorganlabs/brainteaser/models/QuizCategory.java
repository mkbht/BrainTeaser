package com.zorganlabs.brainteaser.models;

public class QuizCategory {
    private String title;

    public QuizCategory() {

    }

    public QuizCategory(String title) {
        this.title = title;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }
}
