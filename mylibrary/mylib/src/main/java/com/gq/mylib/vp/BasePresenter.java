package com.gq.mylib.vp;

/**
 * Created by gaoqun on 2016/7/18.
 */
public class BasePresenter<T extends Contract.MvpView> implements Contract.Presenter<T> {
    private T mvpView;

    @Override
    public void attachView(T view) {
        this.mvpView = view;
    }

    @Override
    public void detachView() {
        this.mvpView = null;
    }

    public boolean isAttachedView() {
        return mvpView != null;
    }

    public T getMvpView() {
        return mvpView;
    }
}
