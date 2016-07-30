package project.devmob.tripcount.utils;

import android.accounts.Account;
import android.content.Context;

import com.android.volley.Request;
import com.google.gson.reflect.TypeToken;

import java.lang.reflect.Type;
import java.util.List;

/**
 * Created by MicroStop on 29/07/2016.
 */
public class APIHelper {
    public static final String DOMAIN = "http://0.0.0.0:3000/api/";
    public static final String URL_ACCOUNTS = DOMAIN + "accounts";

    public static void getMyAccount(Context c, String access_token, Type typeAnswer, TaskComplete<Type> taskComplete) {
        //Type listType = new TypeToken<List<Account>>(){}.getType();
        NetworkVolley<Type> networkVolley = new NetworkVolley<>(c, typeAnswer, taskComplete);
        networkVolley.setMethod(Request.Method.GET);
        networkVolley.execute(URL_ACCOUNTS);
    }
}
