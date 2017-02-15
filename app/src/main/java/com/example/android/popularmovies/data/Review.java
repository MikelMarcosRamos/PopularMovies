package com.example.android.popularmovies.data;


import android.os.Parcel;
import android.os.Parcelable;

/**
 * Created by android on 14/02/17.
 * Class to save review information
 */
public class Review implements Parcelable {

    protected String id;
    protected String author;
    protected String content;
    protected String url;

    @Override
    public int describeContents() {
        return hashCode();
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(id);
        dest.writeString(author);
        dest.writeString(content);
        dest.writeString(url);
    }

    /**
     * Constructor to load Parcelable object
     * @param p Parcelable object to load
     */
    public Review(Parcel p) {
        id = p.readString();
        author = p.readString();
        content = p.readString();
        url = p.readString();
    }

    /**
     * Public empty constructor
     */
    public Review() {}

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

    /**
     * Get the value of the property id
     * @return string with the value of id
     */
    public String getId() {
        return id;
    }
    /**
     * Set the value to the id property
     * @param id value to store
     */
    public void setId(String id) {
        this.id = id;
    }

    /**
     * Get the value of the property author
     * @return string with the value of author
     */
    public String getAuthor() {
        return author;
    }
    /**
     * Set the value to the author property
     * @param author value to store
     */
    public void setAuthor(String author) {
        this.author = author;
    }

    /**
     * Get the value of the property content
     * @return string with the value of content
     */
    public String getContent() {
        return content;
    }
    /**
     * Set the value to the content property
     * @param content value to store
     */
    public void setContent(String content) {
        this.content = content;
    }

    /**
     * Get the value of the property url
     * @return string with the value of url
     */
    public String getUrl() {
        return url;
    }
    /**
     * Set the value to the url property
     * @param url value to store
     */
    public void setUrl(String url) {
        this.url = url;
    }

}
