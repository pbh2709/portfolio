package com.ssafy.pjt.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import com.ssafy.pjt.auth.JwtUtil;
import com.ssafy.pjt.model.dto.Review;
import com.ssafy.pjt.model.service.ReviewService;

@RestController
@RequestMapping("/review")
public class ReviewController {

    private final ReviewService reviewService;
    private final JwtUtil jwtUtil;

    @Autowired
    public ReviewController(ReviewService reviewService, JwtUtil jwtUtil) {
        this.reviewService = reviewService;
        this.jwtUtil = jwtUtil;
    }

    // ✅ 리뷰 전체 목록 조회
    @GetMapping("/list")
    public ResponseEntity<List<Review>> list() {
        List<Review> list = reviewService.getReviewList();
        return ResponseEntity.ok(list);
    }

    // ✅ 특정 리뷰 상세 조회
    @GetMapping("/detail/{id}")
    public ResponseEntity<?> detail(@PathVariable("id") int id) {
        Review review = reviewService.readReview(id);
        if (review == null) {
            return ResponseEntity.notFound().build();
        }
        return ResponseEntity.ok(review);
    }

    // ✅ 리뷰 등록 (JWT 기반)
    @PostMapping("/write")
    public ResponseEntity<?> write(
            @RequestHeader("Authorization") String token,
            @RequestBody Review review) {

        String userId = jwtUtil.getUserIdFromToken(token.replace("Bearer ", ""));
        review.setWriterId(userId);

        System.out.println("🔥 받은 Review = " + review);
        reviewService.addReview(review);
        return ResponseEntity.ok("리뷰 등록 성공");
    }

    // ✅ 리뷰 수정
    @PutMapping("/update")
    public ResponseEntity<?> update(@RequestBody Review review) {
        reviewService.modifyReview(review);
        return ResponseEntity.ok("리뷰 수정 성공");
    }

    // ✅ 리뷰 삭제
    @DeleteMapping("/delete/{id}")
    public ResponseEntity<?> delete(@PathVariable("id") int id) {
        reviewService.removeReview(id);
        return ResponseEntity.ok("리뷰 삭제 성공");
    }
}
