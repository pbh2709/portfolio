package com.ssafy.pjt.model.service;

import com.ssafy.pjt.model.dao.UserDao;
import com.ssafy.pjt.model.dto.User;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class userServiceImpl implements userService {

    @Autowired
    private UserDao userDao;

    @Override
    public User login(String userId, String password) {
        return userDao.login(userId, password);
    }

    @Override
    public boolean insert(User user) {
        return userDao.insert(user) > 0;
    }

    @Override
    public boolean update(User user) {
        return userDao.update(user) > 0;
    }

    @Override
    public boolean delete(String userId) {
        return userDao.delete(userId) > 0;
    }

    @Override
    public User getUserById(String userId) {
        return userDao.select(userId);
    }

    @Override
    public void updatePassword(String userId, String newPassword) {
        User user = userDao.select(userId);
        if (user != null) {
            user.setPassword(newPassword);
            userDao.updatePassword(user);
        }
    }

}
