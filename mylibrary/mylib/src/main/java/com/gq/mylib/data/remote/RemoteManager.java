package com.gq.mylib.data.remote;

import com.gq.mylib.data.CallBack;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * Created by gaoqun on 2016/7/19.
 * the method can deal data from net
 */
public class RemoteManager extends RemoteCommon {

    public static RemoteManager getInstance() {
        return Instance.instance;
    }

    private static class Instance {
        private static final RemoteManager instance = new RemoteManager();
    }

    public <T> void getRemoteData(Call<T> tCall, final CallBack callBack) {
        if (tCall == null) {
            throw new IllegalArgumentException("call can't be null!");
        } else {
            tCall.enqueue(new Callback<T>() {
                @Override
                public void onResponse(Call<T> call, Response<T> response) {
                    handleSucceed(response, callBack);
                }

                @Override
                public void onFailure(Call<T> call, Throwable t) {
                    handleFailed(t, callBack);
                }
            });
        }
    }


}
