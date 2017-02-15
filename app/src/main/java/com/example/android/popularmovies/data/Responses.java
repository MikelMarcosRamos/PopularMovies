package com.example.android.popularmovies.data;

import android.os.Parcel;
import android.os.Parcelable;

import java.util.ArrayList;
import java.util.List;

public class Responses<T> implements Parcelable {
    protected Integer page;
    protected List<T> results;
    protected Integer total_results;
    protected Integer total_pages;

    public void setResults(List<T> movies){
        this.results = movies;
    }

    public List<T> getResults(){
        return this.results;
    }

    @Override
    public int describeContents() {
        return hashCode();
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeInt(page);
        dest.writeList(results);
        dest.writeInt(total_results);
        dest.writeInt(total_pages);
    }

    /**
     * Constructor to load Parcelable object
     * @param p Parcelable object to load
     */
    public Responses(Parcel p) {
        page = p.readInt();
        results = new ArrayList<>();
        p.readList(results, null);
        total_results = p.readInt();
        total_pages = p.readInt();
    }

    /**
     * Public empty constructor
     */
    public Responses() {}

    /**
     * Class to create the Movie instance from the Parcel data
     */
    public static final Creator<Review> CREATOR = new Creator<Review>() {

        /**
         * Create a Movie object from the Parcel data
         * @param in Parcel object with the data to load
         * @return Movie with the data loaded
         */
        public Review createFromParcel(Parcel in) {
            return new Review(in);
        }

        /**
         * Create an array of Movie object to be loaded from a Parcelable array
         * @param size Size of the array to create
         * @return Empty array with the size of Parcelable objects to load
         */
        public Review[] newArray(int size) {
            return new Review[size];
        }
    };

}

