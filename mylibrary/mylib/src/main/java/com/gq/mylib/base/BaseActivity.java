package com.gq.mylib.base;

import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
import android.view.View;
import android.widget.Toast;

import com.gq.mylib.utils.LogUtil;
import com.gq.mylib.vp.BasePresenter;
import com.gq.mylib.vp.Contract;

/**
 * Created by gaoqun on 2016/7/18.
 */
public abstract class BaseActivity<V extends Contract.MvpView, T extends BasePresenter<V>> extends AppCompatActivity {
    public T presenter;
    public static Handler sHandler = new Handler(Looper.getMainLooper());
    private ProgressDialog mProgressDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        presenter = initPresenter();
        mProgressDialog = new ProgressDialog(this);
    }

    @Override
    protected void onStop() {
        super.onStop();
    }

    /**
     * 保存永久数据
     */
    @Override
    protected void onPause() {
        super.onPause();
    }

    /**
     * 保存临时数据
     *
     * @param outState
     */
    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
    }

    @Override
    protected void onPostResume() {
        super.onPostResume();
        presenter.attachView((V) this);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        presenter.detachView();
        sHandler.removeCallbacksAndMessages(null);
    }

    public abstract T initPresenter();

    /**
     * show short time toast
     *
     * @param msg
     */
    public void showLongToast(String msg) {
        if (!TextUtils.isEmpty(msg)) {
            Toast.makeText(this, msg, Toast.LENGTH_LONG).show();
        } else {
            LogUtil.d("toast message is null!");
        }
    }

    /**
     * show long time toast
     *
     * @param msg
     */
    public void showShortToast(String msg) {
        if (!TextUtils.isEmpty(msg)) {
            Toast.makeText(this, msg, Toast.LENGTH_LONG).show();
        } else {
            LogUtil.d("toast message is null!");
        }
    }

    /**
     * show snackBar with onclick action
     *
     * @param view
     * @param msg
     * @param actionName
     * @param onClickListener
     */
    public void showSnackBar(View view, String msg, String actionName, View.OnClickListener onClickListener) {
        Snackbar snackbar = Snackbar.make(view, msg, Snackbar.LENGTH_SHORT);
        if (!TextUtils.isEmpty(actionName) && onClickListener != null) {
            snackbar.setAction(actionName, onClickListener);
        }
        if (!snackbar.isShown()) {
            snackbar.show();
        }

    }

    /**
     * show commend dialog
     *
     * @param resLayoutId
     * @param message
     * @param positiveMsg
     * @param positiveListener
     * @param negativeMsg
     * @param negativeListener
     */
    public void showDialog(int resLayoutId, String message, String positiveMsg, AlertDialog.OnClickListener positiveListener, String negativeMsg, AlertDialog.OnClickListener negativeListener) {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        if (resLayoutId != 0) {
            builder.setView(resLayoutId);
        }
        if (!TextUtils.isEmpty(message))
            builder.setMessage(message);
        if (!TextUtils.isEmpty(positiveMsg))
            builder.setPositiveButton(positiveMsg, positiveListener);
        if (!TextUtils.isEmpty(negativeMsg))
            builder.setNegativeButton(negativeMsg, negativeListener);
        builder.show();
    }

    public void cancleProgressDialog() {
        if (mProgressDialog.isShowing()) {
            mProgressDialog.cancel();
        }
    }

    public void showProgressDialog(String msg, DialogInterface.OnCancelListener onCancelListener) {
        if (mProgressDialog == null) {
            mProgressDialog = new ProgressDialog(this);
            mProgressDialog.setCancelable(true);
            if (onCancelListener != null)
                mProgressDialog.setOnCancelListener(onCancelListener);
            mProgressDialog.setIndeterminate(true);
            if (TextUtils.isEmpty(msg))
                ProgressDialog.show(this, "", "正在加载...");
            else
                ProgressDialog.show(this, "", msg);
        }
        if (!mProgressDialog.isShowing())
            mProgressDialog.show();
    }


}
