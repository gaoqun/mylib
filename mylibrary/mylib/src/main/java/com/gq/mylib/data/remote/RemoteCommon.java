package com.gq.mylib.data.remote;

import android.text.TextUtils;

import com.gq.mylib.data.CallBack;

import retrofit2.Response;

/**
 * Created by gaoqun on 2016/5/13.
 * 公共数据处理
 */
public class RemoteCommon implements CallBack.ResponseResult {

    @Override
    public <T> void handleSucceed(Response<T> response, CallBack callBack) {

        if (response == null) {
            callBack.Failed("response is null!");
        } else if (response.code() == 200) {
            T t = response.body();
            if (t == null) {
                callBack.Failed("no data!");
            } else {
                callBack.Success(t);
            }
        } else {
            callBack.Failed("error code:" + response.code());
        }
    }

    @Override
    public void handleFailed(Throwable throwable, CallBack callBack) {
        if (throwable != null) {
            String errorMessage = throwable.getMessage();
            if (TextUtils.isEmpty(errorMessage)) {
                callBack.Failed("throwable is null!");
            } else {
                callBack.Failed(throwable.getMessage());
            }
        } else
            callBack.Failed("request failed!");
    }
}
