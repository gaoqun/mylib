package com.gq.mylib.data;

import android.support.annotation.IntDef;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;

/**
 * Created by gaoqun on 2016/8/2.
 */
public class State {

    private static final int OPEN = 0;
    private static final int CLOSE = 1;

    @IntDef({OPEN, CLOSE})

    @Retention(RetentionPolicy.SOURCE)
    public @interface MyState {
    }

    @State.MyState int state = OPEN;

    public void a(){
        switch (state)
        {
            case OPEN:
                break;
        }
    }
}
