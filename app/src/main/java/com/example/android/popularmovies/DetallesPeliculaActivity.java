package com.example.android.popularmovies;

import android.content.ContentValues;
import android.content.Intent;
import android.database.Cursor;
import android.databinding.DataBindingUtil;
import android.net.Uri;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;


import com.example.android.popularmovies.data.Movie;
import com.example.android.popularmovies.data.db.MovieContract;
import com.example.android.popularmovies.databinding.ActivityDetallesPeliculaBinding;
import com.example.android.popularmovies.fragment.DetallesPeliculaFragment;


/**
 * Activity to show the film details
 */
public class DetallesPeliculaActivity extends AppCompatActivity {

    private final String TAG = DetallesPeliculaActivity.class.getSimpleName();

    private ActivityDetallesPeliculaBinding mBinding;

    private Movie mMovie;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detalles_pelicula);

        this.mBinding = DataBindingUtil.setContentView(this, R.layout.activity_detalles_pelicula);

        this.cargarDetalles();
    }

    protected void cargarDetalles() {
        Intent intent = getIntent();
        if (intent.hasExtra("movie") && mBinding != null) {
            this.mMovie = intent.getParcelableExtra("movie");

            Bundle arguments = new Bundle();
            arguments.putParcelable("movie", this.mMovie);
            DetallesPeliculaFragment fragment = new DetallesPeliculaFragment();
            fragment.setArguments(arguments);
            getSupportFragmentManager().beginTransaction()
                    .replace(R.id.datos_pelicula_container, fragment)
                    .commit();
        }
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        if (id == android.R.id.home) {
            onBackPressed();
            return true;
        } else if (id == R.id.m_favorita) {
            Uri uri = MovieContract.MovieEntry.CONTENT_URI.buildUpon().appendPath(Integer.toString(mMovie.getId())).build();
            // COMPLETED (2) Delete a single row of data using a ContentResolver
            int rowCount = getContentResolver().delete(uri, null, null);

            // COMPLETED (3) Restart the loader to re-query for all tasks after a deletion
            if (rowCount > 0) {
                invalidateOptionsMenu();
            }
        } else if (id == R.id.m_no_favorita) {
            ContentValues contentValues = new ContentValues();
            // Put the task description and selected mPriority into the ContentValues
            contentValues.put(MovieContract.MovieEntry.COLUMN_ID, mMovie.getId());
            contentValues.put(MovieContract.MovieEntry.COLUMN_TITULO, mMovie.getTitulo());
            contentValues.put(MovieContract.MovieEntry.COLUMN_POSTER, mMovie.getPoster());
            contentValues.put(MovieContract.MovieEntry.COLUMN_FECHA_ESTRENO, mMovie.getFechaEstreno());
            contentValues.put(MovieContract.MovieEntry.COLUMN_MEDIA_VOTOS, mMovie.getMediaVotos());
            contentValues.put(MovieContract.MovieEntry.COLUMN_SINOPSIS, mMovie.getSinopsis());
            // Insert the content values via a ContentResolver
            Uri uri = getContentResolver().insert(MovieContract.MovieEntry.CONTENT_URI, contentValues);

            // Display the URI that's returned with a Toast
            // [Hint] Don't forget to call finish() to return to MainActivity after this insert is complete
            if (uri != null) {
                invalidateOptionsMenu();
            }

        }

        return super.onOptionsItemSelected(item);
    }




    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        /* Use AppCompatActivity's method getMenuInflater to get a handle on the menu inflater */
        MenuInflater inflater = getMenuInflater();
        /* Use the inflater's inflate method to inflate our menu layout to this menu */
        inflater.inflate(R.menu.favorita, menu);

        Uri uri = MovieContract.MovieEntry.CONTENT_URI;//.buildUpon().appendPath(Integer.toString(mMovie.getId())).build();
        String condicion = MovieContract.MovieEntry.COLUMN_ID + " = ?";
        String[] valores = {""};
        valores[0] = Integer.toString(mMovie.getId());
        Cursor cursor = getContentResolver().query(uri,
                null,
                condicion,
                valores,
                null);

        if (cursor != null && cursor.getCount() > 0) {
            menu.findItem(R.id.m_favorita).setVisible(true);
            menu.findItem(R.id.m_no_favorita).setVisible(false);
        } else {
            menu.findItem(R.id.m_favorita).setVisible(false);
            menu.findItem(R.id.m_no_favorita).setVisible(true);
        }

        if (cursor != null) {
            cursor.close();
        }

        /* Return true so that the menu is displayed in the Toolbar */
        return true;
    }
}
