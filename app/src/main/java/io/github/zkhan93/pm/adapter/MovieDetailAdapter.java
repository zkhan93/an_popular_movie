package io.github.zkhan93.pm.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

import io.github.zkhan93.pm.R;
import io.github.zkhan93.pm.models.Movie;
import io.github.zkhan93.pm.models.Review;
import io.github.zkhan93.pm.models.Trailer;

/**
 * this adapter will host all the information available for a movie
 * Created by Zeeshan Khan on 6/13/2016.
 */
public class MovieDetailAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {
    private List<Review> mReviews;
    private List<Trailer> mTrailers;
    private Movie mMovie;

    public MovieDetailAdapter() {
        mMovie = null;
        mReviews = new ArrayList<>();
        mTrailers = new ArrayList<>();
    }

    public void setMovie(Movie movie) {
        if (movie == null)
            return;
        mMovie = movie;
        notifyItemRangeChanged(0, 1);
    }

    public void setReviews(List<Review> reviews) {
        if (reviews == null)
            return;
        mReviews = reviews;
        notifyItemRangeChanged(4 + mTrailers.size(), reviews.size());
    }

    public void setTrailers(List<Trailer> trailers) {
        if (trailers == null)
            return;
        mTrailers = trailers;
        notifyItemRangeChanged(3, trailers.size());
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        LayoutInflater lf = LayoutInflater.from(parent.getContext());
        RecyclerView.ViewHolder holder = null;
        View view = null;
        switch (viewType) {
            case VIEW_TYPES.MOVIE_DETAILS:
                view = lf.inflate(R.layout.movie_details_item, parent, false);
                holder = new MovieDetailsVH(view);
                break;
            case VIEW_TYPES.REVIEW_HEADING:
            case VIEW_TYPES.TRAILER_HEADING:
                view = lf.inflate(R.layout.movie_detail_list_header, parent, false);
                holder = new HeadingVH(view);
                break;
            case VIEW_TYPES.REVIEW_ITEM:
                view = lf.inflate(R.layout.movie_detail_review_item, parent, false);
                holder = new ReviewItemVH(view);
                break;
            case VIEW_TYPES.TRAILER_ITEM:
                view = lf.inflate(R.layout.movie_detail_trailer_item, parent, false);
                holder = new TrailerItemVH(view);
                break;
        }
        return holder;
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
        switch (getItemViewType(position)) {
            case VIEW_TYPES.MOVIE_DETAILS:
                ((MovieDetailsVH) holder).setMovieDetails(mMovie);
                break;
            case VIEW_TYPES.REVIEW_HEADING:
                ((HeadingVH) holder).setHeading("Reviews");
                break;
            case VIEW_TYPES.REVIEW_ITEM:
                ((ReviewItemVH) holder).setReview(mReviews.get(position - 3 - mTrailers.size()));
                break;
            case VIEW_TYPES.TRAILER_HEADING:
                ((HeadingVH) holder).setHeading("Trailers");
                break;
            case VIEW_TYPES.TRAILER_ITEM:
                ((TrailerItemVH) holder).setTrailer(mTrailers.get(position - 2));
                break;
        }
    }

    @Override
    public int getItemCount() {
        //movie details(1) + trailer header(1) + review heading(1) + length of trailers+length of
        // reviews
        return 3 + mTrailers.size() + mReviews.size();
    }

    @Override
    public int getItemViewType(int position) {
        if (position == 0)
            return VIEW_TYPES.MOVIE_DETAILS; //movie details
        else if (position == 1)
            return VIEW_TYPES.TRAILER_HEADING;//trailer heading
        else if (position < 2 + mTrailers.size())
            return VIEW_TYPES.TRAILER_ITEM;//trailer item
        else if (position == 2 + mTrailers.size())
            return VIEW_TYPES.REVIEW_HEADING;//review heading
        else
            return VIEW_TYPES.REVIEW_ITEM;//review item
    }

    public void clear() {
        mTrailers.clear();
        mReviews.clear();
        notifyDataSetChanged();
    }

    public interface VIEW_TYPES {
        int MOVIE_DETAILS = 0;
        int TRAILER_HEADING = 1;
        int TRAILER_ITEM = 2;
        int REVIEW_HEADING = 3;
        int REVIEW_ITEM = 4;
    }

    public static class MovieDetailsVH extends RecyclerView.ViewHolder {
        private TextView txtViewTitle, txtViewDate, txtViewRating, txtViewOverview;
        private ImageView imgViewMoviePoster;
        private Context context;

        public MovieDetailsVH(View view) {
            super(view);
            context = view.getContext();
            txtViewTitle = (TextView) view.findViewById(R.id.movie_title);
            imgViewMoviePoster = (ImageView) view.findViewById(R.id.movie_poster);
            txtViewOverview = (TextView) view.findViewById(R.id.movie_overview);
            txtViewDate = (TextView) view.findViewById(R.id.movie_release_date);
            txtViewRating = (TextView) view.findViewById(R.id.movie_rating);
        }

        public void setMovieDetails(Movie movie) {
            if (movie == null)
                return;
            txtViewTitle.setText(movie.getTitle());
            Picasso.with(context).load(movie.getFullPosterPath()).into(imgViewMoviePoster);
            txtViewOverview.setText(movie.getOverview());
            txtViewRating.setText(String.format("%.1f", movie.getRating()) + "/10");
            Calendar cal = Calendar.getInstance();
            cal.setTime(movie.getReleaseDate());
            txtViewDate.setText(String.valueOf(cal.get(Calendar.YEAR)));
        }
    }

    public static class HeadingVH extends RecyclerView.ViewHolder {
        TextView txtHeading;

        public HeadingVH(View itemView) {
            super(itemView);
            txtHeading = (TextView) itemView;
        }

        public void setHeading(String heading) {
            txtHeading.setText(heading);
        }
    }

    public static class TrailerItemVH extends RecyclerView.ViewHolder {
        private TextView txtName;
        private Context context;

        public TrailerItemVH(View itemView) {
            super(itemView);
            context = itemView.getContext();
            txtName = (TextView) itemView.findViewById(R.id.trailer_name);
        }

        public void setTrailer(final Trailer trailer) {
            txtName.setText(trailer.getName());
            txtName.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Toast.makeText(context, trailer.getKey(), Toast.LENGTH_SHORT).show();
                }
            });
        }
    }

    public static class ReviewItemVH extends RecyclerView.ViewHolder {
        private TextView txtAuthor, txtContent;

        public ReviewItemVH(View itemView) {
            super(itemView);
            txtAuthor = (TextView) itemView.findViewById(R.id.review_author);
            txtContent = (TextView) itemView.findViewById(R.id.review_content);
        }

        public void setReview(Review review) {
            txtAuthor.setText(review.getAuthor());
            txtContent.setText(review.getContent());
        }
    }
}
