package com.gq.mylib.base;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;

import java.util.List;

public abstract class BaseListViewAdapter<T> extends BaseAdapter {

    protected Context mContext;
    protected List<T> mDatas;
    protected final int mItemLayoutId;

    public BaseListViewAdapter(Context context, List<T> mDatas, int itemLayoutId) {
        this.mContext = context;
        this.mDatas = mDatas;
        this.mItemLayoutId = itemLayoutId;
    }

    @Override
    public int getCount() {
        return mDatas.size();
    }

    @Override
    public T getItem(int position) {
        return mDatas.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(final int position, View convertView, ViewGroup parent) {
        final ViewHolder viewHolder;

        viewHolder = getViewHolder(position, convertView, parent);

        convert(viewHolder, getItem(position));

        return viewHolder.getConvertView();

    }

    public abstract void convert(ViewHolder helper, T item);

    public ViewHolder getViewHolder(int position, View convertView,
                                    ViewGroup parent) {
        return ViewHolder.get(mContext, convertView, parent, mItemLayoutId,
                position);
    }

}
