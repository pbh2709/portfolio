package com.ssafy.pjt.model.service;

import java.util.List;
import java.util.Set;

import com.ssafy.pjt.model.dto.Video;

public interface VideoService {
    List<Video> getAll();
    Video getById(int videoNo);
    boolean insert(Video video);
    boolean update(Video video);
    boolean delete(int videoNo);
    List<Video> search(String keyword);
    List<Video> getAllSortedByViewCount();
    List<Video> getByField(String field);
    List<Video> filterAndSort(String field, String sort);
    List<Video> getPaged(int offset, int size);
    int getTotalCount();
    void insertIfNotExists(Video video);
    List<Video> selectLatestRecommendedForUser(String userId); // 추천 영상 3개
    Set<String> getFavoriteVideoIdsByUser(String userId);  
    int insertUserRecommendation(String userId, String videoId);
    
}