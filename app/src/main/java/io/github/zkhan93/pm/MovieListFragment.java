package io.github.zkhan93.pm;

import android.content.Intent;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.v4.app.Fragment;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import java.util.ArrayList;
import java.util.List;

import io.github.zkhan93.pm.adapter.MovieListAdapter;
import io.github.zkhan93.pm.api.ApiServiceGenerator;
import io.github.zkhan93.pm.api.MovieDbClient;
import io.github.zkhan93.pm.models.Movie;
import io.github.zkhan93.pm.models.MovieResult;
import retrofit2.Call;
import retrofit2.Response;

/**
 * A placeholder fragment containing a simple view.
 */
public class MovieListFragment extends Fragment {
    private RecyclerView movieListView;
    private MovieListAdapter movieAdapter;
    ArrayList<Movie> movies;
    private String sortOrder;
    private MovieDbClient mMovieDbClient;
    private retrofit2.Callback<MovieResult> clientCallback;
    public static final String TAG = MovieListFragment.class.getSimpleName();

    {
        clientCallback = new retrofit2.Callback<MovieResult>() {
            @Override
            public void onResponse(Call<MovieResult> call, Response<MovieResult> response) {
                if (response == null || !response.isSuccessful() || response.body() == null) {
                    Log.d(TAG, "no response");
                    return;
                }
                List<Movie> movies = response.body().getResults();
                if (movies == null || movies.size() == 0) {
                    Log.d(TAG, "movie list is empty");
                    return;
                }
                if (movieAdapter != null)
                    movieAdapter.addAll(movies);
                MovieListFragment.this.movies = (ArrayList) movies;
            }

            @Override
            public void onFailure(Call<MovieResult> call, Throwable t) {
                Log.e(TAG, "error fetching movies url:" + call.request().url() + " error:" + t
                        .getLocalizedMessage());
            }
        };
    }

    public MovieListFragment() {
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_movie_list, container, false);
        movieListView = (RecyclerView) rootView.findViewById(R.id.movie_list);
        movieListView.setLayoutManager(new GridLayoutManager(getActivity(), 2));
        movieAdapter = new MovieListAdapter(new ArrayList<Movie>(), new OnMovieClickListener() {
            @Override
            public void onClick(Movie movie) {
                Bundle bundle = new Bundle();
                bundle.putParcelable("movie", movie);
                Intent intent = new Intent(getActivity(), DetailActivity.class);
                intent.putExtras(bundle);
                ((Callback) getActivity()).OnItemSelected(intent);

            }
        });
        movieListView.setAdapter(movieAdapter);
        sortOrder = PreferenceManager.getDefaultSharedPreferences(getActivity()).getString
                (getString(R.string
                        .pref_sort_order_key), "1");
        //initializing retrofit client
        mMovieDbClient = ApiServiceGenerator.createClient(MovieDbClient.class);
        if (savedInstanceState != null) {
            this.movies = savedInstanceState.getParcelableArrayList("movies");
            movieAdapter.addAll(movies);
        } else {
            if (sortOrder.equals("1")) {
                mMovieDbClient.popular().enqueue(clientCallback);
            } else {
                mMovieDbClient.topRated().enqueue(clientCallback);
            }
        }
        return rootView;
    }

    @Override
    public void onStart() {
        super.onStart();

    }

    @Override
    public void onResume() {
        super.onResume();
        String newSortOrder = PreferenceManager.getDefaultSharedPreferences(getActivity())
                .getString(getString(R.string
                        .pref_sort_order_key), "1");
        if (!sortOrder.equals(newSortOrder)) {
            if (newSortOrder.equals("1")) {
                mMovieDbClient.popular().enqueue(clientCallback);
            } else {
                mMovieDbClient.topRated().enqueue(clientCallback);
            }
            sortOrder = newSortOrder;
        }
    }


    @Override
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        if (movies != null && movies.size() > 0) {
            outState.putParcelableArrayList("movies", movies);
        }
    }

    public interface Callback {
        void OnItemSelected(Intent intent);
    }

    public interface OnMovieClickListener {
        void onClick(Movie movie);
    }
}
