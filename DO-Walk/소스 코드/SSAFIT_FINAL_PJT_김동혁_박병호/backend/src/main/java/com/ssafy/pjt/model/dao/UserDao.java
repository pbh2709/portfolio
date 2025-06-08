package com.ssafy.pjt.model.dao;

import org.apache.ibatis.annotations.Mapper;
import com.ssafy.pjt.model.dto.User;
import org.apache.ibatis.annotations.Param;

@Mapper
public interface UserDao {
    User login(String userId, String password);
    int insert(User user);
    int update(User user);
    int delete(String userId);
    User select(String userId);
    void updatePassword(User user);
}
