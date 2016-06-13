package io.github.zkhan93.pm.models;

import android.os.Parcel;
import android.os.Parcelable;

import com.google.gson.annotations.SerializedName;

import java.util.Date;

/**
 * Model to store a movie detail.
 * Created by Zeeshan Khan on 4/7/2016.
 */
public class Movie implements Parcelable {
    private String title, overview;
    @SerializedName("poster_path")
    private String posterPath;
    @SerializedName("release_date")
    private Date releaseDate;
    @SerializedName("vote_average")
    private float rating;
    private boolean isFavorite;
    private int id;
    private static String POSTER_URL_PREFIX = "http://image.tmdb.org/t/p/w185";

    public Movie() {
    }

    private Movie(Parcel in) {
        this.title = in.readString();
        this.overview = in.readString();
        this.posterPath = in.readString();
        this.releaseDate = new Date(in.readLong());
        this.rating = in.readFloat();
        boolean[] flags = new boolean[1];
        in.readBooleanArray(flags);
        this.isFavorite = flags[0];
        id=in.readInt();
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getOverview() {
        return overview;
    }

    public void setOverview(String overview) {
        this.overview = overview;
    }

    public String getPosterPath() {
        return posterPath;
    }

    public void setPosterPath(String posterPath) {
        this.posterPath = posterPath;
    }

    public String getFullPosterPath() {
        return POSTER_URL_PREFIX + posterPath;
    }

    public Date getReleaseDate() {
        return releaseDate;
    }

    public void setReleaseDate(Date releaseDate) {
        this.releaseDate = releaseDate;
    }

    public float getRating() {
        return rating;
    }

    public void setRating(float rating) {
        this.rating = rating;
    }

    @Override
    public int describeContents() {
        return 1;
    }

    public int getId() {
        return id;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(title);
        dest.writeString(overview);
        dest.writeString(posterPath);
        dest.writeLong(releaseDate.getTime());
        dest.writeFloat(rating);
        dest.writeBooleanArray(new boolean[]{isFavorite});
        dest.writeInt(id);
    }

    public static final Creator<Movie> CREATOR = new Parcelable.Creator<Movie>() {
        @Override
        public Movie[] newArray(int size) {
            return new Movie[size];
        }

        @Override
        public Movie createFromParcel(Parcel source) {
            return new Movie(source);
        }

    };


}
