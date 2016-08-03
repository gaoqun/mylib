package com.gaige.mylibrary;

import android.content.DialogInterface;
import android.content.res.Resources;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.os.HandlerThread;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v4.graphics.drawable.DrawableCompat;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.gq.mylib.base.BaseActivity;
import com.gq.mylib.utils.LogUtil;

import java.util.Calendar;

public class MainActivity extends BaseActivity<MainMvpView, MainPresenter> implements MainMvpView {

    TextView mHello;
    HandlerThread mHandlerThread;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        mHello = (TextView) findViewById(R.id.hello);
        mHello.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                presenter.deleteData();
            }
        });

        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
                        .setAction("Action", new View.OnClickListener() {
                            @Override
                            public void onClick(View view) {
                                presenter.save();
                            }
                        }).show();
            }
        });

        showProgressDialog(null, new DialogInterface.OnCancelListener() {
            @Override
            public void onCancel(DialogInterface dialogInterface) {
                if (presenter != null) presenter.cancelRequest();
            }
        });

//        String text = getString(R.string.comeback,"zhangsan");
//        mHello.setText(text);
        int minutes = Calendar.getInstance().get(Calendar.MINUTE);
        try {
            String minutes1 = getResources().getQuantityString(R.plurals.minutes,minutes);
            mHello.setText(minutes1);
        }catch (Resources.NotFoundException e)
        {
            LogUtil.d("not found!");
        }


        final Drawable tintDrawables = getResources().getDrawable(R.mipmap.ic_launcher).mutate();
        ImageView imageView = (ImageView) findViewById(R.id.image);
        final Drawable tintdrawabletemp = DrawableCompat.wrap(tintDrawables);
        DrawableCompat.setTintList(tintdrawabletemp,getResources().getColorStateList(R.color.select_color));
        imageView.setImageDrawable(tintdrawabletemp);
        imageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

            }
        });
        for (int i=0;i<100;i++){
            getAsyncThread().post(new Runnable() {
                @Override
                public void run() {
                    a();
                }
            });
        }
    }


    private void a(){
        LogUtil.d("线程执行："+Thread.currentThread().getName());
    }

    @Override
    protected void onPostResume() {
        super.onPostResume();
        presenter.onResume();
    }

    @Override
    public MainPresenter initPresenter() {
        return new MainPresenter();
    }

    @Override
    public void refresh(String msg) {
        showShortToast("refresh new data!");
        mHello.setText(msg);
        cancleProgressDialog();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        presenter.onDestroyed();
    }
}
