package com.example.android.popularmovies;

import android.content.Intent;
import android.databinding.DataBindingUtil;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.android.popularmovies.data.Movie;
import com.example.android.popularmovies.databinding.ActivityDetallesPeliculaBinding;
import com.example.android.popularmovies.utilities.NetworkUtils;
import com.squareup.picasso.Picasso;

/**
 * Activity to show the film details
 */
public class DetallesPelicula extends AppCompatActivity {

    private ActivityDetallesPeliculaBinding mBinding;

    private Movie mMovie;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detalles_pelicula);

        mBinding = DataBindingUtil.setContentView(this, R.layout.activity_detalles_pelicula);

        this.cargarDetalles();
    }

    protected void cargarDetalles() {
        Intent intent = getIntent();
        if (intent.hasExtra("movie") && mBinding != null) {
            this.mMovie = intent.getParcelableExtra("movie");
            this.mBinding.peliculaTitulo.setText(this.mMovie.getTitulo());
            Picasso.with(this)
                    .load(NetworkUtils.getRutaImagen(this.mMovie.getPoster()))
                    .placeholder(R.mipmap.im_loading)
                    .error(R.mipmap.ic_not_found)
                    .into(this.mBinding.peliculaPoster);
            this.mBinding.peliculaFechaEstreno.setText(this.mMovie.getFechaEstreno());
            this.mBinding.peliculaMediaVotos.setText(Double.toString(this.mMovie.getMediaVotos()));
            this.mBinding.peliculaSinopsis.setText(this.mMovie.getSinopsis());
        }
    }
}
