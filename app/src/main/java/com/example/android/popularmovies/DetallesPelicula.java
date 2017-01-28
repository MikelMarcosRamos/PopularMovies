package com.example.android.popularmovies;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.android.popularmovies.data.Pelicula;
import com.example.android.popularmovies.utilities.NetworkUtils;
import com.squareup.picasso.Picasso;

/**
 * Activity to show the film details
 */
public class DetallesPelicula extends AppCompatActivity {

    private TextView mTitulo;
    private ImageView mPoster;
    private TextView mFechaEstreno;
    private TextView mMediaVotos;
    private TextView mSinopsis;

    private Pelicula mPelicula;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detalles_pelicula);

        this.mTitulo = (TextView) this.findViewById(R.id.pelicula_titulo);
        this.mPoster = (ImageView) this.findViewById(R.id.pelicula_poster);
        this.mFechaEstreno = (TextView) this.findViewById(R.id.pelicula_fecha_estreno);
        this.mMediaVotos = (TextView) this.findViewById(R.id.pelicula_media_votos);
        this.mSinopsis = (TextView) this.findViewById(R.id.pelicula_sinopsis);

        Intent intent = getIntent();
        if (intent.hasExtra("pelicula")) {
            this.mPelicula = intent.getParcelableExtra("pelicula");
            this.mTitulo.setText(this.mPelicula.getTitulo());
            Picasso.with(this)
                    .load(NetworkUtils.getRutaImagen(this.mPelicula.getPoster()))
                    .into(this.mPoster);
            this.mFechaEstreno.setText(this.mPelicula.getFechaEstreno());
            this.mMediaVotos.setText(Double.toString(this.mPelicula.getMediaVotos()));
            this.mSinopsis.setText(this.mPelicula.getSinopsis());
        }
    }
}
