package com.gaige.mylibrary;

import android.os.Handler;
import android.os.Looper;

import com.gq.mylib.data.BaseRepository;
import com.gq.mylib.data.CallBack;
import com.gq.mylib.netWork.NetWork;
import com.gq.mylib.utils.GsonUtil;
import com.gq.mylib.utils.LogUtil;
import com.gq.mylib.vp.BasePresenter;

import retrofit2.Call;

/**
 * Created by gaoqun on 2016/7/18.
 */
public class MainPresenter extends BasePresenter<MainMvpView> {

    private Call<ResponseData> mResponseDataCall;
    private BaseRepository mBaseRepository;

    public void onResume() {
        mResponseDataCall = NetWork.createService(ServiceTest.class).fetchBanner();
        mBaseRepository = new BaseRepository();
        /*mBaseRepository.fetchData(mResponseDataCall, new CallBack<ResponseData>() {
            @Override
            public void Success(ResponseData responseData) {
                if (getMvpView() != null) {
                    getMvpView().refresh(GsonUtil.GsonString(responseData));
                }
            }

            @Override
            public <T> void Failed(T t) {

                LogUtil.d("request failed!");
            }
        });*/

        mBaseRepository.fetchDataWithCache(mResponseDataCall, new CallBack<ResponseData>() {
            @Override
            public void Success(final ResponseData responseData) {
                if (Thread.currentThread().getName().equals("main")) {
                    if (getMvpView() != null) {
                        getMvpView().refresh(GsonUtil.GsonString(responseData));
                    }
                } else {
                    new Handler(Looper.getMainLooper()).post(new Runnable() {
                        @Override
                        public void run() {
                            if (getMvpView() != null) {
                                getMvpView().refresh(GsonUtil.GsonString(responseData));
                            }
                        }
                    });
                }
            }

            @Override
            public <T> void Failed(T t) {

                LogUtil.d("request failed!");
            }
        }, "banner", ResponseData.class);


    }

    public void save() {


    }

    public void deleteData() {

    }

    public void cancelRequest() {
        if (mResponseDataCall != null && !mResponseDataCall.isCanceled())
            mResponseDataCall.cancel();
    }


    public void onDestroyed() {
        cancelRequest();
        if (mBaseRepository != null)
            mBaseRepository.removeAllMessages();
    }

}