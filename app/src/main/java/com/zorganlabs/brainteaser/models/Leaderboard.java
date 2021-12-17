package com.zorganlabs.brainteaser.models;

public class Leaderboard {
    // member variables
    private int id;
    private int correct;
    private int incorrect;
    private int total;
    private String category;

    // getters and setters
    public int getTotal() {
        return total;
    }

    public void setTotal(int total) {
        this.total = total;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getCorrect() {
        return correct;
    }

    public void setCorrect(int correct) {
        this.correct = correct;
    }

    public int getIncorrect() {
        return incorrect;
    }

    public void setIncorrect(int incorrect) {
        this.incorrect = incorrect;
    }

    public String getCategory() {
        return category;
    }

    public void setCategory(String category) {
        this.category = category;
    }

    // constructor
    public Leaderboard() {
    }

    // parameterized constructor
    public Leaderboard(int correct, int incorrect, String category) {
        this.correct = correct;
        this.incorrect = incorrect;
        this.category = category;
    }
}
