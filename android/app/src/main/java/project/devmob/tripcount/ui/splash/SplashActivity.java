package project.devmob.tripcount.ui.splash;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import java.lang.reflect.Type;
import java.util.List;
import java.util.Timer;
import java.util.TimerTask;

import project.devmob.tripcount.R;
import project.devmob.tripcount.models.Account;
import project.devmob.tripcount.ui.grouplist.GroupListActivity;
import project.devmob.tripcount.ui.login.LoginActivity;
import project.devmob.tripcount.utils.Constant;
import project.devmob.tripcount.utils.Preference;
import project.devmob.tripcount.utils.requests.APIHelper;
import project.devmob.tripcount.utils.requests.TaskComplete;

public class SplashActivity extends AppCompatActivity {
    private Timer timer;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);
        // Default go on LoginActivity
        timer = new Timer();
        timer.schedule(new TimerTask() {
            @Override
            public void run(){
                LoginActivity.show(SplashActivity.this);
            }
        }, Constant.SPLASH_TIME);

        // Check if has acccount & connected
        Account account = Preference.getAccount(SplashActivity.this);
        if (account != null) {
            APIHelper.getMyAccounts(SplashActivity.this, account.access_token, new TaskComplete<Type>() {
                @Override
                public void run() {
                    List<Account> list = (List<Account>) result;
                    if (list != null && list.size() > 0) {
                        Account acc = list.get(0);
                        Preference.setAccount(SplashActivity.this, acc);
                        stopTimer();
                        timer = new Timer();
                        timer.schedule(new TimerTask() {
                            @Override
                            public void run() {
                                GroupListActivity.show(SplashActivity.this);
                            }
                        }, Constant.SPLASH_TIME);
                    }
                }
            });
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
    }

    @Override
    protected void onPause() {
        super.onPause();
        stopTimer();
    }

    private void stopTimer() {
        if(timer != null) {
            timer.cancel();
            timer = null;
        }
    }
}
