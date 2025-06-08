package com.ssafy.pjt.model.dao;

import java.util.List;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import com.ssafy.pjt.model.dto.Video;

@Mapper
public interface VideoDao {
    List<Video> selectAll();
    Video selectById(int videoNo);
    int insert(Video video);
    int update(Video video);
    int delete(int videoNo);
    void deleteReviewsByVideoNo(int videoNo);
    List<Video> searchByTitle(String keyword);
    List<Video> selectAllSortedByViewCount();
    List<Video> selectByField(String field);
    List<Video> selectByFieldAndSort(@Param("field") String field, @Param("sort") String sort);
    List<Video> getPaged(@Param("offset") int offset, @Param("size") int size);
    int getTotalCount();
    boolean existsByVideoId(String videoId);
    void insertFromYoutube(Video video);
    List<Video> selectLatestRecommendedForUser(String userId);
    List<String> getFavoriteVideoIdsByUser(String userId);
    int insertUserRecommendation(@Param("userId") String userId, @Param("videoId") String videoId);
}