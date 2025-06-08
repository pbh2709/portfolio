package com.ssafy.pjt.controller;

import com.ssafy.pjt.auth.JwtUtil;
import com.ssafy.pjt.model.dto.Video;
import com.ssafy.pjt.model.service.FavoriteService;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/favorite")
public class FavoriteController {

    @Autowired
    private FavoriteService favoriteService;

    @Autowired
    private JwtUtil jwtUtil;

    // ✅ 찜 등록
    @PostMapping("/{videoNo}")
    @Operation(summary = "비디오 찜하기", description = "로그인한 사용자가 특정 비디오를 찜 목록에 추가합니다.")
    public ResponseEntity<String> addFavorite(
            @Parameter(description = "찜할 비디오 번호") @PathVariable int videoNo,
            @RequestHeader("Authorization") String token) {

        String userId = jwtUtil.getUserIdFromToken(token.replace("Bearer ", ""));
        favoriteService.addFavorite(userId, videoNo);
        return ResponseEntity.ok("찜 완료!");
    }

    // ✅ 찜 해제
    @DeleteMapping("/{videoNo}")
    @Operation(summary = "비디오 찜 해제", description = "로그인한 사용자가 특정 비디오를 찜 목록에서 제거합니다.")
    public ResponseEntity<String> removeFavorite(
            @Parameter(description = "찜 해제할 비디오 번호") @PathVariable int videoNo,
            @RequestHeader("Authorization") String token) {

        String userId = jwtUtil.getUserIdFromToken(token.replace("Bearer ", ""));
        favoriteService.removeFavorite(userId, videoNo);
        return ResponseEntity.ok("찜 해제됨!");
    }

    // ✅ 찜한 영상 목록 조회
    @GetMapping
    @Operation(summary = "찜한 비디오 목록 조회", description = "로그인한 사용자의 찜한 비디오들을 반환합니다.")
    public ResponseEntity<List<Video>> getMyFavorites(
            @RequestHeader("Authorization") String token) {

        String userId = jwtUtil.getUserIdFromToken(token.replace("Bearer ", ""));
        List<Video> favorites = favoriteService.getFavoritesByUser(userId);
        return ResponseEntity.ok(favorites);
    }
}
