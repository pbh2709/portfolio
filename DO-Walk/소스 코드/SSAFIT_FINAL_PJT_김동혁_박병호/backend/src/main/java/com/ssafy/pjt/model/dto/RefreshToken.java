package com.ssafy.pjt.model.dto;

import java.time.LocalDateTime;

public class RefreshToken {
    private String userId;
    private String token;
    private LocalDateTime expiry;

    public RefreshToken() {}

    public RefreshToken(String userId, String token, LocalDateTime expiry) {
        this.userId = userId;
        this.token = token;
        this.expiry = expiry;
    }

    // getter/setter 생략 가능 (롬복 써도 됨)
    public String getUserId() { return userId; }
    public void setUserId(String userId) { this.userId = userId; }

    public String getToken() { return token; }
    public void setToken(String token) { this.token = token; }

    public LocalDateTime getExpiry() { return expiry; }
    public void setExpiry(LocalDateTime expiry) { this.expiry = expiry; }
}
