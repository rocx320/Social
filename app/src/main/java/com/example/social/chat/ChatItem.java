package com.example.social.chat;

public class ChatItem {
    private int profileImageRes;
    private String profileName;

    public ChatItem(int profileImageRes, String profileName) {
        this.profileImageRes = profileImageRes;
        this.profileName = profileName;
    }

    public int getProfileImageRes() {
        return profileImageRes;
    }

    public String getProfileName() {
        return profileName;
    }
}

