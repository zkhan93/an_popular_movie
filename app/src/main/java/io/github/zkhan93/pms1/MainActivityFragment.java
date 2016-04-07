package io.github.zkhan93.pms1;

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

import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

import io.github.zkhan93.pms1.adapter.MovieListAdapter;
import io.github.zkhan93.pms1.models.Movie;
import io.github.zkhan93.pms1.util.Constants;

/**
 * A placeholder fragment containing a simple view.
 */
public class MainActivityFragment extends Fragment {
    public static final String TAG = MainActivityFragment.class.getSimpleName();
    private RecyclerView movieListView;
    private List<Movie> movieList;
    private RecyclerView.Adapter<MovieListAdapter.ViewHolder> movieAdapter;

    public MainActivityFragment() {
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_main, container, false);
        movieListView = (RecyclerView) rootView.findViewById(R.id.movie_list);
        movieListView.setLayoutManager(new GridLayoutManager(getActivity(), 2));
        movieAdapter = new MovieListAdapter(new ArrayList<Movie>());
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

    private class FetchMovies extends AsyncTask<Void, Void, Void> {
        @Override
        protected Void doInBackground(Void... params) {
            try {
                URL url = new URL(Constants.URL.POP_MOVIES);
                HttpURLConnection connection = (HttpURLConnection) url.openConnection();
                InputStream is = connection.getInputStream();
                BufferedReader br = new BufferedReader(new InputStreamReader(is));
                String line = null;
                StringBuffer buffer = new StringBuffer();
                while ((line = br.readLine()) != null) {
                    buffer.append(line + "\n");
                }
                //TODO: parse the data to json and populate adapter with data
            } catch (MalformedURLException urlEx) {
                Log.e(TAG, "could not parse url " + urlEx);
            } catch (IOException ioEx) {
                Log.e(TAG, "could not open connection " + ioEx);
            }
            return null;
        }
    }

}
