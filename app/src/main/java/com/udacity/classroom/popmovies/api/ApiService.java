package com.udacity.classroom.popmovies.api;

import com.udacity.classroom.popmovies.model.MovieDetail;
import com.udacity.classroom.popmovies.model.MovieList;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Path;
import retrofit2.http.Query;

/**
 * Created by NgekNgok
 */

public interface ApiService {
    @GET("movie/popular")
    Call<MovieList> getPopularMovies(@Query("api_key") String apiKey);

    @GET("movie/top_rated")
    Call<MovieList> getTopRatedMovies(@Query("api_key") String apiKey);

    @GET("movie/{movie_id}")
    Call<MovieDetail> getMovieDetail(@Path("movie_id") int movieId, @Query("api_key") String apiKey);
}
