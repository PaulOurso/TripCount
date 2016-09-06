package project.devmob.tripcount.ui.group.spending;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.CheckBox;
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

public class DetailSpendingActivity extends AppCompatActivity implements OnMapReadyCallback {

    private static final String TAG_LOCATION = "DSA Loc";

    private GoogleMap mMap;
    private String spending_id;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail_spending);
        spending_id = (String) getIntent().getExtras().get(Constant.INTENT_SPENDING_ID);
        if (getSupportActionBar() != null) {
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        }

        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager().findFragmentById(R.id.map_spending);
        mapFragment.getMapAsync(this);
    }

    @Override
    public void onMapReady(GoogleMap googleMap) {
        Log.d(TAG_LOCATION + " Maps", "Maps Ready");
        mMap = googleMap;
        APIHelper.getSpending(DetailSpendingActivity.this, spending_id, new TaskComplete<Type>() {
            @Override
            public void run() {
                Spending spending = (Spending) this.result;
                displayInfos(spending);
            }
        });
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                onBackPressed();
                return true;

            default:
                return super.onOptionsItemSelected(item);
        }
    }

    public void displayInfos(Spending spending) {
        TextView date = (TextView) findViewById(R.id.detail_spending_date);
        TextView price = (TextView) findViewById(R.id.detail_spending_price);
        TextView purchaser = (TextView) findViewById(R.id.detail_spending_payer);
        Calendar calendar = FormatHelper.formatStringToCal(spending.create_date);
        if (getSupportActionBar() != null)
            getSupportActionBar().setTitle(spending.name);
        if (price != null)
            price.setText(String.format(getString(R.string.currency), spending.price));
        if (date != null)
            date.setText(calendar.get(Calendar.DAY_OF_MONTH)+"/"+(calendar.get(Calendar.MONTH)+1)+"/"+calendar.get(Calendar.YEAR));
        if (purchaser != null) {
            purchaser.setText(spending.purchaser.name);
        }

        // +1 for purchaser
        double priceByPerson = FormatHelper.formatPrice(spending.price/(double)(spending.indebted.size()+1));
        for (Person person: spending.indebted) {
            createItemParticipant(priceByPerson, person);
        }

        if (spending.latitude != 0.0 && spending.longitude != 0.0) {
            LatLng latLng = spending.getLatLng();
            mMap.addMarker(new MarkerOptions().position(latLng).title(spending.name));
            mMap.moveCamera(CameraUpdateFactory.newLatLng(latLng));
            mMap.setOnMapClickListener(new GoogleMap.OnMapClickListener() {
                @Override
                public void onMapClick(LatLng latLng) {
                    // Nothing
                }
            });
            LinearLayout linearLayoutMap = (LinearLayout) findViewById(R.id.linearlayout_detail_spending_map);
            if (linearLayoutMap != null)
                linearLayoutMap.setVisibility(View.VISIBLE);
        }
    }

    public void createItemParticipant(double price, final Person person){

        LayoutInflater layoutInflater = LayoutInflater.from(this);

        LinearLayout item_participant = (LinearLayout) layoutInflater.inflate(R.layout.item_detail_participant,null);
        LinearLayout layoutParticipantsList = (LinearLayout) findViewById(R.id.linearlayout_participants_list);

        TextView participantName = (TextView) item_participant.findViewById(R.id.item_participant_name);
        TextView participantPrice = (TextView) item_participant.findViewById(R.id.item_participant_price);

        participantName.setText(person.name);
        participantPrice.setText(String.valueOf(String.format(getString(R.string.currency), price)));

        layoutParticipantsList.addView(item_participant);
    }
}
