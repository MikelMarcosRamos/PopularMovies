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
package com.example.android.popularmovies.data;

import com.example.android.popularmovies.R;

/**
 * Class to store prefered values to search
 */
public class TheMovieDbPreferencias {

    public final static String VALOR_IDIOMA = "en-EN";
    public final static Integer VALOR_PAGINA = 1;
    public final static String VALOR_REGION = "Spain";

    /**
     * Get the default list to show
     * @return id of the list to show by default
     */
    public static Integer getListaPorDefecto() {
        return R.id.accion_popular;
    }
}