package io.github.zkhan93.pm;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.squareup.picasso.Picasso;

import java.util.Calendar;

import io.github.zkhan93.pm.models.Movie;

/**
 * A placeholder fragment containing a simple view.
 */
public class DetailActivityFragment extends Fragment {
    private TextView txtViewTitle, txtViewDate, txtViewRating, txtViewOverview;
    private ImageView imgViewMoviePoster;
    private RecyclerView listTrailers;
    public static final String TAG = DetailActivityFragment.class.getSimpleName();

    public DetailActivityFragment() {
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_detail, container, false);
        txtViewTitle = (TextView) view.findViewById(R.id.movie_title);
        imgViewMoviePoster = (ImageView) view.findViewById(R.id.movie_poster);
        txtViewOverview = (TextView) view.findViewById(R.id.movie_overview);
        txtViewDate = (TextView) view.findViewById(R.id.movie_release_date);
        txtViewRating = (TextView) view.findViewById(R.id.movie_rating);
        listTrailers = (RecyclerView) view.findViewById(R.id.movie_trailer_list);
        listTrailers.setLayoutManager(new LinearLayoutManager(getContext()));
        return view;
    }

    @Override
    public void onStart() {
        super.onStart();
        displayMovieFromIntent(getActivity().getIntent());
    }

    public void displayMovieFromIntent(Intent intent) {
        Bundle bundle = intent.getExtras();
        if (bundle == null)
            return;
        Movie movie = bundle.getParcelable("movie");
        if (movie == null)
            return;
        txtViewTitle.setText(movie.getTitle());
        Picasso.with(getContext()).load(movie.getPosterPath()).into(imgViewMoviePoster);
        txtViewOverview.setText(movie.getOverview());
        txtViewRating.setText(String.format("%.1f", movie.getRating()) + "/10");
        Calendar cal = Calendar.getInstance();
        cal.setTime(movie.getReleaseDate());
        txtViewDate.setText(String.valueOf(cal.get(Calendar.YEAR)));
    }
}
