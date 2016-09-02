package project.devmob.tripcount.ui.group.fragment;


import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import project.devmob.tripcount.R;
import project.devmob.tripcount.models.Group;

/**
 * A simple {@link Fragment} subclass.
 */
public class BilanFragment extends Fragment {

    private Group myGroup;

    public BilanFragment() {
        // Required empty public constructor
    }


    public static BilanFragment newInstance(Group group) {
        BilanFragment fragment = new BilanFragment();
        fragment.myGroup = group;
        return fragment;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_bilan, container, false);
    }

}
