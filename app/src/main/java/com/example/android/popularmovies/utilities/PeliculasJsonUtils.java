/*
 * Copyright (C) 2016 The Android Open Source Project
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package com.example.android.popularmovies.utilities;

import android.content.ContentValues;
import android.content.Context;
import android.util.Log;

import com.example.android.popularmovies.data.Pelicula;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.net.HttpURLConnection;

/**
 * Utility functions to handle OpenWeatherMap JSON data.
 */
public final class PeliculasJsonUtils {

    private static final String JM_CODIGO_ERROR = "status_code";
    private static final String JM_RESULTADOS = "results";

    private static final String JM_RUTA_POSTER = "poster_path";
    private static final String JM_FECHA_ESTRENO = "release_date";
    private static final String JM_TITULO = "title";
    private static final String JM_MEDIA_VOTOS = "vote_average";
    private static final String JM_SINOPSIS = "overview";

    /**
     * This method parses JSON from a web response and returns an array of Pelicula
     * with the information.
     * <p/>
     *
     * @param peliculasJsonStr JSON response from server
     *
     * @return Array of Pelicula with the films data
     *
     * @throws JSONException If JSON data cannot be properly parsed
     */
    public static Pelicula[] getPeliculas(Context context, String peliculasJsonStr)
            throws JSONException {

        JSONObject peliculasJson = new JSONObject(peliculasJsonStr);

        if (peliculasJson.has(JM_CODIGO_ERROR)) {
            int errorCode = peliculasJson.getInt(JM_CODIGO_ERROR);

            switch (errorCode) {
                case HttpURLConnection.HTTP_OK:
                    break;
                case HttpURLConnection.HTTP_UNAUTHORIZED:
                    /* Invalid API key */
                    return null;
                case HttpURLConnection.HTTP_NOT_FOUND:
                    /* Invalid request */
                    return null;
                default:
                    /* Server probably down */
                    return null;
            }
        }

        JSONArray arrayPeliculas = peliculasJson.getJSONArray(JM_RESULTADOS);

        Pelicula[] peliculas = new Pelicula[arrayPeliculas.length()];

        for (int i = 0; i < arrayPeliculas.length(); i++) {

            JSONObject peliculaJSObject = arrayPeliculas.getJSONObject(i);

            Pelicula pelicula = new Pelicula();
            pelicula.setTitulo(peliculaJSObject.getString(JM_TITULO));
            pelicula.setPoster(peliculaJSObject.getString(JM_RUTA_POSTER));
            pelicula.setFechaEstreno(peliculaJSObject.getString(JM_FECHA_ESTRENO));
            pelicula.setMediaVotos(peliculaJSObject.getDouble(JM_MEDIA_VOTOS));
            pelicula.setSinopsis(peliculaJSObject.getString(JM_SINOPSIS));
            peliculas[i] = pelicula;
        }
        return peliculas;
    }
}