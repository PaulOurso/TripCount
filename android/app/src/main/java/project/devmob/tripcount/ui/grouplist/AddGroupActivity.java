package project.devmob.tripcount.ui.grouplist;

import android.content.Context;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import project.devmob.tripcount.R;

public class AddGroupActivity extends AppCompatActivity {


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_group);


        /*TODO: load all groups of the account*/
    }

    public static void show(Context context) {
        context.startActivity(new Intent(context, AddGroupActivity.class));
    }

    public void addNewGroup(View view) {
        EditText editNewGroupName = (EditText) findViewById(R.id.new_group_name);

        if(editNewGroupName.getText().toString().isEmpty() ){
            Toast.makeText(AddGroupActivity.this, R.string.add_group_toast_alert_no_name,Toast.LENGTH_LONG).show();
        }
        else {
            finish();
        }
    }
}
