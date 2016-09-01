package project.devmob.tripcount.ui.grouplist;

import android.content.Context;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import java.lang.reflect.Type;
import java.util.Calendar;
import java.util.List;

import project.devmob.tripcount.R;
import project.devmob.tripcount.models.Account;
import project.devmob.tripcount.models.Group;
import project.devmob.tripcount.utils.Preference;
import project.devmob.tripcount.utils.helpers.FormatHelper;
import project.devmob.tripcount.utils.helpers.GroupHelper;
import project.devmob.tripcount.utils.requests.APIHelper;
import project.devmob.tripcount.utils.requests.TaskComplete;

public class AddGroupActivity extends AppCompatActivity {

    private static final String TAG = "AddGroupActivity" ;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_group);
    }

    public static void show(Context context) {
        context.startActivity(new Intent(context, AddGroupActivity.class));
    }

    public void addNewGroup(View view) {
        EditText editNewGroupName = (EditText) findViewById(R.id.new_group_name);
        if (editNewGroupName != null) {
            if(editNewGroupName.getText().toString().isEmpty()){
                showError(R.id.error_new_group, R.string.add_group_toast_alert_no_name);
            }
            else {
                Group group = new Group();
                group.name = editNewGroupName.getText().toString();
                group.token = GroupHelper.generateToken();
                group.create_date = FormatHelper.formatCalToString(Calendar.getInstance());
                Account account = Preference.getAccount(AddGroupActivity.this);
                APIHelper.createGroup(AddGroupActivity.this, account, group, new TaskComplete<Type>() {
                    @Override
                    public void run() {
                        Toast.makeText(AddGroupActivity.this, R.string.add_group_success, Toast.LENGTH_LONG).show();
                        finish();
                    }
                });
            }
        }
    }

    public void joinGroup(View view) {
        EditText editToken = (EditText) findViewById(R.id.add_token_name);
        if (editToken != null) {
            if(editToken.getText().toString().isEmpty()){
                showError(R.id.error_add_group_token, R.string.add_group_toast_alert_no_token);
            }
            else {
                Log.d(TAG, GroupHelper.generateToken());
                String token = editToken.getText().toString();
                APIHelper.findGroupWithToken(AddGroupActivity.this, token, new TaskComplete<Type>() {
                    @Override
                    public void run() {
                        if (this.result == null) {
                            showError(R.id.error_add_group_token, R.string.group_not_found);
                        }
                        else {
                            List<Group> listGroups = (List<Group>) this.result;
                            if (listGroups != null && listGroups.size() > 0) {
                                Group group = listGroups.get(0);
                                Account account = Preference.getAccount(AddGroupActivity.this);
                                APIHelper.joinGroup(AddGroupActivity.this, account, group, new TaskComplete<Type>() {
                                    @Override
                                    public void run() {
                                        Toast.makeText(AddGroupActivity.this, R.string.add_group_success, Toast.LENGTH_LONG).show();
                                        finish();
                                    }
                                });
                            }
                            else
                                showError(R.id.error_add_group_token, R.string.group_not_found);
                        }
                    }
                });
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
}
