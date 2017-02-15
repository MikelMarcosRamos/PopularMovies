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
import com.example.android.popularmovies.data.Responses;
import com.example.android.popularmovies.data.TheMovieDbPreferencias;
import com.example.android.popularmovies.utilities.ClienteRest;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class MainActivity extends AppCompatActivity implements TheMovieDbAdapter.TheMovieDbAdapterOnClickHandler, Callback<Responses<Movie>> {

    private RecyclerView mRvPeliculas;

    private TheMovieDbAdapter mTheMovieDbAdapter;

    private TextView mMostrarError;

    private ProgressBar mCargando;

    private int mTipoLista;

    private static final String TIPO_LISTA = "TIPO_LISTA";

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

        if (savedInstanceState != null) {
            this.mTipoLista = savedInstanceState.getInt(TIPO_LISTA);
        }

        cargarDatosPeliculas();
    }


    private void cargarDatosPeliculas() {
        if (this.mTipoLista < 1) {
            this.mTipoLista = TheMovieDbPreferencias.getListaPorDefecto();
        }

        mostrarDatosPeliculas();

        Call<Responses<Movie>> moviesCall = new ClienteRest(this).obtenerPeliculas(this.mTipoLista);
        moviesCall.enqueue(this);
    }


    private int calcularColumnasListado(Context context) {
        DisplayMetrics displayMetrics = context.getResources().getDisplayMetrics();
        float dpWidth = displayMetrics.widthPixels / displayMetrics.density;
        return (int) (dpWidth / 180);
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
        this.mTipoLista = item.getItemId();

        switch (this.mTipoLista) {
            case R.id.accion_popular:
            case R.id.accion_puntuacion:
                cargarDatosPeliculas();
                return true;
            default:
                throw new UnsupportedOperationException();
        }
    }

    @Override
    public void onResponse(Call<Responses<Movie>> call, Response<Responses<Movie>> response) {
        if(response.isSuccessful()) {
            mCargando.setVisibility(View.INVISIBLE);
            mostrarDatosPeliculas();
            mTheMovieDbAdapter.setDatosPeliculas(response.body());
        } else {
            mostrarMensajeError();
        }
    }

    @Override
    public void onFailure(Call<Responses<Movie>> call, Throwable t) {
        mostrarMensajeError();
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putInt(TIPO_LISTA, this.mTipoLista);
    }
}
