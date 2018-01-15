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
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.AttributeSet;

/**
 * A RecyclerView using LinearLayoutManager or GridLayoutManager.
 * <p>
 * This RecycleView will listening itself whether scroll at the top or bottom.
 * </p>
 * Created by Eric on 2017/7/6.
 */
public class LinearRecyclerView extends RecyclerView {

  private boolean isBottom;
  private boolean isTop;

  private int firstVisibleItem;
  private int visibleItemCount;
  private int totalItemCount;

  private OnScrollPositionListener onScrollPositionListener;
  private OnScrollListener onScrollListener;

  public void setOnScrollPositionListener(OnScrollPositionListener listener) {
    onScrollPositionListener = listener;
  }

  public void setOnScrollListener(OnScrollListener listener) {
    onScrollListener = listener;
  }

  public LinearRecyclerView(Context context) {
    super(context);
    init();
  }

  public LinearRecyclerView(Context context, AttributeSet attrs) {
    super(context, attrs);
    init();
  }

  public LinearRecyclerView(Context context, AttributeSet attrs, int defStyle) {
    super(context, attrs, defStyle);
    init();
  }

  private void init() {
    setLayoutManager(new LinearLayoutManager(getContext()));
    addOnScrollListener(new RecyclerView.OnScrollListener() {
      @Override
      public void onScrollStateChanged(RecyclerView recyclerView, int newState) {
        if (newState == RecyclerView.SCROLL_STATE_IDLE && onScrollPositionListener != null) {
          if (isBottom) {
            onScrollPositionListener.onScrollToBottom();
          }
          if (isTop) {
            onScrollPositionListener.onScrollToTop();
          }
        }

        if (onScrollListener != null) {
          onScrollListener.onScrollStateChanged(recyclerView, newState);
        }
      }

      @Override
      public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
        if (onScrollPositionListener != null) {
          visibleItemCount = recyclerView.getChildCount();
          totalItemCount = getLayoutManager().getItemCount();
          firstVisibleItem = getFirstVisibleItem();

          isBottom = (firstVisibleItem + visibleItemCount) >= (totalItemCount - 1);
          isTop = firstVisibleItem == 0;
        }

        if (onScrollListener != null) {
          onScrollListener.onScrolled(recyclerView, dx, dy);
        }
      }
    });
  }

  public void setGridStyle(int spanCount) {
    setLayoutManager(new GridLayoutManager(getContext(), spanCount));
  }

  private int getFirstVisibleItem() {
    LayoutManager layoutManager = getLayoutManager();
    if (layoutManager instanceof LinearLayoutManager) {
      return ((LinearLayoutManager) layoutManager).findFirstVisibleItemPosition();
    } else if (layoutManager instanceof GridLayoutManager) {
      return ((GridLayoutManager) layoutManager).findFirstVisibleItemPosition();
    } else {
      return 0;
    }
  }

  public interface OnScrollPositionListener {

    void onScrollToTop();

    void onScrollToBottom();
  }

  public interface OnScrollListener {

    void onScrollStateChanged(RecyclerView recyclerView, int newState);

    void onScrolled(RecyclerView recyclerView, int dx, int dy);
  }

}
