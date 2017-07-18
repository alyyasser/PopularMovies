package com.udacity.classroom.popmovies.fragment;

import android.content.Context;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.constraint.ConstraintLayout;
import android.support.design.widget.Snackbar;
import android.support.v4.app.Fragment;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.squareup.picasso.Picasso;
import com.udacity.classroom.popmovies.MainActivity;
import com.udacity.classroom.popmovies.R;
import com.udacity.classroom.popmovies.api.ApiClient;
import com.udacity.classroom.popmovies.api.ApiService;
import com.udacity.classroom.popmovies.interfaces.SnackbarListener;
import com.udacity.classroom.popmovies.model.MovieDetail;
import com.udacity.classroom.popmovies.utils.InternetConnection;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * Created by NgekNgok
 */

public class MovieDetailFragment extends Fragment {
    private Context context;
    private int movieId;

    private ConstraintLayout movieContainer;
    private ProgressBar movieDetailSpinner;
    private Toolbar toolbar;
    private TextView releaseDate;
    private TextView length;
    private TextView rating;
    private TextView overview;

    private SnackbarListener snackbarListener;

    private View.OnClickListener clickListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            getMovieDetail();
        }
    };

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_movie_detail, container, false);

        ((AppCompatActivity) getActivity()).getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        ImageView poster = (ImageView) rootView.findViewById(R.id.movie_detail_poster);
        toolbar = (Toolbar) rootView.findViewById(R.id.movie_toolbar);
        releaseDate = (TextView) rootView.findViewById(R.id.movie_release_date);
        length = (TextView) rootView.findViewById(R.id.movie_length);
        rating = (TextView) rootView.findViewById(R.id.movie_rating);
        overview = (TextView) rootView.findViewById(R.id.movie_overview);
        movieContainer = (ConstraintLayout) rootView.findViewById(R.id.movie_detail_container);
        movieDetailSpinner = (ProgressBar) rootView.findViewById(R.id.movie_detail_spinner);

        snackbarListener.showLoadingSnackbar(R.string.msg_load_detail_movies, Snackbar.LENGTH_LONG);

        Bundle bundle = getArguments();
        if (bundle != null) {
            movieId = bundle.getInt(MainActivity.MOVIE_ID);
            getMovieDetail();

            String posterPath = bundle.getString(MainActivity.POSTER_PATH);
            Picasso.with(context).load(ApiClient.BASE_POSTER_PATH + posterPath).into(poster);
        }

        return rootView;
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        this.context = context;

        snackbarListener = (SnackbarListener) context;
    }

    private void getMovieDetail() {
        movieDetailSpinner.setVisibility(View.VISIBLE);

        ApiService service = ApiClient.getService();
        Call<MovieDetail> call = service.getMovieDetail(movieId, ApiClient.API_KEY);

        call.enqueue(new Callback<MovieDetail>() {
            @Override
            public void onResponse(@NonNull Call<MovieDetail> call, @NonNull Response<MovieDetail> response) {
                movieDetailSpinner.setVisibility(View.GONE);
                if (response.isSuccessful()) {
                    MovieDetail movieDetail = response.body();

                    toolbar.setTitle(movieDetail.getTitle());
                    releaseDate.setText(movieDetail.getReleaseDate().split("-")[0]);
                    length.setText(getResources().getString(R.string.text_movie_length, movieDetail.getLength()));
                    rating.setText(getResources().getString(R.string.text_movie_rating, movieDetail.getVoteAverage()));
                    overview.setText(movieDetail.getOverview());

                    movieContainer.setVisibility(View.VISIBLE);
                } else {
                    if (!InternetConnection.isAvailable(context)) {
                        snackbarListener.showNoInternetSnackbar(clickListener);
                    } else {
                        snackbarListener.showErrorSnackbar(clickListener);
                    }
                }
            }

            @Override
            public void onFailure(@NonNull Call<MovieDetail> call, @NonNull Throwable t) {
                movieDetailSpinner.setVisibility(View.GONE);
                snackbarListener.showFailedSnackbar(R.string.msg_detail_load_failed, clickListener);
            }
        });

    }
}
