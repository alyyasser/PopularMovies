package com.udacity.classroom.popmovies.interfaces;

import android.view.View;

/**
 * Created by NgekNgok
 */

public interface SnackbarListener {
    void showLoadingSnackbar(int resId, int duration);

    void showNoInternetSnackbar(View.OnClickListener clickListener);

    void showErrorSnackbar(View.OnClickListener clickListener);

    void showFailedSnackbar(int resId, View.OnClickListener clickListener);
}
