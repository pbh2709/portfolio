package com.ssafy.pjt.model.dao;

import org.apache.ibatis.annotations.Mapper;
import com.ssafy.pjt.model.dto.RefreshToken;

@Mapper
public interface RefreshTokenDao {

    void saveOrUpdate(RefreshToken refreshToken);

    String findByUserId(String userId);

    void deleteByUserId(String userId);
}
