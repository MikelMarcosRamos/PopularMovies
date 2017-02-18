package com.example.android.popularmovies;

import android.content.Context;
import android.database.Cursor;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.example.android.popularmovies.data.Movie;
import com.example.android.popularmovies.data.MovieContract;
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
    private Cursor mCursor;

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
            mClickHandler.onClick(getMovie(adapterPosition));
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
        Picasso.with(theMovieDbAdapteriewHolder.mContext)
                .load(NetworkUtils.getRutaImagen(this.getMovie(position).getPoster()))
                .placeholder(R.mipmap.im_loading)
                .error(R.mipmap.ic_not_found)
                .into(theMovieDbAdapteriewHolder.mCartelPelicula);
    }

    protected Movie getMovie(int position) {
        Movie movie;
        if (this.mMovies != null) {
            movie = mMovies.getResults().get(position);
        } else if (this.mCursor != null) {
            movie = new Movie();
            int idIndex = mCursor.getColumnIndex(MovieContract.MovieEntry.COLUMN_ID);
            int idTitulo = mCursor.getColumnIndex(MovieContract.MovieEntry.COLUMN_TITULO);
            int idPoster = mCursor.getColumnIndex(MovieContract.MovieEntry.COLUMN_POSTER);
            int idFechaEstreno = mCursor.getColumnIndex(MovieContract.MovieEntry.COLUMN_FECHA_ESTRENO);
            int idMediaVotos = mCursor.getColumnIndex(MovieContract.MovieEntry.COLUMN_MEDIA_VOTOS);
            int idSinopsis = mCursor.getColumnIndex(MovieContract.MovieEntry.COLUMN_SINOPSIS);

            mCursor.moveToPosition(position); // get to the right location in the cursor

            // Determine the values of the wanted data
            movie.setId(mCursor.getInt(idIndex));
            movie.setTitulo(mCursor.getString(idTitulo));
            movie.setPoster(mCursor.getString(idPoster));
            movie.setFechaEstreno(mCursor.getString(idFechaEstreno));
            movie.setMediaVotos(mCursor.getDouble(idMediaVotos));
            movie.setSinopsis(mCursor.getString(idSinopsis));
        } else {
            throw new UnsupportedOperationException();
        }
        return movie;
    }


    @Override
    public int getItemCount() {
        if (null == this.mMovies) {
            if (null == this.mCursor) {
                return 0;
            } else {
                return mCursor.getCount();
            }
        } else {
            return this.mMovies.getResults().size();
        }
    }


    public void setDatosPeliculas(Responses<Movie> movies) {
        if (this.mMovies != movies) {
            this.mCursor = null;
            this.mMovies = movies;
            if (this.mMovies != null) {
                notifyDataSetChanged();
            }
        }
    }

    public void swapSource(Cursor cursor) {
        if (mCursor != cursor) {
            this.mMovies = null;
            this.mCursor = cursor;
            if (this.mCursor != null) {
                this.notifyDataSetChanged();
            }
        }
    }

}
