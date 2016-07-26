package com.gq.mylib.vp;

/**
 * Created by gaoqun on 2016/7/18.
 */
public interface Contract {

    interface Presenter<V extends MvpView> {
        void attachView(V view);

        void detachView();
    }

    interface MvpView {
    }

}
