package com.gq.mylib.data;

import android.os.Handler;
import android.os.Looper;

import com.gq.mylib.data.local.LocalManager;
import com.gq.mylib.data.remote.RemoteManager;

import retrofit2.Call;

/**
 * Created by gaoqun on 2016/7/25.
 * 可设置是否缓存
 * 设置缓存方式 本地文件缓存
 */
public class BaseRepository {

    private boolean isCache;
    private static Handler sHandler = new Handler(Looper.getMainLooper());

    public <T> void fetchDataWithCache(final Call<T> call, final CallBack callBack, final String key, final Class<T> tClass) {
        final RemoteManager remoteManager = RemoteManager.getInstance();
        final LocalManager localManager = LocalManager.getInstance();
        //缓存
        sHandler.post(new Runnable() {
            @Override
            public void run() {
                T t = localManager.getCache(key, tClass);
                if (t == null) {
                    callBack.Failed("no data!");
                } else {
                    callBack.Success(t);
                }
                remoteManager.getRemoteDataWithCache(call, callBack, true, key);
            }
        });
    }

    public <T> void fetchData(final Call<T> call, final CallBack callBack) {
        final RemoteManager remoteManager = RemoteManager.getInstance();
        //不缓存
        remoteManager.getRemoteData(call, callBack);
    }

    public void removeAllMessages(){
        sHandler.removeCallbacksAndMessages(null);
    }

}
