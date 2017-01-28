package com.example.android.popularmovies.data;


import android.os.Parcel;
import android.os.Parcelable;

/**
 * Created by android on 27/01/17.
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

    public Pelicula(Parcel p) {
        titulo = p.readString();
        poster = p.readString();
        fechaEstreno = p.readString();
        mediaVotos = p.readDouble();
        sinopsis = p.readString();
    }

    public Pelicula() {}

    public static final Parcelable.Creator<Pelicula> CREATOR = new Parcelable.Creator<Pelicula>() {

        public Pelicula createFromParcel(Parcel in) {
            return new Pelicula(in);
        }

        public Pelicula[] newArray(int size) {
            return new Pelicula[size];
        }
    };

    public String getTitulo() {
        return titulo;
    }

    public void setTitulo(String titulo) {
        this.titulo = titulo;
    }

    public String getPoster() {
        return poster;
    }

    public void setPoster(String poster) {
        this.poster = poster;
    }

    public String getFechaEstreno() {
        return fechaEstreno;
    }

    public void setFechaEstreno(String fechaEstreno) {
        this.fechaEstreno = fechaEstreno;
    }

    public double getMediaVotos() {
        return mediaVotos;
    }

    public void setMediaVotos(double mediaVotos) {
        this.mediaVotos = mediaVotos;
    }

    public String getSinopsis() {
        return sinopsis;
    }

    public void setSinopsis(String sinopsis) {
        this.sinopsis = sinopsis;
    }
}
