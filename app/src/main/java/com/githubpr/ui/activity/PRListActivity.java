package com.githubpr.ui.activity;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.TextView;

import com.githubpr.R;
import com.githubpr.data.BaseData;
import com.githubpr.data.DataModel;
import com.githubpr.data.LoaderData;
import com.githubpr.retrofit.CallHandler;
import com.githubpr.retrofit.RequestListener;
import com.githubpr.ui.adapter.PRListAdapter;
import com.githubpr.utils.Constants;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import io.reactivex.Observable;
import io.reactivex.ObservableEmitter;
import io.reactivex.ObservableOnSubscribe;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.Disposable;
import io.reactivex.observers.DisposableObserver;
import io.reactivex.schedulers.Schedulers;
import retrofit2.Call;
import retrofit2.Response;

public class PRListActivity extends BaseActivity {

    @BindView(R.id.recycle_view)
    RecyclerView recyclerView;

    @BindView(R.id.swipe_refresh_layout)
    SwipeRefreshLayout swipeRefreshLayout;

    @BindView(R.id.empty_view)
    TextView emptyView;

    @BindView(R.id.loader)
    View loader;

    @BindView(R.id.go_to_top)
    View goToTop;

    private String githubOwnerName;
    private String githubRepoName;
    private CallHandler callHandler;
    private List<BaseData> dataList;
    private PRListAdapter adapter;
    private boolean syncCallFinish = false;
    private boolean completeItemLoaded = false;
    private int currentPage = 1;
    private Disposable disposable;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_pr_list);
        setTitle(R.string.open_pull_request);
        ButterKnife.bind(this);
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
                    loadPage(++currentPage);
                }
                int firstCompletelyVisibleItemPosition = ((LinearLayoutManager) recyclerView.getLayoutManager()).findFirstCompletelyVisibleItemPosition();
                if (firstCompletelyVisibleItemPosition == 0) {
                    goToTop.setVisibility(View.GONE);
                } else {
                    goToTop.setVisibility(View.VISIBLE);
                }
            }
        });
        swipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                currentPage = 1;
                completeItemLoaded = false;
                emptyView.setVisibility(View.GONE);
                loadPage(currentPage);
            }
        });
    }

    @OnClick(R.id.go_to_top)
    public void onClick() {
        if (dataList.size() > 40) {
            recyclerView.scrollToPosition(20);
        }
        recyclerView.smoothScrollToPosition(0);
    }

    private void loadPage(final int page) {
        if (syncCallFinish)     // if call is already going for syncing then wait for that response
            return;

        syncCallFinish = true;

        if (disposable != null)
            disposable.dispose();

        disposable = Observable.create(new ObservableOnSubscribe<Response<List<DataModel>>>() {
            @Override
            public void subscribe(final ObservableEmitter<Response<List<DataModel>>> emitter) {
                callHandler.getAllOpenPullRequest(githubOwnerName, githubRepoName, page, Constants.GITHUB_PER_PAGE_COUNT, new RequestListener() {
                    @Override
                    public void onResponse(Call<List<DataModel>> call, Response<List<DataModel>> response) {
                        emitter.onNext(response);
                    }

                    @Override
                    public void onFailure(Call<List<DataModel>> call, Throwable t) {
                        emitter.onNext(null);
                    }
                });
            }
        }).subscribeOn(Schedulers.io()).observeOn(AndroidSchedulers.mainThread()).subscribeWith(new DisposableObserver<Response<List<DataModel>>>() {
            @Override
            public void onNext(Response<List<DataModel>> response) {
                loader.setVisibility(View.GONE);
                syncCallFinish = false;
                swipeRefreshLayout.setRefreshing(false);

                if (response == null) {
                    showEmptyView();
                    return;
                } else if (response.body() != null) {
                    if (page == 1) {
                        dataList.clear();   // clear data list on refresh call
                    } else {
                        removeLoader();
                    }
                    dataList.addAll(response.body());
                    dataList.add(new LoaderData());
                    adapter.updateData(dataList);
                    adapter.notifyDataSetChanged();
                    if (response.body().size() != Constants.GITHUB_PER_PAGE_COUNT) {
                        completeItemLoaded = true;  //if all item have loaded
                        removeLoader();
                    }
                }
                if (dataList.size() == 0) {
                    emptyView.setVisibility(View.VISIBLE);
                    emptyView.setText(getString(R.string.no_open_pull_request_found, githubOwnerName, githubRepoName));
                } else {
                    emptyView.setVisibility(View.GONE);
                }
            }

            private void showEmptyView() {
                swipeRefreshLayout.setRefreshing(false);
                recyclerView.setVisibility(View.GONE);
                emptyView.setVisibility(View.VISIBLE);
                emptyView.setText(R.string.something_went_wrong);
            }

            private void removeLoader() {
                if (dataList.size() > 0)
                    dataList.remove(dataList.size() - 1);
            }

            @Override
            public void onError(Throwable e) {

            }

            @Override
            public void onComplete() {

            }
        });
    }

    @Override
    protected void onDestroy() {
        if (disposable != null)
            disposable.dispose();
        super.onDestroy();
    }
}
