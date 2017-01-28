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
import com.example.android.popularmovies.data.TheMovieDbPreferencias;

import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.HashMap;
import java.util.Map;
import java.util.Scanner;

/**
 * These utilities will be used to communicate with the movie db server.
 */
public final class NetworkUtils {

    private static final String TAG = NetworkUtils.class.getSimpleName();

    private static final String BASE_URL_BUSCAR =
            "https://api.themoviedb.org/3/movie/";

    private static final String BASE_URL_IMAGEN =
            "https://image.tmdb.org/t/p/w500/";


    private static final String format = "json";

    private static Map<Integer, String> BUSQUEDAS = new HashMap<Integer, String>() {{
        put(R.id.accion_popular, "popular");
        put(R.id.accion_puntuacion, "top_rated");
    }} ;

    private final static String PARAMETRO_API = "api_key";
    private final static String PARAMETRO_IDIOMA = "language";
    private final static String PARAMETRO_PAGINA = "page";
    private final static String PARAMETRO_REGION = "region";




    /**
     * Build the url of the request for the movie db server.
     *
     * @param tipoBusqueda with the type of search to do.
     * @return URL for the request.
     */
    public static URL buildUrl(Context context, Integer tipoBusqueda) {
        String etiqueta = BUSQUEDAS.get(tipoBusqueda);
        Uri builtUri = Uri.parse(BASE_URL_BUSCAR + etiqueta).buildUpon()
                .appendQueryParameter(PARAMETRO_API, context.getString(R.string.api_key))
                .appendQueryParameter(PARAMETRO_IDIOMA, TheMovieDbPreferencias.VALOR_IDIOMA)
                .appendQueryParameter(PARAMETRO_PAGINA, Integer.toString(TheMovieDbPreferencias.VALOR_PAGINA))
                .appendQueryParameter(PARAMETRO_REGION, TheMovieDbPreferencias.VALOR_REGION)
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

    /**
     * Build the url of the image for the movie db server.
     *
     * @param imagen with the image path.
     * @return String for the request.
     */
    public static String getRutaImagen(String imagen) {
        return BASE_URL_IMAGEN + imagen;
    }
}