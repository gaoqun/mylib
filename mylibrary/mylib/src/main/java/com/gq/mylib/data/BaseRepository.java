package com.gq.mylib.data;

import android.os.Handler;
import android.os.Looper;

import com.gq.mylib.data.local.LocalManager;
import com.gq.mylib.data.remote.RemoteManager;

import retrofit2.Call;

/**
 * Created by gaoqun on 2016/7/25.
 * 可设置是否缓存
 * 设置缓存方式 1，数据库 2，本地文件缓存 3，网络框架缓存
 */
public class BaseRepository {

    //本地缓存
    public static final int LOCALFILECACHE = 0;
    //网络缓存
    public static final int NETCACHE = 1;
    //本地数据库缓存
    public static final int DATABASECACHE = 2;

    private boolean isCache;
    private int cacheType;

    private static Handler sHandler = new Handler(Looper.getMainLooper());

    private BaseRepository(boolean isCache, int cacheType) {
        this.isCache = isCache;
        this.cacheType = cacheType;
    }

    public <T> void fetchData(Call<T> call, CallBack callBack, final String key, final Class<T> tClass) {
        RemoteManager remoteManager = RemoteManager.getInstance();
        final LocalManager localManager = LocalManager.getInstance();
        if (isCache) {
            //缓存
            switch (cacheType) {
                case LOCALFILECACHE:
                    sHandler.post(new Runnable() {
                        @Override
                        public void run() {
                            T t = localManager.getCache(key,tClass);
                            sHandler.sendEmptyMessage(0);

                        }
                    });

                    break;
                case NETCACHE:
                    break;
                case DATABASECACHE:
                    break;
            }
        } else {
            //不缓存
            remoteManager.getRemoteData(call, callBack);
        }
    }

    public static final class Builder {
        private boolean isCache = false;
        private int cacheType = LOCALFILECACHE;

        //设置是否缓存
        public Builder isCache(boolean cache) {
            isCache = cache;
            return this;
        }

        //设置缓存方式
        public Builder cacheType(int cacheType) {
            this.cacheType = cacheType;
            return this;
        }

        public BaseRepository build() {
            return new BaseRepository(this.isCache, this.cacheType);
        }
    }

}
