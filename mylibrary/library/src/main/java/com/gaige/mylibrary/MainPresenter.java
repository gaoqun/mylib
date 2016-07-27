package com.gaige.mylibrary;

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
        mBaseRepository.fetchData(mResponseDataCall, new CallBack<ResponseData>() {
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
        });
        /*mResponseDataCall = NetWork.createService(ServiceTest.class).fetchBanner();
        mBaseRepository = new BaseRepository();
        mBaseRepository.fetchDataWithCache(mResponseDataCall, new CallBack<ResponseData>() {
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
        }, "banner", ResponseData.class);*/


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