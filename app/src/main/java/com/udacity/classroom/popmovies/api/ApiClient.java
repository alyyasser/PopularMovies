package com.udacity.classroom.popmovies.api;

import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

/**
 * Created by NgekNgok
 */

public class ApiClient {
    public static final String API_KEY = "b48ceb020036b89222782f2087bfd2c8";
    public static final String BASE_POSTER_PATH = "http://image.tmdb.org/t/p/w185/";

    private static final String API_URL = "https://api.themoviedb.org/3/";

    private static Retrofit getInstance() {
        return new Retrofit.Builder()
                .baseUrl(API_URL)
                .addConverterFactory(GsonConverterFactory.create())
                .build();
    }

    public static ApiService getService() {
        return getInstance().create(ApiService.class);
    }
}
