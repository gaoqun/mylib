package com.gaige.mylibrary;

import android.content.DialogInterface;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.text.TextUtils;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.gq.mylib.base.BaseActivity;

public class MainActivity extends BaseActivity<MainMvpView, MainPresenter> implements MainMvpView {

    TextView mHello;

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

//        showProgressDialog("title","message");
//        showProgressDialog("title","message",true);
//        showProgressDialog("title","message",true,true);
        showProgressDialog("title", "message", true, true, new DialogInterface.OnCancelListener() {
            @Override
            public void onCancel(DialogInterface dialogInterface) {
                showSnackBar(mHello, "cancel", null, null);
            }
        });

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
        mHello.setText(msg);
    }

    @Override
    public void notify(String msg) {
        Toast.makeText(App.sContext, !TextUtils.isEmpty(msg) ? msg : "", Toast.LENGTH_SHORT).show();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        presenter.onDestroyed();
    }
}
