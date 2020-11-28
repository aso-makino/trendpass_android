package com.example.trendpass.ui.login;

/**
 * Class exposing authenticated user details to the UI.
 */
class LoggedInUserView {
    private String userId;
    private String userName;
    private String userIcon;

    public LoggedInUserView(String userId, String userName, String userIcon) {
        this.userId = userId;
        this.userName = userName;
        this.userIcon = userIcon;
    }

    public String getUserId() {
        return userId;
    }

    public String getUserName() {
        return userName;
    }

    public String getUserIcon() {
        return userIcon;
    }
}
