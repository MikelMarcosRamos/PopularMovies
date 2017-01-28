package com.example.android.popularmovies;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.example.android.popularmovies.data.Pelicula;
import com.example.android.popularmovies.utilities.NetworkUtils;
import com.squareup.picasso.Picasso;

/**
 * Adaptador que gestiona las llamadas a www.themoviedb.org
 * {@link TheMovieDbAdapter} pasa una lista de películas a
 * {@link android.support.v7.widget.RecyclerView}
 */
public class TheMovieDbAdapter extends RecyclerView.Adapter<TheMovieDbAdapter.TheMovieDbAdapteriewHolder> {

    private Pelicula[] mPeliculas;

    /*
     * An on-click handler that we've defined to make it easy for an Activity to interface with
     * our RecyclerView
     */
    private final TheMovieDbAdapterOnClickHandler mClickHandler;

    /**
     * The interface that receives onClick messages.
     */
    public interface TheMovieDbAdapterOnClickHandler {
        void onClick(Pelicula codPelicula);
    }

    /**
     * Creates a ForecastAdapter.
     *
     * @param clickHandler The on-click handler for this adapter. This single handler is called
     *                     when an item is clicked.
     */
    public TheMovieDbAdapter(TheMovieDbAdapterOnClickHandler clickHandler) {
        mClickHandler = clickHandler;
    }

    public class TheMovieDbAdapteriewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        public final ImageView mCartelPelicula;
        public Context mContext;

        public TheMovieDbAdapteriewHolder(View view) {
            super(view);
            this.mContext = view.getContext();
            this.mCartelPelicula = (ImageView) view.findViewById(R.id.iv_cartel_pelicula);
            view.setOnClickListener(this);
        }

        @Override
        public void onClick(View v) {
            int adapterPosition = getAdapterPosition();
            Pelicula pelicula = mPeliculas[adapterPosition];
            mClickHandler.onClick(pelicula);
        }
    }


    @Override
    public TheMovieDbAdapteriewHolder onCreateViewHolder(ViewGroup viewGroup, int viewType) {
        Context context = viewGroup.getContext();
        int layoutIdForListItem = R.layout.elemento_lista_peliculas;
        LayoutInflater inflater = LayoutInflater.from(context);
        boolean shouldAttachToParentImmediately = false;

        View view = inflater.inflate(layoutIdForListItem, viewGroup, shouldAttachToParentImmediately);
        return new TheMovieDbAdapteriewHolder(view);
    }


    @Override
    public void onBindViewHolder(TheMovieDbAdapteriewHolder theMovieDbAdapteriewHolder, int position) {
        Pelicula pelicula = this.mPeliculas[position];

        Picasso.with(theMovieDbAdapteriewHolder.mContext)
                .load(NetworkUtils.getRutaImagen(pelicula.getPoster()))
                .into(theMovieDbAdapteriewHolder.mCartelPelicula);
    }


    @Override
    public int getItemCount() {
        if (null == this.mPeliculas) return 0;
        return this.mPeliculas.length;
    }


    public void setDatosPeliculas(Pelicula[] pelicula) {
        this.mPeliculas = pelicula;
        notifyDataSetChanged();
    }

}
