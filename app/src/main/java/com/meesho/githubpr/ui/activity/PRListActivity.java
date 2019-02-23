package com.meesho.githubpr.ui.activity;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.TextView;

import com.meesho.githubpr.R;
import com.meesho.githubpr.data.BaseData;
import com.meesho.githubpr.data.DataModel;
import com.meesho.githubpr.data.LoaderData;
import com.meesho.githubpr.retrofit.CallHandler;
import com.meesho.githubpr.retrofit.RequestListener;
import com.meesho.githubpr.ui.adapter.PRListAdapter;
import com.meesho.githubpr.utils.Constants;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import retrofit2.Call;
import retrofit2.Response;

public class PRListActivity extends AppCompatActivity {

    @BindView(R.id.recycle_view)
    RecyclerView recyclerView;
    SwipeRefreshLayout swipeRefreshLayout;

    TextView emptyView;

    View loader;
    private String githubOwnerName;
    private String githubRepoName;
    private CallHandler callHandler;
    private List<BaseData> dataList;
    private PRListAdapter adapter;
    private boolean syncCallFinish = false;
    private boolean completeItemLoaded = false;
    private int currentPage = 1;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_pr_list);
        setTitle(R.string.open_pull_request);
        ButterKnife.bind(this);
        recyclerView = findViewById(R.id.recycle_view);
        swipeRefreshLayout = findViewById(R.id.swipe_refresh_layout);
        emptyView = findViewById(R.id.empty_view);
        loader = findViewById(R.id.loader);
        githubOwnerName = getIntent().getExtras().getString(Constants.GITHUB_OWNER_NAME);
        githubRepoName = getIntent().getExtras().getString(Constants.GITHUB_REPO_NAME);
        init();
    }

    private void init() {
        dataList = new ArrayList<>();
        adapter = new PRListAdapter(this, dataList);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        recyclerView.setAdapter(adapter);
        callHandler = new CallHandler();
        loadPage(currentPage);
        recyclerView.setOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrollStateChanged(@NonNull RecyclerView recyclerView, int newState) {
                super.onScrollStateChanged(recyclerView, newState);
            }

            @Override
            public void onScrolled(@NonNull RecyclerView recyclerView, int dx, int dy) {
                super.onScrolled(recyclerView, dx, dy);
                int lastPosition = ((LinearLayoutManager) recyclerView.getLayoutManager()).findLastVisibleItemPosition();
                if (lastPosition == adapter.getItemCount() - 1 && !completeItemLoaded && !syncCallFinish) {
                    loadPage(currentPage++);
                }
            }
        });
        swipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                currentPage = 1;
                completeItemLoaded = false;
                loadPage(currentPage);
            }
        });
    }

    private void loadPage(final int page) {
        if (syncCallFinish)     // if call is already going for syncing then wait for it's response
            return;

        syncCallFinish = true;
        callHandler.getAllOpenPullRequest(githubOwnerName, githubRepoName, page, Constants.GITHUB_PER_PAGE_COUNT, new RequestListener() {
            @Override
            public void onResponse(Call<List<DataModel>> call, Response<List<DataModel>> response) {
                loader.setVisibility(View.GONE);
                swipeRefreshLayout.setRefreshing(false);
                if (page == 1) {
                    dataList.clear();   // clear data list on refresh call
                }
                if (response != null && response.body() != null) {
                    removeLoader();
                    dataList.addAll(response.body());
                    dataList.add(new LoaderData());
                    adapter.updateData(dataList);
                    adapter.notifyDataSetChanged();
                    if (response.body().size() != Constants.GITHUB_PER_PAGE_COUNT) {
                        completeItemLoaded = true;  //if all item have loaded
                        removeLoader();
                    }
                } else {
                    removeLoader();
                }
                if (dataList.size() == 0) {
                    emptyView.setVisibility(View.VISIBLE);
                    emptyView.setText(getString(R.string.no_open_pull_request_found, githubOwnerName, githubRepoName));
                } else {
                    emptyView.setVisibility(View.GONE);
                }
                syncCallFinish = false;
            }

            private void removeLoader() {
                if (dataList.size() > 0)
                    dataList.remove(dataList.size() - 1);
            }

            @Override
            public void onFailure(Call<List<DataModel>> call, Throwable t) {
                swipeRefreshLayout.setRefreshing(false);
                loader.setVisibility(View.GONE);
                emptyView.setVisibility(View.VISIBLE);
                emptyView.setText(R.string.something_went_wrong);
                syncCallFinish = false;
            }
        });
    }
}
