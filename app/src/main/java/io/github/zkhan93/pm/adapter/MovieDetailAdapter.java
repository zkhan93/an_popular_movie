package io.github.zkhan93.pm.adapter;

import android.content.Context;
import android.content.Intent;
import android.graphics.PorterDuff;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

import io.github.zkhan93.pm.R;
import io.github.zkhan93.pm.models.FavClickCallback;
import io.github.zkhan93.pm.models.Movie;
import io.github.zkhan93.pm.models.Review;
import io.github.zkhan93.pm.models.Trailer;

/**
 * this adapter will host all the information available for a movie including review and trailers
 * Created by Zeeshan Khan on 6/13/2016.
 */
public class MovieDetailAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {
    private List<Review> mReviews;
    private List<Trailer> mTrailers;
    private Movie mMovie;
    private boolean trailerUpdated, reviewUpdated;
    int trailerProgress, reviewProgress;
    private FavClickCallback favClickCallback;
    public static String TAG = MovieDetailAdapter.class.getSimpleName();

    public MovieDetailAdapter(FavClickCallback favClickCallback) {
        mMovie = null;
        mReviews = new ArrayList<>();
        mTrailers = new ArrayList<>();
        trailerUpdated = false;
        reviewUpdated = false;
        this.favClickCallback = favClickCallback;
        trailerProgress = 1;
        reviewProgress = 1;
    }

    public void setMovie(Movie movie) {
        if (movie == null)
            return;
        mMovie = movie;
        notifyItemRangeChanged(0, 1);
    }

    public void setReviews(List<Review> reviews) {
        reviewUpdated = true;
        if (reviews == null) {
            mReviews = new ArrayList<>();

        } else {
            mReviews = reviews;
        }
        reviewProgress = 0;
        notifyItemRemoved(3 + (trailerUpdated ? mTrailers.size() : 1));//remove review progress
        notifyItemRangeInserted(3 + (trailerUpdated ? mTrailers.size() : 1), mTrailers.size());
    }

    public void setTrailers(List<Trailer> trailers) {
        trailerUpdated = true;
        if (trailers == null) {
            mTrailers = new ArrayList<>();
        } else {
            mTrailers = trailers;
        }
        trailerProgress = 0;
        notifyItemRemoved(2);//remove trailer progress
        notifyItemRangeInserted(2, mTrailers.size());
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        LayoutInflater lf = LayoutInflater.from(parent.getContext());
        RecyclerView.ViewHolder holder = null;
        View view;
        switch (viewType) {
            case VIEW_TYPES.MOVIE_DETAILS:
                view = lf.inflate(R.layout.movie_details_item, parent, false);
                holder = new MovieDetailsVH(view, favClickCallback);
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
            case VIEW_TYPES.PROGRESS_BAR:
                view = lf.inflate(R.layout.progressbar, parent, false);
                holder = new ProgressVH(view);
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
            case VIEW_TYPES.TRAILER_HEADING:
                ((HeadingVH) holder).setHeading("Trailers");
                break;
            case VIEW_TYPES.TRAILER_ITEM:
                ((TrailerItemVH) holder).setTrailer(mTrailers.get(position - 2));
                break;
            case VIEW_TYPES.REVIEW_HEADING:
                ((HeadingVH) holder).setHeading("Reviews");
                break;
            case VIEW_TYPES.REVIEW_ITEM:
                int reviewItemPosition = position - 3 - (trailerUpdated ? mTrailers.size()
                        : 1);
                ((ReviewItemVH) holder).setReview(mReviews.get(reviewItemPosition));
                break;
            case VIEW_TYPES.PROGRESS_BAR:
                break;
        }
    }

    @Override
    public int getItemCount() {
        //movie details(1) + trailer header(1) + review heading(1) + length of trailers+length of
        // reviews
        int items = 3;
        if (mTrailers == null || mTrailers.size() == 0)
            if (!trailerUpdated) items += 1;
        if (mReviews == null || mReviews.size() == 0)
            if (!reviewUpdated) items += 1;

        return items + mTrailers.size() + mReviews.size();// 2 for progress bars;
    }

