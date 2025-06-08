package com.ssafy.pjt.model.service;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.ssafy.pjt.model.dao.ReviewDao;
import com.ssafy.pjt.model.dao.VideoDao;
import com.ssafy.pjt.model.dto.Video;

@Service
public class VideoServiceImpl implements VideoService {

    @Autowired
    private VideoDao videoDao;
    
    @Autowired
    private ReviewDao reviewDao;

    @Override
    public List<Video> getAll() {
        return videoDao.selectAll();
    }

    @Override
    public Video getById(int videoNo) {
        return videoDao.selectById(videoNo);
    }

    @Override
    public boolean insert(Video video) {
        return videoDao.insert(video) > 0;
    }

    @Override
    public boolean update(Video video) {
        return videoDao.update(video) > 0;
    }

    @Transactional
    @Override
    public boolean delete(int videoNo) {
        videoDao.deleteReviewsByVideoNo(videoNo);   // 1. 리뷰 먼저 삭제
        return videoDao.delete(videoNo) > 0;  // 2. 비디오 삭제
    }

    @Override
    public List<Video> search(String keyword) {
        return videoDao.searchByTitle(keyword);
    }
    
    @Override
    public List<Video> getAllSortedByViewCount() {
        return videoDao.selectAllSortedByViewCount();
    }
    
    @Override
    public List<Video> getByField(String field) {
        return videoDao.selectByField(field);
    }
    
    
    @Override
    public List<Video> filterAndSort(String field, String sort) {
        return videoDao.selectByFieldAndSort(field, sort);
    }
    
    @Override
    public List<Video> getPaged(int offset, int size) {
        return videoDao.getPaged(offset, size); // ✅ 이걸 꼭 구현!
    }

    @Override
    public int getTotalCount() {
        return videoDao.getTotalCount();
    }
    
    @Override
    public void insertIfNotExists(Video video) {
        if (!videoDao.existsByVideoId(video.getVideoId())) {
            videoDao.insertFromYoutube(video); // 👉 유튜브용 insert 사용
        }
    }
    
    @Override
    public List<Video> selectLatestRecommendedForUser(String userId) {
        return videoDao.selectLatestRecommendedForUser(userId);
    }

    @Override
    public Set<String> getFavoriteVideoIdsByUser(String userId) {
        return new HashSet<>(videoDao.getFavoriteVideoIdsByUser(userId));
    }
    
    @Override
    public int insertUserRecommendation(String userId, String videoId) {
        return videoDao.insertUserRecommendation(userId, videoId);
    }
}