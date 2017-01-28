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

/**
 * Utility functions to handle OpenWeatherMap JSON data.
 */
public final class PeliculasJsonUtils {

    private static final String JM_RESULTADOS = "results";

    private static final String JM_RUTA_POSTER = "poster_path";
    private static final String JM_FECHA_ESTRENO = "release_date";
    private static final String JM_TITULO = "title";
    private static final String JM_MEDIA_VOTOS = "vote_average";
    private static final String JM_SINOPSIS = "overview";

    /**
     * This method parses JSON from a web response and returns an array of Strings
     * describing the weather over various days from the forecast.
     * <p/>
     * Later on, we'll be parsing the JSON into structured data within the
     * getFullWeatherDataFromJson function, leveraging the data we have stored in the JSON. For
     * now, we just convert the JSON into human-readable strings.
     *
     * @param peliculasJsonStr JSON response from server
     *
     * @return Array of Strings describing weather data
     *
     * @throws JSONException If JSON data cannot be properly parsed
     */
    public static Pelicula[] getPeliculas(Context context, String peliculasJsonStr)
            throws JSONException {

        JSONObject peliculasJson = new JSONObject(peliculasJsonStr);

        JSONArray arrayPeliculas = peliculasJson.getJSONArray(JM_RESULTADOS);

        Pelicula[] peliculas = new Pelicula[arrayPeliculas.length()];

        for (int i = 0; i < arrayPeliculas.length(); i++) {

            /* Get the JSON object representing the day */
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

    /**
     * Parse the JSON and convert it into ContentValues that can be inserted into our database.
     *
     * @param context         An application context, such as a service or activity context.
     * @param forecastJsonStr The JSON to parse into ContentValues.
     *
     * @return An array of ContentValues parsed from the JSON.
     */
    public static ContentValues[] getFullWeatherDataFromJson(Context context, String forecastJsonStr) {
        /** This will be implemented in a future lesson **/
        return null;
    }
}