package project.devmob.tripcount.ui.groupe;

import android.content.Context;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;

import java.util.List;

import project.devmob.tripcount.R;
import project.devmob.tripcount.models.Group;

public class GroupeActivity extends AppCompatActivity {

    private List<Group> groupList;
    private ListView listViewGroup;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_groupe);

        listViewGroup = (ListView) findViewById(R.id.listing_group);

        listViewGroup.setAdapter(new AdapterGroup(GroupeActivity.this, R.layout.item_groupe, groupList));

        listViewGroup.setOnItemClickListener(new AdapterView.OnItemClickListener() {

            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

    }

    public static void show(Context context){
        context.startActivity(new Intent(context, GroupeActivity.class));
    }
}
