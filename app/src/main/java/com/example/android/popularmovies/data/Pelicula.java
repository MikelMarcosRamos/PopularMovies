package com.example.android.popularmovies.data;


import android.os.Parcel;
import android.os.Parcelable;

/**
 * Created by android on 27/01/17.
 * Class to save films information
 */
public class Pelicula implements Parcelable {

    private String titulo;
    private String poster;
    private String fechaEstreno;
    private double mediaVotos;
    private String sinopsis;

    @Override
    public int describeContents() {
        return hashCode();
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(titulo);
        dest.writeString(poster);
        dest.writeString(fechaEstreno);
        dest.writeDouble(mediaVotos);
        dest.writeString(sinopsis);
    }

    /**
     * Constructor to load Parcelable object
     * @param p Parcelable object to load
     */
    public Pelicula(Parcel p) {
        titulo = p.readString();
        poster = p.readString();
        fechaEstreno = p.readString();
        mediaVotos = p.readDouble();
        sinopsis = p.readString();
    }

    /**
     * Public empty constructor
     */
    public Pelicula() {}

    /**
     * Class to create the Pelicula instance from the Parcel data
     */
    public static final Parcelable.Creator<Pelicula> CREATOR = new Parcelable.Creator<Pelicula>() {

        /**
         * Create a Pelicula object from the Parcel data
         * @param in Parcel object with the data to load
         * @return Pelicula with the data loaded
         */
        public Pelicula createFromParcel(Parcel in) {
            return new Pelicula(in);
        }

        /**
         * Create an array of Pelicula object to be loaded from a Parcelable array
         * @param size Size of the array to create
         * @return Empty array with the size of Parcelable objects to load
         */
        public Pelicula[] newArray(int size) {
            return new Pelicula[size];
        }
    };

    /**
     * Get the value of the property titulo
     * @return string with the value of titulo
     */
    public String getTitulo() {
        return titulo;
    }
    /**
     * Set the value to the titulo property
     * @param titulo value to store
     */
    public void setTitulo(String titulo) {
        this.titulo = titulo;
    }
    /**
     * Get the value of the property poster
     * @return string with the value of poster
     */
    public String getPoster() {
        return poster;
    }
    /**
     * Set the value to the poster property
     * @param poster value to store
     */
    public void setPoster(String poster) {
        this.poster = poster;
    }
    /**
     * Get the value of the property fechaEstreno
     * @return string with the value of fechaEstreno
     */
    public String getFechaEstreno() {
        return fechaEstreno;
    }
    /**
     * Set the value to the fechaEstreno property
     * @param fechaEstreno value to store
     */
    public void setFechaEstreno(String fechaEstreno) {
        this.fechaEstreno = fechaEstreno;
    }
    /**
     * Get the value of the property mediaVotos
     * @return string with the value of mediaVotos
     */
    public double getMediaVotos() {
        return mediaVotos;
    }
    /**
     * Set the value to the mediaVotos property
     * @param mediaVotos value to store
     */
    public void setMediaVotos(double mediaVotos) {
        this.mediaVotos = mediaVotos;
    }
    /**
     * Get the value of the property sinopsis
     * @return string with the value of sinopsis
     */
    public String getSinopsis() {
        return sinopsis;
    }
    /**
     * Set the value to the sinopsis property
     * @param sinopsis value to store
     */
    public void setSinopsis(String sinopsis) {
        this.sinopsis = sinopsis;
    }
}
