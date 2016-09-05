package project.devmob.tripcount.ui.group.fragment;


import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.LinearLayout;
import android.widget.TextView;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;

import project.devmob.tripcount.R;
import project.devmob.tripcount.models.Group;
import project.devmob.tripcount.models.Person;
import project.devmob.tripcount.models.Spending;
import project.devmob.tripcount.ui.group.spending.AdapterSpendingList;
import project.devmob.tripcount.ui.group.spending.DetailSpendingActivity;
import project.devmob.tripcount.utils.Constant;
import project.devmob.tripcount.utils.requests.APIHelper;
import project.devmob.tripcount.utils.requests.TaskComplete;

/**
 * A simple {@link Fragment} subclass.
 */
public class BilanFragment extends Fragment {

    private static final String TAG = "BilanFragment" ;

    private Group myGroup;
    private List<Person> personList;

    public BilanFragment() {
        // Required empty public constructor
    }

    public static BilanFragment newInstance(Group group) {
        BilanFragment fragment = new BilanFragment();
        fragment.myGroup = group;
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public void onResume() {
        super.onResume();

        APIHelper.getPersonByGroup(getContext(), myGroup, new TaskComplete<Type>() {
            @Override
            public void run() {


                personList = (List<Person>) this.result;
                Log.d(TAG, ""+personList.size());

                for (Person person: personList) {
                    createItemBilan(person);

                }
            }
        });
    }

    private void createItemBilan(Person person) {
        LayoutInflater layoutInflater = LayoutInflater.from(getContext());
        LinearLayout item_bilan= (LinearLayout) layoutInflater.inflate(R.layout.item_bilan,null);
        TextView bilanPersonName = (TextView) item_bilan.findViewById(R.id.item_bilan_person_name);
        TextView bilanTotal = (TextView) item_bilan.findViewById(R.id.item_bilan_total);
        bilanPersonName.setText(person.name);

        //TODO: requet sur la person pour prendre toutes ses dependances positive ou negative

        /*APIHelper.getSpendingByIndepted(getContext(), person, new TaskComplete<Type>() {
            @Override
            public void run() {

            }
        });*/

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_bilan, container, false);
    }

}
