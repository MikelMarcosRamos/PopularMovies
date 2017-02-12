package com.example.android.popularmovies.utilities;

import com.example.android.popularmovies.data.Movies;

import java.util.Map;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Path;
import retrofit2.http.QueryMap;

public interface IMovieApi {
    @GET("movie/{tipo_listado}")
    Call<Movies> obtenerPeliculas(@Path("tipo_listado") String tipo_listado, @QueryMap Map<String, String> opciones);


}
