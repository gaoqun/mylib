package com.gq.mylib.data.local;

import android.text.TextUtils;

import com.gq.mylib.App;
import com.gq.mylib.utils.DiskCacheUtil;
import com.gq.mylib.utils.ExecutorUtil;
import com.gq.mylib.utils.GsonUtil;

import java.util.concurrent.Callable;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.FutureTask;

/**
 * Created by gaoqun on 2016/7/19.
 * the method deal data with realm
 */
public class LocalManager {

    public static LocalManager getInstance() {
        return Instance.instance;
    }

    private static class Instance {
        private static final LocalManager instance = new LocalManager();
    }

    /**
     * 使用本地文件缓存
     *
     * @param key
     * @param t
     * @param <T>
     */
    public <T> void saveCache(final String key, final T t) {
        if (TextUtils.isEmpty(key) || t == null) {
            throw new IllegalArgumentException("cache key can't be null");
        } else {
            ExecutorUtil.getInstance().getExecutor().execute(new Runnable() {
                @Override
                public void run() {
                    DiskCacheUtil.getDiskCacheUtil(App.sContext).put(key, GsonUtil.GsonString(t));
                }
            });
        }
    }

    /**
     * 获取本地文件缓存
     *
     * @param key
     * @param tClass
     * @param <T>
     * @return
     */
    public <T> T getCache(final String key, final Class<T> tClass) {
        if (TextUtils.isEmpty(key) || tClass == null) {
            throw new IllegalArgumentException("cache key can't be null");
        } else {
            try {
                final FutureTask<T> futureTask = new FutureTask<>(new Callable<T>() {
                    @Override
                    public T call() {
                        String result = DiskCacheUtil.getDiskCacheUtil(App.sContext).getAsString(key);
                        return TextUtils.isEmpty(result) ? null : GsonUtil.GsonToBean(result, tClass);
                    }
                });
                ExecutorUtil.getInstance().getExecutor().submit(futureTask);
                return futureTask.get();
            } catch (InterruptedException e1) {
                return null;
            } catch (ExecutionException e2) {
                return null;
            } catch (Exception e3) {
                return null;
            }
        }
    }

}
