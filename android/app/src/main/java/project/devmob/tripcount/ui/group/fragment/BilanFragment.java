package project.devmob.tripcount.ui.group.fragment;


import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.widget.SwipeRefreshLayout;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.ScrollView;
import android.widget.TextView;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import project.devmob.tripcount.R;
import project.devmob.tripcount.models.Group;
import project.devmob.tripcount.models.Person;
import project.devmob.tripcount.models.Spending;
import project.devmob.tripcount.utils.helpers.FormatHelper;
import project.devmob.tripcount.utils.requests.APIHelper;
import project.devmob.tripcount.utils.requests.TaskComplete;

/**
 * A simple {@link Fragment} subclass.
 */
public class BilanFragment extends Fragment {

    private static final String TAG = "BilanFragment" ;

    private Group myGroup;
    private List<Person> personList;
    private Map<String,Integer> spendingCount;
    private LinearLayout bilanLayout;
    private LinearLayout refundingLayout;
    private LinearLayout linearLayoutBilanAndRefunding;
    private SwipeRefreshLayout swipeContainer;

    private List<Refunding> purchasersRefundList;
    private List<Refunding> indebtedRefundList;

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
        spendingCount = new HashMap<>();
        purchasersRefundList = new ArrayList<>();
        indebtedRefundList = new ArrayList<>();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_bilan, container, false);
        bilanLayout = (LinearLayout) view.findViewById(R.id.linearlayout_bilan_list);
        refundingLayout = (LinearLayout) view.findViewById(R.id.linearlayout_refunding_list);
        linearLayoutBilanAndRefunding = (LinearLayout) view.findViewById(R.id.linearlayout_bilan_and_refunding);

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

    @Override
    public void onResume() {
        super.onResume();
        refreshData();
    }

    private void refreshData() {
        spendingCount.clear();
        APIHelper.getPersonAndSpendingByGroup(getContext(), myGroup, new TaskComplete<Type>() {
            @Override
            public void run() {

                personList = (List<Person>) this.result;
                Log.d(TAG, "" + personList.size());
                bilanLayout.removeAllViews();
                refundingLayout.removeAllViews();
                if (personList != null && personList.size() > 0) {
                    linearLayoutBilanAndRefunding.setVisibility(View.VISIBLE);
                    calculBilan(personList);
                    calculRefund();
                }
                swipeContainer.setRefreshing(false);
            }
        });
    }

    private static class Refunding implements Comparable<Refunding> {
        Person person;
        double money;

        public Refunding(Person _person, double _money) {
            person = _person;
            money = _money;
        }

        @Override
        public int compareTo(Refunding refunding) {
            int compare;
            double diff = refunding.money - money;

            if (diff == 0)
                compare = 0;
            else if (diff > 0)
                compare = 1;
            else
                compare = -1;

            return compare;
        }
    }


    private void calculBilan(List<Person> persons){

        for (Person person: persons){
            if(person.indebted != null) {
                for (Spending spendingIndepted : person.indebted) {
                    checkSpendingMap(spendingIndepted);
                }
            }
        }
        purchasersRefundList.clear();
        indebtedRefundList.clear();
        for (Person person: persons){
            double bilanValue = 0;

            if(person.indebted != null) {
                for (Spending spendingIndepted : person.indebted) {
                    //divide the initial price by the number of indebted person for this spending
                    bilanValue = bilanValue - (spendingIndepted.price/(double) spendingCount.get(spendingIndepted.id));
                }
            }
            if(person.purchaser != null) {
                for (Spending spendingPurchaser : person.purchaser) {
                    bilanValue = bilanValue + spendingPurchaser.price-(spendingPurchaser.price/(double) spendingCount.get(spendingPurchaser.id));
                }
            }

            Refunding refunding = new Refunding(person, Math.abs(bilanValue));
            if (bilanValue > 0)
                purchasersRefundList.add(refunding);
            else if (bilanValue < 0)
                indebtedRefundList.add(refunding);

            createItemBilan(person, bilanValue);
        }
    }

    //method to check how many person are link to a spending
    private void checkSpendingMap(Spending spendingIndepted) {

        if(spendingCount.containsKey(spendingIndepted.id)){
            int mapValue = spendingCount.get(spendingIndepted.id);
            spendingCount.put(spendingIndepted.id,mapValue+1);
        }
        else{
            spendingCount.put(spendingIndepted.id,2);
        }
    }

    private void createItemBilan(Person person, double bilanValue) {
        LayoutInflater layoutInflater = LayoutInflater.from(getContext());
        bilanValue = FormatHelper.formatPrice(bilanValue);

        LinearLayout item_bilan= (LinearLayout) layoutInflater.inflate(R.layout.item_bilan,null);
        TextView bilanPersonName = (TextView) item_bilan.findViewById(R.id.item_bilan_person_name);
        TextView bilanTotal = (TextView) item_bilan.findViewById(R.id.item_bilan_total);

        if(bilanValue > 0){
            bilanTotal.setTextColor(getContext().getResources().getColor(R.color.green));
        }
        else if(bilanValue < 0){
            bilanTotal.setTextColor(getContext().getResources().getColor(R.color.red));
        }

        bilanPersonName.setText(person.name);
        bilanTotal.setText(String.format(getContext().getString(R.string.currency),String.valueOf(bilanValue)));

        bilanLayout.addView(item_bilan);
    }


    // Remboursement
    public void calculRefund() {
        double purchaserValue;
        double indebtedValue;
        Collections.sort(purchasersRefundList);
        Collections.sort(indebtedRefundList);
        for (Refunding refundingPurchaser: purchasersRefundList) {
            Person purchaserPerson = refundingPurchaser.person;
            purchaserValue = refundingPurchaser.money;

            for (Refunding refundingIndebted: indebtedRefundList) {
                final Person indebtedPerson = refundingIndebted.person;
                if (refundingIndebted.money != 0) {
                    indebtedValue = refundingIndebted.money;
                    if (purchaserValue > indebtedValue) {
                        purchaserValue = purchaserValue - indebtedValue;
                        refundingIndebted.money = 0;
                        createItemRefund(refundingIndebted.person, refundingPurchaser.person, indebtedValue);
                    }
                    else {
                        indebtedValue = indebtedValue - purchaserValue;
                        refundingPurchaser.money = 0;
                        refundingIndebted.money = indebtedValue;
                        createItemRefund(refundingIndebted.person, refundingPurchaser.person, purchaserValue);
                        break;
                    }
                }
            }
        }
    }

    public void createItemRefund(Person indebted, Person purchaser, double refund) {
        LayoutInflater layoutInflater = LayoutInflater.from(getContext());
        LinearLayout itemRefunding = (LinearLayout) layoutInflater.inflate(R.layout.item_bilan_refunding, null);
        TextView refundingTextviewIndebted = (TextView) itemRefunding.findViewById(R.id.item_refunding_indebted);
        TextView refundingTextviewMoney = (TextView) itemRefunding.findViewById(R.id.item_refunding_money);
        TextView refundingTextviewPurchaser = (TextView) itemRefunding.findViewById(R.id.item_refunding_purchaser);

        String priceRefund = String.format(getString(R.string.currency), FormatHelper.formatPrice(refund));

        refundingTextviewIndebted.setText(indebted.name);
        refundingTextviewMoney.setText(priceRefund);
        refundingTextviewPurchaser.setText(purchaser.name);

        refundingLayout.addView(itemRefunding);
    }
}
