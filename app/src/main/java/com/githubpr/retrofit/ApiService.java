package com.githubpr.retrofit;

import com.githubpr.data.DataModel;
import com.githubpr.utils.Constants;

import java.util.List;

import retrofit2.Call;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;
import retrofit2.http.GET;
import retrofit2.http.Path;
import retrofit2.http.Query;

public interface ApiService {

    @GET("repos/{owner}/{repo}/pulls")
    Call<List<DataModel>> getAllOpenPullRequest(@Path("owner") String githubOwnerName, @Path("repo") String githubRepoName, @Query("page") int page, @Query("per_page") int perPageCount);

    class Creator {

        private Creator() {

        }

        public static ApiService newApiService() {
            Retrofit retrofit = new Retrofit.Builder()
                    .addConverterFactory(GsonConverterFactory.create())
                    .baseUrl(Constants.BASE_URL)
                    .build();
            return retrofit.create(ApiService.class);
        }
    }
}
