package io.github.zkhan93.pm;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

/**
 * A placeholder fragment containing a simple view.
 */
public class DetailActivityFragment extends Fragment {
    private TextView movieTitle;

    public DetailActivityFragment() {
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_detail, container, false);
        movieTitle = (TextView) view.findViewById(R.id.movie_title);
        return view;
    }

    @Override
    public void onStart() {
        super.onStart();
        movieTitle.setText(getActivity().getIntent().getExtras().getString("movie"));
    }
}
