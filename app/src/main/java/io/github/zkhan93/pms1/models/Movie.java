package io.github.zkhan93.pms1.models;

import java.util.Date;

/**
 * Model to store a movie detail.
 * Created by Zeeshan Khan on 4/7/2016.
 */
public class Movie {
    private String title, overview, posterPath;
    private Date releaseDate;
    private float rating;
    private static String POSTER_URL_PREFIX = "http://image.tmdb.org/t/p/w185";

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
        this.posterPath = POSTER_URL_PREFIX + posterPath;
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
}
