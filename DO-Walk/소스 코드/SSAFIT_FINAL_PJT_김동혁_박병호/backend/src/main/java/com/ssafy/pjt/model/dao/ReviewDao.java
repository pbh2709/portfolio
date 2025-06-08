package com.ssafy.pjt.model.dao;

import java.util.List;

import com.ssafy.pjt.model.dto.Review;

public interface ReviewDao {
	// 전체 게시글을 조회
	public List<Review> selectAllReview();

	Review selectReviewById(int id);

	// 게시글 등록
	public void insertReview(Review review);

	// 게시글 삭제
	public void deleteReview(int id);

	// 게시글 수정
	public void updateReview(Review review);
	
	public List<Review> getReviewsByVideoNo(int videoNo);

	

}
