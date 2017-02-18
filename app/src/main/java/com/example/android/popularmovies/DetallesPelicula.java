package com.example.android.popularmovies;

import android.content.ContentValues;
import android.content.Intent;
import android.database.Cursor;
import android.databinding.DataBindingUtil;
import android.net.Uri;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;

import com.example.android.popularmovies.adapter.ReviewAdapter;
import com.example.android.popularmovies.adapter.VideoAdapter;
import com.example.android.popularmovies.data.Movie;
import com.example.android.popularmovies.data.db.MovieContract;
import com.example.android.popularmovies.data.Responses;
import com.example.android.popularmovies.data.Review;
import com.example.android.popularmovies.data.Video;
import com.example.android.popularmovies.databinding.ActivityDetallesPeliculaBinding;
import com.example.android.popularmovies.utilities.ClienteRest;
import com.example.android.popularmovies.utilities.NetworkUtils;
import com.squareup.picasso.Picasso;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * Activity to show the film details
 */
public class DetallesPelicula extends AppCompatActivity implements VideoAdapter.VideoAdapterOnClickHandler, ReviewAdapter.ReviewAdapterOnClickHandler {

    private final String TAG = DetallesPelicula.class.getSimpleName();

    private ActivityDetallesPeliculaBinding mBinding;

    private Movie mMovie;
    private VideoAdapter mVideoAdapter;
    private ReviewAdapter mReviewAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detalles_pelicula);

        this.mBinding = DataBindingUtil.setContentView(this, R.layout.activity_detalles_pelicula);

        this.mBinding.rvVideos.setLayoutManager(new LinearLayoutManager(this));
        this.mBinding.rvVideos.setEmptyView(this.findViewById(R.id.lbl_rv_videos_empty));
        this.mBinding.rvVideos.setHasFixedSize(true);
        this.mVideoAdapter = new VideoAdapter(this);
        this.mBinding.rvVideos.setAdapter(this.mVideoAdapter);

        this.mBinding.rvCriticas.setLayoutManager(new LinearLayoutManager(this));
        this.mBinding.rvCriticas.setEmptyView(this.findViewById(R.id.lbl_rv_criticas_empty));
        this.mBinding.rvCriticas.setHasFixedSize(true);
        this.mReviewAdapter = new ReviewAdapter(this);
        this.mBinding.rvCriticas.setAdapter(this.mReviewAdapter);


        this.cargarDetalles();
    }

    protected void cargarDetalles() {
        Intent intent = getIntent();
        if (intent.hasExtra("movie") && mBinding != null) {
            this.mMovie = intent.getParcelableExtra("movie");

            this.mBinding.peliculaTitulo.setText(this.mMovie.getTitulo());
            Picasso.with(this)
                    .load(NetworkUtils.getRutaImagen(this.mMovie.getPoster()))
                    .placeholder(R.mipmap.im_loading)
                    .error(R.mipmap.ic_not_found)
                    .into(this.mBinding.peliculaPoster);
            this.mBinding.datosPelicula.peliculaFechaEstreno.setText(this.mMovie.getFechaEstreno());
            this.mBinding.datosPelicula.peliculaMediaVotos.setText(Double.toString(this.mMovie.getMediaVotos()));
            this.mBinding.datosPelicula.peliculaSinopsis.setText(this.mMovie.getSinopsis());

            Call<Responses<Video>> videosCall = new ClienteRest(this).obtenerVideos(this.mMovie.getId());
            videosCall.enqueue(new Callback<Responses<Video>>() {
                @Override
                public void onResponse(Call<Responses<Video>> call, Response<Responses<Video>> response) {
                    if (response.isSuccessful()) {
                        mVideoAdapter.setVideos(response.body());
                    }
                }

                @Override
                public void onFailure(Call<Responses<Video>> call, Throwable t) {
                    Log.e(TAG, t.getMessage(), t);
                }
            });

            Call<Responses<Review>> reviewsCall = new ClienteRest(this).obtenerCriticas(this.mMovie.getId());
            reviewsCall.enqueue(new Callback<Responses<Review>>() {
                @Override
                public void onResponse(Call<Responses<Review>> call, Response<Responses<Review>> response) {
                    if (response.isSuccessful()) {
                        mReviewAdapter.setCriticas(response.body());

                    }
                }

                @Override
                public void onFailure(Call<Responses<Review>> call, Throwable t) {
                    Log.e(TAG, t.getMessage(), t);
                }
            });
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
    public void onClick(Review review) {

        Uri uri = Uri.parse(review.getUrl());

        Intent intent = new Intent(Intent.ACTION_VIEW);
        intent.setData(uri);

        if (intent.resolveActivity(getPackageManager()) != null) {
            startActivity(intent);
        } else {
            Log.d(TAG, "Couldn't call " + uri.toString() + ", no receiving apps installed!");
        }
    }

    @Override
    public void onClick(Video video) {
        Uri uri = Uri.parse("https://www.youtube.com/watch?v=" + video.getKey());

        Intent intent = new Intent(Intent.ACTION_VIEW);
        intent.setData(uri);

        if (intent.resolveActivity(getPackageManager()) != null) {
            startActivity(intent);
        } else {
            Log.d(TAG, "Couldn't call " + uri.toString() + ", no receiving apps installed!");
        }
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
