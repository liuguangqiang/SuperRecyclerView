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

package com.liuguangqiang.recyclerview;

import android.content.Context;
import android.support.annotation.ColorRes;
import android.support.annotation.LayoutRes;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.annotation.StringRes;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.RecyclerView.ItemDecoration;
import android.text.TextUtils;
import android.util.AttributeSet;
import android.view.View;
import android.view.ViewTreeObserver;
import android.widget.RelativeLayout;
import android.widget.TextView;

/**
 * A extension of RecyclerView that implemented paging and pulling to refresh.
 * <p>
 * Created by Eric on 2017/7/6.
 */
public class SuperRecyclerView extends RelativeLayout implements
    SwipeRefreshLayout.OnRefreshListener,
    OnPageListener {

  private SwipeRefreshLayout refreshLayout;
  private PageableRecyclerView recyclerView;
  private TextView tvEmpty;

  private SwipeRefreshLayout.OnRefreshListener onRefreshListener;
  private OnPageListener onPageListener;

  private boolean pageable = true;
  private boolean refreshable = true;
  private boolean autoRefresh = false;
  private boolean isLoading = false;
  private String emptyText;

  public SuperRecyclerView(@NonNull final Context context) {
    this(context, null);
  }

  public SuperRecyclerView(@NonNull final Context context, @Nullable final AttributeSet attrs) {
    this(context, attrs, 0);
  }

  public SuperRecyclerView(@NonNull final Context context, @Nullable final AttributeSet attrs,
      final int defStyleAttr) {
    super(context, attrs, defStyleAttr);
    initViews();
  }

  private void initViews() {
    inflate(getContext(), R.layout.layout_super_recyclerview, this);
    tvEmpty = findViewById(R.id.tv_empty);
    refreshLayout = findViewById(R.id.refreshLayout);
    refreshLayout.setOnRefreshListener(this);
    refreshLayout.setEnabled(refreshable);
    recyclerView = findViewById(R.id.recyclerView);
    recyclerView.setPageFooter(R.layout.footer_loading);
    recyclerView.setOnPageListener(this);
  }

  public void setGridStyle(int spanCount) {
    recyclerView.setGridStyle(spanCount);
  }

  public void setColorSchemeResources(@ColorRes int... colorResIds) {
    refreshLayout.setColorSchemeResources(colorResIds);
  }

  public void setPageFooter(@LayoutRes int resource) {
    recyclerView.setPageFooter(resource);
  }

  public void setPageable(boolean pageable) {
    this.pageable = pageable;
  }

  public void setRefreshable(boolean refreshable) {
    this.refreshable = refreshable;
    refreshLayout.setEnabled(refreshable);
  }

  public void setRefreshing(boolean refreshing) {
    refreshLayout.setRefreshing(refreshing);
  }

  public void setAutoRefresh(boolean autoRefresh) {
    this.autoRefresh = autoRefresh;
    if (autoRefresh) {
      refreshLayout.getViewTreeObserver()
          .addOnPreDrawListener(new ViewTreeObserver.OnPreDrawListener() {
            @Override
            public boolean onPreDraw() {
              refreshLayout.getViewTreeObserver().removeOnPreDrawListener(this);
              refreshLayout.setRefreshing(true);
              return true;
            }
          });
    } else {
      refreshLayout.setRefreshing(false);
    }
  }

  public void setEmptyText(@StringRes int resId) {
    this.emptyText = getResources().getString(resId);
    tvEmpty.setText(resId);
  }

  public void setEmptyText(String text) {
    this.emptyText = text;
    tvEmpty.setText(text);
  }

  private RecyclerView.Adapter adapter;

  public void setAdapter(RecyclerView.Adapter adapter) {
    this.adapter = adapter;
    recyclerView.setAdapter(adapter);
  }

  public void setOnRefreshListener(SwipeRefreshLayout.OnRefreshListener onRefreshListener) {
    this.onRefreshListener = onRefreshListener;
  }

  public void setOnPageListener(OnPageListener onPageListener) {
    this.onPageListener = onPageListener;
  }

  @Override
  public void onRefresh() {
    if (onRefreshListener != null && refreshable && !isLoading) {
      onRefreshListener.onRefresh();
    }
  }

  @Override
  public void onPage() {
    if (onPageListener != null && pageable && !isLoading) {
      onPageListener.onPage();
    }
  }

  public boolean isLoading() {
    return isLoading;
  }

  public void startLoading() {
    isLoading = true;
    recyclerView.setIsLoading(true);
  }

  protected void onLoadingFinished() {
    refreshLayout.setRefreshing(false);
    isLoading = false;
    recyclerView.setIsLoading(false);
    chkEmpty();
  }

  public void notifyDataSetChanged() {
    notifyDataSetChanged(false);
  }

  public void notifyDataSetChanged(boolean pageFinished) {
    if (pageable) {
      if (!pageFinished) {
        recyclerView.setPageEnable(true);
        recyclerView.showLoadingFooter();
      } else {
        recyclerView.setPageEnable(false);
        recyclerView.removePageFooter();
      }
    }

    onLoadingFinished();
    recyclerView.notifyDataSetChanged();
  }

  private void chkEmpty() {
    if (adapter.getItemCount() == 0 && !TextUtils.isEmpty(emptyText)) {
      tvEmpty.setVisibility(VISIBLE);
    } else {
      tvEmpty.setVisibility(GONE);
    }
  }

  public void addHeader(View view) {
    recyclerView.addHeader(view);
  }

  public void removeHeader(View view) {
    recyclerView.removeHeader(view);
  }

  public void addFooter(View view) {
    recyclerView.addFooter(view);
  }

  public void removeFooter(View view) {
    recyclerView.removeFooter(view);
  }

  /**
   * Just only the BaseAdapter support this listener.
   *
   * @see BaseAdapter
   */
  public void setOnItemClickListener(BaseAdapter.OnItemClickListener onItemClickListener) {
    if (adapter instanceof BaseAdapter) {
      ((BaseAdapter) adapter).setOnItemClickListener(onItemClickListener);
    }
  }

  /**
   * Just only the BaseAdapter support this listener.
   *
   * @see BaseAdapter
   */
  public void setOnItemLongClickListener(
      BaseAdapter.OnItemLongClickListener onItemLongClickListener) {
    if (adapter instanceof BaseAdapter) {
      ((BaseAdapter) adapter).setOnItemLongClickListener(onItemLongClickListener);
    }
  }

  public void scrollToPosition(int position) {
    recyclerView.scrollToPosition(position);
  }

  public void addItemDecoration(ItemDecoration decor, int index) {
    recyclerView.addItemDecoration(decor, index);
  }

  public void addItemDecoration(ItemDecoration decor) {
    recyclerView.addItemDecoration(decor);
  }
}
