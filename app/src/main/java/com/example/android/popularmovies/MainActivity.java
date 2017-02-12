package com.example.android.popularmovies;

import android.content.Context;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.DisplayMetrics;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.example.android.popularmovies.data.Movie;
import com.example.android.popularmovies.data.Movies;
import com.example.android.popularmovies.data.TheMovieDbPreferencias;
import com.example.android.popularmovies.utilities.ClienteRest;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class MainActivity extends AppCompatActivity implements TheMovieDbAdapter.TheMovieDbAdapterOnClickHandler, Callback<Movies> {

    private RecyclerView mRvPeliculas;

    private TheMovieDbAdapter mTheMovieDbAdapter;

    private TextView mMostrarError;

    private ProgressBar mCargando;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        this.mRvPeliculas = (RecyclerView) this.findViewById(R.id.rv_peliculas);
        this.mMostrarError = (TextView) this.findViewById(R.id.tv_mostrar_error);
        this.mCargando = (ProgressBar) this.findViewById(R.id.pb_cargando);

        Context context = this;

        int numColumnas = calcularColumnasListado(context);
        this.mRvPeliculas.setLayoutManager(new GridLayoutManager(context, numColumnas));
        this.mRvPeliculas.setHasFixedSize(true);
        this.mTheMovieDbAdapter = new TheMovieDbAdapter(this);
        this.mRvPeliculas.setAdapter(this.mTheMovieDbAdapter);

        cargarDatosPeliculas();
    }


    private void cargarDatosPeliculas() {
        Integer tipoLista = TheMovieDbPreferencias.getListaPorDefecto();
        cargarDatosPeliculas(tipoLista);
    }

    private void cargarDatosPeliculas(Integer tipoLista) {
        mostrarDatosPeliculas();

        Call<Movies> moviesCall = new ClienteRest(this).obtenerPeliculas(tipoLista);
        moviesCall.enqueue(this);
    }

    private int calcularColumnasListado(Context context) {
        DisplayMetrics displayMetrics = context.getResources().getDisplayMetrics();
        float dpWidth = displayMetrics.widthPixels / displayMetrics.density;
        int numColumnas = (int) (dpWidth / 180);
        return numColumnas;
    }

    @Override
    public void onClick(Movie movie) {
        Context context = this;
        Class destinationClass = DetallesPelicula.class;
        Intent intentToStartDetailActivity = new Intent(context, destinationClass);
        intentToStartDetailActivity.putExtra("movie", movie);
        startActivity(intentToStartDetailActivity);
    }

    private void mostrarDatosPeliculas() {
        /* First, make sure the error is invisible */
        this.mMostrarError.setVisibility(View.INVISIBLE);
        /* Then, make sure the weather data is visible */
        this.mRvPeliculas.setVisibility(View.VISIBLE);
    }

    private void mostrarMensajeError() {
        /* First, hide the currently visible data */
        this.mRvPeliculas.setVisibility(View.INVISIBLE);
        /* Then, show the error */
        this.mMostrarError.setVisibility(View.VISIBLE);
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
        int id = item.getItemId();

        if (id == R.id.accion_popular) {
            mTheMovieDbAdapter.setDatosPeliculas(null);
            cargarDatosPeliculas(id);
            return true;
        } else if (id == R.id.accion_puntuacion) {
            mTheMovieDbAdapter.setDatosPeliculas(null);
            cargarDatosPeliculas(id);
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onResponse(Call<Movies> call, Response<Movies> response) {
        if(response.isSuccessful()) {
            mCargando.setVisibility(View.INVISIBLE);
            mostrarDatosPeliculas();
            mTheMovieDbAdapter.setDatosPeliculas(response.body());
        } else {
            mostrarMensajeError();
        }
    }

    @Override
    public void onFailure(Call<Movies> call, Throwable t) {
        mostrarMensajeError();
    }
}
