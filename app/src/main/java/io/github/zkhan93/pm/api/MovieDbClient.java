package io.github.zkhan93.pm.api;

import io.github.zkhan93.pm.models.MovieResult;
import io.github.zkhan93.pm.models.ReviewResult;
import io.github.zkhan93.pm.models.TrailerResult;
import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Path;

/**
 * Created by Zeeshan Khan on 6/12/2016.
 */
public interface MovieDbClient {
    @GET("popular")
    Call<MovieResult> popular();

    @GET("top_rated")
    Call<MovieResult> topRated();

    @GET("{id}/videos")
    Call<TrailerResult> videos(@Path("id") int id);

    @GET("{id}/reviews")
    Call<ReviewResult> reviews(@Path("id") int id);
}
