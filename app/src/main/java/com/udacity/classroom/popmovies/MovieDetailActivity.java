package com.udacity.classroom.popmovies;

import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.squareup.picasso.Picasso;
import com.udacity.classroom.popmovies.api.ApiClient;
import com.udacity.classroom.popmovies.api.ApiService;
import com.udacity.classroom.popmovies.model.MovieDetail;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class MovieDetailActivity extends AppCompatActivity {
    private Toolbar toolbar;
    private TextView releaseDate;
    private TextView length;
    private TextView rating;
    private TextView overview;

    private int movieId;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_movie_detail);

        toolbar = (Toolbar) findViewById(R.id.movie_toolbar);
        ImageView poster = (ImageView) findViewById(R.id.movie_detail_poster);
        releaseDate = (TextView) findViewById(R.id.movie_release_date);
        length = (TextView) findViewById(R.id.movie_length);
        rating = (TextView) findViewById(R.id.movie_rating);
        overview = (TextView) findViewById(R.id.movie_overview);

        Snackbar.make(findViewById(android.R.id.content), R.string.msg_load_detail_movies, Snackbar.LENGTH_SHORT).show();

        Intent intent = getIntent();
        movieId = intent.getIntExtra(MainActivity.MOVIE_ID, 0);
        getMovieDetail();

        String posterPath = intent.getStringExtra(MainActivity.POSTER_PATH);
        Picasso.with(this).load(ApiClient.BASE_POSTER_PATH + posterPath).into(poster);
    }

    private void getMovieDetail() {
        ApiService service = ApiClient.getService();
        Call<MovieDetail> call = service.getMovieDetail(movieId, ApiClient.API_KEY);

        call.enqueue(new Callback<MovieDetail>() {
            @Override
            public void onResponse(Call<MovieDetail> call, Response<MovieDetail> response) {
                if (response.isSuccessful()) {
                    MovieDetail movieDetail = response.body();

                    toolbar.setTitle(movieDetail.getTitle());
                    releaseDate.setText(movieDetail.getReleaseDate());
                    length.setText(movieDetail.getLength() + " min");
                    rating.setText(String.valueOf(movieDetail.getVoteAverage()) + "/10");
                    overview.setText(movieDetail.getOverview());
                } else {
                    if (!isOnline()) {
                        showSnackbar(R.string.msg_no_internet, R.string.btn_text_try_again);
                    } else {
                        showSnackbar(R.string.msg_something_wrong, R.string.btn_text_try_again);
                    }
                }
            }

            @Override
            public void onFailure(Call<MovieDetail> call, Throwable t) {
                showSnackbar(R.string.msg_list_load_failed, R.string.btn_text_try_again);
            }
        });

    }

    private void showSnackbar(int warnText, int buttonText) {
        Snackbar snackbar = Snackbar.make(findViewById(android.R.id.content), warnText, Snackbar.LENGTH_LONG);
        snackbar.setAction(buttonText, new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getMovieDetail();
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