    @Override
    public int getItemViewType(int position) {
        if (position == 0)
            return VIEW_TYPES.MOVIE_DETAILS; //movie details

        if (position == 1)
            return VIEW_TYPES.TRAILER_HEADING;//trailer heading

        if (position == 2) {
            if (!trailerUpdated)
                return VIEW_TYPES.PROGRESS_BAR;//progressbar if no trailer items are present
        }

        if (position < 2 + mTrailers.size())
            return VIEW_TYPES.TRAILER_ITEM;//trailer item

        if (position == 2 + mTrailers.size() + trailerProgress)
            return VIEW_TYPES.REVIEW_HEADING;//review heading

        if (position == 3 + mTrailers.size() + trailerProgress) {
            if (!reviewUpdated)
                return VIEW_TYPES.PROGRESS_BAR;//trailer item
        }
        if (position < 3 + mTrailers.size() + trailerProgress + mReviews.size())
            return VIEW_TYPES.REVIEW_ITEM;//review item
        Log.d(TAG, "invalid view type" + position);
        return -1;
    }

    public void clear() {
        mTrailers.clear();
        mReviews.clear();
        trailerUpdated = false;
        reviewUpdated = false;
        trailerProgress = 1;
        reviewProgress = 1;
        notifyDataSetChanged();
    }

    public interface VIEW_TYPES {
        int MOVIE_DETAILS = 0;
        int TRAILER_HEADING = 1;
        int TRAILER_ITEM = 2;
        int REVIEW_HEADING = 3;
        int REVIEW_ITEM = 4;
        int PROGRESS_BAR = 5;
    }

    public static class MovieDetailsVH extends RecyclerView.ViewHolder {
        private TextView txtViewTitle, txtViewDate, txtViewRating, txtViewOverview;
        private ImageView imgViewMoviePoster;
        private ImageButton imgBtnMarkFav;
        private Context context;
        private FavClickCallback favClickCallback;

        public MovieDetailsVH(View view, final FavClickCallback
                favClickCallback) {
            super(view);
            this.favClickCallback = favClickCallback;
            context = view.getContext();
            txtViewTitle = (TextView) view.findViewById(R.id.movie_title);
            imgViewMoviePoster = (ImageView) view.findViewById(R.id.movie_poster);
            txtViewOverview = (TextView) view.findViewById(R.id.movie_overview);
            txtViewDate = (TextView) view.findViewById(R.id.movie_release_date);
            txtViewRating = (TextView) view.findViewById(R.id.movie_rating);
            imgBtnMarkFav = (ImageButton) view.findViewById(R.id.movie_mark_fav);
        }

        public void setMovieDetails(final Movie movie) {
            if (movie == null)
                return;
            txtViewTitle.setText(movie.getTitle());
            Picasso.with(context).load(movie.getFullPosterPath()).into(imgViewMoviePoster);
            txtViewOverview.setText(movie.getOverview());
            txtViewRating.setText(String.format("%.1f", movie.getRating()) + "/10");
            Calendar cal = Calendar.getInstance();
            cal.setTime(movie.getReleaseDate());
            txtViewDate.setText(String.valueOf(cal.get(Calendar.YEAR)));
            imgBtnMarkFav.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    favClickCallback.onClick(movie.getId());
                    movie.setFavorite(!movie.isFavorite());
                    syncMarkFav(movie.isFavorite());
                }
            });
            syncMarkFav(movie.isFavorite());
        }

        private void syncMarkFav(boolean isFav) {
            if (isFav) {
                Drawable drawable = context.getResources().getDrawable(R.drawable
                        .ic_star_white_36dp);
                if (drawable != null)
                    drawable.setColorFilter(context.getResources().getColor(R.color
                                    .colorPrimary)

                            , PorterDuff.Mode.MULTIPLY);
                imgBtnMarkFav.setImageDrawable(drawable);
            } else {
                Drawable drawable = context.getResources().getDrawable(R.drawable
                        .ic_star_border_white_36dp);
                if (drawable != null)
                    drawable.setColorFilter(context.getResources().getColor(R.color
                                    .colorPrimary)

                            , PorterDuff.Mode.MULTIPLY);
                imgBtnMarkFav.setImageDrawable(drawable);
            }
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
        private View rootView;

        public TrailerItemVH(View itemView) {
            super(itemView);
            rootView = itemView;
            context = itemView.getContext();
            txtName = (TextView) itemView.findViewById(R.id.trailer_name);
        }

        public void setTrailer(final Trailer trailer) {
            txtName.setText(trailer.getName());
            rootView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse(String.format
                            ("http://www.youtube.com/watch?v=%s", trailer.getKey())));
                    if (intent.resolveActivity(context.getPackageManager()) != null) {
                        context.startActivity(intent);
                    } else {
                        Toast.makeText(context, "No app to view videos", Toast.LENGTH_SHORT).show();
                    }
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

    public static class ProgressVH extends RecyclerView.ViewHolder {
        public ProgressVH(View itemView) {
            super(itemView);
        }
    }
}
