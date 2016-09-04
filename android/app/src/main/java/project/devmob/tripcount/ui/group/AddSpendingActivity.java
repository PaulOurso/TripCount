package project.devmob.tripcount.ui.group;

import android.content.Context;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import project.devmob.tripcount.R;
import project.devmob.tripcount.models.Group;
import project.devmob.tripcount.models.Person;
import project.devmob.tripcount.ui.grouplist.AdapterGroupList;
import project.devmob.tripcount.utils.Constant;
import project.devmob.tripcount.utils.requests.APIHelper;
import project.devmob.tripcount.utils.requests.TaskComplete;

public class AddSpendingActivity extends AppCompatActivity {

    private HashMap<Person, Boolean> personMap;
    private static final String TAG = "AddSpendingActivity" ;
    private Group myGroup;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_spending);

        Intent intent = getIntent();
        myGroup = (Group) intent.getExtras().get(Constant.INTENT_GROUPLIST_TO_GROUPACTIVITY);
        personMap = new HashMap<>();

        APIHelper.getPersonByGroup(AddSpendingActivity.this, myGroup, new TaskComplete<Type>() {
            @Override
            public void run() {
                List<Person> personList = (List<Person>) this.result;
                Log.d(TAG, ""+personList.size());

                for (Person person: personList) {
                    personMap.put(person, false);
                    createItemParticipant(person);
                }
            }
        });
    }

    public static void show(Context context, Group group) {
        Intent intent = new Intent(context, AddSpendingActivity.class);
        intent.putExtra(Constant.INTENT_GROUPLIST_TO_GROUPACTIVITY,group);
        context.startActivity(intent);
    }

    public void addNewSpending(View view) {
        EditText editNewSpendingName = (EditText) findViewById(R.id.new_spending_name);
        EditText editNewSpendingCost = (EditText) findViewById(R.id.new_spending_cost);
        EditText editNewSpendingPayer = (EditText) findViewById(R.id.new_spending_payer);

        if (editNewSpendingName != null && editNewSpendingCost != null && editNewSpendingPayer != null) {
            if (editNewSpendingName.getText().toString().isEmpty()) {
                showError(R.id.add_spending_error_no_name, R.string.add_spending_error_no_name);
            }
            if (editNewSpendingCost.getText().toString().isEmpty()) {
                showError(R.id.add_spending_error_no_cost, R.string.add_spending_error_no_cost);
            }
            if(editNewSpendingPayer.getText().toString().isEmpty() ){
                showError(R.id.add_spending_error_no_payer, R.string.add_spending_error_no_payer);
            }
            else {

                List<Person> personAddToSpending = new ArrayList<>();

                for (Map.Entry<Person,Boolean> entry: personMap.entrySet()) {
                    Person person= entry.getKey();
                    Boolean checked = entry.getValue();

                    if(checked){
                        if(person.id == "0"){
                            //request to post a new person
                        }
                        personAddToSpending.add(person);
                    }
                }

                //request to post a spending


                finish();
            }
        }
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
                person.id = "0";
                personMap.put(person, false);

                createItemParticipant(person);
                editNewParticipantName.setText("");
            }
        }
    }

    public void createItemParticipant(final Person person){

        LayoutInflater layoutInflater = LayoutInflater.from(this);

        LinearLayout item_participant = (LinearLayout) layoutInflater.inflate(R.layout.item_participant,null);
        LinearLayout layoutParticipantsList = (LinearLayout) findViewById(R.id.linearlayout_participants_list);

        CheckBox participantCheckBox = (CheckBox) item_participant.findViewById(R.id.item_participant_checkbox);
        TextView participantsName = (TextView) item_participant.findViewById(R.id.item_participant_name);

        participantsName.setText(person.name);
        participantCheckBox.setOnCheckedChangeListener(
                new CompoundButton.OnCheckedChangeListener() {
                   @Override
                   public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                       Log.d(TAG, ""+isChecked);

                       personMap.put(person, isChecked);
                   }
               }
        );

        layoutParticipantsList.addView(item_participant);
    }
}
