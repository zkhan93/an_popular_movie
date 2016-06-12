package io.github.zkhan93.pm;

import android.content.Intent;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

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
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
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
    ArrayList<Movie> movies;
    private String sortOrder = "1";
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
                ((Callback)getActivity()).OnItemSelected(intent);

            }
        });
        movieListView.setAdapter(movieAdapter);
        sortOrder = PreferenceManager.getDefaultSharedPreferences(getActivity()).getString
                (getString(R.string
                        .pref_sort_order_key), "1");
        if (savedInstanceState != null) {
            this.movies = savedInstanceState.getParcelableArrayList("movies");
            movieAdapter.addAll(movies);
        } else {
            new FetchMovies().execute();
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
            new FetchMovies().execute();
            sortOrder = newSortOrder;
        }
    }

    private class FetchMovies extends AsyncTask<Void, Void, List<Movie>> {
        @Override
        protected List<Movie> doInBackground(Void... params) {
            try {
                String baseurl;
                if (PreferenceManager.getDefaultSharedPreferences(getActivity()).getString
                        (getString(R.string
                                .pref_sort_order_key), "1").equals("1")) {
                    
                    baseurl = Constants.URL.POP_MOVIES;
                } else {
                    baseurl = Constants.URL.TOP_MOVIES;
                }
                URL url = new URL(Uri.parse(baseurl).buildUpon()
                        .appendQueryParameter(Constants.PARAMS.API_KEY,BuildConfig.API_KEY)
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
                        try {
                            movie.setReleaseDate(new SimpleDateFormat(Constants.DATE_FORMAT)
                                    .parse(jMovie.optString(Constants.JSON_KEYS.MOVIE
                                            .RELEASE_DATE)));

                        } catch (ParseException pex) {
                            Log.e(TAG, "error parsing date string in movie");
                        }
                        movie.setRating((float) jMovie.optDouble(Constants.JSON_KEYS.MOVIE
                                .VOTE_AVERAGE));
                        movieList.add(movie);
                    }
                    return movieList;
                }
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
            MovieListFragment.this.movies = (ArrayList) movies;
        }
    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        if (movies != null && movies.size() > 0) {
            outState.putParcelableArrayList("movies", movies);
        }
    }
    public interface Callback{
        void OnItemSelected(Intent intent);
    }
}
