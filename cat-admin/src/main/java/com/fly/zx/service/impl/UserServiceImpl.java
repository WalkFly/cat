package com.fly.zx.service.impl;

import com.fly.zx.dao.UserDao;
import com.fly.zx.model.User;
import com.fly.zx.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class UserServiceImpl implements UserService {
    @Autowired
    private UserDao userDao;

    @Override
    public User getById(Long id) {
        return userDao.selectById(id);
    }
}
