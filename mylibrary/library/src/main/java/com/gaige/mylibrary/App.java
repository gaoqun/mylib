package com.gaige.mylibrary;

import com.gq.mylib.netWork.NetWork;

import retrofit2.converter.gson.GsonConverterFactory;

/**
 * Created by gaoqun on 2016/7/20.
 */
public class App extends com.gq.mylib.App {

    @Override
    public void onCreate() {
        super.onCreate();
        new NetWork
                .Builder()
                .setBaseUrl("http://watermelon.wiseweb.com.cn")
                .addConverterFactory(GsonConverterFactory.create())
                .build();
    }
}
