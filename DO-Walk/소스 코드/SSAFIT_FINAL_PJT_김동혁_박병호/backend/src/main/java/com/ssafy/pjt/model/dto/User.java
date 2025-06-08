package com.ssafy.pjt.model.dto;

public class User {
    private String userId;
    private String userName;
    private String password;
    private String email;
    private int age;

    public User() {}

    public User(String userId, String userName, String password, String email, int age) {
        this.userId = userId;
        this.userName = userName;
        this.password = password;
        this.email = email;
        this.age = age;
    }

    public String getUserId() {
        return userId;
    }
    public void setUserId(String userId) {
        this.userId = userId;
    }

    public String getUserName() {
        return userName;
    }
    public void setUserName(String userName) {
        this.userName = userName;
    }

    public String getPassword() {
        return password;
    }
    public void setPassword(String password) {
        this.password = password;
    }

    public String getEmail() {
        return email;
    }
    public void setEmail(String email) {
        this.email = email;
    }

    public int getAge() {
        return age;
    }
    public void setAge(int age) {
        this.age = age;
    }
}
