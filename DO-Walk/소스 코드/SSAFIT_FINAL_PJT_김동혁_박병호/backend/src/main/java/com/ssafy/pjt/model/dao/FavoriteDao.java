package com.ssafy.pjt.model.dao;

import com.ssafy.pjt.model.dto.Video;
import org.apache.ibatis.annotations.Param;

import java.util.List;

public interface FavoriteDao {
    void insertFavorite(@Param("userId") String userId, @Param("videoNo") int videoNo);
    void deleteFavorite(@Param("userId") String userId, @Param("videoNo") int videoNo);
    List<Video> selectFavoritesByUser(String userId);
}
