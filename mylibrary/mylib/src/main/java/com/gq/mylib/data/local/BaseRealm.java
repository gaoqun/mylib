package com.gq.mylib.data.local;

import android.content.Context;

import com.gq.mylib.data.CallBack;

import io.realm.Realm;
import io.realm.RealmAsyncTask;
import io.realm.RealmConfiguration;
import io.realm.RealmModel;
import io.realm.RealmQuery;
import io.realm.RealmResults;
import io.realm.Sort;

/**
 * Created by gaoqun on 2016/7/22.
 */
public class BaseRealm {
    private RealmConfiguration mRealmConfiguration;
    private Realm mRealm;

    public Realm getRealm() {
        return mRealm;
    }

    public BaseRealm(Context context, RealmConfiguration realmConfiguration) {
        mRealmConfiguration = realmConfiguration;
        Realm.deleteRealm(mRealmConfiguration);
    }

    public BaseRealm open() {
        mRealm = Realm.getInstance(mRealmConfiguration);
        return this;
    }

    public void close() {
        if (!mRealm.isClosed()) mRealm.close();
    }


    /**
     * @param tClass   操作库类型
     * @param callBack 回调
     * @param <T>
     * @return the method can save data with realm&T and the method can invoked in async
     */
    public <T extends RealmModel> RealmAsyncTask save(final Realm realm, final Class<T> tClass, final CallBack callBack, final Save<T> save) {

        return realm.executeTransactionAsync(new Realm.Transaction() {
            @Override
            public void execute(Realm bgRealm) {
                T t = bgRealm.createObject(tClass);
                //you can modify the table
                save.save(t);
            }
        }, new Realm.Transaction.OnSuccess() {
            @Override
            public void onSuccess() {
                callBack.Success("success");
            }
        }, new Realm.Transaction.OnError() {
            @Override
            public void onError(Throwable error) {
                callBack.Failed(error);
            }
        });
    }


    /**
     * 按条件查询
     *
     * @param query
     * @return result
     */
    public RealmResults quaryWithConditions(Query query) {
        return query.doQuery().findAllAsync();
    }

    /**
     * 排序
     *
     * @param realmResults
     * @param sort
     * @param condition
     * @return
     */
    public RealmResults sortResult(RealmResults realmResults, Sort sort, String condition) {
        return realmResults.sort(condition, sort);
    }

    /**
     * 根据类型查询
     *
     * @param tClass
     * @param <T>
     * @return
     */
    public <T extends RealmModel> RealmResults queryWithClassType(Realm realm,Class<T> tClass) {
        return realm.where(tClass).findAll();
    }

    /**
     * 删除数据
     *
     * @param delete
     */
    public void delete(Realm realm,final Delete delete) {
        realm.executeTransaction(new Realm.Transaction() {
            @Override
            public void execute(Realm realm) {
                delete.delete();
            }
        });
    }

    public interface Query {
        RealmQuery doQuery();
    }

    public interface Save<T>{
        void save(T t);
    }

    public interface Delete{
        void delete();
    }

}
