package project.devmob.tripcount.ui.group.fragment;


import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.google.android.gms.maps.CameraUpdate;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapView;
import com.google.android.gms.maps.MapsInitializer;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.LatLngBounds;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;

import project.devmob.tripcount.R;
import project.devmob.tripcount.models.Group;
import project.devmob.tripcount.models.Spending;
import project.devmob.tripcount.utils.requests.APIHelper;
import project.devmob.tripcount.utils.requests.TaskComplete;

/**
 * A simple {@link Fragment} subclass.
 */
public class MapFragment extends Fragment implements OnMapReadyCallback {

    public Group myGroup;
    private GoogleMap mMap;
    private boolean mapsReady;
    private List<Marker> markers;
    private SupportMapFragment mSupportMapFragment;

    public MapFragment() {
        // Required public constructor
        mapsReady = false;
        markers = new ArrayList<>();
    }

    public static MapFragment newInstance(Group group) {
        MapFragment fragment = new MapFragment();
        fragment.myGroup = group;
        return fragment;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View v = inflater.inflate(R.layout.fragment_map, container, false);
        mSupportMapFragment = (SupportMapFragment) getChildFragmentManager().findFragmentById(R.id.map_fragment);
        if (mSupportMapFragment == null) {
            FragmentManager fragmentManager = getFragmentManager();
            FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
            mSupportMapFragment = SupportMapFragment.newInstance();
            fragmentTransaction.replace(R.id.map_fragment, mSupportMapFragment).commit();
        }

        if (mSupportMapFragment != null) {
            mSupportMapFragment.getMapAsync(this);
        }
        return v;
    }

    @Override
    public void onMapReady(GoogleMap googleMap) {
        if (googleMap != null) {
            mMap = googleMap;
            googleMap.getUiSettings().setAllGesturesEnabled(true);
            Log.d("MapFragment", "onMapReady");
            refreshMarker();
        }
    }

    public void refreshMarker() {
        mapsReady = true;
        for (Marker m: markers) {
            m.setVisible(false);
            m.remove();
        }
        markers.clear();
        APIHelper.getSpendingsByGroupId(getContext(), myGroup, new TaskComplete<Type>() {
            @Override
            public void run() {
                List<Spending> spendings = (List<Spending>) this.result;
                for (Spending sp : spendings) {
                    if (sp.position != null) {
                        LatLng latLng = new LatLng(sp.position.lat, sp.position.lng);
                        Marker marker = mMap.addMarker(new MarkerOptions().position(latLng).title(sp.name));
                        markers.add(marker);
                    }
                }
                LatLngBounds.Builder builder = new LatLngBounds.Builder();
                for (Marker marker : markers) {
                    builder.include(marker.getPosition());
                }
                if (markers.size() > 0) {
                    LatLngBounds bounds = builder.build();
                    int padding = 100; // offset from edges of the map in pixels
                    CameraUpdate cu = CameraUpdateFactory.newLatLngBounds(bounds, padding);
                    mMap.moveCamera(cu);
                }
            }
        });
    }


    @Override
    public void onResume() {
        super.onResume();
        if (mapsReady) {
            refreshMarker();
        }
    }
}
