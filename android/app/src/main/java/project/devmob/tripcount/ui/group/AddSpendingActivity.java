package project.devmob.tripcount.ui.group;

import android.content.Context;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;
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

        if (editNewSpendingName != null && editNewSpendingCost != null && editNewSpendingPayer != null) {
            if (editNewSpendingName.getText().toString().isEmpty()) {
                showError(R.id.add_spending_error_no_name, R.string.add_spending_error_no_name);
            }
            if (editNewSpendingCost.getText().toString().isEmpty()) {
                showError(R.id.add_spending_error_no_cost, R.string.add_spending_error_no_cost);
            }
            if(editNewSpendingPayer.getText().toString().isEmpty() ){
                showError(R.id.add_spending_error_no_payer, R.string.add_spending_error_no_payer);
            }
            else {

                finish();
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
