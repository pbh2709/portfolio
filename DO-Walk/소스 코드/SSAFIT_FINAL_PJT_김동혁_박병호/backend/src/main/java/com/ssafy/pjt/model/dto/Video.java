package com.ssafy.pjt.model.dto;

import java.util.Date;

public class Video {
    private int videoNo;
    private String videoId;             // ✅ 유튜브 고유 ID
    private String videoTitle;
    private String channelName;
    private String thumbnailUrl;        // ✅ 유튜브 썸네일 주소
    private int viewCount;
    private Date uploadDate;
    private byte videoLike;
    private String field;
    private int fitnessPartNo;
    private String tag;   
    private boolean isFavorite; // ✅ 추천 영상 구분용

    public Video() {}

    public Video(int videoNo, String videoId, String videoTitle, String channelName, String thumbnailUrl,
                 int viewCount, Date uploadDate, byte videoLike, String field, int fitnessPartNo, String tag) {
        this.videoNo = videoNo;
        this.videoId = videoId;
        this.videoTitle = videoTitle;
        this.channelName = channelName;
        this.thumbnailUrl = thumbnailUrl;
        this.viewCount = viewCount;
        this.uploadDate = uploadDate;
        this.videoLike = videoLike;
        this.field = field;
        this.fitnessPartNo = fitnessPartNo;
        this.tag = tag;
    }
    
    public boolean isFavorite() {
        return isFavorite;
    }

    public void setIsFavorite(boolean isFavorite) {
        this.isFavorite = isFavorite;
    }

    // Getter / Setter
    public int getVideoNo() { return videoNo; }
    public void setVideoNo(int videoNo) { this.videoNo = videoNo; }

    public String getVideoId() { return videoId; }
    public void setVideoId(String videoId) { this.videoId = videoId; }

    public String getVideoTitle() { return videoTitle; }
    public void setVideoTitle(String videoTitle) { this.videoTitle = videoTitle; }

    public String getChannelName() { return channelName; }
    public void setChannelName(String channelName) { this.channelName = channelName; }

    public String getThumbnailUrl() { return thumbnailUrl; }
    public void setThumbnailUrl(String thumbnailUrl) { this.thumbnailUrl = thumbnailUrl; }

    public int getViewCount() { return viewCount; }
    public void setViewCount(int viewCount) { this.viewCount = viewCount; }

    public Date getUploadDate() { return uploadDate; }
    public void setUploadDate(Date uploadDate) { this.uploadDate = uploadDate; }

    public byte getVideoLike() { return videoLike; }
    public void setVideoLike(byte videoLike) { this.videoLike = videoLike; }

    public String getField() { return field; }
    public void setField(String field) { this.field = field; }

    public int getFitnessPartNo() { return fitnessPartNo; }
    public void setFitnessPartNo(int fitnessPartNo) { this.fitnessPartNo = fitnessPartNo; }

    public String getTag() { return tag; }
    public void setTag(String tag) { this.tag = tag; }
}
