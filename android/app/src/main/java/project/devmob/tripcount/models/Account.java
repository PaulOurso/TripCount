package project.devmob.tripcount.models;

import com.google.gson.reflect.TypeToken;

import java.lang.reflect.Type;
import java.util.List;

/**
 * Created by Jean-Noel on 28/07/2016.
 */
public class Account {
    public String id;
    public String mail;
    public String access_token;

    public static Type typeObjectOf() {
        return new TypeToken<Account>() {
        }.getType();
    }

    public static Type typeListOf() {
        return new TypeToken<List<Account>>() {
        }.getType();
    }
}
