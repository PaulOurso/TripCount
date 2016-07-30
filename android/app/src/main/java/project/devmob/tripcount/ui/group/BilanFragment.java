package project.devmob.tripcount.ui.group;


import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import project.devmob.tripcount.R;

/**
 * A simple {@link Fragment} subclass.
 */
public class BilanFragment extends Fragment {


    public BilanFragment() {
        // Required empty public constructor
    }


    public static BilanFragment newInstance() {
        return new BilanFragment();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_bilan, container, false);
    }

}
