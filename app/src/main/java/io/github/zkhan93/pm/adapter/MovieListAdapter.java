package io.github.zkhan93.pm.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.List;

import io.github.zkhan93.pm.R;
import io.github.zkhan93.pm.models.Movie;
import io.github.zkhan93.pm.models.OnMovieClickListener;

/**
 * Created by Zeeshan Khan on 4/7/2016.
 */
public class MovieListAdapter extends RecyclerView.Adapter<MovieListAdapter.ViewHolder> {
    private List<Movie> movieList = null;
    public static String TAG = MovieListAdapter.class.getSimpleName();
    private OnMovieClickListener onClickListener;

    public MovieListAdapter(List<Movie> movieList, OnMovieClickListener onClickListener) {
        if (movieList != null)
            this.movieList = movieList;
        else
            this.movieList = new ArrayList<>();
        this.onClickListener = onClickListener;
    }

    public void addAll(List<Movie> movies) {
        if (movies == null)
            return;
        if (movieList != null)
            movieList.clear();
        else
            movieList = new ArrayList<>();
        movieList.addAll(movies);
        notifyDataSetChanged();

    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        holder.populateView(movieList.get(position));
    }

    @Override
    public int getItemCount() {
        return movieList.size();
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext()).inflate(R.layout.movie_item,
                parent, false);
        return new ViewHolder(itemView, parent.getContext(), onClickListener);

    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        private ImageView poster;
        Context context;
        private Movie movie;

        public ViewHolder(View parent, Context context, final OnMovieClickListener
                onClickListener) {
            super(parent);
            this.context = context;
            poster = (ImageView) parent.findViewById(R.id.movie_poster);
            parent.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    onClickListener.onClick(getMovie());
                }
            });
        }

        public void populateView(Movie movie) {
            Picasso.with(context).load(movie.getPosterPath()).into(poster);
            this.movie = movie;
        }

        private Movie getMovie() {
            return movie;
        }
    }
}