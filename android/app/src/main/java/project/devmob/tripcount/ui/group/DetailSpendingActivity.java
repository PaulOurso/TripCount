package project.devmob.tripcount.ui.group;

import android.content.pm.PackageManager;
import android.location.Location;
import android.location.LocationListener;
import android.support.annotation.NonNull;
import android.support.v4.app.FragmentActivity;
import android.os.Bundle;
import android.util.Log;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;

import project.devmob.tripcount.R;
import project.devmob.tripcount.models.Spending;
import project.devmob.tripcount.utils.Constant;
import project.devmob.tripcount.utils.LocationHelper;

public class DetailSpendingActivity extends FragmentActivity implements OnMapReadyCallback {

    private static final String TAG_LOCATION = "DSA Loc";

    Spending spending;
    private GoogleMap mMap;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail_spending);
        spending = (Spending) getIntent().getExtras().get(Constant.INTENT_SPENDING);
        if (getActionBar() != null)
            getActionBar().setDisplayHomeAsUpEnabled(true);

        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager().findFragmentById(R.id.map_spending);
        mapFragment.getMapAsync(this);
    }

    @Override
    public void onMapReady(GoogleMap googleMap) {
        Log.d(TAG_LOCATION+" Maps", "Maps Ready");
        mMap = googleMap;

        // Add a marker and move the camera
        if (spending.position != null) {
            mMap.addMarker(new MarkerOptions().position(spending.position).title(spending.name));
            mMap.moveCamera(CameraUpdateFactory.newLatLng(spending.position));
            mMap.setOnMapClickListener(new GoogleMap.OnMapClickListener() {
                @Override
                public void onMapClick(LatLng latLng) {
                    // Nothing
                }
            });
        }
    }
}
