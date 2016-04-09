package io.github.zkhan93.pm;

import android.content.Intent;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import io.github.zkhan93.pm.adapter.MovieListAdapter;
import io.github.zkhan93.pm.models.Movie;
import io.github.zkhan93.pm.models.OnMovieClickListener;
import io.github.zkhan93.pm.util.Constants;

/**
 * A placeholder fragment containing a simple view.
 */
public class MovieListFragment extends Fragment {
    public static final String TAG = MovieListFragment.class.getSimpleName();
    private RecyclerView movieListView;
    private MovieListAdapter movieAdapter;

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
                bundle.putString("movie", movie.getTitle());
                Intent intent = new Intent(getActivity(), DetailActivity.class);
                intent.putExtras(bundle);
                startActivity(intent);
            }
        });
        if (savedInstanceState != null) {
            //TODO:add Movies to movieAdapter from savedInstanceState
            //rotation or configuration change
        }
        movieListView.setAdapter(movieAdapter);

        return rootView;
    }

    @Override
    public void onStart() {
        super.onStart();
        new FetchMovies().execute();
    }

    private class FetchMovies extends AsyncTask<Void, Void, List<Movie>> {
        @Override
        protected List<Movie> doInBackground(Void... params) {
            try {
                URL url = new URL(Uri.parse(Constants.URL.POP_MOVIES).buildUpon()
                        .appendQueryParameter(Constants.PARAMS.API_KEY, Constants.VALUES.API_KEY)
                        .toString());

                HttpURLConnection connection = (HttpURLConnection) url.openConnection();
                InputStream is = connection.getInputStream();
                BufferedReader br = new BufferedReader(new InputStreamReader(is));
                String line = null;
                StringBuffer buffer = new StringBuffer();
                while ((line = br.readLine()) != null) {
                    buffer.append(line + "\n");
                }
                JSONObject response = new JSONObject(buffer.toString());
                if (response != null) {
                    JSONArray movieArray = response.optJSONArray(Constants.JSON_KEYS.RESULT);
                    if (movieArray == null)
                        return null;
                    Movie movie;
                    JSONObject jMovie;
                    List<Movie> movieList = new ArrayList<>();
                    for (int i = 0; i < movieArray.length(); i++) {
                        movie = new Movie();
                        jMovie = movieArray.optJSONObject(i);
                        if (jMovie == null)
                            continue;
                        movie.setTitle(jMovie.optString(Constants.JSON_KEYS.MOVIE.TITLE));
                        movie.setOverview(jMovie.optString(Constants.JSON_KEYS.MOVIE.OVERVIEW));
                        movie.setPosterPath(jMovie.optString(Constants.JSON_KEYS.MOVIE
                                .POSTER_PATH));
                        movie.setReleaseDate(new Date(jMovie.optLong(Constants.JSON_KEYS.MOVIE
                                .RELEASE_DATE)));
                        movie.setRating((float) jMovie.optDouble(Constants.JSON_KEYS.MOVIE
                                .VOTE_AVERAGE));
                        movieList.add(movie);
                    }
                    return movieList;
                }
                //TODO: parse the data to json and populate adapter with data
            } catch (MalformedURLException urlEx) {
                Log.e(TAG, "could not parse url " + urlEx);
            } catch (IOException ioEx) {
                Log.e(TAG, "could not open connection " + ioEx);
            } catch (JSONException jex) {
                Log.e(TAG, "could not parse data to JSON" + jex);
            }
            return null;
        }

        @Override
        protected void onPostExecute(List<Movie> movies) {
            if (movies == null)
                return;

            if (movieAdapter != null)
                movieAdapter.addAll(movies);
        }
    }

}
