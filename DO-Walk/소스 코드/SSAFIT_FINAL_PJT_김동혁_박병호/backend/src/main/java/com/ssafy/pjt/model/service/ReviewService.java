package com.ssafy.pjt.model.service;

import java.util.List;

import com.ssafy.pjt.model.dto.Review;


public interface ReviewService {
	// 게시글 전체 조회
	public List<Review> getReviewList();

	// 게시글 상세조회 (클릭시 읽는거)
	public Review readReview(int id);
	
	// 게시글 상세조회 (수정용가져오기)
	//public Review getReview(int id);

	// 게시글 작성
	public void addReview(Review review);

	// 게시글 삭제
	public void removeReview(int id);

	// 게시글 수정
	public void modifyReview(Review review);
	
	//비디오 id로 리뷰 불러오기
	List<Review> getReviewsByVideoNo(int videoNo);
	
	

}
