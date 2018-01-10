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
import android.content.res.TypedArray;
import android.graphics.Color;
import android.support.v7.widget.helper.ItemTouchHelper;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;

import com.liuguangqiang.recyclerview.R;
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
    private OnPageListener onPageListener;

    public SuperRecyclerView(Context context) {
        this(context, null);
    }

    public SuperRecyclerView(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public SuperRecyclerView(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        init();
        loadStyledAttr(context, attrs, defStyle);
    }

    public boolean isLoading() {
        return isLoading;
    }

    private void init() {
        itemTouchHelperCallback = new ItemTouchHelperCallback();
        ItemTouchHelper itemTouchHelper = new ItemTouchHelper(itemTouchHelperCallback);
        itemTouchHelper.attachToRecyclerView(this);
        setOnScrollPositionListener(this);
    }

    private void loadStyledAttr(Context context, AttributeSet attrs, int defStyle) {
        final TypedArray typedArray = context.getTheme().obtainStyledAttributes(attrs, R.styleable.SuperRecyclerView, defStyle, 0);
        boolean swipeEnabled = typedArray.getBoolean(R.styleable.SuperRecyclerView_swipe_enabled, false);
        boolean dragEnabled = typedArray.getBoolean(R.styleable.SuperRecyclerView_drag_enabled, false);
        int loadingFooterResId = typedArray.getResourceId(R.styleable.SuperRecyclerView_loading_footer, 0);
        int selectedColor = typedArray.getColor(R.styleable.SuperRecyclerView_selected_color, Color.LTGRAY);
        typedArray.recycle();

        setSwipeEnabled(swipeEnabled);
        setDragEnabled(dragEnabled);
        setSelectedColor(selectedColor);
        if (loadingFooterResId > 0) {
            setLoadingFooter(loadingFooterResId);
        }
    }

    public void setSwipeEnabled(boolean swipeEnable) {
        if (itemTouchHelperCallback != null) {
            itemTouchHelperCallback.setSwipeEnabled(swipeEnable);
        }
    }

    public void setDragEnabled(boolean dragEnable) {
        if (itemTouchHelperCallback != null) {
            itemTouchHelperCallback.setDragEnabled(dragEnable);
        }
    }

    public void setSelectedColor(int color) {
        itemTouchHelperCallback.setSelectedColor(color);
    }

    public void setOnPageListener(OnPageListener pageListener) {
        this.onPageListener = pageListener;
    }

    @Override
    public void onScrollToTop() {
    }

    @Override
    public void onScrollToBottom() {
        if (onPageListener != null && !isLoading) {
            onLoadStart();
            onPageListener.onPage();
        }
    }

    public void notifyDataSetChanged() {
        bookends.notifyDataSetChanged();
    }

    public void setAdapter(BaseAdapter adapter) {
        adapter.setRecyclerView(this);
        bookends = new Bookends(adapter);
        addLoadingFooter();
        super.setAdapter(bookends);
        itemTouchHelperCallback.setAdapter(adapter);
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
        loadingFooter = view;
    }

    public void setLoadingFooter(int resId) {
        loadingFooter = LayoutInflater.from(getContext()).inflate(resId, null);
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
