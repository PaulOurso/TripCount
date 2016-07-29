package project.devmob.tripcount.ui.grouplist;

import android.content.Context;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;

import java.util.ArrayList;
import java.util.List;

import project.devmob.tripcount.R;
import project.devmob.tripcount.models.Group;

public class GroupeListActivity extends AppCompatActivity {

    private List<Group> groupList;
    private ListView listViewGroup;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_group_list);

        listViewGroup = (ListView) findViewById(R.id.listing_group);
        groupList = new ArrayList<>();

        listViewGroup.setAdapter(new AdapterGroupList(GroupeListActivity.this, R.layout.item_group, groupList));

        listViewGroup.setOnItemClickListener(new AdapterView.OnItemClickListener() {

            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Group groupSelected = (Group) listViewGroup.getAdapter().getItem(position);


            }
        });
    }

    public static void show(Context context){
        context.startActivity(new Intent(context, GroupeListActivity.class));
    }

    public void addGroup(View view) {
    }
}
