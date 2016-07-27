package com.gaige.mylibrary;

import com.gq.mylib.vp.Contract;

/**
 * Created by gaoqun on 2016/7/18.
 */
public interface MainMvpView extends Contract.MvpView {
    void refresh(String msg);
}
