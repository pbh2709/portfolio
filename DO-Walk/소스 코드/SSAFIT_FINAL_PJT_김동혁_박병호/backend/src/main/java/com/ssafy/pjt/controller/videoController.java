package com.ssafy.pjt.controller;

import com.ssafy.pjt.model.dto.Review;
import com.ssafy.pjt.model.dto.User;
import com.ssafy.pjt.model.dto.Video;
import com.ssafy.pjt.model.dto.VideoDetailResponse;
import com.ssafy.pjt.model.service.ReviewService;
import com.ssafy.pjt.model.service.VideoService;
import com.ssafy.pjt.auth.JwtUtil;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.*;

@RestController
@RequestMapping("/video")
public class videoController {

    @Autowired
    private VideoService videoService;

    @Autowired
    private ReviewService reviewService;

    @Autowired
    private JwtUtil jwtUtil;

    // ✅ 1. 전체 목록 (페이징)
    @GetMapping("/list")
    public ResponseEntity<Map<String, Object>> list(
            @RequestParam(defaultValue = "1") int page,
            @RequestParam(defaultValue = "10") int size) {

        int offset = (page - 1) * size;
        List<Video> videos = videoService.getPaged(offset, size);
        int totalCount = videoService.getTotalCount();
        int totalPages = (int) Math.ceil((double) totalCount / size);

        Map<String, Object> result = new HashMap<>();
        result.put("videos", videos);
        result.put("currentPage", page);
        result.put("totalPages", totalPages);

        return ResponseEntity.ok(result);
    }

    // ✅ 2. 비디오 등록
    @PostMapping("/save")
    public ResponseEntity<?> save(@RequestBody Video video) {
        videoService.insert(video);
        return ResponseEntity.ok("비디오 등록 성공");
    }

    // ✅ 3. 비디오 수정 폼
    @GetMapping("/update/{videoNo}")
    public ResponseEntity<Video> videoUpdateForm(@PathVariable int videoNo) {
        return ResponseEntity.ok(videoService.getById(videoNo));
    }

    // ✅ 4. 비디오 수정 처리
    @PutMapping("/update")
    public ResponseEntity<?> update(@RequestBody Video video) {
        videoService.update(video);
        return ResponseEntity.ok("비디오 수정 성공");
    }

    // ✅ 5. 비디오 삭제
    @DeleteMapping("/delete/{videoNo}")
    public ResponseEntity<?> delete(@PathVariable int videoNo) {
        videoService.delete(videoNo);
        return ResponseEntity.ok("비디오 삭제 성공");
    }

    // ✅ 6. 비디오 상세보기 (+ 리뷰 리스트)
    @GetMapping("/detail/{videoNo}")
    public ResponseEntity<VideoDetailResponse> detail(@PathVariable int videoNo) {
        Video video = videoService.getById(videoNo);
        List<Review> reviews = reviewService.getReviewsByVideoNo(videoNo);
        return ResponseEntity.ok(new VideoDetailResponse(video, reviews));
    }

    // ✅ 7. 검색
    @GetMapping("/search")
    public ResponseEntity<List<Video>> search(@RequestParam String keyword) {
        return ResponseEntity.ok(videoService.search(keyword));
    }

    // ✅ 8. 조회수순 정렬
    @GetMapping("/sort/viewcount")
    public ResponseEntity<List<Video>> sortByViewCount() {
        return ResponseEntity.ok(videoService.getAllSortedByViewCount());
    }

    // ✅ 9. 운동 부위로 필터링
    @GetMapping("/filter")
    public ResponseEntity<List<Video>> filterByField(@RequestParam String field) {
        return ResponseEntity.ok(videoService.getByField(field));
    }

    // ✅ 10. 부위 + 정렬 필터링
    @GetMapping("/filter-advanced")
    @Operation(
            summary = "운동 부위 및 정렬 기준으로 비디오 필터링",
            description = "운동 부위(field)와 정렬 기준(sort: view, like, default)을 입력받아 필터링된 비디오 목록을 반환합니다."
    )
    public ResponseEntity<List<Video>> filterAndSort(
            @Parameter(description = "운동 부위 이름 (예: 복근, 하체, 가슴 등)", example = "복근")
            @RequestParam(required = false) String field,

            @Parameter(description = "정렬 기준 (view: 조회수순, like: 좋아요순, default: 기본)", example = "view")
            @RequestParam(required = false, defaultValue = "default") String sort
    ) {
        return ResponseEntity.ok(videoService.filterAndSort(field, sort));
    }

    // ✅ 11. 로그인 유저의 추천 영상 (최신 기분추천 + 찜 여부 포함)
    @GetMapping("/list/user")
    public ResponseEntity<List<Video>> getLatestMoodRecommendedVideos(
            @RequestHeader("Authorization") String authorizationHeader) {

        String token = authorizationHeader.replace("Bearer ", "");
        String userId = jwtUtil.getUserIdFromToken(token);

        List<Video> videos = videoService.selectLatestRecommendedForUser(userId);
        Set<String> favorites = videoService.getFavoriteVideoIdsByUser(userId);

        for (Video video : videos) {
            video.setIsFavorite(favorites.contains(video.getVideoId()));
        }

        return ResponseEntity.ok(videos);
    }
}
