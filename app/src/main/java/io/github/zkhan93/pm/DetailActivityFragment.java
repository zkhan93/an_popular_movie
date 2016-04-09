package io.github.zkhan93.pm;

import android.os.Bundle;
import android.support.v4.app.Fragment;
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
        return view;
    }

    @Override
    public void onStart() {
        super.onStart();
        Movie movie = getActivity().getIntent().getExtras().getParcelable("movie");
        txtViewTitle.setText(movie.getTitle());
        Picasso.with(getContext()).load(movie.getPosterPath()).into(imgViewMoviePoster);
        txtViewOverview.setText(movie.getOverview());
        txtViewRating.setText(String.format("%.1f", movie.getRating())+"/10");
        Calendar cal = Calendar.getInstance();
        cal.setTime(movie.getReleaseDate());
        txtViewDate.setText(String.valueOf(cal.get(Calendar.YEAR)));
    }
}
