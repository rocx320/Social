package com.example.social.extra;

public class CountUser {
    private int count;

    public CountUser(int initialValue) {
        this.count = initialValue;
    }

    public int getCount() {
        return count;
    }

    public void incrementCount() {
        count++;
    }
}
