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

import android.content.Context;
import android.net.Uri;
import android.util.Log;

import com.example.android.popularmovies.R;

import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.HashMap;
import java.util.Map;
import java.util.Scanner;

/**
 * These utilities will be used to communicate with the weather servers.
 */
public final class NetworkUtils {

    private static final String TAG = NetworkUtils.class.getSimpleName();

    private static final String BASE_URL_BUSCAR =
            "https://api.themoviedb.org/3/movie/";

    private static final String BASE_URL_IMAGEN =
            "https://image.tmdb.org/t/p/w185/";


    /*
     * NOTE: These values only effect responses from OpenWeatherMap, NOT from the fake weather
     * server. They are simply here to allow us to teach you how to build a URL if you were to use
     * a real API.If you want to connect your app to OpenWeatherMap's API, feel free to! However,
     * we are not going to show you how to do so in this course.
     */

    /* The format we want our API to return */
    private static final String format = "json";

    static Map<Integer, String> BUSQUEDAS = new HashMap<Integer, String>() {{
        put(R.id.accion_popular, "popular");
        put(R.id.accion_puntuacion, "top_rated");
    }} ;

    final static String PARAMETRO_API = "api_key";
    final static String PARAMETRO_IDIOMA = "language";
    final static String PARAMETRO_PAGINA = "page";
    final static String PARAMETRO_REGION = "region";

    final static String VALOR_IDIOMA = "es-ES";
    final static Integer VALOR_PAGINA = 1;
    final static String VALOR_REGION = "Spain";


    /**
     * Crea la URL usada para pedir al servidor el listado de películas.
     *
     * @param tipoBusqueda tipo de búsqueda a realizar.
     * @return URL para la búsqueda de las películas.
     */
    public static URL buildUrl(Context context, Integer tipoBusqueda) {
        String etiqueta = BUSQUEDAS.get(tipoBusqueda);
        Uri builtUri = Uri.parse(BASE_URL_BUSCAR + etiqueta).buildUpon()
                .appendQueryParameter(PARAMETRO_API, context.getString(R.string.api_key))
                .appendQueryParameter(PARAMETRO_IDIOMA, VALOR_IDIOMA)
                .appendQueryParameter(PARAMETRO_PAGINA, Integer.toString(VALOR_PAGINA))
                .appendQueryParameter(PARAMETRO_REGION, VALOR_REGION)
                .build();

        URL url = null;
        try {
            url = new URL(builtUri.toString());
        } catch (MalformedURLException e) {
            e.printStackTrace();
        }

        Log.v(TAG, "Built URI " + url);

        return url;
    }

    /**
     * Builds the URL used to talk to the weather server using latitude and longitude of a
     * location.
     *
     * @param lat The latitude of the location
     * @param lon The longitude of the location
     * @return The Url to use to query the weather server.
     */
    public static URL buildUrl(Double lat, Double lon) {
        /** This will be implemented in a future lesson **/
        return null;
    }

    /**
     * This method returns the entire result from the HTTP response.
     *
     * @param url The URL to fetch the HTTP response from.
     * @return The contents of the HTTP response.
     * @throws IOException Related to network and stream reading
     */
    public static String getResponseFromHttpUrl(URL url) throws IOException {
        HttpURLConnection urlConnection = (HttpURLConnection) url.openConnection();
        try {
            InputStream in = urlConnection.getInputStream();

            Scanner scanner = new Scanner(in);
            scanner.useDelimiter("\\A");

            boolean hasInput = scanner.hasNext();
            if (hasInput) {
                return scanner.next();
            } else {
                return null;
            }
        } finally {
            urlConnection.disconnect();
        }
    }

    public static String obtenerRutaImagen(String imagen) {
        return BASE_URL_IMAGEN + imagen;
    }
}