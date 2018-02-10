package com.matsg.battlegrounds.di;

import java.util.HashMap;
import java.util.Map;

public class DIFactory {

    private static Map<Class, DIContainer> containers = new HashMap<>();

    public static Object createInstance(Class clazz, Object... args) {
        if (!containers.containsKey(clazz)) {
            return null;
        }
        return containers.get(clazz).createInstance(args);
    }

    public static void registerContainer(Class clazz, DIContainer<?> container) {
        if (containers.containsKey(clazz)) {
            return;
        }
        containers.put(clazz, container);
    }
}