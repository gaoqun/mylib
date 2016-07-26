package com.gaige.mylibrary;

import io.realm.RealmObject;

/**
 * Created by gaoqun on 2016/7/22.
 */
public class Test extends RealmObject{

    private String mString;

    public String getString() {
        return mString;
    }

    public void setString(String string) {
        mString = string;
    }
}
