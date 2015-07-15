package com.liuguangqiang.recyclerview.sample;

import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import com.liuguangqiang.asyncokhttp.AsyncOkHttp;
import com.liuguangqiang.asyncokhttp.JsonResponseHandler;
import com.liuguangqiang.recyclerview.listener.OnPageListener;
import com.liuguangqiang.recyclerview.widget.SuperRecyclerView;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity implements OnPageListener {

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
        recyclerView = (SuperRecyclerView) findViewById(R.id.recycler_view);
        adapter = new StoryAdapter(getApplicationContext(), data);
        recyclerView.setAdapter(adapter);
        recyclerView.setOnPageListener(this);
    }

    @Override
    public void onPage() {
        getDaily(lastDatetime);
    }

    public void getDaily(int datetime) {
        String url = datetime > 0 ? ApiUtils.getNewsBefore(datetime) : ApiUtils.getLatest();
        AsyncOkHttp.getInstance().get(url, new JsonResponseHandler<Daily>(Daily.class) {
            @Override
            public void onSuccess(Daily daily) {
                if (daily != null) {
                    lastDatetime = daily.getDate();
                    data.addAll(daily.getStories());

                    new Handler().postDelayed(new Runnable() {
                        @Override
                        public void run() {
                            recyclerView.notifyDataSetChanged();
                            recyclerView.onLoadFinish();
                        }
                    }, 1000);
                }
            }

            @Override
            public void onStart() {
                super.onStart();
                recyclerView.onLoadStart();
            }
        });
    }

}
