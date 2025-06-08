package com.ssafy.pjt.model.dto;

import java.util.List;

public class VideoDetailResponse {
    private Video video;
    private List<Review> reviewList;

    public VideoDetailResponse() {
    }

    public VideoDetailResponse(Video video, List<Review> reviewList) {
        this.video = video;
        this.reviewList = reviewList;
    }

    public Video getVideo() {
        return video;
    }

    public void setVideo(Video video) {
        this.video = video;
    }

    public List<Review> getReviewList() {
        return reviewList;
    }

    public void setReviewList(List<Review> reviewList) {
        this.reviewList = reviewList;
    }
}