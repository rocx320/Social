package com.example.social.extra;

public class User {

    private String username;

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    private String email;
    private String name;

    public User(String name, String email, String username, String password) {
        this.username = username;
        this.email = email;
        this.name = name;
        this.password = password;
    }

    private String password;


    public User() {
    }
}
