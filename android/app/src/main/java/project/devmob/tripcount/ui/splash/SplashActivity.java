package project.devmob.tripcount.ui.splash;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import java.util.Timer;
import java.util.TimerTask;

import project.devmob.tripcount.R;
import project.devmob.tripcount.ui.login.LoginActivity;
import project.devmob.tripcount.utils.Constant;

public class SplashActivity extends AppCompatActivity {
    private Timer timer;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);

        timer = new Timer();
        timer.schedule(new TimerTask() {
            @Override
            public void run(){
                LoginActivity.show(SplashActivity.this);
            }
        }, Constant.SPLASH_TIME);
    }

    @Override
    protected void onPause() {
        super.onPause();
        if(timer != null)
            timer.cancel();
    }
}
