package io.github.zkhan93.pm;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import java.util.ArrayList;
import java.util.List;

import io.github.zkhan93.pm.adapter.MovieDetailAdapter;
import io.github.zkhan93.pm.api.ApiServiceGenerator;
import io.github.zkhan93.pm.api.MovieDbClient;
import io.github.zkhan93.pm.models.FavClickCallback;
import io.github.zkhan93.pm.models.Movie;
import io.github.zkhan93.pm.models.Review;
import io.github.zkhan93.pm.models.ReviewResult;
import io.github.zkhan93.pm.models.Trailer;
import io.github.zkhan93.pm.models.TrailerResult;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * A placeholder fragment containing a simple view.
 */
public class DetailActivityFragment extends Fragment implements FavClickCallback {

    private MovieDetailAdapter movieDetailAdapter;
    private RecyclerView mMovieDetailList;
    private MovieDbClient client;
    public static final String TAG = DetailActivityFragment.class.getSimpleName();
    private Callback<TrailerResult> trailerCallback;
    private Callback<ReviewResult> reviewCallback;
    private ArrayList<Trailer> trailers;
    private ArrayList<Review> reviews;
    private Movie movie;

    {
        trailerCallback = new Callback<TrailerResult>() {
            @Override
            public void onResponse(Call<TrailerResult> call, Response<TrailerResult> response) {
                if (response == null || !response.isSuccessful() || response.body() == null)
                    return;
                List<Trailer> trailers = response.body().getResults();
                movieDetailAdapter.setTrailers(trailers);
                DetailActivityFragment.this.trailers = (ArrayList<Trailer>) trailers;
            }

            @Override
            public void onFailure(Call<TrailerResult> call, Throwable t) {
                Log.e(TAG, "error fetching trailers :" + t.getLocalizedMessage());
            }
        };
        reviewCallback = new Callback<ReviewResult>() {
            @Override
            public void onResponse(Call<ReviewResult> call, Response<ReviewResult> response) {
                if (response == null || !response.isSuccessful() || response.body() == null)
                    return;
                List<Review> reviews = response.body().getResults();
                movieDetailAdapter.setReviews(reviews);
                DetailActivityFragment.this.reviews = (ArrayList<Review>) reviews;
            }

            @Override
            public void onFailure(Call<ReviewResult> call, Throwable t) {
                Log.e(TAG, "error fetching reviews :" + t.getLocalizedMessage());
            }
        };
    }

    public DetailActivityFragment() {
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_detail, container, false);

        mMovieDetailList = (RecyclerView) view.findViewById(R.id.movie_detail_list);
        mMovieDetailList.setLayoutManager(new LinearLayoutManager(getContext()));
        movieDetailAdapter = new MovieDetailAdapter(this);
        mMovieDetailList.setAdapter(movieDetailAdapter);
        client = ApiServiceGenerator.createClient(MovieDbClient.class);
        if (savedInstanceState != null) {
            trailers = savedInstanceState.getParcelableArrayList("trailers");
            reviews = savedInstanceState.getParcelableArrayList("reviews");
            movie = savedInstanceState.getParcelable("movie");
            movieDetailAdapter.setMovie(movie);
            movieDetailAdapter.setReviews(reviews);
            movieDetailAdapter.setTrailers(trailers);
        } else {
            Intent intent = getActivity().getIntent();
            if (intent != null)
                displayMovieFromIntent(intent);
        }
        return view;
    }

    public void displayMovieFromIntent(Intent intent) {
        if (intent != null) {
            Bundle bundle = intent.getExtras();
            if (bundle != null)
                movie = bundle.getParcelable("movie");
        }
        movieDetailAdapter.clear();
        if (movie == null)
            return;
        movieDetailAdapter.setMovie(movie);
        client.videos(movie.getId()).enqueue(trailerCallback);
        client.reviews(movie.getId()).enqueue(reviewCallback);
    }

    @Override
    public void onClick(int movieId) {
        Log.d(TAG, "movie clicked" + movieId);
    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putParcelable("movie", movie);
        outState.putParcelableArrayList("trailers", trailers);
        outState.putParcelableArrayList("reviews", reviews);
    }
}
