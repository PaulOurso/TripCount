package project.devmob.tripcount.models;

import com.google.gson.reflect.TypeToken;

import java.io.Serializable;
import java.lang.reflect.Type;
import java.util.List;

/**
 * Created by Jean-Noel on 26/07/2016.
 */
public class Group implements Serializable {
    public String id;
    public String name;
    public String token;
    public String create_date;
    public List<Spending> spendings;

    public double getTotalSpendings() {
        double total = 0.0;
        if (spendings == null)
            return total;
        else {
            for (Spending spending: spendings) {
                total += spending.price;
            }
        }
        return total;
    }

    public static Type typeObjectOf() {
        return new TypeToken<Group>() {
        }.getType();
    }

    public static Type typeListOf() {
        return new TypeToken<List<Group>>() {
        }.getType();
    }
}
