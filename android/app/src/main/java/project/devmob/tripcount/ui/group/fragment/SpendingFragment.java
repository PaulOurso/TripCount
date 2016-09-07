package project.devmob.tripcount.ui.group.fragment;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.widget.SwipeRefreshLayout;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.LinearLayout;
import android.widget.ListView;

import java.lang.reflect.Type;
import java.util.List;

import project.devmob.tripcount.R;
import project.devmob.tripcount.models.Group;
import project.devmob.tripcount.models.Spending;
import project.devmob.tripcount.ui.group.spending.AdapterSpendingList;
import project.devmob.tripcount.ui.group.spending.DetailSpendingActivity;
import project.devmob.tripcount.utils.Constant;
import project.devmob.tripcount.utils.requests.APIHelper;
import project.devmob.tripcount.utils.requests.TaskComplete;

public class SpendingFragment extends Fragment {

    private Group myGroup;
    private List<Spending> spendingList;
    private ListView listViewSpending;

    private static final String TAG = "SpendingFrament" ;
    private LinearLayout linearLayoutNothingSpending;
    private SwipeRefreshLayout swipeContainer;

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
        refreshData();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view =inflater.inflate(R.layout.fragment_spending, container, false);
        listViewSpending = (ListView) view.findViewById(R.id.listing_spending);

        linearLayoutNothingSpending = (LinearLayout) view.findViewById(R.id.linearlayout_nothing_in_spending);

        swipeContainer = (SwipeRefreshLayout) view.findViewById(R.id.swipeContainer);
        swipeContainer.setColorSchemeResources(R.color.colorPrimary);

        swipeContainer.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                refreshData();
            }
        });

        return view;
    }

    public void refreshData() {
        APIHelper.getSpendingsByGroupId(getContext(), myGroup, new TaskComplete<Type>() {

            @Override
            public void run() {
                spendingList= (List<Spending>) this.result;
                listViewSpending.setAdapter(new AdapterSpendingList(getContext(), R.layout.item_spending, spendingList));
                if (spendingList != null && spendingList.size() > 0) {
                    linearLayoutNothingSpending.setVisibility(View.GONE);
                    listViewSpending.setVisibility(View.VISIBLE);
                }
                swipeContainer.setRefreshing(false);
                Log.d(TAG, ""+spendingList.size());
            }
        });
        listViewSpending.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Spending spendingSelected = (Spending) listViewSpending.getAdapter().getItem(position);
                Intent intent = new Intent(getContext(), DetailSpendingActivity.class);
                intent.putExtra(Constant.INTENT_SPENDING_ID, spendingSelected.id);
                startActivity(intent);
            }
        });
    }

}
