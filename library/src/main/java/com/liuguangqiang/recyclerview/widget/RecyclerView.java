package com.liuguangqiang.recyclerview.widget;

import android.content.Context;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;

import com.liuguangqiang.recyclerview.adapter.Bookends;
import com.liuguangqiang.recyclerview.listener.OnPageListener;

/**
 * Created by Eric on 15/7/7.
 */
public class RecyclerView extends LinearRecyclerView implements LinearRecyclerView.OnScrollPositionListener {

    private View loadingFooter;

    private boolean isLoading = false;

    private Bookends bookends;

    private OnPageListener listener;


    public RecyclerView(Context context) {
        super(context);
        init();
    }

    public RecyclerView(Context context, AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    public RecyclerView(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        init();
    }

    private void init() {
        setOnScrollPositionListener(this);
    }

    @Override
    public void onScrollToTop() {
    }

    @Override
    public void onScrollToBottom() {
        if (listener != null && !isLoading) {
            onPageStart();
            listener.onPage();
        }
    }

    public void notifyDataSetChanged() {
        bookends.notifyDataSetChanged();
    }

    @Override
    public void setAdapter(Adapter adapter) {
        bookends = new Bookends(adapter);
        addFooter();
        super.setAdapter(bookends);
    }

    public void onPageStart() {
        isLoading = true;
        showLoadingFooter();
    }

    public void onPageFinished() {
        isLoading = false;
        hideLoadingFooter();
    }

    public void setLoadingFooter(View view) {
        this.loadingFooter = view;
    }

    public void setLoadingFooter(int resId) {
        this.loadingFooter = LayoutInflater.from(getContext()).inflate(resId, null);
    }

    public void setOnPageListener(OnPageListener pageListener) {
        this.listener = pageListener;
    }

    private void addFooter() {
        bookends.addFooter(loadingFooter);
    }

    public void hideLoadingFooter() {
        bookends.setFooterVisibility(false);
    }

    public void showLoadingFooter() {
        bookends.setFooterVisibility(true);
    }

}
