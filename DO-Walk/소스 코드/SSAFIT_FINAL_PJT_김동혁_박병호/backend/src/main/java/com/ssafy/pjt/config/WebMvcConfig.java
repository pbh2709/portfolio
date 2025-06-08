package com.ssafy.pjt.config;

import com.ssafy.pjt.interceptor.loginInterceptor;
import com.ssafy.pjt.interceptor.JwtInterceptor; // ✅ 추가
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.*;

@Configuration
public class WebMvcConfig implements WebMvcConfigurer {

    @Override
    public void addInterceptors(InterceptorRegistry registry) {

        // ✅ 1. 세션 기반 로그인 인터셉터 (기존)
        registry.addInterceptor(new loginInterceptor())
                .addPathPatterns("/admin/**", "/user/mypage"); // 예시로 관리자 페이지

        // ✅ 2. JWT 기반 인증 인터셉터 (추가)
        registry.addInterceptor(new JwtInterceptor())
                .addPathPatterns("/video/**", "/review/**", "/favorite/**") // Vue에서 호출할 API
                .excludePathPatterns("/user/login", "/user/regist", "/css/**", "/js/**", "/images/**");

    }

    @Override
    public void addResourceHandlers(ResourceHandlerRegistry registry) {
        registry.addResourceHandler("/static/**")
                .addResourceLocations("classpath:/static/");
    }

    @Override
    public void addCorsMappings(CorsRegistry registry) {
        registry.addMapping("/**")
                .allowedOrigins("http://localhost:5173")
                .allowedMethods("GET", "POST", "PUT", "DELETE", "OPTIONS")
                .allowedHeaders("*")
                .exposedHeaders("Authorization") // ✅ 토큰 응답 허용
                .allowCredentials(true)
                .maxAge(3600); // preflight 캐시 (선택)
    }
}
