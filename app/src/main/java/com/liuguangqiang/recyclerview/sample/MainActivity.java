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

import android.os.Bundle;
import android.os.Handler;
import android.support.v4.widget.SwipeRefreshLayout.OnRefreshListener;
import android.support.v7.app.AppCompatActivity;
import com.liuguangqiang.asyncokhttp.AsyncOkHttp;
import com.liuguangqiang.asyncokhttp.JsonResponseHandler;
import com.liuguangqiang.recyclerview.OnPageListener;
import com.liuguangqiang.recyclerview.SuperRecyclerView;
import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity implements OnPageListener, OnRefreshListener {

  private SuperRecyclerView recyclerView;
  private List<Story> data = new ArrayList<>();
  private StoryAdapter adapter;
  private int lastDatetime = 0;

  @Override
  protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    setContentView(R.layout.activity_main);
    initViews();
    getDaily(lastDatetime);
  }

  private void initViews() {
    recyclerView = findViewById(R.id.recycler_view);
    adapter = new StoryAdapter(getApplicationContext(), data);
    recyclerView.setAdapter(adapter);
    recyclerView.setOnPageListener(this);
    recyclerView.setOnRefreshListener(this);
    recyclerView.setAutoRefresh(true);
  }

  @Override
  public void onPage() {
    getDaily(lastDatetime);
  }

  @Override
  public void onRefresh() {
    lastDatetime = 0;
    getDaily(lastDatetime);
  }

  public void getDaily(final int datetime) {
    String url = datetime > 0 ? ApiUtils.getNewsBefore(datetime) : ApiUtils.getLatest();
    AsyncOkHttp.getInstance().get(url, new JsonResponseHandler<Daily>(Daily.class) {
      @Override
      public void onSuccess(Daily daily) {
        if (datetime == 0) {
          data.clear();
        }

        if (daily != null) {
          lastDatetime = daily.getDate();
          data.addAll(daily.getStories());

          new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
              recyclerView.notifyDataSetChanged();
              recyclerView.onRequestFinished();
            }
          }, 1000);
        }
      }

      @Override
      public void onStart() {
        super.onStart();
        recyclerView.startLoading();
      }
    });
  }
}
