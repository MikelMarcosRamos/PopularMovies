package com.example.android.popularmovies.data;


import android.os.Parcel;
import android.os.Parcelable;

import com.example.android.popularmovies.VideoAdapter;
import com.example.android.popularmovies.utilities.ClienteRest;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * Created by android on 27/01/17.
 * Class to save films information
 */
public class Movie implements Parcelable {

    protected String poster_path;
    protected boolean adult;
    protected String overview;
    protected String release_date;
    protected int[] genre_ids;
    protected int id;
    protected String original_title;
    protected String original_language;
    protected String title;
    protected String backdrop_path;
    protected double popularity;
    protected int vote_count;
    protected boolean video;
    protected double vote_average;

    protected Responses<Video> mVideos;
    protected Responses<Review> mCriticas;

    @Override
    public int describeContents() {
        return hashCode();
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(poster_path);
        dest.writeByte((byte) (adult ? 1 : 0));
        dest.writeString(overview);
        dest.writeString(release_date);
        dest.writeInt( (genre_ids != null)?genre_ids.length : 0);
        if (genre_ids != null) {
            dest.writeIntArray(genre_ids);
        }
        dest.writeInt(id);
        dest.writeString(original_title);
        dest.writeString(original_language);
        dest.writeString(title);
        dest.writeString(backdrop_path);
        dest.writeDouble(popularity);
        dest.writeInt(vote_count);
        dest.writeByte((byte) (video ? 1 : 0));
        dest.writeDouble(vote_average);
        dest.writeParcelable(mVideos, 0);
        dest.writeParcelable(mCriticas, 0);
    }

    /**
     * Constructor to load Parcelable object
     * @param p Parcelable object to load
     */
    public Movie(Parcel p) {
        poster_path = p.readString();
        adult = p.readByte() != 0;
        overview = p.readString();
        release_date = p.readString();
        genre_ids = new int[p.readInt()];
        if (genre_ids.length > 0) {
            p.readIntArray(genre_ids);
        }
        id = p.readInt();
        original_title = p.readString();
        original_language = p.readString();
        title = p.readString();
        backdrop_path = p.readString();
        popularity = p.readDouble();
        vote_count = p.readInt();
        video = p.readByte() != 0;
        vote_average = p.readDouble();
        mVideos = p.readParcelable(Responses.class.getClassLoader());
        mCriticas = p.readParcelable(Responses.class.getClassLoader());

    }

    /**
     * Public empty constructor
     */
    public Movie() {}

    /**
     * Class to create the Movie instance from the Parcel data
     */
    public static final Parcelable.Creator<Movie> CREATOR = new Parcelable.Creator<Movie>() {

        /**
         * Create a Movie object from the Parcel data
         * @param in Parcel object with the data to load
         * @return Movie with the data loaded
         */
        public Movie createFromParcel(Parcel in) {
            return new Movie(in);
        }

        /**
         * Create an array of Movie object to be loaded from a Parcelable array
         * @param size Size of the array to create
         * @return Empty array with the size of Parcelable objects to load
         */
        public Movie[] newArray(int size) {
            return new Movie[size];
        }
    };

    /**
     * Get the value of the property id
     * @return int with the value of id
     */
    public int getId() {
        return id;
    }
    /**
     * Set the value to the id property
     * @param id value to store
     */
    public void setId(int id) {
        this.id = id;
    }


    /**
     * Get the value of the property titulo
     * @return string with the value of titulo
     */
    public String getTitulo() {
        return title;
    }
    /**
     * Set the value to the titulo property
     * @param titulo value to store
     */
    public void setTitulo(String titulo) {
        this.title = titulo;
    }
    /**
     * Get the value of the property poster
     * @return string with the value of poster
     */
    public String getPoster() {
        return poster_path;
    }
    /**
     * Set the value to the poster property
     * @param poster value to store
     */
    public void setPoster(String poster) {
        this.poster_path = poster;
    }
    /**
     * Get the value of the property fechaEstreno
     * @return string with the value of fechaEstreno
     */
    public String getFechaEstreno() {
        return release_date;
    }
    /**
     * Set the value to the fechaEstreno property
     * @param fechaEstreno value to store
     */
    public void setFechaEstreno(String fechaEstreno) {
        this.release_date = fechaEstreno;
    }
    /**
     * Get the value of the property mediaVotos
     * @return string with the value of mediaVotos
     */
    public double getMediaVotos() {
        return vote_average;
    }
    /**
     * Set the value to the mediaVotos property
     * @param mediaVotos value to store
     */
    public void setMediaVotos(double mediaVotos) {
        this.vote_average = mediaVotos;
    }
    /**
     * Get the value of the property sinopsis
     * @return string with the value of sinopsis
     */
    public String getSinopsis() {
        return overview;
    }
    /**
     * Set the value to the sinopsis property
     * @param sinopsis value to store
     */
    public void setSinopsis(String sinopsis) {
        this.overview = sinopsis;
    }

}
