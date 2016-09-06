package project.devmob.tripcount.ui.group.spending;

import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Location;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.maps.model.LatLng;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import project.devmob.tripcount.R;
import project.devmob.tripcount.models.Group;
import project.devmob.tripcount.models.Person;
import project.devmob.tripcount.models.Spending;
import project.devmob.tripcount.utils.Constant;
import project.devmob.tripcount.utils.LocationHelper;
import project.devmob.tripcount.utils.helpers.FormatHelper;
import project.devmob.tripcount.utils.requests.APIHelper;
import project.devmob.tripcount.utils.requests.TaskComplete;

public class AddSpendingActivity extends AppCompatActivity implements android.location.LocationListener{

    private static final String TAG_LOCATION = "ASA Loc";
    private static final String TAG = "AddSpendingActivity" ;

    private HashMap<Person, Boolean> personMap;
    private Group myGroup;
    private LocationHelper locationHelper;
    private LatLng position;
    private List<Person> personList;

    private Spinner spinner;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_spending);
        if (getSupportActionBar() != null) {
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        }

        locationHelper = LocationHelper.getInstance();
        Intent intent = getIntent();
        myGroup = (Group) intent.getExtras().get(Constant.INTENT_GROUP);
        personMap = new HashMap<>();
        personList = new ArrayList<>();

        spinner = (Spinner) findViewById(R.id.add_spending_spinner);

        APIHelper.getPersonByGroup(AddSpendingActivity.this, myGroup, new TaskComplete<Type>() {
            @Override
            public void run() {
                LayoutInflater layoutInflater = LayoutInflater.from(AddSpendingActivity.this);
                personList = (List<Person>) this.result;
                Log.d(TAG, ""+personList.size());

                for (Person person: personList) {
                    personMap.put(person, false);
                    createItemParticipant(person);

                    LinearLayout item_payer= (LinearLayout) layoutInflater.inflate(R.layout.item_payer,null);
                    TextView payerName = (TextView) item_payer.findViewById(R.id.item_payer_name);
                    payerName.setText(person.name);

                    ArrayAdapter<String> adapterSpinner = new ArrayAdapter<>(AddSpendingActivity.this, android.R.layout.simple_spinner_item, new ArrayList<String>());
                    adapterSpinner.addAll(getListNamePersons(personList));
                    adapterSpinner.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                    spinner.setAdapter(adapterSpinner);
                }
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

    @Override
    protected void onResume() {
        super.onResume();
        locationHelper.createRequest(AddSpendingActivity.this, AddSpendingActivity.this);
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        switch (requestCode) {
            case Constant.REQUEST_PERMISSIONS_GPS: {
                // If request is cancelled, the result arrays are empty.
                if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    locationHelper.createRequest(AddSpendingActivity.this, AddSpendingActivity.this);
                }
            }
        }
    }

    @Override
    public void onLocationChanged(Location location) {
        LatLng latLng = new LatLng(location.getLatitude(), location.getLongitude());
        position = latLng;
        Log.d(TAG_LOCATION+" onLocation", latLng.toString());
    }

    @Override
    public void onStatusChanged(String s, int i, Bundle bundle) {
        Log.d(TAG_LOCATION+" onStatus", s);
    }

    @Override
    public void onProviderEnabled(String s) {
        Log.d(TAG_LOCATION+" onProvider", s);
    }

    @Override
    public void onProviderDisabled(String s) {
        Log.d(TAG_LOCATION+" onProvider", s);
    }

    public List<String> getListNamePersons(List<Person> list) {
        List<String> listP = new ArrayList<>();
        for (Person p: personList) {
            listP.add(p.name);
        }
        return listP;
    }

    public static void show(Context context, Group group) {
        Intent intent = new Intent(context, AddSpendingActivity.class);
        intent.putExtra(Constant.INTENT_GROUP,group);
        context.startActivity(intent);
    }


    public void showError(int resTextView, int resMsg) {
        TextView textViewErr = (TextView) findViewById(resTextView);
        if (textViewErr != null) {
            textViewErr.setVisibility(View.VISIBLE);
            textViewErr.setText(resMsg);
        }
    }

    public void addParticipantToListView(View view) {
        EditText editNewParticipantName = (EditText) findViewById(R.id.new_participant_name);

        if (editNewParticipantName != null) {
            if (!editNewParticipantName.getText().toString().isEmpty()) {

                Person person = new Person();
                person.name = editNewParticipantName.getText().toString();
                person.id = null;
                personMap.put(person, false);

                createItemParticipant(person);
                personList.add(person);
                editNewParticipantName.setText("");

                ArrayAdapter<String> adapterSpinner = new ArrayAdapter<>(AddSpendingActivity.this, android.R.layout.simple_spinner_item, new ArrayList<String>());
                adapterSpinner.addAll(getListNamePersons(personList));
                adapterSpinner.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                spinner.setAdapter(adapterSpinner);
            }
        }
    }

    public void createItemParticipant(final Person person){
        LayoutInflater layoutInflater = LayoutInflater.from(this);

        LinearLayout item_participant = (LinearLayout) layoutInflater.inflate(R.layout.item_participant,null);
        LinearLayout layoutParticipantsList = (LinearLayout) findViewById(R.id.linearlayout_participants_list);

        final CheckBox participantCheckBox = (CheckBox) item_participant.findViewById(R.id.item_participant_checkbox);
        TextView participantsName = (TextView) item_participant.findViewById(R.id.item_participant_name);

        participantsName.setText(person.name);
        participantsName.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (isNotPurchaserSelected(person)) {
                    boolean checkStatus = !participantCheckBox.isChecked();
                    participantCheckBox.setChecked(checkStatus);
                    personMap.put(person, checkStatus);
                }
            }
        });
        participantCheckBox.setOnCheckedChangeListener(
                new CompoundButton.OnCheckedChangeListener() {
                    @Override
                    public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                        Log.d(TAG, ""+isChecked);
                        if (isNotPurchaserSelected(person))
                            personMap.put(person, isChecked);
                        else {
                            buttonView.setChecked(false);
                            personMap.put(person, false);
                        }
                    }
                }
        );

        layoutParticipantsList.addView(item_participant);
    }

    public boolean isNotPurchaserSelected(Person personSelected) {
        int spinnerPosSelected = spinner.getSelectedItemPosition();
        return spinnerPosSelected < personList.size() && personSelected != personList.get(spinnerPosSelected);
    }

    public void addNewSpending(View view) {
        EditText editNewSpendingName = (EditText) findViewById(R.id.new_spending_name);
        EditText editNewSpendingCost = (EditText) findViewById(R.id.new_spending_cost);
        Spending mySpending = new Spending();
        if (mySpending.indebted == null)
            mySpending.indebted = new ArrayList<>();

        if (editNewSpendingName != null && editNewSpendingCost != null) {
            if (editNewSpendingName.getText().toString().isEmpty()) {
                showError(R.id.add_spending_error_no_name, R.string.add_spending_error_no_name);
            }
            if (editNewSpendingCost.getText().toString().isEmpty()) {
                showError(R.id.add_spending_error_no_cost, R.string.add_spending_error_no_cost);
            }
            else {
                //create a new spending
                if (spinner.getSelectedItemPosition() < personList.size())
                    mySpending.purchaser = personList.get(spinner.getSelectedItemPosition());

                if (mySpending.purchaser == null) {
                    Toast.makeText(AddSpendingActivity.this, R.string.missing_purchaser_select, Toast.LENGTH_LONG).show();
                    return;
                }
                mySpending.name = editNewSpendingName.getText().toString();
                mySpending.price = Double.parseDouble(editNewSpendingCost.getText().toString());
                mySpending.create_date = FormatHelper.formatCalToString(Calendar.getInstance());
                if(position != null){
                    mySpending.latitude = position.latitude;
                    mySpending.longitude = position.longitude;
                }

                mySpending.indebted.clear();
                for (Map.Entry<Person,Boolean> entry: personMap.entrySet()) {
                    final Person person = entry.getKey();
                    boolean checked = entry.getValue();
                    if (checked) {
                        mySpending.indebted.add(person);
                    }
                }
                if (mySpending.indebted.size() == 0) {
                    Toast.makeText(AddSpendingActivity.this, R.string.missing_indebted_select, Toast.LENGTH_LONG).show();
                    return;
                }
                // First : Purchaser Person if necessary
                // Second : Spending with purchaser
                // Third : Indebted Person if necessary
                // Four : Link indebted Person <-> Spending
                if (mySpending.purchaser.id == null)
                    launchCreatePurchaser(mySpending);
                else
                    launchCreateSpending(mySpending);
            }
        }
    }

    private void launchCreatePurchaser(final Spending mySpending) {
        APIHelper.createPerson(AddSpendingActivity.this, myGroup, mySpending.purchaser, new TaskComplete<Type>() {
            @Override
            public void run() {
                mySpending.purchaser = (Person) this.result;
                launchCreateSpending(mySpending);
            }
        });
    }

    private void launchCreateSpending(final Spending mySpending) {
        APIHelper.createSpending(AddSpendingActivity.this, myGroup, mySpending, new TaskComplete<Type>() {
            @Override
            public void run() {
                final Spending spending = (Spending) this.result;

                for (Person p: spending.indebted) {
                    if (p.id == null)
                        launchCreatePersonIndebted(mySpending, p);
                    else
                        launchLinkSpendingToPerson(mySpending, p);
                }
            }
        });
    }

    private void launchCreatePersonIndebted(final Spending mySpending, Person person) {
        APIHelper.createPerson(AddSpendingActivity.this, myGroup, person, new TaskComplete<Type>() {
            @Override
            public void run() {
                Person personResult = (Person) this.result;
                launchLinkSpendingToPerson(mySpending, personResult);
            }
        });
    }

    private void launchLinkSpendingToPerson(Spending mySpending, Person person){
        APIHelper.linkPersonToSpending(AddSpendingActivity.this, mySpending, person,new TaskComplete<Type>() {
            @Override
            public void run() {
                Toast.makeText(AddSpendingActivity.this, R.string.add_spending_success, Toast.LENGTH_SHORT).show();
                finish();
            }
        });
    }
}
