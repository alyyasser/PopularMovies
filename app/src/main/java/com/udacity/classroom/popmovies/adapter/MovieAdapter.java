package com.udacity.classroom.popmovies.adapter;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;

import com.squareup.picasso.Picasso;
import com.udacity.classroom.popmovies.R;
import com.udacity.classroom.popmovies.api.ApiClient;
import com.udacity.classroom.popmovies.model.Movie;

import java.util.List;

/**
 * Created by NgekNgok
 */

public class MovieAdapter extends ArrayAdapter<Movie> {
    public MovieAdapter(Context context, List<Movie> movies) {
        super(context, 0, movies);
    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        Movie movie = getItem(position);

        if (convertView == null) {
            convertView = LayoutInflater.from(getContext()).inflate(R.layout.movie_poster, parent, false);
        }

        Picasso.with(getContext()).load(ApiClient.BASE_POSTER_PATH + movie.getPosterPath()).into((ImageView) convertView);

        return convertView;
    }

}
