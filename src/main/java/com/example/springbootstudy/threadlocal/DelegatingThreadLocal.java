package com.example.springbootstudy.threadlocal;

import org.springframework.util.CollectionUtils;

import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

/**
 * @author MCW 2024/2/22
 */
public class DelegatingThreadLocal<T> extends ThreadLocal<T> {
    private static ThreadLocal<Map<DelegatingThreadLocal<Object>, Object>> holder;

    static {
        holder = new ThreadLocal<>();
        holder.set(new HashMap<>());
    }

    @Override
    public T get() {
        return (T) holder.get().get(this);
    }

    @Override
    public void set(T value) {
        holder.get().put((DelegatingThreadLocal<Object>) this, value);
    }


    public static Map<DelegatingThreadLocal<Object>, Object> copyForm() {
        Map<DelegatingThreadLocal<Object>, Object> contextMap = holder.get();
        Map<DelegatingThreadLocal<Object>, Object> snapshot = new HashMap<>();
        snapshot.putAll(contextMap);
        return snapshot;
    }

    public static void copyTo(Map<DelegatingThreadLocal<Object>, Object> snapshot) {
        Map<DelegatingThreadLocal<Object>, Object> contextMap = holder.get();

        if (contextMap == null || CollectionUtils.isEmpty(contextMap.entrySet())) {
            holder.set(snapshot);
            return;
        }

        snapshot.forEach((key, value) -> {
            if (contextMap.containsKey(key)) {
                contextMap.put(key, value);
            }
        });
    }

}
