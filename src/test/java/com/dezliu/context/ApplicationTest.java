package com.dezliu.context;

import com.dezliu.context.service.UserService;
import com.dezliu.context.support.AnnotationConfigApplicationContext;
import com.dezliu.context.web.UserController;

public class ApplicationTest {

    public static void main(String[] args) {
        ApplicationContext context = new AnnotationConfigApplicationContext(ApplicationTest.class);

        UserController bean = context.getBean(UserController.class);
        bean.test();
        System.out.println(bean);
    }
}
