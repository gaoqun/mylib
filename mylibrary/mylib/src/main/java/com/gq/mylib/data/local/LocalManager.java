package com.gq.mylib.data.local;

import android.text.TextUtils;

import com.gq.mylib.App;
import com.gq.mylib.data.CallBack;
import com.gq.mylib.utils.DiskCacheUtil;
import com.gq.mylib.utils.GsonUtil;

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
            DiskCacheUtil.getDiskCacheUtil(App.sContext).put(key, GsonUtil.GsonString(t));
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
    public <T> void getCache(final String key, final Class<T> tClass, final CallBack callBack) {
        if (TextUtils.isEmpty(key) || tClass == null) {
            throw new IllegalArgumentException("cache key can't be null");
        } else {
            String result = DiskCacheUtil.getDiskCacheUtil(App.sContext).getAsString(key);
            if (!TextUtils.isEmpty(result)) {
                T t = GsonUtil.GsonToBean(result, tClass);
                callBack.Success(t);
            }
        }
    }

}
