package project.devmob.tripcount.ui.groupe;

import android.content.Context;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import java.util.List;

import project.devmob.tripcount.R;
import project.devmob.tripcount.models.Group;

public class GroupeActivity extends AppCompatActivity {

    private List<Group> groupList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_groupe);
    }

    public static void show(Context context){
        context.startActivity(new Intent(context, GroupeActivity.class));
    }
}
