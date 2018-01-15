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
import android.support.v7.widget.RecyclerView;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;

/**
 * Created by Eric on 2017/7/6.
 */
public class PageableRecyclerView extends LinearRecyclerView implements
    LinearRecyclerView.OnScrollPositionListener {

  private View loadingFooter;

  private boolean hasAttachedFooter = false;
  private boolean pageEnable = true;
  private boolean isLoading = false;
  private Bookends bookends;
  private OnPageListener onPageListener;
  private OnTopPageListener onTopPageListener;

  public PageableRecyclerView(Context context) {
    this(context, null);
  }

  public PageableRecyclerView(Context context, AttributeSet attrs) {
    this(context, attrs, 0);
  }

  public PageableRecyclerView(Context context, AttributeSet attrs, int defStyle) {
    super(context, attrs, defStyle);
    init();
  }

  public void setPageEnable(boolean pageEnable) {
    this.pageEnable = pageEnable;
  }

  public boolean isLoading() {
    return isLoading;
  }

  public void setIsLoading(boolean b) {
    isLoading = b;
  }

  private void init() {
    setOnScrollPositionListener(this);
  }

  public void setOnPageListener(OnPageListener pageListener) {
    this.onPageListener = pageListener;
  }

  public void setOnTopPageListener(OnTopPageListener onTopPageListener) {
    this.onTopPageListener = onTopPageListener;
  }

  @Override
  public void onScrollToTop() {
    if (pageEnable && onTopPageListener != null && !isLoading) {
      isLoading = true;
      onTopPageListener.onTopPage();
    }
  }

  @Override
  public void onScrollToBottom() {
    if (pageEnable && onPageListener != null && !isLoading) {
      isLoading = true;
      onPageListener.onPage();
    }
  }

  public void notifyDataSetChanged() {
    if (bookends != null) {
      bookends.notifyDataSetChanged();
    } else {
    }
  }

  public void notifyItemRangeChanged(int positionStart, int itemCount) {
    bookends.notifyItemRangeChanged(positionStart, itemCount);
  }

  public void notifyItemInserted(int position) {
    bookends.notifyItemInserted(position);
  }

  public void notifyItemRemoved(int position) {
    bookends.notifyItemRemoved(position);
  }

  public void notifyItemChanged(int position) {
    bookends.notifyItemChanged(position);
  }

  @Override
  public void setAdapter(RecyclerView.Adapter adapter) {
    bookends = new Bookends(adapter);
    super.setAdapter(bookends);
  }

  public void setAdapter(Bookends adapter) {
    super.setAdapter(adapter);
    this.bookends = adapter;
  }

  public Bookends<?> getBookendsAdapter() {
    return bookends;
  }

  /**
   * Add a header view.
   */
  public void addHeader(View view) {
    bookends.addHeader(view);
  }

  public void removeHeader(View view) {
    bookends.removeHeader(view);
  }

  /**
   * Add a footer view.
   */
  public void addFooter(View view) {
    bookends.addFooter(view);
  }

  public void removeFooter(View view) {
    bookends.removeFooter(view);
  }

  public void setPageFooter(View view) {
    loadingFooter = view;
  }

  public void setPageFooter(int resource) {
    loadingFooter = LayoutInflater.from(getContext()).inflate(resource, null);
  }

  private void attachPageFooter() {
    if (loadingFooter != null && !hasAttachedFooter) {
      hasAttachedFooter = true;
      bookends.addFooter(loadingFooter);
      bookends.setFooterVisibility(loadingFooter, false);
    }
  }

  /**
   * 加载完所有数据后，移除Footer
   */
  public void removePageFooter() {
    if (loadingFooter != null && hasAttachedFooter) {
      hasAttachedFooter = false;
      bookends.removeFooter(loadingFooter);
    }
  }

  public void showLoadingFooter() {
    if (!hasAttachedFooter) {
      attachPageFooter();
    }

    if (loadingFooter != null) {
      bookends.setFooterVisibility(loadingFooter, true);
    }
  }

  public void setOnScrollDirectionListener(final OnScrollDirectionListener listener) {
    addOnScrollListener(new RecyclerView.OnScrollListener() {
      @Override
      public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
        super.onScrolled(recyclerView, dx, dy);
        if (dy < -20) {
          listener.onScrollDown();
        } else if (dy > 20) {
          listener.onScrollUp();
        }
      }
    });
  }

  public interface OnScrollDirectionListener {

    void onScrollUp();

    void onScrollDown();
  }
}