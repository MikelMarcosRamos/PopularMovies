package com.example.android.popularmovies;

import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.databinding.DataBindingUtil;
import android.support.v4.app.LoaderManager;
import android.support.v4.content.Loader;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.GridLayoutManager;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;

import com.example.android.popularmovies.adapter.TheMovieDbAdapter;
import com.example.android.popularmovies.data.Movie;
import com.example.android.popularmovies.data.Responses;
import com.example.android.popularmovies.data.TheMovieDbPreferencias;
import com.example.android.popularmovies.databinding.ActivityMainBinding;
import com.example.android.popularmovies.loader.DbAsyncLoader;
import com.example.android.popularmovies.utilities.ClienteRest;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class MainActivity extends AppCompatActivity implements
        TheMovieDbAdapter.TheMovieDbAdapterOnClickHandler
        , Callback<Responses<Movie>>
        ,LoaderManager.LoaderCallbacks<Cursor>{

    private final String TAG = MainActivity.class.getSimpleName();

    private TheMovieDbAdapter mTheMovieDbAdapter;

    private ActivityMainBinding mBinding;

    private int mTipoLista;

    private static final String TIPO_LISTA = "TIPO_LISTA";

    private static final int MOVIE_LOADER_ID = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        this.mBinding = DataBindingUtil.setContentView(this, R.layout.activity_main);

        Context context = this;

        int numColumnas = calcularColumnasListado(context);
        this.mBinding.rvPeliculas.setLayoutManager(new GridLayoutManager(context, numColumnas));
        this.mBinding.rvPeliculas.setEmptyView(this.findViewById(R.id.lbl_rv_peliculas_empty));
        this.mBinding.rvPeliculas.setHasFixedSize(true);
        this.mTheMovieDbAdapter = new TheMovieDbAdapter(this);
        this.mBinding.rvPeliculas.setAdapter(this.mTheMovieDbAdapter);

        if (savedInstanceState != null) {
            this.mTipoLista = savedInstanceState.getInt(TIPO_LISTA);
        }

        getSupportLoaderManager().initLoader(MOVIE_LOADER_ID, null, this);
        cargarDatosPeliculas();
    }


    private void cargarDatosPeliculas() {
        if (this.mTipoLista < 1) {
            this.mTipoLista = TheMovieDbPreferencias.getListaPorDefecto();
        }
        this.mBinding.pbCargando.setVisibility(View.VISIBLE);
        mostrarDatosPeliculas();

        if (this.mTipoLista == R.id.accion_favoritas) {
            getSupportLoaderManager().restartLoader(MOVIE_LOADER_ID, null, this);
        } else {
            Call<Responses<Movie>> moviesCall = new ClienteRest(this).obtenerPeliculas(this.mTipoLista);
            moviesCall.enqueue(this);
        }
    }


    private int calcularColumnasListado(Context context) {
        DisplayMetrics displayMetrics = context.getResources().getDisplayMetrics();
        float dpWidth = displayMetrics.widthPixels / displayMetrics.density;
        return (int) (dpWidth / 180);
    }

    @Override
    public void onClick(Movie movie) {
        Context context = this;
        Class destinationClass = DetallesPeliculaActivity.class;
        Intent intentToStartDetailActivity = new Intent(context, destinationClass);
        intentToStartDetailActivity.putExtra("movie", movie);
        startActivity(intentToStartDetailActivity);
    }

    private void mostrarDatosPeliculas() {
        /* First, make sure the error is invisible */
        this.mBinding.tvMostrarError.setVisibility(View.GONE);
        /* Then, make sure the weather data is visible */
        this.mBinding.rvPeliculas.setVisibility(View.VISIBLE);
    }

    private void mostrarMensajeError() {
        /* First, hide the currently visible data */
        this.mBinding.rvPeliculas.setVisibility(View.GONE);
        /* Then, show the error */
        this.mBinding.tvMostrarError.setVisibility(View.VISIBLE);
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

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        this.mTipoLista = item.getItemId();

        switch (this.mTipoLista) {
            case R.id.accion_popular:
            case R.id.accion_puntuacion:
            case R.id.accion_favoritas:
                cargarDatosPeliculas();
                return true;
            default:
                throw new UnsupportedOperationException();
        }
    }

    @Override
    public void onResponse(Call<Responses<Movie>> call, Response<Responses<Movie>> response) {
        if(response.isSuccessful()) {
            this.mBinding.pbCargando.setVisibility(View.GONE);
            mostrarDatosPeliculas();
            mTheMovieDbAdapter.setDatosPeliculas(response.body());
        } else {
            mostrarMensajeError();
        }
    }

    @Override
    public void onFailure(Call<Responses<Movie>> call, Throwable t) {
        mostrarMensajeError();
        Log.e(TAG, t.getMessage(), t);
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putInt(TIPO_LISTA, this.mTipoLista);
    }

    @Override
    protected void onResume() {
        super.onResume();

        if (this.mTipoLista == R.id.accion_favoritas) {
            getSupportLoaderManager().restartLoader(MOVIE_LOADER_ID, null, this);
        }
    }

    @Override
    public Loader<Cursor> onCreateLoader(int id, Bundle args) {
        return new DbAsyncLoader(this);
    }

    @Override
    public void onLoadFinished(Loader<Cursor> loader, Cursor data) {
        mTheMovieDbAdapter.swapSource(data);
    }

    @Override
    public void onLoaderReset(Loader<Cursor> loader) {
        mTheMovieDbAdapter.swapSource(null);
    }
}
