package com.ssafy.pjt.model.service;

import com.ssafy.pjt.model.dto.User;

public interface userService {
    User login(String userId, String password); // 로그인
    boolean insert(User user);                  // 회원가입
    boolean update(User user);                  // 수정
    boolean delete(String userId);
    User getUserById(String userId);// 탈퇴
    void updatePassword(String userId, String newPassword);
}