package com.pisti.szotanulo;

import java.util.List;

import retrofit.Call;
import retrofit.http.Field;
import retrofit.http.FormUrlEncoded;
import retrofit.http.GET;
import retrofit.http.Path;

public interface WordsService {
    @GET("api/words/getAll/{request}")
    Call<List<Repository>> listRepos(@Path("request") int value);

    @GET("/api/words/{id}")
    Call<Repository> getWord();
}