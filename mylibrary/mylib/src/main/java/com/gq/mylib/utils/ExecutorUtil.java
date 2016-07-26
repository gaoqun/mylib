package com.gq.mylib.utils;

import java.util.concurrent.ExecutorService;

/**
 * Created by gaoqun on 2016/7/26.
 */
public class ExecutorUtil {
    public static ExecutorUtil getInstance() {
        return Instance.instance;
    }

    private static class Instance {
        private static ExecutorUtil instance = new ExecutorUtil();
    }

    public ExecutorService getExecutor() {
        return java.util.concurrent.Executors.newFixedThreadPool(5);
    }
}
