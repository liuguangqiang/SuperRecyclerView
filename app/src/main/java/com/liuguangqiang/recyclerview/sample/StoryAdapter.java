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

package com.liuguangqiang.recyclerview.sample;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import com.bumptech.glide.Glide;
import com.liuguangqiang.recyclerview.BaseAdapter;
import com.liuguangqiang.recyclerview.sample.StoryAdapter.StoryViewHolder;
import java.util.List;

/**
 * Created by Eric on 15/7/7.
 */
public class StoryAdapter extends BaseAdapter<Story, StoryViewHolder> {


  public StoryAdapter(Context context, List<Story> data) {
    super(context, data);
  }

  @Override
  public StoryViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
    return new StoryViewHolder(layoutInflater.inflate(R.layout.item_story, parent, false));
  }

  @Override
  public void onBindViewHolder(StoryViewHolder holder, int position) {
    super.onBindViewHolder(holder, position);
    holder.bindData(data.get(position));
  }

  public static class StoryViewHolder extends RecyclerView.ViewHolder {

    private TextView tvTitle;
    private ImageView ivPic;

    public StoryViewHolder(View view) {
      super(view);
      ivPic = itemView.findViewById(R.id.item_iv_pic);
      tvTitle = itemView.findViewById(R.id.item_tv_title);
    }

    public void bindData(Story story) {
      tvTitle.setText(story.getTitle());
      Glide.with(itemView.getContext()).load(story.getImages().get(0)).into(ivPic);
    }
  }
}
