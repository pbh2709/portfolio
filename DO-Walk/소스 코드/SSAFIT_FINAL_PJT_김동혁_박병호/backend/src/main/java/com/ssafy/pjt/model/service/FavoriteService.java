package com.ssafy.pjt.model.service;

import com.ssafy.pjt.model.dto.Video;

import java.util.List;

public interface FavoriteService {
    void addFavorite(String userId, int videoNo);
    void removeFavorite(String userId, int videoNo);
    List<Video> getFavoritesByUser(String userId);
}
