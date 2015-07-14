/*
 * Copyright 2015 Eric Liu
 *
 *  Licensed under the Apache License, Version 2.0 (the "License");
 *  you may not use this file except in compliance with the License.
 *  You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 *  Unless required by applicable law or agreed to in writing, software
 *  distributed under the License is distributed on an "AS IS" BASIS,
 *  WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 *  See the License for the specific language governing permissions and
 *  limitations under the License.
 */

package com.liuguangqiang.recyclerview.widget;

import android.content.Context;
import android.support.v7.widget.helper.ItemTouchHelper;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;

import com.liuguangqiang.recyclerview.adapter.BaseAdapter;
import com.liuguangqiang.recyclerview.adapter.Bookends;
import com.liuguangqiang.recyclerview.listener.OnPageListener;
import com.liuguangqiang.recyclerview.utils.ItemTouchHelperCallback;

/**
 * Created by Eric on 15/7/7.
 */
public class SuperRecyclerView extends LinearRecyclerView implements LinearRecyclerView.OnScrollPositionListener {

    private View loadingFooter;

    private boolean isLoading = false;
    private ItemTouchHelperCallback itemTouchHelperCallback;
    private Bookends bookends;
    private BaseAdapter adapter;
    private OnPageListener listener;

    public SuperRecyclerView(Context context) {
        super(context);
        init();
    }

    public SuperRecyclerView(Context context, AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    public SuperRecyclerView(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        init();
    }

    private void init() {
        setOnScrollPositionListener(this);
    }

    public void setSwipeEnable(boolean swipeEnable) {
        if (this.itemTouchHelperCallback != null) {
            itemTouchHelperCallback.setSwipeEnable(swipeEnable);
        }
    }

    public void setDragEnable(boolean dragEnable) {
        if (this.itemTouchHelperCallback != null) {
            itemTouchHelperCallback.setDragEnable(dragEnable);
        }
    }

    public void setOnPageListener(OnPageListener pageListener) {
        this.listener = pageListener;
    }

    @Override
    public void onScrollToTop() {
    }

    @Override
    public void onScrollToBottom() {
        if (listener != null && !isLoading) {
            onLoadStart();
            listener.onPage();
        }
    }

    public void notifyDataSetChanged() {
        bookends.notifyDataSetChanged();
    }

    public void setAdapter(BaseAdapter adapter) {
        this.adapter = adapter;
        adapter.setRecyclerView(this);
        bookends = new Bookends(adapter);
        addLoadingFooter();
        super.setAdapter(bookends);

        itemTouchHelperCallback = new ItemTouchHelperCallback(adapter);
        ItemTouchHelper itemTouchHelper = new ItemTouchHelper(itemTouchHelperCallback);
        itemTouchHelper.attachToRecyclerView(this);
    }


    public Bookends<?> getBookendsAdapter() {
        return bookends;
    }

    public void onLoadStart() {
        isLoading = true;
        showLoadingFooter();
    }

    public void onLoadFinish() {
        isLoading = false;
        hideLoadingFooter();
    }

    /**
     * Add a header view.
     *
     * @param view
     */
    public void addHeader(View view) {
        bookends.addHeader(view);
    }

    /**
     * Add a footer view.
     *
     * @param view
     */
    public void addFooter(View view) {
        bookends.addFooter(view);
    }

    public void setLoadingFooter(View view) {
        this.loadingFooter = view;
    }

    public void setLoadingFooter(int resId) {
        this.loadingFooter = LayoutInflater.from(getContext()).inflate(resId, null);
    }

    private void addLoadingFooter() {
        if (loadingFooter != null)
            bookends.addFooter(loadingFooter);
    }

    public void hideLoadingFooter() {
        bookends.setFooterVisibility(loadingFooter, false);
    }

    public void showLoadingFooter() {
        bookends.setFooterVisibility(loadingFooter, true);
    }

}
