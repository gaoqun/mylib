package com.gq.mylib.data;

import retrofit2.Response;

/**
 * Created by gaoqun on 2016/7/19.
 */
public interface CallBack {

    <T> void Success(T t);

    <T> void Failed(T t);

    interface ResponseResult {
        <T>void handleSucceed(Response<T> response, CallBack callBack);

        void handleFailed(Throwable throwable, CallBack callBack);
    }
}
