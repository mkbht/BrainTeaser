package com.zorganlabs.brainteaser.models;

public class QuizCategory {
    private String title, image;

    public QuizCategory() {

    }

    public QuizCategory(String title, String image) {
        this.title = title;
        this.image = image;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
    }
}
