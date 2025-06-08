package com.ssafy.pjt.interceptor;

import com.ssafy.pjt.auth.JwtUtil;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.web.servlet.HandlerInterceptor;

public class JwtInterceptor implements HandlerInterceptor {

    @Override
    public boolean preHandle(HttpServletRequest request,
                             HttpServletResponse response,
                             Object handler) throws Exception {

        String authHeader = request.getHeader("Authorization");

        if ("OPTIONS".equalsIgnoreCase(request.getMethod())) {
            return true;
        }
        // 토큰이 존재하고 Bearer로 시작하는지 확인
        if (authHeader != null && authHeader.startsWith("Bearer ")) {
            String token = authHeader.substring(7); // "Bearer " 이후 부분 자르기
            String userId = JwtUtil.validateToken(token);

            if (userId != null) {
                request.setAttribute("userId", userId); // 요청 속성에 유저 ID 저장
                return true; // 통과
            }
        }

        // 인증 실패 처리
        response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
        response.setContentType("application/json;charset=UTF-8");
        response.getWriter().write("{\"message\":\"유효하지 않은 토큰입니다.\"}");
        return false;
    }
}
