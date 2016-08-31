package project.devmob.tripcount.models;

import com.google.gson.reflect.TypeToken;

import java.lang.reflect.Type;
import java.util.List;

/**
 * Created by Jean-Noel on 26/07/2016.
 */
public class Person {
    public String id;
    public String name;

    public static Type typeObjectOf() {
        return new TypeToken<Person>() {
        }.getType();
    }

    public static Type typeListOf() {
        return new TypeToken<List<Person>>() {
        }.getType();
    }
}
