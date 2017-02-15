package com.example.android.popularmovies.data;


import android.os.Parcel;
import android.os.Parcelable;

/**
 * Created by android on 14/02/17.
 * Class to save video information
 */
public class Video implements Parcelable {

    protected String id;
    protected String iso_639_1;
    protected String iso_3166_1;
    protected String key;
    protected String name;
    protected String site;
    protected int size;
    protected String type;

    @Override
    public int describeContents() {
        return hashCode();
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(id);
        dest.writeString(iso_639_1);
        dest.writeString(iso_3166_1);
        dest.writeString(key);
        dest.writeString(name);
        dest.writeString(site);
        dest.writeInt(size);
        dest.writeString(type);
    }

    /**
     * Constructor to load Parcelable object
     * @param p Parcelable object to load
     */
    public Video(Parcel p) {
        id = p.readString();
        iso_639_1 = p.readString();
        iso_3166_1 = p.readString();
        key = p.readString();
        name = p.readString();
        site = p.readString();
        size = p.readInt();
        type = p.readString();
    }

    /**
     * Public empty constructor
     */
    public Video() {}

    /**
     * Class to create the Movie instance from the Parcel data
     */
    public static final Creator<Video> CREATOR = new Creator<Video>() {

        /**
         * Create a Movie object from the Parcel data
         * @param in Parcel object with the data to load
         * @return Movie with the data loaded
         */
        public Video createFromParcel(Parcel in) {
            return new Video(in);
        }

        /**
         * Create an array of Movie object to be loaded from a Parcelable array
         * @param size Size of the array to create
         * @return Empty array with the size of Parcelable objects to load
         */
        public Video[] newArray(int size) {
            return new Video[size];
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
     * Get the value of the property key
     * @return string with the value of key
     */
    public String getKey() {
        return key;
    }
    /**
     * Set the value to the key property
     * @param key value to store
     */
    public void setKey(String key) {
        this.key = key;
    }

    /**
     * Get the value of the property name
     * @return string with the value of name
     */
    public String getName() {
        return name;
    }
    /**
     * Set the value to the key property
     * @param name value to store
     */
    public void setName(String name) {
        this.name = name;
    }

}
