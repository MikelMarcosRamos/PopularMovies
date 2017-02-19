package com.example.android.popularmovies.fragment;

import android.content.ContentValues;
import android.content.Intent;
import android.database.Cursor;
import android.databinding.DataBindingUtil;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.android.popularmovies.MainActivity;
import com.example.android.popularmovies.R;
import com.example.android.popularmovies.adapter.ReviewAdapter;
import com.example.android.popularmovies.adapter.VideoAdapter;
import com.example.android.popularmovies.data.Movie;
import com.example.android.popularmovies.data.Responses;
import com.example.android.popularmovies.data.Review;
import com.example.android.popularmovies.data.Video;
import com.example.android.popularmovies.data.db.MovieContract;
import com.example.android.popularmovies.databinding.DetallesPeliculaBinding;
import com.example.android.popularmovies.utilities.ClienteRest;
import com.example.android.popularmovies.utilities.NetworkUtils;
import com.squareup.picasso.Picasso;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * A fragment representing a single DetallesPelicula detail screen.
 * This fragment is either contained in a {@link com.example.android.popularmovies.MainActivity}
 * in two-pane mode (on tablets) or a {@link com.example.android.popularmovies.DetallesPeliculaActivity}
 * on handsets.
 */
public class DetallesPeliculaFragment extends Fragment implements VideoAdapter.VideoAdapterOnClickHandler, ReviewAdapter.ReviewAdapterOnClickHandler {
    private final String TAG = DetallesPeliculaFragment.class.getSimpleName();

    public static final String ARG_ITEM_ID = "item_id";

    private DetallesPeliculaBinding mBinding;

    private Movie mMovie;
    private VideoAdapter mVideoAdapter;
    private ReviewAdapter mReviewAdapter;

    private boolean mDual;

    /**
     * Mandatory empty constructor for the fragment manager to instantiate the
     * fragment (e.g. upon screen orientation changes).
     */
    public DetallesPeliculaFragment() {
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        if (getArguments().containsKey("movie")) {
            this.mMovie = getArguments().getParcelable("movie");
        }

        this.mDual = getArguments().containsKey("dual");
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        this.mBinding = DataBindingUtil.inflate(
                inflater, R.layout.detalles_pelicula, container, false);
        View view = this.mBinding.getRoot();

        this.mBinding.datosVideos.rvVideos.setLayoutManager(new LinearLayoutManager(getContext()));
        this.mBinding.datosVideos.rvVideos.setEmptyView(this.mBinding.datosVideos.lblRvVideosEmpty);
        this.mBinding.datosVideos.rvVideos.setHasFixedSize(true);
        this.mVideoAdapter = new VideoAdapter(this);
        this.mBinding.datosVideos.rvVideos.setAdapter(this.mVideoAdapter);

        this.mBinding.datosCriticas.rvCriticas.setLayoutManager(new LinearLayoutManager(getContext()));
        this.mBinding.datosCriticas.rvCriticas.setEmptyView(this.mBinding.datosCriticas.lblRvCriticasEmpty);
        this.mBinding.datosCriticas.rvCriticas.setHasFixedSize(true);
        this.mReviewAdapter = new ReviewAdapter(this);
        this.mBinding.datosCriticas.rvCriticas.setAdapter(this.mReviewAdapter);

        cargarDetalles();

        cargarBoton();

        return view;
    }

    protected void cargarDetalles() {
        if (this.mMovie != null && mBinding != null) {

            this.mBinding.peliculaTitulo.setText(this.mMovie.getTitulo());
            Picasso.with(getContext())
                    .load(NetworkUtils.getRutaImagen(this.mMovie.getPoster()))
                    .placeholder(R.mipmap.im_loading)
                    .error(R.mipmap.ic_not_found)
                    .into(this.mBinding.peliculaPoster);
            this.mBinding.datosPelicula.peliculaFechaEstreno.setText(this.mMovie.getFechaEstreno());
            this.mBinding.datosPelicula.peliculaMediaVotos.setText(Double.toString(this.mMovie.getMediaVotos()));
            this.mBinding.datosPelicula.peliculaSinopsis.setText(this.mMovie.getSinopsis());

            Call<Responses<Video>> videosCall = new ClienteRest(getContext()).obtenerVideos(this.mMovie.getId());
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

            Call<Responses<Review>> reviewsCall = new ClienteRest(getContext()).obtenerCriticas(this.mMovie.getId());
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

    protected void cargarBoton() {
        if (this.mBinding.btnFavorito != null) {
            if (this.mDual) {
                this.mBinding.btnFavorito.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        Uri uri = MovieContract.MovieEntry.CONTENT_URI.buildUpon().appendPath(Integer.toString(mMovie.getId())).build();
                        int rowCount = getActivity().getContentResolver().delete(uri, null, null);
                        cargarBoton();
                    }
                });
                this.mBinding.btnNoFavorito.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        ContentValues contentValues = new ContentValues();
                        // Put the task description and selected mPriority into the ContentValues
                        contentValues.put(MovieContract.MovieEntry.COLUMN_ID, mMovie.getId());
                        contentValues.put(MovieContract.MovieEntry.COLUMN_TITULO, mMovie.getTitulo());
                        contentValues.put(MovieContract.MovieEntry.COLUMN_POSTER, mMovie.getPoster());
                        contentValues.put(MovieContract.MovieEntry.COLUMN_FECHA_ESTRENO, mMovie.getFechaEstreno());
                        contentValues.put(MovieContract.MovieEntry.COLUMN_MEDIA_VOTOS, mMovie.getMediaVotos());
                        contentValues.put(MovieContract.MovieEntry.COLUMN_SINOPSIS, mMovie.getSinopsis());
                        // Insert the content values via a ContentResolver
                        Uri uri = getActivity().getContentResolver().insert(MovieContract.MovieEntry.CONTENT_URI, contentValues);
                        cargarBoton();
                    }
                });

                Uri uri = MovieContract.MovieEntry.CONTENT_URI;//.buildUpon().appendPath(Integer.toString(mMovie.getId())).build();
                String condicion = MovieContract.MovieEntry.COLUMN_ID + " = ?";
                String[] valores = {""};
                valores[0] = Integer.toString(mMovie.getId());
                Cursor cursor = getContext().getContentResolver().query(uri,
                        null,
                        condicion,
                        valores,
                        null);


                if (cursor != null && cursor.getCount() > 0) {
                    this.mBinding.btnFavorito.setVisibility(View.VISIBLE);
                    this.mBinding.btnNoFavorito.setVisibility(View.GONE);
                } else {
                    this.mBinding.btnFavorito.setVisibility(View.GONE);
                    this.mBinding.btnNoFavorito.setVisibility(View.VISIBLE);
                }

                if (cursor != null) {
                    cursor.close();
                }
            } else {
                this.mBinding.btnFavorito.setVisibility(View.GONE);
                this.mBinding.btnNoFavorito.setVisibility(View.GONE);
            }
        }
    }

    @Override
    public void onClick(Review review) {

        Uri uri = Uri.parse(review.getUrl());

        Intent intent = new Intent(Intent.ACTION_VIEW);
        intent.setData(uri);

        if (intent.resolveActivity(getActivity().getPackageManager()) != null) {
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

        if (intent.resolveActivity(getActivity().getPackageManager()) != null) {
            startActivity(intent);
        } else {
            Log.d(TAG, "Couldn't call " + uri.toString() + ", no receiving apps installed!");
        }
    }
}
