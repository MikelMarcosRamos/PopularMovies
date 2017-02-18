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

/**
 * These utilities will be used to communicate with the movie db server.
 */
public final class NetworkUtils {

    private static final String BASE_URL_IMAGEN =
            "https://image.tmdb.org/t/p/w500/";

    private NetworkUtils(){}

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