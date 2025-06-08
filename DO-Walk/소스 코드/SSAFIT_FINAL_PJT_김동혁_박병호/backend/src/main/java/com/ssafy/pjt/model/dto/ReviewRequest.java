package com.ssafy.pjt.model.dto;

public class ReviewRequest {
    private String reviewText;
    private String mood;
    private String partnerId;

    // 기본 생성자
    public ReviewRequest() {}

    public String getReviewText() {
        return reviewText;
    }
    public void setReviewText(String reviewText) {
        this.reviewText = reviewText;
    }

    public String getMood() {
        return mood;
    }
    public void setMood(String mood) {
        this.mood = mood;
    }

    public String getPartnerId() {
        return partnerId;
    }
    public void setPartnerId(String partnerId) {
        this.partnerId = partnerId;
    }
}
