package project.devmob.tripcount.ui.grouplist;

import android.content.Context;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.LinearLayout;
import android.widget.ListView;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;

import project.devmob.tripcount.R;
import project.devmob.tripcount.models.Account;
import project.devmob.tripcount.models.Group;
import project.devmob.tripcount.ui.group.GroupActivity;
import project.devmob.tripcount.utils.Constant;
import project.devmob.tripcount.utils.Preference;
import project.devmob.tripcount.utils.requests.APIHelper;
import project.devmob.tripcount.utils.requests.TaskComplete;

public class GroupListActivity extends AppCompatActivity {

    private List<Group> groupList;
    private ListView listViewGroup;
    private LinearLayout linearLayoutNothing;

    private static final String TAG = "GroupListActivity" ;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_group_list);

        linearLayoutNothing = (LinearLayout) findViewById(R.id.linearlayout_nothing_in_group);
        listViewGroup = (ListView) findViewById(R.id.listing_group);
        groupList = new ArrayList<>();
    }

    @Override
    protected void onResume() {
        super.onResume();
        Account account = Preference.getAccount(GroupListActivity.this);
        APIHelper.getMyGroups(GroupListActivity.this, account, new TaskComplete<Type>() {
            @Override
            public void run() {
                groupList = (List<Group>) this.result;
                if (groupList != null && groupList.size() > 0) {
                    linearLayoutNothing.setVisibility(View.GONE);
                    listViewGroup.setVisibility(View.VISIBLE);
                }
                listViewGroup.setAdapter(new AdapterGroupList(GroupListActivity.this, R.layout.item_group, groupList));
                Log.d(TAG, ""+groupList.size());
            }
        });
        listViewGroup.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Group groupSelected = (Group) listViewGroup.getAdapter().getItem(position);
                GroupActivity.show(GroupListActivity.this, groupSelected);
            }
        });
    }

    public static void show(Context context){
        context.startActivity(new Intent(context, GroupListActivity.class));
    }

    public void addGroup(View view) {
        AddGroupActivity.show(GroupListActivity.this);
    }
}
