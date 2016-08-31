package project.devmob.tripcount.models;

import com.google.gson.reflect.TypeToken;

import java.lang.reflect.Type;
import java.util.List;

/**
 * Created by Jean-Noel on 26/07/2016.
 */
public class Group {
    public String id;
    public String name;
    public String token;
    public String create_date;

    public static Type typeObjectOf() {
        return new TypeToken<Group>() {
        }.getType();
    }

    public static Type typeListOf() {
        return new TypeToken<List<Group>>() {
        }.getType();
    }
}
