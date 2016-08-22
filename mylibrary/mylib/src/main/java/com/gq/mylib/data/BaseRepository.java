package com.gq.mylib.data;

import android.os.Build;
import android.os.Handler;
import android.os.HandlerThread;

import com.gq.mylib.data.local.LocalManager;
import com.gq.mylib.data.remote.RemoteManager;

import retrofit2.Call;

/**
 * Created by gaoqun on 2016/7/25.
 * 可设置是否缓存
 * 设置缓存方式 本地文件缓存
 */
public class BaseRepository {
    private HandlerThread mHandlerThread;

    public <T> void fetchDataWithCache(final Call<T> call, final CallBack callBack, final String key, final Class<T> tClass) {
        mHandlerThread = new HandlerThread(this.getClass().getName() + "_asyncThread");
        mHandlerThread.start();
        final RemoteManager remoteManager = RemoteManager.getInstance();
        final LocalManager localManager = LocalManager.getInstance();
        //缓存
        new Handler(mHandlerThread.getLooper()).post(new Runnable() {
            @Override
            public void run() {
                localManager.getCache(key, tClass, new CallBack() {
                    @Override
                    public void Success(Object o) {
                        callBack.Success(o);
                    }

                    @Override
                    public void Failed(Object o) {
                        callBack.Failed("no data!");

                    }
                });
                remoteManager.getRemoteDataWithCache(call, callBack, true, key);
            }
        });
    }

    public <T> void fetchData(final Call<T> call, final CallBack callBack) {
        final RemoteManager remoteManager = RemoteManager.getInstance();
        //不缓存
        remoteManager.getRemoteData(call, callBack);
    }

    public void removeAllMessages() {
        if (mHandlerThread!=null){
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN_MR2) {
                mHandlerThread.quitSafely();
            } else mHandlerThread.quit();
        }
    }

}
