package com.example.android.popularmovies;

import android.content.ContentValues;
import android.content.Intent;
import android.database.Cursor;
import android.databinding.DataBindingUtil;
import android.net.Uri;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;

import com.example.android.popularmovies.data.Movie;
import com.example.android.popularmovies.data.MovieContract;
import com.example.android.popularmovies.data.Responses;
import com.example.android.popularmovies.data.Review;
import com.example.android.popularmovies.data.Video;
import com.example.android.popularmovies.databinding.ActivityDetallesPeliculaBinding;
import com.example.android.popularmovies.utilities.ClienteRest;
import com.example.android.popularmovies.utilities.NetworkUtils;
import com.google.android.gms.appindexing.Action;
import com.google.android.gms.appindexing.AppIndex;
import com.google.android.gms.appindexing.Thing;
import com.google.android.gms.common.api.GoogleApiClient;
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


    private RecyclerView mRvVideos;
    private VideoAdapter mVideoAdapter;

    private RecyclerView mRvCriticas;
    private ReviewAdapter mReviewAdapter;
    /**
     * ATTENTION: This was auto-generated to implement the App Indexing API.
     * See https://g.co/AppIndexing/AndroidStudio for more information.
     */
    private GoogleApiClient client;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detalles_pelicula);

        this.mBinding = DataBindingUtil.setContentView(this, R.layout.activity_detalles_pelicula);

        this.mRvVideos = (RecyclerView) this.findViewById(R.id.rv_videos);
        this.mRvVideos.setLayoutManager(new LinearLayoutManager(this));
        this.mRvVideos.setHasFixedSize(true);
        this.mVideoAdapter = new VideoAdapter(this);
        this.mRvVideos.setAdapter(this.mVideoAdapter);

        this.mRvCriticas = (RecyclerView) this.findViewById(R.id.rv_criticas);
        this.mRvCriticas.setLayoutManager(new LinearLayoutManager(this));
        this.mRvCriticas.setHasFixedSize(true);
        this.mReviewAdapter = new ReviewAdapter(this);
        this.mRvCriticas.setAdapter(this.mReviewAdapter);


        this.cargarDetalles();
        // ATTENTION: This was auto-generated to implement the App Indexing API.
        // See https://g.co/AppIndexing/AndroidStudio for more information.
        client = new GoogleApiClient.Builder(this).addApi(AppIndex.API).build();
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
                        if (mVideoAdapter.getItemCount() > 0) {
                            mBinding.lblVideos.setVisibility(View.VISIBLE);
                        }
                    } else {
                        mBinding.lblVideos.setVisibility(View.INVISIBLE);
                    }
                }

                @Override
                public void onFailure(Call<Responses<Video>> call, Throwable t) {
                    mBinding.lblVideos.setVisibility(View.INVISIBLE);
                    t.printStackTrace();
                }
            });

            Call<Responses<Review>> reviewsCall = new ClienteRest(this).obtenerCriticas(this.mMovie.getId());
            reviewsCall.enqueue(new Callback<Responses<Review>>() {
                @Override
                public void onResponse(Call<Responses<Review>> call, Response<Responses<Review>> response) {
                    if (response.isSuccessful()) {
                        mReviewAdapter.setCriticas(response.body());
                        if (mReviewAdapter.getItemCount() > 0) {
                            mBinding.lblCriticas.setVisibility(View.VISIBLE);
                        }

                    } else {
                        mBinding.lblCriticas.setVisibility(View.INVISIBLE);
                    }
                }

                @Override
                public void onFailure(Call<Responses<Review>> call, Throwable t) {
                    mBinding.lblCriticas.setVisibility(View.INVISIBLE);
                    t.printStackTrace();
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

        /* Return true so that the menu is displayed in the Toolbar */
        return true;
    }

    /**
     * ATTENTION: This was auto-generated to implement the App Indexing API.
     * See https://g.co/AppIndexing/AndroidStudio for more information.
     */
    public Action getIndexApiAction() {
        Thing object = new Thing.Builder()
                .setName("DetallesPelicula Page")
                .setUrl(Uri.parse("http://[ENTER-YOUR-URL-HERE]"))
                .build();
        return new Action.Builder(Action.TYPE_VIEW)
                .setObject(object)
                .setActionStatus(Action.STATUS_TYPE_COMPLETED)
                .build();
    }

    @Override
    public void onStart() {
        super.onStart();

        // ATTENTION: This was auto-generated to implement the App Indexing API.
        // See https://g.co/AppIndexing/AndroidStudio for more information.
        client.connect();
        AppIndex.AppIndexApi.start(client, getIndexApiAction());
    }

    @Override
    public void onStop() {
        super.onStop();

        // ATTENTION: This was auto-generated to implement the App Indexing API.
        // See https://g.co/AppIndexing/AndroidStudio for more information.
        AppIndex.AppIndexApi.end(client, getIndexApiAction());
        client.disconnect();
    }
}
