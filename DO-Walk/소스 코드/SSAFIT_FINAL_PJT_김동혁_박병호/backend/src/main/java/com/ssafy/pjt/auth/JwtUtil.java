package com.ssafy.pjt.auth;

import io.jsonwebtoken.*;
import org.springframework.stereotype.Component;

import java.util.Date;
@Component
public class JwtUtil {

    private static final String SECRET_KEY = "your-secret-key"; // 바꾸지 않았다면 그대로

    // ✅ Access Token 생성 (15분 예시)
    public static String generateToken(String userId) {
        return Jwts.builder()
                .setSubject(userId)
                .setIssuedAt(new Date())
                .setExpiration(new Date(System.currentTimeMillis() + 1000 * 60 * 15)) // 15분
                .signWith(SignatureAlgorithm.HS256, SECRET_KEY)
                .compact();
    }

    // ✅ Refresh Token 생성 (7일 예시)
    public static String generateRefreshToken(String userId) {
        return Jwts.builder()
                .setSubject(userId)
                .setIssuedAt(new Date())
                .setExpiration(new Date(System.currentTimeMillis() + 1000L * 60 * 60 * 24 * 7)) // 7일
                .signWith(SignatureAlgorithm.HS256, SECRET_KEY)
                .compact();
    }

    // ✅ 토큰에서 userId 추출 및 유효성 검증
    public static String validateToken(String token) {
        return Jwts.parser()
                .setSigningKey(SECRET_KEY)
                .parseClaimsJws(token)
                .getBody()
                .getSubject(); // userId 반환
    }
    public String getUserIdFromToken(String token) {
        return validateToken(token); // 내부 재사용
    }
}
