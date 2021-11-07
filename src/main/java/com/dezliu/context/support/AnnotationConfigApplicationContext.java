package com.dezliu.context.support;

import com.dezliu.annotation.Autowired;
import com.dezliu.annotation.Component;
import com.dezliu.context.ApplicationContext;
import com.dezliu.util.ClassUtil;
import com.dezliu.util.StringUtil;

import java.util.HashMap;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.stream.Stream;

public class AnnotationConfigApplicationContext implements ApplicationContext {
    private Map<String, Object> singletonFactories = new HashMap<>();

    public AnnotationConfigApplicationContext(Class clazz) {
        this(clazz.getPackage().getName());
    }

    public AnnotationConfigApplicationContext(String packagePath) {
        Set<Class<?>> classSet = ClassUtil.getClassSet(packagePath);

        for (Class<?> clazz : classSet) {
            AtomicBoolean isCreate = new AtomicBoolean(false);
            Stream.of(clazz.getAnnotations()).forEach(anno -> {
                if (anno.annotationType().isAnnotationPresent(Component.class) || anno.annotationType().isAssignableFrom(Component.class)) {
                    isCreate.set(true);
                }
            });
            if (isCreate.get()) {
                String className = clazz.getSimpleName();
                className = StringUtil.toLowerCaseFirstOne(className);
                try {
                    singletonFactories.put(className, clazz.newInstance());
                } catch (InstantiationException | IllegalAccessException e) {
                    e.printStackTrace();
                }
            }
        }

        singletonFactories.forEach((name, obj) -> {
            Stream.of(obj.getClass().getDeclaredFields()).forEach(field -> {
                Autowired annotation = field.getAnnotation(Autowired.class);
                if (annotation != null) {
                    field.setAccessible(true);
                    String fieldName = field.getName();
                    Class<?> fieldType = field.getType();
                    Object o = singletonFactories.get(fieldName);
                    if (o == null) {
                        throw new RuntimeException("Cannot found obj");
                    }
                    if (o.getClass().isAssignableFrom(fieldType)) {
                        try {
                            field.set(obj, o);
                        } catch (IllegalAccessException e) {
                            e.printStackTrace();
                        }
                    }
                }
            });
        });
    }

    @Override
    public Object getBean(String name) {
        return singletonFactories.get(name);
    }

    @Override
    public <T> T getBean(Class<T> clazz) {
        String name = StringUtil.toLowerCaseFirstOne(clazz.getSimpleName());
        return getBean(name, clazz);
    }

    @Override
    public <T> T getBean(String name, Class<T> clazz) {
        Object bean = singletonFactories.get(name);
        return (T) bean;
    }

}
