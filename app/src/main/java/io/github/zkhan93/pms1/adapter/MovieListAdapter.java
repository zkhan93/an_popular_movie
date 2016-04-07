package io.github.zkhan93.pms1.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.List;

import io.github.zkhan93.pms1.R;
import io.github.zkhan93.pms1.models.Movie;

/**
 * Created by Zeeshan Khan on 4/7/2016.
 */
public class MovieListAdapter extends RecyclerView.Adapter<MovieListAdapter.ViewHolder> {
    private List<Movie> movieList = null;

    public MovieListAdapter(List<Movie> movieList) {
        if (movieList != null)
            this.movieList = movieList;
        else
            this.movieList = new ArrayList<>();
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
        View itemView= LayoutInflater.from(parent.getContext()).inflate(R.layout.movie_item,parent);
        return new ViewHolder(itemView,parent.getContext());

    }
    public class ViewHolder extends RecyclerView.ViewHolder {
        private ImageView poster;
        Context context;
        public ViewHolder(View parent,Context context) {
            super(parent);
            this.context=context;
            poster=(ImageView)parent.findViewById(R.id.movie_poster);
        }
        public void populateView(Movie movie){
            Picasso.with(context).load(movie.getPosterPath()).into(poster);
        }
    }

}