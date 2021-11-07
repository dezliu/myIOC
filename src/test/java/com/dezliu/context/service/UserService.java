package com.dezliu.context.service;

import com.dezliu.annotation.Autowired;
import com.dezliu.annotation.Service;
import com.dezliu.context.dao.User;

@Service
public class UserService {

    @Autowired
    private User user;

    public void test() {
        System.out.print("hi: ");
        System.out.println(user);
    }
}
