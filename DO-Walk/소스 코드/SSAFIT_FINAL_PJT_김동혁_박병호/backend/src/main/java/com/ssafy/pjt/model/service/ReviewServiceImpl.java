package com.ssafy.pjt.model.service;

import java.util.List;

import org.springframework.stereotype.Service;

import com.ssafy.pjt.model.dao.ReviewDao;
import com.ssafy.pjt.model.dto.Review;


@Service
public class ReviewServiceImpl implements ReviewService{

	private final ReviewDao reviewDao;
	
	public ReviewServiceImpl(ReviewDao reviewDao) {
		this.reviewDao = reviewDao;
	}
	
	@Override
	public List<Review> getReviewList() {
		System.out.println("리뷰 전체 목록");
		return reviewDao.selectAllReview();
	}

	@Override
	public Review readReview(int id) {
		System.out.println("리뷰 상세 조회");
		return reviewDao.selectReviewById(id);
	}
	
	
	@Override
	public void addReview(Review review) {
		System.out.println("리뷰 작성");
		reviewDao.insertReview(review);
	}

	@Override
	public void removeReview(int id) {
		System.out.println("리뷰 삭제");
		reviewDao.deleteReview(id);
	}

	@Override
	public void modifyReview(Review review) {
		System.out.println("리뷰 수정");
		reviewDao.updateReview(review);
		
	}

	@Override
	public List<Review> getReviewsByVideoNo(int videoNo){
		return reviewDao.getReviewsByVideoNo(videoNo);
		
	}

}
