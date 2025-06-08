package com.ssafy.pjt.controller;

import com.ssafy.pjt.auth.JwtUtil;
import com.ssafy.pjt.model.dto.Video;
import com.ssafy.pjt.model.service.MoodKeywordService;
import com.ssafy.pjt.model.service.VideoService;
import com.ssafy.pjt.model.service.YoutubeService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.*;

@RestController
@RequestMapping("/api/recommendation")
public class RecommendationController {

    @Autowired
    private MoodKeywordService keywordService;

    @Autowired
    private YoutubeService youtubeService;

    @Autowired
    private VideoService videoService;

    @PostMapping("/generate")
    public ResponseEntity<?> generateRecommendation(
            @RequestHeader(value = "Authorization", required = false) String authHeader,
            @RequestBody Map<String, String> request
    ) {
        System.out.println("🔐 Authorization 헤더 = " + authHeader);
        try {
            // ✅ 1. 토큰에서 userId 추출
            String token = authHeader.replace("Bearer ", "");
            String userId = JwtUtil.validateToken(token);

            String mood = request.get("mood");

            if (mood == null || mood.isBlank()) {
                return ResponseEntity.badRequest().body("기분이 입력되지 않았습니다.");
            }

            // ✅ 2. 기분 → 키워드 목록
            List<String> keywords = keywordService.getYoutubeSearchKeywordsByMood(mood);

            // ✅ 3. 키워드로 영상 검색
            List<Video> allVideos = new ArrayList<>();
            for (String keyword : keywords) {
                allVideos.addAll(youtubeService.fetchVideosByKeyword(keyword));
            }

            // ✅ 4. 셔플 + 3개 선택
            Collections.shuffle(allVideos);
            List<Video> selected = allVideos.stream().limit(3).toList();

            // ✅ 5. DB 저장
            for (Video video : selected) {
                video.setTag("기분추천");
                videoService.insertIfNotExists(video); // 중복 방지 저장
                videoService.insertUserRecommendation(userId, video.getVideoId()); // 추천 기록
            }

            return ResponseEntity.ok("추천 완료");

        } catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity.status(401).body("유효하지 않은 토큰이거나 로그인 필요");
        }
    }
}
