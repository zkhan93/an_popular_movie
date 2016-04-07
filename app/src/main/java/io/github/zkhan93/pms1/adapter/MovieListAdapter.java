package io.github.zkhan93.pms1.adapter;

import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.view.ViewGroup;

import java.util.ArrayList;
import java.util.List;

import io.github.zkhan93.pms1.models.Movie;

/**
 * Created by Zeeshan Khan on 4/7/2016.
 */
public class MovieListAdapter extends RecyclerView.Adapter<MovieListAdapter.ViewHolder> {
    private List<Movie> movieList = null;

    MovieListAdapter(List<Movie> movieList) {
        if (movieList != null)
            this.movieList = movieList;
        else
            this.movieList = new ArrayList<>();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        public ViewHolder(View parent) {
            super(parent);
        }
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {

    }

    @Override
    public int getItemCount() {
        return movieList.size();
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        return null;

    }
}