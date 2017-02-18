package com.example.android.popularmovies;


import android.content.Context;
import android.database.Cursor;
import android.support.v4.content.AsyncTaskLoader;

import com.example.android.popularmovies.data.MovieContract;


public class DbAsyncLoader extends AsyncTaskLoader<Cursor> {

    // Initialize a Cursor, this will hold all the task data
    Cursor mMovieData = null;

    Context mContext;

    public DbAsyncLoader(Context context) {
        super(context);
        mContext = context;
    }


    // onStartLoading() is called when a loader first starts loading data
    @Override
    protected void onStartLoading() {
        if (mMovieData != null) {
            // Delivers any previously loaded data immediately
            deliverResult(mMovieData);
        } else {
            // Force a new load
            forceLoad();
        }
    }

    // loadInBackground() performs asynchronous loading of data
    @Override
    public Cursor loadInBackground() {
        // Will implement to load data

        try {
            return mContext.getContentResolver().query(MovieContract.MovieEntry.CONTENT_URI,
                    null,
                    null,
                    null,
                    null);

        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    // deliverResult sends the result of the load, a Cursor, to the registered listener
    public void deliverResult(Cursor data) {
        mMovieData = data;
        super.deliverResult(data);
    }
}
