package com.example.android.popularmovies;

import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.example.android.popularmovies.data.Pelicula;
import com.example.android.popularmovies.data.TheMovieDbPreferencias;
import com.example.android.popularmovies.utilities.NetworkUtils;
import com.example.android.popularmovies.utilities.PeliculasJsonUtils;


import java.net.URL;

public class MainActivity extends AppCompatActivity implements TheMovieDbAdapter.TheMovieDbAdapterOnClickHandler {

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

        this.mRvPeliculas.setLayoutManager(new GridLayoutManager(context, 2));
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

        new CargarPeliculasTarea().execute(tipoLista);
    }

    @Override
    public void onClick(Pelicula pelicula) {
        Context context = this;
        Class destinationClass = DetallesPelicula.class;
        Intent intentToStartDetailActivity = new Intent(context, destinationClass);
        intentToStartDetailActivity.putExtra("pelicula", pelicula);
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

    /**
     * AsyncTask to load films information
     */
    public class CargarPeliculasTarea extends AsyncTask<Integer, Void, Pelicula[]> {

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            mCargando.setVisibility(View.VISIBLE);
        }

        @Override
        protected Pelicula[] doInBackground(Integer... params) {

            /* If there's no zip code, there's nothing to look up. */
            if (params.length == 0) {
                return null;
            }

            URL url = NetworkUtils.buildUrl(MainActivity.this, params[0]);

            try {
                String respuestaJson = NetworkUtils
                        .getResponseFromHttpUrl(url);

                return PeliculasJsonUtils
                        .getPeliculas(MainActivity.this, respuestaJson);

            } catch (Exception e) {
                e.printStackTrace();
                return null;
            }
        }

        @Override
        protected void onPostExecute(Pelicula[] peliculas) {
            mCargando.setVisibility(View.INVISIBLE);
            if (peliculas != null) {
                mostrarDatosPeliculas();
                mTheMovieDbAdapter.setDatosPeliculas(peliculas);
            } else {
                mostrarMensajeError();
            }
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
}
