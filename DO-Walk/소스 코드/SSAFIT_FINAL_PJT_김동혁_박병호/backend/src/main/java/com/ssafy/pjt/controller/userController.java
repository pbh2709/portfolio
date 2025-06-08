package com.ssafy.pjt.controller;

import com.ssafy.pjt.auth.JwtUtil;
import com.ssafy.pjt.model.dto.User;
import com.ssafy.pjt.model.service.RefreshTokenService;
import com.ssafy.pjt.model.service.userService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@RestController
@RequestMapping("/user")
public class userController {

    @Autowired
    private userService userService;

    @Autowired
    private RefreshTokenService refreshTokenService;

    @Autowired
    private JwtUtil jwtUtil;

    // ✅ 로그인 (JWT 발급)
    @PostMapping("/login")
    public ResponseEntity<?> loginWithToken(@RequestBody User user) {
        User loginUser = userService.login(user.getUserId(), user.getPassword());

        if (loginUser != null) {
            String accessToken = JwtUtil.generateToken(loginUser.getUserId());
            String refreshToken = JwtUtil.generateRefreshToken(loginUser.getUserId());

            refreshTokenService.saveOrUpdate(loginUser.getUserId(), refreshToken);

            return ResponseEntity.ok(Map.of(
                    "accessToken", accessToken,
                    "refreshToken", refreshToken,
                    "userId", loginUser.getUserId(),
                    "userName", loginUser.getUserName()
            ));
        } else {
            return ResponseEntity.status(401).body("로그인 실패: 아이디 또는 비밀번호 오류");
        }
    }

    // ✅ Access Token 재발급
    @PostMapping("/refresh")
    public ResponseEntity<?> refreshToken(@RequestBody Map<String, String> request) {
        String refreshToken = request.get("refreshToken");

        String userId;
        try {
            userId = JwtUtil.validateToken(refreshToken);
        } catch (Exception e) {
            return ResponseEntity.status(401).body("유효하지 않은 Refresh Token");
        }

        String savedToken = refreshTokenService.getTokenByUserId(userId);
        if (!refreshToken.equals(savedToken)) {
            return ResponseEntity.status(401).body("서버에 저장된 토큰과 다름");
        }

        String newAccessToken = JwtUtil.generateToken(userId);
        return ResponseEntity.ok(Map.of("accessToken", newAccessToken));
    }

    // ✅ 유저 정보 조회 (JWT 기반)
    @GetMapping("/info")
    public ResponseEntity<?> getUserInfo(@RequestHeader("Authorization") String authHeader) {
        if (authHeader == null || !authHeader.startsWith("Bearer ")) {
            return ResponseEntity.status(401).body("토큰이 없습니다.");
        }

        String token = authHeader.replace("Bearer ", "");
        String userId;

        try {
            userId = jwtUtil.getUserIdFromToken(token); // 만료되었을 경우 여기서 예외 발생
        } catch (io.jsonwebtoken.ExpiredJwtException e) {
            return ResponseEntity.status(401).body("Access Token 만료");
        } catch (Exception e) {
            return ResponseEntity.status(401).body("유효하지 않은 토큰");
        }

        User user = userService.getUserById(userId);
        if (user == null) {
            return ResponseEntity.status(404).body("유저를 찾을 수 없습니다.");
        }

        return ResponseEntity.ok(user);
    }

    // ✅ 유저 정보 수정 (JWT 기반)
    @PutMapping("/update")
    public ResponseEntity<?> updateUser(
            @RequestHeader("Authorization") String authHeader,
            @RequestBody User user) {

        if (authHeader == null || !authHeader.startsWith("Bearer ")) {
            return ResponseEntity.status(401).body("토큰이 없습니다.");
        }

        String token = authHeader.replace("Bearer ", "");
        String userId = jwtUtil.getUserIdFromToken(token);
        user.setUserId(userId); // 보안상 안전하게 서버에서 ID 설정

        userService.update(user);
        return ResponseEntity.ok("회원정보 수정 성공");
    }

    // ✅ 로그아웃
    @DeleteMapping("/logout")
    public ResponseEntity<?> logout(@RequestBody Map<String, String> request) {
        String userId = request.get("userId");
        refreshTokenService.deleteByUserId(userId);
        return ResponseEntity.ok("로그아웃 완료");
    }

    // ✅ 회원가입
    @PostMapping("/regist")
    public ResponseEntity<?> regist(@RequestBody User user) {
        userService.insert(user);
        return ResponseEntity.ok("회원가입 성공");
    }

    // ✅ 회원 탈퇴
    @DeleteMapping("/delete/{userId}")
    public ResponseEntity<?> delete(@PathVariable String userId) {
        userService.delete(userId);
        refreshTokenService.deleteByUserId(userId);
        return ResponseEntity.ok("회원 탈퇴 완료");
    }

    @PutMapping("/password")
    public ResponseEntity<?> updatePassword(@RequestHeader("Authorization") String authHeader,
                                            @RequestBody Map<String, String> request) {
        if (authHeader == null || !authHeader.startsWith("Bearer ")) {
            return ResponseEntity.status(401).body("토큰이 없습니다.");
        }

        String token = authHeader.replace("Bearer ", "");
        String userId;
        try {
            userId = jwtUtil.getUserIdFromToken(token);
        } catch (Exception e) {
            return ResponseEntity.status(401).body("유효하지 않은 토큰");
        }

        String newPassword = request.get("password");
        if (newPassword == null || newPassword.isBlank()) {
            return ResponseEntity.badRequest().body("새 비밀번호가 비어 있습니다.");
        }

        userService.updatePassword(userId, newPassword);
        refreshTokenService.deleteByUserId(userId); // 보안을 위해 강제 로그아웃

        return ResponseEntity.ok("비밀번호가 변경되었습니다. 다시 로그인 해주세요.");
    }

}
