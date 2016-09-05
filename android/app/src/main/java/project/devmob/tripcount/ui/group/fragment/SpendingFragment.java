package project.devmob.tripcount.ui.group.fragment;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ListView;

import java.lang.reflect.Type;
import java.util.List;

import project.devmob.tripcount.R;
import project.devmob.tripcount.models.Group;
import project.devmob.tripcount.models.Spending;
import project.devmob.tripcount.ui.group.AdapterSpendingList;
import project.devmob.tripcount.utils.requests.APIHelper;
import project.devmob.tripcount.utils.requests.TaskComplete;

public class SpendingFragment extends Fragment {

    private Group myGroup;
    private List<Spending> spendingList;
    private ListView listViewSpending;

    private static final String TAG = "SpendingFrament" ;

    public SpendingFragment() {
        // Required empty public constructor
    }

        // TODO: Rename and change types and number of parameters
    public static SpendingFragment newInstance(Group group) {
        SpendingFragment fragment = new SpendingFragment();
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

        APIHelper.getSpendingsByGroupId(getContext(), myGroup, new TaskComplete<Type>() {

            @Override
            public void run() {
                spendingList= (List<Spending>) this.result;
                listViewSpending.setAdapter(new AdapterSpendingList(getContext(), R.layout.item_spending, spendingList));
                Log.d(TAG, ""+spendingList.size());
            }
        });
        listViewSpending.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Spending spendingSelected = (Spending) listViewSpending.getAdapter().getItem(position);
                /*Intent intent = new Intent(getContext(), DetailSpendingActivity.class);
                intent.putExtra(Constant.INTENT_GROUPLIST_TO_GROUPACTIVITY, spendingSelected);
                startActivity(intent);*/
            }
        });
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view =inflater.inflate(R.layout.fragment_spending, container, false);
        listViewSpending = (ListView) view.findViewById(R.id.listing_spending);

        return view;
    }


}
