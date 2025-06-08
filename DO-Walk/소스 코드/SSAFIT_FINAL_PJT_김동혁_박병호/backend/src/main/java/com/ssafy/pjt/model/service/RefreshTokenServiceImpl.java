package com.ssafy.pjt.model.service;

import com.ssafy.pjt.model.dao.RefreshTokenDao;
import com.ssafy.pjt.model.dto.RefreshToken;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;

@Service
public class RefreshTokenServiceImpl implements RefreshTokenService {

    @Autowired
    private RefreshTokenDao refreshTokenDao;

    @Override
    public void saveOrUpdate(String userId, String token) {
        RefreshToken rt = new RefreshToken(userId, token, LocalDateTime.now().plusDays(7));
        refreshTokenDao.saveOrUpdate(rt);
    }

    @Override
    public String getTokenByUserId(String userId) {
        return refreshTokenDao.findByUserId(userId);
    }

    @Override
    public void deleteByUserId(String userId) {
        refreshTokenDao.deleteByUserId(userId);
    }
}
