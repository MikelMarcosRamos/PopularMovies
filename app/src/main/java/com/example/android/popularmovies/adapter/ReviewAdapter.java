package com.example.android.popularmovies.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.example.android.popularmovies.R;
import com.example.android.popularmovies.data.Responses;
import com.example.android.popularmovies.data.Review;
import com.example.android.popularmovies.utilities.RecyclerViewEmptySupport;

/**
 * Adaptador que gestiona las llamadas a www.themoviedb.org
 * {@link ReviewAdapter} pasa una lista de películas a
 * {@link RecyclerViewEmptySupport}
 */
public class ReviewAdapter extends RecyclerViewEmptySupport.Adapter<ReviewAdapter.ReviewAdapterViewHolder> {

    private Responses<Review> mReviews;

    /*
     * An on-click handler that we've defined to make it easy for an Activity to interface with
     * our RecyclerView
     */
    private final ReviewAdapterOnClickHandler mClickHandler;

    /**
     * The interface that receives onClick messages.
     */
    public interface ReviewAdapterOnClickHandler {
        void onClick(Review idReview);
    }

    /**
     * Creates a ForecastAdapter.
     *
     * @param clickHandler The on-click handler for this adapter. This single handler is called
     *                     when an item is clicked.
     */
    public ReviewAdapter(ReviewAdapterOnClickHandler clickHandler) {
        mClickHandler = clickHandler;
    }

    public class ReviewAdapterViewHolder extends RecyclerViewEmptySupport.ViewHolder implements View.OnClickListener {
        public final TextView mAuthor;

        public ReviewAdapterViewHolder(View view) {
            super(view);
            this.mAuthor = (TextView) view.findViewById(R.id.tv_critica_autor);
            view.setOnClickListener(this);
        }

        @Override
        public void onClick(View v) {
            int adapterPosition = getAdapterPosition();
            Review review = mReviews.getResults().get(adapterPosition);
            mClickHandler.onClick(review);
        }
    }


    @Override
    public ReviewAdapterViewHolder onCreateViewHolder(ViewGroup viewGroup, int viewType) {
        Context context = viewGroup.getContext();
        int layoutIdForListItem = R.layout.elemento_lista_criticas;
        LayoutInflater inflater = LayoutInflater.from(context);
        boolean shouldAttachToParentImmediately = false;

        View view = inflater.inflate(layoutIdForListItem, viewGroup, shouldAttachToParentImmediately);
        return new ReviewAdapterViewHolder(view);
    }


    @Override
    public void onBindViewHolder(ReviewAdapterViewHolder reviewAdapterViewHolder, int position) {

        Review review = mReviews.getResults().get(position);
        reviewAdapterViewHolder.mAuthor.setText(review.getAuthor());
    }


    @Override
    public int getItemCount() {
        if (null == this.mReviews) return 0;
        return this.mReviews.getResults().size();
    }


    public void setCriticas(Responses<Review> reviews) {
        this.mReviews = reviews;
        notifyDataSetChanged();
    }

}
