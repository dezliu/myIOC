package com.dezliu.context.web;

import com.dezliu.annotation.Autowired;
import com.dezliu.annotation.Controller;
import com.dezliu.context.service.UserService;

@Controller
public class UserController {

    @Autowired
    private UserService userService;

    public void test() {
        userService.test();
    }
}
