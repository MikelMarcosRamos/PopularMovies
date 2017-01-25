package com.example.android.popularmovies;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.example.android.popularmovies.utilities.NetworkUtils;
import com.squareup.picasso.Picasso;

/**
 * Adaptador que gestiona las llamadas a www.themoviedb.org
 * {@link TheMovieDbAdapter} pasa una lista de películas a
 * {@link android.support.v7.widget.RecyclerView}
 */
public class TheMovieDbAdapter extends RecyclerView.Adapter<TheMovieDbAdapter.TheMovieDbAdapteriewHolder> {

    private String[] mDatosPeliculas;

    /*
     * An on-click handler that we've defined to make it easy for an Activity to interface with
     * our RecyclerView
     */
    private final TheMovieDbAdapterOnClickHandler mClickHandler;

    /**
     * The interface that receives onClick messages.
     */
    public interface TheMovieDbAdapterOnClickHandler {
        void onClick(String codPelicula);
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

        /**
         * This gets called by the child views during a click.
         *
         * @param v The View that was clicked
         */
        @Override
        public void onClick(View v) {
            int adapterPosition = getAdapterPosition();
            String weatherForDay = mDatosPeliculas[adapterPosition];
            mClickHandler.onClick(weatherForDay);
        }
    }

    /**
     * This gets called when each new ViewHolder is created. This happens when the RecyclerView
     * is laid out. Enough ViewHolders will be created to fill the screen and allow for scrolling.
     *
     * @param viewGroup The ViewGroup that these ViewHolders are contained within.
     * @param viewType  If your RecyclerView has more than one type of item (which ours doesn't) you
     *                  can use this viewType integer to provide a different layout. See
     *                  {@link android.support.v7.widget.RecyclerView.Adapter#getItemViewType(int)}
     *                  for more details.
     * @return A new TheMovieDbAdapteriewHolder that holds the View for each list item
     */
    @Override
    public TheMovieDbAdapteriewHolder onCreateViewHolder(ViewGroup viewGroup, int viewType) {
        Context context = viewGroup.getContext();
        int layoutIdForListItem = R.layout.elemento_lista_peliculas;
        LayoutInflater inflater = LayoutInflater.from(context);
        boolean shouldAttachToParentImmediately = false;

        View view = inflater.inflate(layoutIdForListItem, viewGroup, shouldAttachToParentImmediately);
        return new TheMovieDbAdapteriewHolder(view);
    }

    /**
     * OnBindViewHolder is called by the RecyclerView to display the data at the specified
     * position. In this method, we update the contents of the ViewHolder to display the weather
     * details for this particular position, using the "position" argument that is conveniently
     * passed into us.
     *
     * @param theMovieDbAdapteriewHolder The ViewHolder which should be updated to represent the
     *                                  contents of the item at the given position in the data set.
     * @param position                  The position of the item within the adapter's data set.
     */
    @Override
    public void onBindViewHolder(TheMovieDbAdapteriewHolder theMovieDbAdapteriewHolder, int position) {
        String weatherForThisDay = this.mDatosPeliculas[position];
        Log.v("prueba", weatherForThisDay);
        Picasso.with(theMovieDbAdapteriewHolder.mContext)
                .load(NetworkUtils.obtenerRutaImagen(weatherForThisDay))
                .into(theMovieDbAdapteriewHolder.mCartelPelicula);
    }

    /**
     * This method simply returns the number of items to display. It is used behind the scenes
     * to help layout our Views and for animations.
     *
     * @return The number of items available in our forecast
     */
    @Override
    public int getItemCount() {
        if (null == this.mDatosPeliculas) return 0;
        return this.mDatosPeliculas.length;
    }

    /**
     * This method is used to set the weather forecast on a ForecastAdapter if we've already
     * created one. This is handy when we get new data from the web but don't want to create a
     * new ForecastAdapter to display it.
     *
     * @param postersPeliculas The new weather data to be displayed.
     */
    public void establecerDatosPeliculas(String[] postersPeliculas) {
        this.mDatosPeliculas = postersPeliculas;
        notifyDataSetChanged();
    }

}
