package com.chasing.fan.common;

import lombok.extern.slf4j.Slf4j;

@Slf4j
public class BaseContext {
    private static final ThreadLocal<Long> threadLocal = new ThreadLocal<>();

    public static void setCurrentId(Long id) {
        log.info("BaseContext setCurrentId {}", id);
        threadLocal.set(id);
    }

    public static Long getCurrentId() {
        log.info("BaseContext getCurrentId {}", threadLocal.get());
        return threadLocal.get();
    }

    public static void close() {
        threadLocal.remove();
    }
}
