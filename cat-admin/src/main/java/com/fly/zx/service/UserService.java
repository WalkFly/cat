package com.fly.zx.service;

import com.fly.zx.model.User;

public interface UserService {

    /**
     * 根据id获取用户
     * @param id
     * @return
     */
    User getById(Long id);
}
