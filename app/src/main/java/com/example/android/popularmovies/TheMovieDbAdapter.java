package com.example.android.popularmovies;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.example.android.popularmovies.data.Movie;
import com.example.android.popularmovies.data.Responses;
import com.example.android.popularmovies.utilities.NetworkUtils;
import com.squareup.picasso.Picasso;

/**
 * Adaptador que gestiona las llamadas a www.themoviedb.org
 * {@link TheMovieDbAdapter} pasa una lista de películas a
 * {@link android.support.v7.widget.RecyclerView}
 */
public class TheMovieDbAdapter extends RecyclerView.Adapter<TheMovieDbAdapter.TheMovieDbAdapteriewHolder> {

    private Responses<Movie> mMovies;

    /*
     * An on-click handler that we've defined to make it easy for an Activity to interface with
     * our RecyclerView
     */
    private final TheMovieDbAdapterOnClickHandler mClickHandler;

    /**
     * The interface that receives onClick messages.
     */
    public interface TheMovieDbAdapterOnClickHandler {
        void onClick(Movie codMovie);
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
            Movie movie = mMovies.getResults().get(adapterPosition);
            mClickHandler.onClick(movie);
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
        Movie movie = mMovies.getResults().get(position);

        Picasso.with(theMovieDbAdapteriewHolder.mContext)
                .load(NetworkUtils.getRutaImagen(movie.getPoster()))
                .placeholder(R.mipmap.im_loading)
                .error(R.mipmap.ic_not_found)
                .into(theMovieDbAdapteriewHolder.mCartelPelicula);
    }


    @Override
    public int getItemCount() {
        if (null == this.mMovies) return 0;
        return this.mMovies.getResults().size();
    }


    public void setDatosPeliculas(Responses<Movie> movies) {
        this.mMovies = movies;
        notifyDataSetChanged();
    }

}
