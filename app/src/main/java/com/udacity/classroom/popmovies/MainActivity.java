package com.udacity.classroom.popmovies;

import android.os.Bundle;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;

import com.udacity.classroom.popmovies.fragment.MovieDetailFragment;
import com.udacity.classroom.popmovies.fragment.MovieListFragment;
import com.udacity.classroom.popmovies.fragment.SettingsFragment;
import com.udacity.classroom.popmovies.interfaces.OnMovieSelectedListener;
import com.udacity.classroom.popmovies.interfaces.SnackbarListener;
import com.udacity.classroom.popmovies.model.Movie;

public class MainActivity extends AppCompatActivity implements SnackbarListener, OnMovieSelectedListener {
    public static String MOVIE_ID = "Movie ID";
    public static String POSTER_PATH = "Poster Path";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        getSupportFragmentManager().beginTransaction()
                .add(R.id.fragment_container, new MovieListFragment())
                .commit();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        switch (id) {
            case R.id.action_settings :
                showSettingsFragment();
                return true;
            case android.R.id.home :
                onBackPressed();
                return true;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public void showLoadingSnackbar(int resId, int duration) {
        Snackbar.make(findViewById(android.R.id.content), resId, duration)
                .show();
    }

    @Override
    public void showNoInternetSnackbar(View.OnClickListener clickListener) {
        Snackbar.make(findViewById(android.R.id.content), R.string.msg_no_internet, Snackbar.LENGTH_LONG)
                .setAction(R.string.action_try_again, clickListener)
                .show();
    }

    @Override
    public void showErrorSnackbar(View.OnClickListener clickListener) {
        Snackbar.make(findViewById(android.R.id.content), R.string.msg_something_wrong, Snackbar.LENGTH_LONG)
                .setAction(R.string.action_try_again, clickListener)
                .show();
    }

    @Override
    public void showFailedSnackbar(int resId, View.OnClickListener clickListener) {
        Snackbar.make(findViewById(android.R.id.content), resId, Snackbar.LENGTH_LONG)
                .setAction(R.string.action_try_again, clickListener)
                .show();
    }

    @Override
    public void onMovieSelected(Movie movie) {
        MovieDetailFragment fragment = new MovieDetailFragment();
        Bundle bundle = new Bundle();
        bundle.putInt(MOVIE_ID, movie.getMovieId());
        bundle.putString(POSTER_PATH, movie.getPosterPath());
        fragment.setArguments(bundle);

        getSupportFragmentManager().beginTransaction()
                .replace(R.id.fragment_container, fragment)
                .addToBackStack(null)
                .commit();
    }

    private void showSettingsFragment() {
        getSupportFragmentManager().beginTransaction()
                .replace(R.id.fragment_container, new SettingsFragment())
                .addToBackStack(null)
                .commit();
    }
}
