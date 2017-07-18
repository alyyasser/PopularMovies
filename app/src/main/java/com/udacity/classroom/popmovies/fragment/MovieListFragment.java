package com.udacity.classroom.popmovies.fragment;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.Snackbar;
import android.support.v4.app.Fragment;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.preference.PreferenceManager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.GridView;
import android.widget.ProgressBar;

import com.udacity.classroom.popmovies.R;
import com.udacity.classroom.popmovies.adapter.MovieAdapter;
import com.udacity.classroom.popmovies.api.ApiClient;
import com.udacity.classroom.popmovies.api.ApiService;
import com.udacity.classroom.popmovies.interfaces.OnMovieSelectedListener;
import com.udacity.classroom.popmovies.interfaces.SnackbarListener;
import com.udacity.classroom.popmovies.model.Movie;
import com.udacity.classroom.popmovies.model.MovieList;
import com.udacity.classroom.popmovies.utils.InternetConnection;

import java.util.ArrayList;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * Created by NgekNgok
 */

public class MovieListFragment extends Fragment {
    private static String KEY_PREF_SORT_ORDER = "sort_preference";

    private Context context;
    private ArrayList<Movie> movies = new ArrayList<>();

    private SnackbarListener snackbarListener;
    private OnMovieSelectedListener onMovieSelectedListener;

    private GridView movieGrid;
    private ProgressBar movieListSpinner;

    private View.OnClickListener clickListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            getMovieList();
        }
    };

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_movie_list, container, false);

        ((AppCompatActivity) getActivity()).getSupportActionBar().setDisplayHomeAsUpEnabled(false);

        movieListSpinner = (ProgressBar) rootView.findViewById(R.id.movie_list_spinner);

        movieGrid = (GridView) rootView.findViewById(R.id.movies_grid);
        movieGrid.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                onMovieSelectedListener.onMovieSelected(movies.get(position));
            }
        });

        if (!InternetConnection.isAvailable(context)) {
            snackbarListener.showNoInternetSnackbar(clickListener);
            return rootView;
        }

        snackbarListener.showLoadingSnackbar(R.string.msg_load_list_movies, Snackbar.LENGTH_LONG);
        getMovieList();

        return rootView;
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        this.context = context;

        snackbarListener = (SnackbarListener) context;
        onMovieSelectedListener = (OnMovieSelectedListener) context;
    }

    private void getMovieList() {
        movieListSpinner.setVisibility(View.VISIBLE);

        SharedPreferences sharedPrefs = PreferenceManager.getDefaultSharedPreferences(context);
        int pref = Integer.parseInt(sharedPrefs.getString(KEY_PREF_SORT_ORDER, "1"));

        ApiService service = ApiClient.getService();
        Call<MovieList> call = null;
        if (pref == 1) {
            call = service.getPopularMovies(ApiClient.API_KEY);
        } else {
            call = service.getTopRatedMovies(ApiClient.API_KEY);
        }

        call.enqueue(new Callback<MovieList>() {
            @Override
            public void onResponse(@NonNull Call<MovieList> call, @NonNull Response<MovieList> response) {
                movieListSpinner.setVisibility(View.GONE);
                if (response.isSuccessful()) {
                    movies = response.body().getMovies();
                    MovieAdapter adapter = new MovieAdapter(context, movies);
                    movieGrid.setAdapter(adapter);
                    movieGrid.setVisibility(View.VISIBLE);
                } else {
                    if (!InternetConnection.isAvailable(context)) {
                        snackbarListener.showNoInternetSnackbar(clickListener);
                    } else {
                        snackbarListener.showErrorSnackbar(clickListener);
                    }
                }
            }

            @Override
            public void onFailure(@NonNull Call<MovieList> call, @NonNull Throwable t) {
                movieListSpinner.setVisibility(View.GONE);
                snackbarListener.showFailedSnackbar(R.string.msg_list_load_failed, clickListener);
            }
        });

    }
}
