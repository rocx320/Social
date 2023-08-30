package com.example.social.extra;

public class User {

    private String profileImageUrl;
    private String gender;
    private String birthdate;
    private String username;
    private String email;
    private String name;
    private String password;


    public User(String name, String email, String username, String password) {
        this.username = username;
        this.email = email;
        this.name = name;
        this.password = password;
    }

    public User(String profileImageUrl, String gender, String birthdate) {
        this.profileImageUrl = profileImageUrl;
        this.gender = gender;
        this.birthdate = birthdate;
    }

    public User() {
    }

    public String getProfileImageUrl() {
        return profileImageUrl;
    }

    public void setProfileImageUrl(String profileImageUrl) {
        this.profileImageUrl = profileImageUrl;
    }

    public String getGender() {
        return gender;
    }

    public void setGender(String gender) {
        this.gender = gender;
    }

    public String getBirthdate() {
        return birthdate;
    }

    public void setBirthdate(String birthdate) {
        this.birthdate = birthdate;
    }

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
}
