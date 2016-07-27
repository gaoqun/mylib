package com.gaige.mylibrary;

import retrofit2.Call;
import retrofit2.http.GET;

/**
 * Created by gaoqun on 2016/7/27.
 */
public interface ServiceTest {

    //获取banner
    @GET("/api/banner/get")
    Call<ResponseData> fetchBanner();
}
