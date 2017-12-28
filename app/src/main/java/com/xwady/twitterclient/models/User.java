package com.xwady.twitterclient.models;

public class User {
    private String name,
    token,
    secret;
    private long userId;
    private AccountStatus accountStatus=AccountStatus.LOGGED_OUT;

    public User(String name, String token, String secret, long userId) {
        this.name = name;
        this.token = token;
        this.secret = secret;
        this.userId = userId;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }

    public String getSecret() {
        return secret;
    }

    public void setSecret(String secret) {
        this.secret = secret;
    }

    public AccountStatus getAccountStatus() {
        return accountStatus;
    }

    public void setAccountStatus(AccountStatus accountStatus) {
        this.accountStatus = accountStatus;
    }

    public enum AccountStatus {
        LOGGED_IN, LOGGED_OUT
    }

    public long getUserId() {
        return userId;
    }
}

