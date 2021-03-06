package com.fly.zx.controller;

import com.fly.zx.service.UserService;
import io.swagger.annotations.Api;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

@Api("用户管理类")
@RestController
public class UserController {

    @Autowired
    private UserService userService;

    @RequestMapping(value = "user/{id}" , method = RequestMethod.GET)
    public Object getUserById(@PathVariable(value = "id") Long id){
        return userService.getById(1l);
    }
}
