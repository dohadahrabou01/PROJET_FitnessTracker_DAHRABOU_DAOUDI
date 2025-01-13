package com.example.fitnesstracker.database;

public class HydrationRecommendation {

    private String message;
    public HydrationRecommendation(String s) {
        message=s;
    }

    public String getRecommendation() {
        return  message;
    }
}
