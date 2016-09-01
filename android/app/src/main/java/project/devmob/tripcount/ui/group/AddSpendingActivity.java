package project.devmob.tripcount.ui.group;

import android.content.Context;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import project.devmob.tripcount.R;

public class AddSpendingActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_spending);
    }

    public static void show(Context context) {
        context.startActivity(new Intent(context, AddSpendingActivity.class));
    }

    public void addNewSpending(View view) {
        EditText editNewSpendingName = (EditText) findViewById(R.id.new_spending_name);
        EditText editNewSpendingCost = (EditText) findViewById(R.id.new_spending_cost);
        EditText editNewSpendingPayer = (EditText) findViewById(R.id.new_spending_payer);

        if(editNewSpendingName.getText().toString().isEmpty() ){
            Toast.makeText(AddSpendingActivity.this, R.string.add_spending_toast_alert_no_name,Toast.LENGTH_LONG).show();
        }
        if(editNewSpendingCost.getText().toString().isEmpty() ){
            Toast.makeText(AddSpendingActivity.this, R.string.add_spending_toast_alert_no_cost,Toast.LENGTH_LONG).show();
        }
        if(editNewSpendingPayer.getText().toString().isEmpty() ){
            Toast.makeText(AddSpendingActivity.this, R.string.add_spending_toast_alert_no_payer,Toast.LENGTH_LONG).show();
        }
        else {
            finish();
        }
    }
}
