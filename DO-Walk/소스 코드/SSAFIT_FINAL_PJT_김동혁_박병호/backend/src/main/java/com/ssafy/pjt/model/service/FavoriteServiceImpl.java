package com.ssafy.pjt.model.service;

import com.ssafy.pjt.model.dao.FavoriteDao;
import com.ssafy.pjt.model.dto.Video;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class FavoriteServiceImpl implements FavoriteService {

    @Autowired
    private FavoriteDao favoriteDao;

    @Override
    public void addFavorite(String userId, int videoNo) {
        favoriteDao.insertFavorite(userId, videoNo);
    }

    @Override
    public void removeFavorite(String userId, int videoNo) {
        favoriteDao.deleteFavorite(userId, videoNo);
    }

    @Override
    public List<Video> getFavoritesByUser(String userId) {
        return favoriteDao.selectFavoritesByUser(userId);
    }
}
