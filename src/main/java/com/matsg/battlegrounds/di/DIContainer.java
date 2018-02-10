package com.matsg.battlegrounds.di;

import java.lang.reflect.Constructor;
import java.util.ArrayList;
import java.util.List;

public class DIContainer<T> {

    private Class<? extends T> clazz;

    public DIContainer(Class<? extends T> clazz) {
        this.clazz = clazz;
    }

    public T createInstance(Object... args) {
        try {
            Constructor constructor = clazz.getConstructor(toConstructorParameters(args));
            return (T) constructor.newInstance(args);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    private Class[] toConstructorParameters(Object... args) {
        List<Class> list = new ArrayList<>();
        for (Object object : args) {
            list.add(object.getClass());
        }
        return list.toArray(new Class[list.size()]);
    }
}