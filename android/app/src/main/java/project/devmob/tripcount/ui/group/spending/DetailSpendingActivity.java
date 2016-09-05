package project.devmob.tripcount.ui.group.spending;

import android.content.pm.PackageManager;
import android.location.Location;
import android.location.LocationListener;
import android.support.annotation.NonNull;
import android.support.v4.app.FragmentActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;

import java.lang.reflect.Type;
import java.util.Calendar;

import project.devmob.tripcount.R;
import project.devmob.tripcount.models.Person;
import project.devmob.tripcount.models.Spending;
import project.devmob.tripcount.utils.Constant;
import project.devmob.tripcount.utils.helpers.FormatHelper;
import project.devmob.tripcount.utils.requests.APIHelper;
import project.devmob.tripcount.utils.requests.TaskComplete;

public class DetailSpendingActivity extends FragmentActivity implements OnMapReadyCallback {

    private static final String TAG_LOCATION = "DSA Loc";

    private GoogleMap mMap;
    private String spending_id;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail_spending);
        spending_id = (String) getIntent().getExtras().get(Constant.INTENT_SPENDING_ID);
        if (getActionBar() != null)
            getActionBar().setDisplayHomeAsUpEnabled(true);

        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager().findFragmentById(R.id.map_spending);
        mapFragment.getMapAsync(this);
    }

    @Override
    public void onMapReady(GoogleMap googleMap) {
        Log.d(TAG_LOCATION+" Maps", "Maps Ready");
        mMap = googleMap;
        APIHelper.getSpending(DetailSpendingActivity.this, spending_id, new TaskComplete<Type>() {
            @Override
            public void run() {
                Spending spending = (Spending) this.result;
                displayInfos(spending);
            }
        });
    }

    public void displayInfos(Spending spending) {
        TextView date = (TextView) findViewById(R.id.detail_spending_date);
        TextView price = (TextView) findViewById(R.id.detail_spending_price);
        price.setText(String.format(getString(R.string.currency), spending.price));
        Calendar calendar = FormatHelper.formatStringToCal(spending.create_date);
        date.setText(calendar.get(Calendar.DAY_OF_MONTH)+"/"+(calendar.get(Calendar.MONTH)+1)+"/"+calendar.get(Calendar.YEAR));
        //createItemParticipant(spending.indebteds);

        if (spending.position != null) {
            LatLng latLng = new LatLng(spending.position.lat, spending.position.lng);
            mMap.addMarker(new MarkerOptions().position(latLng).title(spending.name));
            mMap.moveCamera(CameraUpdateFactory.newLatLng(latLng));
            mMap.setOnMapClickListener(new GoogleMap.OnMapClickListener() {
                @Override
                public void onMapClick(LatLng latLng) {
                    // Nothing
                }
            });
        }
    }

    public void createItemParticipant(final Person person){

        LayoutInflater layoutInflater = LayoutInflater.from(this);

        LinearLayout item_participant = (LinearLayout) layoutInflater.inflate(R.layout.item_participant,null);
        LinearLayout layoutParticipantsList = (LinearLayout) findViewById(R.id.linearlayout_participants_list);

        CheckBox participantCheckBox = (CheckBox) item_participant.findViewById(R.id.item_participant_checkbox);
        TextView participantsName = (TextView) item_participant.findViewById(R.id.item_participant_name);

        participantsName.setText(person.name);
        participantCheckBox.setVisibility(View.GONE);

        layoutParticipantsList.addView(item_participant);
    }
}
