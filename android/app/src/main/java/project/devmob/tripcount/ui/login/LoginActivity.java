package project.devmob.tripcount.ui.login;

import android.content.Context;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

import com.google.android.gms.auth.api.Auth;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.auth.api.signin.GoogleSignInResult;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.SignInButton;
import com.google.android.gms.common.api.GoogleApiClient;

import java.lang.reflect.Type;
import java.util.List;

import project.devmob.tripcount.R;
import project.devmob.tripcount.models.Account;
import project.devmob.tripcount.ui.grouplist.GroupeListActivity;
import project.devmob.tripcount.utils.Constant;
import project.devmob.tripcount.utils.Preference;
import project.devmob.tripcount.utils.requests.APIHelper;
import project.devmob.tripcount.utils.requests.TaskComplete;

public class LoginActivity extends AppCompatActivity implements GoogleApiClient.OnConnectionFailedListener {

    private static final String TAG = "LoginActivity" ;
    GoogleApiClient mGoogleApiClient;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        // Configure sign-in to request the user's ID, email address, and basic
        // profile. ID and basic profile are included in DEFAULT_SIGN_IN.
        GoogleSignInOptions gso = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                .requestEmail()
                .build();

        // Build a GoogleApiClient with access to the Google Sign-In API and the
        // options specified by gso.
        mGoogleApiClient = new GoogleApiClient.Builder(this)
                .enableAutoManage(LoginActivity.this, this)
                .addApi(Auth.GOOGLE_SIGN_IN_API, gso)
                .build();

        SignInButton button = (SignInButton) findViewById(R.id.sign_in_button);
        if (button != null) {
            button.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    signIn();
                }
            });
        }
    }

    public void signIn() {
        Log.d(TAG, "signIn clicked");
        Intent signInIntent = Auth.GoogleSignInApi.getSignInIntent(mGoogleApiClient);
        startActivityForResult(signInIntent, Constant.RC_SIGN_IN);
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        // Result returned from launching the Intent from GoogleSignInApi.getSignInIntent(...);
        if (requestCode == Constant.RC_SIGN_IN) {
            GoogleSignInResult result = Auth.GoogleSignInApi.getSignInResultFromIntent(data);
            handleSignInResult(result);
        }
    }

    private void handleSignInResult(GoogleSignInResult result) {
        Log.d(TAG, "handleSiFgnInResult:" + result.isSuccess());
        if (result.isSuccess()) {
            // Signed in successfully, show authenticated UI.
            final GoogleSignInAccount acct = result.getSignInAccount();

            APIHelper.getMyAccounts(LoginActivity.this, "tp"+acct.getId(), new TaskComplete<Type>() {
                @Override
                public void run() {
                    List<Account> list = (List<Account>) result;
                    // if user has account
                    if (list != null && list.size() > 0) {
                        Account myAcc = list.get(0);
                        Preference.setAccount(LoginActivity.this, myAcc);
                        next();
                    }
                    else {
                        // Create account
                        Account account = new Account();
                        account.mail = acct.getEmail();
                        account.access_token = "tp"+acct.getId();
                        APIHelper.createAccount(LoginActivity.this, account, new TaskComplete<Type>() {
                            @Override
                            public void run() {
                                Account resultAccount = (Account) result;
                                Preference.setAccount(LoginActivity.this, resultAccount);
                                if (resultAccount != null) {
                                    next();
                                }
                            }
                        });
                    }
                }
            });
        } else {
            //TODO:montrer une erreur d'identification
            Toast.makeText(LoginActivity.this, R.string.login_toast_error_identification, Toast.LENGTH_LONG).show();
        }
    }

    public void next() {
        GroupeListActivity.show(LoginActivity.this);
        finish();
    }

    public static void show(Context context){
        context.startActivity(new Intent(context, LoginActivity.class));
    }

    @Override
    public void onConnectionFailed(ConnectionResult connectionResult) {
        Toast.makeText(LoginActivity.this, R.string.login_toast_error_identification, Toast.LENGTH_LONG).show();
    }
}
