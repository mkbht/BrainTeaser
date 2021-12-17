package com.zorganlabs.brainteaser.models;

public class QuizCategory {
    // member variables
    private String title, image;

    // constructor
    public QuizCategory() {

    }

    // parameterized constructor
    public QuizCategory(String title, String image) {
        this.title = title;
        this.image = image;
    }

    // getters and setters
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
