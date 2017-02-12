package com.example.android.popularmovies.utilities;

import android.content.Context;

import com.example.android.popularmovies.R;
import com.example.android.popularmovies.data.Movies;
import com.example.android.popularmovies.data.TheMovieDbPreferencias;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import java.util.HashMap;
import java.util.Map;

import retrofit2.Call;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class ClienteRest {
    private static final String BASE_URL = "api.themoviedb.org";
    private static final int VERSION_API = 3;
    private static final String URL = "https://" + BASE_URL + "/" + Integer.toString(VERSION_API) + "/";

    private String mClave;

    private Retrofit mRestAdapter;

    private static final String PARAMETRO_API = "api_key";
    private static final String PARAMETRO_IDIOMA = "language";
    private static final String PARAMETRO_PAGINA = "page";
    private static final String PARAMETRO_REGION = "region";
    private static final Map<Integer, String> BUSQUEDAS = new HashMap<Integer, String>() {{
        put(R.id.accion_popular, "popular");
        put(R.id.accion_puntuacion, "top_rated");
    }} ;

    // Servicios
    private IMovieApi mMovieService;

    public ClienteRest(Context context)
    {
        mClave = context.getString(R.string.api_key);
    }

    protected Retrofit.Builder retrofitBuilder() {
        Gson gson = new GsonBuilder()
                .setDateFormat("yyyy'-'MM'-'dd'T'HH':'mm':'ss'.'SSS'Z'")
                .create();
        return new Retrofit.Builder()
                .baseUrl(URL)
                .addConverterFactory(GsonConverterFactory.create(gson));
    }

    protected Retrofit obtenerRestAdapter() {
        if (mRestAdapter == null) {
            mRestAdapter = retrofitBuilder().build();
        }
        return mRestAdapter;
    }

    protected IMovieApi getMovieService(){
        if (mMovieService == null) {
            mMovieService = obtenerRestAdapter().create(IMovieApi.class);
        }
        return mMovieService;
    }

    public Call<Movies> obtenerPeliculas(Integer tipoLista){
        String etiqueta = BUSQUEDAS.get(tipoLista);

        Map<String, String> opciones = new HashMap<String, String>();
        opciones.put(PARAMETRO_API, mClave);
        opciones.put(PARAMETRO_IDIOMA, TheMovieDbPreferencias.VALOR_IDIOMA);
        opciones.put(PARAMETRO_PAGINA, Integer.toString(TheMovieDbPreferencias.VALOR_PAGINA));
        opciones.put(PARAMETRO_REGION, TheMovieDbPreferencias.VALOR_REGION);

        return getMovieService().obtenerPeliculas(etiqueta, opciones);
    }



}
