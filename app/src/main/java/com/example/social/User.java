package com.example.social;

public class User {

    private String username;
    // Required default constructor for Firebase
    public User() {
    }

    public User(String username) {
        this.username = username;
    }

    public String getUsername() {
        return username;
    }


}
