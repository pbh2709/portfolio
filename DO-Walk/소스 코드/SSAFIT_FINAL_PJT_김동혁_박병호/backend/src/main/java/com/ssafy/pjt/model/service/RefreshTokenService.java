package com.ssafy.pjt.model.service;

import com.ssafy.pjt.model.dto.RefreshToken;

public interface RefreshTokenService {

    void saveOrUpdate(String userId, String token);

    String getTokenByUserId(String userId);

    void deleteByUserId(String userId);
}
