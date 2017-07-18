package com.udacity.classroom.popmovies.api;

import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

/**
 * Created by NgekNgok
 */

public class ApiClient {
    //TODO : Add TMDB Api Key here
    public static final String API_KEY = "";
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
