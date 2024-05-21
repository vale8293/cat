package org.acitech.cat.utils;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

public class Broadcast {

    private static final Map<String, Set<Method>> eventHandlers = new HashMap<>();

    public static void registerClass(Class<?> listener) {
        for (Method method : listener.getDeclaredMethods()) {
            if (!method.isAnnotationPresent(EventHandler.class)) continue;

            EventHandler annotation = method.getAnnotation(EventHandler.class);
            String eventName = annotation.eventName();

            Set<Method> events = eventHandlers.getOrDefault(eventName, new HashSet<>());
            events.add(method);
            eventHandlers.put(eventName, events);
        }
    }

    public static void emit(String eventName, Object... args) {
        Set<Method> handlers = eventHandlers.get(eventName);

        if (handlers == null) return;

        for (Method method : handlers) {
            if (method == null) continue;

            try {
                method.invoke(null, args);
            } catch (NullPointerException e) {
                System.err.println("Perhaps the EventHandler method \"" + method.getName() + "\" should be made static");
                e.printStackTrace();
            } catch (IllegalArgumentException e) {
                System.err.println("Perhaps the EventHandler method \"" + method.getName() + "\" does not have matching arguments for the event \"" + eventName + "\"");
                e.printStackTrace();
            } catch (IllegalAccessException e) {
                System.err.println("Perhaps the EventHandler method \"" + method.getName() + "\" should be made public");
                e.printStackTrace();
            } catch (InvocationTargetException e) {
                e.printStackTrace();
            }
        }
    }

}
