package com.example.android.popularmovies.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.example.android.popularmovies.R;
import com.example.android.popularmovies.data.Responses;
import com.example.android.popularmovies.data.Video;
import com.example.android.popularmovies.utilities.RecyclerViewEmptySupport;

/**
 * Adaptador que gestiona las llamadas a www.themoviedb.org
 * {@link VideoAdapter} pasa una lista de películas a
 * {@link RecyclerViewEmptySupport}
 */
public class VideoAdapter extends RecyclerViewEmptySupport.Adapter<VideoAdapter.VideoAdapteriewHolder> {

    private Responses<Video> mVideos;

    /*
     * An on-click handler that we've defined to make it easy for an Activity to interface with
     * our RecyclerView
     */
    private final VideoAdapterOnClickHandler mClickHandler;

    /**
     * The interface that receives onClick messages.
     */
    public interface VideoAdapterOnClickHandler {
        void onClick(Video idVideo);
    }

    /**
     * Creates a ForecastAdapter.
     *
     * @param clickHandler The on-click handler for this adapter. This single handler is called
     *                     when an item is clicked.
     */
    public VideoAdapter(VideoAdapterOnClickHandler clickHandler) {
        mClickHandler = clickHandler;
    }

    public class VideoAdapteriewHolder extends RecyclerViewEmptySupport.ViewHolder implements View.OnClickListener {

        protected TextView mVideoNombre;

        public VideoAdapteriewHolder(View view) {
            super(view);
            view.setOnClickListener(this);
            this.mVideoNombre = (TextView) view.findViewById(R.id.video_nombre);
        }

        @Override
        public void onClick(View v) {
            int adapterPosition = getAdapterPosition();
            Video video = mVideos.getResults().get(adapterPosition);
            mClickHandler.onClick(video);
        }
    }


    @Override
    public VideoAdapteriewHolder onCreateViewHolder(ViewGroup viewGroup, int viewType) {
        Context context = viewGroup.getContext();
        int layoutIdForListItem = R.layout.elemento_lista_videos;
        LayoutInflater inflater = LayoutInflater.from(context);
        boolean shouldAttachToParentImmediately = false;

        View view = inflater.inflate(layoutIdForListItem, viewGroup, shouldAttachToParentImmediately);
        return new VideoAdapteriewHolder(view);
    }


    @Override
    public void onBindViewHolder(VideoAdapteriewHolder videoAdapteriewHolder, int position) {
        Video video = mVideos.getResults().get(position);

        videoAdapteriewHolder.mVideoNombre.setText(video.getName());
    }


    @Override
    public int getItemCount() {
        if (null == this.mVideos) return 0;
        return this.mVideos.getResults().size();
    }


    public void setVideos(Responses<Video> videos) {
        this.mVideos = videos;
        notifyDataSetChanged();
    }

}
