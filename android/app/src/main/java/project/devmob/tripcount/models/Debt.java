package project.devmob.tripcount.models;

import com.google.gson.reflect.TypeToken;

import java.io.Serializable;
import java.lang.reflect.Type;
import java.util.List;

/**
 * Created by Jean-Noel on 28/07/2016.
 */
public class Debt implements Serializable {
    public String id;
    public boolean refunded;

    public static Type typeObjectOf() {
        return new TypeToken<Debt>() {
        }.getType();
    }

    public static Type typeListOf() {
        return new TypeToken<List<Debt>>() {
        }.getType();
    }
}
