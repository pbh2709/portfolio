package com.ssafy.pjt.interceptor;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import org.springframework.web.servlet.HandlerInterceptor;

import java.io.PrintWriter;

public class loginInterceptor implements HandlerInterceptor {

	@Override
	public boolean preHandle(HttpServletRequest request,
	                         HttpServletResponse response,
	                         Object handler) throws Exception {

	    HttpSession session = request.getSession(false);

	    System.out.println("[인터셉터] 세션 ID = " + (session != null ? session.getId() : "없음"));
	    System.out.println("[인터셉터] loginUser = " + (session != null ? session.getAttribute("loginUser") : "없음"));
	    System.out.println("[인터셉터] 요청 URI = " + request.getRequestURI());

	    if (session == null || session.getAttribute("loginUser") == null) {
	        response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
	        response.setContentType("application/json;charset=UTF-8");
	        response.getWriter().write("{\"message\":\"로그인이 필요합니다\"}");
	        return false;
	    }

	    return true;
	}
}