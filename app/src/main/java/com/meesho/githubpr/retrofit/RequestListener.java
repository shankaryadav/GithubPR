package com.meesho.githubpr.retrofit;

import com.meesho.githubpr.data.DataModel;

import java.util.List;

import retrofit2.Call;
import retrofit2.Response;

public interface RequestListener {

    //TODO will make it generic type of response
    void onResponse(Call<List<DataModel>> call, Response<List<DataModel>> response);

    void onFailure(Call<List<DataModel>> call, Throwable t);

}
