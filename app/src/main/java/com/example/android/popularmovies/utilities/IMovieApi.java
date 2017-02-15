package com.example.android.popularmovies.utilities;

import com.example.android.popularmovies.data.Movie;
import com.example.android.popularmovies.data.Responses;
import com.example.android.popularmovies.data.Review;
import com.example.android.popularmovies.data.Video;

import java.util.Map;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Path;
import retrofit2.http.QueryMap;

interface IMovieApi {
    @GET("movie/{tipo_listado}")
    Call<Responses<Movie>> obtenerPeliculas(@Path("tipo_listado") String tipo_listado, @QueryMap Map<String, String> opciones);

    @GET("movie/{movie_id}/videos")
    Call<Responses<Video>> obtenerVideos(@Path("movie_id") Integer movie_id, @QueryMap Map<String, String> opciones);

    @GET("movie/{movie_id}/reviews")
    Call<Responses<Review>> obtenerCriticas(@Path("movie_id") Integer movie_id, @QueryMap Map<String, String> opciones);
}
