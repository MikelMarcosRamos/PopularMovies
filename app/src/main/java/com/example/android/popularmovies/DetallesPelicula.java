package com.example.android.popularmovies;

import android.content.Intent;
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


    private RecyclerView mRvVideos;
    private VideoAdapter mVideoAdapter;

    private RecyclerView mRvCriticas;
    private ReviewAdapter mReviewAdapter;


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
                    if(response.isSuccessful()) {
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
                    if(response.isSuccessful()) {
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
        inflater.inflate(R.menu.orden_peliculas, menu);
        /* Return true so that the menu is displayed in the Toolbar */
        return true;
    }
}
