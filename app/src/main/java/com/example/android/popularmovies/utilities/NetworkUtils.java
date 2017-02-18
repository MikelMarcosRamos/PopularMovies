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


    private static Map<Integer, String> BUSQUEDAS = new HashMap<Integer, String>() {{
        put(R.id.accion_popular, "popular");
        put(R.id.accion_puntuacion, "top_rated");
    }} ;

    private final static String PARAMETRO_API = "api_key";
    private final static String PARAMETRO_IDIOMA = "language";
    private final static String PARAMETRO_PAGINA = "page";
    private final static String PARAMETRO_REGION = "region";

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