package io.github.zkhan93.pms1;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

/**
 * A placeholder fragment containing a simple view.
 */
public class MainActivityFragment extends Fragment {
    private RecyclerView movieList;

    public MainActivityFragment() {
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_main, container, false);
        movieList = (RecyclerView) rootView.findViewById(R.id.movie_list);
        movieList.setLayoutManager(new GridLayoutManager(getActivity(),2));
        return rootView;
    }

    @Override
    public void onStart() {
        super.onStart();
    }

}
