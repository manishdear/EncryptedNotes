package com.wordpress.uniquecoder.encryptednotes;

public class userProfile {
    String userId;
    String userName;
    String userEmailid;
    String loginId;

    public userProfile() {
    }

    public userProfile(String userId, String userName, String userEmailid) {
        this.userId = userId;
        this.userName = userName;
        this.userEmailid = userEmailid;
    }

    public String getUserId() {
        return userId;
    }

    public String getUserName() {
        return userName;
    }

    public String getUserEmailid() {
        return userEmailid;
    }

    public void setLoginId(String loginId) {
        this.loginId = loginId;
    }
}
