package com.udacity.classroom.popmovies;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.GridView;

import com.udacity.classroom.popmovies.adapter.MovieAdapter;
import com.udacity.classroom.popmovies.api.ApiClient;
import com.udacity.classroom.popmovies.api.ApiService;
import com.udacity.classroom.popmovies.model.Movie;
import com.udacity.classroom.popmovies.model.MovieList;

import java.util.ArrayList;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class MainActivity extends AppCompatActivity {
    private View rootView;
    private GridView movieGrid;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        rootView = findViewById(android.R.id.content);

        movieGrid = (GridView) findViewById(R.id.movies_grid);

        if (!isOnline()) {
            showSnackbar(R.string.msg_no_internet, R.string.btn_text_try_again);
            return;
        }

        Snackbar.make(rootView, R.string.msg_load_movies, Snackbar.LENGTH_LONG).show();
        getMovieList();
    }

    private void getMovieList() {
        ApiService service = ApiClient.getService();
        Call<MovieList> call = service.getPopularMovies(ApiClient.API_KEY);

        call.enqueue(new Callback<MovieList>() {
            @Override
            public void onResponse(Call<MovieList> call, Response<MovieList> response) {
                if (response.isSuccessful()) {
                    ArrayList<Movie> movies = response.body().getMovies();
                    MovieAdapter adapter = new MovieAdapter(getApplicationContext(), movies);
                    movieGrid.setAdapter(adapter);
                } else {
                    if (!isOnline()) {
                        showSnackbar(R.string.msg_no_internet, R.string.btn_text_try_again);
                    } else {
                        showSnackbar(R.string.msg_something_wrong, R.string.btn_text_try_again);
                    }
                }
            }

            @Override
            public void onFailure(Call<MovieList> call, Throwable t) {
                showSnackbar(R.string.msg_load_failed, R.string.btn_text_try_again);
            }
        });

    }

    private void showSnackbar(int warnText, int buttonText) {
        Snackbar snackbar = Snackbar.make(rootView, warnText, Snackbar.LENGTH_LONG);
        snackbar.setAction(buttonText, new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getMovieList();
            }
        });
        snackbar.show();
    }

    // credit : https://stackoverflow.com/a/4009133
    private boolean isOnline() {
        ConnectivityManager cm = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo netInfo = cm.getActiveNetworkInfo();
        return netInfo != null && netInfo.isConnectedOrConnecting();
    }
}
