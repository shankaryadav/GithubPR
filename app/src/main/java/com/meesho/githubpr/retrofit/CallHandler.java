package com.meesho.githubpr.retrofit;

import com.meesho.githubpr.data.DataModel;
import com.meesho.githubpr.utils.Constants;

import java.util.Iterator;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class CallHandler {

    private ApiService apiService;

    public CallHandler() {
        apiService = ApiService.Creator.newApiService();
    }

    public void getAllOpenPullRequest(String githubOwnerName, String githubRepoName, int page, int perPageCount, final RequestListener listener) {
        Call<List<DataModel>> openPRResponseCall = apiService.getAllOpenPullRequest(githubOwnerName, githubRepoName, page, perPageCount);
        openPRResponseCall.enqueue(new Callback<List<DataModel>>() {

            @Override
            public void onResponse(Call<List<DataModel>> call, Response<List<DataModel>> response) {
                if (response.isSuccessful() && response.body() != null && response.body().size() != 0) {
                    removeOtherPR(response);
                }
                listener.onResponse(call, response);
            }

            @Override
            public void onFailure(Call<List<DataModel>> call, Throwable throwable) {
                listener.onFailure(call, throwable);
            }
        });
    }

    private void removeOtherPR(Response<List<DataModel>> response) {
        Iterator iterator = response.body().iterator();
        while (iterator.hasNext()) {
            DataModel dataModel = (DataModel) iterator.next();
            if (!dataModel.getState().equals(Constants.OPEN_PR))
                iterator.remove();
        }
    }
}
